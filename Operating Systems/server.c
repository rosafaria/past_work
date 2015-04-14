/*
 * Autores:
 * Rosa Manuela Rodrigues de Faria (nº 2005128014)
 * Vasco Bruno dos Santos Gouveia (nº 2010106666)
 * 
*/

#include "header.h"

#define SERVER "TO_SERVER"
#define N_PIPES 3
#define N_THREADS 4

#define MAX_BUFFER 500
#define MAX_PATH 512

#define DEBUG
#define DEBUGEXIT
//#define DEBUGSYNC

pid_t processes[3];
int pipein[N_PIPES+1][2];
int pipeout[N_PIPES][2];
pthread_t threads[N_THREADS];

sigset_t unblock_ctrlc; //nao bloqueia SIGINT - server
sigset_t unblock_sigusr1; //nao bloqueia SIGUSR1 - threads
sigset_t unblock_sigusr2; //nao bloqueia SIGUSR2 - processos
sigset_t block_all; //bloqueia tudo - enquanto ha rotacao

int sair = 1;
msg client_msg;
mem_struct *buf;

int get_stat(int fdin){
	struct stat pstatbuf;	
	if (fstat(fdin, &pstatbuf) < 0)	{
		fprintf(stderr,"fstat error\n");
		exit(1);
	}
	return (int) pstatbuf.st_size;
}

//Cleanup
void cleanup() {
	int i;
	for (i=0; i<N_PIPES;i++) {
		close(pipein[i][0]);
		close(pipein[i][1]);
		close(pipeout[i][0]);
		close(pipeout[i][1]);
	}
}

void cleanup_proc() {
	bufferPtr delete;
	int i, id=buf->rot/90-1;
	
	#ifdef DEBUGEXIT
		printf("Process %i exiting.\n",getpid());
	#endif
	
	///***** Join das threads *****///
	for (i=0; i<N_THREADS; i++) {
		if (pthread_join (threads[i], NULL)!=0)
			fprintf(stderr, "Failed to join thread %i.\n", i);
	}
	#ifdef DEBUGEXIT
		printf("Process %i joined threads.\n",id);
	#endif
	///***** Fim do join das threads *****///

	
	///***** Limpeza dos semáforos *****///
	sem_close(&buf->sem_empty);
	sem_close(&buf->sem_full);
	if (pthread_mutex_destroy(&buf->mutex)<0)
		fprintf(stderr, "mutex destruction failed.\n");
	
	#ifdef DEBUGEXIT
		printf("Process %i closed semaphores.\n",getpid()); 
	#endif
	///***** Fim da limpeza dos semáforos *****///


	///***** Limpeza do buffer *****///
	while(buf->bufferStart->next!=NULL) {
		delete = buf->bufferStart;
		kill (buf->bufferStart->message.clientPid, SIGUSR2);
		buf->bufferStart = buf->bufferStart->next;
		free(delete);
	}
	
	free(buf->bufferStart);
	free(buf);
	
	#ifdef DEBUGEXIT
		printf("Process %i cleaned buffer\n", getpid());
	#endif
	///***** Fim da limpeza do buffer *****///
	
	
	///***** Envio de mensagem de saida *****///
	client_msg.rotation = -2;
	client_msg.clientPid = getpid();
	write(pipein[id][1],&client_msg,sizeof(msg));
	
	#ifdef DEBUGEXIT
		printf("Process %i sent exit signal to server\n", getpid());
	#endif
	///***** Fim do envio de mensagem de saida *****///
	
	exit(0);
}


//Tratamento de SIGINT
void sigint(int signum) {
	int i;
	if (signum == SIGINT) {
		char option[2];
		printf("\n ^C pressed. Do you want to abort? ");
		scanf("%1s", option);
		if (option[0] == 'y') {
			#ifdef DEBUGEXIT
				printf("Going to shutdown...\n");
			#endif
			sair = 0;
			for (i = 0; i<3; i++)
				if (kill (processes[i], SIGUSR2)<0)
					fprintf(stderr, "Failed to send signal SIGUSR2 to process %i.\n",processes[i]);
			#ifdef DEBUGEXIT
				else printf("killed process %i.\n",i);
			#endif
			if (sigprocmask(SIG_BLOCK, &block_all, NULL)<0)
				fprintf(stderr,"Failed to block signals from now on.\n");
		}
		return;
	}
	else if (signum == SIGUSR2) { //processos filhos
		#ifdef DEBUGEXIT
			printf("Process %i: got signal SIGUSR2.\n", getpid());
		#endif
		for (i = 0; i<N_THREADS; i++)
			if (pthread_kill(threads[i],SIGUSR1)!=0)
				fprintf(stderr, "Failed to kill thread %i on process %i.\n", i, getpid());
		#ifdef DEBUGEXIT
			printf("Process %i: sent kill signals to threads.\n", getpid());
		#endif
		sair = 0;
		cleanup_proc();
	}
	else {//worker threads
		#ifdef DEBUGEXIT
			printf("Processo %i: Worker a sair...\n", getpid());
		#endif
		pthread_exit (NULL);
	}
}


