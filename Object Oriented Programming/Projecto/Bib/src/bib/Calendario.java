package bib;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/** Classe que representa datas com dia, mês e ano.
 * <p> Implementa um tratamento mais fácil de datas ignorando as unidades menores que o dia
 * e faz conversão para String das mesmas
 * @author André Baptista (2012137523)
 * @author Rosa Faria (2005128014)
 */

public class Calendario implements Serializable{
	private Calendar calendar;
	static final long serialVersionUID = 3469165778394055780L;
	
	Calendario(){
		this.calendar = Calendar.getInstance();
	}
	
	Calendario(Calendar calendar){
		this.calendar = calendar;
	}
	
	Calendario (int dia, int mes, int ano) {
		this.calendar = Calendar.getInstance();
		this.calendar.set(ano, mes, dia);
	}

	/** Getter para o parametro calendario
	 * @return o atributo calendar do tipo Calendar
	 */
	public Calendar getCalendar() {
		return calendar;
	}
	
	/** Método para determinar o dia do mês
	 * @return dia do mês
	 */
	public int getDia(){
		return this.calendar.get(Calendar.DAY_OF_MONTH);
	}
	
	/** Método para determinar o mês
	 * @return mês
	 */
	public int getMes(){
		return this.calendar.get(Calendar.MONTH)+1;
	}
	
	/** Método para determinar o ano
	 * @return ano
	 */
	public int getAno(){
		return this.calendar.get(Calendar.YEAR);
	}
	
	/** Método para determinar o número de dias do mês
	 * @return número de dias do mês em questão
	 */
	public int getMaxDias(){
		return this.calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	/** Define o valor do atributo calendar
	 * @param calendar variável do tipo Calendar
	 */
	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}
	
	@Override
	public String toString(){
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		return dateFormat.format(this.calendar.getTime()).toString();
	}

	/** Compara o valor de dois calendarioss
	 * @param data Calendario com a data a comparar
	 * @return true se iguais, false caso contrário
	 */
	public boolean equals(Calendario data) {
		return this.calendar.equals(data.getCalendar());
	}
}
