package pahilopaila.view;

import com.toedter.calendar.JDateChooser;
import pahilopaila.model.Notification;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import pahilopaila.Controller.Dashboard_JobseekersController;

public class Dashboard_JobSeekers extends javax.swing.JFrame {
    // State variables for label hover and pressed states
    private boolean dashboardPressed = false, dashboardHovered = false;
    private boolean vacancyPressed = false, vacancyHovered = false;
    private boolean CVPressed = false, CVHovered = false;
    public boolean notificationsPressed = false, notificationsHovered = false;
    private boolean settingsPressed = false, settingsHovered = false;
    private boolean myAccountPressed = false, myAccountHovered = false;
    private boolean signOutPressed = false, signOutHovered = false;
    private boolean isDarkMode = false;
    private int unreadNotificationCount = 0; // Track unread notifications

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
    public JComboBox<String> jobTypeCombo;
    public JComboBox<String> experienceLevelCombo;
    public JDateChooser startDateChooser;
    public JDateChooser endDateChooser;
    public JButton applyFilterButton;
    public JButton clearFilterButton;
    // Notification components
    public JPanel notificationPanel;
    public JList<Notification> notificationList;
    public JButton markAllReadButton;
    // Popup menu for CV
    private JPopupMenu cvPopupMenu;
    public JMenuItem uploadCVItem;
    public JMenuItem viewCVItem;

    // Reference to controller for notification interactions
    private Dashboard_JobseekersController controller;

    public Dashboard_JobSeekers() {
        initComponents();
        setResizable(false);
        setSize(900, 600);
        setLocationRelativeTo(null);
        applyTheme();
    }

    // Method to set controller reference
    public void setController(Dashboard_JobseekersController controller) {
        this.controller = controller;
    }

    // Method to update the unread notification count
    public void updateUnreadNotificationCount(int count) {
        this.unreadNotificationCount = count;
        notifications.repaint(); // Trigger repaint to update badge
    }

    private ImageIcon createNotificationIcon() {
        // Kept as fallback, but not used for notifications label since emoji is used
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
    JLabel label = new JLabel(text) {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color defaultBg = isDarkMode ? new Color(40, 40, 40) : new Color(240, 240, 240);
            Color hoverBg = isDarkMode ? new Color(60, 60, 60) : new Color(220, 220, 220);
            Color pressedBg = isDarkMode ? new Color(50, 50, 50) : new Color(200, 200, 200);

            // Determine background color based on state
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

            // Draw badge number for notifications label (no circle)
            if (this == notifications && unreadNotificationCount > 0) {
                String countText = unreadNotificationCount >= 100 ? "99+" : String.valueOf(unreadNotificationCount);
                Font badgeFont = new Font("Segoe UI", Font.BOLD, 13); // Smaller font size
                g2d.setFont(badgeFont);
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(countText);
                int textHeight = fm.getHeight();
                int badgeX = getWidth() - textWidth - 10; // Position to the right of the bell icon
                int badgeY = -4; // Position slightly above the bell icon

                // Draw white shadow for text clarity
                g2d.setColor(new Color(255, 255, 255, 200)); // Slight shadow
                int textX = badgeX;
                int textY = badgeY + fm.getAscent();
                g2d.drawString(countText, textX + 1, textY + 1); // Shadow
                // Draw red number
                g2d.setColor(new Color(255, 0, 0)); // Bright red for number
                g2d.drawString(countText, textX, textY); // Main text
            }

            super.paintComponent(g);
        }
    };
    label.setFont(new Font("Segoe UI", Font.BOLD, 14));
    label.setForeground(isDarkMode ? new Color(220, 220, 220) : new Color(51, 51, 51));
    try {
        if (iconPath != null) {
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                label.setIcon(icon);
                label.setVerticalTextPosition(JLabel.CENTER);
                label.setHorizontalTextPosition(JLabel.RIGHT);
                label.setIconTextGap(10);
            } else {
                System.err.println("Failed to load icon: " + iconPath);
            }
        }
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
            JLabel label = (JLabel) evt.getSource();
            if (label == CV) {
                cvPopupMenu.show(label, evt.getX(), evt.getY());
            } else if (label == notifications) {
                if (controller != null) {
                    notificationsPressed = true;
                    List<Notification> notificationsList = controller.loadNotifications();
                    showNotificationPopup(notificationsList);
                    notificationsPressed = false;
                    notificationsHovered = false;
                    notifications.repaint();
                } else {
                    System.err.println("Controller is null. Cannot fetch notifications.");
                }
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
        notifications = createStyledLabel("", "/Image/logo/notification.png");
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

        // Initialize notification components
        notificationPanel = new JPanel();
        notificationList = new JList<>();
        markAllReadButton = new JButton("Mark All as Read");

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

        // Configure notification panel (for popup or other uses, not displayed on dashboard)
        notificationPanel.setBackground(isDarkMode ? new Color(40, 40, 40) : new Color(245, 245, 245));
        notificationPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(isDarkMode ? new Color(60, 60, 60) : new Color(200, 200, 200)),
                        "Notifications",
                        javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                        javax.swing.border.TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 14),
                        isDarkMode ? new Color(220, 220, 220) : new Color(0, 4, 80)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        notificationPanel.setLayout(new BorderLayout(5, 5));
        notificationPanel.setPreferredSize(new Dimension(680, 200));

