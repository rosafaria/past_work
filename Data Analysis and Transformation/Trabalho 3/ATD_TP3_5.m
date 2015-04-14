%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%Autores: Grupo 23                                     %
%Joao Miguel Rodrigues Jesus	            2008111667 %
%Rosa Manuela Rodrigues de Faria            2005128014 %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


clc
clear all
close all
 
%%%%%%%%%%%%%%%%%%%%%%%%%%%% EXERCICIO 5.1 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%
[xt,fs,~]=wavread('escala.wav');
%wavplay(xt,fs);
wavplot(xt,fs);

%%%%%%%%%%%%%%%%%%%%%%%%%%%%% EXERCICIO 5.2 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%
janela = input('\nescala.wav: Por favor insira o tamanho da janela em ms: ');
sobrepos = input('\nescala.wav: Por favor insira o tamanho da sobreposição em ms: ');
figure;
[F,~] = STFT(xt,fs,0, janela, sobrepos);
notasmusicais(F);
%%%%%%%%%%%%%%%%%%%%%%%%%%% EXERCICIO 5.3/4 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%%%%%%%%%%%%%% piano.wav
[xt,fs,~]=wavread('piano.wav');
%wavplay(xt,fs);
wavplot(xt,fs);

janela = input('\npiano.wav: Por favor insira o tamanho da janela em ms: \n');
sobrepos = input('\npiano.wav: Por favor insira o tamanho da sobreposição em ms: \n');
figure;
[F,~] = STFT(xt,fs,0, janela, sobrepos);
notasmusicais(F);

%%%%%%%%%%%%% flauta.wav
[xt,fs,nbits]=wavread('flauta.wav');
xt = xt(:,1);
%wavplay(xt,fs);
wavplot(xt,fs);

janela = input('\nflauta.wav: Por favor insira o tamanho da janela em ms: \n');
sobrepos = input('\nflauta.wav: Por favor insira o tamanho da sobreposição em ms: \n');
figure;
[F,S] = STFT(xt,fs,1, janela, sobrepos);
notasmusicais(F);