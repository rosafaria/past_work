package bib;

import java.io.Serializable;

/** Classe que representa um Artigo do tipo DVD.
 * @author André Baptista (2012137523)
 * @author Rosa Faria (2005128014)
 */
public class DVD extends Artigo implements Serializable{
	private int duracao;
	private String realizador;
	static final long serialVersionUID = -7743761448586186551L;
	
	/** Construtor vazio */
	public DVD () {}
	
	/** Construtor do tipo que define os vários atributos do artigo
	 * @param titulo Titulo da obra
	 * @param ano Ano de publicação
	 * @param editora Editora
	 * @param nCopias Nº de cópias
	 * @param duracao Duração do DVD
	 * @param realizador Realizador do DVD
	 */
	public DVD(String titulo, int ano, String editora, int nCopias, int duracao, String realizador) {
		super(titulo, ano, editora, nCopias);
		this.duracao = duracao;
		this.realizador = realizador;
	}

	/** Getter para o atributo duracao
	 * @return duração do DVD
	 */
	public int getDuracao() {
		return this.duracao;
	}

	/** Getter para o atributo realizador
	 * @return nome do realizador
	 */
	public String getRealizador() {
		return this.realizador;
	}

	/** Setter para o parâmetro duração
	 * @param duracao Duração do DVD
	 */
	public void setDuracao(int duracao) {
		this.duracao = duracao;
	}

	/** Setter para o parâmetro realizador
	 * @param realizador Realizador do DVD
	 */
	public void setRealizador(String realizador) {
		this.realizador = realizador;
	}
	
	@Override
	public String toString() {
		return "\"" + this.getTitulo() +"\" ("+ this.getAnoPublicacao() + ") de "+this.getRealizador()+", " + this.getDuracao()+" min";
	}

	/** Método que compara dois DVDs
	 * @param artigo Artigo a comparar
	 * @return true se forem iguais, false caso contrário
	 */
	@Override
	public boolean equals(Artigo artigo) {
		DVD dvd = (DVD) artigo;
		return (this.getTitulo().equals(dvd.getTitulo()))&&(this.getRealizador().equals(dvd.getRealizador()))&&(this.getAnoPublicacao() == dvd.getAnoPublicacao())&&(this.getEditora().equals(dvd.getEditora()))&&(this.getDuracao() == dvd.getDuracao());
	}
	
	/** Método para criar a String que vai ser guardada no ficheiro
	 * @return String a imprimir
	 */
	public String toFile() {
		String output=this.getTitulo()+"\n"+this.getEditora()+"\n"+this.getAnoPublicacao()+"\n"+this.getnCopias()+"\n"+this.getDuracao()+"\n"+this.getRealizador();
		return output+"\n";
	}
}
