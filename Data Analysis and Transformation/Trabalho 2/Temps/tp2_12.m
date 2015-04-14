


%%exercício 1.2.1
a = [1 -2.3 1.74 -0.432 0.0];
b = [0 0 0 0.3137 0 -0.1537];

polos = roots(a);
zeros = roots(b);

zplano = zplane(b,a);

%disp(zplano)

fprintf('Pressione uma tecla para continuar\n');
pause();


