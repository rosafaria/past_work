import java.util.Scanner;


public class A {
	private static Point[] pontos;
	
	
	private static class Point {
		String out;
		String comp;
		
		Point(float lat, float lon) {
			this.out = String.format("%.2f,%.2f", ((int)(lat*100))/100.,((int)(lon*100))/100.);
			this.comp = String.format("%3.2f,%3.2f", 90+((int)(lat*100))/100.,360+((int)(lon*100))/100.);
		}
		
		@Override
		public String toString() {
			return this.out;
		}
		
		public int compareTo(Point other) {
			return (this.comp).compareTo(other.comp);
		}
		
	}
	
	private static void print() {
		int aux = 1, i;
		for (i = 0; i<pontos.length-1;++i) {
			if (pontos[i].out.equals(pontos[i+1].out)) {
				aux++;
			}
			else {
				System.out.print(""+pontos[i]+","+aux+"\r");
				aux = 1;
			}
		}
		System.out.print(""+pontos[i]+","+aux+"\r");
	}
	
	private static void ordenar () {
		Point chave, aux;
		int j;
		for (int i = 1; i < pontos.length; ++i){
			chave = pontos[i];
			j = i;
			while ((j>0)&&(pontos[j-1].compareTo(chave)>0)) {
				aux = chave;
				
				pontos[j] = pontos[j-1];
				--j;
			}
			pontos[j] = chave;
		}
	}

	
	public static void main (String[] args) {
		Scanner sc = new Scanner (System.in);
		int n = sc.nextInt();
		sc.nextLine();
		if (n==0) return;
		pontos = new Point[n];
		String line;
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
		System.out.println("Ordenação =" + elapsedTime);
		
		print();
	}
	
}
