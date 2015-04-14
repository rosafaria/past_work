package bib;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.math.plot.Plot2DPanel;

/** Classe de implementação de Gráficos de Estatísticas.
 * @author André Baptista (2012137523)
 * @author Rosa Faria (2005128014)
 */
public class Grafico extends JFrame{
    private JPanel panel = new JPanel();
	
	/** Construtor vazio */
    public Grafico() {}
	
	/** Construtor do tipo JFrame que apresenta um gráfico
	 * @param String title
	 * @param double[] x
	 * @param double[] y
	 */
    public Grafico(String title, double[] x, double[] y) {
		this.setIconImage(new ImageIcon(getClass().getResource("/bib/resources/Logo.gif")).getImage());
		this.setTitle(title);
		this.setResizable(false);

		//Cria um novo objecto do tipo Plot2DPanel, utilizando a biblioteca jmathplot
		Plot2DPanel plot = new Plot2DPanel() {
		   @Override
			public Dimension getPreferredSize() {
			return new Dimension(460, 440);
			}
		};

		plot.addLinePlot(title, Color.RED, x, y);
		plot.setAxisLabels("Dias do mês","Nº de requisições");
		panel.add(plot);
		this.add(panel);
		this.pack();
		this.setLocationRelativeTo(null);
    }
}
