package pahilopaila.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;
import pahilopaila.Controller.Dashboard_JobseekersController;

/**
 * Job Seeker Dashboard UI for the PahiloPaila application.
 */
public class Dashboard_JobSeekers extends JFrame {
    // State variables for label hover and pressed states
    private boolean dashboardPressed = false, dashboardHovered = false;
    private boolean vacancyPressed = false, vacancyHovered = false;
    private boolean CVPressed = false, CVHovered = false;
    private boolean settingsPressed = false, settingsHovered = false;
    private boolean myAccountPressed = false, myAccountHovered = false;
    private boolean signOutPressed = false, signOutHovered = false;

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
    public JLabel featured;
    public JButton see_all;
    private JPopupMenu cvPopupMenu;
    public JMenuItem uploadCVItem;
    public JMenuItem viewCVItem;

    public Dashboard_JobSeekers() {
        initComponents();
        setResizable(false);
        setSize(900, 700);
        setLocationRelativeTo(null);
    }

    private JLabel createStyledLabel(String text, String iconPath) {
        JLabel label = new JLabel(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (this == dashboard && dashboardPressed || this == vacancy && vacancyPressed ||
                    this == CV && CVPressed || this == settings && settingsPressed ||
                    this == myAccount && myAccountPressed || this == signOut && signOutPressed) {
                    g2d.setColor(new Color(200, 200, 200));
                } else if (this == dashboard && dashboardHovered || this == vacancy && vacancyHovered ||
                           this == CV && CVHovered || this == settings && settingsHovered ||
                           this == myAccount && myAccountHovered || this == signOut && signOutHovered) {
                    g2d.setColor(new Color(230, 230, 230));
                } else {
                    g2d.setColor(new Color(255, 255, 255));
                }
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                super.paintComponent(g);
            }
        };
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(102, 102, 102));
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
        label.addMouseListener(new MouseAdapter() {
            private Timer pressTimer;

            @Override
            public void mousePressed(MouseEvent evt) {
                if (label == dashboard) dashboardPressed = true;
                else if (label == vacancy) vacancyPressed = true;
                else if (label == CV) CVPressed = true;
                else if (label == settings) settingsPressed = true;
                else if (label == myAccount) myAccountPressed = true;
                else if (label == signOut) signOutPressed = true;
                label.repaint();
                if (pressTimer != null) pressTimer.cancel();
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
            public void mouseEntered(MouseEvent evt) {
                if (label == dashboard) dashboardHovered = true;
                else if (label == vacancy) vacancyHovered = true;
                else if (label == CV) CVHovered = true;
                else if (label == settings) settingsHovered = true;
                else if (label == myAccount) myAccountHovered = true;
                else if (label == signOut) signOutHovered = true;
                label.repaint();
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                if (label == dashboard) dashboardHovered = false;
                else if (label == vacancy) vacancyHovered = false;
                else if (label == CV) CVHovered = false;
                else if (label == settings) settingsHovered = false;
                else if (label == myAccount) myAccountHovered = false;
                else if (label == signOut) signOutHovered = false;
                label.repaint();
            }

            @Override
            public void mouseClicked(MouseEvent evt) {
                if (label == CV) {
                    cvPopupMenu.show(label, evt.getX(), evt.getY());
                }
            }
        });
        return label;
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        Searchfield = new JTextField();
        filter = new JButton();
        search = new JButton();
        username = new JLabel();
        email = new JLabel();
        featurePanel = new JPanel();
        logo = new JPanel();
        jLabel4 = new JLabel();
        dashboard = createStyledLabel("Dashboard", "/Image/logo/dashboard.jpg");
        vacancy = createStyledLabel("Vacancy", "/Image/logo/vacancy.png");
        CV = createStyledLabel("CV", "/Image/logo/application.png");
        settings = createStyledLabel("Settings", "/Image/logo/setting.png");
        myAccount = createStyledLabel("My Account", "/Image/logo/account.png");
        signOut = createStyledLabel("Sign Out", "/Image/logo/signout.png");
        profileIcon1 = new JLabel();
        content = new JPanel();
        messagePanel = new JPanel();
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        learnMore = new JButton();
        getStarted = new JButton();
        featured = new JLabel();
        see_all = new JButton();

        cvPopupMenu = new JPopupMenu();
        uploadCVItem = new JMenuItem("Upload CV");
        viewCVItem = new JMenuItem("View CV");
        cvPopupMenu.add(uploadCVItem);
        cvPopupMenu.add(viewCVItem);
        cvPopupMenu.setBackground(new Color(255, 255, 255));
        cvPopupMenu.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        uploadCVItem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        viewCVItem.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBackground(new Color(255, 255, 255));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Searchfield.setHorizontalAlignment(JTextField.LEFT);
        getContentPane().add(Searchfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 30, 280, 30));

        filter.setIcon(new ImageIcon(getClass().getResource("/Image/logo/filter.png")));
        getContentPane().add(filter, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 30, 30, 30));

        search.setIcon(new ImageIcon(getClass().getResource("/Image/logo/search.jpg")));
        getContentPane().add(search, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 30, 30, 30));

        username.setFont(new Font("Segoe UI Semibold", Font.BOLD, 18));
        username.setForeground(new Color(0, 0, 102));
        username.setText("Ram Kumar");
        getContentPane().add(username, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 20, -1, 30));

        email.setText("@ramkumar");
        getContentPane().add(email, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 50, -1, -1));

        featurePanel.setBackground(new Color(255, 255, 255));
        logo.setBackground(new Color(255, 255, 255));
        jLabel4.setIcon(new ImageIcon(getClass().getResource("/Image/pahilopaila_logo.png")));
        GroupLayout logoLayout = new GroupLayout(logo);
        logo.setLayout(logoLayout);
        logoLayout.setHorizontalGroup(
            logoLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(logoLayout.createSequentialGroup()
                    .addGap(30)
                    .addComponent(jLabel4, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        logoLayout.setVerticalGroup(
            logoLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(GroupLayout.Alignment.TRAILING, logoLayout.createSequentialGroup()
                    .addContainerGap(18, Short.MAX_VALUE)
                    .addComponent(jLabel4)
                    .addGap(14))
        );

        GroupLayout featurePanelLayout = new GroupLayout(featurePanel);
        featurePanel.setLayout(featurePanelLayout);
        featurePanelLayout.setHorizontalGroup(
            featurePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(logo, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(featurePanelLayout.createSequentialGroup()
                    .addGap(25)
                    .addGroup(featurePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                        .addComponent(dashboard, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(vacancy, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(CV, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(settings, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(myAccount, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(signOut, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap(30, Short.MAX_VALUE))
        );
        featurePanelLayout.setVerticalGroup(
            featurePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(featurePanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(logo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addGap(29)
                    .addComponent(dashboard, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(vacancy, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(CV, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(settings, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
                    .addGap(85)
                    .addComponent(myAccount, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(signOut, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(105, Short.MAX_VALUE))
        );

        getContentPane().add(featurePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 170, 520));

        profileIcon1.setIcon(new ImageIcon(getClass().getResource("/Image/logo/ram.png")));
        getContentPane().add(profileIcon1, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 10, 40, 60));

        content.setBackground(new Color(245, 245, 245));
        messagePanel.setBackground(new Color(0, 4, 80));
        jLabel1.setFont(new Font("Segoe UI Symbol", Font.BOLD, 24));
        jLabel1.setForeground(Color.WHITE);
        jLabel1.setText("Discover Opportunities That ");
        jLabel2.setFont(new Font("Segoe UI Symbol", Font.BOLD, 24));
        jLabel2.setForeground(Color.WHITE);
        jLabel2.setText("Match Your Skill");
        jLabel2.setVerticalAlignment(SwingConstants.TOP);
        learnMore.setBackground(new Color(0, 4, 80));
        learnMore.setForeground(Color.WHITE);
        learnMore.setText("Learn More");
        learnMore.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        getStarted.setForeground(new Color(0, 0, 102));
        getStarted.setText("Get Started");

        GroupLayout messagePanelLayout = new GroupLayout(messagePanel);
        messagePanel.setLayout(messagePanelLayout);
        messagePanelLayout.setHorizontalGroup(
            messagePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(messagePanelLayout.createSequentialGroup()
                    .addGap(22)
                    .addGroup(messagePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel2)
                        .addComponent(jLabel1)
                        .addGroup(messagePanelLayout.createSequentialGroup()
                            .addComponent(getStarted)
                            .addGap(26)
                            .addComponent(learnMore, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap(276, Short.MAX_VALUE))
        );
        messagePanelLayout.setVerticalGroup(
            messagePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(messagePanelLayout.createSequentialGroup()
                    .addGap(5)
                    .addComponent(jLabel1)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel2)
                    .addGap(18)
                    .addGroup(messagePanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(getStarted)
                        .addComponent(learnMore, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(20, Short.MAX_VALUE))
        );

        GroupLayout contentLayout = new GroupLayout(content);
        content.setLayout(contentLayout);
        contentLayout.setHorizontalGroup(
            contentLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(contentLayout.createSequentialGroup()
                    .addGap(22)
                    .addComponent(messagePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(36, Short.MAX_VALUE))
        );
        contentLayout.setVerticalGroup(
            contentLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(contentLayout.createSequentialGroup()
                    .addGap(22)
                    .addComponent(messagePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(286, Short.MAX_VALUE))
        );

        getContentPane().add(content, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 80, 680, 430));

        pack();
    }

    public void setUserInfo(String updatedUsername, String updatedEmail) {
        username.setText(updatedUsername);
        email.setText(updatedEmail);
    }

    public JPanel getContentPanel() {
        return content;
    }

    public static void main(String args[]) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(Dashboard_JobSeekers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        EventQueue.invokeLater(() -> {
            Dashboard_JobSeekers view = new Dashboard_JobSeekers();
            int userId = 1;
            new Dashboard_JobseekersController(view, userId);
        });
    }
}