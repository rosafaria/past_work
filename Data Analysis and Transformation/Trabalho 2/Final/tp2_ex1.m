clear a b polos zeros X Y y 
clc

%%exercício 1.2.1 - calculo de polos e zeros e representação gráfica
a = [1 -2.3 1.74 -0.432 0 0];
b = [0 0 0 0.3137 0 -0.1537];

polos = roots(a);
zeros = roots(b);

zplano = zplane(b,a);
title('Polos e zeros da funcao de transferência');


%exercicio 1.2.3 - calcular iztrans
syms Z;
bz = (0.3137*Z^(-3)) - (0.1537*Z^(-5));
az = 1 - 2.3*Z^(-1) + 1.74*Z^(-2) - 0.432*Z^(-3);
hn = iztrans(bz/az);

fprintf('A resposta de impulso do sistema e dada por:\n');
pretty(hn);

%exercicio 1.2.4 - calculo do impulso do sistema para 0<=n<=70

pause(2);
figure();
n = 0:70;
h1 = subs(hn, n);
h2 = impz(b, a, length(n));
h3 = dimpulse(b, a, length(n));
[X Y] = stairs(n, h1);
plot(X, Y,'g', n, h2, 'ob', n, h3, '+r');
title('Resposta a impulso do sistema');
xlabel('n');

%exercicio 1.2.5

HZ = bz/az;
UZ = 1/(1-Z^-1);
YZ = UZ*HZ;
yn = iztrans(YZ);
fprintf('\n\n\nA expressão do sistema em resposta ao degrau unitário e:\n'); 
pretty(yn);

%exercicio 1.2.6
pause(3);
figure();
y1 = subs(yn, n);
hold on;
stairs(n, y1, 'or');
dstep(b, a, length(n));
hold off;
title('Resposta a degrau unitário do sistema');

%exercicio 1.2.7

xn = input('\n\n\nPor favor insira o vetor cuja resposta do sistema quer determinar: ');
XZ = 0;
for i = 1:length(xn)
   XZ = XZ + xn(i)*Z^(-i+1);
end
YZ = XZ*HZ;
y = iztrans(YZ);
pretty(y);

%exercicio 1.2.8
figure();
clear X Y y1 y2
%syms('n');
n=0:length(xn)-1;
[X Y] = stairs(n, subs(y, n));
y2 = filter(b, a, subs(xn, n));
y3 = dlsim(b, a, subs(xn, n));
plot(X, Y, 'g', n, y2, 'ob', n, y3, '+r');
title('Respostas do sistema com diversas funçoes');


%exercicio 1.2.9

figure();
omega = linspace(0, pi, 500);
H = freqz(b, a, omega);
amplitude = 20*log10(abs(H)); %conversao para dB
fase = unwrap(angle(H))*180/pi; %conversao para graus
subplot(2, 1, 1);
plot(amplitude,'g');
ylabel('Amplitude');
subplot(2, 1, 2);
plot(fase,'b');
ylabel('Fase');


%exercicio 1.2.10

ganho = ddcgain(b, a);
fprintf('O ganho do sistema foi de %f\n', ganho);


