
import java.io.File;
import java.io.FileNotFoundException;
import static java.lang.Math.abs;
import java.util.Scanner;
import java.util.StringTokenizer;

public class AVL2 {
	
	private static class Node { 
		private Node left; 
		private Node right; 
		private int num;
		private String word;
		private Node parent;
		private int FE;

		Node(String NewWord) { 
			left = null; 
			right = null; 
			word = NewWord;
			num = 1;
			parent = null;
			FE = 0;
		}
		
		@Override
		public String toString () {
			if (this==null) return "";
			return this.getWord()+": "+this.getNum();
		}
		
		public void addOne() {
			this.num++;
		}
		
		public boolean equals (Node node) {
			if ((this == null)||(node==null)) return false;
			return this.getWord().equals(node.getWord());
		}
		
		public void copy (Node node) {
			this.word = node.getWord();
			this.num = node.getNum();
		}

		public void setFE(int FE) {
			this.FE = FE;
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

		public int getFE() {
			return FE;
		}

		public int getNum() {
			return num;
		}

		public String getWord() {
			return word;
		}
		
	}
	
	private static class Tree {
		private Node root;
		//public int rotacoes = 0;
		//public int atravessados = 0;
		
		/**funções AVL**/
		private int calcHeight (Node node) {
			if (node == null) return -1;

			int left = calcHeight(node.getLeft());
			int right = calcHeight(node.getRight());

			int h;
			if (left>=right) 
				h = left+1;
			else 
				h = right+1;
			node.setFE(left-right);
			return h;
		}

		private int equilibrar (Node node) {
			if ((node == null)) return 0;
			equilibrar(node.getLeft());
			equilibrar(node.getRight());
			calcHeight(node);
			if (node.getFE()>1) {
				if ((node.getLeft()!=null)&&(node.getFE()*node.getLeft().getFE()<0)) {
					if (node.getLeft().getRight()!=null) {
						rotateLeft(node.getLeft());
					}
				}
				rotateRight(node);
				return 1;
			}
			else if (node.getFE()<-1) {
				if ((node.getRight()!=null)&&(node.getFE()*node.getRight().getFE()<0)) {
					if (node.getRight().getLeft()!=null) {
						rotateRight(node.getRight());
					}
				}
				rotateLeft(node);
				return 1;
			}
			return 0;
		}

		private void rotateRight(Node k1) {
			Node k2 = k1.getLeft();
			k1.setLeft(k2.getRight());
			k2.setRight(k1);

			if (k1.getParent() == null)
				setRoot(k2);
			else if (k1.getParent().getRight() == k1)
				k1.getParent().setRight(k2);
			else k1.getParent().setLeft(k2);
			k2.setParent(k1.getParent());
			k1.setParent(k2);

			if (k1.getLeft()!=null)
				k1.getLeft().setParent(k1);
			
			//rotacoes++;
		}

		private void rotateLeft(Node k1) {
			Node k2 = k1.getRight();
			k1.setRight(k2.getLeft());
			k2.setLeft(k1);

			if (k1.getParent() == null)
				setRoot(k2);
			else if (k1.getParent().getRight() == k1)
				k1.getParent().setRight(k2);
			else k1.getParent().setLeft(k2);
			k2.setParent(k1.getParent());
			k1.setParent(k2);
			if (k1.getRight()!=null)
				k1.getRight().setParent(k1);
			
			//rotacoes++;
		}

		/**funções auxiliares**/
		private Node bigger (Node node) {
			Node aux = node;
			while (aux.getRight()!=null) {
				aux = aux.getRight();
				//atravessados++;
			}
			return aux;
		}

		private Node lookup(String word) {
			Node aux = root;
			while ((aux!=null)&&(!word.equals(aux.getWord()))) {
				if (word.compareToIgnoreCase(aux.getWord())>0) 
					aux = aux.getRight();
				else
					aux = aux.getLeft();
				//atravessados++;
			}
			return aux;

		}

		/**adicionar**/
		private void add(String word) { 
			Node found = lookup(word);
			if (found == null) {
				insert(word);
				equilibrar(root);
				calcHeight(root);
				while(abs(root.getFE())>1) {
					equilibrar(root);
					calcHeight(root);
				}
			}
			else {
				found.addOne();
			}
		}

		private void insert(String word) {
			if (root == null) {
				setRoot(new Node(word));
				return;
			} //é raiz!
			Node aux = root;
			while (aux!=null) {
				//atravessados++;
				if (word.compareToIgnoreCase(aux.getWord())<0)
					if (aux.getLeft()==null) {
						aux.setLeft(new Node(word));
						aux.getLeft().setParent(aux);
						break;
					}
					else aux = aux.getLeft();

				else if (word.compareToIgnoreCase(aux.getWord())>0)
					if (aux.getRight()==null) {
						aux.setRight(new Node(word));
						aux.getRight().setParent(aux);
						break;
					}
					else aux = aux.getRight();
			}
		}

		/**remover**/
		private Node remove(String word) {
			Node found = lookup(word);
			if (found == null) return root;
			return deleteNode(root,found);
		}

		private Node deleteNode(Node root, Node remover) {

			if (remover.getParent()==null) { //é raiz
				if (remover.getLeft() == null) {
					if (remover.getRight()==null) return null; //arvore vazia

					remover.getRight().setParent(null); //membro da direita passa a raiz
					return remover.getRight();
				}

				if (remover.getRight() == null) {
					remover.getLeft().setParent(null); //membro da esquerda passa a raiz
					return remover.getLeft();
				}
			}

			Node aux = null;

			Node parent = remover.getParent();

			if ((remover.getRight()==null)||(remover.getLeft()==null)) { //só tem um ramo ou nenhum
				//determinar qual o ramo que tem
				if (remover.getLeft()==null) aux = remover.getRight();
				if (remover.getRight()==null) aux = remover.getLeft();
				if (aux!=null) aux.setParent(parent); //actualizar pai do filho que fica

				if ((parent.getLeft()!=null) && (parent.getLeft().equals(remover)))//está à esquerda do pai
					parent.setLeft(aux);
				else if (parent.getRight()!=null)
					parent.setRight(aux);
			}

			else { //tem ambos os filhos
				aux = bigger(remover.getLeft());
				remover.copy(aux);
				deleteNode (root, aux);
			}
			equilibrar(root);
			return root;
		}
		
		private void print (Node node) {
			if (node==null) return;
			if (node.getLeft()!=null) print (node.getLeft());
			System.out.println(""+node);
			if (node.getRight()!=null) print(node.getRight());
		}
		
		public void print() {
			print(this.root);
		}

		/**
		 * @param root the root to set
		 */
		public void setRoot(Node root) {
			this.root = root;
		}
	}
	
	
	
	public static void main(String[] args) throws FileNotFoundException{
		//new File("C:\\Users\\Rosa\\Desktop\\T1.txt")
		Scanner sc = new Scanner(System.in);
		String line = sc.nextLine();
		String word;
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
						BR.setRoot(BR.remove(word));
					}
				}
				else {
					word = word.toLowerCase();
					BR.add (word);
				}
			}
			line = sc.nextLine();
		}
		BR.print();
	}
}

