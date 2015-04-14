function [ ruido, noiseadded ] = addnoise( f, t, T0, rmin, rmax, func )
%UNTITLED2 Summary of this function goes here
%   Detailed explanation goes here

tam = length(t);
ruido = zeros(1,tam);
noiseadded = 1;
W0 = 2*pi/T0;

if strcmp(f,'raleatorio') %completamente aleatorio
    ruido = 0.4*rand(tam)-0.2;
    
elseif strcmp(f,'frequencias') %numa gama de frequencias
    Cruido = 0.2*rand(tam);
    Truido = 2*pi*rand(tam)-pi;
    for m = rmin:rmax %calcular serie
        ruido = ruido + (Cruido(m)*cos(W0.*t*(m-1) + Truido(m)));
    end
    
elseif strcmp(f,'ruidofunc') %a partir de funcao dada
    ruido = func;
    
else
    noiseadded = 0; %sem ruido
end

end

