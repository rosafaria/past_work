package aedois;

import java.util.Scanner;
import java.util.StringTokenizer;

public class AVP3 {
	
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
		
		public boolean blackChildren() {
			return (left!=null && right!= null && !left.isRed() && !right.isRed());
		}
		
		public void copy (Node aux) {
			word = aux.getWord();
			num = aux.getNum();
		}
		
	}
	
	private static class Tree {
	
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
					}
					else break;
				}
				else {
					if (prev.isLeft()&&current.isLeft()) { //child on the same side as the parent
						System.out.println("caso 1A");
						rotateRight(parent);
						current.setRed(false);
						//prev = current;
					}
					else if (!prev.isLeft() && !current.isLeft()) {
						System.out.println("caso 1B");
						rotateLeft(current.getParent());
						current.setRed(false);
						//prev = current;
					}
					else {
						if (prev.isLeft()) {
							System.out.println("caso 2");
							rotateRight(current);
							rotateLeft(parent);
						}
						else {
							System.out.println("caso 3");
							rotateLeft(current);
							rotateRight(prev.getParent());
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
			}
			return aux;

		}

		private void add(String word, int num) { 
			Node found = lookup(getRoot(), word);
			if (found == null) {
				setRoot(insert(getRoot(), word, num));
				if (getRoot().isRed()) getRoot().setRed(false);
			}
			else {
				found.addOne();
			}
		}

		private Node insert(Node node, String word, int num) {
			if (node == null) {
				Node aux = new Node(word, num);
				aux.setRed(false);
				return aux;
			} //é raiz!

			if (word.compareToIgnoreCase(node.getWord())<0)
				if (node.getLeft()==null) {
					node.setLeft(new Node(word, num));
					node.getLeft().setParent(node);
					node.getLeft().setLeft(true);

					System.out.println("Inserted: "+node.getLeft().getWord());
					print(root);
					System.out.println("");
					if (node.isRed()) {
						checkColors(node, node.getLeft());
					}
				}
				else insert(node.getLeft(), word, num);

			else if (word.compareToIgnoreCase(node.getWord())>0)
				if (node.getRight()==null) {
					node.setRight(new Node(word, num));
					node.getRight().setParent(node);

					System.out.println("Inserted: "+node.getRight().getWord());
					print(root);
					System.out.println("");
					if (node.isRed())	
						checkColors(node, node.getRight());
				}
				else insert(node.getRight(), word, num);
			if (node.getParent()!=null) return node.getParent();
			else return node;
		}

		/**Removal**/
		
		private static Node whereToGo (Node node, String word) {
			Node X = node;
			Node r = X.getRight();
			Node l = X.getLeft();
			if (word.compareToIgnoreCase(X.getWord())>0) {
				if (l.getWord().compareToIgnoreCase(X.getWord())>0)
					return l;
				return r;
			}
			return l;
		}


		public void remove (Node start, String word) {
			Node found = lookup(root, word);
			if (found == null) return ;
			if ((found.getParent()==null)&&(found.getLeft() == null) && (found.getRight()==null)) {
				root = null; //arvore vazia
				return;
			}
			if (found.getLeft()!=null) {
				Node aux = bigger(found.getLeft());
				found.copy(aux);
				aux.setWord(word);
				print(root);
			} 
			step2(root,word);
		}

		private void step2 (Node start, String word) {
			if (start == null) return;
			Node X = start;
			while(!X.getWord().equals(word)) {
				if (X.blackChildren()) {
					X.setRed(true);
					X = whereToGo(X,word);
				}
				else {
					step2B(X, word);
					return;
				}
			}
			step2A(X);
		}

		private void step2A(Node X) {
			//X has 2 black children
			Node T = X.getSibling();
			if (T == null) {
				deleteNode(X);
				return;
			}
			Node P = X.getParent();
			if (T.blackChildren()) {
				System.out.println("case 1");
				X.setRed(true);
				T.setRed(true);
				P.setRed(false);
			}
			else if ((T.getLeft()!=null) && (T.getLeft().isRed())) {
				System.out.println("case 2");
				rotateLeft(T);
				rotateRight(P);
				X.setRed(true);
				P.setRed(false);
			}
			else //if ((T.getRight()!=null) && (T.getRight().isRed())){
			{
				System.out.println("case 3");
				rotateLeft(P);
				X.setRed(true);
				P.setRed(false);
				T.setRed(true);
			}
			deleteNode(X);
		}

		private void step2B (Node newRoot, String word) {
			//X has at least one red child
			Node X = newRoot;
			X = whereToGo(X,word);

			while (X.isRed() && !X.getWord().equals(word)) {
				System.out.println("2B -> X: "+X);
				X = whereToGo(X,word);
			}
			if (!X.isRed()) {
				Node P = X.getParent();
				Node T = X.getSibling();
				System.out.println("X: "+X);
				if (X.isLeft()) {
					rotateLeft(P);
					P.setRed(true);
					T.setRed(false);
				}
				else {
					rotateRight(P);
					P.setRed(true);
					T.setRed(false);
				}
				step2(X, word);
			}
		}

		private void deleteNode (Node node) {
			if (node == null) return;

			System.out.println("Finally: ");
			print(root);
			System.out.println("\n");
			if (node.isLeft()) {
				if (node.getLeft()!=null) {
					node.getLeft().setParent(node.getParent());
					node.getLeft().setRed(false);
					node.getParent().setLeft(node.getLeft());
				}
				else if (node.getRight()!=null) {
					node.getRight().setParent(node.getParent());
					node.getRight().setRed(false);
					node.getRight().setLeft(true);
					node.getParent().setLeft(node.getRight());
				}
				else node.getParent().setLeft(null);
			}
			else {
				if (node.getLeft()!=null) {
					node.getLeft().setParent(node.getParent());
					node.getLeft().setLeft(false);
					node.getParent().setRight(node.getLeft());
				}
				else if (node.getRight()!=null) {
					node.getRight().setParent(node.getParent());
					node.getParent().setRight(node.getRight());
				}
				else node.getParent().setRight(null);

			}
			root.setRed(false);
		}

		private Node bigger (Node x) {
			Node node = x;
			while (node.getRight()!= null) {
				node = node.getRight();
			}
			return node;
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
	
	public static void main(String[] args){
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
						BR.remove(BR.root, word);
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
