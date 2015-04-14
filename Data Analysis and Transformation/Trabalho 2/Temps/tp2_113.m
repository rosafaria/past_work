

%1.2.13
a = [1 -2.3 1.74 -0.432 0 0];
b = [0 0 0 0.3137 0 -0.1537];
ganho = ddcgain(b, a);
fprintf('O ganho do sistema em regime estacionário é: ');
disp(ganho);