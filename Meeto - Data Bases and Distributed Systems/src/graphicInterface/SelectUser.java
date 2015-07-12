package graphicInterface;

import globalclasses.Message;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;

public class SelectUser extends javax.swing.JFrame {
    private static final long serialVersionUID = 1L;
	
    private UIClient client;
    private DefaultListModel<String> model;
    
    public SelectUser(UIClient client) {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setTitle(client.getUsername());
        
        btnGroup.add(this.all);
        btnGroup.add(this.fromGroup);
        btnGroup.add(this.fromMeeting);
        if (client.lastWindow() instanceof MeetingWindow) {
            btnGroup.add(this.wholeGroup);
        }
        else {
            this.wholeGroup.setVisible(false);
            this.groupList1.setVisible(false);
        }
        this.client = client;
        
        this.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGroup = new javax.swing.ButtonGroup();
        groupList = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        userList = new javax.swing.JList();
        meetingList = new javax.swing.JComboBox();
        all = new javax.swing.JRadioButton();
        fromGroup = new javax.swing.JRadioButton();
        fromMeeting = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        OKBtn = new javax.swing.JButton();
        CancelBtn = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        wholeGroup = new javax.swing.JRadioButton();
        groupList1 = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        groupList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                groupListActionPerformed(evt);
            }
        });

        userList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(userList);

        meetingList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                meetingListActionPerformed(evt);
            }
        });

        all.setText("All");
        all.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allActionPerformed(evt);
            }
        });

        fromGroup.setText("From Group");
        fromGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fromGroupActionPerformed(evt);
            }
        });

        fromMeeting.setText("From Meeting");
        fromMeeting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fromMeetingActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("Choose User");

        OKBtn.setText("OK");
        OKBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OKBtnActionPerformed(evt);
            }
        });

        CancelBtn.setText("Cancel");
        CancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelBtnActionPerformed(evt);
            }
        });

        jButton1.setText("jButton1");

        wholeGroup.setText("Entire Group");
        wholeGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                wholeGroupActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(OKBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                        .addComponent(CancelBtn)
                        .addGap(26, 26, 26))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(56, 56, 56))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(all, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(wholeGroup)
                                .addGap(12, 12, 12)
                                .addComponent(groupList1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(fromGroup)
                                    .addComponent(fromMeeting))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(groupList, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(meetingList, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel1)
                .addGap(32, 32, 32)
                .addComponent(all)
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(groupList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fromGroup))
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fromMeeting)
                    .addComponent(meetingList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(wholeGroup)
                    .addComponent(groupList1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CancelBtn)
                    .addComponent(OKBtn))
                .addGap(21, 21, 21))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void OKBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OKBtnActionPerformed
        if (this.wholeGroup.isSelected()) {
            ((MeetingWindow)(client.lastWindow())).setUser(((String) this.groupList1.getSelectedItem()).trim());
            leave();
            return;
        }
        
        int selectedItem = this.userList.getSelectedIndex();
        if (selectedItem==-1) leave();
        String user = ((String)model.get(selectedItem)).trim();
        
        JFrame previous = client.lastWindow();
        
        
        if (previous instanceof AddAction)
            ((AddAction)(previous)).setUser(user.trim());
        else if (previous instanceof ManageGroup)
            ((ManageGroup)(previous)).setUser(user.trim());
        else if (previous instanceof MeetingWindow)
            ((MeetingWindow)(previous)).setUser(user.trim());
        
        leave();
    }//GEN-LAST:event_OKBtnActionPerformed

    private void CancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelBtnActionPerformed
        leave();
    }//GEN-LAST:event_CancelBtnActionPerformed

    private void fromGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fromGroupActionPerformed
        if (this.groupList.getItemCount()!=0) return;
        Message result = client.sendTCP(7,new ArrayList());
        String[] groups = result.toString().split("\n");
        for (String group : groups) {
            this.groupList.addItem(group.trim());
        }
    }//GEN-LAST:event_fromGroupActionPerformed

    private void fromMeetingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fromMeetingActionPerformed
        if (this.meetingList.getItemCount()!=0) return;
        Message result = client.sendTCP(16,new ArrayList());
        String[] meets = result.toString().split("\n");
        for (String meet : meets) {
            this.meetingList.addItem(meet.split(" ")[0]);
        }
    }//GEN-LAST:event_fromMeetingActionPerformed

    private void groupListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_groupListActionPerformed
        String groupname = (String) this.groupList.getSelectedItem();
        ArrayList<Object> par = new ArrayList<Object>();
        par.add(groupname.trim());
        par.add(false);
        String[] groups = client.sendTCP(6, par).toString().split("\n");
        model = new DefaultListModel<String>();
        for (String group : groups) {
            if (!group.trim().equals(client.getUsername()))
            model.addElement(group.trim());
        }
        this.userList.setModel(model);
    }//GEN-LAST:event_groupListActionPerformed

    private void meetingListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_meetingListActionPerformed
        String meet = (String) this.meetingList.getSelectedItem();
        ArrayList<Object> par = new ArrayList<Object>();
        par.add(meet.trim());
        String[] meets = client.sendTCP(36, par).toString().split("\n");
        model = new DefaultListModel<String>();
        for (String group : meets) {
            if (!group.trim().equals(client.getUsername()))
                model.addElement(group.trim());
        }
        this.userList.setModel(model);
    }//GEN-LAST:event_meetingListActionPerformed

    private void wholeGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_wholeGroupActionPerformed
        if (this.groupList1.getItemCount()!=0) return;
        Message result = client.sendTCP(7,new ArrayList());
        String[] groups = result.toString().split("\n");
        for (String group : groups) {
            this.groupList1.addItem(group.trim());
        }
    }//GEN-LAST:event_wholeGroupActionPerformed

    private void allActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allActionPerformed
        if (this.groupList1.getItemCount()!=0) return;
        Message result = client.sendTCP(37,new ArrayList());
        String[] users = result.toString().split("\n");
        model = new DefaultListModel<String>();
        for (String user : users) {
            if (!user.trim().equals(client.getUsername()))
            model.addElement(user.trim());
        }
        this.userList.setModel(model);
    }//GEN-LAST:event_allActionPerformed

    private void leave() {
        client.lastWindow().setVisible(true);
        this.setVisible(false);
        this.dispose();
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CancelBtn;
    private javax.swing.JButton OKBtn;
    private javax.swing.JRadioButton all;
    private javax.swing.ButtonGroup btnGroup;
    private javax.swing.JRadioButton fromGroup;
    private javax.swing.JRadioButton fromMeeting;
    private javax.swing.JComboBox groupList;
    private javax.swing.JComboBox groupList1;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox meetingList;
    private javax.swing.JList userList;
    private javax.swing.JRadioButton wholeGroup;
    // End of variables declaration//GEN-END:variables
}
