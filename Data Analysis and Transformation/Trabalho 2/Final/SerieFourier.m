function [Cm, tetam, cc, tetac]=SerieFourier(t,x,T0,m_max)
% t e x devem ser vectores coluna

	A=zeros(length(t),2*m_max+2);
    for k=0:m_max
		A(:,k+1)=cos(2*pi/T0*t*k);
		A(:,m_max+1+k+1)=-sin(2*pi/T0*t*k);
    end
	coef=pinv(A)*x;
	a=coef(1:m_max+1);
	b=coef(m_max+2:2*m_max+2);
	[nl,nc]=size(a);
	for lin=1:nl
		for col=1:nc
			if abs(a(lin,col))<0.001 && abs(b(lin,col))<0.001
				a(lin,col)=0; b(lin,col)=0;
			end
		end
	end
	Cm=abs(a+b*1i);	%Cm=(a.^2+b.^2).^0.5
	tetam=angle(a+b*1i);   %tetam=atan(b./a)
    
    
    %%Coeficientes da serie de Fourier complexa
    cc = zeros(1,2*m_max+1);
    tetac = zeros(1,2*m_max+1);

    cc(1:m_max+1) = flipud(Cm)/2; %cm = C/2
    cc(m_max+1:2*m_max+1) = Cm/2;
    cc(m_max+1) = Cm(1);

    tetac(1:m_max+1) = -flipud(tetam); %thetam = -theta
    tetac(m_max+1:2*m_max+1) = tetam; %thetam = theta
end
