package bib;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/** Classe que representa um Utilizador do tipo Administrador.
 * <p> Cada administrador tem a função de gerir utilizadores e artigos na biblioteca.
 * 
 * @author André Baptista (2012137523)
 * @author Rosa Faria (2005128014)
 */
public class Admin extends Utilizador implements Serializable{
	private String categoria;
	static final long serialVersionUID = 6784420523545791913L;
	
	/** Construtor vazio */
	Admin(){}
	
	/** Construtor do tipo Administrador.
	 * <p> Chama o construtor super e define a categoria e número interno do Administrador
	 * 
	 * @param nome nome do utilizador
	 * @param morada morada do utilizador
	 * @param telefone telefone do utilizador
	 * @param email e-mail do utilizador
	 * @param data data de nascimento do utilizador
	 * @param pass palavra-chave da conta do utilizador
	 * @param categoria categoria profissional do administrador
	 * @param nInterno número interno do Administrador
	*/	
	Admin(String nome, String morada, String telefone, String email, Calendar data, String pass, String categoria, int nInterno) {
		super(nome, morada, telefone, email, data, pass);
		this.categoria = categoria;
		this.setnInterno(nInterno);
	}

	/** Método para remover uma cópia do artigo se houver alguma cópia não requisitada
	 * @param artigo artigo a que se quer remover uma cópia
	 */
	public void removeCopy (Artigo artigo) {
		if (artigo.availCopy()>0) artigo.setnCopias(artigo.getnCopias()-1);
	}
	
	/** Método para criar um novo artigo.
	 * <p> Começa por verificar que o artigo ainda não existe na base de dados
	 * <p> Se não existir adiciona, pondo o número de cópias a 1
	 * @param dados ArrayList com todos os dados da biblioteca
	 * @param novo Artigo a inserir
	 * @return ponteiro para novo artigo, null se artigo já existia
	 */
	public Artigo addArtigo(ArrayList<ArrayList> dados, Artigo novo) {
		if (novo instanceof Livro) {
			/**dados.get(1) é o ArrayList de livros do sistema**/
			for (int i = 0; i<dados.get(1).size();i++) {
				if (novo.equals(dados.get(1).get(i)))
					/**encontrou um artigo igual**/
					return null;
			}
			novo.setnCopias(1);
			dados.get(1).add(novo);
			return (Artigo)dados.get(1).get(dados.get(1).size()-1);
		}
		else {
			/**dados.get(0) é o ArrayList de DVDs do sistema**/
			for (int i = 0; i<dados.get(0).size();i++) {
				if (novo.equals(dados.get(0).get(i)))
					/**encontrou um artigo igual**/
					return null;
			}
			novo.setnCopias(1);
			dados.get(0).add(novo);
			return (Artigo) dados.get(0).get(dados.get(0).size()-1);
		}
	}

	/** Método para adicionar utilizador.
	 * <p> Começa por verificar se ainda não existe o utilizador
	 * <p> Se não existir, adiciona e atribui-lhe um id igual ao 
	 * id do último utilizador da lista +1
	 * 
	 * @param dados ArrayList com os dados da biblioteca
	 * @param user Utilizador novo a inserir
	 * @return ponteiro para novo utilizador, null se o utilizador já existia
	 */
	public Utilizador addUser(ArrayList<ArrayList> dados, Utilizador user) {
		if (user instanceof Leitor) {
			/**dados.get(5) é o ArrayList de leitores do sistema**/
			ArrayList<Leitor> leitor = dados.get(5);
			for (int i = 0; i<leitor.size();i++) {
				if (user.equals(leitor.get(i)))
					/**encontrou um Utilizador igual**/
					return null;
			}
			/**Cria novo leitor com os mesmos dados e ID = ID do último no array +1**/
			Leitor aux = new Leitor(user.getNome(), user.getMorada(), user.getTelefone(), user.getEmail(), user.getDataNascimento().getCalendar(), user.getPalavraChave(), ((Leitor)user).getTipo(), leitor.get(leitor.size()-1).getnInterno()+1);
			dados.get(5).add(aux);
			return (Utilizador)dados.get(5).get(leitor.size()-1);
		}
		else {
			/**dados.get(4) é o ArrayList de administradores do sistema**/
			ArrayList<Admin> admins = dados.get(4);
			for (int i = 0; i<admins.size();i++) {
				if (user.equals(admins.get(i))) 
					/**encontrou um Utilizador igual**/
					return null;
			}
			/**Cria novo administrador com os mesmos dados e ID = ID do último no array +1**/
			Admin aux = new Admin(user.getNome(), user.getMorada(), user.getTelefone(), user.getEmail(), user.getDataNascimento().getCalendar(), user.getPalavraChave(), ((Admin)user).getCategoria(),admins.get(admins.size()-1).getnInterno()+1);
			dados.get(4).add(aux);
			return (Utilizador) dados.get(4).get(admins.size()-1);
		}
	}
	
