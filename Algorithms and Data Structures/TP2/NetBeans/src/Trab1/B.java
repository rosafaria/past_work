package Trab1;

import java.io.IOException;
import java.util.StringTokenizer;

public class B {
	
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
	
	public static int[][] lerSeq (int N, int tempo) {
		int[][] sequencia = new int[N][2];
		int k = 0;
		for (int i = 0; i<N; ++i) {
			String readin = readLn(1000);
			StringTokenizer strtok = new StringTokenizer(readin);
			sequencia[k][0] = Integer.parseInt(strtok.nextToken());
			sequencia[k][1] = Integer.parseInt(strtok.nextToken());
			if ((sequencia[k][1]<=tempo)&&(sequencia[k][1]>0)) ++k;
		}
		if (k<N-1) {
			sequencia[N-1][0] = k;
			sequencia[N-1][1] = 0;
		}
		return sequencia;
	}
		
	
	public static int best(int[][] lista, int ind, int tempo) {
		if (ind>=lista.length) return 0;
		if ((tempo<=0)||(lista[ind][1]==0)) return 0;
		if (tempo<lista[ind][1]) return best(lista, ind+1, tempo);
		int este = lista[ind][0]+best(lista, ind+1, tempo-lista[ind][1]);
		int outro = best(lista, ind+1, tempo);
		if (este>outro) return este;
		return outro;
	}
	
	public static void main(String[] args) {
		int num, tempo;
		int[][] sequencia;
		String readin;
		
		readin = readLn(1000);
		StringTokenizer strtok = new StringTokenizer(readin);
		try {
			tempo = Integer.parseInt(strtok.nextToken());
			num = Integer.parseInt(strtok.nextToken());
		}
		catch (NumberFormatException e) {
			return;
		}
		
		if (num==0) {
			System.out.println("0");
			return;
		}
		sequencia = lerSeq(num, tempo);
		if (tempo==0) {
			System.out.println("0");
			return;
		}
		
		long start = System.nanoTime();
		int resultado = best(sequencia, 0, tempo);
		long total = System.nanoTime()-start;
		System.out.println(""+resultado);
	}
	
}
