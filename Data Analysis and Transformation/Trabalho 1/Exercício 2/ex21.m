%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%Autores: Grupo 23                                     %
%Joao Miguel Rodrigues Jesus	            2008111667 %
%Rosa Manuela Rodrigues de Faria            2005128014 %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%



clear all

n = -50:50;

xn = ux(n);
y1n = 0.4*ux(n-1) + 0.9*ux(n-3) - 0.4*ux(n-4);
y2n = 1.2*ux(2*n-4);
y3n = ux(n-2).*ux(n-3);
y4n = (n-2).*ux(n-3);

subplot(5,1,1)
plot(n, xn)
ylabel('x[n]')
subplot(5,1,2)
plot(n, y1n)
ylabel('y1[n]')
subplot(5,1,3)
plot(n, y2n)
ylabel('y2[n]')
subplot(5,1,4)
plot(n, y3n)
ylabel('y3[n]')
subplot(5,1,5)
plot(n, y4n)
ylabel('y4[n]')

