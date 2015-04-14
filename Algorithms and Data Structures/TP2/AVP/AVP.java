import java.util.Scanner;
import java.util.StringTokenizer;

public class AVP {
 
	private static Node root;
	
	private static class Node { 
		private Node left; 
		private Node right;
		private Node parent;
		private int num;
		private String word;
		private boolean red;
		private boolean pLeft; //true if it is the left of the parent

		Node(String NewWord) { 
			left = null; 
			right = null;
			parent = null;
			pLeft = false;
			red = true;
			word = NewWord;
			num = 1;
			
		}
		
		public void addOne () {
			this.num++;
		}
		
		@Override
		public String toString () {
			if (this==null) return "";
			//return this.getWord()+": "+this.getNum();
			//for debug
			String out;
			if (root==this) out = "RAIZ------->";
			else out = this.getParent().getWord()+"-->";
			return out+this.getWord()+": "+this.getNum()+" Red: "+this.isRed()+" Left: "+this.isLeft();
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
		
		public void copy (Node node) {
			this.num = node.getNum();
			this.word = node.getWord();
		}
		
		public boolean hasNoChildren () {
			return (this.left==null) && (this.right == null);
		}
		
		public boolean hasOneChild() {
			return (this.left==null) != (this.right == null);
		}
		
		public boolean blackChildren () {
			if ((this.left==null) || (this.right == null)) return false;
			return (!this.left.isRed()) && (!this.right.isRed());
		}
		
		public boolean redChildren () {
			if ((this.left==null) || (this.right == null)) return false;
			return (this.left.isRed()) && (this.right.isRed());
		}
	}
	
	/**Insertion "aids"**/
	private static void checkColors (Node node) {
		if (node.isRed() && node.getParent().isRed()) {
			if (node.getParent().getParent().redChildren()) {
				flipColor(node.getParent().getParent());
				System.out.println("After coloring nodes");
				print(root);
				System.out.println("");
			}
			else {
				if (node.isLeft()) {
					Node aux = node.getParent();
					rotateRight(node.getParent());
					if (!aux.isLeft()) {
						rotateLeft(node.getParent());
						flipColor(node);
					}
				}
				else {
					Node aux = node.getParent();
					rotateLeft(node.getParent());
					if (!aux.isLeft()) {
						rotateRight(node.getParent());
						flipColor(node);
					}
				}
				System.out.println("After rotations:");
				print(root);
				System.out.println("");
			}
		}
	}
	
	private static void flipColor (Node node) {
		node.getLeft().setRed(node.isRed());
		node.getRight().setRed(node.isRed());
		node.setRed(!node.isRed());
	}
	
	private static Node bigger (Node node) { //devolve o maior nó da árvore cuja raiz é node
		Node aux = node;
		while (aux.getRight()!=null) {
			aux = aux.getRight();
		}
		return aux;
	}
	
	/**Rotations**/
	private static void rotateRight(Node k1) {
		Node k2 = k1.getLeft();
		k1.setLeft(k2.getRight());
		if (k1.getLeft()!=null)
			k1.getLeft().setLeft(true);
		k2.setRight(k1);
		k1.setLeft(false);

		if (k1.getParent() == null)
			root = k2;
		else if (!k1.isLeft()) {
			k1.getParent().setRight(k2);
			k2.setLeft(false);
		}
		else k1.getParent().setLeft(k2);
		
		k2.setParent(k1.getParent());
		k1.setParent(k2);
		if (k1.getLeft()!=null)
			k1.getLeft().setParent(k1);
	}
	
	private static void rotateLeft(Node k1) {
		Node k2 = k1.getRight();
		k1.setRight(k2.getLeft());
		if (k1.getRight()!=null)
			k1.getRight().setLeft(false);
		k2.setLeft(k1);
		k1.setLeft(true);
		
		if (k1.getParent() == null) //k1 é raiz
			root = k2;
		
		else if (k1.isLeft()) {
			k1.getParent().setLeft(k2);
			k2.setLeft(true);
		}
		else k1.getParent().setRight(k2);
		
		k2.setParent(k1.getParent());
		k1.setParent(k2);
		if (k1.getRight()!=null)
			k1.getRight().setParent(k1);
	}
	
