
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;


public class RandomTree {
	
	final static int MAXZERO = 300;
	
	private static class Node { 
		private Node left; 
		private Node right;
		private Node parent;
		private int num;
		private String word;
		private int count;

		Node(){}
		
		Node(String NewWord, int num, int count) { 
			this.left = null; 
			this.right = null;
			this.parent = null;
			this.word = NewWord;
			this.num = num;
			this.count = count;
		}
		
		public void setWord(String word) {
			this.word = word;
		}
	
		public void addOne () {
			this.num++;
		}
		
		@Override
		public String toString () {
			if (this==null) return "";
			//for debug
			String out = "";
			//if (this.getParent()==null) out = "RAIZ->";
			//else out = this.getParent().getWord()+"---->";
			out+= this.getWord()+": "+this.getNum();
			//out += "\tCount: "+this.count;
			return out;
		}
		
		public boolean equals (Node node) {
			if ((this == null)||(node==null)) return false;
			return this.getWord().equals(node.getWord());
		}

		public Node getLeft() {
			return left;
		}

		public void setLeft(Node left) {
			this.left = left;
		}

		public Node getRight() {
			return right;
		}

		public void setRight(Node right) {
			this.right = right;
		}

		public Node getParent() {
			return parent;
		}

		public void setParent(Node parent) {
			this.parent = parent;
		}

		public int getNum() {
			return num;
		}

		public String getWord() {
			return word;
		}
		
		public void delete() {
			this.num = 0;
		}
		
		public void addCount() {
			this.count++;
		}

		public int getCount() {
			return count;
		}
		
		public boolean biggerThan(Node aux) {
			return (this.getWord().compareToIgnoreCase(aux.getWord())>0);
		}

		/**
		 * @param count the count to set
		 */
		public void setCount(int count) {
			this.count = count;
		}
	}
	
	private static class Tree {
		private int deleted = 0;
		public int atravessados = 0;
		private Node root;
		

		Tree (){
			root = null;
		}
	
		public Node getRoot() {
			return root;
		}

		public void setRoot(Node root) {
			this.root = root;
		}

		/**Insertion**/
		private Node lookup(Node node, String word) { //Devolve o nó com a palavra word se existir
			Node aux = node;
			while ((aux!=null)&&(!word.equals(aux.getWord()))) {
				if (word.compareToIgnoreCase(aux.getWord())>0) 
					aux = aux.getRight();
				else
					aux = aux.getLeft();
				atravessados++;
			}
			return aux;

		}

		private void add(String word, int num) { 
			Node found = lookup(getRoot(), word);
			if (found == null) {
				insert(word, num);
			}
			else {
				found.addOne();
			}
		}
		
		private Node inHere(Node novo, Node cur) {
			novo.setParent(cur.getParent());
			if (cur.getParent()!=null) {
				if (cur == cur.getParent().getLeft())
					cur.getParent().setLeft(novo);
				else
					cur.getParent().setRight(novo);
			}
			else root = novo;
			
			Node aux;
			aux = split(novo, cur);
			novo.setLeft(aux.getLeft());
			novo.setRight(aux.getRight());
			resetCount(novo);
			//actualizar parents
			if (novo.getLeft()!=null)
				novo.getLeft().setParent(novo);
			if (novo.getRight()!=null)
				novo.getRight().setParent(novo);
			return novo;
		}

		
		//Devolve um nó vazio que será substituido pelo novo nó
		//com ponteiros para as novas sub-arvores direita e esquerda
		private Node split(Node novo, Node cur) {
			Node res = new Node();
			if (cur == null) {
				res.setLeft(null);
				res.setRight(null);
				return res;
			}
			
			Node aux;
			if (cur.getWord().compareToIgnoreCase(novo.getWord())>0) {
				//ramo direito correcto, tem que se verificar o esquerdo
				res.setRight(cur);
				cur.setParent(res);
				aux = split(novo, cur.getLeft());
				res.setLeft(aux.getLeft());
				res.getRight().setLeft(aux.getRight());
				if (aux.getLeft()!=null)
					aux.getLeft().setParent(res);
				if (aux.getRight()!=null)
					aux.getRight().setParent(res.getRight());
			}
			
			else {
				res.setLeft(cur);
				cur.setParent(res);
				aux = split(novo, cur.getRight());
				res.setRight(aux.getRight());
				res.getLeft().setRight(aux.getLeft());	
				if (aux.getLeft()!=null)
					aux.getLeft().setParent(res.getLeft());
				if (aux.getRight()!=null)
					aux.getRight().setParent(res);
			}
			return res;
		}
		
		private int resetCount(Node node) {
			if (node == null) return 0;
			node.setCount(resetCount(node.getLeft())+resetCount(node.getRight())+1);
			return node.getCount();
		}
		
		private void insert(String word, int num) {
			Node novo = new Node(word, num,1);
			if (root == null) {
				root = novo;
				return ;
			} //é raiz!

			Node cur = root;
			Random rand = new Random();
			int prob = rand.nextInt(cur.getCount()+1)+1;
			while (true) {
				if (prob>cur.getCount()) {
					inHere(novo,cur);
					break;
				}
				else {
					cur.addCount();
					if (cur.getWord().compareToIgnoreCase(word)>0) {
						if (cur.getLeft()==null) {
							cur.setLeft(novo);
							novo.setParent(cur);
							return;
						}
						cur = cur.getLeft();
					}
					else {
						if (cur.getRight()==null) {
							cur.setRight(novo);
							novo.setParent(cur);
							return;
						}
						cur = cur.getRight();
					}
				}
			}
		}

		/**Removal**/
		public int remove (String word) {
			Node found = lookup(getRoot(), word);
			if (found != null) {
				if ((found.getParent()==null)&&(found.getLeft() == null) && (found.getRight()==null)) {
					setRoot(null); //arvore vazia
					deleted = 0;
				}
				else {//põe contador a zeros
					found.delete();
					deleted++;
				} 
			}
			return deleted;
		}
		
		private void print (Node node) {
			if (node==null) return;
			if (node.getLeft()!=null) print (node.getLeft());
			System.out.println(""+node);
			if (node.getRight()!=null) print(node.getRight());
		}
		
		public void print() {
			print(this.getRoot());
		}
		
	}
	
	private static void copybranch (Node node, Tree nova) {
		if (node == null) return;
		if (node.getNum()!=0) 
			nova.add(node.getWord(), node.getNum());
		copybranch(node.getLeft(), nova);
		copybranch(node.getRight(), nova);
	}
	
	private static Tree copyTree(Tree start) {
		Tree newTree = new Tree();
		copybranch(start.getRoot(), newTree);
		return newTree;
	}
	
	public static void main(String[] args) throws FileNotFoundException{
		Scanner sc = new Scanner(System.in);
		String line = sc.nextLine();
		String word;
		int deleted = 0;
		Tree BR = new Tree();
		
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
						if (deleted>=MAXZERO) {
							Tree aux = copyTree(BR);
							BR = aux;
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
		if (deleted>0){
			Tree aux = copyTree(BR);
			BR = aux;
		}
		BR.print();
		//System.out.println("atravessados: "+BR.atravessados);
	}
}
