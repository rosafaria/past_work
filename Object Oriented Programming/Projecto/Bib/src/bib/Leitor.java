package bib;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/** Classe que representa um Leitor.
 * <p> Com permissões para requisitar, entregar e consultar Artigos
 * @author André Baptista (2012137523)
 * @author Rosa Faria (2005128014)
 */
public class Leitor extends Utilizador implements Serializable {
	private Boolean tipo;
	private Requisicao[] requisicoes;
	private int nReqs;
	static final long serialVersionUID = -4185566377128093394L;

	/** Construtor vazio */
	public Leitor(){}
	
	/** Construtor do tipo que define os dados e nº de identificação do leitor
	 * @param nome Nome do utilizador
	 * @param morada Morada do utilizador
	 * @param telefone Telefone do utilizador
	 * @param email E-mail do utilizador
	 * @param data Data do utilizador
	 * @param pass Palavra-chave do utilizador
	 * @param tipo Tipo (true - professor, false - aluno) de Leitor
	 * @param nInterno Nº interno do Leitor
	 */
	public Leitor(String nome, String morada, String telefone, String email, Calendar data, String pass, boolean tipo, int nInterno) {
		super(nome, morada, telefone, email, data, pass);
		this.tipo = tipo;
		this.setnInterno(nInterno);
		this.requisicoes = new Requisicao[this.getMaxReqs()];
	}

	/** Método para calcular a duração máxima de um empréstimo consoante o tipo de Leitor
	 * @return 90 se for professor (tipo = true), 5 se for aluno (tipo = false)
	 */
	public int getDuracao() {
		if (tipo) return 90;
		else return 5;
	}

	/** Método para calcular o número máximo de artigos a emprestar consoante o tipo de Leitor
	 * @return 5 se for professor (tipo = true), 2 se for aluno (tipo = false)
	 */
	public final int getMaxReqs() {
		if (tipo) return 5;
		else return 2;
	}

	/** Getter para o atributo tipo de Leitor
	 * @return true se for professor, false se for aluno
	 */
	public boolean getTipo() {
		return this.tipo;
	}
	
	/** Getter para o atributo com a lista de requisições
	 * @return Array de requisições do utilizador
	 */
	public Requisicao[] getRequisicoes() {
		return requisicoes;
	}

	/** Getter para o atributo número de requisições
	 * @return nº de requisições actuais do utilizador
	 */
	public int getnReqs() {
		return this.nReqs;
	}	
	
	/** Setter para o atributo tipo de Leitor
	 * @param tipo Tipo do Leitor (true - professor, false - aluno)
	 */
	public void setTipo(Boolean tipo) {
		this.tipo = tipo;
	}
	
	@Override
	public String toString() {
		return this.getNome() +  ", "+ this.getEmail() + ", Telefone: " + this.getTelefone() +", Data de nascimento: " + this.getDataNascimento();
	}

	/** Compara o Leitor this com o Leitor user.
	 * Compara os campos nome, nº interno, telefone, morada e e-mail
	 * @param user Utilizador a comparar
	 * @return true se todos forem iguais, false caso contrário
	 */
	@Override
	public boolean equals(Utilizador user) {
		Leitor leitor = (Leitor) user;
		return (this.getNome().equals(leitor.getNome()))&&(this.getnInterno() == leitor.getnInterno())&&(this.getTelefone().equals(leitor.getTelefone()))&&(this.getMorada().equals(leitor.getMorada()))&&(this.getEmail().equals(leitor.getEmail()));
	}

	
	/** Método para utilizador entregar um artigo
	 * @param artigo Artigo a entregar
	 * @return 1 se entregar com sucesso, -1 se não for encontrada a requisição
	 */
	public int entregar(Artigo artigo) {
		int i = 0, j;

		//Procura a requsição que contém o artigo no ArrayList de requisições
		while (i<this.nReqs){
			if (this.getRequisicoes()[i].getArtigo().equals(artigo)) break;
			i++;
		}
		if (i==this.nReqs) return -1;
		j = i;

		//Remove a requisição e actualiza o ArrayList de requisições
		this.getRequisicoes()[i].entregar();
		for (j = i; j<nReqs-1; j++)
			this.requisicoes[j] = this.getRequisicoes()[j+1];
		this.requisicoes[j] = null;
		nReqs--;

		//Coloca o artigo como não requisitado
		artigo.removeReq();
		return 1;
	}

	/** Método para utilizador requisitar um artigo
	 * @param artigo Artigo a requisitar
	 * @param reqs Lista de requisições do sistema
	 * @return 1 se for bem sucedido, 0 se falhar
	 */
	public int requisitar(Artigo artigo, ArrayList<Requisicao> reqs) {

		//Requisita caso o leitor possa requisitar mais artigos e o artigo esteja disponível
		if ((this.nReqs<this.requisicoes.length)&&(artigo.availCopy()>0)) {
			Requisicao nova = new Requisicao(this, artigo, Calendar.getInstance());
			this.requisicoes[nReqs++] = nova;
			reqs.add(nova);
			artigo.addReq();
			return 1;
		}
		return 0;
	}
}
