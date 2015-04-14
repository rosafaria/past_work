import java.util.Scanner;


public class A2 {
	private static Point[] pontos;
	
	
	private static class Point {
		float lat;
		float lon;
		
		Point(float lat, float lon) {
			this.lat = (float) ((int)(lat*100))/100;
			this.lon = (float) ((int)(lon*100))/100;
			
		}
		
		@Override
		public String toString() {
			return String.format("%.2f,%.2f", this.lat,this.lon);
		}
	}
	
	private static void print() {
		int aux = 1, i;
		for (i = 0; i<pontos.length-1;++i) {
			if ((pontos[i].lat == pontos[i+1].lat)&&(pontos[i].lon == pontos[i+1].lon)) {
				aux++;
			}
			else {
				System.out.print(""+pontos[i]+","+aux+"\n");
				aux = 1;
			}
		}
		System.out.print(""+pontos[i]+","+aux+"\n");
	}
	
	private static void ordenar () {
		Point chave;
		int j;
		for (int i = 1; i < pontos.length; ++i){
			chave = pontos[i];
			j = i;
			while ((j>0)&&(pontos[j-1].lat>chave.lat)) {
				pontos[j] = pontos[j-1];
				--j;
			}
			while ((j>0)&&(pontos[j-1].lat==chave.lat)&&(pontos[j-1].lon>chave.lon)) {
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
		
		//long start = System.nanoTime();
		ordenar();
		//long elapsedTime = System.nanoTime() - start;
		//System.out.println("Ordenação =" + elapsedTime);
		
		print();
	}
	
}
