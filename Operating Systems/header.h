/*
 * Autores:
 * Rosa Manuela Rodrigues de Faria (nº 2005128014)
 * Vasco Bruno dos Santos Gouveia (nº 2010106666)
 * 
*/

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <semaphore.h>
#include <fcntl.h>
#include <signal.h>
#include <pthread.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/mman.h>
#include <limits.h>
#include <sys/wait.h>

#define MAX_BUFFER 500
#define MAX_PATH 512

//Estruturas
typedef struct msg {
	pid_t clientPid;
	char tempmsg[MAX_PATH]; //caminho para ficheiro
	int rotation;
} msg;

typedef struct bufferStruct* bufferPtr;

typedef struct bufferStruct{
	msg message;
	bufferPtr next;
} bufferStruct;

typedef struct mem_struct{
	sem_t sem_empty, sem_full;
	pthread_mutex_t mutex;
	bufferPtr bufferStart, bufferEnd;
	int rot;
} mem_struct;

struct pixel
{
	char R;
	char G;
	char B;
};

//Prototipos
void* worker(void*) ;
void *master (void*);
int rotation(int id);
struct pixel *get_pixel(char *buf, int *pos);
void write_pixel(struct pixel *ppix, char *buf, int *pos);
void do_rotation(char *src, int rotation);