int main() {

	int i;
	
	#ifdef DEBUG
		printf("Server is up! Pid = %i.\n\n",getpid());
	#endif
	
	///***** Criacao dos unnamed pipe e processos filhos *****///
	for (i=0;i<N_PIPES;i++) {
		if (pipe(pipein[i])<0) {
			fprintf(stderr, "Pipein for process %i failed.\n",i);
			exit(0);
		}
		if (pipe(pipeout[i])<0) {
			fprintf(stderr, "Pipeout for process %i failed.\n",i);
			exit(0);
		}
		processes[i] = fork();
		if (processes[i]==0) {
			rotation(i);
			exit(0);
		}
		else if (processes[i] == -1) {
			fprintf(stderr, "Error creating process %i.\n",i);
			if (i == 0) exit(0);
			i--;
		}
	}
	///***** Fim da criacao dos unnamed pipe e processos filhos *****///
	
	#ifdef DEBUG
		printf("Pipes e processos filhos criados.\n");
	#endif
	
	///***** Criacao do named pipe *****///
	unlink(SERVER);
	if (mkfifo(SERVER, O_CREAT|O_EXCL|0666)<0) {
		fprintf(stderr, "Error in mkfifo. Exiting.\n");
		cleanup();
		exit(0);
	}
	if ((pipein[N_PIPES][0] = open(SERVER, O_RDWR))<0) { //pipein[N_PIPES][1] fica inutilizado para se poder usar o select com todos
		fprintf(stderr, "Creation of pipe TO_SERVER has failed.\n");
		cleanup();
		exit(0);
	}
	#ifdef DEBUG
		printf("Named pipe criado\n");
	#endif
	///***** Fim da criacao do named pipe *****///
	
	///***** Tratamento de sinais *****///
	sigfillset(&unblock_ctrlc);
	signal(SIGINT, sigint);
	if (sigdelset(&unblock_ctrlc, SIGINT)<0)
		fprintf(stderr, "Failed to delete SIGINT from signal block set.\n");
	if (sigprocmask (SIG_BLOCK, &unblock_ctrlc, NULL)<0)
		fprintf(stderr, "Failed to apply mask on blocked signals.\n");
	sigfillset(&block_all); //set for blocking all signals during job execution
	///***** Fim do tratamento de sinais *****///
  
	#ifdef DEBUG
		printf("Signal treatment done.\n");
	#endif
 
	fd_set read_set;
	int processterm = 0;
	
	while (processterm<3) {
		
		FD_ZERO(&read_set);
		for (i=0; i<N_PIPES+1;i++) {
			FD_SET(pipein[i][0],&read_set);
		}
		
		if (select(pipein[N_PIPES][0]+1,&read_set, NULL, NULL, NULL) > 0) {
			for (i = 0; i<N_PIPES+1; i++) {
				if (FD_ISSET(pipein[i][0],&read_set)) {
					
					if (read(pipein[i][0],&client_msg,sizeof(msg))<0)
						fprintf(stderr,"Failed to read from pipe %i.\n",i);
					
					if (i==N_PIPES) { //Pipe selecionado é o named pipe
#ifdef DEBUG
							printf("Server received message from client %i for rotation %i with message %s.\n", client_msg.clientPid, client_msg.rotation, client_msg.tempmsg);
#endif						
						if (sair==0) { //Foi mandado SIGINT
							if (kill (client_msg.clientPid,SIGUSR2)<0)
								fprintf(stderr, "Failed to send signal SIGUSR2 to client %i.\n", client_msg.clientPid); 
#ifdef DEBUG
							printf("Sent SIGUSR2 to client %i: Store is closed.\n", client_msg.clientPid);
#endif
						}
						else { //enviar pedido para processo
							if (write(pipeout[(client_msg.rotation/90)-1][1],&client_msg, sizeof(msg))<0)
								fprintf(stderr, "Failed to write to pipe %i.\n",client_msg.rotation/90-1);
#ifdef DEBUG
							printf("Server sent message from client %i to pipe %i.\n", client_msg.clientPid, (client_msg.rotation/90)-1);
#endif
						}
					}
					else {
						if (client_msg.rotation == -2) { //Processo terminou
							processterm++;
#ifdef DEBUGEXIT
							printf("Process %i terminated\n",client_msg.clientPid);
#endif
						}
						
						else if (client_msg.rotation == -1) { //Rotacao teve erros
							if (kill(client_msg.clientPid,SIGUSR2)<0)
								fprintf(stderr, "Failed to send signal SIGUSR2 to client %i.\n", client_msg.clientPid);
#ifdef DEBUG
							printf("Server received failure message from client %i for rotation %i with message %s. Sending SIGUSR2\n", client_msg.clientPid, client_msg.rotation, client_msg.tempmsg);
#endif
						}
						
						else { //Rotacao feita sem erros
							if (kill(client_msg.clientPid,SIGUSR1)<0)
								fprintf(stderr, "Failed to send signal SIGUSR1 to client %i.\n", client_msg.clientPid);
#ifdef DEBUG
							printf("Server received success message from client %i for rotation %i with message %s. Sending SIGUSR1\n", client_msg.clientPid, client_msg.rotation, client_msg.tempmsg);
#endif
						}
					}
				}
			}
		}
	}
	
	//Terminou select -> vai sair
	for (i = 0; i<3; i++) {
		if (waitpid(processes[i], NULL, WNOHANG)<0)
			fprintf(stderr, "Failed to wait for process %i.\n",processes[i]);
	}
	cleanup();
	unlink(SERVER);
	printf("Cleanup done. Server is now closed.\n");
	exit(0);
	return 1;
}

