%exercicio 2.4
clear all

m = sym('m');
t = sym('t');

%sinal: x(t) = 1+2sin(12*pi*t+pi/4)cos(21*pi*t)
T0 = 2/3;
x = 1+2*sin(12*pi*t+pi/4)*cos(21*pi*t);
cm = int(x*exp(-1i*m*3*pi*t), t, -T0/2, T0/2)/T0;%integral dependente de m
coef = zeros(1,101);

for k = 0:100
    coef(k+1) = limit(cm,k);
end


%sinal: x(t) = -2+4cos(4t+pi/3)-2sin(10t)
T0b = pi;
xb = -2+4*cos(4*t+pi/3)-2*sin(10*t);
cmb = int(xb*exp(-1i*m*3*pi*t), t, -T0b/2, T0b/2)/T0;
coefb = zeros(1,101);

for k = 0:100
    coefb(k+1) = limit(cmb,k);
end