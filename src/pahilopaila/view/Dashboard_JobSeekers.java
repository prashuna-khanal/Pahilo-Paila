package pahilopaila.view;

import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;
import pahilopaila.Controller.Dashboard_JobseekersController;

public class Dashboard_JobSeekers extends javax.swing.JFrame {
    // State variables for label hover and pressed states
    private boolean dashboardPressed = false, dashboardHovered = false;
    private boolean vacancyPressed = false, vacancyHovered = false;
    private boolean CVPressed = false, CVHovered = false;
    private boolean notificationsPressed = false, notificationsHovered = false;
    private boolean settingsPressed = false, settingsHovered = false;
    private boolean myAccountPressed = false, myAccountHovered = false;
    private boolean signOutPressed = false, signOutHovered = false;
    private boolean isDarkMode = false;

    // UI components
    public JLabel notifications;
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
    // Filter components
    public JComboBox<String> jobTypeCombo; // Properly declared
    public JComboBox<String> experienceLevelCombo;
    public JDateChooser startDateChooser;
    public JDateChooser endDateChooser;
    public JButton applyFilterButton;
    public JButton clearFilterButton;
    // Popup menu for CV
    private JPopupMenu cvPopupMenu;
    public JMenuItem uploadCVItem;
    public JMenuItem viewCVItem;

    public Dashboard_JobSeekers() {
        initComponents();
        setResizable(false);
        setSize(900, 600);
        setLocationRelativeTo(null);
        applyTheme();
    }

    private ImageIcon createNotificationIcon() {
        int width = 16;
        int height = 16;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(isDarkMode ? new Color(220, 220, 220) : new Color(102, 102, 102));
        g2d.fillOval(5, 10, 6, 4);
        g2d.fillArc(3, 2, 10, 10, 0, 180);
        int[] xPoints = {3, 13, 10, 6};
        int[] yPoints = {7, 7, 12, 12};
        g2d.fillPolygon(xPoints, yPoints, 4);
        g2d.dispose();
        return new ImageIcon(image);
    }

    private JLabel createStyledLabel(String text, String iconPath) {
        // Unchanged, as provided
        JLabel label = new JLabel(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color defaultBg = isDarkMode ? new Color(40, 40, 40) : new Color(240, 240, 240);
                Color hoverBg = isDarkMode ? new Color(60, 60, 60) : new Color(220, 220, 220);
                Color pressedBg = isDarkMode ? new Color(50, 50, 50) : new Color(200, 200, 200);

                if (this == dashboard && dashboardPressed || this == vacancy && vacancyPressed ||
                        this == CV && CVPressed || this == notifications && notificationsPressed ||
                        this == settings && settingsPressed || this == myAccount && myAccountPressed ||
                        this == signOut && signOutPressed) {
                    g2d.setColor(pressedBg);
                } else if (this == dashboard && dashboardHovered || this == vacancy && vacancyHovered ||
                        this == CV && CVHovered || this == notifications && notificationsHovered ||
                        this == settings && settingsHovered || this == myAccount && myAccountHovered ||
                        this == signOut && signOutHovered) {
                    g2d.setColor(hoverBg);
                } else {
                    g2d.setColor(defaultBg);
                }
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                super.paintComponent(g);
            }
        };
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(isDarkMode ? new Color(220, 220, 220) : new Color(51, 51, 51));
        try {
            if (text.equals("Notifications")) {
                label.setIcon(createNotificationIcon());
            } else {
                label.setIcon(new ImageIcon(getClass().getResource(iconPath)));
            }
            label.setVerticalTextPosition(JLabel.CENTER);
            label.setHorizontalTextPosition(JLabel.RIGHT);
            label.setIconTextGap(10);
        } catch (Exception e) {
            System.err.println("Error loading icon for " + text + ": " + e.getMessage());
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
                else if (label == notifications) notificationsPressed = true;
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
                        else if (label == notifications) notificationsPressed = false;
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
                else if (label == notifications) notificationsHovered = true;
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
                else if (label == notifications) notificationsHovered = false;
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
        // Initialize components
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
        notifications = createStyledLabel("Notifications", null);
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

        // Initialize filter components
        jobTypeCombo = new JComboBox<>();
        experienceLevelCombo = new JComboBox<>();
        startDateChooser = new JDateChooser();
        endDateChooser = new JDateChooser();
        applyFilterButton = new JButton("Apply");
        clearFilterButton = new JButton("Clear");

        // Set models for filter combos to align with VacancyDao
        jobTypeCombo.setModel(new DefaultComboBoxModel<>(new String[]{"All", "Full time", "Part time", "Internship"}));
        experienceLevelCombo.setModel(new DefaultComboBoxModel<>(new String[]{"All", "Junior-Level", "Mid-Level", "Senior-Level"}));

        // Initialize CV popup menu
        cvPopupMenu = new JPopupMenu();
        uploadCVItem = new JMenuItem("Upload CV");
        viewCVItem = new JMenuItem("View CV");
        cvPopupMenu.add(uploadCVItem);
        cvPopupMenu.add(viewCVItem);
        updatePopupTheme();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Searchfield.setBackground(isDarkMode ? new Color(40, 40, 40) : new Color(255, 255, 255));
        Searchfield.setForeground(isDarkMode ? new Color(220, 220, 220) : new Color(0, 0, 0));
        Searchfield.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(isDarkMode ? new Color(60, 60, 60) : new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        Searchfield.setHorizontalAlignment(JTextField.LEFT);
        getContentPane().add(Searchfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 30, 280, 30));

        filter.setIcon(new ImageIcon(getClass().getResource("/Image/logo/filter.png")));
        filter.setBackground(isDarkMode ? new Color(25, 118, 210) : new Color(240, 240, 240));
        filter.setBorder(BorderFactory.createEmptyBorder());
        getContentPane().add(filter, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 30, 30, 30));

