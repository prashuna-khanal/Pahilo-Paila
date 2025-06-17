package pahilopaila.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.util.Timer;
import java.util.TimerTask;
import pahilopaila.Controller.Dashboard_RecruitersController;

/**
 * Dashboard view for recruiters.
 */
public class Dashboard_Recruiters extends JFrame {

    private Dashboard_RecruitersController controller;
    private boolean dashboardPressed = false, dashboardHovered = false;
    private boolean vacancyPressed = false, vacancyHovered = false;
    private boolean applicationPressed = false, applicationHovered = false;
    private boolean settingsPressed = false, settingsHovered = false;
    private boolean myAccountPressed = false, myAccountHovered = false;
    private boolean signOutPressed = false, signOutHovered = false;

    // UI components
    public JTextField Searchfield;
    public JLabel application;
    public JPanel content;
    public JLabel dashboard;
    public JLabel email;
    private JPanel featurePanel;
    private JLabel find;
    public JButton getStarted;
    public JButton jButton1;
    public JButton jButton2;
    private JLabel jLabel1;
    private JLabel jLabel4;
    public JButton learnMore;
    private JPanel logo;
    private JPanel messagePanel;
    public JLabel myAccount;
    private JLabel profileIcon;
    private JLabel right;
    public JLabel settings;
    public JLabel signOut;
    public JLabel username;
    public JLabel vacancy;

    public Dashboard_Recruiters() {
        initComponents();
        controller = new Dashboard_RecruitersController(this);
    }

