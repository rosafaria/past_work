
import java.io.IOException;
import java.util.StringTokenizer;

public class A {
	
	static String readLn (int maxLg){
		byte lin[] = new byte [maxLg];
		int lg = 0, car = -1;
		try {
			while (lg < maxLg){
				car = System.in.read();
				if ((car < 0) || (car == '\n')) break;
				lin [lg++] += car;
			}
		}
		catch (IOException e){
			return (null);
		}
		if ((car < 0) && (lg == 0)) return (null);
		return (new String (lin, 0, lg));
	}
	
	public static int[] lerSeq (int N) {
		int [] sequencia = new int[N];
		for (int i = 0; i<N; i++) {
			String readin = readLn(1000);
			StringTokenizer strtok = new StringTokenizer(readin);
			sequencia[i] = Integer.parseInt(strtok.nextToken());
		}
		return sequencia;
	}
		
	public static void main(String[] args) {
		int soma = 0, valor, N, i;
		int[] sequencia;
		String readin;
		while (true) {
			readin = readLn(1000);
			StringTokenizer strtok = new StringTokenizer(readin);
			try {
				N = Integer.parseInt(strtok.nextToken());
				valor = Integer.parseInt(strtok.nextToken());
			}
			catch (NumberFormatException e) {
				return;
			}
			if ((N==0)&&(valor==0)) return;
			
			sequencia = lerSeq(N);
			for (i = 0; i<N; i++) {
				for (int j = i; j<N; j++) {
					soma = 0;
					for (int z = i; z<=j; z++) {
						soma+=sequencia[z];
					}
					if (soma==valor) {
						System.out.println("SUBSEQUENCIA NA POSICAO "+(i+1));
						break;
					}
				}
				if (soma==valor) break;
			}
			if (soma!=valor) System.out.println("SUBSEQUENCIA NAO ENCONTRADA");
		}
	}
}
