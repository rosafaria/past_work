%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%Autores: Grupo 23                                     %
%Joao Miguel Rodrigues Jesus	            2008111667 %
%Rosa Manuela Rodrigues de Faria            2005128014 %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

clc
clear all
%%%%%%%%%%%%%%%%%%%%%%% EXERCICIO 2.1
[x, fs, nbits] = wavread('saxriff.wav');
%wavplay(x, fs);
[X,freq] = wavplot(x,fs);


%%%%%%%%%%%%%%%%%%%%%%% EXERCICIO 2.2
%Amplitude máxima
fprintf('Pause ... Press any key\n');
pause();
maxamp = max(abs(X));

%Frequência correspondente
ind = find(abs(X) == maxamp);
freqmax = freq(ind(2));

fprintf('A frequência da componente com aplitude máxima %f é %f Hz\n', maxamp, freqmax);
fprintf('Pause ... Press any key\n');
pause();


%%%%%%%%%%%%%%%%%%%%%%% EXERCICIO 2.3


%Criar ruído entre 8500 e 9500 Hz
fmin = 8500;
fmax = 9500;
ind_pos = find(freq>=fmin & freq<=fmax);
ind_neg = find(freq<=-fmin & freq>=-fmax);

%obter valores absolutos apartir de imaginarios e definicao da amp max.
xabs = 0.1*maxamp*rand(size(ind_pos));
xang = 2*pi*rand(size(ind_pos))-pi;
Xpos = xabs.*(cos(xang)+1i*sin(xang));
Xneg = conj(Xpos); %Calcular conjugado


%Adicionar a transformada do ruido ao anterior
Xr = X;
Xr(ind_pos) = X(ind_pos)' + Xpos;
Xr(ind_neg) = X(ind_neg)' + fliplr(Xneg); 
figure();
stem(freq, abs(Xr));
title('Espectro do sinal com ruído entre 8500 e 9500');
fprintf('Pause ... Press any key\n');
pause()

%%%%%%%%%%%%%%%%%%%%%%% EXERCICIO 2.4
%Inversa de fft para obter sinal no dominio temporal
xr = real(ifft(ifftshift(Xr)));
%wavplay(xr, fs);
 
 
%%%%%%%%%%%%%%%%%%%%%%% EXERCICIO 2.5
%Aplicar um filtro para eliminar o ruído gerado anteriormente
fc = 8000; %8kHz
wn = 2*fc/fs; % ratio of wn = 2*fc/fs
[b a] = butter(6, wn); %6 é a ordem do filtro passa-baixo de Butterworth com uma freq de corte 8KHz
xfiltrado = filter(b, a, xr); %xfiltrado
Xfiltrado = fftshift(fft(xfiltrado)); %reproduzir o sinal com o ruido filtrado

polos = roots(a); % polos 
zeros = roots(b); % zeros

%Apresentar valores
fprintf('Pólos:\n');
disp(polos);
fprintf('Zeros:\n');
disp(zeros);
fprintf('Coeficientes do numerador de G(Z):\n');% Y(Z)
disp(b);
fprintf('Coeficientes do denominador de G(Z):\n');% X(Z)
disp(a);

figure();
zplane(b,a)

%%%%%%%%%%%%%%%%%%%%%%% EXERCICIO 2.6
fprintf('Pause... Press any key\n');
pause();
figure();
stem(freq, abs(Xfiltrado));
title('Espectro do sinal com filtro');
%wavplay(xfiltrado, fs);

%Utilizacao de ruido na mesma banda de freq. do sinal original (2 - 3KHZ)
frq1 = 2000;
frq2 = 3000;
indicespos = find(freq>=frq1 & freq<=frq2);
indicesneg = find(freq>=-frq2 & freq<=-frq1);

Xrabs = 0.1 * maxamp*rand(size(indicespos));
Xrangle = 2*pi * rand(size(indicespos)) - pi;
Xpos = Xrabs.*(cos(Xrangle)+1i*sin(Xrangle));
Xneg = conj(Xpos);

%Adicionar a transformada do ruido ao sinal anterior
Xr = X;
Xr(indicespos) = X(indicespos)'+Xpos;

%Apresentar ao contrário
Xr(indicesneg) = X(indicesneg)'+fliplr(Xneg); 
%Aprensentar Valores
figure();
stem(abs(Xr));
title('Espetro do sinal com ruído entre 2 a 3 KHz');

%Obter o sinal com ruído através da inversa de Fourier
xr = real(ifft(ifftshift(Xr)));
%wavplay(xr,fs);

fprintf('Pause... Press any key ');
pause();

fc1 = 2000;
fc2 = 3000;
wn = [2*fc1/fs,2*fc2/fs];

%filtrar sinal utilizando butterworth de ordem 6
[b,a] = butter(6,wn,'stop');

xfiltered = filter(b,a,xr);
Xfiltered = fftshift(fft(xfiltrado));

%Calcular polos do sinal com ruido
polos = roots(a);
zeros = roots(b);
fprintf('Os Pólos são : \n ');
disp(polos);
fprintf('Os Zeros são : \n ');
disp(zeros);
fprintf('Coeficientes do numerador de G(Z): \n');
disp(b);
fprintf('Coeficientes do denominador de G(Z): \n');
disp(a);

%Apresentar valores 
figure();
zplane(b,a)

fprintf('Pause ... Press any key\n');
pause();
figure();
stem(abs(Xfiltered));
title('Espectro do sinal com um filtro');
%wavplay(xfiltered,fs);
