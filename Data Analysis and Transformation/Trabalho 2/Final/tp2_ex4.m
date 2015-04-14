%exercicio 4.1 - representação da Transformada de Fourier
syms w t;

D = -30*pi:pi/6:30*pi;
xt = 2*exp(-0.7*t) * sin(4*pi*t);
X = int(xt * exp(-1i*w*t), t, 0, 6);

Xw = double(subs(X, w, D));

subplot(2, 1, 1);
plot(D, abs(Xw));
title('Módulo');
subplot(2, 1, 2);
plot(D, angle(Xw));
title('Fase');


%exercicio 4.2 - comparacao da Transformada inversa de Fourier
tt = linspace(0, 6, 500);
xtf = ifourier(X,t);

figure();
hold on;
plot(tt, double(subs(xt,t,tt)), 'or');
plot(tt, double(subs(xtf, t, tt)), 'b');
hold off;
title('comparacao da Transformada inversa de Fourier');


%exercicio 4.3 - calculo dos coeficientes da serie complexa
syms m;

%coeficientes do sinal dado através da definição
mm = -25:25;
cm = int(xt*exp(-1i*m*t),t,0,6)/(2*pi);
c = subs(cm, m, mm);

%coeficientes calculados a partir de X(mw0)
mw0 = linspace(-25,25,500);
Xmw = subs(X, w, mw0)/(2*pi);

figure();
subplot(2, 1, 1);
plot(mw0, abs(Xmw),'b', mm, abs(c), 'or');
xlabel('w e m*w0 [rad/s]');
title('Comparação da amplitude');

subplot(2, 1, 2);
plot(mw0, unwrap(angle(Xmw)),'b', mm, unwrap(angle(c)), 'or');
xlabel('w e m*w0 [rad/s]');
ylabel('[rad]');
title('Comparação da fase');
