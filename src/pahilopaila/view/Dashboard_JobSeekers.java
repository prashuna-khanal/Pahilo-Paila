package pahilopaila.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.util.Timer;
import java.util.TimerTask;
import pahilopaila.Controller.Dashboard_JobseekersController;

/**
 * Job Seeker Dashboard UI for the PahiloPaila application.
 */
public class Dashboard_JobSeekers extends javax.swing.JFrame {
    // State variables for label hover and pressed states
    private boolean dashboardPressed = false, dashboardHovered = false;
    private boolean vacancyPressed = false, vacancyHovered = false;
    private boolean CVPressed = false, CVHovered = false;
    private boolean settingsPressed = false, settingsHovered = false;
    private boolean myAccountPressed = false, myAccountHovered = false;
    private boolean signOutPressed = false, signOutHovered = false;
    private boolean isDarkMode = false; // Default to light mode

    // UI components
    public JTextField Searchfield;
    public JButton filter;
    public JButton search;
    public JLabel username;
    public JLabel email;
    public JPanel featurePanel;
    public JPanel logo;
    public JLabel jLabel4;
    public JLabel dashboard;
    public JLabel vacancy;
    public JLabel CV;
    public JLabel settings;
    public JLabel myAccount;
    public JLabel signOut;
    public JLabel profileIcon1;
    public JPanel content;
    public JPanel messagePanel;
    public JLabel jLabel1;
    public JLabel jLabel2;
    public JButton learnMore;
    public JButton getStarted;
    public JPanel jPanel1;
    public JLabel jLabel13;
    public JPanel jPanel2;
    public JLabel jLabel15;
    public JPanel jPanel3;
    public JLabel jLabel16;
    public JPanel jPanel4;
    public JLabel jLabel17;
    public JPanel jPanel9;
    public JLabel jLabel23;
    public JLabel jLabel24;
    public JPanel jPanel10;
    public JLabel jLabel25;
    public JPanel jPanel11;
    public JLabel jLabel26;
    public JPanel jPanel12;
    public JLabel jLabel27;
    public JLabel featured;
    public JButton see_all;
    public JPanel jPanel13;
    public JLabel jLabel29;
    public JLabel jLabel30;
    public JPanel jPanel14;
    public JLabel jLabel31;
    public JPanel jPanel15;
    public JLabel jLabel32;
    public JPanel jPanel16;
    public JLabel jLabel33;
    public JLabel jLabel34;

    // Popup menu for CV
    private JPopupMenu cvPopupMenu;
    public JMenuItem uploadCVItem; // Made public for controller access
    public JMenuItem viewCVItem;   // Made public for controller access

    public Dashboard_JobSeekers() {
        initComponents();
        setResizable(false);
        setSize(900, 600); // Fixed window size
        setLocationRelativeTo(null);
        applyTheme(); // Apply theme based on isDarkMode
    }

