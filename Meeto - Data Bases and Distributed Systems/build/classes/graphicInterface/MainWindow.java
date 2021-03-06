package graphicInterface;

import globalclasses.Message;

import java.util.ArrayList;
import interfaces.UIWindow;
import javax.swing.JOptionPane;


public class MainWindow extends UIWindow{

    private static final long serialVersionUID = 1L;
    private UIClient client;
    /**
     * Creates new form Main
     * @param client
     */
    public MainWindow(UIClient client) {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setTitle(client.getUsername());
        
        this.client = client;
        client.addWindow(this);
        this.setVisible(true);
    }

    @Override
    public void setClosed(int closed){return;}
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        createGroup = new javax.swing.JButton();
        listGroups = new javax.swing.JButton();
        groupRequests = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        myActions = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        createMeeting = new javax.swing.JButton();
        meetInvite = new javax.swing.JButton();
        backBtn = new javax.swing.JButton();
        pastBtn = new javax.swing.JButton();
        currentBtn = new javax.swing.JButton();
        futureBtn = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        onlineBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setBackground(new java.awt.Color(0, 51, 51));
        setBounds(new java.awt.Rectangle(0, 0, 2, 2));
        setForeground(new java.awt.Color(0, 0, 0));

        createGroup.setText("Create Group");
        createGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createGroupActionPerformed(evt);
            }
        });

        listGroups.setText("List/Manage Groups");
        listGroups.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listGroupsActionPerformed(evt);
            }
        });

        groupRequests.setText("Manage Group Requests");
        groupRequests.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                groupRequestsActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("Groups");
        jLabel1.setToolTipText("");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("To Do");
        jLabel2.setToolTipText("");

        myActions.setText("My Action Items");
        myActions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                myActionsActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Meetings");
        jLabel3.setToolTipText("");

        createMeeting.setText("Create Meeting");
        createMeeting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createMeetingActionPerformed(evt);
            }
        });

        meetInvite.setText("My meetings & invitations");
        meetInvite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                meetInviteActionPerformed(evt);
            }
        });

        backBtn.setText("Exit");
        backBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backBtnActionPerformed(evt);
            }
        });

        pastBtn.setText("Past Meetings");
        pastBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pastBtnActionPerformed(evt);
            }
        });

        currentBtn.setText("Current Meetings");
        currentBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                currentBtnActionPerformed(evt);
            }
        });

        futureBtn.setText("Future Meetings");
        futureBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                futureBtnActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Extra");

        onlineBtn.setText("List Online Users");
        onlineBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onlineBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(78, 78, 78)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(groupRequests, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(createGroup, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(listGroups, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(72, 72, 72)
                                .addComponent(jLabel2))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(onlineBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(myActions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(76, 76, 76)
                                .addComponent(jLabel4)))
                        .addGap(54, 54, 54)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(meetInvite, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(createMeeting, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pastBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(currentBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(futureBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3))))
                .addGap(29, 29, 29))
            .addGroup(layout.createSequentialGroup()
                .addGap(175, 175, 175)
                .addComponent(backBtn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jLabel1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(createMeeting)
                        .addGap(30, 30, 30)
                        .addComponent(pastBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(currentBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(futureBtn))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(createGroup)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(listGroups)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(groupRequests)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(myActions)
                        .addGap(16, 16, 16)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(meetInvite)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(onlineBtn)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(backBtn)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void myActionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_myActionsActionPerformed
        Message action = client.sendTCP(17, new ArrayList());
        if (action.getResult()!=1) {
            JOptionPane.showMessageDialog(null, "No actions found", "Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        String[] actions = action.toString().split("\n");
        this.setVisible(false);
        new PickFromList(6,actions, client,"",0);
    }//GEN-LAST:event_myActionsActionPerformed

    private void createMeetingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createMeetingActionPerformed
        this.setVisible(false);
        new CreateMeet(client);
    }//GEN-LAST:event_createMeetingActionPerformed

    private void meetInviteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_meetInviteActionPerformed
        this.setVisible(false);
        new Requests(16, client);
    }//GEN-LAST:event_meetInviteActionPerformed

    private void groupRequestsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_groupRequestsActionPerformed
        this.setVisible(false);
        new Requests(13, client);
    }//GEN-LAST:event_groupRequestsActionPerformed

    private void listGroupsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listGroupsActionPerformed
        ArrayList<Object> par = new ArrayList<Object>();
        Message group = client.sendTCP(7, par);
        if (group.getResult()!=1) {
            JOptionPane.showMessageDialog(null, "No groups found", "Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        String[] groups = group.toString().split("\n");
        new PickFromList(2,groups, client,"",0);
        this.setVisible(false);
    }//GEN-LAST:event_listGroupsActionPerformed

    private void createGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createGroupActionPerformed
        new CreateGroup(client);
        this.setVisible(false);
    }//GEN-LAST:event_createGroupActionPerformed

    private void backBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBtnActionPerformed
        client.removeWindow().dispose();
        this.setVisible(false);
        this.dispose();
        client.sendTCP(51, null);
    }//GEN-LAST:event_backBtnActionPerformed

    private void pastBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pastBtnActionPerformed
        ArrayList<Object> par = new ArrayList<Object>();
        par.add(-1);
        Message meets = client.sendTCP(19, par);
        if (meets.getResult()!=1) {
            JOptionPane.showMessageDialog(null, "No meetings found", "Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        String[] meetings = meets.toString().split("\n");
        new PickFromList(1,meetings, client,"",0);
        this.setVisible(false);
    }//GEN-LAST:event_pastBtnActionPerformed

    private void currentBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_currentBtnActionPerformed
        ArrayList<Object> par = new ArrayList<Object>();
        par.add(0);
        Message meets = client.sendTCP(19, par);
        if (meets.getResult()!=1) {
            JOptionPane.showMessageDialog(null, "No meetings found", "Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        String[] meetings = meets.toString().split("\n");
        System.out.println("asd");
        new PickFromList(1,meetings, client,"",0);
        this.setVisible(false);
    }//GEN-LAST:event_currentBtnActionPerformed

    private void futureBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_futureBtnActionPerformed
        ArrayList<Object> par = new ArrayList<Object>();
        par.add(1);
        Message meets = client.sendTCP(19, par);
        if (meets.getResult()!=1) {
            JOptionPane.showMessageDialog(null, "No meetings found", "Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        String[] meetings = meets.toString().split("\n");
        new PickFromList(1,meetings, client,"",0);
        this.setVisible(false);
    }//GEN-LAST:event_futureBtnActionPerformed

    private void onlineBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onlineBtnActionPerformed
        Message users = client.sendTCP(50, new ArrayList<>());
        if (users.getResult()!=1) {
            JOptionPane.showMessageDialog(null, "No online users", "Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        new PickFromList(7,users.toString().split("\n"), client,"",0);
        this.setVisible(false);
    }//GEN-LAST:event_onlineBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backBtn;
    private javax.swing.JButton createGroup;
    private javax.swing.JButton createMeeting;
    private javax.swing.JButton currentBtn;
    private javax.swing.JButton futureBtn;
    private javax.swing.JButton groupRequests;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JButton listGroups;
    private javax.swing.JButton meetInvite;
    private javax.swing.JButton myActions;
    private javax.swing.JButton onlineBtn;
    private javax.swing.JButton pastBtn;
    // End of variables declaration//GEN-END:variables
}
