syms n;
syms z;
xn = input('\nInsira o vector de x[n]: ');
HZ = (0.3137*z^-3 - 0.1537*z^-5)/(1 - 2.3*z^-1 + 1.74*z^-2 - 0.432*z^-3);
XZ = 0;
for i = 1:length(xn)
   XZ = XZ + xn(i)*z^(-i+1);
end
YZ = XZ*HZ;
yn = iztrans(YZ);
pretty(yn);


a = [1 -2.3 1.74 -0.432 0 0];
b = [0 0 0 0.3137 0 -0.1537];


nn=0:length(xn)-1;
[X Y] = stairs(nn, subs(yn, nn));
y21 = filter(b, a, subs(xn, nn));
y22 = dlsim(b, a, subs(xn, nn));
plot(X, Y, nn, y21, 'or', nn, y22, 'g+');