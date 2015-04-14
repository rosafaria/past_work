package bib;

import java.io.Serializable;

/** Classe abstracta Artigo que representa cada item existente na Biblioteca
 * @author André Baptista (2012137523)
 * @author Rosa Faria (2005128014)
 */
public abstract class Artigo implements Serializable {
	private String titulo;
	private int anoPublicacao;
	private String editora;
	private int nCopias;
	private int nRequisitados;
	static final long serialVersionUID = -5889329237349479458L;
	
	/** Construtor vazio */
	public Artigo(){}

	/** Construtor de tipo que define alguns atributos do objecto
	 * @param titulo Titulo da obra
	 * @param ano Ano de publicação
	 * @param editora Editora
	 * @param nCopias Número de cópias existentes
	 */
	public Artigo (String titulo, int ano, String editora, int nCopias) {
		this.titulo = titulo;
		this.anoPublicacao = ano;
		this.editora = editora;
		this.nCopias = nCopias;
	}
	
	/** Método para incrementar contador de número de cópias requisitadas
	 * @return -1 se já não houver cópias disponíveis, 1 se a requisicão foi bem sucedida
	 */
	public int addReq() {
		if (this.getnRequisitados() >= this.getnCopias()) return -1;
		this.setnRequisitados(this.getnRequisitados() + 1);
		return 1;
	}

	/** Método para decrementar contador de número de cópias requisitadas
	 * @return -1 se não houver nenhuma cópia requisitada, 1 se a remoção foi bem sucedida
	 */
	public int removeReq() {
		if (this.getnRequisitados() <=0) return -1;
		this.setnRequisitados(this.getnRequisitados() - 1);
		return 1;
	}

	/** Método que calcula o nº de cópias disponíveis
	 * @return nº de cópias disponíveis
	 */	
	public int availCopy() {
		return (this.getnCopias()-this.getnRequisitados());
	}

	@Override
	public String toString() {
		return "Título: " + this.getTitulo() +", ano: " + this.getAnoPublicacao() + ", editora: " + this.getEditora() + ", número de copias: " + this.getnCopias();
	}

	abstract boolean equals(Artigo a);

	/** Getter para o atributo título
	 * @return título do artigo
	 */
	public String getTitulo() {
		return this.titulo;
	}

	/** Getter para o atributo editora
	 * @return editora do artigo
	 */
	public String getEditora() {
		return this.editora;
	}

	/**
	 * Getter para o atributo nº de cópias
	 * @return nº de cópias existentes do artigo
	 */
	public int getnCopias() {
		return this.nCopias;
	}
		
	/** Getter para o atributo ano de publicação
	 * @return ano de publicação
	 */
	public int getAnoPublicacao() {
		return anoPublicacao;
	}

	/** Getter para o atributo nº de cópias requisitadas
	 * @return nº de cópias requisitadas
	 */
	public int getnRequisitados() {
		return nRequisitados;
	}

	/** Setter para o atributo titulo
	 * @param titulo Titulo da obra
	 */
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	/** Setter para o atributo ano de publicação
	 * @param anoPublicacao Ano de publicação da obra
	 */
	public void setAnoPublicacao(int anoPublicacao) {
		this.anoPublicacao = anoPublicacao;
	}

	/** Setter para o atributo editora
	 * @param editora Editora
	 */
	public void setEditora(String editora) {
		this.editora = editora;
	}

	/** Setter para o atributo nCopias
	 * @param nCopias Nº de cópias existentes
	 */
	public void setnCopias(int nCopias) {
		this.nCopias = nCopias;
	}

	/** Setter para o numero de atributos requisitados
	 * @param nRequisitados Nº de cópias requisitadas
	 */
	public void setnRequisitados(int nRequisitados) {
		this.nRequisitados = nRequisitados;
	}
}
