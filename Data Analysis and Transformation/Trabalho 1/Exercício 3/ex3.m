%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%Autores: Grupo 23                                     %
%Joao Miguel Rodrigues Jesus	            2008111667 %
%Rosa Manuela Rodrigues de Faria            2005128014 %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

clear all
clc

%definição dos parâmetros
sampRate = 44100; %frequencia
dur = 0.5; %duracao

freq = 200:100:18000; %vector com todas as frequências a reproduzir
n = length(freq);

t = 0:1/sampRate:dur; %vector com tempos de amostragem

freqsample = 100; %intervalo de frequencias
numSamples = floor((dur*sampRate+1)/freqsample); %numero de amostras

%inicializacao de variaveis
xt = zeros(n,length(t));
gravacoes = zeros(n,dur*sampRate+1);
samples = zeros(1,numSamples);
medias = zeros(1,n);



for i=1:n
    xt(i,:) = sin(2*pi*freq(i)*t); %sinal a reproduzir
    wavplay(xt(i,:),sampRate,'async');
    gravacoes(i,:) = wavrecord(dur*sampRate+1,sampRate);
    for j=1:numSamples
        samples(j) = max(abs(gravacoes(i,(j-1)*freqsample+1 : j*freqsample))); %vector com amostras (valor maximo) desta frequência
    end
    medias(i) = mean(samples); %armazenamento da média desta frequência
end



plot(freq,medias);
xlabel('Frequência');
ylabel('Amplitude');
title('Amplitude média da resposta do sistema');