	/**Deletion "aids"**/
	
	
	private static Node lookup(Node node, String word) { //Devolve o nó com a palavra word se existir
		Node aux = node;
		while ((aux!=null)&&(!word.equals(aux.getWord()))) {
			if (word.compareToIgnoreCase(aux.getWord())>0) 
				aux = aux.getRight();
			else
				aux = aux.getLeft();
		}
		return aux;
		
	}
 
	private static Node add(String word) { 
		Node found = lookup(root, word);
		if (found == null) {
			root = insert(root, word);
			if (root.isRed()) root.setRed(false);
		}
		else {
			found.addOne();
		}
		return root;
	}
 
	private static Node insert(Node node, String word) {
		if (node == null) {
			Node aux = new Node(word);
			aux.setRed(false);
			return aux;
		} //é raiz!
		
		if (word.compareToIgnoreCase(node.getWord())<0)
			if (node.getLeft()==null) {
				node.setLeft(new Node(word));
				node.getLeft().setParent(node);
				node.getLeft().setLeft(true);
				
				System.out.println("Inserted: "+node.getLeft().getWord());
				print(root);
				System.out.println("");
				if (node.isRed()) {
					checkColors(node.getLeft());
				}
				
			}
			else insert(node.getLeft(), word);
		
		else if (word.compareToIgnoreCase(node.getWord())>0)
			if (node.getRight()==null) {
				node.setRight(new Node(word));
				node.getRight().setParent(node);
				
				System.out.println("Inserted: "+node.getRight().getWord());
				print(root);
				System.out.println("");
				if (node.isRed())	checkColors(node.getRight());
			}
			else insert(node.getRight(), word);
		if (node.getParent()!=null) return node.getParent();
		else return node;
	}

	private static Node remove(String word) {
		Node found = lookup(root, word);
		if (found == null) return root;
		return deleteNode(found);
	}
	
 	private static Node deleteNode(Node remover) {
		if (remover.isRed()) return deleteRed(remover); //apagar vermelho - remoção normal
		
		if (remover.hasNoChildren()) {
			if (remover.getParent()==null)
				return null; //raiz sem filhos
			//senão é folha
			
			//*******************TO DO**************************//
		}
		
		if (remover.hasOneChild()) { //só tem um filho - tem de ser folha vermelha
			Node aux;
			if (remover.getLeft() == null) {
				aux = remover.getRight();
			}
			else {
				aux = remover.getLeft();
			}
			
			aux.setRed(false); //passa a preto
			aux.setParent(remover.getParent()); //actualiza ponteiro para pai
			
			if (remover.getParent()!=null) { //se não for raiz actualiza ponteiro do pai
				if (remover.isLeft()) remover.getParent().setLeft(aux);
				else remover.getParent().setRight(aux);
				return root;
			}
			else return null;
		}
		
		else { //tem dois filhos
			Node sibling; //ver irmão
			if (remover.isLeft()) sibling = remover.getParent().getRight();
			else sibling = remover.getParent().getRight();
				
			if (remover.blackChildren()) {
				if (sibling.blackChildren()) {
					
				}
			}
			else if (remover.redChildren()) {
				//bleble
			}
			else {//one is red and one is black
				
				
			}
		}
		
		return root;
	}
	
	private static Node deleteRed(Node remover) {
		if (remover.getRight()==null) { //nao é preciso testar esquerda porque nunca vai ter só um filho
			if (remover.isLeft())
				remover.getParent().setLeft(null);
			else remover.getParent().setRight(null);
		}
		
		//se não for folha é nó com 2 filhos pretos
		Node aux = bigger(remover.getLeft());
		remover.copy(aux);
		deleteNode (aux);
		return null;
	}
	
	private static void print (Node node) {
		if (node==null) return;
		if (node.getLeft()!=null) print (node.getLeft());
		System.out.println(""+node);
		if (node.getRight()!=null) print(node.getRight());
	}
	
	public static void main(String[] args){
		root = null;
		Scanner sc = new Scanner(System.in);
		String line = sc.nextLine();
		String word;
		
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
						root = remove(word);
					}
				}
				else {
					word = word.toLowerCase();
					root = add (word);
					print(root);
					System.out.println("\n");
				}
			}
			line = sc.nextLine();
		}
		//print(root);
	}
}
