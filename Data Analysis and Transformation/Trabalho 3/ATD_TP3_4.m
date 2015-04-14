%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%Autores: Grupo 23                                     %
%Joao Miguel Rodrigues Jesus	            2008111667 %
%Rosa Manuela Rodrigues de Faria            2005128014 %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


clear all
clc

%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Exercicio 4.1 %%%%%%%%%%%%%%%%%%%%%%%%%%%%
[xt fs nbits] = wavread('saxriff.wav');
tam = size(xt);
%wavplay(xt,fs);
wavplot(xt,fs);

%%%%%%%%%%%%%%%%%%%%%%%%%%% Exercicio 4.2 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
figure;
janela = 46.44; sobrepos = 5.8;
[~,SO] = STFT(xt,fs,1, janela, sobrepos);

%%%%%%%%%%%%%%%%%%%%%%%%% Exercicio 4.3 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%reconstrução sinal 4.2

wavwrite(SO, fs, nbits, 'novo_saxriff.wav');
rec = wavread('novo_saxriff.wav');
%wavplay(rec)
figure(6);
plot(rec);
title('saxriff.wav: Sinal reconstruido');


mediana_5 = zeros(1,length(xt)-5);
mediana_7 = zeros(1,length(xt)-7);
mediana_9 = zeros(1,length(xt)-9);

for i=1:length(xt)-5
    mediana_5(i) = median(xt(i:i+5-1));
end
figure(4);
subplot(3,1,1);
[~,S5] = STFT(mediana_5',fs,1, janela, sobrepos);
title('Mediana 5: Sucessao frequencias fundamentais');

wavwrite(S5, fs, nbits, 'saxriffMed_5.wav');
rec = wavread('saxriffMed_5.wav');
%wavplay(rec)

figure(5);
subplot(3,1,1);
plot(rec);
title('Mediana 5: Sinal reconstruido');



for i=1:length(xt)-7
    mediana_7(i) = median(xt(i:i+7-1));
end
figure(4);
subplot(3,1,2);
[~,S7] = STFT(mediana_7',fs,1, janela,sobrepos);
title('Mediana 7: Sucessao frequencias fundamentais');

wavwrite(S5, fs, nbits, 'saxriffMed_7.wav');
rec = wavread('saxriffMed_7.wav');
%wavplay(rec)
figure(5);
subplot(3,1,2);
plot(rec);
title('Mediana 7: Sinal reconstruido');


for i=1:length(xt)-9
    mediana_9(i) = median(xt(i:i+9-1));
end
figure(4);
subplot(3,1,3);
[~,S9] = STFT(mediana_9',fs,1, janela, sobrepos);
title('Mediana 9: Sucessao frequencias fundamentais');

wavwrite(S5, fs, nbits, 'saxriffMed_9.wav');
rec = wavread('saxriffMed_9.wav');
%wavplay(rec)
figure(5);
subplot(3,1,3);
plot(rec);
title('Mediana 9: Sinal reconstruido');


