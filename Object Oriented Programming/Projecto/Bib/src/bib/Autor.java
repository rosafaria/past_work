package bib;

import java.io.Serializable;

/**Classe que representa um autor de pelo menos uma obra incluída na Biblioteca
 * @author André Baptista (2012137523)
 * @author Rosa Faria (2005128014)
 */
public class Autor implements Serializable{
	private final String nome;
	static final long serialVersionUID = -5315745972852757639L;
	
	/** Construtor do tipo que define o nome do objecto
	 * @param nome Nome do autor
	 */
	public Autor(String nome) {
		this.nome = nome;
	}

	/** Getter para o atributo nome
	 * @return nome do autor
	 */
	public String getNome() {
		return this.nome;
	}

	@Override
	public String toString() {
		return this.nome;
	}

	/** Método que compara dois autores pelo nome
	 * @param autor Autor a comparar com o que chamou o método
	 * @return true se nomes dos autores são iguais, false caso contrário.
	 */
	public boolean equals (Autor autor) {
		return this.nome.equals(autor.getNome());
	}
}
