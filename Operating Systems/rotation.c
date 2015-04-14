/*
 * Autores:
 * Rosa Manuela Rodrigues de Faria (nº 2005128014)
 * Vasco Bruno dos Santos Gouveia (nº 2010106666)
 * 
*/

#include "header.h"
#define	FILE_MODE	(S_IRUSR | S_IWUSR | S_IRGRP | S_IROTH)

#define DEBUG

#ifndef	MAP_FILE	/* 44BSD defines this & requires it to mmap files */
#define	MAP_FILE	0	/* to compile under systems other than 44BSD */
#endif


#define N 3

struct stat statbuf;
struct pixel *get_pixel(char *buf, int *pos){
	struct pixel pix;
	struct pixel *ppix = &pix;
	ppix->R = buf[*pos];
	ppix->G = buf[(*pos)+1];
	ppix->B = buf[(*pos)+2];
	(*pos) += 3;
	return ppix;
}

void write_pixel(struct pixel *ppix, char *buf, int *pos){
	buf[*pos] = ppix->G;
	buf[(*pos)+1] = ppix->B;
	buf[(*pos)+2] = ppix->R;
	(*pos) += 3;
}

void do_rotation(char *src, int rotation){

	int counter,index, pos, x, y;
	int xmax = 0;
	int ymax = 0;
	int colormax = 0;

	sscanf(src,"P6\n%d %d\n%d\n",&xmax,&ymax,&colormax);
	
	struct pixel imagem [ymax][xmax];
	
	for (counter=0, index=0; counter<3;index++)
	{
		if (src[index]=='\n')
			++counter;
	} 	
	pos=index-1;
	for (y=0;y<ymax;y++)
		for (x=0;x<xmax;x++)
			imagem[y][x] = *(get_pixel(src,&pos));
	pos=index;

	
	switch (rotation/90-1)
	{
		case 0 :
			//Rotação 90º clockwise
			sprintf(src,"P6\n%d %d\n%d\n",ymax,xmax,colormax);
			for (x=0; x<xmax; x++)
				for (y=ymax-1; y> -1;y--)
					write_pixel(&(imagem[y][x]),src,&pos);
			break;
		case 1:
			//Rotação 180º
			sprintf(src,"P6\n%d %d\n%d\n",xmax,ymax,colormax);
			for (y=ymax-1;y>-1;y--)
				for (x = xmax-1; x>-1; x--)
					write_pixel(&(imagem[y][x]),src,&pos);
			break;
		case 2:
			//Rotação 270º clockwise
			sprintf(src,"P6\n%d %d\n%d\n",ymax,xmax,colormax);
			for (x=xmax-1; x>-1; x--)
				for (y=0; y<ymax;y++)
					write_pixel(&(imagem[y][x]),src,&pos);
			break;
	}
}
