
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%Autores: Grupo 23                                     %
%Joao Miguel Rodrigues Jesus	            2008111667 %
%Rosa Manuela Rodrigues de Faria            2005128014 %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%%%%%%%%%%%%%%%%%%%% EXERCICIO 2.1
[X map] = imread('lenna.jpg');
[C S] = wavedec2(X,2,'haar');

CA2 = appcoef2(C, S, 'haar');
[CDH2 CDV2 CDD2] = detcoef2('all', C, S, 2);
[CDH1 CDV1 CDD1] = detcoef2('all', C, S, 1);

%Matriz com indices do mapa de cores
maximo = double(max(max(X)));
maxCA2 = wcodemat(CA2, maximo);
maxCDH2 = wcodemat(CDH2, maximo);
maxCDV2 = wcodemat(CDV2, maximo);
maxCDD2 = wcodemat(CDD2, maximo);
maxCDH1 = wcodemat(CDH1, maximo);
maxCDV1 = wcodemat(CDV1, maximo);
maxCDD1 = wcodemat(CDD1, maximo);


coef1 = [ [CA2 CDH2; CDV2 CDD2] CDH1; CDV1 CDD1];

coef2 = [ [maxCA2 maxCDH2; maxCDV2 maxCDD2] maxCDH1; maxCDV1 maxCDD1];

figure;
imshow(coef1, map);
title('Decomposicao com 2 niveis da Wavelet de Haar');

figure;
imshow(coef2, map);
title('Decomposicao com 2 niveis da Wavelet de Haar (cores do mapa)');

%%%%%%%%%%%%%%%%%%%% EXERCICIO 2.2

CA2 = wrcoef2('a', C, S, 'haar', 2);
CDH2 = wrcoef2('h', C, S, 'haar', 2);
CDV2 = wrcoef2('v', C, S, 'haar', 2);
CDD2 = wrcoef2('d', C, S, 'haar', 2);

CDH1 = wrcoef2('h', C, S, 'haar', 1);
CDV1 = wrcoef2('v', C, S, 'haar', 1);
CDD1 = wrcoef2('d', C, S, 'haar', 1);

img1 = CA2 + CDH2 + CDV2 + CDD2;
figure;
imshow(img1, map);
title('Imagem reconstruida com coeficientes de nivel 2');

img2 = img1 + CDH1 + CDV1 + CDD1;
figure;
imshow(img2, map);
title('Imagem reconstruida com todos os componentes');

img3 = waverec2(C, S, 'haar');
figure;
imshow(img3, map);
title('Imagem reconstruida com o waverec2');

