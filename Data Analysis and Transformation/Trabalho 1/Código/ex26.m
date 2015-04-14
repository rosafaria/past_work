%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%Autores: Grupo 23                                     %
%Joao Miguel Rodrigues Jesus	            2008111667 %
%Rosa Manuela Rodrigues de Faria            2005128014 %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

clc
clear

Funcgz = ztrans(sym('0.4*kroneckerDelta(n-1) + 0.9*kroneckerDelta(n-3) - 0.4*kroneckerDelta(n-4)'));
disp(Funcgz)
