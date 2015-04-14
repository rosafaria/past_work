import java.io.IOException;
import java.util.StringTokenizer;

public class C {
	
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
		if (k<N) {
			sequencia[N-1][0] = k;
			sequencia[N-1][1] = 0;
		}
		return sequencia;
	}
	
	public static int matriz (int[][] lista, int tempo) {
		int max = 0, j, i = 1, temp, num;
		
		if (lista[lista.length-1][1]==0)
			num = lista[lista.length-1][0];
		else num = lista.length;
		
		int[][] V = new int[num+1][tempo + 1];
		
		while (i < V.length){
			j = 1;
			
			while (j<lista[i-1][1]) {
				V[i][j] = V[i-1][j];
				++j;
			}
			
			while (j<tempo+1){
				temp = (V[i-1][j-lista[i-1][1]])+lista[i-1][0];
				if (temp>V[i-1][j])	V[i][j] = temp;
				else V[i][j] = V[i-1][j];
				if (V[i][j]>max) max = V[i][j];
				++j;
			}
			++i;
		}
		return max;
	}
	
	public static void main (String[] args) {
		int num, tempo, resultado;
		int[][] sequencia;
		String readin;
		
		readin = readLn(1000);
		StringTokenizer strtok = new StringTokenizer(readin);
		tempo = Integer.parseInt(strtok.nextToken());
		num = Integer.parseInt(strtok.nextToken());
		
		if (num==0) resultado = 0;
		else {
			sequencia = lerSeq(num, tempo);
			if (tempo==0) resultado = 0;
			else resultado = matriz(sequencia, tempo);
		}
		System.out.println(""+resultado);
	}
}