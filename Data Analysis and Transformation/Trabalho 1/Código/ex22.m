%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%Autores: Grupo 23                                     %
%Joao Miguel Rodrigues Jesus	            2008111667 %
%Rosa Manuela Rodrigues de Faria            2005128014 %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

clear all


n = -50:50;

%Criação de ruído uniforme no intervalo [-0.2;0.2]
ruido = 0.4*rand(size(n))-0.2;
xnr = ux(n) + ruido; %sinal + ruido

%Cálculo da resposta do sistema y1[n] com ruído
y1nr = 0.4*(ux(n-1)+ruido) + 0.9*(ux(n-3)+ruido) - 0.4*(ux(n-4)+ruido);

subplot(2,1,1);
plot(n, xnr);
ylabel('x[n] + ruido');
subplot(2,1,2);
plot(n, y1nr);
ylabel('y1[n] + ruido');
