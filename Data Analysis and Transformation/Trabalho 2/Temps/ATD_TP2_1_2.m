a = [1 -2.3 1.74 -0.432 0 0];
b = [0 0 0 0.3137 0 -0.1537];

%1.2.1
polo = roots(a);
zero = roots(b);

zplane(b, a)

fprintf('Prima uma tecla para continuar.\n\n');
pause();
%1.2.2
if(all(abs(polo) < 1))
    fprintf('O sistema é estável porque todos os pólos são menores que 1\n');
else
    fprintf('O sistema não é estável porque nem todos os pólos são menores que 1\n');
end

fprintf('Prima uma tecla para continuar.\n\n');
pause();
%1.2.3
syms z;
hn = iztrans((0.3137*z^-3 - 0.1537*z^-5)/(1 - 2.3*z^-1 + 1.74*z^-2 - 0.432*z^-3));

fprintf('A resposta a impulso do sistema h[n] é\n'); pretty(hn);

fprintf('Prima uma tecla para continuar.\n\n');
pause();
%1.2.4
nn = 0:70;
h1 = subs(hn, nn);
hold on;

y1 = impz(b, a, 71);
y2 = dimpulse(b, a, 71);
[X Y] = stairs(nn, h1);

plot(X, Y, nn, y1, 'or', nn, y2, 'g+');
axis([0 70 0 1.5]);
hold off;

fprintf('Prima uma tecla para continuar.\n\n');
pause();
%1.2.5
HZ = (0.3137*z^-3 - 0.1537*z^-5)/(1 - 2.3*z^-1 + 1.74*z^-2 - 0.432*z^-3);
UZ = 1/(1-z^-1);
YZ = UZ*HZ;
yn = iztrans(YZ);
fprintf('\n\nA expressão do sistema em resposta ao degrau unitário é:\n'); pretty(yn);

fprintf('Prima uma tecla para continuar.\n\n');
pause();
%1.2.6
hold on;
stairs(nn, subs(yn, nn), 'o g');
dstep(b, a, length(nn));
hold off;

fprintf('Prima uma tecla para continuar.\n\n');
%pause();
%1.2.7
%str = input('Insira a expressão: ');
%XZ7 = ztrans(sym(eval(str)));
%pretty(XZ7)
%YZ7 = XZ7*HZ;
%yn7 = iztrans(YZ7);
%pretty(yn7);

syms n;
xn7 = input('\nInsira o vector de x[n]: ');

XZ = 0;
for i = 1:length(xn7)
   XZ = XZ + xn7(i)*z^(-i+1);
end
YZ7 = XZ*HZ;
yn7 = iztrans(YZ7);
pretty(yn7);

fprintf('Prima uma tecla para continuar.\n\n');
pause();
%1.2.8
nn=0:length(xn7)-1;
[X Y] = stairs(nn, subs(yn7, nn));
y81 = filter(b, a, subs(xn7, nn));
y82 = dlsim(b, a, subs(xn7, nn));
plot(X, Y, nn, y81, 'or', nn, y82, 'g+');


fprintf('Prima uma tecla para continuar.\n\n');
pause();
%1.2.9
w = linspace(0, pi, 500);
H = freqz(b, a, w);

subplot(2, 1, 1);
plot(20*log10(abs(H)),'g');
ylabel('Amplitude');
subplot(2, 1, 2);
plot(unwrap(angle(H)),'b');
ylabel('Fase');

fprintf('Prima uma tecla para continuar.\n\n');
pause();
%1.2.10
ganho = ddcgain(b, a);
fprintf('O ganho do sistema em regime estacionário é: ');
disp(ganho);
