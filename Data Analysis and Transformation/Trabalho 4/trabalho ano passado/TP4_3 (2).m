%3.1
load sinal.mat
sinal = sumsin_freqbrk;


plot(sinal);
title('Sinal Original');
disp('Prima qualquer tecla para continuar');
pause();

%3.2
[CA, CD] = dwt(sinal, 'haar');

subplot(3,1,1);
plot(CA);
title('Coeficientes de Aproximação');

subplot(3,1,2);
plot(CD);
title('Coeficientes de Detalhe');

%3.3
X = idwt(CA,CD,'haar');
subplot(3,1,3);
plot(X);
title('Sinal reconstruido');

disp('Prima qualquer tecla para continuar');
pause();

%3.4
wavem = 'db3';

for i=0:1 
    [C,L] = wavedec(sinal,4,wavem);
    CA4 = appcoef(C,L,wavem,4);
    CD4 = detcoef(C,L,4);
    CD3 = detcoef(C,L,3);
    CD2 = detcoef(C,L,2);
    CD1 = detcoef(C,L,1);

    n = 0:length(sinal)-1;
    n1 = linspace(0,max(n),length(CA4));
    n2 = linspace(0,max(n),length(CD4));
    n3 = linspace(0,max(n),length(CD3));
    n4 = linspace(0,max(n),length(CD2));
    n5 = linspace(0,max(n),length(CD1));

    subplot(6,1,1);
    plot(n,sinal);
    title('Sinal original');

    subplot(6,1,2);
    plot(n1,CA4);
    title(sprintf('Coeficientes de Aproximação, nível 4 (wavelet: %s)', wavem));

    subplot(6,1,3);
    plot(n2,CD4);
    title(sprintf('Coeficientes de Detalhe, nível 4 (wavelet: %s)', wavem));

    subplot(6,1,4);
    plot(n3,CD3);
    title(sprintf('Coeficientes de Detalhe, nível 3 (wavelet: %s)', wavem));

    subplot(6,1,5);
    plot(n4,CD2);
    title(sprintf('Coeficientes de Detalhe, nível 2 (wavelet: %s)', wavem));

    subplot(6,1,6);
    plot(n5,CD1);
    title(sprintf('Coeficientes de Detalhe, nível 1 (wavelet: %s)', wavem));

    figure(2)
    x = waverec(C,L,wavem);

    SCA4 = wrcoef('a',C,L,wavem,4);
    SCD4 = wrcoef('d',C,L,wavem,4);
    SCD3 = wrcoef('d',C,L,wavem,3);
    SCD2 = wrcoef('d',C,L,wavem,2);
    SCD1 = wrcoef('d',C,L,wavem,1);
    x2 = SCA4+SCD4+SCD3+SCD2+SCD1;
    
    subplot(3, 1, 1);
    plot(n, sinal);
    title('Sinal Original');
    subplot(3, 1, 2);
    plot(n, x);
    title('Sinal Reconstruido com waverec');
    subplot(3, 1, 3);
    plot(n, x2);
    title('Sinal Reconstruido pela soma');
    
    figure(3);
    plot(n, sinal, n, x, '+g', n, x2, 'or');
    title(sprintf('Sinal Original + Sinais reconstruidos sobrepostos (wavelet: %s)', wavem));
    
    disp('Prima qualquer tecla para continuar');
    pause();
    
    wavem ='sym2';
    
end;
