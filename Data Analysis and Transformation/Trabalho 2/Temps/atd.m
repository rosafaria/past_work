function varargout = atd(varargin)
% ATD MATLAB code for atd.fig
%      ATD, by itself, creates a new ATD or raises the existing
%      singleton*.
% Begin initialization code - DO NOT EDIT
gui_Singleton = 1;
gui_State = struct('gui_Name',       mfilename, ...
                   'gui_Singleton',  gui_Singleton, ...
                   'gui_OpeningFcn', @atd_OpeningFcn, ...
                   'gui_OutputFcn',  @atd_OutputFcn, ...
                   'gui_LayoutFcn',  [] , ...
                   'gui_Callback',   []);
if nargin && ischar(varargin{1})
    gui_State.gui_Callback = str2func(varargin{1});
end

if nargout
    [varargout{1:nargout}] = gui_mainfcn(gui_State, varargin{:});
else
    gui_mainfcn(gui_State, varargin{:});
end
% End initialization code - DO NOT EDIT
end


% --- Executes just before atd is made visible.
function atd_OpeningFcn(hObject, eventdata, handles, varargin)
handles.output = hObject; % Choose default command line output for atd
guidata(hObject, handles); % Update handles structure
end

function varargout = atd_OutputFcn(hObject, eventdata, handles) 
% Get default command line output from handles structure
varargout{1} = handles.output;
end

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%% Cálculos e plots %%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

function plot_button_Callback(hObject, eventdata, handles)

%%%%%%%%%%%%%%%%%%%%% Ler inputs %%%%%%%%%%%%%%%%%%%%%%%%%%
T0 = eval(get(handles.periodo,'String'));
mmax = str2double(get(handles.m_max,'String'));
a = str2double(get(handles.sequencia,'String')); %numero de amostras
m = [0:mmax+1]';
Ts = T0/a;              %frequencia de amostragem
t = 0:Ts:T0-Ts;         %sequencia temporal
W0 = 2*pi/T0;           %frequencia fundamental
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


%%%%%%%%%%%%%%%%%%%%% Calcular sinal %%%%%%%%%%%%%%%%%%%%%%
b = get(handles.sinal,'SelectedObject'); %Verificar tipo de onda escolhido
f = get(b,'tag');

if strcmp(f,'quadrada') %onda quadrada
    x = zeros(1,length(t));
    x(1:round(length(x)/2)) = 1;
    
elseif strcmp(f,'serra') %onda em dente de serra
    x = t/T0;
    
elseif strcmp(f,'dataset') %dados fornecidos num ficheiro externo
    a = get(handles.datafile,'String');
    fid = fopen(a);
    x = (fscanf(fid,'%f'))';
    fclose(fid);
    a = length(x);
    Ts = T0/a;
    t = 0:Ts:T0-Ts;
    
else
    x = eval(get(handles.funcao,'String')); %funcao dada
end

%%%%%%% Coeficientes de Fourier
[C,theta] = SerieFourier(t',x',T0,mmax);

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%%%%%%%%%%%%%%%%%%%Calculo do ruido%%%%%%%%%%%%%%%%%%%%%%%%%
b = get(handles.ruido,'SelectedObject'); %selecionar tipo de ruido
f = get(b,'tag');

if strcmp(f,'raleatorio') %completamente aleatorio
    r = 0.4*rand(size(x))-0.2;
    
elseif strcmp(f,'frequencias') %numa gama de frequencias
    rmax = eval(get(handles.maxr,'String'));
    rmin = eval(get(handles.minr,'String'));
    Cruido = 0.2*rand(size(m));
    Truido = 2*pi*rand(size(m))-pi;
    r = zeros(size(t));
    for m = rmin:rmax %calcular serie
        r = r + (Cruido(m)*cos(W0.*t*(m-1) + Truido(m)));
    end
    
elseif strcmp(f,'ruidofunc') %a partir de funcao dada
    r = eval(get(handles.noisefunc,'String'));
    
else
    r = 0; %sem ruido
end

xr = x + r; %onda + ruido
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


