%4.1
syms t m w;
W = linspace(0, 6, 500);

xt = 2*exp(-0.7*t) * sin(4*pi*t);
X = int(xt * exp(-1i*w*t), t, 0, 6);

Xw = double(subs(X, W));

subplot(2, 1, 1);
plot(W, abs(Xw));
title('Módulo');
axis([-15 15 0 1.6]);
subplot(2, 1, 2);
plot(W, angle(Xw));
title('Fase');
axis([-15 15 -0.4 0.4]);

fprintf('Prima uma tecla para continuar.\n\n');
pause();
%4.2
p = linspace(0, 6, 500);
xp = 2*exp(-0.7*p).*sin(4*pi*p);
xtf = ifourier(X, t);

hold on;
plot(p, xp, 'ob');
plot(p, double(subs(xtf, t, p)), 'g');
hold off;

%4.3
fprintf('Prima uma tecla para continuar.\n\n');
pause();
W = linspace(-11, 11, 500);
cm = int(2*exp(-0.7*t)*sin(4*pi*t)*exp(-1i*m*1*t),t,0,6)/(1/2);
m = -11 : 11;
c = subs(cm);
X2 = subs(X/(1/2), W);

subplot(2, 1, 1);
plot(W, abs(X2), m, abs(c), 'o');
xlabel('w e m*w0 [rad/s]');
title('Comparação entre |X(m*w0)/T0| e |cm| com m entre -11 e 11');

subplot(2, 1, 2);
plot(W, unwrap(angle(X2)), m, unwrap(angle(c)), 'o');
xlabel('w e m*w0 [rad/s]');
ylabel('[rad]');
title('Comparação entre <X(m*w0)/T0 e <cm com m entre -11 e 11');
