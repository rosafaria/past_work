%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%Autores: Grupo 23                                     %
%Joao Miguel Rodrigues Jesus	            2008111667 %
%Rosa Manuela Rodrigues de Faria            2005128014 %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

clear all
clc

%Exercicio 3.1
[img map] = imread('lenna.jpg');
imshow(img);
title ('Imagem original');

%Exercicio 3.2

figure;
DCT = dct2(img);
new_img = 20*log10(abs(DCT));
new_img (new_img == -Inf) = 0;
imshow(new_img,map);
colormap(jet(64));
colorbar;
title ('DCT');

%Exercicio 3.3

figure;
DCT8 = blkproc(img, [8 8], 'dct2');
new_img = 20*log10(abs(DCT8));
new_img (new_img == -Inf) = 0;
imshow(new_img,map);
colormap(jet(64));
colorbar;
title ('DCT em blocos 8x8');


%Exercicio 3.4

figure;
subplot(2,3,1);
iDCT8 = blkproc(DCT8, [8 8], 'idct2');
imshow(iDCT8,map);
title (sprintf('DCT inversa\nblocos 8x8'));

coeficientes = [1 5 10 20 64];

for i = 1: length(coeficientes)
    DCTc = blkproc(DCT8,[8 8],'PickCoef',coeficientes(i));
    iDCTc = blkproc(DCTc, [8 8],'idct2');
    subplot(2,3,(i+1));
    imshow(iDCTc,map);
    title (sprintf('DCT inversa\n%d coficientes',coeficientes(i)));
end




%Exercicio 3.5