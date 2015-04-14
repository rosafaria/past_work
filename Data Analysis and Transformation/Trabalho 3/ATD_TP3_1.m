%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%Autores: Grupo 23                                     %
%Joao Miguel Rodrigues Jesus	            2008111667 %
%Rosa Manuela Rodrigues de Faria            2005128014 %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%%%%%%%%%%%%%%%%%%%%%%% EXERCICIO 1.3

fs = 60;
ws = 2*pi*fs;
Ts = 1/fs;
w0 = 3*pi;
f0 = w0/(2*pi);
T0 = 1/f0;
N = fs/f0;

t = linspace(0, T0, 500);
n = 0:N-1;

xt = -1+3*cos(30*pi*t - pi/2)+2*cos(33*pi*t+pi/4)- 2*cos(9*pi*t+pi/4);
xn = -1+3*cos(30*pi*n*Ts - pi/2)+2*cos(33*pi*n*Ts+pi/4)- 2*cos(9*pi*n*Ts+pi/4);

plot(t,xt,'m',n*Ts,xn,'b*');
legend('x(t)', 'x[n]');
title('Sinal amostrado e Sinal Original');


%%%%%%%%%%%%%%%%%%%%%%% EXERCICIO 1.4
figure;
X = fftshift(fft(xn,N));

%Definição dos eixo xx
if(mod(N,2)==0)
    omega=linspace(-pi, pi-(2*pi/N), N);
    W = linspace(-ws/2, ws/2 - ws/N, N);
else
    omega=linspace(-pi+pi/N, pi-pi/N, N);
    W = linspace(-ws/2+2*ws/N, ws/2-2*ws/N, N);
end

X(abs(X) <= 0.001) = 0;%Frequências que quase não contribuem para o sinal - ignorar

%Plot de fftshift(fft)
subplot(2,1,1);
stem(omega,abs(X),'o');
axis tight;
title('Modulo do shift da transformada de Fourier');
ylabel('Amplitude');
xlabel('Frequencia');

subplot(2,1,2);
stem(omega,angle(X),'o');
axis tight;
title('Angulo do shift da transformada de Fourier');
ylabel('Fase');
xlabel('Frequencia');

%%%%%%%%%%%%%%%%%%%%%%% EXERCICIO 1.5

figure;

cm = X/N;
subplot(2,1,1);
plot(omega,abs(cm),'.');
axis tight;
title('Módulo');
subplot(2,1,2);
plot(omega,angle(cm),'.');
axis tight;
title('Fase');

%%%%%%%%%%%%%%%%%%%%%%% EXERCICIO 1.6

figure;
meio = floor(N/2)+1;
Cm = abs(cm(meio:N))*2;
Cm(1) = Cm(1)/2;
Teta = angle(cm(meio:N));

subplot(2,1,1);
stem(0:length(Cm)-1, Cm);
title('Cm');
subplot(2,1,2);
plot(0:length(Teta)-1, Teta, '*');
title('Tetam');

%%%%%%%%%%%%%%%%%%%%%%% EXERCICIO 1.7

figure;
xc = 0;
for m = 1:length(Cm)
    xc = xc + (Cm(m)*cos(w0.*n*Ts*(m-1) + Teta(m)));
end
plot(t,xt,'r',n*Ts,xc,'b+');
legend('sinal original', 'sinal reconstruído');
title('Comparação entre sinal original e reconstruido com coeficientes de Fourier');
