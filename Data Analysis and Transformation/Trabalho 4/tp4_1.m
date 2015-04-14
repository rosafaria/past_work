%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%Autores: Grupo 23                                     %
%Joao Miguel Rodrigues Jesus	            2008111667 %
%Rosa Manuela Rodrigues de Faria            2005128014 %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
clc

%%%%%%%%%%%%%%%%%%%%% EXERCICIO 1.1
x = load('sinal.mat');
sinal = x.sumsin_freqbrk;

plot(sinal);
title('Sinal sumsin\_freqbrk');
xlabel('n');
ylabel('Amplitude');

%%%%%%%%%%%%%%%%%%%%% EXERCICIO 1.2
%%wavelet Haar
[CA,CD] = dwt(sinal,'haar');

figure;
n = linspace(0, length(sinal)-1, length(CA));
plot(n,CA);
title('Coeficientes de Aproximação');
xlabel('n');
ylabel('Amplitude');

figure;
n = linspace(0, length(sinal)-1, length(CD));
plot(n,CD);
title('Coeficientes de Detalhe');
xlabel('n');
ylabel('Amplitude');

%%%%%%%%%%%%%%%%%%%%% EXERCICIO 1.3

X = idwt(CA,CD,'haar');
figure;
hold on;
plot(sinal,'b');
plot(X,'m');
hold off;
legend('Sinal original','Reconstrucao a partir de CA e CD',2);
title('Comparacao entre sinal original e reconstituído');
xlabel('n');
ylabel('Amplitude');

%%%%%%%%%%%%%%%%%%%%% EXERCICIO 1.4
wave = 'db3';
[C1,L1] = wavedec(X,4,wave);

%Calculo dos coeficientes de nivel 4
for i=4:-1:1
    CD_1= detcoef(C1,L1,i);
    figure(5);    subplot(5,1,i+1);    plot(CD_1);
    ylabel(sprintf('CD%i',i));
    
    d = wrcoef('d', C1, L1, wave, i);
    figure(6);    subplot(5,1,i+1);    plot(d);
    ylabel(sprintf('d%i',i));
end

a4 = wrcoef('a', C1, L1, wave, 4);
CA4 = appcoef(C1, L1, wave, 4);
figure(6);    subplot(5,1,1);    plot(a4); ylabel('a4'); title('Sinais dos Coeficientes com wavelet db3');
figure(5);    subplot(5,1,1);    plot(CA4); ylabel('CA4'); title('Coeficientes com wavelet db3');

%%%%%%%%%%%%%%%%%%%%% EXERCICIO 1.5
wave = 'sym2';
[C2,L2] = wavedec(X,4,wave);

%Calculo dos coeficientes de nivel 4
for i=4:-1:1
    CD_4 = detcoef(C2, L2, i);
    figure(7);    subplot(5,1,i+1);    plot(CD_4);
    ylabel(sprintf('CD%i',i));
    
    d = wrcoef('d', C2, L2, wave, i);
    figure(8);    subplot(5,1,i+1);   plot(d);
    ylabel(sprintf('d%i',i));
end

a4 = wrcoef('a', C2, L2, wave, 4);
CA4 = appcoef(C2, L2, wave, 4);
figure(8);    subplot(5,1,1);    plot(a4);  ylabel('a4'); title('Sinais dos Coeficientes com wavelet sym2');
figure(7);    subplot(5,1,1);    plot(CA4); ylabel('CA4'); title('Coeficientes com wavelet sym2');


%%%%%%%%%%%%%%%%%%%%% EXERCICIO 1.6

%Daubechies 3
CA4_1 = wrcoef('a', C1, L1, 'db3',4);
CD4_1 = wrcoef('d', C1, L1, 'db3',4);
CD3_1 = wrcoef('d', C1, L1, 'db3',3);
CD2_1 = wrcoef('d', C1, L1, 'db3',2);
CD1_1 = wrcoef('d', C1, L1, 'db3',1);

DB31 = CA4_1 + CD4_1;
DB32 = DB31 + CD3_1;
DB33 = DB32 + CD2_1;
DB34 = DB33 + CD1_1;

%Symlet 2
CA4_2 = wrcoef('a', C2, L2, 'sym2',4);
CD4_2 = wrcoef('d', C2, L2, 'sym2',4);
CD3_2 = wrcoef('d', C2, L2, 'sym2',3);
CD2_2 = wrcoef('d', C2, L2, 'sym2',2);
CD1_2 = wrcoef('d', C2, L2, 'sym2',1);

S21 = CA4_2 + CD4_2;
S22 = S21 + CD3_2;
S23 = S22 + CD2_2;
S24 = S23 + CD1_2;

figure;
subplot(3,1,1);plot(sinal); title ('Comparacoes sinal com reconstrucoes CA4+CD4');
subplot(3,1,2);plot(DB31); legend('db3');
subplot(3,1,3);plot(S21); legend('sym2');


figure;
subplot(3,1,1);plot(sinal); title ('Comparacoes sinal com reconstrucoes CA4+CD4+CD3');
subplot(3,1,2);plot(DB32); legend('db3');
subplot(3,1,3);plot(S22); legend('sym2');

figure;
subplot(3,1,1);plot(sinal); title ('Comparacoes sinal com reconstrucoes CA4+CD4+CD3+CD2');
subplot(3,1,2);plot(DB33); legend('db3');
subplot(3,1,3);plot(S23); legend('sym2');

figure;
subplot(3,1,1);plot(sinal); title ('Comparacoes sinal com reconstrucoes CA4+CD4+CD3+CD2+CD1');
subplot(3,1,2);plot(DB34); legend('db3');
subplot(3,1,3);plot(S24); legend('sym2');

figure;
subplot(3,1,1);plot(sinal); title('Sinal Original vs. Sinais reconstruidos');
subplot(3,1,2);plot(waverec(C1, L1, 'db3')); legend('db3');
subplot(3,1,3);plot(waverec(C2, L2, 'sym2')); legend('sym2');
