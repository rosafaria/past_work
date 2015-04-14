package Trab2;

import java.util.Scanner;
import java.util.StringTokenizer;

public class Binary {
	
	private static class Node { 
		private Node left; 
		private Node right; 
		private int num;
		private String word;
		private Node parent;

		Node(String NewWord) { 
			left = null; 
			right = null; 
			word = NewWord;
			num = 1;
			parent = null;
		}
		
		@Override
		public String toString () {
			if (this==null) return "";
			return this.getWord()+": "+this.getNum();
		}
		
		public boolean equals (Node node) {
			if ((this == null)||(node==null)) return false;
			return this.getWord().equals(node.getWord());
		}
		
		public void copy (Node node) {
			this.word = node.getWord();
			this.num = node.getNum();
		}
		
		public void addOne() {
			this.num ++;
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
		
	}
	
	private static class Tree {
		private Node root;
		
		private Node bigger (Node node) {
			Node aux = node;
			while (aux.getRight()!=null) {
				aux = aux.getRight();
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
			}
			return aux;

		}

		private void add(String word) { 
			Node found = lookup(word);
			if (found == null) {
				insert(word);
			}
			else {
				found.addOne();
			}
		}

		private void insert(String word) {
			if (root == null) {
				root = new Node(word);
				return;
			} //é raiz!
			Node aux = root;
			while (aux!=null) {
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

		private Node remove(String word) {
			Node found = lookup(word);
			if (found == null) return root;
			return deleteNode(found);
		}

		private Node deleteNode(Node remover) {

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
				deleteNode (aux);
			}
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
		
	}
	
	public static void main(String[] args){
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
						BR.remove(word);
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
