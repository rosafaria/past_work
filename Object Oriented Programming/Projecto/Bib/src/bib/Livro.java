package bib;

import java.io.Serializable;
import java.util.ArrayList;

/** Classe que representa um Artigo do tipo Livro.
 * @author André Baptista (2012137523)
 * @author Rosa Faria (2005128014)
 */
public class Livro extends Artigo implements Serializable {
	private String cota;
	private String isbn;
	private ArrayList <Autor> autores;
	static final long serialVersionUID = -1631655505216644749L;
	
	/** Construtor vazio */
	public Livro(){}

	/**  Construtor do tipo que define as características do Artigo
	 * @param titulo Titulo do livro
	 * @param ano Ano de publicação
	 * @param editora Editora
	 * @param nCopias Número de cópias existentes
	 * @param cota Cota
	 * @param isbn ISBN 
	 * @param autores Lista de autores
	 */
	
	
	public Livro(String titulo, int ano, String editora, int nCopias, String cota, String isbn, ArrayList <Autor> autores) {
		super(titulo,ano,editora,nCopias);
		this.cota = cota;
		this.isbn = isbn;
		this.autores = autores;
	}

	/** Getter para o atributo cota
	 * @return cota do Livro
	 */
	public String getCota() {
		return this.cota;
	}

	/** Getter para o atributo ISBN
	 * @return ISBN do livro
	 */	
	public String getIsbn() {
		return this.isbn;
	}

	/** Getter para o atributo autores
	 * @return ArrayList dos autores
	 */
	public ArrayList<Autor> getAutores() {
		return this.autores;
	}

	/** Método para comparar o livro com outro livro
	 * @param artigo Livro a ser comparado
	 * @return true se forem iguais, false caso contrario
	 */
	@Override
    public boolean equals(Artigo artigo){
		Livro livro = (Livro) artigo;
		return ((this.getTitulo().equals(livro.getTitulo())) && (this.getAnoPublicacao() == livro.getAnoPublicacao()) && (this.getEditora().equals(livro.getEditora())) && (this.getCota().equals(livro.getCota())) && (this.getIsbn().equals(livro.getIsbn())));
	}

	@Override
	public String toString() {
		String output =  "\"" + this.getTitulo() +"\" (" + this.getAnoPublicacao() + ")";
		for (int i=0;i<this.getAutores().size();i++){
			output += "; " + this.getAutores().get(i);
		}
		output+=", Cota: "+this.getCota();
		return output;
	}
	
	
	/** Método para criar a String que vai ser guardada no ficheiro
	 * @return String a imprimir
	 */
	public String toFile() {
		String output=this.getTitulo()+"\n"+this.getEditora()+"\n"+this.getAnoPublicacao()+"\n"+this.getnCopias()+"\n"+this.getCota()+"\n"+getIsbn()+"\n"+this.getAutores().size();
		for  (int i = 0; i<this.getAutores().size();i++)
			output+="\n"+this.getAutores().get(i);
		return output+"\n";
	}

	/** Setter para o atributo Cota
	 * @param cota cota do Livro
	 */
	public void setCota(String cota) {
		this.cota = cota;
	}

	/** Setter para o atributo ISBN
	 * @param isbn ISBN do Livro
	 */
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	/** Setter para o atributo com a lista de autores
	 * @param autores ArrayList<Autor> com os autores do Livro
	 */
	public void setAutores(ArrayList <Autor> autores) {
		this.autores = autores;
	}
}
