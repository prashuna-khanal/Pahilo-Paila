/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbproject/ide-file-targets/nb-generated/GUIForms/JFrame.java to edit this template
 */
package pahilopaila.view;

import java.awt.Color;
import java.awt.Cursor;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author LENOVO
 */
public class Dashboard_Recruiters extends javax.swing.JFrame {

    // State variables for each label to track hover and pressed states
    private boolean dashboardPressed = false, dashboardHovered = false;
    private boolean vacancyPressed = false, vacancyHovered = false;
    private boolean trainingPressed = false, trainingHovered = false;
    private boolean messagePressed = false, messageHovered = false;
    private boolean appliccationPressed = false, appliccationHovered = false;
    private boolean settingsPressed = false, settingsHovered = false;
    private boolean myAccountPressed = false, myAccountHovered = false;
    private boolean signOutPressed = false, signOutHovered = false;

    /**
     * Creates new form Dashboard_Recruiters
     */
    public Dashboard_Recruiters() {
        initComponents();
    }

    // Helper method to create styled JLabel with rounded corners and click/hover effects
    private JLabel createStyledLabel(String text, String iconPath, Runnable onClick) {
        JLabel label = new JLabel(text) {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
                g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                // Set background color based on state
                if (this == dashboard && dashboardPressed || this == vacancy && vacancyPressed ||
                    this == training && trainingPressed || this == message && messagePressed ||
                    this == appliccation && appliccationPressed || this == settings && settingsPressed ||
                    this == myAccount && myAccountPressed || this == signOut && signOutPressed) {
                    g2d.setColor(new Color(200, 200, 200)); // Light gray when pressed
                } else if (this == dashboard && dashboardHovered || this == vacancy && vacancyHovered ||
                           this == training && trainingHovered || this == message && messageHovered ||
                           this == appliccation && appliccationHovered || this == settings && settingsHovered ||
                           this == myAccount && myAccountHovered || this == signOut && signOutHovered) {
                    g2d.setColor(new Color(230, 230, 230)); // Slightly darker gray on hover
                } else {
                    g2d.setColor(new Color(255, 255, 255)); // Default white background
                }
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12); // Rounded corners
                super.paintComponent(g);
            }
        };
        label.setFont(new java.awt.Font("Segoe UI", 1, 14)); // Reverted font size back to 14
        label.setForeground(new Color(45, 22, 116)); // #2d1674
        label.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconPath)));
        label.setOpaque(false);
        label.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 15, 8, 15)); // Reverted padding
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        label.addMouseListener(new java.awt.event.MouseAdapter() {
            private Timer pressTimer;

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (label == dashboard) dashboardPressed = true;
                else if (label == vacancy) vacancyPressed = true;
                else if (label == training) trainingPressed = true;
                else if (label == message) messagePressed = true;
                else if (label == appliccation) appliccationPressed = true;
                else if (label == settings) settingsPressed = true;
                else if (label == myAccount) myAccountPressed = true;
                else if (label == signOut) signOutPressed = true;
                System.out.println(text + " pressed");
                label.repaint();
                if (pressTimer != null) {
                    pressTimer.cancel();
                }
                pressTimer = new Timer();
                pressTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (label == dashboard) dashboardPressed = false;
                        else if (label == vacancy) vacancyPressed = false;
                        else if (label == training) trainingPressed = false;
                        else if (label == message) messagePressed = false;
                        else if (label == appliccation) appliccationPressed = false;
                        else if (label == settings) settingsPressed = false;
                        else if (label == myAccount) myAccountPressed = false;
                        else if (label == signOut) signOutPressed = false;
                        label.repaint();
                    }
                }, 200); // 200ms delay
            }
            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                System.out.println(text + " released");
            }
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (label == dashboard) dashboardHovered = true;
                else if (label == vacancy) vacancyHovered = true;
                else if (label == training) trainingHovered = true;
                else if (label == message) messageHovered = true;
                else if (label == appliccation) appliccationHovered = true;
                else if (label == settings) settingsHovered = true;
                else if (label == myAccount) myAccountHovered = true;
                else if (label == signOut) signOutHovered = true;
                System.out.println(text + " hovered");
                label.repaint();
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (label == dashboard) dashboardHovered = false;
                else if (label == vacancy) vacancyHovered = false;
                else if (label == training) trainingHovered = false;
                else if (label == message) messageHovered = false;
                else if (label == appliccation) appliccationHovered = false;
                else if (label == settings) settingsHovered = false;
                else if (label == myAccount) myAccountHovered = false;
                else if (label == signOut) signOutHovered = false;
                System.out.println(text + " hover exited");
                label.repaint();
            }
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                System.out.println(text + " clicked");
                if (onClick != null) {
                    onClick.run();
                }
            }
        });
        return label;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Searchfield = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        username = new javax.swing.JLabel();
        email = new javax.swing.JLabel();
        profileIcon = new javax.swing.JLabel();
        content = new javax.swing.JPanel();
        messagePanel = new javax.swing.JPanel();
        find = new javax.swing.JLabel();
        right = new javax.swing.JLabel();
        getStarted = new javax.swing.JButton();
        learnMore = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        featurePanel = new javax.swing.JPanel();
        logo = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        dashboard = new javax.swing.JLabel();
        vacancy = new javax.swing.JLabel();
        appliccation = new javax.swing.JLabel();
        settings = new javax.swing.JLabel();
        myAccount = new javax.swing.JLabel();
        signOut = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setAutoRequestFocus(false);
        setBackground(new java.awt.Color(255, 255, 255));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Searchfield.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        Searchfield.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchfieldActionPerformed(evt);
            }
        });
        getContentPane().add(Searchfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 30, 280, 30));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/logo/search.jpg"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 30, 30, 30));

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/logo/filter.png"))); // NOI18N
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 30, 30, 30));

        username.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        username.setForeground(new java.awt.Color(0, 0, 102));
        username.setText("Ram Kumar");
        getContentPane().add(username, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 20, -1, 30));

        email.setText("@ramkumar");
        getContentPane().add(email, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 50, -1, -1));

        profileIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/logo/ram.png"))); // NOI18N
        getContentPane().add(profileIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 10, 40, 60));

        content.setBackground(new java.awt.Color(245, 245, 245));

        messagePanel.setBackground(new java.awt.Color(0, 4, 80));

        find.setBackground(new java.awt.Color(0, 0, 102));
        find.setFont(new java.awt.Font("Segoe UI Symbol", 1, 24)); // NOI18N
        find.setForeground(new java.awt.Color(255, 255, 255));
        find.setText("Find the right people  ");

        right.setBackground(new java.awt.Color(0, 0, 102));
        right.setFont(new java.awt.Font("Segoe UI Symbol", 1, 24)); // NOI18N
        right.setForeground(new java.awt.Color(255, 255, 255));
        right.setText("for the right Job");
        right.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        getStarted.setForeground(new java.awt.Color(0, 0, 102));
        getStarted.setText("Get Started");
        getStarted.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getStartedActionPerformed(evt);
            }
        });

        learnMore.setBackground(new java.awt.Color(0, 4, 80));
        learnMore.setForeground(new java.awt.Color(255, 255, 255));
        learnMore.setText("Learn More");
        learnMore.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        learnMore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                learnMoreActionPerformed(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/logo/3man.png"))); // NOI18N
        jLabel1.setText("jLabel1");

        javax.swing.GroupLayout messagePanelLayout = new javax.swing.GroupLayout(messagePanel);
        messagePanel.setLayout(messagePanelLayout);
        messagePanelLayout.setHorizontalGroup(
            messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(messagePanelLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(right, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(find, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(messagePanelLayout.createSequentialGroup()
                        .addComponent(getStarted, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(learnMore, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 165, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );
        messagePanelLayout.setVerticalGroup(
            messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(messagePanelLayout.createSequentialGroup()
                .addGroup(messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(messagePanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(find)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(right)
                        .addGap(18, 18, 18)
                        .addGroup(messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(learnMore, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(getStarted, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                    .addGroup(messagePanelLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabel1)))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout contentLayout = new javax.swing.GroupLayout(content);
        content.setLayout(contentLayout);
        contentLayout.setHorizontalGroup(
            contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contentLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(messagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        contentLayout.setVerticalGroup(
            contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contentLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(messagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(content, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 70, 700, 400));

        featurePanel.setBackground(new java.awt.Color(255, 255, 255));

        logo.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/pahilopaila_logo.png"))); // NOI18N

        javax.swing.GroupLayout logoLayout = new javax.swing.GroupLayout(logo);
        logo.setLayout(logoLayout);
        logoLayout.setHorizontalGroup(
            logoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logoLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        logoLayout.setVerticalGroup(
            logoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, logoLayout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(14, 14, 14))
        );

        dashboard.setBackground(new java.awt.Color(0, 51, 153));
        dashboard.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        dashboard.setForeground(new java.awt.Color(102, 102, 102));
        dashboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/logo/dashboard.jpg"))); // NOI18N
        dashboard.setText("Dashboard");

        vacancy.setBackground(new java.awt.Color(0, 51, 153));
        vacancy.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        vacancy.setForeground(new java.awt.Color(102, 102, 102));
        vacancy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/logo/vacancy.png"))); // NOI18N
        vacancy.setText("Vacancy");

        appliccation.setBackground(new java.awt.Color(0, 51, 153));
        appliccation.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        appliccation.setForeground(new java.awt.Color(102, 102, 102));
        appliccation.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/logo/application.png"))); // NOI18N
        appliccation.setText("Applications");

        settings.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        settings.setForeground(new java.awt.Color(102, 102, 102));
        settings.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/logo/setting.png"))); // NOI18N
        settings.setText("Settings");

        myAccount.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        myAccount.setForeground(new java.awt.Color(102, 102, 102));
        myAccount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/logo/account.png"))); // NOI18N
        myAccount.setText("My Account");
        myAccount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                myAccountMouseClicked(evt);
            }
        });

        signOut.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        signOut.setForeground(new java.awt.Color(102, 102, 102));
        signOut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/logo/signout.png"))); // NOI18N
        signOut.setText("Sign Out");

        javax.swing.GroupLayout featurePanelLayout = new javax.swing.GroupLayout(featurePanel);
        featurePanel.setLayout(featurePanelLayout);
        featurePanelLayout.setHorizontalGroup(
            featurePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(featurePanelLayout.createSequentialGroup()
                .addComponent(logo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 9, Short.MAX_VALUE))
            .addGroup(featurePanelLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(featurePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(appliccation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(vacancy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dashboard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(settings, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(signOut, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(myAccount, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        featurePanelLayout.setVerticalGroup(
            featurePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(featurePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(logo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(dashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(vacancy, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(appliccation, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(settings, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(85, 85, 85)
                .addComponent(myAccount, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(signOut, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(55, Short.MAX_VALUE))
        );

        getContentPane().add(featurePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 150, 470));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void SearchfieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchfieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SearchfieldActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void getStartedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getStartedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_getStartedActionPerformed

    private void learnMoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_learnMoreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_learnMoreActionPerformed

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
            java.util.logging.Logger.getLogger(Dashboard_Recruiters.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Dashboard_Recruiters.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Dashboard_Recruiters.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Dashboard_Recruiters.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Dashboard_Recruiters().setVisible(true);
            }
        });
    }

    // This is the content when My Account is pressed 
    
    private void showMyAccountPanel() {
        // Main panel for the "My Account" section
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(245, 245, 245)); // Light grey background
        mainPanel.setLayout(new java.awt.BorderLayout(15, 15)); // Increased gaps for an airy feel
        mainPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15)); // Outer padding

        // Header panel with gradient background
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
                g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                java.awt.GradientPaint gp = new java.awt.GradientPaint(
                    0, 0, new Color(0, 4, 80), // Dark blue
                    0, getHeight(), new Color(0, 20, 120) // Slightly lighter blue
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setPreferredSize(new java.awt.Dimension(680, 70)); // Taller header
        headerPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 25, 20));

        JLabel headerLabel = new JLabel("My Account");
        headerLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 24)); // Larger, bold font
        headerLabel.setForeground(Color.WHITE); // White text
        headerPanel.add(headerLabel);

        // Center-align wrapper panel for the form
        JPanel centerWrapper = new JPanel();
        centerWrapper.setBackground(new Color(245, 245, 245));
        centerWrapper.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        // Form panel with a card-like appearance
        JPanel formPanel = new JPanel();
        formPanel.setBackground(new Color(252, 252, 252)); // Soft off-white background
        formPanel.setLayout(new java.awt.GridBagLayout());
        formPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20), // Inner padding
            javax.swing.BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true) // Rounded border
        ));
        // Add a subtle shadow effect
        formPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 2, new Color(180, 180, 180, 100)), // Shadow
            formPanel.getBorder()
        ));

        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.insets = new java.awt.Insets(15, 15, 15, 15); // Adjusted padding for a more compact look
        gbc.fill = java.awt.GridBagConstraints.NONE; // No horizontal fill to allow centering
        gbc.anchor = java.awt.GridBagConstraints.CENTER; // Center-align all components

        // Username row: Icon + Label + Field
        JPanel usernameRow = new JPanel();
        usernameRow.setBackground(new Color(252, 252, 252));
        usernameRow.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));

        JLabel usernameIcon = new JLabel();
        try {
            javax.swing.ImageIcon icon = new javax.swing.ImageIcon(
                "C:\\Users\\LENOVO\\Documents\\NetBeansProjects\\Pahilo-Paila\\src\\Image\\profile-user .png"
            );
            java.awt.Image scaledImage = icon.getImage().getScaledInstance(24, 24, java.awt.Image.SCALE_SMOOTH);
            usernameIcon.setIcon(new javax.swing.ImageIcon(scaledImage));
            usernameIcon.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 5)); // Padding
        } catch (Exception e) {
            System.out.println("Error loading username icon: " + e.getMessage());
            usernameIcon.setText("U");
        }
        usernameRow.add(usernameIcon);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13)); // Smaller label font
        usernameLabel.setForeground(new Color(0, 0, 102)); // Dark blue text
        usernameRow.add(usernameLabel);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(usernameRow, gbc);

        JTextField usernameField = new JTextField(25); // Increased columns for even longer field
        usernameField.setText("Ram Kumar");
        usernameField.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11)); // Smaller font
        usernameField.setBackground(new Color(245, 245, 245));
        usernameField.setPreferredSize(new java.awt.Dimension(usernameField.getPreferredSize().width, 25)); // Further reduced height
        usernameField.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true), // Rounded border
            javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8) // Adjusted padding for smaller field
        ));
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(usernameField, gbc);

        // Password row: Icon + Label + Field
        JPanel passwordRow = new JPanel();
        passwordRow.setBackground(new Color(252, 252, 252));
        passwordRow.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));

        JLabel passwordIcon = new JLabel();
        try {
            javax.swing.ImageIcon icon = new javax.swing.ImageIcon(
                "C:\\Users\\LENOVO\\Documents\\NetBeansProjects\\Pahilo-Paila\\src\\Image\\locked-computer.png"
            );
            java.awt.Image scaledImage = icon.getImage().getScaledInstance(24, 24, java.awt.Image.SCALE_SMOOTH);
            passwordIcon.setIcon(new javax.swing.ImageIcon(scaledImage));
            passwordIcon.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 5));
        } catch (Exception e) {
            System.out.println("Error loading password icon: " + e.getMessage());
            passwordIcon.setText("P");
        }
        passwordRow.add(passwordIcon);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13)); // Smaller font
        passwordLabel.setForeground(new Color(0, 0, 102));
        passwordRow.add(passwordLabel);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(passwordRow, gbc);

        JPasswordField passwordField = new JPasswordField(25); // Increased columns for longer field
        passwordField.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11)); // Smaller font
        passwordField.setBackground(new Color(245, 245, 245));
        passwordField.setPreferredSize(new java.awt.Dimension(passwordField.getPreferredSize().width, 25)); // Further reduced height
        passwordField.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true),
            javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8) // Adjusted padding
        ));
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(passwordField, gbc);

        // New Password row: Icon + Label + Field
        JPanel newPasswordRow = new JPanel();
        newPasswordRow.setBackground(new Color(252, 252, 252));
        newPasswordRow.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));

        JLabel newPasswordIcon = new JLabel();
        try {
            javax.swing.ImageIcon icon = new javax.swing.ImageIcon(
                "C:\\Users\\LENOVO\\Documents\\NetBeansProjects\\Pahilo-Paila\\src\\Image\\locked-computer.png"
            );
            java.awt.Image scaledImage = icon.getImage().getScaledInstance(24, 24, java.awt.Image.SCALE_SMOOTH);
            newPasswordIcon.setIcon(new javax.swing.ImageIcon(scaledImage));
            newPasswordIcon.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 5));
        } catch (Exception e) {
            System.out.println("Error loading new password icon: " + e.getMessage());
            newPasswordIcon.setText("NP");
        }
        newPasswordRow.add(newPasswordIcon);

        JLabel changePasswordLabel = new JLabel("New Password:");
        changePasswordLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13)); // Smaller font
        changePasswordLabel.setForeground(new Color(0, 0, 102));
        newPasswordRow.add(changePasswordLabel);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(newPasswordRow, gbc);

        JPasswordField changePasswordField = new JPasswordField(25); // Increased columns for longer field
        changePasswordField.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11)); // Smaller font
        changePasswordField.setBackground(new Color(245, 245, 245));
        changePasswordField.setPreferredSize(new java.awt.Dimension(changePasswordField.getPreferredSize().width, 25)); // Further reduced height
        changePasswordField.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true),
            javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8) // Adjusted padding
        ));
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(changePasswordField, gbc);

        // Update button with hover effect
        javax.swing.JButton updateButton = new javax.swing.JButton("Update") {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
                g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2d.setColor(new Color(0, 20, 120)); // Lighter blue on hover
                } else {
                    g2d.setColor(new Color(0, 4, 80)); // Default dark blue
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        updateButton.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13)); // Smaller font
        updateButton.setForeground(Color.WHITE);
        updateButton.setContentAreaFilled(false); // Custom painting
        updateButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 20, 8, 20)); // Adjusted padding
        updateButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        updateButton.setFocusPainted(false);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = java.awt.GridBagConstraints.NONE;
        gbc.anchor = java.awt.GridBagConstraints.CENTER; // Center-align the button
        formPanel.add(updateButton, gbc);

        // Add action listener to the update button
        updateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                System.out.println("Update button clicked!");
                // Add logic to update the username/password here if needed
            }
        });

        // Add the form panel to the center wrapper
        centerWrapper.add(formPanel);

        // Add the header and center wrapper to the main panel
        mainPanel.add(headerPanel, java.awt.BorderLayout.NORTH);
        mainPanel.add(centerWrapper, java.awt.BorderLayout.CENTER);

        // Update the content panel
        content.removeAll();
        content.setLayout(new java.awt.BorderLayout());
        content.add(mainPanel, java.awt.BorderLayout.CENTER);
        content.revalidate();
        content.repaint();

        // Debug output
        System.out.println("My Account panel added. Main panel bounds: " + mainPanel.getBounds());
        System.out.println("Content panel bounds: " + content.getBounds());
        System.out.println("Content panel visible: " + content.isVisible());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Searchfield;
    public javax.swing.JLabel appliccation;
    private javax.swing.JPanel content;
    public javax.swing.JLabel dashboard;
    private javax.swing.JLabel email;
    private javax.swing.JPanel featurePanel;
    private javax.swing.JLabel find;
    private javax.swing.JButton getStarted;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JButton learnMore;
    private javax.swing.JPanel logo;
    private javax.swing.JPanel messagePanel;
    private javax.swing.JLabel myAccount;
    private javax.swing.JLabel profileIcon;
    private javax.swing.JLabel right;
    private javax.swing.JLabel settings;
    private javax.swing.JLabel signOut;
    private javax.swing.JLabel username;
    public javax.swing.JLabel vacancy;
    // End of variables declaration//GEN-END:variables
}