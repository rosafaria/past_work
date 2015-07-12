package graphicInterface;

import globalclasses.Message;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/** Classe de interface grafica para escolher um item de uma lista.
 */
public class PickFromList extends javax.swing.JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final String[] meetings;
        private final UIClient client;
        private final String meetID;
        private final int closed;
        private final int type;
	 
	public PickFromList(int type, String[] meetings, UIClient client, String meetingID, int closed) {
            initComponents();
            this.setLocationRelativeTo(null);
            this.setTitle(client.getUsername());
            
            this.meetings = meetings;
            this.client = client;

            //only used if it is agenda item
            this.meetID = meetingID;
            this.closed = closed;

            this.table.setColumnSelectionAllowed(false);
            this.table.setEnabled(false);
            this.type = type;
            //type = 1 - meeting
            //type = 2 - group
            //type = 3 - agenda item
            //type = 4 - Action Item
            //type = 5 - key decision
            //type = 6 - action item to do
            //type = 7 - user list
            //type = 8 - agenda item history

            setModel();
            if (type !=7) addMouseListener();
            this.setVisible(true);
	}
	
        private void setModel() {
            TableModel tm;
            Object[][] data = new Object[meetings.length][meetings[0].split("\t").length];
            for (int i = 0; i<meetings.length;i++) {
                data[i] = meetings[i].split("\t");
            }
            switch(type) {
                case 1:
                    Object[] header1 = {"Alias","Title","Outcome","Location","Date/time","Creator","Closed"};
                    tm = new DefaultTableModel(data,header1);
                    this.table.setModel(tm);
                    return;
                case 2:
                    Object[] header2 = {"Name"};
                    tm = new DefaultTableModel(data,header2);
                    this.table.setModel(tm);
                    return;
                case 3:
                    Object[] header3 = {"Number","Title","Description"};
                    tm = new DefaultTableModel(data,header3);
                    this.table.setModel(tm);
                    return;
                case 4:
                    Object[] header4 = {"Number","User","Meeting","Task","Done"};
                    tm = new DefaultTableModel(data,header4);
                    this.table.setModel(tm);
                    return;
                case 5:
                    Object[] header5 = {"Decision"};
                    tm = new DefaultTableModel(data,header5);
                    this.table.setModel(tm);
                    return;
                case 6:
                    Object[] header6 = {"Number","User","Meeting","Task","Done"};
                    tm = new DefaultTableModel(data,header6);
                    this.table.setModel(tm);
                    return;
                case 7:
                    Object[] header7 = {"Username"};
                    tm = new DefaultTableModel(data,header7);
                    this.table.setModel(tm);
                    return;
                case 8:
                    Object[] header8 = {"Number","Title","Editor","Description"};
                    tm = new DefaultTableModel(data,header8);
                    this.table.setModel(tm);
            }
            
            
	}
	
	private void addMouseListener() {
            /**Fica a espera de um duplo-clique num item da lista para o abrir**/
            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent evt) {
                    if (type==7) return;
                    JTable table =(JTable) evt.getSource();
                    int index = table.rowAtPoint(evt.getPoint());
                    if (evt.getClickCount() == 2) { //duplo-clique -> abrir nova janela
                        if ((index>=0)&&(index<meetings.length)) {
                            switch(type) {
                                case 1:
                                    launchMeet(meetings[index]);
                                    break;
                                case 2:
                                    launchGroup(meetings[index]);
                                    break;
                                case 3:
                                    launchItem(meetings[index]);
                                    break;
                                case 4:
                                    launchDecision(meetings[index], "Action Item");
                                    break;
                                case 5:
                                    launchDecision(meetings[index], "Key Decision");
                                    break;
                                case 6:
                                    doMyAction(meetings[index], index);
                                    break;
                                case 8:
                                    launchDecision(meetings[index].split("\t")[3], "Agenda Item History");
                                    break;
                                default:
                                    JOptionPane.showMessageDialog(null, "There was a problem performing this operation", "Error",JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            });
	}
	
	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(table);

        jScrollPane1.setViewportView(jScrollPane2);

        jButton1.setText("Back");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jButton1)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 659, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jButton1)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        leave();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void leave() {
        JFrame previous = client.lastWindow();
        previous.setVisible(true);
        this.setVisible(false);
        this.dispose();
    }
    
    private void launchMeet(String meeting) {
        new MeetingWindow(client, meeting);
        this.setVisible(false);
        this.dispose();
    }
    
    private void launchItem(String item) {
        new AgendaItem(client, meetID, item.trim(), closed);
        this.setVisible(false);
    }
    
    private void launchGroup(String item) {
        new ManageGroup(client, item.trim());
        this.setVisible(false);
        this.dispose();
    }
    
    private void launchDecision(String item, String title) {
      JTextArea textArea = new JTextArea(6, 25);
      textArea.setText(item);
      textArea.setEditable(false);
      JScrollPane scrollPane = new JScrollPane(textArea);
      JOptionPane.showMessageDialog(null, scrollPane, title,JOptionPane.PLAIN_MESSAGE);
    }
    
    private void doMyAction(String item, int row) {
        String[] actionData = item.split("\t");
        
        JTextArea textArea = new JTextArea(6, 25);
        textArea.setText(actionData[3]);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        
        int yes = JOptionPane.showConfirmDialog(null, scrollPane, "Do action for meeting '"+actionData[2]+"'?", JOptionPane.OK_CANCEL_OPTION);
        
        if (yes == 0) {
            ArrayList<Object> par = new ArrayList<Object>();
            par.add(Integer.parseInt(actionData[0].trim()));
            par.add(actionData[2].trim());
            Message result = client.sendTCP(27,par);
            switch (result.getResult()) {
                case 1:
                    JOptionPane.showMessageDialog(null, "Action has been completed", "Success",JOptionPane.INFORMATION_MESSAGE);
                    this.table.setValueAt("YES",row,4);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "There was an error performing this operation", "Error",JOptionPane.ERROR_MESSAGE);
                    leave();
            }
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}
