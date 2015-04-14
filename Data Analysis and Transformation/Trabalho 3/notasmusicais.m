%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%Autores: Grupo 23                                     %
%Joao Miguel Rodrigues Jesus	            2008111667 %
%Rosa Manuela Rodrigues de Faria            2005128014 %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

function notasmusicais( F )

clear ind
freq_notas = [262 277 294 311 330 349 370 392 415 440 466 494];
%Ponto médio entre frequências correspondentes a notas - define intervalos nos quais se situam as notas, em vez de o fazer pontualmente
notas_medias = [254.5 269.5 285.5 302.5 320.5 339.5 359.5 381 403.5 427.5 453 480 509];
nomes_notas = {'C' 'C#' 'D' 'D#' 'E' 'F' 'F#' 'G' 'G#' 'A' 'A#' 'B'};


    string = '\n';
    string2 = '\n';
    for i=1:length(F);
        if F(i)~=0 %frequência diferente de 0, protecção contra ciclos infinitos, não entrará na lista de notas musicais.
            while F(i)>notas_medias(end) % pôr uma oitava abaixo
                F(i) = F(i)/2;
            end
            while F(i)<notas_medias(1) %pôr uma oitava acima
                F(i) = F(i)*2;
            end
            ind = find(notas_medias<=F(i));
            string = sprintf('%s %s ', string,nomes_notas{ind(end)});
            string2 = sprintf('%s %3.2f ', string2,F(i));
        end
    end
    fprintf(string);
    string2 = sprintf('%s \n\n', string2);
    fprintf(string2);
end