int rotation(int id){
	int rot = (id+1)*90;
	int i;
	msg client_msg;
	
	///***** Inicializacao buffer *****///
	if ((buf = (mem_struct*) malloc(sizeof(mem_struct)))==NULL) {
		fprintf(stderr, "Failed to allocate memory for buf.\n");
		exit(0);
	}
	if ((buf->bufferStart = (bufferPtr) malloc(sizeof(bufferStruct)))==NULL) {
		fprintf(stderr, "Failed to allocate memory for bufferStart.\n");
		free(buf);
		exit(0);
	}
	buf->bufferEnd = buf->bufferStart;
	if (sem_init(&buf->sem_empty, 1, MAX_BUFFER)<0) {
		fprintf(stderr, "Sem_init failed for sem_empty.\n");
		cleanup_proc();
        exit(0);
	}
	if (sem_init(&buf->sem_full, 1, 0)<0) {
		fprintf(stderr, "sem_init(sem_full) failed.\n");
		cleanup_proc();
        exit(0);
	}
	pthread_mutex_init(&buf->mutex, NULL);
	
	#ifdef DEBUG
		printf("Buffer for process %i created.\n",id);
	#endif
	///***** Fim da inicializacao buffer *****///
	
	
	///***** Criacao das threads *****///
	buf->rot = rot;
	for (i=0; i<N_THREADS; i++) {
		if (pthread_create (&threads[i], NULL, *worker, buf)!=0) {
			perror("Error creating workers!\n");
            cleanup_proc(buf);
            exit(0);
        }
	}
	///***** Fim da criacao das threads *****///
	
	///***** Tratamento de sinais *****///
	sigfillset(&unblock_sigusr2);
	signal(SIGUSR2, sigint);
	sigdelset(&unblock_sigusr2, SIGUSR2);
	sigprocmask (SIG_BLOCK, &unblock_sigusr2, NULL);
	///***** Fim do tratamento de sinais *****///

	while(sair) {
		#ifdef DEBUG
			printf("Process %i: waiting for message from server.\n", id);
		#endif

		if (read(pipeout[id][0],&client_msg,sizeof(msg))<=0) {
			fprintf(stderr, "Error reading from pipeout %i.\n",id);
			break;
		}
		
		if (sigprocmask (SIG_BLOCK, &block_all, NULL)<0) {
			fprintf(stderr, "failed to block all signals during job execution. Continuing anyway.\n"); //bloqueia todos os sinais até terminar o trabalho
		}
		
		#ifdef DEBUG
			printf("Process %i: Got work from client %i for file %s\n",id, client_msg.clientPid, client_msg.tempmsg);
		#endif
		
		if (sem_wait(&buf->sem_empty)<0) {
			fprintf(stderr, "sem_wait(sem_empty) failed.\n");
			cleanup_proc(buf);
            exit(0);
		}
		
		buf->bufferEnd->message = client_msg;
		if ((buf->bufferEnd->next = (bufferStruct*) malloc(sizeof(bufferStruct)))==NULL) {
			fprintf(stderr, "bufferEnd malloc failed.\n");
			cleanup_proc(buf);
            exit(0);
		}
		buf->bufferEnd =  buf->bufferEnd->next;
		
		#ifdef DEBUG
			printf("Process %i: message saved in buffer.\n", id);
		#endif
		
		if (sem_post(&buf->sem_full)<0) {
			fprintf(stderr, "sem_post(sem_full) failed.\n");
			cleanup_proc(buf);
            exit(0);
		}
		
		if (sigprocmask (SIG_UNBLOCK, &block_all, NULL)<0)
			fprintf(stderr, "Failed to unblock signals in worker thread.\n");
	}
	cleanup_proc();
    exit(0);
}