	/** Método para procurar o utilizador comparando os parametros dados 
	 * com todos os utilizadores do mesmo tipo
	 * @param administradores ArrayList dos administradores do sistema
	 * @param leitores ArrayList dos leitores do sistema
	 * @param utilizador Utilizador a procurar
	 * @param parametros Lista de Strings representando os parâmetros a comparar. 
	 * Parâmetros possíveis: nome, morada, telefone, email, data, tipo, categoria, id(dependendo do tipo de Utilizador)
	 * @return lista com utilizadores cujos parametros são identicos, null se não existir nenhum
	 */
	public ArrayList<Utilizador> procurarUtilizadores(ArrayList<Admin> administradores, ArrayList<Leitor> leitores, Utilizador utilizador, String ... parametros){
		/* Copiar todos os utilizadores do mesmo tipo para um array auxiliar
		 * Desse array vão sendo retirados todos os que são diferentes do inserido
		 */
		ArrayList<Utilizador> aux = new ArrayList<>();
		if (utilizador instanceof Leitor)
			for (int i = 0; i<leitores.size();i++)
				aux.add(leitores.get(i));
		else
			for (int i = 0; i<administradores.size();i++)
				aux.add(administradores.get(i));
		
		
		for (String param : parametros){
			/* Percorre todos os parâmetros
			 * Para cada um percorre todos os utilizadores do array aux 
			 * e vê se coincidem. Se não coincidirem remove do array aux
			 */
			try {
				switch(param) {
					case "nome":
						for (int i = 0; i<aux.size();i++) {
							if (!aux.get(i).getNome().contains(utilizador.getNome()))
								aux.remove(i--);
						}	break;
					case "morada":
						for (int i = 0; i<aux.size();i++) {
							if (!aux.get(i).getMorada().contains(utilizador.getMorada()))
								aux.remove(i--);
						}	break;

					case "telefone":	
						for (int i = 0; i<aux.size();i++) {
							if (!aux.get(i).getTelefone().contains(utilizador.getTelefone()))
								aux.remove(i--);
						}	break;
					case "email":
						for (int i = 0; i<aux.size();i++) {
							if (!aux.get(i).getEmail().contains(utilizador.getEmail()))
								aux.remove(i--);
						}	break;
					case "data":
						for (int i = 0; i<aux.size();i++) {
							if (!aux.get(i).getDataNascimento().equals(utilizador.getDataNascimento()))
								aux.remove(i--);
						}	break;
					case "tipo":
						for (int i = 0; i<aux.size();i++) {
							if (((Leitor)aux.get(i)).getTipo()!=(((Leitor)utilizador).getTipo()))
								aux.remove(i--);
						}	break;
					case "categoria":
						for (int i = 0; i<aux.size();i++) {
							if (!((Admin)aux.get(i)).getCategoria().equals(((Admin)utilizador).getCategoria()))
								aux.remove(i--);
						}	break;
					case "id":
						for (int i = 0; i<aux.size();i++) {
							if (aux.get(i).getnInterno()!=utilizador.getnInterno())
								aux.remove(i--);
						}	break;
					default:
						System.out.println("Parâmetro não existente: "+param);
				}
			}
			catch (ClassCastException e){
				System.out.println("Parâmetro inválido para o tipo de utilizador: "+param);
			}
		}
		return aux;
	}
	
