%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%Autores: Grupo 23                                     %
%Joao Miguel Rodrigues Jesus	            2008111667 %
%Rosa Manuela Rodrigues de Faria            2005128014 %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

clear


n = -50:50;
for i=1:length(n)
    ypulse(i) = 0.4*(Krond(n(i),-1)) + 0.9*(Krond(n(i),-3)) - 0.4*(Krond(n(i),-4));
end
ylabel('y(pulse[n])')
plot(n, ypulse)
