function [filtro, filtrado] = addfilter( t, r, option, fmin, fmax, T0, mmax )
%funcao que calcula o filtro para um determinado xr e consoante a opção
%escolhida
%escolha das frequências a utilizar

[Cmr, tetamr] = SerieFourier(t', r', T0, mmax); %coeficientes de Fourier do ruido
filtro = zeros(size(t));
filtrado = 1;
W0 = 2*pi/T0;
if strcmp(option,'alto')
    fmax = mmax;
elseif strcmp(option,'baixo')
    fmin = 1;
end

if (~(strcmp(option,'nofilter'))) && (~(strcmp(option,'nofilter'))) %se houver filtro
    for m = fmin:fmax
        filtro = filtro + (Cmr(m)*cos(W0.*t*(m-1) + tetamr(m)));
    end
elseif ~strcmp(option,'nofilter')
    for m = 1:fmin
        filtro = filtro + (Cmr(m)*cos(W0.*t*(m-1) + tetamr(m)));
    end
    for m = fmax:mmax
        filtro = filtro + (Cmr(m)*cos(W0.*t*(m-1) + tetamr(m)));
    end
else filtrado = 0;

end

