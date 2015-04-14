%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%Autores: Grupo 23                                     %
%Joao Miguel Rodrigues Jesus	            2008111667 %
%Rosa Manuela Rodrigues de Faria            2005128014 %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

clc
clear all
%%%%%%%%%%%%%%%%%%%%%%% EXERCICIO 3.1
%ler imagem
[imagem,cores] = imread('lena.bmp');
%imagem - matriz de H*W com o indice de cores contidas no vector cores.


%%%%%%%%%%%%%%%%%%%%%%% EXERCICIO 3.2
%Apresentar a imagem  com o indice de cores pré definido...
imshow(imagem,cores);
title('lena.bmp');


fprintf('Press any key ...\n');
pause();

%Transformada de Fourier

Ximagem = fftshift(fft2(imagem));

%Obter tamanho da matriz da imagem ...
[nlinhas,ncolunas] = size(Ximagem);

%Se for par
if(mod(nlinhas,2) == 0)
    eixo_x = -nlinhas/2:nlinhas/2-1;
else
    eixo_x = fix(-n1inhas/2):fix(n1inhas/2);
end

if(mod(ncolunas,2) == 0)
    eixo_y = -ncolunas/2:ncolunas/2-1;
else
    eixo_y = fix(-ncolunas/2) : fix(ncolunas/2);
end

%Representar valores na mesh ...
figure();
length(eixo_x)
mesh(eixo_x,eixo_y,20*log(abs(Ximagem)));
axis([eixo_x(1) eixo_x(end) eixo_y(1) eixo_y(end)]);

view([-37,5 , 30]); %Alterar a perspectiva da vista
rotate3d; %Activar Rotação

fprintf('Press any key ... \n\n');
pause();

%Encontrar cor média na posicao com freq = 0 Hz;
cx = find(eixo_x == 0);%eixo dos x na freq = 0;
cy = find(eixo_y == 0);%eixo dos y na freq = 0;

fprintf('Cor média: ');
disp(Ximagem(cx,cy) / (ncolunas*nlinhas));%Cor média da imagem.

fprintf('Press any key ...\n\n');
pause();

%%%%%%%%%%%%%%%%%%%%%%% EXERCICIO 3.3
k = 1;

size(Ximagem);
%Criar filtro ideal
mask = zeros(size(Ximagem));

%criar menu
filtro = menu('Filtro', 'Passo - Baixo', 'Passo - Alto');
corte = input('Frequência de corte');

for l=1 : nlinhas
    for c=1 : ncolunas
        dist = (l-cx)^2 + (c-cy)^2;
        if(dist <= corte^2)
            mask(l, c) = 1;
        end
    end
end
    
%Filtrar
if(filtro == 2)
    mask = ones(size(mask)) - mask;
    k = 10;
end

figure();
mesh(eixo_x, eixo_y, mask);
rotate3d;
title('Magnitude de Mask');

fprintf('Press any key ...\n\n');
pause();

%Aplicar filtro à imagem obtendo a imagem da transformada
Ximagemfilt = Ximagem.*mask;

%Apresentar Resultados imagem com filtro
figure();
mesh(eixo_x, eixo_y, 20*log10(abs(Ximagemfilt)+1)); %adiciona - se mais 1 para nao dar erro em log(0)
rotate3d;
title('Magnitude da imagem com filtro ideal');

fprintf('Press any key ... \n\n');
pause();

%Obter imagem com filtro ideal ...
imagefilt = ifft2(ifftshift(real(Ximagemfilt)));

%Apresentar a imagem reconstruida;
figure();
imshow(real(imagefilt*k), cores); 
title('Imagem com filtro ideal');
