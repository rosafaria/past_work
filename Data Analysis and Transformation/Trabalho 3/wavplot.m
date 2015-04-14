%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%Autores: Grupo 23                                     %
%Joao Miguel Rodrigues Jesus	            2008111667 %
%Rosa Manuela Rodrigues de Faria            2005128014 %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


function [dft,freq] = wavplot(xt,fs)

%Representacao grafica
figure;
duracao = length(xt)/fs;
t = linspace(0,duracao,length(xt));
plot(t,xt);
title('Sinal');
xlabel('Tempo(s)'); ylabel('Amplitude');

%Representacao do espectro
figure;
Ts = fs/length(xt);
freq = -fs/2:Ts:fs/2-Ts;
dft = fftshift(fft(xt));
plot(freq,abs(dft));
title ('Espectro do sinal');
xlabel('Frequência'); ylabel('Amplitude');

end