void* worker(void *buf_ptr) {
	mem_struct *buf =&(*((mem_struct *)buf_ptr));
	msg job;
	int fdin, size;
    void *src;
    bufferPtr delete;
	
	///***** Tratamento de sinais *****///
	sigfillset(&unblock_sigusr1);
	signal(SIGUSR1, sigint);
	if (sigdelset(&unblock_sigusr1, SIGUSR1)<0)
		fprintf(stderr, "Failed to delete SIGUSR1 from blocked signals set. Proceeding anyway.\n");
	if (sigprocmask (SIG_BLOCK, &unblock_sigusr1, NULL)<0)
		fprintf(stderr, "Failed to apply blocked signal mask. Proceeding anyway.\n");
	///***** Fim do tratamento de sinais *****///   
	
	
	#ifdef DEBUG
		printf("Worker thread for rotation %i created\n", buf->rot);
	#endif
	
	while(1) {
		
		if (sem_wait(&buf->sem_full)<0) {
			fprintf(stderr, "sem_wait(sem_full) failed\n");
			pthread_exit (NULL);
		}
		if (pthread_mutex_lock(&buf->mutex)<0) {
			fprintf(stderr, "sem_wait(sem_mutex) failed\n");
			pthread_exit(NULL);
		}
		
		if (sigprocmask (SIG_BLOCK, &block_all, NULL)<0) {
			fprintf(stderr, "failed to block all signals during job execution. Continuing anyway.\n"); //bloqueia todos os sinais até terminar o trabalho
		}
		
		#ifdef DEBUG
			printf("Process %i: worker thread retrieving job from buffer.\n", buf->rot);
		#endif
		
		job = buf->bufferStart->message;
		delete = buf->bufferStart;
		buf->bufferStart =  buf->bufferStart->next;
		free(delete);
		
		if (pthread_mutex_unlock(&buf->mutex)<0) {
			fprintf(stderr, "sem_post(sem_mutex) failed. Exiting.\n");
			pthread_exit(NULL);
		}
		if (sem_post(&buf->sem_empty)<0) {
			fprintf(stderr, "sem_post(sem_empty) failed. Exiting.\n");
			pthread_exit(NULL);
		}
		
		//fazer rotacao
		if ( (fdin = open(job.tempmsg, O_RDWR)) < 0)	{
			fprintf(stderr,"can't open %s for reading\n", job.tempmsg);
			job.rotation = -1;
			write(pipein[(buf->rot/90)-1][1],&job,sizeof(msg));
			pthread_exit(NULL);
		}
		size = get_stat(fdin);

		if (size<1) {
			fprintf(stderr, "Invalid size.\n");job.rotation = -1;
			write(pipein[(buf->rot/90)-1][1],&job,sizeof(msg));
			pthread_exit(NULL);
		}

		if ( (src = mmap(0, size, O_RDWR, MAP_FILE | MAP_SHARED, fdin, 0)) == (caddr_t) -1)	{
			fprintf(stderr,"mmap error for input\n");
			job.rotation = -1;
			write(pipein[(buf->rot/90)-1][1],&job,sizeof(msg));
			pthread_exit(NULL);
		}
		
#ifdef DEBUG
			printf("File %s mapped. Going to rotate.\n", job.tempmsg);
#endif
		
		do_rotation(src, buf->rot);		
		if (munmap(src, size)<0)
			fprintf(stderr, "Failed to unmap memory.\n");
		close(fdin);
		
#ifdef DEBUG
		printf("File closed. Sending message through pipe...\n");
#endif
		
		if (write(pipein[(buf->rot/90)-1][1],&job,sizeof(msg))<0) {
			fprintf(stderr, "Error writing to buffer pipein %i.\n",(buf->rot/90)-1);
			kill(job.clientPid, SIGUSR1);
			break;
		}
		if (sigprocmask (SIG_UNBLOCK, &block_all, NULL)<0)
			fprintf(stderr, "Failed to unblock signals in worker thread.\n");
	}
	
#ifdef DEBUGEXIT
		printf("Worker a sair...\n");
#endif
	pthread_exit (NULL);
}