        search.setIcon(new ImageIcon(getClass().getResource("/Image/logo/search.jpg")));
        search.setBackground(isDarkMode ? new Color(25, 118, 210) : new Color(240, 240, 240));
        search.setBorder(BorderFactory.createEmptyBorder());
        getContentPane().add(search, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 30, 30, 30));

        username.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 18));
        username.setForeground(isDarkMode ? new Color(245, 245, 245) : new Color(0, 0, 102));
        username.setText("Ram Kumar");
        getContentPane().add(username, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 20, -1, 30));

        email.setForeground(isDarkMode ? new Color(245, 245, 245) : new Color(0, 0, 0));
        email.setText("@ramkumar");
        getContentPane().add(email, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 50, -1, -1));

        featurePanel.setBackground(isDarkMode ? new Color(40, 40, 40) : new Color(255, 255, 255));
        logo.setBackground(isDarkMode ? new Color(40, 40, 40) : new Color(255, 255, 255));
        try {
            jLabel4.setIcon(new ImageIcon(getClass().getResource("/Image/pahilopaila_logo.png")));
        } catch (Exception e) {
            System.err.println("Error loading logo: " + e.getMessage());
        }
        logo.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        logo.add(jLabel4, gbc);

        GroupLayout featurePanelLayout = new GroupLayout(featurePanel);
        featurePanel.setLayout(featurePanelLayout);
        featurePanelLayout.setHorizontalGroup(
                featurePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(logo, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(featurePanelLayout.createSequentialGroup()
                                .addGap(15)
                                .addGroup(featurePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(dashboard, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(vacancy, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(CV, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(notifications, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(settings, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(myAccount, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(signOut, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(15, Short.MAX_VALUE)));
        featurePanelLayout.setVerticalGroup(
                featurePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(featurePanelLayout.createSequentialGroup()
                                .addComponent(logo, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                                .addGap(20)
                                .addComponent(dashboard, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                .addGap(10)
                                .addComponent(vacancy, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                .addGap(10)
                                .addComponent(CV, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                .addGap(10)
                                .addComponent(notifications, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                .addGap(10)
                                .addComponent(settings, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                .addGap(50)
                                .addComponent(myAccount, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                .addGap(10)
                                .addComponent(signOut, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        getContentPane().add(featurePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 170, 520));

        profileIcon1.setIcon(new ImageIcon(getClass().getResource("/Image/logo/ram.png")));
        getContentPane().add(profileIcon1, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 10, 40, 60));

        content.setBackground(isDarkMode ? new Color(35, 35, 35) : new Color(245, 245, 245));
        getContentPane().add(content, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 80, 680, 430));

        pack();
    }

    private void applyTheme() {
        getContentPane().setBackground(isDarkMode ? new Color(35, 35, 35) : new Color(255, 255, 255));
        featurePanel.setBackground(isDarkMode ? new Color(40, 40, 40) : new Color(255, 255, 255));
        logo.setBackground(isDarkMode ? new Color(40, 40, 40) : new Color(255, 255, 255));
        content.setBackground(isDarkMode ? new Color(35, 35, 35) : new Color(245, 245, 245));
        Searchfield.setBackground(isDarkMode ? new Color(40, 40, 40) : new Color(255, 255, 255));
        Searchfield.setForeground(isDarkMode ? new Color(220, 220, 220) : new Color(0, 0, 0));
        Searchfield.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(isDarkMode ? new Color(60, 60, 60) : new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        filter.setBackground(isDarkMode ? new Color(25, 118, 210) : new Color(240, 240, 240));
        search.setBackground(isDarkMode ? new Color(25, 118, 210) : new Color(240, 240, 240));
        username.setForeground(isDarkMode ? new Color(245, 245, 245) : new Color(0, 0, 102));
        email.setForeground(isDarkMode ? new Color(245, 245, 245) : new Color(0, 0, 0));
        notifications.setIcon(createNotificationIcon());
        updatePopupTheme();
        // Apply theme to filter components
        jobTypeCombo.setBackground(isDarkMode ? new Color(40, 40, 40) : new Color(255, 255, 255));
        jobTypeCombo.setForeground(isDarkMode ? new Color(220, 220, 220) : new Color(0, 0, 0));
        experienceLevelCombo.setBackground(isDarkMode ? new Color(40, 40, 40) : new Color(255, 255, 255));
        experienceLevelCombo.setForeground(isDarkMode ? new Color(220, 220, 220) : new Color(0, 0, 0));
        applyFilterButton.setBackground(isDarkMode ? new Color(25, 118, 210) : new Color(0, 4, 80));
        applyFilterButton.setForeground(isDarkMode ? new Color(220, 220, 220) : new Color(255, 255, 255));
        clearFilterButton.setBackground(isDarkMode ? new Color(100, 100, 100) : new Color(200, 200, 200));
        clearFilterButton.setForeground(isDarkMode ? new Color(220, 220, 220) : new Color(0, 0, 0));
        dashboard.repaint();
        vacancy.repaint();
        CV.repaint();
        notifications.repaint();
        settings.repaint();
        myAccount.repaint();
        signOut.repaint();
    }

    private void updatePopupTheme() {
        cvPopupMenu.setBackground(isDarkMode ? new Color(45, 45, 48) : new Color(255, 255, 255));
        cvPopupMenu.setBorder(BorderFactory.createLineBorder(isDarkMode ? new Color(60, 60, 60) : new Color(200, 200, 200)));
        uploadCVItem.setForeground(isDarkMode ? new Color(220, 220, 220) : new Color(0, 0, 0));
        viewCVItem.setForeground(isDarkMode ? new Color(220, 220, 220) : new Color(0, 0, 0));
    }

    public void toggleDarkMode(boolean enableDarkMode) {
        isDarkMode = enableDarkMode;
        applyTheme();
    }

    public String getJobTypeFilter() {
        return jobTypeCombo.getSelectedItem().equals("All") ? null : (String) jobTypeCombo.getSelectedItem();
    }

    public String getExperienceLevelFilter() {
        return experienceLevelCombo.getSelectedItem().equals("All") ? null : (String) experienceLevelCombo.getSelectedItem();
    }

    public java.util.Date getStartDateFilter() {
        return startDateChooser.getDate();
    }

    public java.util.Date getEndDateFilter() {
        return endDateChooser.getDate();
    }

    public JButton getApplyFilterButton() {
        return applyFilterButton;
    }

    public JButton getClearFilterButton() {
        return clearFilterButton;
    }

    public void setUserInfo(String updatedUsername, String updatedEmail) {
        username.setText(updatedUsername);
        email.setText(updatedEmail);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("Nimbus");
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(Dashboard_JobSeekers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        EventQueue.invokeLater(() -> {
            Dashboard_JobSeekers view = new Dashboard_JobSeekers();
            int userId = 1;
            Dashboard_JobseekersController controller = new Dashboard_JobseekersController(view, userId);
            view.setVisible(true);
            controller.setUserInfo("Ram Kumar", "@ramkumar", userId);
        });
    }
}