package bib;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/** Classe abstracta que define os atributos de um utilizador genérico
 * @author André Baptista (2012137523)
 * @author Rosa Faria (2005128014)
 */
public abstract class Utilizador implements Serializable{

	private String nome;
	private String morada;
	private String telefone;
	private String email;
	private Calendario dataNascimento;
	private String palavraChave;
	private int nInterno;
	static final long serialVersionUID = -7403151711204682368L;
	
	/** Construtor vazio */
	public Utilizador(){}
	
	/** Construtor do tipo definindo todos os atributos à excepção do nº interno
	 * @param nome Nome do utilizador
	 * @param morada Morada do utilizador
	 * @param telefone Telefone do utilizador
	 * @param email E-mail do utilizador
	 * @param data Data de nascimento do utilizador
	 * @param pass Palavra-chave da conta do utilizador
	 */
	public Utilizador(String nome, String morada, String telefone, String email, Calendar data, String pass) {
		this.nome = nome;
		this.morada = morada;
		this.telefone = telefone;
		this.email = email;
		this.dataNascimento = new Calendario(data);
		this.palavraChave = pass;
	}

	/** Método para utilizador fazer login
	 * @param pass Palavra-chave inserida
	 * @return true se a password inserida corresponde à definida, false caso contrário
	 */
	public boolean entrar(String pass) {
		return this.getPalavraChave().equals(pass);
	}

	/** Getter do atributo nome
	 * @return nome do utilizador
	 */
	public String getNome() {
		return this.nome;
	}

	/** Getter para o atributo morada
	 * @return morada do utilizador
	 */
	public String getMorada() {
		return this.morada;
	}

	/** Getter para o atributo telefone
	 * @return telefone do utilizador
	 */
	public String getTelefone() {
		return this.telefone;
	}
	
	/** Getter para o atributo e-mail
	 * @return e-mail do utilizador
	 */
	public String getEmail() {
		return this.email;
	}

	/** Getter para o atributo palavra chave
	 * @return palavra-chave do utilizador
	 */
	public String getPalavraChave() {
		return palavraChave;
	}

	/**
	 * Getter para o atributo nInterno
	 * @return numero interno do utilizador
	 */
	public int getnInterno() {
			return this.nInterno;
	}
			
	/** Getter para o atributo dataNascimento
	 * @return data de nascimento
	 */
	public Calendario getDataNascimento() {
		return dataNascimento;
	}
	
	/** Setter para o parâmetro morada
	 * @param morada Morada do utilizador
	 */
	public void setMorada(String morada) {
		this.morada = morada;
	}

	/** Setter para o parâmetro telefone
	 * @param telefone Telefone do utilizador
	 */
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	/** Setter para o parâmetro e-mail
	 * @param email E-mail do utilizador
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/** Setter para o atributo nome
	 * @param nome Nome do utilizador
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/** Setter para o atributo data de nascimento
	 * @param dataNascimento Data de nascimento do utilizador
	 */
	public void setDataNascimento(Calendario dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	/** Setter para o atributo palavra-chave
	 * @param palavraChave Palavra-chave do utilizador
	 */
	public void setPalavraChave(String palavraChave) {
		this.palavraChave = palavraChave;
	}
	
	/** Setter para o número interno
	 * @param nInterno Nº interno do utilizador
	 */
	public void setnInterno(int nInterno) {
		this.nInterno = nInterno;
	}
	
	/** Método para utilizador alterar a password
	 * @param old Palavra-Passe antiga
	 * @param pass Nova palavra-passe
	 * @return true se a palavra passe anterior está correcta, false caso contrário
	 */	
	public boolean changePass(String old, String pass) {
		if (this.getPalavraChave().equals(old)){
			this.setPalavraChave(pass);
			return true;
		}
		return false;
	}

	abstract boolean equals(Utilizador user);

	/** Método para determinar se algum utilizador do grupo 1 está contido no grupo 2
	 * (mesmo que seja apenas uma parte do nome)
	**/
	private static int compararAutores (ArrayList<Autor> grupo1, ArrayList<Autor> grupo2) {
		for(int i = 0; i<grupo2.size();i++)
			for (int j = 0; j<grupo1.size();j++) {
				if (grupo2.get(i).getNome().contains(grupo1.get(j).getNome())) {
					return 1;
				}
			}
		return 0;
	}
	
	/** Método para procurar artigos comparando os parametros dados 
	 * com todos os artigos do mesmo tipo
	 * @param livros ArrayList dos livros existentes na Biblioteca
	 * @param dvds ArrayList dos DVDs existentes na Biblioteca
	 * @param artigo Artigo a procurar
	 * @param parametros Lista de Strings representando os parâmetros a comparar. 
	 * Parâmetros possíveis: titulo, ano, editora, cota, isbn, autores, realizador, duracao (dependendo do tipo de Artigo)
	 * @return ArrayList com os artigos cujos atributos coincidem ou contêm os dados
	 */
	public ArrayList<Artigo> procurarArtigos(ArrayList<Livro> livros, ArrayList<DVD> dvds, Artigo artigo, String... parametros) {
        /* Copiar todos os artigos do mesmo tipo para um array auxiliar
		 * Desse array vão sendo retirados todos os que são diferentes do inserido
		 */
        ArrayList<Artigo> aux = new ArrayList<>();
        if (artigo instanceof Livro)
			for (int i = 0; i<livros.size();i++)
				aux.add(livros.get(i));
        else
			for (int i = 0; i<dvds.size();i++)
				aux.add(dvds.get(i));
        for (String param: parametros)
        	/* Percorre todos os parâmetros
			 * Para cada um percorre todos os artigos do array aux e verifica se coincidem
			 * Se não coincidirem remove o artigo desse array
			 */
			try {
				switch (param) {
					case "titulo":
						for (int i = 0; i<aux.size();i++) {
							if (!aux.get(i).getTitulo().contains(artigo.getTitulo()))
								aux.remove(i--);
						}	break;
					case "ano":
						for (int i = 0; i<aux.size();i++) {
							if (aux.get(i).getAnoPublicacao()!=artigo.getAnoPublicacao())
								aux.remove(i--);
						}	break;
					case "editora":
						for (int i = 0; i<aux.size();i++) {
							if (!aux.get(i).getEditora().contains(artigo.getEditora()))
								aux.remove(i--);
						}	break;
					case "cota":
						for (int i = 0; i<aux.size();i++) {
							if (!((Livro)aux.get(i)).getCota().equals(((Livro)artigo).getCota()))
								aux.remove(i--);
						}	break;
					case "isbn":
						for (int i = 0; i<aux.size();i++) {
							if (!((Livro)aux.get(i)).getIsbn().equals(((Livro)artigo).getIsbn()))
								aux.remove(i--);
						}	break;
					case "autores":
						for (int i = 0; i<aux.size();i++) {
							if (compararAutores(((Livro)artigo).getAutores(), ((Livro)aux.get(i)).getAutores())==0)
								aux.remove(i--);
						}	break;
					case "realizador":
						for (int i = 0; i<aux.size();i++) {
							if (!((DVD)aux.get(i)).getRealizador().equals(((DVD)artigo).getRealizador()))
								aux.remove(i--);
						}	break;
					case "duracao":
						for (int i = 0; i<aux.size();i++) {
							if (((DVD)aux.get(i)).getDuracao()!=(((DVD)artigo).getDuracao()))
								aux.remove(i--);
						}	break;
					default:
						System.out.println("Parâmetro inválido: "+param);
						break;
				} 
			}
			catch (ClassCastException e){
				System.out.println("Invalid parameter: "+param);
			}
		return aux;
	}
}

