/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package uasreservationtaliscocab;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Bryan
 */
public class FormEvent extends javax.swing.JFrame {

    /**
     * Creates new form FormEvent
     */
    public FormEvent() {
        initComponents();
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
        jTableEvent = new javax.swing.JTable();
        jButtonViewEvent = new javax.swing.JButton();
        jButtonReservationEvent = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        qtyTxt = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        idEventTxt = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 153, 153));
        setPreferredSize(new java.awt.Dimension(900, 450));
        setSize(new java.awt.Dimension(900, 450));
        getContentPane().setLayout(null);

        jTableEvent.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "id ", "event_name", "event_date", "category", "status", "participant_slot", "number_of_participant", "open_reservation_date", "close_reservation_date", "locations_id ", "price", "description"
            }
        ));
        jScrollPane1.setViewportView(jTableEvent);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(30, 30, 840, 250);

        jButtonViewEvent.setText("View");
        jButtonViewEvent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonViewEventActionPerformed(evt);
            }
        });
        getContentPane().add(jButtonViewEvent);
        jButtonViewEvent.setBounds(770, 290, 72, 23);

        jButtonReservationEvent.setBackground(new java.awt.Color(0, 102, 102));
        jButtonReservationEvent.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jButtonReservationEvent.setForeground(new java.awt.Color(255, 255, 255));
        jButtonReservationEvent.setText("Reserve");
        jButtonReservationEvent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReservationEventActionPerformed(evt);
            }
        });
        getContentPane().add(jButtonReservationEvent);
        jButtonReservationEvent.setBounds(580, 450, 110, 28);

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jLabel1.setText("Quantity");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(370, 360, 57, 30);
        getContentPane().add(qtyTxt);
        qtyTxt.setBounds(440, 360, 110, 22);

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jLabel2.setText("ID Event");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(675, 360, 70, 30);
        getContentPane().add(idEventTxt);
        idEventTxt.setBounds(751, 360, 110, 22);

        jButton1.setBackground(new java.awt.Color(0, 102, 102));
        jButton1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("BACK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1);
        jButton1.setBounds(780, 450, 84, 28);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/event.png"))); // NOI18N
        getContentPane().add(jLabel4);
        jLabel4.setBounds(0, 0, 900, 500);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonViewEventActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonViewEventActionPerformed
        try {
            String hasil;
            
            Socket clientSocket = new Socket("localhost",6666);
            DataOutputStream sendToServer = new DataOutputStream(clientSocket.getOutputStream());
            sendToServer.writeBytes("EVENTVIEW~" + "\n");
            
            BufferedReader chatFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            hasil = chatFromServer.readLine();
            System.out.println(hasil);
            
            DefaultTableModel tableModel = (DefaultTableModel) jTableEvent.getModel();
            tableModel.setRowCount(0);  
            String[] hasils = hasil.split("~");
            
            for (int i = 0; i < hasils.length; i += 12) {
                String[] row = new String[12];
                System.arraycopy(hasils, i, row, 0, 12);
                tableModel.addRow(row);
            }
        } catch(IOException ex) {
            Logger.getLogger(FormLogin.class.getName()).log(Level.SEVERE,null,ex);
        }
    }//GEN-LAST:event_jButtonViewEventActionPerformed

    private void jButtonReservationEventActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReservationEventActionPerformed
        try {
            String hasil;
            int id_event = Integer.parseInt(idEventTxt.getText());
            int quantity = Integer.parseInt(qtyTxt.getText());

            DefaultTableModel tableModel = (DefaultTableModel) jTableEvent.getModel();
            int rowCount = tableModel.getRowCount();
            boolean found = false;

            String eventName = "";
            String eventDate = "";
            String category = "";
            String status = "";
            int participantSlot = 0;
            int numberOfParticipant = 0;
            String openReservationDate = "";
            String closeReservationDate = "";
            int locationsId = 0;
            double price = 0.0;
            String description = "";

            for (int row = 0; row < rowCount; row++) {
                if (Integer.parseInt(String.valueOf(tableModel.getValueAt(row, 0))) == id_event) {
                    found = true;
                    eventName = String.valueOf(tableModel.getValueAt(row, 1));

                    String rawEventDate = String.valueOf(tableModel.getValueAt(row, 2));
                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date date = inputFormat.parse(rawEventDate);
                        eventDate = outputFormat.format(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        eventDate = rawEventDate; 
                    }

                    category = String.valueOf(tableModel.getValueAt(row, 3));
                    status = String.valueOf(tableModel.getValueAt(row, 4));
                    participantSlot = Integer.parseInt(String.valueOf(tableModel.getValueAt(row, 5)));
                    numberOfParticipant = Integer.parseInt(String.valueOf(tableModel.getValueAt(row, 6)));
                    openReservationDate = String.valueOf(tableModel.getValueAt(row, 7));
                    closeReservationDate = String.valueOf(tableModel.getValueAt(row, 8));
                    locationsId = Integer.parseInt(String.valueOf(tableModel.getValueAt(row, 9)));
                    price = Double.parseDouble(String.valueOf(tableModel.getValueAt(row, 10)));
                    description = String.valueOf(tableModel.getValueAt(row, 11));
                    break;
                }
            }

            if (!found) {
                JOptionPane.showMessageDialog(this, "Event ID not found in table.");
                return;
            }

            Socket clientSocket = new Socket("localhost", 6666);
            DataOutputStream sendToServer = new DataOutputStream(clientSocket.getOutputStream());

            FormMenu menu = new FormMenu();
            String dataToSend = "EVENTRESERVATION~" 
                + menu.id_user + "~" 
                + id_event + "~" 
                + quantity + "~" 
                + eventName + "~" 
                + eventDate + "~" 
                + category + "~" 
                + status + "~" 
                + participantSlot + "~" 
                + numberOfParticipant + "~" 
                + openReservationDate + "~" 
                + closeReservationDate + "~" 
                + locationsId + "~" 
                + price + "~" 
                + description + "\n";
        
            sendToServer.writeBytes(dataToSend);

            BufferedReader chatFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            hasil = chatFromServer.readLine();
            System.out.println(hasil);

            String[] hasils = hasil.split("~");
            
            if (hasils[0].equals("TRUE")) {
                JOptionPane.showMessageDialog(this, "Reservation successful!");
                FormMenu form = new FormMenu();
                form.balance = Double.parseDouble(hasils[1]);
                System.out.println(form.balance);
            } else {
                JOptionPane.showMessageDialog(this, "Reservation failed");
                FormMenu form = new FormMenu();
                form.balance = Double.parseDouble(hasils[1]);
                System.out.println(form.balance);
            }
        } catch (IOException ex) {
            Logger.getLogger(FormLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonReservationEventActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        FormMenu home = new FormMenu();
        home.show();
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FormEvent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormEvent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormEvent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormEvent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormEvent().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField idEventTxt;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonReservationEvent;
    private javax.swing.JButton jButtonViewEvent;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableEvent;
    private javax.swing.JTextField qtyTxt;
    // End of variables declaration//GEN-END:variables
}