    private JLabel createStyledLabel(String text, String iconPath, Runnable onClick) {
        JLabel label = new JLabel(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (this == dashboard && dashboardPressed || this == vacancy && vacancyPressed ||
                    this == application && applicationPressed || this == settings && settingsPressed ||
                    this == myAccount && myAccountPressed || this == signOut && signOutPressed) {
                    g2d.setColor(new Color(200, 200, 200));
                } else if (this == dashboard && dashboardHovered || this == vacancy && vacancyHovered ||
                           this == application && applicationHovered || this == settings && settingsHovered ||
                           this == myAccount && myAccountHovered || this == signOut && signOutHovered) {
                    g2d.setColor(new Color(230, 230, 230));
                } else {
                    g2d.setColor(Color.WHITE);
                }
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                super.paintComponent(g);
            }
        };
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(45, 22, 116));
        try {
            label.setIcon(new ImageIcon(getClass().getResource(iconPath)));
            label.setVerticalTextPosition(JLabel.CENTER);
            label.setHorizontalTextPosition(JLabel.RIGHT);
            label.setIconTextGap(10);
        } catch (Exception e) {
            System.out.println("Error loading icon for " + text + ": " + e.getMessage());
        }
        label.setOpaque(false);
        label.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        label.addMouseListener(new java.awt.event.MouseAdapter() {
            private Timer pressTimer;

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (label == dashboard) dashboardPressed = true;
                else if (label == vacancy) vacancyPressed = true;
                else if (label == application) applicationPressed = true;
                else if (label == settings) settingsPressed = true;
                else if (label == myAccount) myAccountPressed = true;
                else if (label == signOut) signOutPressed = true;
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
                        else if (label == application) applicationPressed = false;
                        else if (label == settings) settingsPressed = false;
                        else if (label == myAccount) myAccountPressed = false;
                        else if (label == signOut) signOutPressed = false;
                        label.repaint();
                    }
                }, 200);
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (label == dashboard) dashboardHovered = true;
                else if (label == vacancy) vacancyHovered = true;
                else if (label == application) applicationHovered = true;
                else if (label == settings) settingsHovered = true;
                else if (label == myAccount) myAccountHovered = true;
                else if (label == signOut) signOutHovered = true;
                label.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (label == dashboard) dashboardHovered = false;
                else if (label == vacancy) vacancyHovered = false;
                else if (label == application) applicationHovered = false;
                else if (label == settings) settingsHovered = false;
                else if (label == myAccount) myAccountHovered = false;
                else if (label == signOut) signOutHovered = false;
                label.repaint();
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (onClick != null) {
                    onClick.run();
                }
            }
        });
        return label;
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        Searchfield = new JTextField();
        jButton1 = new JButton();
        jButton2 = new JButton();
        username = new JLabel();
        email = new JLabel();
        profileIcon = new JLabel();
        content = new JPanel();
        messagePanel = new JPanel();
        find = new JLabel();
        right = new JLabel();
        getStarted = new JButton();
        learnMore = new JButton();
        jLabel1 = new JLabel();
        featurePanel = new JPanel();
        logo = new JPanel();
        jLabel4 = new JLabel();

        dashboard = createStyledLabel("Dashboard", "/Image/logo/dashboard.jpg", () -> controller.showDashboardPanel());
        vacancy = createStyledLabel("Vacancy", "/Image/logo/vacancy.png", () -> controller.showVacancyPanel());
        application = createStyledLabel("Application", "/Image/logo/application.png", () -> controller.showApplicationsPanel());
        settings = createStyledLabel("Settings", "/Image/logo/settings.png", () -> controller.showSettingsPanel());
        myAccount = createStyledLabel("My Account", "/Image/logo/account.png", () -> controller.showMyAccountPanel());
        signOut = createStyledLabel("Sign Out", "/Image/logo/signout.png", () -> controller.logout());

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBackground(Color.WHITE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Searchfield.setHorizontalAlignment(JTextField.LEFT);
        Searchfield.addActionListener(evt -> controller.searchFieldActionPerformed(evt));
        getContentPane().add(Searchfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 30, 280, 30));

        try {
            jButton1.setIcon(new ImageIcon(getClass().getResource("/Image/logo/search.png")));
        } catch (Exception e) {
            System.out.println("Error loading search icon: " + e.getMessage());
        }
        jButton1.addActionListener(evt -> controller.searchButtonActionPerformed(evt));
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 30, 30, 30));

        try {
            jButton2.setIcon(new ImageIcon(getClass().getResource("/Image/logo/filter.png")));
        } catch (Exception e) {
            System.out.println("Error loading filter icon: " + e.getMessage());
        }
        jButton2.addActionListener(evt -> controller.filterButtonActionPerformed(evt));
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 30, 30, 30));

        username.setFont(new Font("Segoe UI Semibold", Font.BOLD, 18));
        username.setForeground(new Color(0, 0, 102));
        username.setText("Ram Kumar");
        getContentPane().add(username, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 20, 100, 30));

        email.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        email.setText("@ramkumar");
        getContentPane().add(email, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 50, 100, 20));

        try {
            profileIcon.setIcon(new ImageIcon(getClass().getResource("/Image/logo/ram.png")));
        } catch (Exception e) {
            System.out.println("Error loading profile icon: " + e.getMessage());
        }
        getContentPane().add(profileIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 10, 40, 60));

        content.setBackground(new Color(245, 245, 245));
        messagePanel.setBackground(new Color(0, 4, 80));

        find.setFont(new Font("Segoe UI Symbol", Font.BOLD, 24));
        find.setForeground(Color.WHITE);
        find.setText("Find the right people");

        right.setFont(new Font("Segoe UI Symbol", Font.BOLD, 24));
        right.setForeground(Color.WHITE);
        right.setText("for the right Job");
        right.setVerticalAlignment(SwingConstants.TOP);

        getStarted.setForeground(new Color(0, 0, 102));
        getStarted.setText("Get Started");
        getStarted.addActionListener(evt -> controller.getStartedActionPerformed(evt));
        learnMore.setBackground(new Color(0, 4, 80));
        learnMore.setForeground(Color.WHITE);
        learnMore.setText("Learn More");
        learnMore.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.WHITE), // Use a white line border
            "", // Empty title
            TitledBorder.DEFAULT_JUSTIFICATION,
            TitledBorder.DEFAULT_POSITION,
            new Font("Segoe UI", Font.PLAIN, 12),
            Color.WHITE
        ));
        learnMore.addActionListener(evt -> controller.learnMoreActionPerformed(evt));

        try {
            jLabel1.setIcon(new ImageIcon(getClass().getResource("/Image/logo/3man.png")));
        } catch (Exception e) {
            System.out.println("Error loading 3man icon: " + e.getMessage());
        }

        GroupLayout messagePanelLayout = new GroupLayout(messagePanel);
        messagePanel.setLayout(messagePanelLayout);
        messagePanelLayout.setHorizontalGroup(
            messagePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(messagePanelLayout.createSequentialGroup()
                    .addGap(39)
                    .addGroup(messagePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(find, GroupLayout.PREFERRED_SIZE, 287, GroupLayout.PREFERRED_SIZE)
                        .addComponent(right, GroupLayout.PREFERRED_SIZE, 199, GroupLayout.PREFERRED_SIZE)
                        .addGroup(messagePanelLayout.createSequentialGroup()
                            .addComponent(getStarted, GroupLayout.PREFERRED_SIZE, 98, GroupLayout.PREFERRED_SIZE)
                            .addGap(18)
                            .addComponent(learnMore, GroupLayout.PREFERRED_SIZE, 98, GroupLayout.PREFERRED_SIZE)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 165, Short.MAX_VALUE)
                    .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)
                    .addGap(22))
        );
        messagePanelLayout.setVerticalGroup(
            messagePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(messagePanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(messagePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(messagePanelLayout.createSequentialGroup()
                            .addComponent(find)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(right)
                            .addGap(18)
                            .addGroup(messagePanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(getStarted)
                                .addComponent(learnMore)))
                        .addComponent(jLabel1))
                    .addContainerGap(12, Short.MAX_VALUE))
        );

        GroupLayout contentLayout = new GroupLayout(content);
        content.setLayout(contentLayout);
        contentLayout.setHorizontalGroup(
            contentLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(contentLayout.createSequentialGroup()
                    .addGap(31)
                    .addComponent(messagePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(32, Short.MAX_VALUE))
        );
        contentLayout.setVerticalGroup(
            contentLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(contentLayout.createSequentialGroup()
                    .addGap(6)
                    .addComponent(messagePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(309, Short.MAX_VALUE))
        );

        getContentPane().add(content, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 70, 700, 440));

        featurePanel.setBackground(Color.WHITE);
        logo.setBackground(Color.WHITE);

        try {
            jLabel4.setIcon(new ImageIcon(getClass().getResource("/Image/pahilopaila_logo.png")));
        } catch (Exception e) {
            System.out.println("Error loading logo icon: " + e.getMessage());
        }

        GroupLayout logoLayout = new GroupLayout(logo);
        logo.setLayout(logoLayout);
        logoLayout.setHorizontalGroup(
            logoLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(logoLayout.createSequentialGroup()
                    .addGap(30)
                    .addComponent(jLabel4, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(15, Short.MAX_VALUE))
        );
        logoLayout.setVerticalGroup(
            logoLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(logoLayout.createSequentialGroup()
                    .addGap(10)
                    .addComponent(jLabel4)
                    .addContainerGap(10, Short.MAX_VALUE))
        );

        GroupLayout featurePanelLayout = new GroupLayout(featurePanel);
        featurePanel.setLayout(featurePanelLayout);
        featurePanelLayout.setHorizontalGroup(
            featurePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(featurePanelLayout.createSequentialGroup()
                    .addGap(15)
                    .addGroup(featurePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(dashboard)
                        .addComponent(vacancy)
                        .addComponent(application)
                        .addComponent(settings)
                        .addComponent(myAccount)
                        .addComponent(signOut))
                    .addContainerGap(15, Short.MAX_VALUE))
                .addComponent(logo, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        featurePanelLayout.setVerticalGroup(
            featurePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(featurePanelLayout.createSequentialGroup()
                    .addComponent(logo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addGap(5)
                    .addComponent(dashboard)
                    .addGap(20)
                    .addComponent(vacancy)
                    .addGap(20)
                    .addComponent(application)
                    .addGap(20)
                    .addComponent(settings)
                    .addGap(40)
                    .addComponent(myAccount)
                    .addGap(10)
                    .addComponent(signOut)
                    .addContainerGap(15, Short.MAX_VALUE))
        );

        getContentPane().add(featurePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 150, 510));

        pack();
        setLocationRelativeTo(null);
    }

    public void setUserInfo(String usernameText, String emailText) {
        username.setText(usernameText);
        email.setText(emailText);
    }

    public JPanel getContentPanel() {
        return content;
    }

    public Dashboard_RecruitersController getController() {
        return controller;
    }
}