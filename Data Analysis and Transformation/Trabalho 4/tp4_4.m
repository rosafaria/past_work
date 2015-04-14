%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%Autores: Grupo 23                                     %
%Joao Miguel Rodrigues Jesus	            2008111667 %
%Rosa Manuela Rodrigues de Faria            2005128014 %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

clear all
clc

%%%%%%%%%%%%%%%%%%%%% EXERCICIO 4.1
%ler ficheiros com as series temporais
load seriestemp.dat
d2 = seriestemp; %series temporais
[nl, nc] = size(d2); %dimensoes
t = (0:nl-1)'; %escala temporal

figure;
plot(t,d2,'-+');
legend('Serie temporal original 1', 'Serie temporal original 2');
title ('Contagens em 48 horas');
xlabel ('Tempo (horas)');
ylabel ('Contagens');
fprintf('Pressione uma tecla para continuar\n');
pause

%%%%%%%%%%%%%%%%%%%%% EXERCICIO 4.2
%Verificar existencia de NaN
haNaN = any(isnan(d2));
col = find (haNaN);
%Copiar matriz original, para depois reconstruir
d3 = d2;
if (any(haNaN))
    for c = col
		td = [t d3(:,c)];
		%elimina linhas com NaN
		td (any(isnan(td),2),:) = [];
		%reconstroi linhas
        d3(:,c) = interp1(td(:,1),td(:,2),t,'linear');
    end
end

figure;
plot(t,d2(:,1),'-+',t,d3(:,1),'-o');
legend('s. t. original 1','s.t. sem nan',2);
title ('contagens em 48h');
xlabel('tempo(horas)');
ylabel('Contagens');
fprintf('Pressione uma tecla para continuar\n');
pause

%%%%%%%%%%%%%%%%%%%%%%%% EXERCICIO 4.3
media = mean(d3);
sigma = std(d3);
corr = corrcoef(d3);
disp('Media:'); disp(media);
disp('Desvio-Padrao:'); disp(sigma);
disp('Correlacao:'); disp(corr);


%%%%%%%%%%%%%%%%%%%%%%%% EXERCICIO 4.4
MeanMat = repmat(media, nl, 1); %constroi matriz com a mesma dimensao que a original so com a media
SigmaMat = repmat(sigma, nl, 1); %o mesmo para o desvio-padrao
%identificar outliers
outliers = abs(d3-MeanMat) > 3*SigmaMat;
nout = sum(outliers); % numero de outliers por coluna
fprintf ('Nº de outliers por coluna:\n');
disp(nout);
haOutlier = any(nout); % verificar se ha outliers
col = find(nout); % colunas com outliers

%---- Elimina linhas com outliers e reconstroi
d4 = d3;
if any(haOutlier)
    for c = col
        td = [t d4(:,c)];
        %Elimina linhas outlier
        td((outliers(:,c)==1),:) = [];
        %reconstroi linhas
        d4(:,c) = interp1(td(:,1),td(:,2),t,'linear');
    end
end
figure;
plot(t,d3(:,2),'-+',t,d4(:,2),'-o');
legend('s.t. com outliers', 's.t. sem outliers',2);
title('Comparacao entre serie com e sem outliers');
xlabel('tempo(horas)');
ylabel('Contagens');
fprintf('Pressione uma tecla para continuar\n');
pause

%%%%%%%%%%%%%%%%%%%%%%%% EXERCICIO 4.5
%Estimar tendencia parametrica, trend de ordem 0
media = mean(d4);
MeanMatd4 = repmat(media,nl,1);
x = d4-MeanMatd4;
%trend de ordem 1
detrend_d4 = detrend(d4);

disp ('Modelo linear de 2ª ordem:');
p1 = polyfit(t,d4(:,1),2);
p2 = polyfit(t,d4(:,2),2);
tr1 = polyval(p1,t);
tr2 = polyval(p2,t);
tr = [tr1,tr2];

X = [t.^2 2*ones(length(t),1)];
Y=d4;
B=X\Y;
Tr = X*B;
%Series temporais sem tendencias
d4_dt = d4-tr;
figure;
plot(t,d4, '-+',t,tr,'-o');
legend('s. t. 1','s.t.2', 'Tendencia 1','Tendencia2',2);
title('Contagens em 48h');
xlabel ('tempo(horas)');
ylabel ('Contagens');
fprintf('Pressione uma tecla para continuar\n');
pause

figure;
plot(t, d4_dt, '-+')
legend('s. t. 1 sem tendencia', 's. t. 2 sem tendencia', 1)
title ('Contagens em 48h');
xlabel('tempo(horas)');
ylabel('Contagens');
fprintf('Pressione uma tecla para continuar\n');
pause

%%%%%%%%%%%%%%%%%%%%%%%% EXERCICIO 4.6

%%%%%%%%%%%%%%%%%%%%%%%% EXERCICIO 4.7
ho = repmat((1:12)',4,1);
sX = dummyvar(ho);
Bs = sX\d4_dt;
st = sX*Bs;

figure;
plot (t, st,'-+');
legend('Sazonalidade de s.t.1','Sazonalidade de s.t.2',2);
title('Contagens em 48h');
xlabel('tempo(horas)');
ylabel('Contagens');
fprintf('Pressione uma tecla para continuar\n');
pause

figure;
plot(t,d4-st,'-+');
legend('S. t. 1 sem sazonalidade','S. t. 2 sem sazonalidade');
title('Contagens em 48h');
xlabel('tempo(horas)');
ylabel('Contagens');
fprintf('Pressione uma tecla para continuar\n');
pause

figure;
plot(t,d4-st,'-+',t,d4-tr,'-o');
title('Contagens em 48h');
legend('S. t. 1 sem sazonalidade','S. t. 2 sem sazonalidade', 'S. t. 1 sem tendencia','S. t. 2 sem tendencia',2);
xlabel('tempo(horas)');
ylabel('Contagens');
fprintf('Pressione uma tecla para continuar\n');
pause

%%%%%%%%%%%%%%%%%%%%%%%% EXERCICIO 4.8
irreg_d4 = d4-tr-st;
figure;
plot(t, irreg_d4,'-+');
title('Contagens em 48h');
legend('Componente irregular da s.t.1','Componente irregular da s.t.2');
xlabel('tempo(horas)');
ylabel('Contagens');
fprintf('Pressione uma tecla para continuar\n');
pause;

%serie temporal sem a componente irregular
d4_irreg = d4-irreg_d4;
figure;
plot(t, d4_irreg, '-+')
legend('S. t. 1 sem componente irregular','S. t. 2 sem componente irregular');
title ('Contagens em 48h'); 
xlabel('tempo(horas)');
ylabel('Contagens');
fprintf('Pressione uma tecla para continuar\n');
pause

%%%%%%%%%%%%%%%%%%%%%%%% EXERCICIO 4.9
%filtro media movel
b = [1 1 1 ]/3; % media movel com 3 elementos
a = 1;
% aplica filtro
y1 = filter(b,a,d4(:,1));
y2 = filter(b,a,d4(:,2));
y = [y1 y2];
figure;
plot(t, d4, '-+', t, y, '-o', t, d4_irreg, '-+')
legend('S.t.1', 'S.t.2', 'S.t.1 filtrada', 'S.t.2 filtrada','s.t.1 sem componente irregular','s.t. 2 sem componente irregular');
title ('Contagens em 48h');
xlabel('tempo(horas)');
ylabel('Contagens');