%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%Autores: Grupo 23                                     %
%Joao Miguel Rodrigues Jesus	            2008111667 %
%Rosa Manuela Rodrigues de Faria            2005128014 %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

clear

tic()
Ts = 0.1;
n = floor(-pi/Ts):floor(pi/Ts);
xn = cos(14*n*Ts+pi/2)-cos(4*n*Ts+pi/2)+ 5/2*cos(12*n*Ts)+5/2;
E = sum(xn.^2)
toc()
