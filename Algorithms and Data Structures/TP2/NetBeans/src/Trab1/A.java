package Trab1;

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
	
	public static int greedy (int[][] lista, int tempo) {
		int max = 0, i = 0;
		while ((i<lista.length)&&(tempo>=lista[i][1])&&(lista[i][1]>0)) {
			max+=lista[i][0];
			tempo-=lista[i][1];
			i++;
		}
		return max;
	}
	
	public static void main(String[] args) {
		int num, tempo;
		int[][] sequencia;
		String readin;
		
		readin = readLn(1000);
		StringTokenizer strtok = new StringTokenizer(readin);
		tempo = Integer.parseInt(strtok.nextToken());
		num = Integer.parseInt(strtok.nextToken());
		
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
		int resultado = greedy(sequencia, tempo);
		long total = System.nanoTime()-start;
		System.out.println(""+resultado);
	}
	
}