    // Helper method to create styled navigation labels
    private JLabel createStyledLabel(String text, String iconPath) {
        JLabel label = new JLabel(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color defaultBg = isDarkMode ? new Color(40, 40, 40) : new Color(240, 240, 240); // Darker default in dark mode
                Color hoverBg = isDarkMode ? new Color(60, 60, 60) : new Color(220, 220, 220);   // Slightly darker hover
                Color pressedBg = isDarkMode ? new Color(50, 50, 50) : new Color(200, 200, 200); // Slightly darker pressed

                if (this == dashboard && dashboardPressed || this == vacancy && vacancyPressed ||
                    this == CV && CVPressed || this == settings && settingsPressed ||
                    this == myAccount && myAccountPressed || this == signOut && signOutPressed) {
                    g2d.setColor(pressedBg);
                } else if (this == dashboard && dashboardHovered || this == vacancy && vacancyHovered ||
                           this == CV && CVHovered || this == settings && settingsHovered ||
                           this == myAccount && myAccountHovered || this == signOut && signOutHovered) {
                    g2d.setColor(hoverBg);
                } else {
                    g2d.setColor(defaultBg);
                }
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                super.paintComponent(g);
            }
        };
        label.setFont(new java.awt.Font("Segoe UI", 1, 14));
        label.setForeground(isDarkMode ? new Color(220, 220, 220) : new Color(51, 51, 51)); // Light text in dark mode
        try {
            label.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconPath)));
            label.setVerticalTextPosition(JLabel.CENTER);
            label.setHorizontalTextPosition(JLabel.RIGHT);
            label.setIconTextGap(10);
        } catch (Exception e) {
            System.out.println("Error loading icon for " + text + ": " + e.getMessage());
        }
        label.setOpaque(false); // Keep non-opaque to allow custom painting
        label.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 15, 8, 15));
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        label.addMouseListener(new MouseAdapter() {
            private Timer pressTimer;

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (label == dashboard) dashboardPressed = true;
                else if (label == vacancy) vacancyPressed = true;
                else if (label == CV) CVPressed = true;
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
                        else if (label == CV) CVPressed = false;
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
                else if (label == CV) CVHovered = true;
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
                else if (label == CV) CVHovered = false;
                else if (label == settings) settingsHovered = false;
                else if (label == myAccount) myAccountHovered = false;
                else if (label == signOut) signOutHovered = false;
                System.out.println(text + " hover exited");
                label.repaint();
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                System.out.println(text + " clicked");
                if (label == CV) {
                    cvPopupMenu.show(label, evt.getX(), evt.getY());
                }
            }
        });
        return label;
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        Searchfield = new javax.swing.JTextField();
        filter = new javax.swing.JButton();
        search = new javax.swing.JButton();
        username = new javax.swing.JLabel();
        email = new javax.swing.JLabel();
        featurePanel = new javax.swing.JPanel();
        logo = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        dashboard = createStyledLabel("Dashboard", "/Image/logo/dashboard.jpg");
        vacancy = createStyledLabel("Vacancy", "/Image/logo/vacancy.png");
        CV = createStyledLabel("CV", "/Image/logo/application.png");
        settings = createStyledLabel("Settings", "/Image/logo/setting.png");
        myAccount = createStyledLabel("My Account", "/Image/logo/account.png");
        signOut = createStyledLabel("Sign Out", "/Image/logo/signout.png");
        profileIcon1 = new javax.swing.JLabel();
        content = new javax.swing.JPanel();
        messagePanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        learnMore = new javax.swing.JButton();
        getStarted = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        featured = new javax.swing.JLabel();
        see_all = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();

        // Initialize CV popup menu
        cvPopupMenu = new JPopupMenu();
        uploadCVItem = new JMenuItem("Upload CV");
        viewCVItem = new JMenuItem("View CV");
        cvPopupMenu.add(uploadCVItem);
        cvPopupMenu.add(viewCVItem);
        updatePopupTheme(); // Apply initial theme to popup

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Searchfield.setBackground(isDarkMode ? new Color(37, 37, 38) : new Color(255, 255, 255));
        Searchfield.setForeground(isDarkMode ? new Color(220, 220, 220) : new Color(0, 0, 0));
        Searchfield.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(isDarkMode ? new Color(60, 60, 60) : new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        Searchfield.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        Searchfield.addActionListener(evt -> SearchfieldActionPerformed(evt));
        getContentPane().add(Searchfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 30, 280, 30));

        filter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/logo/filter.png")));
        filter.setBackground(isDarkMode ? new Color(0, 4, 80) : new Color(240, 240, 240));
        filter.setBorder(BorderFactory.createEmptyBorder());
        getContentPane().add(filter, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 30, 30, 30));

        search.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/logo/search.jpg")));
        search.setBackground(isDarkMode ? new Color(0, 4, 80) : new Color(240, 240, 240));
        search.setBorder(BorderFactory.createEmptyBorder());
        search.addActionListener(evt -> searchActionPerformed(evt));
        getContentPane().add(search, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 30, 30, 30));

        username.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18));
        username.setForeground(isDarkMode ? new Color(255, 255, 255) : new Color(0, 0, 102)); // White in dark mode
        username.setText("Ram Kumar");
        getContentPane().add(username, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 20, -1, 30));

        email.setForeground(isDarkMode ? new Color(255, 255, 255) : new Color(0, 0, 0)); // White in dark mode
        email.setText("@ramkumar");
        getContentPane().add(email, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 50, -1, -1));

        featurePanel.setBackground(isDarkMode ? new Color(40, 40, 40) : new Color(255, 255, 255));

        logo.setBackground(isDarkMode ? new Color(40, 40, 40) : new Color(255, 255, 255)); // Adaptive background
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/pahilopaila_logo.png")));
        // Add white background panel for logo in dark mode
        if (isDarkMode) {
            JPanel logoBg = new JPanel();
            logoBg.setBackground(new Color(200, 200, 200));
            logoBg.setOpaque(true);
            logoBg.setBounds(0, 0, logo.getWidth(), logo.getHeight());
            logo.add(logoBg);
            logo.setLayout(null); // Ensure absolute positioning
            jLabel4.setBounds(30, 18, 105, logo.getHeight() - 32); // Adjust position as needed
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
                    .addContainerGap(18, Short.MAX_VALUE)
                    .addComponent(jLabel4)
                    .addGap(14, 14, 14))
        );

        javax.swing.GroupLayout featurePanelLayout = new javax.swing.GroupLayout(featurePanel);
        featurePanel.setLayout(featurePanelLayout);
        featurePanelLayout.setHorizontalGroup(
            featurePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(logo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(featurePanelLayout.createSequentialGroup()
                    .addGap(25, 25, 25)
                    .addGroup(featurePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(dashboard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(vacancy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(CV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(settings, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(myAccount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(signOut, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap(30, Short.MAX_VALUE))
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
                    .addComponent(CV, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(settings, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(85, 85, 85)
                    .addComponent(myAccount, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(signOut, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(105, Short.MAX_VALUE))
        );

        getContentPane().add(featurePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 170, 520));

        profileIcon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/logo/ram.png")));
        getContentPane().add(profileIcon1, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 10, 40, 60));

        content.setBackground(isDarkMode ? new Color(35, 35, 35) : new Color(245, 245, 245));

        messagePanel.setBackground(new Color(0, 4, 80));
        jLabel1.setFont(new java.awt.Font("Segoe UI Symbol", 1, 24));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Discover Opportunities That ");
        jLabel2.setFont(new java.awt.Font("Segoe UI Symbol", 1, 24));
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Match Your Skill");
        jLabel2.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        learnMore.setBackground(new Color(0, 4, 80));
        learnMore.setForeground(new java.awt.Color(255, 255, 255));
        learnMore.setText("Learn More");
        learnMore.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(255, 255, 255)));
        getStarted.setBackground(new Color(255, 255, 255)); // Default light background
        getStarted.setForeground(isDarkMode ? new Color(200, 200, 200) : new Color(0, 0, 102)); // Adjusted for dark mode
        getStarted.setText("Get Started");

        javax.swing.GroupLayout messagePanelLayout = new javax.swing.GroupLayout(messagePanel);
        messagePanel.setLayout(messagePanelLayout);
        messagePanelLayout.setHorizontalGroup(
            messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(messagePanelLayout.createSequentialGroup()
                    .addGap(22, 22, 22)
                    .addGroup(messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel2)
                        .addComponent(jLabel1)
                        .addGroup(messagePanelLayout.createSequentialGroup()
                            .addComponent(getStarted)
                            .addGap(26, 26, 26)
                            .addComponent(learnMore, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap(276, Short.MAX_VALUE))
        );
        messagePanelLayout.setVerticalGroup(
            messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(messagePanelLayout.createSequentialGroup()
                    .addGap(5, 5, 5)
                    .addComponent(jLabel1)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel2)
                    .addGap(18, 18, 18)
                    .addGroup(messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(getStarted)
                        .addComponent(learnMore, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new Color(0, 4, 80));
        jLabel13.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 18));
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Japanese Instructor");
        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jLabel15.setForeground(isDarkMode ? new Color(180, 180, 180) : new Color(0, 0, 0));
        jLabel15.setText("14 Days Left");
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(43, 43, 43)
                    .addComponent(jLabel15)
                    .addContainerGap(52, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
        );

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jLabel16.setForeground(isDarkMode ? new Color(180, 180, 180) : new Color(0, 0, 0));
        jLabel16.setText("Part time");
        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(43, 43, 43)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(42, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
        );

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jLabel17.setForeground(isDarkMode ? new Color(180, 180, 180) : new Color(0, 0, 0));
        jLabel17.setText("Senior - Level");
        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGap(40, 40, 40)
                    .addComponent(jLabel17)
                    .addContainerGap(39, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(18, 18, 18)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel13)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap(13, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(14, 14, 14)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(63, 63, 63)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel9.setBackground(new Color(0, 4, 80));
        jLabel23.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 18));
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("BSc. Nurse");
        jLabel24.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 18));
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("Staff Nurse");
        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jLabel25.setForeground(isDarkMode ? new Color(180, 180, 180) : new Color(0, 0, 0));
        jLabel25.setText("4 Days Left");
        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                    .addContainerGap(46, Short.MAX_VALUE)
                    .addComponent(jLabel25)
                    .addGap(42, 42, 42))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
        );

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jLabel26.setForeground(isDarkMode ? new Color(180, 180, 180) : new Color(0, 0, 0));
        jLabel26.setText("Part time");
        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel11Layout.createSequentialGroup()
                    .addGap(49, 49, 49)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel26, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
        );

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jLabel27.setForeground(isDarkMode ? new Color(180, 180, 180) : new Color(0, 0, 0));
        jLabel27.setText("Junior - Level");
        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                    .addContainerGap(38, Short.MAX_VALUE)
                    .addComponent(jLabel27)
                    .addGap(26, 26, 26))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel27, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addGap(16, 16, 16)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel9Layout.createSequentialGroup()
                            .addGap(27, 27, 27)
                            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel24)
                                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addContainerGap(12, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel23)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel24)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(12, 12, 12)
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(15, 15, 15))
        );

        featured.setFont(new java.awt.Font("Microsoft Himalaya", 1, 36));
        featured.setForeground(isDarkMode ? new Color(180, 180, 180) : new Color(0, 0, 0));
        featured.setText("Featured Jobs");
        see_all.setBackground(new Color(0, 4, 80));
        see_all.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14));
        see_all.setForeground(isDarkMode ? new Color(200, 200, 200) : new Color(0, 0, 0));
        see_all.setText("See all");

        jPanel13.setBackground(new Color(0, 4, 80));
        jLabel29.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 18));
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setText("Salesman Mobile ");
        jLabel30.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 18));
        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setText("Point Sales and");
        jLabel34.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 18));
        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setText("Reparing");
        jLabel31.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jLabel31.setForeground(isDarkMode ? new Color(180, 180, 180) : new Color(0, 0, 0));
        jLabel31.setText("6 Days Left");
        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel14Layout.createSequentialGroup()
                    .addGap(44, 44, 44)
                    .addComponent(jLabel31)
                    .addContainerGap(49, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel31, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
        );

        jLabel32.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jLabel32.setForeground(isDarkMode ? new Color(180, 180, 180) : new Color(0, 0, 0));
        jLabel32.setText("Full time");
        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel15Layout.createSequentialGroup()
                    .addGap(43, 43, 43)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(42, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel32, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
        );

        jLabel33.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jLabel33.setForeground(isDarkMode ? new Color(180, 180, 180) : new Color(0, 0, 0));
        jLabel33.setText("Mid - Level");
        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel16Layout.createSequentialGroup()
                    .addGap(40, 40, 40)
                    .addComponent(jLabel33)
                    .addContainerGap(46, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel33, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel13Layout.createSequentialGroup()
                    .addGap(16, 16, 16)
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel13Layout.createSequentialGroup()
                            .addGap(6, 6, 6)
                            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel34)
                                .addComponent(jLabel30)
                                .addComponent(jLabel29)))
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel13Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel29)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel30)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel34)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(12, 12, 12)
                    .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(15, 15, 15))
        );

        javax.swing.GroupLayout contentLayout = new javax.swing.GroupLayout(content);
        content.setLayout(contentLayout);
        contentLayout.setHorizontalGroup(
            contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(contentLayout.createSequentialGroup()
                    .addGap(22, 22, 22)
                    .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(contentLayout.createSequentialGroup()
                            .addComponent(featured, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(351, 351, 351)
                            .addComponent(see_all))
                        .addComponent(messagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(contentLayout.createSequentialGroup()
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap(36, Short.MAX_VALUE))
        );
        contentLayout.setVerticalGroup(
            contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(contentLayout.createSequentialGroup()
                    .addGap(22, 22, 22)
                    .addComponent(messagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(featured)
                        .addComponent(see_all))
                    .addGap(18, 18, 18)
                    .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(38, Short.MAX_VALUE))
        );

        getContentPane().add(content, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 80, 680, 430));

        pack();
    }

    private void applyTheme() {
        getContentPane().setBackground(isDarkMode ? new Color(35, 35, 35) : new Color(255, 255, 255));
        featurePanel.setBackground(isDarkMode ? new Color(40, 40, 40) : new Color(255, 255, 255));
        logo.setBackground(isDarkMode ? new Color(35, 35, 35) : new Color(255, 255, 255)); // Default background, rectangle will handle contrast

        // Handle logo visibility with a rectangle background
        if (isDarkMode) {
            logo.removeAll(); // Clear existing components
            // Add a rectangular panel behind the logo
            JPanel logoRect = new JPanel();
            logoRect.setBackground(new Color(200, 200, 200)); // Light gray rectangle
            logoRect.setOpaque(true);
            logoRect.setBounds(20, 10, 125, logo.getHeight() - 20); // Adjust size and position to frame the logo
            logo.add(logoRect);

            // Load and add logo image
            if (jLabel4.getIcon() == null) {
                try {
                    jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/pahilopaila_logo.png")));
                    jLabel4.setOpaque(false); // Ensure no unwanted background
                } catch (Exception e) {
                    System.out.println("Logo image not found: " + e.getMessage()); // Debug image loading
                }
            }
            jLabel4.setBounds(30, 18, 105, logo.getHeight() - 32); // Position logo over the rectangle
            logo.add(jLabel4);
            logo.setLayout(null); // Use absolute positioning
        } else {
            logo.removeAll(); // Clear existing components
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
        }
        updatePopupTheme();
    }

    private void updatePopupTheme() {
        cvPopupMenu.setBackground(isDarkMode ? new Color(45, 45, 48) : new Color(255, 255, 255));
        cvPopupMenu.setBorder(BorderFactory.createLineBorder(isDarkMode ? new Color(60, 60, 60) : new Color(200, 200, 200)));
        uploadCVItem.setForeground(isDarkMode ? new Color(220, 220, 220) : new Color(0, 0, 0));
        viewCVItem.setForeground(isDarkMode ? new Color(220, 220, 220) : new Color(0, 0, 0));
    }

    // Method to toggle dark mode (can be called from a UI button or controller)
    public void toggleDarkMode(boolean enableDarkMode) {
        isDarkMode = enableDarkMode;
        applyTheme();

        Searchfield.setBackground(isDarkMode ? new Color(40, 40, 40) : new Color(255, 255, 255));
        Searchfield.setForeground(isDarkMode ? new Color(200, 200, 200) : new Color(0, 0, 0));
        Searchfield.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(isDarkMode ? new Color(60, 60, 60) : new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));

        filter.setBackground(isDarkMode ? new Color(25, 118, 210) : new Color(240, 240, 240));
        search.setBackground(isDarkMode ? new Color(25, 118, 210) : new Color(240, 240, 240));

        // Ensure username and email are visible
        username.setForeground(isDarkMode ? new Color(245, 245, 245) : new Color(0, 0, 102)); // Bright white
        email.setForeground(isDarkMode ? new Color(245, 245, 245) : new Color(0, 0, 0));     // Bright white
        // Set parent panel background for username/email (adjust panel name if needed, e.g., headerPanel)
        JPanel topRightPanel = (JPanel) username.getParent(); // Replace with actual panel name if different
        if (topRightPanel != null) {
            topRightPanel.setBackground(isDarkMode ? new Color(35, 35, 35) : new Color(255, 255, 255));
            topRightPanel.repaint();
        }

        content.setBackground(isDarkMode ? new Color(35, 35, 35) : new Color(245, 245, 245));
        getStarted.setForeground(isDarkMode ? new Color(200, 200, 200) : new Color(0, 0, 102));
        featured.setForeground(isDarkMode ? new Color(180, 180, 180) : new Color(0, 0, 0));
        see_all.setForeground(isDarkMode ? new Color(200, 200, 200) : new Color(0, 0, 0));

        jLabel15.setForeground(isDarkMode ? new Color(180, 180, 180) : new Color(0, 0, 0));
        jLabel16.setForeground(isDarkMode ? new Color(180, 180, 180) : new Color(0, 0, 0));
        jLabel17.setForeground(isDarkMode ? new Color(180, 180, 180) : new Color(0, 0, 0));
        jLabel25.setForeground(isDarkMode ? new Color(180, 180, 180) : new Color(0, 0, 0));
        jLabel26.setForeground(isDarkMode ? new Color(180, 180, 180) : new Color(0, 0, 0));
        jLabel27.setForeground(isDarkMode ? new Color(180, 180, 180) : new Color(0, 0, 0));
        jLabel31.setForeground(isDarkMode ? new Color(180, 180, 180) : new Color(0, 0, 0));
        jLabel32.setForeground(isDarkMode ? new Color(180, 180, 180) : new Color(0, 0, 0));
        jLabel33.setForeground(isDarkMode ? new Color(180, 180, 180) : new Color(0, 0, 0));

        // Repaint all styled labels to apply new theme
        dashboard.repaint();
        vacancy.repaint();
        CV.repaint();
        settings.repaint();
        myAccount.repaint();
        signOut.repaint();
    }

    private void SearchfieldActionPerformed(java.awt.event.ActionEvent evt) {
        // Placeholder for search field action
    }

    private void searchActionPerformed(java.awt.event.ActionEvent evt) {
        // Placeholder for search button action
    }

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Dashboard_JobSeekers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            Dashboard_JobSeekers view = new Dashboard_JobSeekers();
            int userId = 1; // Replace with actual userId from login system
            Dashboard_JobseekersController controller = new Dashboard_JobseekersController(view, userId);
            view.setVisible(true);
            controller.setUserInfo("Ram Kumar", "@ramkumar", userId);
        });
    }

    public void setUserInfo(String updatedUsername, String updatedEmail) {
        username.setText(updatedUsername);
        email.setText(updatedEmail);
    }
}