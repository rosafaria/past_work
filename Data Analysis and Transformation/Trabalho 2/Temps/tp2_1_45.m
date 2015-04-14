
%calcular a resposta ao impulso do sistema h[n] e calcular a resposta a um
%impulso unitario
nn = 0:70;
h1 = subs(hn, nn);
hold on;

a = [1 -2.3 1.74 -0.432 0 0];
b = [0 0 0 0.3137 0 -0.1537];

y1 = impz(b, a, 71);
y2 = dimpulse(b, a, 71.0);
[X Y] = stairs(nn, h1);

plot(X, Y, nn, y1, 'or', nn, y2, 'g+');
axis([0 70 0 1.5]);
hold off;