	/** Método para remover utilizador do sistema.
	 * <p> Verifica se o utilizador existe, 
	 * entrega todos os artigos que tiver requisitado, 
	 * adiciona a palavra "(removido)" 
	 * ao seu nome para manter a referência ao utilizador no registo das 
	 * requisições feitas antes de ser eliminado
	 * @param dados ArrayList dos dados da Biblioteca
	 * @param user Utilizador a apagar
	 * @return 1 se for removido, -1 se não for encontrado (nem removido)
	 */
	public int removeUser(ArrayList<ArrayList> dados, Utilizador user) {
		if (dados.get(4).contains(user)) {
			user.setNome(user.getNome()+" (removido)");
			dados.get(4).remove(user);
			return 1;
		}
		if (dados.get(5).contains(user)) {
			for (int i = 0; i<((Leitor)user).getnReqs();i++)
				((Leitor)user).entregar(((Leitor)user).getRequisicoes()[i].getArtigo());
			user.setNome(user.getNome()+" (removido)");
			dados.get(5).remove(user);
			return 1;
		}
		else return -1;
	}
	
	/** Método que calcula as estatísticas do mês dado.
	 * <p> Calcula o dia com mais requisições, a média das requisições diárias
	 * os artigos mais requisitados, e os que nunca foram requisitados
	 * @param mes (Int) mês cujas estatísticas queremos
	 * @param ano (Int) ano em que o mês se encontra
	 * @param dados ArrayList com os dados do sistema
     * @return Arraylist que contém o número de requisições para cada dia do mês, o número  médio  de  requisições  por  mês  e  o  dia  do  mês  em  que  houve  mais requisições. 
	 */	
	public ArrayList checkStats(int mes, int ano, ArrayList <ArrayList> dados) {
		ArrayList<Requisicao> requisicoes = dados.get(3);
		ArrayList<Livro> livros = dados.get(1);
		ArrayList<DVD> dvds = dados.get(0);
		
		Calendario input = new Calendario(1,mes-1,ano), data;
		double dias[] = new double[input.getMaxDias()], media = 0;
		int max = 0, diaMaximo = 0, maxL = 0, maxD = 0, indexL = -1, indexD = -1;
		int livrosStats[] = new int[livros.size()];
		int dvdsStats[] = new int[dvds.size()];
		ArrayList<Artigo> nenhumArtigo = new ArrayList<>();
		
		ArrayList stats = new ArrayList<>();
		
		/* Percorre o ArrayList de requisições
		 * Verifica se cada uma das requisições foi realizada no mês e ano pretendidos
		 * Incrementa o número de requisições de cada dia
		*/
		for (int i = 0; i<requisicoes.size();i++) {
			data = requisicoes.get(i).getDataInicio();
			if ((data.getMes() == mes) && (data.getAno()  == ano)) {
				dias[data.getDia()-1]++;
				//Incrementa contador das requisições do artigo
				if (requisicoes.get(i).getArtigo() instanceof Livro)
					livrosStats[livros.indexOf(requisicoes.get(i).getArtigo())]++;
				else
					dvdsStats[dvds.indexOf(requisicoes.get(i).getArtigo())]++;
			}
		}
				
		//Procura livro mais requisitado e livros não requisitados
		for (int i = 0; i<livros.size();i++) {
			if (livrosStats[i] == 0) {
				nenhumArtigo.add(livros.get(i));
			}
			else if (livrosStats[i]>maxL) {
				maxL = livrosStats[i];
				indexL = i;
			}
		}

		//Procura DVD mais requisitado e DVDs não requisitados
		for (int i = 0; i<dvds.size();i++) {
			if (dvdsStats[i] == 0) {
				nenhumArtigo.add(dvds.get(i));
			}
			else if (dvdsStats[i]>maxD) {
					maxD = dvdsStats[i];
					indexD = i;
			}
		}
		
		//Percorre o array de dias e obtém o índice com mais requisições
		for (int i=0;i<dias.length;i++){
			media+=dias[i];
			if (dias[i] > max) {
				max = (int)dias[i];
				diaMaximo = i;
			}
		}
		diaMaximo++;
		media /= dias.length;
		
		//juntar todos os arrays e dados no ArrayList stats para ser devolvido
		stats.add(dias); //0
		stats.add(media); //1
		stats.add(diaMaximo); //2
		stats.add(nenhumArtigo); //3
		if (indexL==-1) stats.add(null);
		else stats.add(livros.get(indexL)); //4
		if (indexD==-1) stats.add(null);
		else stats.add(dvds.get(indexD)); //5
		return stats;
	}

