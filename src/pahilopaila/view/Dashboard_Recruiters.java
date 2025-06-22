package pahilopaila.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import pahilopaila.Controller.Dashboard_RecruitersController;
import pahilopaila.model.Notification;

public class Dashboard_Recruiters extends JFrame {
    // State variables for label hover and pressed states
    private boolean dashboardPressed = false, dashboardHovered = false;
    private boolean vacancyPressed = false, vacancyHovered = false;
    private boolean applicationPressed = false, applicationHovered = false;
    private boolean notificationsPressed = false, notificationsHovered = false;
    private boolean settingsPressed = false, settingsHovered = false;
    private boolean myAccountPressed = false, myAccountHovered = false;
    private boolean signOutPressed = false, signOutHovered = false;
    private boolean isDarkMode = false;
    private int unreadNotificationCount = 0; // Track unread notifications for badge

    // UI components
    public JTextField Searchfield;
    public JButton search;
    public JButton filter;
    public JLabel username;
    public JLabel email;
    public JPanel featurePanel;
    public JPanel logo;
    public JLabel jLabel4;
    public JLabel dashboard;
    public JLabel vacancy;
    public JLabel application;
    public JLabel notifications;
    public JLabel settings;
    public JLabel myAccount;
    public JLabel signOut;
    public JLabel profileIcon;
    public JPanel content;
    public JPanel messagePanel;
    public JLabel jLabel1;
    public JLabel find;
    public JLabel right;
    public JButton learnMore;
    public JButton getStarted;
    public JPanel notificationPanel;
    public JList<Notification> notificationList;
    public JButton markAllReadButton;
    public Object jButton1;
    public Object jButton2;

    // Reference to controller for notification interactions
    private Dashboard_RecruitersController controller;

    public Dashboard_Recruiters() {
        initComponents();
        setResizable(false);
        setSize(890, 550);
        setLocationRelativeTo(null);
        applyTheme();
    }

    public void setController(Dashboard_RecruitersController controller) {
        this.controller = controller;
    }

    public void setUnreadNotificationCount(int count) {
        this.unreadNotificationCount = count;
        notifications.repaint();
    }