%%%%%%%%%%%%%%%%%%%Calculo do filtro%%%%%%%%%%%%%%%%%%%%%%%%%
b = get(handles.filtros,'SelectedObject'); %tipo de filtro
f = get(b,'tag');

[Cmr, tetamr] = SerieFourier(t', r', T0, mmax); %coeficientes de Fourier do ruido

fmin = eval(get(handles.minf, 'String'));
fmax = eval(get(handles.maxf, 'String'));
filtro = zeros(size(t));

%escolha das frequências a utilizar
if strcmp(f,'alto')
    fmax = mmax;
elseif strcmp(f,'baixo')
    fmin = 1;
end

if (~(strcmp(f,'nofilter'))) && (~(strcmp(f,'nofilter'))) %se houver filtro
    for m = fmin:fmax
        filtro = filtro + (Cmr(m)*cos(W0.*t*(m-1) + tetamr(m)));
    end
elseif ~strcmp(f,'nofilter')
    for m = 1:fmin
        filtro = filtro + (Cmr(m)*cos(W0.*t*(m-1) + tetamr(m)));
    end
    for m = fmax:mmax
        filtro = filtro + (Cmr(m)*cos(W0.*t*(m-1) + tetamr(m)));
    end
end
xrf = x + filtro; %sinal + ruido + filtro
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%




%%%%%%%%%%%%% Plot Sinal e Serie de Fourier %%%%%%%%%%%%%%%%
m = [0:mmax]';

if (xr == x) %sinal nao tem ruido -> plot de sinal e serie de fourier
    xc = zeros(size(x));
    for m = 1:mmax
        xc = xc + (C(m)*cos(W0.*t*(m-1) + theta(m)));
    end
    plot(handles.sobrepos,t,x,'r',t,xc,'b');
    legend(handles.sobrepos,'show','Sinal', 'Serie de fourier', 'Location','Best')
    set(handles.sobrepos,'XMinorTick','on');
    
else %ha ruido
    if (xrf==x) %sinal tem ruido mas nao filtro -> plot de sinal e sinal + ruido
        plot(handles.sobrepos,t,x,'r',t,xr,'b');
        legend(handles.sobrepos,'show','Sinal', 'Sinal com ruido', 'Location','Best')
        set(handles.sobrepos,'XMinorTick','on');
        [C, theta]=SerieFourier(t', xr', T0, mmax);
    else %sinal tem ruido e filtro -> plot sinal, sinal + ruido e sinal + ruido + filtro
        plot(handles.sobrepos,t,x,'r',t,xr,'b', t, xrf, 'g');
        legend(handles.sobrepos,'show','Sinal', 'Sinal com ruido', 'Sinal com filtro', 'Location','Best')
        set(handles.sobrepos,'XMinorTick','on');
        [C, theta]=SerieFourier(t', xrf', T0, mmax);
    end
end
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


%%%%%%%%%%%%%%%%%% Plot dos coeficientes %%%%%%%%%%%%%%%%%%%

xx = 0:mmax;
plot(handles.coefC,xx,C,'.b');
title(handles.coefC,'Cm');
set(handles.coefC,'XMinorTick','on');

plot(handles.coefT,xx,theta,'.r'); 
title(handles.coefT,'Theta');
set(handles.coefT,'XMinorTick','on');
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


%%%%%%%%%%%%%%%%%% Serie de Fourier complexa %%%%%%%%%%%%%%%%
cm = zeros(1,2*mmax+1);
thetam = zeros(1,2*mmax+1);

cm(1:mmax+1) = flipud(C)/2; %cm = C/2
cm(mmax+1:2*mmax+1) = C/2; %cm = C/2

thetam(1:mmax+1) = -flipud(theta); %thetam = -theta
thetam(mmax+1:2*mmax+1) = theta(1:mmax+1); %thetam = theta

xx = -mmax:mmax;
plot(handles.amp,xx,cm,'.g');
title(handles.amp,'cm');
set(handles.amp,'XMinorTick','on');

plot(handles.fase,xx,thetam,'.r');
title(handles.fase,'thetam');
set(handles.fase,'XMinorTick','on');
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


