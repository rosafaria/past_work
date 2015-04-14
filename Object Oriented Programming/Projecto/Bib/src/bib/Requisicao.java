package bib;

import java.io.Serializable;
import java.util.Calendar;

/** Classe que representa uma requisição.
 * <p> Liga um leitor a um artigo, com data em que foi feita, 
 * data em que é suposto entregar e data em que de facto foi entregue
 * @author André Baptista (2012137523)
 * @author Rosa Faria (2005128014)
 */
public class Requisicao implements Serializable{
	private Leitor leitor;
	private Artigo artigo;
	private Calendario dataInicio;
	private Calendario dataFim;
	private Calendario dataEntrega;
	static final long serialVersionUID = 3398822842236017323L;
	
	/** Construtor do tipo definindo o Leitor, artigo e datas de início e fim
	 * @param leitor Leitor que fez requisição
	 * @param artigo Artigo que requisita
	 * @param data Data de criação da Requisição
	 */
	public Requisicao(Leitor leitor, Artigo artigo, Calendar data) {
		this.leitor = leitor;
		this.artigo = artigo;
		this.dataInicio = new Calendario(data);
		this.dataFim = new Calendario((Calendar)data.clone());
		this.dataFim.getCalendar().add(Calendar.DAY_OF_MONTH,leitor.getDuracao());
	}

	/** Método para entregar artigo.
	 * <p> Põe no atributo data de entrega a data de Hoje
	 */
	public void entregar () {
		this.dataEntrega = new Calendario(Calendar.getInstance());
	}
	
	/** Getter para o atributo artigo
	 * @return artigo requisitado
	 */
	public Artigo getArtigo() {
		return this.artigo;
	}

	/**  Getter para a data da criação da requisição
	 * @return data da requisição
	 */
	public Calendario getDataInicio() {
		return this.dataInicio;
	}

	/** Getter para a data limite da requisição
	 * @return data limite da requisição
	 */
	public Calendario getDataFim() {
		return this.dataFim;
	}
	
	/** Getter para o atributo leitor
	 * @return Leitor que fez a requisição
	 */
	public Leitor getLeitor() {
		return leitor;
	}

	/** Getter para o atributo data de Entrega
	 * @return data de entrega do artigo
	 */
	public Calendario getDataEntrega() {
		return dataEntrega;
	}	
	
	@Override
	public String toString() {
		String output = "";
		if (this.getDataEntrega() == null) output+= "Não entregue: ";
		else output+= "Entregue: ";
		output +=""+this.getLeitor().getNome() + " || \"" + this.artigo.getTitulo() + "\"";
		if (this.getDataEntrega() == null) output+="|| Termina a "+this.getDataFim();
		return output;
	}

}
