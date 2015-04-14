import java.util.Scanner;

public class TesteC {
	
	private static Point[] pontos;
	
	private static class Point {
		String chave;
		
		Point(float lat, float lon) {
			int aux = (int)(lat*100)*1000+(int)(lon*100);
			chave = ""+aux;
		}
		
		public int getDigit(int n, int i) {		
			int res = 0;
			for (int k = n-1; k>=0;k--) {
				if (i-k>0)
					res = res*10+(chave.charAt(i-k)-'0');
			}
			return res;
		}
		
		@Override
		public String toString() {
			int aux = Integer.parseInt(chave);
			float lat2 = (int)(aux/1000)+1;
			float lon2 = (float)((aux-lat2*1000)/100);
			lat2 = (float) (lat2 / 100);
			return String.format("%.2f,%.2f", lat2,lon2);
			
		}
	}
	
	private static void print(Point[] pontos) {
		int count = 1, i;
		for (i = 0; i<pontos.length-1;++i) {
			if (pontos[i].chave == null ? pontos[i+1].chave == null : pontos[i].chave.equals(pontos[i+1].chave)) {
				count++;
			}
			else {
				System.out.print(""+pontos[i]+","+count+"\n");
				count = 1;
			}
		}
		System.out.print(""+pontos[i]+","+count);
	}
	
	private static void printC (int[] C) {
		for (int i = 0; i<C.length;i++) {
			System.out.print(" "+C[i]);
		}
		System.out.println("");
	}
	
	private static Point[] countingSort(Point[] A, int key, int k) {
		int Csize = (int)Math.pow(10,k);
		int[] C = new int[Csize];
		int i;
		Point[] B = new Point[A.length];
		for (i = 0; i< A.length; i+=2) {
			System.out.println(A[i].chave+" "+A[i].getDigit(k,key));
			C[A[i].getDigit(k,key)]++;
		}
		//C[0]--;
		for (i = 0;(i<Csize) && C[i] == 0; i++) ;
		C[i++] +=2;
		for (; i< Csize; i++) {
			C[i] += C[i-1];
		}
		printC(C);
		for (i = A.length-1; i>=0;i--) {
			System.out.print(" "+A[i].getDigit(k,key)+"-->"+C[A[i].getDigit(k,key)]);
			B[C[A[i].getDigit(k,key)]] = A[i];
			C[A[i].getDigit(k,key)]--;
			
		}
		return B;
	}
	
	private static void ordenar() {
		int R = pontos[0].chave.length();
		for (int i = R-1; i>=0;i-=2) 
		{
			pontos = countingSort(pontos,i,2);
			print(pontos);
		}
	}


	
	public static void main (String[] args) {
		String line;
		Scanner sc = new Scanner (System.in);
		int n = sc.nextInt();
		sc.nextLine();
		if (n==0) return;
		pontos = new Point[n];
		for (int i = 0; i<n;i++) {
			if (!sc.hasNext()) break;
			line = sc.nextLine();
			String[] words = line.split(",");
			
			float lat = Float.parseFloat(words[3]);
			float lon = Float.parseFloat(words[4]);
			
			pontos[i] = new Point(lat,lon);
		}
		long start = System.nanoTime();
		ordenar();
		long elapsedTime = System.nanoTime() - start;
		System.out.println("Ordenação C=" + elapsedTime);
		print(pontos);
	}
}
