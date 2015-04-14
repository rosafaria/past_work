%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%Autores: Grupo 23                                     %
%Joao Miguel Rodrigues Jesus	            2008111667 %
%Rosa Manuela Rodrigues de Faria            2005128014 %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

function fu = ux( n )
%Função x(t) para o grupo 23
    for i = 1:length(n)
        fu(i) = 1.5*cos(0.025*pi*n(i)) .* (u(n(i),(-40))- u(n(i),40));
    end
end