	/**
	 * Função para listar os artigos que estão actualmente requisitados
	 * @param dados Dados do sistema
	 * @return ArrayList com os artigos que têm pelo menos 1 cópia requisitada
	 */
	public ArrayList<Artigo> actualmenteRequisitados(ArrayList<ArrayList> dados) {
		ArrayList<Artigo> aux = new ArrayList<>();
		for (int i = 0; i< dados.get(0).size();i++) {
			if (((Artigo)dados.get(0).get(i)).getnRequisitados()>0) {
				aux.add((Artigo)dados.get(0).get(i));
			}
		}
		for (int i = 0; i< dados.get(1).size();i++) {
			if (((Artigo)dados.get(1).get(i)).getnRequisitados()>0) {
				aux.add((Artigo)dados.get(1).get(i));
			}
		}
		return aux;
	}
	
	/** Método que verifica todas as requisicoes atrasadas
	 * @param hoje Calendario com a data de hoje
	 * @param requisicoes ArrayList com todas as requisições do sistema
	 * @return ArrayList com as requisicoes cuja data de entrega é anterior à data de hoje
	 */
	public ArrayList <Requisicao> checkAtrasados(Calendario hoje, ArrayList <Requisicao> requisicoes) {
		ArrayList <Requisicao> atrasadas = new ArrayList <>();
		for (int i = 0; i<requisicoes.size();i++)
			if (((hoje.getCalendar()).after(requisicoes.get(i).getDataFim().getCalendar()))&&(requisicoes.get(i).getDataEntrega()==null))
				atrasadas.add(requisicoes.get(i));
		return atrasadas;
	}

	/** Compara o Administrador this com o Administrador user.
	 * Compara os campos nome, nº interno, telefone, morada e e-mail
	 * @param user Utilizador a comparar
	 * @return true se todos forem iguais, false caso contrário
	 */
	@Override
	public boolean equals(Utilizador user) {
		Admin leitor = (Admin) user;
		return (this.getNome().equals(leitor.getNome()))&&(this.getnInterno() == leitor.getnInterno())&&(this.getTelefone().equals(leitor.getTelefone()))&&(this.getMorada().equals(leitor.getMorada()))&&(this.getEmail().equals(leitor.getEmail()));
	}
	
	@Override
	public String toString() {
		return this.getNome() +  ", " + this.getEmail() + ", Telefone: " + this.getTelefone() +", Data de nascimento: " + this.getDataNascimento();
	}	
	
	/** Setter do parâmetro correspondente à categoria profissional do Administrador
	 * @param categoria String com a categoria profissional do Administrador
	 */
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	/** Getter da categoria profissional do Administrador
	 * @return categoria profissional do Administrador
	 */
	public String getCategoria() {
		return this.categoria;
	}
}