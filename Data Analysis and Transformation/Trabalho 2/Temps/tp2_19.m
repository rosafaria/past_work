
a = [1 -2.3 1.74 -0.432 0 0];
b = [0 0 0 0.3137 0 -0.1537];

w = linspace(0, pi, 500);
H = freqz(b, a, w);


subplot(2, 1, 1);
plot(20*log10(abs(H)),'g');
ylabel('Amplitude');
subplot(2, 1, 2);
plot(unwrap(angle(H)),'b');
ylabel('Fase');