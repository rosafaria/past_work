%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%Autores: Grupo 23                                     %
%Joao Miguel Rodrigues Jesus	            2008111667 %
%Rosa Manuela Rodrigues de Faria            2005128014 %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


%%%%%%%%%%%%%%%%%%% Exerc�cio 1.4 %%%%%%%%%%%%%%%%%%%%%%

clear all

tic()
% Cria��o da vari�vel simb�lica e da respectiva fun��o
t = sym('t');
f(t) = cos(14*t+pi/2)-cos(4*t+pi/2)+ 5/2*cos(12*t)+5/2;

% C�lculo do valor exacto da energia do sinal no intervalo [-pi;pi]
Epi = int(f^2,-pi,pi)

%C�lculo do valor aproximado do integral pelas regras de trap�zio e Simpson
Trap = trapezio(-pi, pi, 13)%13 = 65.188047561988355; 12 = 1.633628179866692e+02
Simp = simpson(-pi, pi, 30)%30 = 65.188047561988199; 29 = 55.583998330829651
toc()
