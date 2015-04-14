
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;


public class RandomList {
	final static int MAXZERO = 300;
	
	private static class Elem {
		private String word;
		private int count;
		private int nPoint;
		private Elem[] pointers;
		
		Elem(String word, int count, int nPoint) {
			this.word = word;
			this.count = count;
			this.nPoint = nPoint;
			this.pointers = new Elem[nPoint];
		}
		
		public void addOne() {
			this.count++;
		}
		
		@Override
		public String toString() {
			String out = this.getWord()+": "+this.count;
			/**out+= " pointers: ";
			for (int i = 0; i<this.nPoint;i++) {
				if (this.pointers[i]== null) 
					out+= " "+i+": null ";
				else
					out+= " "+i+": "+this.pointers[i].getWord();
			}**/
			return out;			
		}
		
		public String getWord() {
			return word;
		}
		public int getnPoint() {
			return nPoint;
		}

		public int getCount() {
			return this.count;
		}
		
		public void setnPoint(int nPoint) {
			this.nPoint = nPoint;
		}
		public Elem[] getPointers() {
			return pointers;
		}
		
		public void setPointers(Elem[] pointers) {
			this.pointers = pointers;
		}
		
		public void delete () {
			count = 0;
		}
	}
	
	private static class List {
		private Elem start;
		private int deleted;
		private Random rand = new Random();
		
		List() {
			this.start = null;
			this.deleted = 0;
		}
		
		/**Funcao para determinar o proximo elemento com altura = level**/
		public Elem nextWithHeight (Elem elem, int level) {
			Elem aux = start;
			while ((aux!=null)&&(aux.getWord().compareToIgnoreCase(elem.getWord())<=0)) {
				aux = aux.getPointers()[0];
			}
			while ((aux!=null)&&(aux.getnPoint()<level)) {
				aux = aux.getPointers()[0];
			}
			if ((aux!=null)&&(aux.getnPoint()>=level))
				return aux;
			else return null;
		}
		
		
		public Elem[] search (String word) {
			Elem cur = start;
			if (cur == null) return null;
			Elem[] before = new Elem[10000];
			
			int i = 10000;
			String val;
			
			while ((cur!= null)&&(i>=0)) {
			
				i = cur.getnPoint()-1;
				while ((i>=0)&&(cur.getPointers()[i]==null)) {
					before[i] = cur;
					i--;
				} //descer todos os níveis que estão a null
				
				if ((i>=0)&&(cur.getPointers()[i]!=null)) { //há pelo menos 2 nível !=null
					
					val = cur.getPointers()[i].getWord();
					while ((val.compareToIgnoreCase(word)>0)&&(i>0)) {
						//a palavra do seguinte é mair que o pretendido - guardar como anetrior
						before[i] = cur;
						
						//descer um nível
						i--; 
						if (cur.getPointers()[i]!=null)
							val = cur.getPointers()[i].getWord();
					}
					
					if (val.compareToIgnoreCase(word)>=0) {
						//este é o elemento imediatamente anterior
						before[0] = cur;
						if (val.equals(word)) //a palavra já existe
							before[0] = cur.getPointers()[i];
						return before;
					}
					else cur = cur.getPointers()[i];
				}
				else break;
			}
			return before;
		}
		
		public void add (String word, int num) {
			if (start==null) { //lista vazia
				float prob = rand.nextFloat();
				int nPoint = (int) (- Math.log(prob)/Math.log(2)+1);
				start = new Elem(word,num,nPoint);
				return;
			}
			if (start.getWord().compareToIgnoreCase(word)>0) { //elemento a pôr no início da lista
				float prob = rand.nextFloat();
				int nPoint = (int) (- Math.log(prob)/Math.log(2)+1);
				Elem novo = new Elem(word,num,nPoint);
				for (int i = 0; i<nPoint;i++) {
					novo.getPointers()[i] = nextWithHeight(novo,i);
				}
				start = novo;
				return;
			}
			Elem[] before = search(word);
			
			if ((before[0]==null)||((before[0]!=null)&& (!before[0].getWord().equals(word)))) {
				float prob = rand.nextFloat();
				int nPoint = (int) (- Math.log(prob)/Math.log(2))+1;
				Elem novo = new Elem(word,num,nPoint);
				for (int j = 0; j<nPoint; j++) {
					if (before[j]!=null) {
						novo.getPointers()[j] = before[j].getPointers()[j];
						before[j].getPointers()[j] = novo;
					}
					else novo.getPointers()[j] = nextWithHeight(novo,j);
				}
			}
			else
				before[0].addOne();
		}
		
		public int remove (String word) {
			Elem[] before = search(word);
			if ((before!=null)&&(before[0]!=null)&&(before[0].getWord().equals(word))) {
				before[0].delete();
				deleted++;
			}
			return deleted;
		}
		
		public List copyList() {
			List nova = new List();
			Elem aux = start;
			while(aux!=null) {
				if (aux.getCount()>0) {
					nova.add(aux.getWord(),aux.getCount());
				}
				aux = aux.getPointers()[0];
			}
			return nova;
		}
		
		public void print () {
			Elem aux = start;
			while (aux!=null) {
				System.out.println(""+aux);
				aux = aux.getPointers()[0];
			}
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException{
		Scanner sc = new Scanner(System.in);
		String line = sc.nextLine();
		String word;
		int deleted = 0;
		List BR = new List();
		
		while(!line.equals("")) {
			StringTokenizer palavras = new StringTokenizer(line);
			while (palavras.hasMoreTokens()) {
				word = palavras.nextToken();
				if ("REMOVE".equals(word)) {
					while (("REMOVE".equals(word))&&(palavras.hasMoreTokens())) {
						word = palavras.nextToken();
					}
					if (!word.equals("REMOVE")) {
						word = word.toLowerCase();
						deleted = BR.remove(word);
						if (deleted>MAXZERO) {
							List nova = BR.copyList();
							BR = nova;
						}
					}
				}
				else {
					word = word.toLowerCase();
					BR.add (word,1);
				}
			}
			line = sc.nextLine();
		}
		if (deleted>0) {
			List nova = BR.copyList();
			BR = nova;
		}
		BR.print();
	}
}
