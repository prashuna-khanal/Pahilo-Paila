package pahilopaila.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import javax.swing.*;
import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;
import pahilopaila.Controller.Dashboard_RecruitersController; // Updated import for controller

/**
 * Dashboard view for recruiters.
 */
public class Dashboard_Recruiters extends javax.swing.JFrame {

    private Dashboard_RecruitersController controller; // Instance of the controller

    // State variables for label hover and pressed states
    private boolean dashboardPressed = false, dashboardHovered = false;
    private boolean vacancyPressed = false, vacancyHovered = false;
    private boolean appliccationPressed = false, appliccationHovered = false;
    private boolean settingsPressed = false, settingsHovered = false;
    private boolean myAccountPressed = false, myAccountHovered = false;
    private boolean signOutPressed = false, signOutHovered = false;
    private boolean isVacancyPosted = false;
    

    /**
     * Creates new form Dashboard_Recruiters
     */
    public Dashboard_Recruiters() {
        initComponents();
        controller = new Dashboard_RecruitersController(this); // Initialize controller
    }

    // Helper method to create styled JLabel with rounded corners and effects
    private JLabel createStyledLabel(String text, String iconPath, Runnable onClick) {
        JLabel label = new JLabel(text) {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
                g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                if (this == dashboard && dashboardPressed || this == vacancy && vacancyPressed ||
                    this == appliccation && appliccationPressed || this == settings && settingsPressed ||
                    this == myAccount && myAccountPressed || this == signOut && signOutPressed) {
                    g2d.setColor(new Color(200, 200, 200));
                } else if (this == dashboard && dashboardHovered || this == vacancy && vacancyHovered ||
                           this == appliccation && appliccationHovered || this == settings && settingsHovered ||
                           this == myAccount && myAccountHovered || this == signOut && signOutHovered) {
                    g2d.setColor(new Color(230, 230, 230));
                } else {
                    g2d.setColor(new Color(255, 255, 255));
                }
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                super.paintComponent(g);
            }
        };
        label.setFont(new java.awt.Font("Segoe UI", 1, 14));
        label.setForeground(new Color(45, 22, 116));
        try {
            label.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconPath)));
            label.setVerticalTextPosition(JLabel.CENTER);
            label.setHorizontalTextPosition(JLabel.RIGHT);
            label.setIconTextGap(10);
        } catch (Exception e) {
            System.out.println("Error loading icon for " + text + ": " + e.getMessage());
        }
        label.setOpaque(false);
        label.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 15, 8, 15));
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        label.addMouseListener(new java.awt.event.MouseAdapter() {
            private Timer pressTimer;

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (label == dashboard) dashboardPressed = true;
                else if (label == vacancy) vacancyPressed = true;
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
                        else if (label == appliccation) appliccationPressed = false;
                        else if (label == settings) settingsPressed = false;
                        else if (label == myAccount) myAccountPressed = false;
                        else if (label == signOut) signOutPressed = false;
                        label.repaint();
                    }
                }, 200);
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                System.out.println(text + " released");
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (label == dashboard) dashboardHovered = true;
                else if (label == vacancy) vacancyHovered = true;
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
     * Initialize the form. Generated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
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
        dashboard = createStyledLabel("Dashboard", "/Image/logo/dashboard.jpg", null);


        vacancy = createStyledLabel("Vacancy", "/Image/logo/vacancy.png", this::showVacancyPanel);
        appliccation = createStyledLabel("Applications", "/Image/logo/application.png", null);

        settings = createStyledLabel("Settings", "/Image/logo/setting.png", null);
        myAccount = createStyledLabel("My Account", "/Image/logo/account.png", this::showMyAccountPanel);
        signOut = createStyledLabel("Sign Out", "/Image/logo/signout.png", null);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setAutoRequestFocus(false);
        setBackground(new java.awt.Color(255, 255, 255));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Searchfield.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        Searchfield.addActionListener(evt -> SearchfieldActionPerformed(evt));
        getContentPane().add(Searchfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 30, 280, 30));

        try {
            jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/logo/search.jpg")));
        } catch (Exception e) {
            System.out.println("Error loading search icon: " + e.getMessage());
        }
        jButton1.addActionListener(evt -> jButton1ActionPerformed(evt));
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 30, 30, 30));

        try {
            jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/logo/filter.png")));
        } catch (Exception e) {
            System.out.println("Error loading filter icon: " + e.getMessage());
        }
        try {
            jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/logo/filter.png")));
        } catch (Exception e) {
            System.out.println("Error loading filter icon: " + e.getMessage());
        }
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 30, 30, 30));

        username.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18));
        username.setForeground(new java.awt.Color(0, 0, 102));
        username.setText("Ram Kumar");
        getContentPane().add(username, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 20, -1, 30));

        email.setFont(new java.awt.Font("Segoe UI", 0, 14));
        email.setText("@ramkumar");
        getContentPane().add(email, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 50, -1, -1));

        try {
            profileIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/logo/ram.png")));
        } catch (Exception e) {
            System.out.println("Error loading profile icon: " + e.getMessage());
        }
        try {
            profileIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/logo/ram.png")));
        } catch (Exception e) {
            System.out.println("Error loading profile icon: " + e.getMessage());
        }
        getContentPane().add(profileIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 10, 40, 60));

        content.setBackground(new java.awt.Color(245, 245, 245));

        messagePanel.setBackground(new java.awt.Color(0, 4, 80));

        find.setBackground(new java.awt.Color(0, 0, 102));
        find.setFont(new java.awt.Font("Segoe UI Symbol", 1, 24));
        find.setForeground(new java.awt.Color(255, 255, 255));
        find.setText("Find the right people");

        right.setBackground(new java.awt.Color(0, 0, 102));
        right.setFont(new java.awt.Font("Segoe UI Symbol", 1, 24));
        right.setForeground(new java.awt.Color(255, 255, 255));
        right.setText("for the right Job");
        right.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        getStarted.setForeground(new java.awt.Color(0, 0, 102));
        getStarted.setText("Get Started");
        getStarted.addActionListener(evt -> getStartedActionPerformed(evt));

        learnMore.setBackground(new java.awt.Color(0, 4, 80));
        learnMore.setForeground(new java.awt.Color(255, 255, 255));
        learnMore.setText("Learn More");
        learnMore.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(255, 255, 255)));
        learnMore.addActionListener(evt -> learnMoreActionPerformed(evt));

        try {
            jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/logo/3man.png")));
        } catch (Exception e) {
            System.out.println("Error loading 3man icon: " + e.getMessage());
        }
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
                            .addComponent(getStarted, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
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

        getContentPane().add(content, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 70, 700, 440));

        featurePanel.setBackground(new java.awt.Color(255, 255, 255));

        logo.setBackground(new java.awt.Color(255, 255, 255));

        try {
            jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/pahilopaila_logo.png")));
        } catch (Exception e) {
            System.out.println("Error loading logo icon: " + e.getMessage());
        }
        try {
            jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/pahilopaila_logo.png")));
        } catch (Exception e) {
            System.out.println("Error loading logo icon: " + e.getMessage());
        }

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
                    .addContainerGap(10, Short.MAX_VALUE)
                    .addComponent(jLabel4)
                    .addGap(10, 10, 10))
        );

        javax.swing.GroupLayout featurePanelLayout = new javax.swing.GroupLayout(featurePanel);
        featurePanel.setLayout(featurePanelLayout);
        featurePanelLayout.setHorizontalGroup(
            featurePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(featurePanelLayout.createSequentialGroup()
                    .addGap(15, 15, 15)
                    .addGroup(featurePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(signOut)
                        .addComponent(settings)
                        .addComponent(myAccount)
                        .addComponent(appliccation)
                        .addComponent(vacancy)
                        .addComponent(dashboard))
                    .addContainerGap(15, Short.MAX_VALUE))
                .addGroup(featurePanelLayout.createSequentialGroup()
                    .addComponent(logo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE))
        );
        featurePanelLayout.setVerticalGroup(
            featurePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(featurePanelLayout.createSequentialGroup()
                    .addContainerGap(5, Short.MAX_VALUE)
                    .addComponent(logo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(5, 5, 5)
                    .addComponent(dashboard)
                    .addGap(20, 20, 20)
                    .addComponent(vacancy)
                    .addGap(20, 20, 20)
                    .addComponent(appliccation)
                    .addGap(20, 20, 20)
                    .addComponent(settings)
                    .addGap(40, 40, 40)
                    .addComponent(myAccount)
                    .addGap(10, 10, 10)
                    .addComponent(signOut)
                    .addContainerGap(15, Short.MAX_VALUE))
        );

        getContentPane().add(featurePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 150, 510));

        pack();
    }

    private void SearchfieldActionPerformed(java.awt.event.ActionEvent evt) {
        // Handled in controller
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        // Handled in controller
    }

    private void getStartedActionPerformed(java.awt.event.ActionEvent evt) {
        // Handled in controller
    }

    private void learnMoreActionPerformed(java.awt.event.ActionEvent evt) {
        // Handled in controller
    }

    /**
     * Show the My Account panel
     */
    public void showMyAccountPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setLayout(new java.awt.BorderLayout(15, 15));
        mainPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setPreferredSize(new Dimension(660, 500)); // Ensure sufficient height

        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
                g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                java.awt.GradientPaint gp = new java.awt.GradientPaint(
                    0, 0, new Color(0, 4, 80),
                    0, getHeight(), new Color(0, 20, 120)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setPreferredSize(new java.awt.Dimension(680, 70));
        headerPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 25, 20));

        JLabel headerLabel = new JLabel("My Account");
        headerLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        JPanel centerWrapper = new JPanel();
        centerWrapper.setBackground(new Color(245, 245, 245));
        centerWrapper.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        JPanel formPanel = new JPanel();
        formPanel.setBackground(new Color(252, 252, 252));
        formPanel.setLayout(new java.awt.GridBagLayout());
        formPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 2, new Color(180, 180, 180, 100)),
            javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20),
                javax.swing.BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true)
            )
        ));

        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.insets = new java.awt.Insets(15, 15, 15, 15);
        gbc.fill = java.awt.GridBagConstraints.NONE;
        gbc.anchor = java.awt.GridBagConstraints.CENTER;

        JPanel usernameRow = new JPanel();
        usernameRow.setBackground(new Color(252, 252, 252));
        usernameRow.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));

        JLabel usernameIcon = new JLabel();
        try {
            javax.swing.ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("/Image/profile-user.png"));
           
            java.awt.Image scaledImage = icon.getImage().getScaledInstance(24, 24, java.awt.Image.SCALE_SMOOTH);
            usernameIcon.setIcon(new javax.swing.ImageIcon(scaledImage));
            usernameIcon.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 5));
        } catch (Exception e) {
            System.out.println("Error loading username icon: " + e.getMessage());
            usernameIcon.setText("U");
        }
        usernameRow.add(usernameIcon);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        usernameLabel.setForeground(new Color(0, 0, 102));
        usernameRow.add(usernameLabel);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(usernameRow, gbc);

        JTextField usernameField = new JTextField(25);
        usernameField.setText("Ram Kumar");
        usernameField.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11));
        usernameField.setBackground(new Color(245, 245, 245));
        usernameField.setPreferredSize(new java.awt.Dimension(usernameField.getPreferredSize().width, 25));
        usernameField.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true),
            javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(usernameField, gbc);

        JPanel passwordRow = new JPanel();
        passwordRow.setBackground(new Color(252, 252, 252));
        passwordRow.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));

        JLabel passwordIcon = new JLabel();
        try {
            javax.swing.ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("/Image/locked-computer.png"));
            java.awt.Image scaledImage = icon.getImage().getScaledInstance(24, 24, java.awt.Image.SCALE_SMOOTH);
            passwordIcon.setIcon(new javax.swing.ImageIcon(scaledImage));
            passwordIcon.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 5));
        } catch (Exception e) {
            System.out.println("Error loading password icon: " + e.getMessage());
            passwordIcon.setText("P");
        }
        passwordRow.add(passwordIcon);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        passwordLabel.setForeground(new Color(0, 0, 102));
        passwordRow.add(passwordLabel);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(passwordRow, gbc);

        JPasswordField passwordField = new JPasswordField(25);
        passwordField.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11));
        passwordField.setBackground(new Color(245, 245, 245));
        passwordField.setPreferredSize(new java.awt.Dimension(passwordField.getPreferredSize().width, 25));
        passwordField.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true),
            javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(passwordField, gbc);

        JPanel newPasswordRow = new JPanel();
        newPasswordRow.setBackground(new Color(252, 252, 252));
        newPasswordRow.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));

        JLabel newPasswordIcon = new JLabel();
        try {
            javax.swing.ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("/Image/locked-computer.png"));
            java.awt.Image scaledImage = icon.getImage().getScaledInstance(24, 24, java.awt.Image.SCALE_SMOOTH);
            newPasswordIcon.setIcon(new javax.swing.ImageIcon(scaledImage));
            newPasswordIcon.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 5));
        } catch (Exception e) {
            System.out.println("Error loading new password icon: " + e.getMessage());
            newPasswordIcon.setText("NP");
        }
        newPasswordRow.add(newPasswordIcon);

        JLabel changePasswordLabel = new JLabel("New Password:");
        changePasswordLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        changePasswordLabel.setForeground(new Color(0, 0, 102));
        newPasswordRow.add(changePasswordLabel);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(newPasswordRow, gbc);

        JPasswordField changePasswordField = new JPasswordField(25);
        changePasswordField.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11));
        changePasswordField.setBackground(new Color(245, 245, 245));
        changePasswordField.setPreferredSize(new java.awt.Dimension(changePasswordField.getPreferredSize().width, 25));
        changePasswordField.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true),
            javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(changePasswordField, gbc);

        JButton updateButton = new JButton("Update") {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
                g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2d.setColor(new Color(0, 20, 120));
                } else {
                    g2d.setColor(new Color(0, 4, 80));
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        updateButton.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        updateButton.setForeground(Color.WHITE);
        updateButton.setContentAreaFilled(false);
        updateButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 20, 8, 20));
        updateButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        updateButton.setFocusPainted(false);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = java.awt.GridBagConstraints.NONE;
        gbc.anchor = java.awt.GridBagConstraints.CENTER;
        formPanel.add(updateButton, gbc);

        updateButton.addActionListener(e -> {
            System.out.println("Update button clicked!");
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String newPassword = new String(changePasswordField.getPassword());
            // Add validation and update logic here
            JOptionPane.showMessageDialog(this, "User info updated!\nUsername: " + username);
        });

        centerWrapper.add(formPanel);
        mainPanel.add(headerPanel, java.awt.BorderLayout.NORTH);
        mainPanel.add(centerWrapper, java.awt.BorderLayout.CENTER);

        content.removeAll();
        content.setLayout(new java.awt.BorderLayout());
        content.add(mainPanel, java.awt.BorderLayout.CENTER);
        content.revalidate();
        content.repaint();

        System.out.println("My Account panel added. Main panel bounds: " + mainPanel.getBounds());
        System.out.println("Content panel bounds: " + content.getBounds());
        System.out.println("Content panel visible: " + content.isVisible());
    }
    private void showVacancyPanel() {
        // Main panel for the "Vacancy" section
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setLayout(new java.awt.BorderLayout(15, 15));
        mainPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Header panel with gradient background
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
                g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                java.awt.GradientPaint gp = new java.awt.GradientPaint(
                    0, 0, new Color(0, 4, 80),
                    0, getHeight(), new Color(0, 20, 120)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setPreferredSize(new java.awt.Dimension(680, 70));
        headerPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 25, 20));

        JLabel headerLabel = new JLabel("Vacancy Management");
        headerLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        // Center-align wrapper panel for the form
        JPanel centerWrapper = new JPanel();
        centerWrapper.setBackground(new Color(245, 245, 245));
        centerWrapper.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        // Form panel for vacancy details
        JPanel formPanel = new JPanel();
        formPanel.setBackground(new Color(252, 252, 252));
        formPanel.setLayout(new java.awt.GridBagLayout());
        formPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20),
            javax.swing.BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true)
        ));

        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.insets = new java.awt.Insets(15, 15, 15, 15);
        gbc.fill = java.awt.GridBagConstraints.NONE;
        gbc.anchor = java.awt.GridBagConstraints.CENTER;

        // Job Title row
        JPanel jobTitleRow = new JPanel();
        jobTitleRow.setBackground(new Color(252, 252, 252));
        jobTitleRow.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));
        JLabel jobTitleIcon = new JLabel();
        try {
            javax.swing.ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("/Image/logo/job.png"));
            java.awt.Image scaledImage = icon.getImage().getScaledInstance(24, 24, java.awt.Image.SCALE_SMOOTH);
            jobTitleIcon.setIcon(new javax.swing.ImageIcon(scaledImage));
        } catch (Exception e) {
            System.out.println("Error loading job title icon: " + e.getMessage());
            jobTitleIcon.setText("J");
        }
        jobTitleRow.add(jobTitleIcon);
        JLabel jobTitleLabel = new JLabel("Job Title:");
        jobTitleLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        jobTitleLabel.setForeground(new Color(0, 0, 102));
        jobTitleRow.add(jobTitleLabel);
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(jobTitleRow, gbc);

        JTextField jobTitleField = new JTextField(25);
        jobTitleField.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11));
        jobTitleField.setBackground(new Color(245, 245, 245));
        jobTitleField.setPreferredSize(new java.awt.Dimension(jobTitleField.getPreferredSize().width, 25));
        jobTitleField.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true),
            javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(jobTitleField, gbc);
        // Added: Status label to show posting result
        JLabel statusLabel = new JLabel();
        statusLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
        statusLabel.setForeground(Color.RED);
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(statusLabel, gbc);

        // Update status label based on isVacancyPosted
        if (isVacancyPosted) {
            statusLabel.setText("Vacancy posted successfully!");
            statusLabel.setForeground(Color.GREEN);
        } else {
            statusLabel.setText("Vacancy not posted yet.");
            statusLabel.setForeground(Color.RED);
        }

        // Post Vacancy button
        javax.swing.JButton postButton = new javax.swing.JButton("Post Vacancy") {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
                g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2d.setColor(new Color(0, 20, 120));
                } else {
                    g2d.setColor(new Color(0, 4, 80));
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        postButton.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        postButton.setForeground(Color.WHITE);
        postButton.setContentAreaFilled(false);
        postButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 20, 8, 20));
        postButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        postButton.setFocusPainted(false);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = java.awt.GridBagConstraints.NONE;
        gbc.anchor = java.awt.GridBagConstraints.CENTER;
        formPanel.add(postButton, gbc);

        // Modified: Action listener to update isVacancyPosted and refresh status
        postButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String jobTitle = jobTitleField.getText().trim();
                if (!jobTitle.isEmpty()) {
                    isVacancyPosted = true;
                    statusLabel.setText("Vacancy posted successfully!");
                    statusLabel.setForeground(Color.GREEN);
                    System.out.println("Vacancy posted: " + jobTitle);
                } else {
                    statusLabel.setText("Please enter a job title.");
                    statusLabel.setForeground(Color.RED);
                }
                formPanel.revalidate();
                formPanel.repaint();
            }
        });

        centerWrapper.add(formPanel);
        mainPanel.add(headerPanel, java.awt.BorderLayout.NORTH);
        mainPanel.add(centerWrapper, java.awt.BorderLayout.CENTER);

        content.removeAll();
        content.setLayout(new java.awt.BorderLayout());
        content.add(mainPanel, java.awt.BorderLayout.CENTER);
        content.revalidate();
        content.repaint();

        System.out.println("Vacancy panel added. Main panel bounds: " + mainPanel.getBounds());
    }

    /**
     * Main method to run the application
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Dashboard_Recruiters.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            Dashboard_Recruiters view = new Dashboard_Recruiters();
            Dashboard_RecruitersController controller = new Dashboard_RecruitersController(view);
            view.setVisible(true);
            controller.setUserInfo("Ram Kumar", "@ramkumar");
        });
    }

    // Variables declaration
    public javax.swing.JTextField Searchfield;
    public javax.swing.JLabel appliccation;
    public javax.swing.JPanel content;
    public javax.swing.JLabel dashboard;
    public javax.swing.JLabel email;
    private javax.swing.JPanel featurePanel;
    private javax.swing.JLabel find;
    public javax.swing.JButton getStarted;
    public javax.swing.JButton jButton1;
    public javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    public javax.swing.JButton learnMore;
    private javax.swing.JPanel logo;
    private javax.swing.JPanel messagePanel;
    public javax.swing.JLabel myAccount;
    private javax.swing.JLabel profileIcon;
    private javax.swing.JLabel right;
    public javax.swing.JLabel settings;
    public javax.swing.JLabel signOut;
    public javax.swing.JLabel username;
    public javax.swing.JLabel vacancy;
}

