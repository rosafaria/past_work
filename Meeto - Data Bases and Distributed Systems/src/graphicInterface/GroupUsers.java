package graphicInterface;

import globalclasses.Message;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import interfaces.UIWindow;

public class GroupUsers extends UIWindow {
	private static final long serialVersionUID = 1L;
        private final UIClient client;
        private final String groupname;
        private final int perm;
	 
	public GroupUsers(UIClient client, String groupname, int permission) {
            initComponents();
            
            client.lastWindow().setVisible(true);
            this.setLocationRelativeTo(client.lastWindow());
            client.lastWindow().setVisible(false);
            this.setTitle(client.getUsername());

            this.client = client;
            this.client.addWindow(this);
            this.groupname = groupname;
            this.perm = permission;
            if (this.perm!=2) {
                this.makeadmin.setVisible(false);
                this.delAdmin.setVisible(false);
                this.delUser.setVisible(false);
            }
            setModel();
            this.setVisible(true);
	}
	public class MyTableModel extends DefaultTableModel{
            
            public MyTableModel(Object[][]data, Object[] header) {
                super(data, header);
            }
            
            public boolean isCellEditable(int row, int column){
              return false;
            }
        }
        private void setModel() {
            ArrayList<Object> par= new ArrayList();
            par.add(groupname);
            par.add(true);
            Message result = client.sendTCP(6,par);   
            if (result.getResult()!=1) {
                JOptionPane.showMessageDialog(null, "No users found in this group", "Error",JOptionPane.ERROR_MESSAGE);
                leave();
            }
            else {
                String[] requests = result.toString().split("\n");
                MyTableModel tm;
                Object[][] data = new Object[requests.length][requests[0].split("\t").length];
                for (int i = 0; i<requests.length;i++) {
                    data[i] = requests[i].split("\t");
                }
                Object[] header2 = {"User","Privileges"};
                for (int i = 0; i<data.length;i++) {
                    if (data[i][1].equals("1"))
                        data[i][1] = "ADMIN";
                    else if (data[i][1].equals("2"))
                        data[i][1] = "CREATOR";
                    else data[i][1] = "";
                }
                tm = new MyTableModel(data,header2);
                this.table.setModel(tm);
                if (this.perm==2) {
                    addListener();
                }
            }
	}
	
        private void addListener () {
            ListSelectionModel listSelectionModel = table.getSelectionModel();
                listSelectionModel.addListSelectionListener(new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent e) { 
                        int selected = table.getSelectedRow();
                        if (selected<0) return;
                        if (table.getValueAt(selected,1).equals("ADMIN")) {
                            makeadmin.setEnabled(false);
                            delAdmin.setEnabled(true);
                            delUser.setEnabled(true);
                        }
                        else if (table.getValueAt(selected,1).equals("CREATOR")||((String)table.getValueAt(selected,0)).trim().equals(client.getUsername())){
                            delAdmin.setEnabled(false);
                            makeadmin.setEnabled(false);
                            delUser.setEnabled(false);
                        }
                        else {
                            delAdmin.setEnabled(false);
                            makeadmin.setEnabled(true);
                            delUser.setEnabled(true);
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

        makeadmin = new javax.swing.JButton();
        delUser = new javax.swing.JButton();
        delAdmin = new javax.swing.JButton();
        BackBtn = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        makeadmin.setText("Make Admin");
        makeadmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                makeadminActionPerformed(evt);
            }
        });

