%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%Autores: Grupo 23                                     %
%Joao Miguel Rodrigues Jesus	            2008111667 %
%Rosa Manuela Rodrigues de Faria            2005128014 %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


function [freqfund, sinal_aprox] = STFT( sinal, fs, reconstruir,janela,sobrepos )

factor = fs/1000; %factor de conversao de ms para frequencias
janela = round (janela*factor);
sobrepos = round (sobrepos*factor);
tam = size(sinal);

%Defini��o do eixo dos xx - intervalos entre frequ�ncias.
if mod(tam(1), 2) == 0
    freqs = linspace (-fs/2, fs/2 - fs/janela, janela);
else
    freqs = linspace (-fs/2 + fs/(2*janela), fs/2 - fs/(2*janela), janela);
end

%inicializa��o de vari�veis
indices = 1:janela-sobrepos:length(sinal)-janela;
amps = zeros(size(indices));
freqfund = zeros(size(indices));
sinal_aprox = zeros(size(sinal));

for k = 1:length(indices);
    imin = indices(k); %limite inferior da janela
    %limite superior da janela -> caso tenha chegado ao limite do sinal, � esse o maximo
    imax = min(janela+imin-1,tam(1));
    
    sinal_i = sinal(imin : imax); %parte do sinal a analisar
    janela_i = sinal_i.*hamming(length(sinal_i)); %janela de hamming
    transformada = fftshift(fft(janela_i)); %dft da janela actual
    
    amps(k) = max(abs(transformada)); %maximo absoluto da dft calculada
    index = find(abs(transformada) == amps(k));%ocorrencias do max absoluto
    freqfund(k) = freqs(index(end)); %ultima acontecimento do maximo
    
	%caso seja necess�rio reconstruir o sinal
    if (reconstruir~=0)
        Ts = 1/fs;
        t = 0: Ts : Ts*(tam(1)-1);
        sinal_aprox(imin:imax)= 10*amps(k)/janela * sin(2*pi*freqfund(k)*t(imin:imax)); %amplitude em decibeis
    end
end

freqfund(freqfund<=100) = 0; %frequencias inferiores a 100 Hz sao descartadas

%gr�fico das frequ�ncias fundamentais
eixox = linspace(1,tam(1)/factor-46.44,length(freqfund));
plot(eixox,freqfund, 'o');
title(sprintf('Sucessao temporal de frequencias fundamentais\nResolu��o em frequ�ncia = %.2f\n', fs/janela));

end

