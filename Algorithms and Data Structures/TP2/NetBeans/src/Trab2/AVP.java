package Trab2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class AVP {
	
	final static int MAXZERO = 300;

	private static class Node { 
		private Node left; 
		private Node right;
		private Node parent;
		private int num;
		private String word;
		private boolean red;
		private boolean pLeft; //true if it is the left of the parent

		Node(String NewWord, int num) { 
			this.left = null; 
			this.right = null;
			this.parent = null;
			this.pLeft = false;
			this.red = true;
			this.word = NewWord;
			this.num = num;
			
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
			return this.getWord()+": "+this.getNum();
			//for debug
			/**String out;
			if (this.getParent()==null) out = "RAIZ------->";
			else out = this.getParent().getWord()+"-->";
			return out+this.getWord()+": "+this.getNum()+" Red: "+this.isRed()+" Left: "+this.isLeft();**/
		}
		
		public boolean equals (Node node) {
			if ((this == null)||(node==null)) return false;
			return this.getWord().equals(node.getWord());
		}

		/**
		 * @return the red
		 */
		public boolean isRed() {
			return red;
		}

		public void setRed(boolean red) {
			this.red = red;
		}

		public boolean isLeft() {
			return pLeft;
		}

		public void setLeft(boolean pLeft) {
			this.pLeft = pLeft;
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
		
		public Node getSibling () {
			if (this.parent==null) return null;
			if (this.isLeft()) return this.getParent().getRight();
			else return this.getParent().getLeft();
		}
		
		public void delete() {
			this.num = 0;
		}
		
	}
	
	private static class Tree {
		public int atravessados = 0;
		public int rotacoes = 0;
		private int deleted;
		private Node root;

		Tree (){
			root = null;
			deleted = 0;
		}
	
		public Node getRoot() {
			return root;
		}

		public void setRoot(Node root) {
			this.root = root;
		}
		
		/**Insertion "aids"**/
	
		private void checkColors (Node node, Node child) {
			Node current = node;
			Node prev = child;
			while ((current!=null)&&(current.getParent()!=null)&&(current.isRed())) {
				Node sibling = current.getSibling();
				Node parent = current.getParent();
				//father is black and sibling is red
				if ((sibling!=null)&&(sibling.isRed())) {
					flipColor(parent);
					if ((parent.getParent()!=null)&&(parent.getParent().isRed())) {
						prev = parent;
						current = parent.getParent();
						atravessados++;
					}
					else break;
				}
				else {
					if (prev.isLeft()&&current.isLeft()) { //child on the same side as the parent
						//System.out.println("caso 1A");
						rotateRight(parent);
						rotacoes++;
						current.setRed(false);
					}
					else if (!prev.isLeft() && !current.isLeft()) {
						//System.out.println("caso 1B");
						rotateLeft(current.getParent());
						rotacoes++;
						current.setRed(false);
					}
					else {
						if (prev.isLeft()) {
							//System.out.println("caso 2");
							rotateRight(current);
							rotateLeft(parent);
							rotacoes+=2;
						}
						else {
							//System.out.println("caso 3");
							rotateLeft(current);
							rotateRight(prev.getParent());
							rotacoes+=2;
						}
						prev.setRed(false);
					}
					parent.setRed(true);
					break;
				}
			}
			root.setRed(false);
		}

		private void flipColor (Node node) {
			node.getLeft().setRed(node.isRed());
			node.getRight().setRed(node.isRed());
			node.setRed(!node.isRed());
		}

		/**Rotations**/
		private void rotateRight(Node k1) {
			Node k2 = k1.getLeft();
			k1.setLeft(k2.getRight());
			k2.setRight(k1);
			
			//actualizar booleanos pLeft
			if (k1.getLeft()!=null)
				k1.getLeft().setLeft(true);
			k2.setLeft(k1.isLeft());
			k1.setLeft(false);

			if (k1.getParent() == null)
				root = k2;
			else if (!k2.isLeft())
				k1.getParent().setRight(k2);
			else k1.getParent().setLeft(k2);
			k2.setParent(k1.getParent());
			k1.setParent(k2);

			if (k1.getLeft()!=null)
				k1.getLeft().setParent(k1);
		}

		private void rotateLeft(Node k1) {
			Node k2 = k1.getRight();
			k1.setRight(k2.getLeft());
			k2.setLeft(k1);

			//actualizar booleanos pLeft
			if (k1.getRight()!=null)
				k1.getRight().setLeft(false);
			k2.setLeft(k1.isLeft());
			k1.setLeft(true);
			
			if (k1.getParent() == null)
				root = k2;
			else if (!k2.isLeft())
				k1.getParent().setRight(k2);
			else k1.getParent().setLeft(k2);
			k2.setParent(k1.getParent());
			k1.setParent(k2);
			if (k1.getRight()!=null)
				k1.getRight().setParent(k1);
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
				if (getRoot().isRed()) getRoot().setRed(false);
			}
			else {
				found.addOne();
			}
		}

		private void insert(String word, int num) {
			if (root == null) {
				Node aux = new Node(word, num);
				aux.setRed(false);
				root = aux;
			} //é raiz!
			Node node = root;
			
			while(node!=null) {
				atravessados++;
				if (word.compareToIgnoreCase(node.getWord())<0)
					if (node.getLeft()==null) {
						node.setLeft(new Node(word, num));
						node.getLeft().setParent(node);
						node.getLeft().setLeft(true);

						/**System.out.println("Inserted: "+node.getLeft().getWord());
						print(root);
						System.out.println("");**/
						if (node.isRed()) {
							checkColors(node, node.getLeft());
						}
						break;
					}
					else node = node.getLeft();

				else if (word.compareToIgnoreCase(node.getWord())>0)
					if (node.getRight()==null) {
						node.setRight(new Node(word, num));
						node.getRight().setParent(node);

						/**System.out.println("Inserted: "+node.getRight().getWord());
						print(root);
						System.out.println("");**/
						if (node.isRed())	
							checkColors(node, node.getRight());
						break;
					}
					else node = node.getRight();
				else break;
			}
		}

		/**Removal**/
		public int remove (String word) {
			Node found = lookup(getRoot(), word);
			if (found != null) {
				if ((found.getParent()==null)&&(found.getLeft() == null) && (found.getRight()==null)) {
					setRoot(null); //arvore vazia
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
					//BR.print();
					//System.out.println("\n");
				}
			}
			line = sc.nextLine();
		}
		if (deleted>0){
			Tree aux = copyTree(BR);
			BR = aux;
		}
		BR.print();
	}
}
