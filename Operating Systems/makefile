
# Autores:
# Rosa Manuela Rodrigues de Faria (nº 2005128014)
# Vasco Bruno dos Santos Gouveia (nº 2010106666)
 
FLAGS             = -Wall
LDFLAGS           = -pthread
DEBUGFLGS         = -g
CC                = gcc
PROG_CLIENT   = client
OBJS_CLIENT   = client.c
PROG_SERVER   = server
OBJS_SERVER   = server.c rotation.c header.h
 
all: ${PROG_CLIENT} ${PROG_SERVER}
 
clean:
		rm -f ${PROG_SERVER} ${PROG_CLIENT}
 
${PROG_CLIENT}: ${OBJS_CLIENT}
		${CC} ${DEBUGFLGS} ${FLAGS} ${OBJS_CLIENT} -o $@
 
${PROG_SERVER}: ${OBJS_SERVER}
		${CC} ${DEBUGFLGS} ${FLAGS} ${OBJS_SERVER} -o $@ ${LDFLAGS}
 
.c.o:
		${CC} ${DEBUGFLGS} ${FLAGS} $< -c
 
##########################
