/*
 * Autores:
 * Rosa Manuela Rodrigues de Faria (nº 2005128014)
 * Vasco Bruno dos Santos Gouveia (nº 2010106666)
 * 
*/

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <fcntl.h>
#include <signal.h>
#include <unistd.h>
#include <limits.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/mman.h>

#define MAX_PATH 512
#define SERVER "TO_SERVER"

#define DEBUG

#define	FILE_MODE	(S_IRUSR | S_IWUSR | S_IRGRP | S_IROTH)

typedef struct {
	pid_t clientPid;
	char tempmsg[MAX_PATH]; //caminho para ficheiro
	int rotation;
} msg;

/*Variaveis Globais*/
msg request;
void *src;
int pipeout, fdin, size;

void cleanup() {
	munmap(src,size);
	#ifdef DEBUG
		printf("Memory unmaped.\n");
	#endif
	close(fdin);
	#ifdef DEBUG
		printf("File descriptor closed.\nTerminating.\n");
	#endif
	exit(0);
}


int get_stat(int fdin){
	struct stat pstatbuf;	
	if (fstat(fdin, &pstatbuf) < 0)	{/* need size of input file */
		fprintf(stderr,"fstat error\n");
		exit(1);
	}
	return pstatbuf.st_size;
}

void signals(int signum) {
	if (signum == SIGUSR1)
		printf("Received signal SIGUSR1 - File rotated correctly.\nCleaning up...\n");
	else printf("Received signal SIGUSR2 - An error ocurred during the process. Please try again.\nCleaning up...\n");
	cleanup();
}




int main (int argc, char* argv[]) {

	/*Tratamento de sinais*/
	signal(SIGUSR1, signals);
	signal(SIGUSR2, signals);
	sigset_t block_all; //Bloquear todos excepto SIGUSR1 e 2
    sigfillset(&block_all);
    sigdelset(&block_all, SIGUSR1);
    sigdelset(&block_all, SIGUSR2);
    sigprocmask (SIG_BLOCK, &block_all, NULL);

	if (argc != 3)	{
		fprintf(stderr,"usage: ./client [file] [rotation]\n");
		exit(1);
	}

	/*Mapear ficheiro*/
	if ( (fdin = open(argv[1], O_RDONLY)) < 0)	{
		fprintf(stderr,"can't open %s for reading\n", argv[1]);
		exit(1);
	}

	size = get_stat(fdin);
	
	if ( (src = mmap(NULL, size, PROT_READ, MAP_SHARED, fdin, 0)) == (caddr_t) -1)	{
		fprintf(stderr,"mmap error for input\n");
		exit(1);
	}
	
	/*Enviar pedido*/
	#ifdef DEBUG
		printf("I'm process number %i.\n\nCreating pipeout\n", getpid());
	#endif

	if ((pipeout = open(SERVER, O_RDWR))<0) {
		fprintf(stderr, "Pipe error: Pipe between client %i and server not opened. Exiting client...",getpid());
		cleanup();
	}
		
	#ifdef DEBUG
		printf("Pipeout opened. Creating message to send...\n");
	#endif
	
	request.clientPid = getpid();
	request.rotation = atoi(argv[2]);
	strcpy(request.tempmsg, argv[1]);
	
	#ifdef DEBUG
		printf("Message to be sent: pid %i, rotation %i, path %s.\n\n",request.clientPid, request.rotation, request.tempmsg);
	#endif
	
	if (write(pipeout, &request, sizeof(msg))<0) {
		fprintf(stderr, "Failed to write to pipe. Exiting client...\n");
		cleanup();
	}
	
	#ifdef DEBUG
		printf("Filepath sent. Waiting for signal...\n");
	#endif

	pause();
	return 1;
}
