%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%Autores: Grupo 23                                     %
%Joao Miguel Rodrigues Jesus	            2008111667 %
%Rosa Manuela Rodrigues de Faria            2005128014 %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


%% Fun��o para o c�lculo de um integral pela Regra do Trap�zio %%

function tra = trapezio(a,b,n)
h = (b-a)/n;
i = 1:(n-1);
tra = h * ((func23(a) + func23(b)) / 2 + sum(func23(a + h * i)));
end