 public void showNotificationPopup(List<Notification> notifications) {
    JPopupMenu popupMenu = new JPopupMenu();
    popupMenu.setBorder(BorderFactory.createLineBorder(isDarkMode ? new Color(60, 60, 60) : new Color(200, 200, 200), 1));
    popupMenu.setBackground(isDarkMode ? new Color(45, 45, 48) : Color.WHITE);

    JPanel popupPanel = new JPanel();
    popupPanel.setLayout(new BorderLayout(0, 5));
    popupPanel.setBackground(isDarkMode ? new Color(45, 45, 48) : Color.WHITE);
    popupPanel.setBorder(new EmptyBorder(5, 0, 5, 0));
    // Dynamically adjust popup height based on content, up to 400px
    int estimatedHeight = Math.min(notifications.size() * 80 + 100, 400); // Increased estimate for wrapped text
    popupPanel.setPreferredSize(new Dimension(350, estimatedHeight));

    JPanel headerPanel = new JPanel(new BorderLayout(0, 0));
    headerPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
    headerPanel.setBackground(isDarkMode ? new Color(45, 45, 48) : Color.WHITE);
    JLabel titleLabel = new JLabel("Notifications");
    titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
    titleLabel.setForeground(isDarkMode ? new Color(220, 220, 220) : new Color(0, 0, 102));
    headerPanel.add(titleLabel, BorderLayout.WEST);

    JButton markAllReadButton = new JButton("Mark All as Read");
    markAllReadButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    markAllReadButton.setForeground(isDarkMode ? new Color(100, 181, 246) : new Color(0, 123, 255));
    markAllReadButton.setBackground(isDarkMode ? new Color(45, 45, 48) : Color.WHITE);
    markAllReadButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    markAllReadButton.setFocusPainted(false);
    markAllReadButton.addActionListener(e -> {
        if (controller != null) {
            controller.markAllNotificationsAsRead();
            popupMenu.setVisible(false);
        }
    });
    headerPanel.add(markAllReadButton, BorderLayout.EAST);

    popupPanel.add(headerPanel, BorderLayout.NORTH);

    JList<Notification> notificationList = new JList<>(notifications.toArray(new Notification[0]));
    notificationList.setCellRenderer(new NotificationCellRenderer());
    notificationList.setBackground(isDarkMode ? new Color(45, 45, 48) : Color.WHITE);
    notificationList.setSelectionBackground(isDarkMode ? new Color(60, 60, 60) : new Color(240, 240, 240));
    notificationList.setSelectionForeground(isDarkMode ? new Color(220, 220, 220) : new Color(0, 0, 0));
    // Removed: notificationList.setFixedCellHeight(60); // Allow dynamic heights

    notificationList.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 1) {
                Notification selected = notificationList.getSelectedValue();
                if (selected != null && !selected.isRead() && controller != null) {
                    controller.markNotificationAsRead(selected.getId());
                    popupMenu.setVisible(false);
                }
            }
        }
    });

    JScrollPane scrollPane = new JScrollPane(notificationList);
    scrollPane.setBorder(BorderFactory.createEmptyBorder());
    scrollPane.setBackground(isDarkMode ? new Color(45, 45, 48) : Color.WHITE);
    scrollPane.getViewport().setBackground(isDarkMode ? new Color(45, 45, 48) : Color.WHITE);
    popupPanel.add(scrollPane, BorderLayout.CENTER);

    JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
    footerPanel.setBackground(isDarkMode ? new Color(45, 45, 48) : Color.WHITE);
    JButton closeButton = new JButton("Close");
    closeButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    closeButton.setForeground(isDarkMode ? new Color(220, 220, 220) : new Color(0, 0, 102));
    closeButton.setBackground(isDarkMode ? new Color(45, 45, 48) : Color.WHITE);
    closeButton.setBorderPainted(false);
    closeButton.setFocusPainted(false);
    closeButton.addActionListener(e -> popupMenu.setVisible(false));
    footerPanel.add(closeButton);
    popupPanel.add(footerPanel, BorderLayout.SOUTH);

    popupMenu.add(popupPanel);

    Point labelLocation = this.notifications.getLocationOnScreen();
    int xOffset = 0;
    int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
    if (labelLocation.x + 350 > screenWidth) {
        xOffset = -(labelLocation.x + 350 - screenWidth + 10);
    }
    popupMenu.show(this.notifications, xOffset, this.notifications.getHeight());
}

   private class NotificationCellRenderer extends JPanel implements ListCellRenderer<Notification> {
    private JTextArea messageLabel;
    private JLabel timeLabel;
    private JLabel unreadIndicator;

    public NotificationCellRenderer() {
        setLayout(new BorderLayout(0, 5));
        setBorder(new EmptyBorder(5, 0, 5, 0));

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setOpaque(false);
        textPanel.setBorder(new EmptyBorder(0, 15, 0, 0));

        messageLabel = new JTextArea();
        messageLabel.setWrapStyleWord(true);
        messageLabel.setLineWrap(true);
        messageLabel.setOpaque(false);
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setEditable(false);
        messageLabel.setFocusable(false);

        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        textPanel.add(messageLabel, BorderLayout.CENTER);
        textPanel.add(timeLabel, BorderLayout.SOUTH);
        add(textPanel, BorderLayout.CENTER);

        unreadIndicator = new JLabel("●");
        unreadIndicator.setFont(new Font("Segoe UI", Font.BOLD, 12));
        unreadIndicator.setForeground(new Color(59, 89, 152));
        add(unreadIndicator, BorderLayout.EAST);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Notification> list, Notification notification,
                                                 int index, boolean isSelected, boolean cellHasFocus) {
        setBackground(isSelected ? (isDarkMode ? new Color(60, 60, 60) : new Color(240, 240, 240))
                : (isDarkMode ? new Color(45, 45, 48) : new Color(255, 255, 255)));
        messageLabel.setForeground(isDarkMode ? new Color(220, 220, 220) : new Color(0, 0, 0));
        timeLabel.setForeground(isDarkMode ? new Color(150, 150, 150) : new Color(100, 100, 100));

        messageLabel.setText(notification.getMessage());
        timeLabel.setText(notification.getTimestamp());
        unreadIndicator.setVisible(!notification.isRead());

        // Calculate dynamic height for wrapped text
        int width = list.getWidth() - 40; // Account for padding and unread indicator
        if (width > 0) {
            // Set messageLabel width to calculate wrapped height
            messageLabel.setSize(width, Short.MAX_VALUE);
            // Force layout to compute preferred height
            messageLabel.setPreferredSize(null); // Reset to allow natural height
            Dimension messagePrefSize = messageLabel.getPreferredSize();
            // Calculate total cell height: message + timeLabel + padding
            int totalHeight = messagePrefSize.height + timeLabel.getPreferredSize().height + 15; // Extra padding
            setPreferredSize(new Dimension(list.getWidth(), totalHeight));
        }

        // Add separator except for the last item
        if (index < list.getModel().getSize() - 1) {
            setBorder(BorderFactory.createCompoundBorder(
                    new EmptyBorder(5, 0, 5, 0),
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240, 240, 240))));
        } else {
            setBorder(new EmptyBorder(5, 0, 5, 0));
        }

        return this;
    }
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

                if (this == dashboard && dashboardPressed || this == vacancy && vacancyPressed ||
                        this == application && applicationPressed || this == notifications && notificationsPressed ||
                        this == settings && settingsPressed || this == myAccount && myAccountPressed ||
                        this == signOut && signOutPressed) {
                    g2d.setColor(pressedBg);
                } else if (this == dashboard && dashboardHovered || this == vacancy && vacancyHovered ||
                        this == application && applicationHovered || this == notifications && notificationsHovered ||
                        this == settings && settingsHovered || this == myAccount && myAccountHovered ||
                        this == signOut && signOutHovered) {
                    g2d.setColor(hoverBg);
                } else {
                    g2d.setColor(defaultBg);
                }
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);

                if (this == notifications && unreadNotificationCount > 0) {
                    String countText = unreadNotificationCount >= 100 ? "99+" : String.valueOf(unreadNotificationCount);
                    Font badgeFont = new Font("Segoe UI", Font.BOLD, 13); // Match JobSeekers
                    g2d.setFont(badgeFont);
                    FontMetrics fm = g2d.getFontMetrics();
                    int textWidth = fm.stringWidth(countText);
                    int textHeight = fm.getHeight();
                    int padding = 4;
                    int badgeSize = Math.max(textWidth + padding, textHeight + padding);
                    int badgeX = getWidth() - badgeSize - 10; // Match JobSeekers
                    int badgeY = -4; // Match JobSeekers

                    g2d.setColor(new Color(200, 0, 0));
                    g2d.fillOval(badgeX + 1, badgeY + 1, badgeSize, badgeSize);
                    g2d.setColor(new Color(255, 0, 0));
                    g2d.fillOval(badgeX, badgeY, badgeSize, badgeSize);

                    g2d.setColor(new Color(255, 255, 255, 200));
                    int textX = badgeX + (badgeSize - textWidth) / 2;
                    int textY = badgeY + (badgeSize - textHeight) / 2 + fm.getAscent();
                    g2d.drawString(countText, textX + 1, textY + 1);
                    g2d.setColor(Color.WHITE);
                    g2d.drawString(countText, textX, textY);
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
                    label.setText("!");
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
                else if (label == application) applicationPressed = true;
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
                        else if (label == application) applicationPressed = false;
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
                else if (label == application) applicationHovered = true;
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
                else if (label == application) applicationHovered = false;
                else if (label == notifications) notificationsHovered = false;
                else if (label == settings) settingsHovered = false;
                else if (label == myAccount) myAccountHovered = false;
                else if (label == signOut) signOutHovered = false;
                label.repaint();
            }

            @Override
            public void mouseClicked(MouseEvent evt) {
                if (label == notifications && controller != null) {
                    List<Notification> notificationsList = controller.loadNotifications();
                    showNotificationPopup(notificationsList);
                }
            }
        });
        return label;
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

        search.setBackground(isDarkMode ? new Color(25, 118, 210) : new Color(240, 240, 240));
        filter.setBackground(isDarkMode ? new Color(25, 118, 210) : new Color(240, 240, 240));

        username.setForeground(isDarkMode ? new Color(245, 245, 245) : new Color(0, 0, 102));
        email.setForeground(isDarkMode ? new Color(245, 245, 245) : new Color(0, 0, 0));

        notificationPanel.setBackground(isDarkMode ? new Color(40, 40, 40) : new Color(245, 245, 245));
        notificationList.setBackground(isDarkMode ? new Color(30, 30, 30) : new Color(255, 255, 255));
        notificationList.setForeground(isDarkMode ? new Color(220, 220, 220) : new Color(0, 0, 0));
        notificationList.setCellRenderer(new NotificationCellRenderer());
        markAllReadButton.setBackground(isDarkMode ? new Color(25, 118, 210) : new Color(0, 4, 80));
        markAllReadButton.setForeground(isDarkMode ? new Color(220, 220, 220) : new Color(255, 255, 255));

        dashboard.repaint();
        vacancy.repaint();
        application.repaint();
        notifications.repaint();
        settings.repaint();
        myAccount.repaint();
        signOut.repaint();
    }

    public void toggleDarkMode(boolean enableDarkMode) {
        isDarkMode = enableDarkMode;
        notificationList.setCellRenderer(new NotificationCellRenderer());
        applyTheme();
    }

    public void updateNotifications(List<Notification> notifications) {
        DefaultListModel<Notification> model = new DefaultListModel<>();
        if (notifications.isEmpty()) {
            Notification placeholder = new Notification(0, 0, "No notifications available", "", false, true);
            model.addElement(placeholder);
        } else {
            for (Notification notification : notifications) {
                model.addElement(notification);
            }
        }
        notificationList.setModel(model);
        notificationList.repaint();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        Searchfield = new JTextField();
        search = new JButton();
        filter = new JButton();
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
        dashboard = createStyledLabel("Dashboard", "/Image/logo/dashboard.jpg");
        vacancy = createStyledLabel("Vacancy", "/Image/logo/vacancy.png");
        application = createStyledLabel("Application", "/Image/logo/application.png");
        notifications = createStyledLabel("", "/Image/logo/notification.png"); // Use createStyledLabel for notifications
        settings = createStyledLabel("Settings", "/Image/logo/setting.png");
        myAccount = createStyledLabel("My Account", "/Image/logo/account.png");
        signOut = createStyledLabel("Sign Out", "/Image/logo/signout.png");

        // Notification panel setup remains unchanged
        notificationPanel = new JPanel();
        notificationList = new JList<>();
        markAllReadButton = new JButton("Mark All as Read");

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

        notificationList.setBackground(isDarkMode ? new Color(30, 30, 30) : new Color(255, 255, 255));
        notificationList.setForeground(isDarkMode ? new Color(220, 220, 220) : new Color(0, 0, 0));
        notificationList.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        notificationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        notificationList.setCellRenderer(new NotificationCellRenderer());
        JScrollPane notificationScroll = new JScrollPane(notificationList);
        notificationScroll.setBorder(BorderFactory.createLineBorder(isDarkMode ? new Color(60, 60, 60) : new Color(200, 200, 200)));
        notificationPanel.add(notificationScroll, BorderLayout.CENTER);

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

        Searchfield.setHorizontalAlignment(JTextField.LEFT);
        getContentPane().add(Searchfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 30, 280, 30));

        search.setIcon(new ImageIcon(getClass().getResource("/Image/logo/search.jpg")));
        getContentPane().add(search, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 30, 30, 30));

        filter.setIcon(new ImageIcon(getClass().getResource("/Image/logo/filter.png")));
        getContentPane().add(filter, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 30, 30, 30));

        username.setFont(new Font("Segoe UI Semibold", Font.BOLD, 18));
        username.setText("Ram Kumar");
        getContentPane().add(username, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 20, 100, 30));

        email.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        email.setText("@ramkumar");
        getContentPane().add(email, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 50, 100, -1));

        profileIcon.setIcon(new ImageIcon(getClass().getResource("/Image/logo/ram.png")));
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

        getStarted.setForeground(Color.WHITE);
        getStarted.setText("Get Started");
        getStarted.setBackground(new Color(0, 4, 80));
        getStarted.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        learnMore.setForeground(Color.WHITE);
        learnMore.setText("Learn More");
        learnMore.setBackground(new Color(0, 4, 80));
        learnMore.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        jLabel1.setIcon(new ImageIcon(getClass().getResource("/Image/logo/3man.png")));

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

        getContentPane().add(content, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 80, 680, 430));

        featurePanel.setBackground(isDarkMode ? new Color(40, 40, 40) : new Color(255, 255, 255));
        logo.setBackground(isDarkMode ? new Color(40, 40, 40) : new Color(255, 255, 255));
        jLabel4.setIcon(new ImageIcon(getClass().getResource("/Image/pahilopaila_logo.png")));

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
                        .addComponent(logo, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(featurePanelLayout.createSequentialGroup()
                                .addGap(15)
                                .addGroup(featurePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(dashboard, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(vacancy, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(application, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(settings, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(myAccount, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(signOut, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(15, Short.MAX_VALUE))
        );
        featurePanelLayout.setVerticalGroup(
                featurePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(featurePanelLayout.createSequentialGroup()
                                .addComponent(logo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(29)
                                .addComponent(dashboard, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(vacancy, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(application, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(settings, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
                                .addGap(97)
                                .addComponent(myAccount, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(signOut, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(105, Short.MAX_VALUE))
        );

        getContentPane().add(featurePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 170, 540));
        getContentPane().add(notifications, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 20, 50, 50));

        pack();
    }

    public void setUserInfo(String usernameText, String emailText) {
        username.setText(usernameText);
        email.setText(emailText);
    }

    public JPanel getContentPanel() {
        return content;
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> {
            Dashboard_Recruiters view = new Dashboard_Recruiters();
            Dashboard_RecruitersController controller = new Dashboard_RecruitersController(view, 1);
            view.setController(controller);
            view.setVisible(true);
        });
    }
}