%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%Autores: Grupo 23                                     %
%Joao Miguel Rodrigues Jesus	            2008111667 %
%Rosa Manuela Rodrigues de Faria            2005128014 %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


%%%%%%%%%%%%%%%%%%% Exercicio 1.3 %%%%%%%%%%%%%%%%%%%%%%

clear all %limpar todas as variaveis

tic()
%criacao do vector t com 500 elementos e dos valores da funcao correspondente
t = linspace(-pi,pi,500);
x = cos(14*t+pi/2)-cos(4*t+pi/2)+ 5/2*cos(12*t)+5/2;

%criacao do sinal discreto n = T/Ts e da funcao x[n]
Ts = 0.1;
n = floor(-pi/Ts):floor(pi/Ts);
xn = cos(14*n*Ts+pi/2)-cos(4*n*Ts+pi/2)+ 5/2*cos(12*n*Ts)+5/2;


%Criacao dos graficos

%Primeira janela: x(t)
subplot(2,2,1);
plot(t, x);
ylabel('x(t)');

%Segunda janela: x[n]
subplot(2,2,2);
plot(n*Ts, xn, 'r.');
ylabel('x[n]')

%Terceira janela: comparacao entre x[n] e x(t)
subplot(2,1,2);
plot(t, x , n*Ts, xn, 'r.');
ylabel('x(t) e x[n]')

toc()
