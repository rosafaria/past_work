package bib;

import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/** Classe responsável pelo lançamento do programa.
 * @author André Baptista (2012137523)
 * @author Rosa Faria (2005128014)
 */


public class Main {
	
	public static Login logwindow;
	
	/** Método para iniciar o programa.
	 * <p> Tenta pôr o LookAndFeel "Nimbus" e lança nova janela de Login
	 * @param args
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, IOException{
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
		} catch (Exception e) {	}
		logwindow = new Login();
		logwindow.setVisible(true);
	}
}

