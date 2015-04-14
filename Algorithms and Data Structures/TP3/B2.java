import java.util.Scanner;

public class B2 {
	
	private static Point[] pontos;
	private static Point[] aux;
	
	private static class Point {
		float lat;
		float lon;
		
		Point(float lat, float lon) {
			this.lat = (float) ((int)(lat*100))/100;
			this.lon = (float) ((int)(lon*100))/100;
		}
		
		public int compareTo(Point other) {
			if ((this.lat == other.lat) &&(this.lon == other.lon)) return 0;
			if (this.lat> other.lat) return 1;
			if (this.lat==other.lat) {
				if (this.lon == other.lon) return 0;
				if (this.lon>other.lon) return 1;
				return -1;
			}
			return -1;
		}
		
		@Override
		public String toString() {
			return String.format("%.2f,%.2f", this.lat,this.lon);
		}
	}
	
	private static void print() {
		int count = 1, i;
		for (i = 0; i<pontos.length-1;++i) {
			if ((pontos[i].lat == pontos[i+1].lat)&&(pontos[i].lon == pontos[i+1].lon)) {
				count++;
			}
			else {
				System.out.print(""+pontos[i]+","+count+"\n");
				count = 1;
			}
		}
		System.out.print(""+pontos[i]+","+count+"\n");
	}
	
	private static void merge (int low, int middle, int high) {
		for (int i = low; i <= high; i++) {
			aux[i] = pontos[i];
		}
		int i = low;
		int j = middle + 1;
		int k = low;
		
		while (i <= middle && j <= high) {
			if (aux[i].compareTo(aux[j])<=0) {
				pontos[k++] = aux[i++];
			}
			else {
				pontos[k++] = aux[j++];
			}
		}
		
		while (i <= middle)
			pontos[k++] = aux[i++];
		
		while (j <= high)
			pontos[k++] = aux[j++];
	}
	
	private static void ordenar(int low, int high) {
		if (low < high) {
			int middle = low + (high - low) / 2;
			ordenar(low, middle);
			ordenar(middle + 1, high);
			merge(low, middle, high);
		}
  }


	
	public static void main (String[] args) {
		String line;
		Scanner sc = new Scanner (System.in);
		int n = sc.nextInt();
		sc.nextLine();
		if (n==0) return;
		pontos = new Point[n];
		aux = new Point[n];
		for (int i = 0; i<n;i++) {
			if (!sc.hasNext()) break;
			line = sc.nextLine();
			String[] words = line.split(",");
			
			float lat = Float.parseFloat(words[3]);
			float lon = Float.parseFloat(words[4]);
			
			pontos[i] = new Point(lat,lon);
		}
		long start = System.nanoTime();
		ordenar(0,n-1);
		long elapsedTime = System.nanoTime() - start;
		//System.out.println("Ordenação mergesort v2=" + elapsedTime);
		print();
	}
}