        // Configure notification list
        notificationList.setBackground(isDarkMode ? new Color(30, 30, 30) : new Color(255, 255, 255));
        notificationList.setForeground(isDarkMode ? new Color(220, 220, 220) : new Color(0, 0, 0));
        notificationList.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        notificationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        notificationList.setCellRenderer(new NotificationListCellRenderer(isDarkMode));
        JScrollPane notificationScroll = new JScrollPane(notificationList);
        notificationScroll.setBorder(BorderFactory.createLineBorder(isDarkMode ? new Color(60, 60, 60) : new Color(200, 200, 200)));
        notificationPanel.add(notificationScroll, BorderLayout.CENTER);

        // Configure mark all read button
        markAllReadButton.setBackground(isDarkMode ? new Color(25, 118, 210) : new Color(0, 4, 80));
        markAllReadButton.setForeground(isDarkMode ? new Color(220, 220, 220) : new Color(255, 255, 255));
        markAllReadButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        markAllReadButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(isDarkMode ? new Color(40, 40, 40) : new Color(245, 245, 245));
        buttonPanel.add(markAllReadButton);
        notificationPanel.add(buttonPanel, BorderLayout.SOUTH);

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

        // Sidebar panel
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
                                .addComponent(settings, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                .addGap(50)
                                .addComponent(myAccount, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                .addGap(10)
                                .addComponent(signOut, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        getContentPane().add(featurePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 170, 520));

        // Add notifications label with bell emoji to top-right
        getContentPane().add(notifications, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 20, 50, 50));

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
        notificationPanel.setBackground(isDarkMode ? new Color(40, 40, 40) : new Color(245, 245, 245));
        notificationList.setBackground(isDarkMode ? new Color(30, 30, 30) : new Color(255, 255, 255));
        notificationList.setForeground(isDarkMode ? new Color(220, 220, 220) : new Color(0, 0, 0));
        notificationList.setCellRenderer(new NotificationListCellRenderer(isDarkMode));
        markAllReadButton.setBackground(isDarkMode ? new Color(25, 118, 210) : new Color(0, 4, 80));
        markAllReadButton.setForeground(isDarkMode ? new Color(220, 220, 220) : new Color(255, 255, 255));
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

    public void showNotificationPopup(List<Notification> notifications) {
        // Create a JPopupMenu for the dropdown effect
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setBorder(BorderFactory.createLineBorder(isDarkMode ? new Color(60, 60, 60) : new Color(200, 200, 200)));
        popupMenu.setBackground(isDarkMode ? new Color(45, 45, 48) : new Color(255, 255, 255));

        // Main panel inside the popup
        JPanel popupPanel = new JPanel();
        popupPanel.setLayout(new BorderLayout(5, 5));
        popupPanel.setBackground(isDarkMode ? new Color(45, 45, 48) : new Color(255, 255, 255));
        popupPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        popupPanel.setPreferredSize(new Dimension(350, Math.min(notifications.size() * 60 + 100, 400)));

        // Header with title and "Mark All as Read" button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(isDarkMode ? new Color(45, 45, 48) : new Color(255, 255, 255));
        JLabel titleLabel = new JLabel("Notifications");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(isDarkMode ? new Color(220, 220, 220) : new Color(0, 0, 102));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton markAllReadButton = new JButton("Mark All as Read");
        markAllReadButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        markAllReadButton.setForeground(isDarkMode ? new Color(100, 181, 246) : new Color(0, 123, 255));
        markAllReadButton.setBackground(isDarkMode ? new Color(45, 45, 48) : new Color(255, 255, 255));
        markAllReadButton.setBorderPainted(false);
        markAllReadButton.setFocusPainted(false);
        markAllReadButton.addActionListener(e -> {
            this.markAllReadButton.doClick(); // Trigger controller's mark all read action
            popupMenu.setVisible(false); // Close popup after marking all as read
        });
        headerPanel.add(markAllReadButton, BorderLayout.EAST);

        popupPanel.add(headerPanel, BorderLayout.NORTH);

        // Notification list
        JList<Notification> notificationList = new JList<>(notifications.toArray(new Notification[0]));
        notificationList.setCellRenderer(new NotificationCellRenderer());
        notificationList.setBackground(isDarkMode ? new Color(45, 45, 48) : new Color(255, 255, 255));
        notificationList.setSelectionBackground(isDarkMode ? new Color(60, 60, 60) : new Color(240, 240, 240));
        notificationList.setSelectionForeground(isDarkMode ? new Color(220, 220, 220) : new Color(0, 0, 0));
        notificationList.setFixedCellHeight(60);

        // Add click listener to mark notification as read
        notificationList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    Notification selected = notificationList.getSelectedValue();
                    if (selected != null && !selected.isRead()) {
                        if (controller != null) {
                            controller.markNotificationAsRead(selected.getId()); // Call controller to mark as read
                        }
                        popupMenu.setVisible(false); // Close popup after marking as read
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(notificationList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(isDarkMode ? new Color(45, 45, 48) : new Color(255, 255, 255));
        scrollPane.getViewport().setBackground(isDarkMode ? new Color(45, 45, 48) : new Color(255, 255, 255));
        popupPanel.add(scrollPane, BorderLayout.CENTER);

        // Footer with Close button
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(isDarkMode ? new Color(45, 45, 48) : new Color(255, 255, 255));
        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        closeButton.setForeground(isDarkMode ? new Color(220, 220, 220) : new Color(0, 0, 102));
        closeButton.setBackground(isDarkMode ? new Color(45, 45, 48) : new Color(255, 255, 255));
        closeButton.setBorderPainted(false);
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> popupMenu.setVisible(false));
        footerPanel.add(closeButton);
        popupPanel.add(footerPanel, BorderLayout.SOUTH);

        popupMenu.add(popupPanel);

        // Show popup below the notifications label
        Point labelLocation = this.notifications.getLocationOnScreen();
        int xOffset = 0;
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        if (labelLocation.x + 350 > screenWidth) {
            xOffset = -(labelLocation.x + 350 - screenWidth + 10); // Shift left to stay on screen
        }
        popupMenu.show(this.notifications, xOffset, this.notifications.getHeight());
    }

    // Custom renderer for notification list in popup
    private class NotificationCellRenderer extends JPanel implements ListCellRenderer<Notification> {
        private JLabel iconLabel;
        private JLabel messageLabel;
        private JLabel timeLabel;
        private JLabel unreadIndicator;

        public NotificationCellRenderer() {
            setLayout(new BorderLayout(10, 5));
            setBorder(new EmptyBorder(5, 10, 5, 10));

            iconLabel = new JLabel();
            iconLabel.setPreferredSize(new Dimension(40, 40));
            add(iconLabel, BorderLayout.WEST);

            JPanel textPanel = new JPanel(new GridLayout(2, 1));
            textPanel.setOpaque(false);
            messageLabel = new JLabel();
            messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            timeLabel = new JLabel();
            timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            textPanel.add(messageLabel);
            textPanel.add(timeLabel);
            add(textPanel, BorderLayout.CENTER);

            unreadIndicator = new JLabel("●");
            unreadIndicator.setFont(new Font("Segoe UI", Font.BOLD, 12));
            unreadIndicator.setForeground(new Color(59, 89, 152)); // Facebook blue
            add(unreadIndicator, BorderLayout.EAST);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Notification> list, Notification notification,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            // Set background and foreground
            setBackground(isSelected ? (isDarkMode ? new Color(60, 60, 60) : new Color(240, 240, 240))
                    : (isDarkMode ? new Color(45, 45, 48) : new Color(255, 255, 255)));
            messageLabel.setForeground(isDarkMode ? new Color(220, 220, 220) : new Color(0, 0, 0));
            timeLabel.setForeground(isDarkMode ? new Color(150, 150, 150) : new Color(100, 100, 100));

            // Set notification data
            messageLabel.setText(notification.getMessage());
            timeLabel.setText(notification.getTimestamp());
            unreadIndicator.setVisible(!notification.isRead());

            return this;
        }
    }

    // Existing method to update notifications
    public void updateNotifications(List<Notification> notifications) {
        DefaultListModel<Notification> model = new DefaultListModel<>();
        int unreadCount = 0;
        for (Notification notification : notifications) {
            model.addElement(notification);
            if (!notification.isRead()) {
                unreadCount++;
            }
        }
        notificationList.setModel(model);
        updateUnreadNotificationCount(unreadCount); // Update badge count
        notificationList.repaint();
    }

    // Existing custom renderer for notification list
    private class NotificationListCellRenderer extends DefaultListCellRenderer {
        private boolean isDarkMode;

        public NotificationListCellRenderer(boolean isDarkMode) {
            this.isDarkMode = isDarkMode;
        }

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Notification) {
                Notification notification = (Notification) value;
                setText(notification.getMessage() + " (" + notification.getTimestamp() + ")");
                if (notification.isRead()) {
                    setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    setForeground(isDarkMode ? new Color(180, 180, 180) : new Color(100, 100, 100));
                } else {
                    setFont(new Font("Segoe UI", Font.BOLD, 12));
                    setForeground(isDarkMode ? new Color(220, 220, 220) : new Color(0, 0, 0));
                }
                setBackground(isDarkMode ? new Color(30, 30, 30) : new Color(255, 255, 255));
                if (isSelected) {
                    setBackground(isDarkMode ? new Color(50, 50, 50) : new Color(200, 200, 200));
                }
            }
            return this;
        }
    }

    public void toggleDarkMode(boolean enableDarkMode) {
        isDarkMode = enableDarkMode;
        notificationList.setCellRenderer(new NotificationListCellRenderer(isDarkMode));
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
            view.setController(controller); // Set controller reference
            view.setVisible(true);
            controller.setUserInfo("Ram Kumar", "@ramkumar", userId);
        });
    }
}