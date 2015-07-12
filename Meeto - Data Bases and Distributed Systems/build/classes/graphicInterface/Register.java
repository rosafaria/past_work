
package graphicInterface;

import globalclasses.Message;
import interfaces.UIWindow;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Register extends UIWindow{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	UIClient client;
    
    public Register(UIClient client) {
        initComponents();
        this.setLocationRelativeTo(null);
        
        this.client = client;
        this.client.addWindow(this);
        client.setUser("CLIENT ON");
        this.setVisible(true);
    }

    @Override
    public void setClosed(int closed){}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        errorLbl = new javax.swing.JLabel();
        name = new javax.swing.JTextField();
        username = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        errorLbl1 = new javax.swing.JLabel();
        password = new javax.swing.JPasswordField();
        CancelBtn1 = new javax.swing.JButton();
        OKBtn = new javax.swing.JButton();

        errorLbl.setForeground(new java.awt.Color(255, 51, 51));
        errorLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        name.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                nameKeyTyped(evt);
            }
        });

        username.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                usernameKeyTyped(evt);
            }
        });

        jLabel1.setText("Name");

        jLabel2.setText("Username");

        jLabel3.setText("Password");

        errorLbl1.setForeground(new java.awt.Color(255, 51, 51));
        errorLbl1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        password.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                passwordKeyTyped(evt);
            }
        });

        CancelBtn1.setText("Cancel");
        CancelBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelBtn1ActionPerformed(evt);
            }
        });

        OKBtn.setText("OK");
        OKBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OKBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(64, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(19, 19, 19)
                                        .addComponent(jLabel1)
                                        .addGap(2, 2, 2))
                                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(name)
                                    .addComponent(username)
                                    .addComponent(password)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(OKBtn)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 134, Short.MAX_VALUE)
                                        .addComponent(CancelBtn1)))))
                        .addGap(42, 42, 42))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(errorLbl1, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(88, 88, 88))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(49, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addComponent(errorLbl1, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(OKBtn)
                    .addComponent(CancelBtn1))
                .addGap(37, 37, 37))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CancelBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelBtn1ActionPerformed
        JFrame previous = client.removeWindow();
        previous.setVisible(true);
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_CancelBtn1ActionPerformed

    private void OKBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OKBtnActionPerformed
        ArrayList <Object> parameters = new ArrayList<Object>();
            parameters.add(this.username.getText());
            parameters.add(this.name.getText());
            parameters.add(this.password.getText());
            Message reply = client.sendTCP(2, parameters);
            switch (reply.getResult()) {
                case 0:
                    this.errorLbl.setText("User already registered"); 
                    break;
                case -2:
                    this.errorLbl.setText("Database Error");
                    break;
                case -1:
                    this.errorLbl.setText("Username already in use");
                    break;
                    
                default:
                    //successful operation
                    this.client.setUser(this.username.getText());
                    JOptionPane.showMessageDialog(null, "User succefully added!", "Sucess",JOptionPane.INFORMATION_MESSAGE);
                    UIWindow previous = client.removeWindow();
                    previous.setVisible(true);
                    this.setVisible(false);
                    break;
            }
    }//GEN-LAST:event_OKBtnActionPerformed

    private void nameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nameKeyTyped
        if (this.name.getText().length()>Global.NAMELEN) {
            this.name.setText(this.name.getText().substring(0,Global.NAMELEN));
        }
    }//GEN-LAST:event_nameKeyTyped

    private void usernameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_usernameKeyTyped
        if (this.username.getText().length()>Global.USERLOGINLEN) {
            this.username.setText(this.username.getText().substring(0,Global.USERLOGINLEN));
        }
    }//GEN-LAST:event_usernameKeyTyped

    private void passwordKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_passwordKeyTyped
        if (this.password.getText().length()>Global.PASSWORDLEN) {
            this.password.setText(this.password.getText().substring(0,Global.PASSWORDLEN));
        }
    }//GEN-LAST:event_passwordKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CancelBtn1;
    private javax.swing.JButton OKBtn;
    private javax.swing.JLabel errorLbl;
    private javax.swing.JLabel errorLbl1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField name;
    private javax.swing.JPasswordField password;
    private javax.swing.JTextField username;
    // End of variables declaration//GEN-END:variables
}