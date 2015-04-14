%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%Autores: Grupo 23                                     %
%Joao Miguel Rodrigues Jesus	            2008111667 %
%Rosa Manuela Rodrigues de Faria            2005128014 %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


%% Função para o cálculo de um integral pela Regra de Simpson %%


function simp = simpson(a,b,n)
h=(b-a)/n;
impares = 1:2:n-1;
pares = 2:2:n-2;

%aplicação directa da fórmula para os vectores de números ímpares e pares
simp = h/3*(func23(a) + func23(b) + sum(4 * func23(a + h * impares)) + sum(2 * func23(a + h * pares)));
end