        delUser.setText("Delete User");
        delUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delUserActionPerformed(evt);
            }
        });

        delAdmin.setText("Remove Admin");
        delAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delAdminActionPerformed(evt);
            }
        });

        BackBtn.setText("Back");
        BackBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BackBtnActionPerformed(evt);
            }
        });

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
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(table);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(delUser)
                            .addComponent(delAdmin)
                            .addComponent(makeadmin))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(BackBtn)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addComponent(makeadmin)
                .addGap(18, 18, 18)
                .addComponent(delAdmin)
                .addGap(30, 30, 30)
                .addComponent(delUser)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(BackBtn)
                .addGap(24, 24, 24))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void delUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delUserActionPerformed
        int selected = table.getSelectedRow();
        if (selected == -1) return;
        ArrayList<Object> par = new ArrayList();
        par.add(groupname);
        par.add(table.getValueAt(selected,0));
        Message result = client.sendTCP(10,par);
        switch(result.getResult()) {
            case -3:
                JOptionPane.showMessageDialog(null, result.getMsg().toString(), "Error",JOptionPane.ERROR_MESSAGE);
                leave();
                break;
            case -2:
                JOptionPane.showMessageDialog(null, "User is not in group anymore", "Error",JOptionPane.ERROR_MESSAGE);
                table.removeRowSelectionInterval(selected, selected);
                break;
            case -1:
                JOptionPane.showMessageDialog(null, "You are not in the group anymore", "Error",JOptionPane.ERROR_MESSAGE);
                leave();
                break;
            case 1:
                JOptionPane.showMessageDialog(null, "User was successfully deleted", "Success",JOptionPane.INFORMATION_MESSAGE);
                table.removeRowSelectionInterval(selected, selected);
                break;
            default:
                JOptionPane.showMessageDialog(null, "There was a problem performing this operation", "Error",JOptionPane.ERROR_MESSAGE);
        }
        setModel();
    }//GEN-LAST:event_delUserActionPerformed

    private void BackBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BackBtnActionPerformed
        leave();
    }//GEN-LAST:event_BackBtnActionPerformed

    private void makeadminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_makeadminActionPerformed
        int selected = table.getSelectedRow();
        if (selected == -1) return;
        ArrayList<Object> par = new ArrayList();
        par.add(groupname);
        par.add(table.getValueAt(selected,0));
        Message result = client.sendTCP(11,par);
        switch(result.getResult()) {
            case -3:
                JOptionPane.showMessageDialog(null, result.getMsg().toString(), "Error",JOptionPane.ERROR_MESSAGE);
                leave();
                break;
            case -2:
                JOptionPane.showMessageDialog(null, "User is not in group anymore", "Error",JOptionPane.ERROR_MESSAGE);
                break;
            case -1:
                JOptionPane.showMessageDialog(null, "You are not in the group anymore", "Error",JOptionPane.ERROR_MESSAGE);
                leave();
                break;
            case 1:
                JOptionPane.showMessageDialog(null, "User is now group administrator.", "Success",JOptionPane.INFORMATION_MESSAGE);
                break;  
            default:
                JOptionPane.showMessageDialog(null, "There was a problem performing this operation", "Error",JOptionPane.ERROR_MESSAGE);
        }
        setModel();
    }//GEN-LAST:event_makeadminActionPerformed

    private void delAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delAdminActionPerformed
        int selected = table.getSelectedRow();
        if (selected == -1) return;
        ArrayList<Object> par = new ArrayList();
        par.add(groupname);
        par.add(table.getValueAt(selected,0));
        Message result = client.sendTCP(12,par);
        switch(result.getResult()) {
            case -3:
                JOptionPane.showMessageDialog(null, result.getMsg().toString(), "Error",JOptionPane.ERROR_MESSAGE);
                leave();
                break;
            case -2:
                JOptionPane.showMessageDialog(null, "User is not in group anymore", "Error",JOptionPane.ERROR_MESSAGE);
                break;
            case -1:
                JOptionPane.showMessageDialog(null, "You are not in the group anymore", "Error",JOptionPane.ERROR_MESSAGE);
                leave();
                break;
            case 1:
                JOptionPane.showMessageDialog(null, "User is not a group administrator anymore.", "Success",JOptionPane.INFORMATION_MESSAGE);
                break;  
            default:
                JOptionPane.showMessageDialog(null, "There was a problem performing this operation", "Error",JOptionPane.ERROR_MESSAGE);
        }
        setModel();
    }//GEN-LAST:event_delAdminActionPerformed

    private void leave() {
        JFrame previous = client.removeWindow();
        previous.setVisible(true);
        this.setVisible(false);
        this.dispose();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BackBtn;
    private javax.swing.JButton delAdmin;
    private javax.swing.JButton delUser;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton makeadmin;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}