%%%%%%%%%%%%%%%% Exportacao de coeficientes %%%%%%%%%%%%%%%%%
M = zeros(length(xx),5);
M(:,1) = xx; %valores de m
M(mmax+1:2*mmax+1,2:3) = [C theta]; %C e theta
M(:,4:5) = [cm' thetam']; %cm e thetam

a = get(handles.fich,'String');
fid = fopen(a,'w');

for i=1:length(xx)
    fprintf(fid,'%f\t%f\t%f\t%f\t%f\n',M(i,:));
end
fclose(fid);
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

end




%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%  codigo standard do GUI  %%%%%%%%%%%%%%%%%%%%%%

function periodo_CreateFcn(hObject, eventdata, handles)
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
end

function sequencia_CreateFcn(hObject, eventdata, handles)
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
end

function m_max_CreateFcn(hObject, eventdata, handles)
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
end

function datafile_CreateFcn(hObject, eventdata, handles)
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
end

function funcao_Callback(hObject, eventdata, handles)
    f = eval(get(hObject,'String')); %devolve funcao como texto
end


function funcao_CreateFcn(hObject, eventdata, handles)
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
end

function sobrepos_CreateFcn(hObject, eventdata, handles)
end

function fich_CreateFcn(hObject, eventdata, handles)
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
end

function noisefunc_CreateFcn(hObject, eventdata, handles)
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
end

function minf_CreateFcn(hObject, eventdata, handles)
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
end

function noisefreq_CreateFcn(hObject, eventdata, handles)
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
end

function periodo_Callback(hObject, eventdata, handles)
T0 = eval(get(hObject,'String'));

%Verificar se T0 e valido
if isnan(T0) || ~isreal(T0)    %T0 nao e numero ou nao e real
    set(handles.plot_button,'String','T0 nao e valido')
    set(handles.plot_button,'Enable','off')
    uicontrol(hObject)
else 
    set(handles.plot_button,'String','Actualizar')
    set(handles.plot_button,'Enable','on')
end
end

function sequencia_Callback(hObject, eventdata, handles)
a = str2double(get(hObject,'String'));

%Verficar se o numero de amostras e valido
if isnan(a) || ~isreal(a)    %a nao e numero ou nao e real
    set(handles.plot_button,'String','o numero de amostras nao e valido')
    set(handles.plot_button,'Enable','off')
    uicontrol(hObject)
else 
    set(handles.plot_button,'String','Actualizar')
    set(handles.plot_button,'Enable','on')
end
end

function m_max_Callback(hObject, eventdata, handles)
mmax = str2double(get(hObject,'String'));

%Verificar se mmax e valido
if isnan(mmax) || ~isreal(mmax)
    set(handles.plot_button,'String','m_max nao e valido')
    set(handles.plot_button,'Enable','off')
    uicontrol(hObject)
else 
    set(handles.plot_button,'String','Actualizar')
    set(handles.plot_button,'Enable','on')
end
end

function datafile_Callback(hObject, eventdata, handles)
%ficheiro do dataset
    dfile = get(hObject,'String');
end

function noisefunc_Callback(hObject, eventdata, handles)
%função do ruído
    nf = eval(get(hObject,'String'));
end


function fich_Callback(hObject, eventdata, handles)
%Nome do ficheiro para exportação
    exportfile = get(hObject,'String');
end

function minf_Callback(hObject, eventdata, handles)
%limites do filtro
    fmin = str2double(get(hObject,'String'));
end

function noisefreq_Callback(hObject, eventdata, handles)
%limites de frequencia para ruido
    nf = eval(get(hObject,'String'));
end

function maxf_Callback(hObject, eventdata, handles)
    fmax = str2double(get(hObject,'String'));
end

function maxf_CreateFcn(hObject, eventdata, handles)
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
end

function minr_Callback(hObject, eventdata, handles)
    rmin = str2double(get(hObject,'String'));
end

function minr_CreateFcn(hObject, eventdata, handles)
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
end

function maxr_Callback(hObject, eventdata, handles)
    rmax = str2double(get(hObject,'String'));
end

function maxr_CreateFcn(hObject, eventdata, handles)
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
end
