package pahilopaila.Controller;

import com.toedter.calendar.JDateChooser;
import pahilopaila.Dao.ApplicationDao;
import pahilopaila.Dao.CVDao;
import pahilopaila.Dao.RatingDao;
import pahilopaila.Dao.UserDao;
import pahilopaila.Dao.VacancyDao;
import pahilopaila.Dao.NotificationDao;
import pahilopaila.model.Notification;
import pahilopaila.model.UserData;
import pahilopaila.model.Vacancy;
import pahilopaila.view.Dashboard_JobSeekers;
import pahilopaila.view.LoginPageview;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import javax.swing.text.JTextComponent;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Controller for the Dashboard_JobSeekers view, handling user interactions and navigation.
 */
public class Dashboard_JobseekersController {
    private final Dashboard_JobSeekers view;
    private final CVDao cvDao;
    private final VacancyDao vacancyDao;
    private final NotificationDao notificationDao;
    private final UserDao userDao;
    private int userId;
    private String currentEmail;
    private static boolean isDarkMode = false;
    private static boolean notificationsEnabled = true;
    private int lastNotificationCount = 0;

    public Dashboard_JobseekersController(Dashboard_JobSeekers view, int userId) {
        this.view = view;
        this.userId = userId;
        this.cvDao = new CVDao();
        this.vacancyDao = new VacancyDao();
        this.notificationDao = new NotificationDao();
        this.userDao = new UserDao();
        this.currentEmail = null;
        initialize();
    }

    private void initialize() {
        initializeListeners();
        applyFeaturePanelTheme(); // Apply initial theme to featurePanel
        startNotificationPolling();
        updateNotificationBadge();
        showDashboardPanel();
    }
    private void initializeListeners() {
        view.dashboard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Dashboard label clicked");
                showDashboardPanel();
            }
        });
        view.vacancy.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Vacancy label clicked");
                showVacancyPanel();
            }
        });

        view.uploadCVItem.addActionListener(e -> {
            System.out.println("Upload CV menu item clicked");
            showCVUploadPanel();
        });

        view.viewCVItem.addActionListener(e -> {
            System.out.println("View CV menu item clicked");
            showCVDisplayPanel();
        });

        view.notifications.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Notifications label clicked");
                showNotificationPopup();
            }
        });

        view.settings.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Settings label clicked");
                showSettingsPanel();
            }
        });

        view.myAccount.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("My Account label clicked");
                showMyAccountPanel();
            }
        });

        view.signOut.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Sign Out label clicked");
                signOut();
            }
        });

        view.getStarted.addActionListener(this::handleGetStarted);
        view.learnMore.addActionListener(this::handleLearnMore);
        view.search.addActionListener(this::handleSearch);
        view.see_all.addActionListener(this::handleSeeAll);
        view.filter.addActionListener(this::handleFilter);
    }
    private void updateContentPanel(JPanel newPanel) {
        view.content.removeAll();
        view.content.setLayout(new java.awt.BorderLayout());
        view.content.add(newPanel, java.awt.BorderLayout.CENTER);

    // Starts a timer to poll for new notifications every 5 seconds
    private void startNotificationPolling() {
        new Timer(true).scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkForNewNotifications();
            }
        }, 0, 5000);
    }

    // Checks for new notifications and triggers the pop-up if new unread notifications are found
    private void checkForNewNotifications() {
        try {
            if (!notificationsEnabled) return;
            List<Notification> notifications = notificationDao.getNotificationsByUserId(userId);
            int unreadCount = (int) notifications.stream().filter(n -> !n.isRead()).count();
            if (unreadCount > lastNotificationCount) {
                showNotificationPopup();
                lastNotificationCount = unreadCount;
            }
            updateNotificationBadge();
        } catch (Exception e) {
            showError("Error checking notifications: " + e.getMessage());
        }
    }

    // Updates the notification badge on the bell icon with the unread count
    private void updateNotificationBadge() {
        try {
            List<Notification> notifications = notificationDao.getNotificationsByUserId(userId);
            int unreadCount = (int) notifications.stream().filter(n -> !n.isRead()).count();
            view.notifications.setText("Notifications (" + unreadCount + ")");
        } catch (Exception e) {
            showError("Error updating notification badge: " + e.getMessage());
        }
    }

    // Displays a pop-up dialog showing the user's notifications
    private void showNotificationPopup() {
        try {
            List<Notification> notifications = notificationDao.getNotificationsByUserId(userId);

            JDialog dialog = new JDialog(view, "Notifications", true);
            dialog.setSize(400, 300);
            dialog.setLocationRelativeTo(view);
            dialog.setLayout(new BorderLayout());
            dialog.getContentPane().setBackground(new Color(245, 245, 245));

            JLabel titleLabel = new JLabel("Your Notifications", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            titleLabel.setForeground(new Color(0, 0, 102));
            titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            dialog.add(titleLabel, BorderLayout.NORTH);

            JList<Notification> notificationList = new JList<>(notifications.toArray(new Notification[0]));
            notificationList.setCellRenderer(new NotificationRenderer());
            notificationList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            notificationList.setBackground(new Color(255, 255, 255));
            JScrollPane scrollPane = new JScrollPane(notificationList);
            scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
            dialog.add(scrollPane, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setBackground(new Color(245, 245, 245));

            JButton markReadButton = new JButton("Mark All as Read");
            markReadButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            markReadButton.setBackground(new Color(0, 102, 0));
            markReadButton.setForeground(Color.WHITE);
            markReadButton.setFocusPainted(false);
            markReadButton.addActionListener(_ -> {
                markAllNotificationsRead();
                dialog.dispose();
                showNotificationPopup();
            });
            buttonPanel.add(markReadButton);

            JButton closeButton = new JButton("Close");
            closeButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            closeButton.setBackground(new Color(0, 0, 102));
            closeButton.setForeground(Color.WHITE);
            closeButton.setFocusPainted(false);
            closeButton.addActionListener(e -> dialog.dispose());
            buttonPanel.add(closeButton);

            dialog.add(buttonPanel, BorderLayout.SOUTH);

            applyThemeToPanel(buttonPanel);
            applyThemeToComponent(notificationList);
            applyThemeToComponent(scrollPane);

            dialog.setVisible(true);
        } catch (Exception e) {
            showError("Error loading notifications: " + e.getMessage());
        }
    }

        private void applyFilter() {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

    // Custom renderer for the notification list
    private static class NotificationRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            Notification notification = (Notification) value;
            String text = notification.getMessage() + " (" + notification.getTimestamp() + ")";
            label.setText(text);
            label.setForeground(notification.isImportant() ? Color.RED : new Color(102, 102, 102));
            label.setFont(new Font("Segoe UI", notification.isRead() ? Font.PLAIN : Font.BOLD, 14));
            return label;
        }
    }

    // Simulates a new notification and saves it to the database
    private void simulateNewNotification() {
        try {
            ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("+0545"));
            String timestamp = zonedDateTime.format(DateTimeFormatter.ofPattern("hh:mm a zzz 'on' EEEE, MMMM dd, yyyy"));
            boolean saved = notificationDao.saveNotification(userId, "New job match found!", timestamp, true);
            if (saved) {
                System.out.println("Test notification saved for user_id: " + userId);
                updateNotificationBadge();
            } else {
                showError("Failed to save test notification");
            }
        } catch (Exception e) {
            showError("Error saving test notification: " + e.getMessage());
        }
    }

    // Marks all notifications as read in the database
    private void markAllNotificationsRead() {
        try {
            boolean updated = notificationDao.markAllNotificationsRead(userId);
            if (updated) {
                System.out.println("All notifications marked as read for user_id: " + userId);
                updateNotificationBadge();
            } else {
                showError("Failed to mark notifications as read");
            }
        } catch (Exception e) {
            showError("Error marking notifications as read: " + e.getMessage());
        }
    }

    // Displays an error message to the user via JOptionPane
    private void showError(String message) {
        JOptionPane.showMessageDialog(view, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Update the content panel with a new panel
    private void updateContentPanel(JPanel newPanel) {
        view.content.removeAll();
        view.content.setLayout(new BorderLayout());
        view.content.add(newPanel, BorderLayout.CENTER);

        applyThemeToPanel(newPanel);
        view.content.revalidate();
        view.content.repaint();
    }
    // Helper method to apply the current theme to a panel
    private void applyThemeToPanel(JPanel panel) {
        if (isDarkMode) {
            applyDarkModeToSettings(true, panel);
        } else {
            applyDarkModeToSettings(false, panel);
        }
    }
private void applyFeaturePanelTheme() {
    // Set background color for the feature panel with a lighter dark mode
    view.featurePanel.setBackground(isDarkMode ? new Color(70, 70, 70) : new Color(245, 245, 245)); // Lightened from 51,51,51

    // Initialize or update logo panel
    if (view.logo.getComponentCount() == 0) {
        JLabel dynamicLogo = new JLabel();
        dynamicLogo.setOpaque(false);
        ImageIcon logoIcon = null;
        try {
            logoIcon = new ImageIcon(getClass().getResource("/Image/pahilopaila_logo.png"));
            if (logoIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                dynamicLogo.setIcon(logoIcon);
                System.out.println("Logo loaded successfully. Icon size: " + logoIcon.getIconWidth() + "x" + logoIcon.getIconHeight());
            } else {
                System.err.println("Logo image failed to load.");
                dynamicLogo.setText("Logo Unavailable");
                dynamicLogo.setForeground(isDarkMode ? Color.WHITE : Color.BLACK);
            }
        } catch (Exception e) {
            System.err.println("Error loading logo image: " + e.getMessage());
            dynamicLogo.setText("Logo Unavailable");
            dynamicLogo.setForeground(isDarkMode ? Color.WHITE : Color.BLACK);
        }

        // Reduced preferred size of logo
        int logoWidth = (logoIcon != null && logoIcon.getIconWidth() > 0) ? logoIcon.getIconWidth() : 120; // Reduced from 150
        int logoHeight = (logoIcon != null && logoIcon.getIconHeight() > 0) ? logoIcon.getIconHeight() : 80;
        dynamicLogo.setPreferredSize(new Dimension(logoWidth, logoHeight));
        view.logo.setOpaque(false);
        view.logo.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        view.logo.add(dynamicLogo, gbc);
        view.logo.setPreferredSize(new Dimension(logoWidth, logoHeight)); // Reduced from 150x80
    } else {
        // Update existing logo to force image repaint
        Component logoComp = view.logo.getComponent(0);
        if (logoComp instanceof JLabel) {
            JLabel dynamicLogo = (JLabel) logoComp;
            ImageIcon logoIcon = (ImageIcon) dynamicLogo.getIcon();
            if (logoIcon != null) {
                logoIcon.getImage().flush();
                dynamicLogo.setIcon(new ImageIcon(logoIcon.getImage()));
                System.out.println("Logo icon refreshed. New size: " + logoIcon.getIconWidth() + "x" + logoIcon.getIconHeight());
            }
            dynamicLogo.setForeground(isDarkMode ? Color.WHITE : Color.BLACK); // Update text color if placeholder
            dynamicLogo.repaint();
        }
    }

    // Debug logo state
    if (view.logo.getComponentCount() > 0) {
        Component logoComp = view.logo.getComponent(0);
        System.out.println("Logo component: " + logoComp.getClass().getSimpleName() + 
                           ", Visible: " + logoComp.isVisible() + 
                           ", Bounds: " + logoComp.getBounds() + 
                           ", Parent visible: " + view.logo.isVisible() + 
                           ", Parent bounds: " + view.logo.getBounds());
    }

    // Create or update top panel
    JPanel topPanel = new JPanel();
    topPanel.setPreferredSize(new Dimension(0, 80)); // Height unchanged
    topPanel.setBackground(isDarkMode ? Color.WHITE : new Color(245, 245, 245));
    topPanel.setOpaque(false); // Ensure transparency
    topPanel.setLayout(new BorderLayout());
    topPanel.removeAll(); // Clear to avoid duplicate adds
    topPanel.add(view.logo, BorderLayout.CENTER);

    // Setup GroupLayout for feature panel with reduced width
    javax.swing.GroupLayout featurePanelLayout = new javax.swing.GroupLayout(view.featurePanel);
    view.featurePanel.setLayout(featurePanelLayout);
    featurePanelLayout.setHorizontalGroup(
        featurePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(topPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE) // Reduced max width to 140
            .addGroup(featurePanelLayout.createSequentialGroup()
                .addGap(15, 15, 15) // Reduced from 25 to 15
                .addGroup(featurePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(view.dashboard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(view.vacancy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(view.CV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(view.settings, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(view.myAccount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(view.signOut, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(20, Short.MAX_VALUE)) // Reduced from 30 to 20
    );
    featurePanelLayout.setVerticalGroup(
        featurePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(featurePanelLayout.createSequentialGroup()
                .addComponent(topPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(view.dashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(view.vacancy, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(view.CV, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(view.settings, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(85, 85, 85)
                .addComponent(view.myAccount, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(view.signOut, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(105, Short.MAX_VALUE))
    );

    // Apply theme to inner panels, including darkening button backgrounds
    for (Component comp : view.featurePanel.getComponents()) {
        if (comp instanceof JPanel && comp != view.logo && comp != topPanel) {
            JPanel panel = (JPanel) comp;
            panel.setBackground(isDarkMode ? new Color(40, 40, 40) : Color.WHITE); // Darkened from white in dark mode
            applyThemeToPanel(panel);
        }
    }

    // Force complete UI refresh
    topPanel.revalidate();
    topPanel.repaint();
    view.logo.revalidate();
    view.logo.repaint();
    view.featurePanel.revalidate();
    view.featurePanel.repaint();
    SwingUtilities.invokeLater(() -> {
        view.featurePanel.repaint();
        System.out.println("Theme applied. Mode: " + (isDarkMode ? "Dark" : "Light") + 
                           ", Logo visible: " + (view.logo.getComponentCount() > 0 && view.logo.getComponent(0).isVisible()));
    });
}

    // Create a vacancy card for display of vacancies
    private JPanel createVacancyCard(Vacancy vacancy) {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(isDarkMode ? new Color(0, 4, 80) : new Color(245, 245, 245));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(isDarkMode ? new Color(50, 50, 50) : new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setPreferredSize(new Dimension(200, 200));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel titleLabel = new JLabel(vacancy.getJobTitle());
        titleLabel.setFont(new Font("Yu Gothic UI Semibold", Font.BOLD, 18));
        titleLabel.setForeground(isDarkMode ? Color.WHITE : new Color(0, 4, 80));
        gbc.gridx = 0;
        gbc.gridy = 0;
        card.add(titleLabel, gbc);

        JLabel typeLabel = new JLabel(vacancy.getJobType());
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        typeLabel.setForeground(isDarkMode ? Color.WHITE : new Color(0, 4, 80));
        gbc.gridy = 1;
        card.add(typeLabel, gbc);

        JLabel experienceLabel = new JLabel(vacancy.getExperienceLevel());
        experienceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        experienceLabel.setForeground(isDarkMode ? Color.WHITE : new Color(0, 4, 80));
        gbc.gridy = 2;
        card.add(experienceLabel, gbc);

        JLabel daysLeftLabel = new JLabel(vacancy.getDaysLeft() + " Days Left");
        daysLeftLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        daysLeftLabel.setForeground(isDarkMode ? Color.WHITE : new Color(0, 4, 80));
        gbc.gridy = 3;
        card.add(daysLeftLabel, gbc);

        JButton applyButton = new JButton("Apply") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2d.setColor(isDarkMode ? new Color(0, 20, 120) : new Color(0, 20, 120));
                } else {
                    g2d.setColor(isDarkMode ? new Color(0, 4, 80) : new Color(0, 4, 80));
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        applyButton.setFont(new Font("Segoe UI Semibold", Font.BOLD, 14));
        applyButton.setForeground(Color.WHITE);
        applyButton.setContentAreaFilled(false);
        applyButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        applyButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        applyButton.setFocusPainted(false);
        gbc.gridy = 4;
        gbc.insets = new Insets(10, 5, 5, 5);
        card.add(applyButton, gbc);

        ApplicationDao applicationDao = new ApplicationDao();
        applyButton.addActionListener(e -> {
            if (applicationDao.hasApplied(userId, vacancy.getId())) {
                JOptionPane.showMessageDialog(view, "You have already applied for this vacancy.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            ResultSet rs = cvDao.getCVByUserId(userId);
            try {
                if (rs == null || !rs.next()) {
                    JOptionPane.showMessageDialog(view, "Please upload a CV before applying.", "Error", JOptionPane.ERROR_MESSAGE);
                    if (rs != null) rs.close();
                    return;
                }
                int cvId = rs.getInt("id");
                rs.close();

                boolean success = applicationDao.saveApplication(userId, vacancy.getRecruiterId(), vacancy.getId(), cvId);
                if (success) {
                    JOptionPane.showMessageDialog(view, "Application submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("+0545"));
                    String timestamp = zonedDateTime.format(DateTimeFormatter.ofPattern("hh:mm a zzz 'on' EEEE, MMMM dd, yyyy"));
                    notificationDao.saveNotification(userId, "Application submitted for " + vacancy.getJobTitle(), timestamp, false);
                } else {
                    JOptionPane.showMessageDialog(view, "Failed to submit application. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                showError("Error processing application: " + ex.getMessage());
            }
        });

        return card;
    }

public void showDashboardPanel() {
    System.out.println("Navigating to Dashboard");
    JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
    contentPanel.setBackground(isDarkMode ? new Color(40, 40, 40) : new Color(245, 245, 245)); // Lightened from 25,25,25

    // Create a wrapper panel with BoxLayout for vertical control
    JPanel wrapperPanel = new JPanel();
    wrapperPanel.setLayout(new BoxLayout(wrapperPanel, BoxLayout.Y_AXIS));
    wrapperPanel.setBackground(isDarkMode ? new Color(40, 40, 40) : new Color(245, 245, 245)); // Match contentPanel

    // Add left padding for horizontal gap
    JPanel leftPadding = new JPanel();
    leftPadding.setPreferredSize(new Dimension(25, 0)); // Match featurePanel gap
    leftPadding.setBackground(isDarkMode ? new Color(40, 40, 40) : new Color(245, 245, 245));
    wrapperPanel.add(leftPadding);

    // Message panel
    JPanel messagePanel = new JPanel(new BorderLayout());
    messagePanel.setBackground(isDarkMode ? new Color(0, 4, 80) : new Color(0, 4, 80)); // Original color unchanged
    messagePanel.setLayout(new javax.swing.GroupLayout(messagePanel));

    JLabel discover = new JLabel("Discover Opportunities That");
    discover.setFont(new java.awt.Font("Segoe UI Symbol", 1, 24));
    discover.setForeground(isDarkMode ? Color.WHITE : new Color(255, 255, 255));

    JLabel match = new JLabel("Match Your Skill");
    match.setFont(new java.awt.Font("Segoe UI Symbol", 1, 24));
    match.setForeground(isDarkMode ? Color.WHITE : new Color(255, 255, 255));

    JButton getStarted = new JButton("Get Started");
    getStarted.setForeground(isDarkMode ? Color.WHITE : new Color(0, 0, 102));
    getStarted.setBackground(isDarkMode ? new Color(20, 20, 20) : null); // Darkened from 33,150,243
    getStarted.addActionListener(this::handleGetStarted);

    JButton learnMore = new JButton("Learn More");
    learnMore.setForeground(isDarkMode ? Color.WHITE : new Color(255, 255, 255));
    learnMore.setBackground(isDarkMode ? new Color(10, 10, 50) : new Color(0, 4, 80)); // Darkened from 0,4,80
    learnMore.setBorder(isDarkMode ? BorderFactory.createEmptyBorder() : javax.swing.BorderFactory.createTitledBorder(null, "",
        javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
        javax.swing.border.TitledBorder.DEFAULT_POSITION,
        new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(255, 255, 255)));
    learnMore.addActionListener(this::handleLearnMore);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(messagePanel);
    messagePanel.setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(discover, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(match, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(getStarted, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(learnMore, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(276, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(discover)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(match)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(getStarted)
                    .addComponent(learnMore, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(12, Short.MAX_VALUE))
    );

    // Featured panel (containing vacancies)
    JPanel featuredPanel = new JPanel(new BorderLayout());
    featuredPanel.setBackground(isDarkMode ? new Color(40, 40, 40) : new Color(245, 245, 245)); // Match contentPanel

    JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    headerPanel.setBackground(isDarkMode ? new Color(40, 40, 40) : new Color(245, 245, 245));
    JLabel featuredLabel = new JLabel("Featured Jobs");
    featuredLabel.setFont(new Font("Microsoft Himalaya", Font.BOLD, 36));
    featuredLabel.setForeground(isDarkMode ? Color.WHITE : new Color(0, 4, 80));
    headerPanel.add(featuredLabel);

    JButton seeAllButton = new JButton("See all");
    seeAllButton.setBackground(isDarkMode ? new Color(20, 20, 20) : new Color(0, 4, 80)); // Darkened
    seeAllButton.setForeground(Color.WHITE);
    seeAllButton.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
    seeAllButton.addActionListener(this::handleSeeAll);
    headerPanel.add(Box.createHorizontalGlue());
    headerPanel.add(seeAllButton);
    featuredPanel.add(headerPanel, BorderLayout.NORTH);

    JPanel vacanciesPanel = new JPanel(new GridLayout(1, 3, 10, 10));
    vacanciesPanel.setBackground(isDarkMode ? new Color(40, 40, 40) : new Color(245, 245, 245));
    JScrollPane scrollPane = new JScrollPane(vacanciesPanel);
    scrollPane.setBorder(null);

    List<Vacancy> allVacancies = vacancyDao.getAllVacancies();
    if (allVacancies.isEmpty()) {
        JLabel noVacanciesLabel = new JLabel("No vacancies available.");
        noVacanciesLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        noVacanciesLabel.setForeground(isDarkMode ? Color.WHITE : new Color(0, 4, 80));
        noVacanciesLabel.setHorizontalAlignment(SwingConstants.CENTER);
        vacanciesPanel.add(noVacanciesLabel);
    } else {
        Random rand = new Random();
        int count = Math.min(3, allVacancies.size());
        java.util.Collections.shuffle(allVacancies, rand);
        for (int i = 0; i < count; i++) {
            JPanel card = createVacancyCard(allVacancies.get(i));
            vacanciesPanel.add(card);
        }

=
    featuredPanel.add(scrollPane, BorderLayout.CENTER);

    // Add panels to wrapper with explicit gap
    wrapperPanel.add(messagePanel);
    wrapperPanel.add(Box.createVerticalStrut(15)); // Consistent gap
    wrapperPanel.add(featuredPanel);


    // Add wrapper to content panel
    contentPanel.add(wrapperPanel, BorderLayout.CENTER);

    updateContentPanel(contentPanel);
}


    public void showVacancyPanel() {
        System.out.println("Navigating to Vacancy");
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(isDarkMode ? new Color(25, 25, 25) : new Color(245, 245, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel headerLabel = new JLabel("Browse Vacancies");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(isDarkMode ? Color.WHITE : new Color(0, 4, 80));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        JPanel vacanciesPanel = new JPanel(new GridLayout(0, 3, 10, 10));

        vacanciesPanel.setBackground(isDarkMode ? new Color(25, 25, 25) : new Color(245, 245, 245));

        JScrollPane scrollPane = new JScrollPane(vacanciesPanel);
        scrollPane.setBorder(null);

        List<Vacancy> vacancies = vacancyDao.getAllVacancies();
        if (vacancies.isEmpty()) {
            JLabel noVacanciesLabel = new JLabel("No vacancies available.");
            noVacanciesLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            noVacanciesLabel.setForeground(isDarkMode ? Color.WHITE : new Color(0, 4, 80));
            noVacanciesLabel.setHorizontalAlignment(SwingConstants.CENTER);
            vacanciesPanel.add(noVacanciesLabel);
        } else {
            for (Vacancy vacancy : vacancies) {
                JPanel vacancyCard = createVacancyCard(vacancy);
                vacanciesPanel.add(vacancyCard);
            }
        }

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        updateContentPanel(mainPanel);
    }

    public void showCVUploadPanel() {
        System.out.println("Navigating to CV Upload");
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(isDarkMode ? new Color(25, 25, 25) : new Color(245, 245, 245));
        mainPanel.setLayout(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, isDarkMode ? new Color(33, 150, 243) : new Color(0, 4, 80),
                    0, getHeight(), isDarkMode ? new Color(66, 165, 245) : new Color(0, 20, 120));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setPreferredSize(new Dimension(680, 70));
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 25, 20));

        JLabel headerLabel = new JLabel("CV Upload");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(isDarkMode ? Color.WHITE : Color.WHITE);
        headerPanel.add(headerLabel);

        JPanel formPanel = new JPanel();
        formPanel.setBackground(isDarkMode ? new Color(30, 30, 30) : new Color(252, 252, 252));
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 2, isDarkMode ? new Color(50, 50, 50) : new Color(180, 180, 180, 100)),
            BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20),
                BorderFactory.createLineBorder(isDarkMode ? new Color(50, 50, 50) : new Color(200, 200, 200), 1, true)
            )
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
        firstNameLabel.setForeground(isDarkMode ? new Color(100, 181, 246) : new Color(0, 0, 102));
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(firstNameLabel, gbc);

        JTextField firstNameField = new JTextField(15);
        firstNameField.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        firstNameField.setBackground(isDarkMode ? new Color(30, 30, 30) : new Color(245, 245, 245));
        firstNameField.setForeground(isDarkMode ? new Color(230, 230, 230) : Color.BLACK);
        firstNameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(isDarkMode ? new Color(50, 50, 50) : new Color(150, 150, 150), 1, true),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(firstNameField, gbc);

        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
        lastNameLabel.setForeground(isDarkMode ? new Color(100, 181, 246) : new Color(0, 0, 102));
        gbc.gridx = 2;
        gbc.gridy = 0;
        formPanel.add(lastNameLabel, gbc);

        JTextField lastNameField = new JTextField(15);
        lastNameField.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lastNameField.setBackground(isDarkMode ? new Color(30, 30, 30) : new Color(245, 245, 245));
        lastNameField.setForeground(isDarkMode ? new Color(230, 230, 230) : Color.BLACK);
        lastNameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(isDarkMode ? new Color(50, 50, 50) : new Color(150, 150, 150), 1, true),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        gbc.gridx = 3;
        gbc.gridy = 0;
        formPanel.add(lastNameField, gbc);

        JLabel dobLabel = new JLabel("Date of Birth:");
        dobLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
        dobLabel.setForeground(isDarkMode ? new Color(100, 181, 246) : new Color(0, 0, 102));
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(dobLabel, gbc);

        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        dateChooser.setBackground(isDarkMode ? new Color(30, 30, 30) : new Color(245, 245, 245));
        dateChooser.setForeground(isDarkMode ? new Color(230, 230, 230) : Color.BLACK);
        dateChooser.setPreferredSize(new Dimension(150, 25));
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(dateChooser, gbc);

        JLabel contactLabel = new JLabel("Contact:");
        contactLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
        contactLabel.setForeground(isDarkMode ? new Color(100, 181, 246) : new Color(0, 0, 102));
        gbc.gridx = 2;
        gbc.gridy = 1;
        formPanel.add(contactLabel, gbc);

        JTextField contactField = new JTextField(15);
        contactField.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        contactField.setBackground(isDarkMode ? new Color(30, 30, 30) : new Color(245, 245, 245));
        contactField.setForeground(isDarkMode ? new Color(230, 230, 230) : Color.BLACK);
        contactField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(isDarkMode ? new Color(50, 50, 50) : new Color(150, 150, 150), 1, true),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        gbc.gridx = 3;
        gbc.gridy = 1;
        formPanel.add(contactField, gbc);

        JLabel educationLabel = new JLabel("Education:");
        educationLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
        educationLabel.setForeground(isDarkMode ? new Color(100, 181, 246) : new Color(0, 0, 102));
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(educationLabel, gbc);

        JTextField educationField = new JTextField(15);
        educationField.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        educationField.setBackground(isDarkMode ? new Color(30, 30, 30) : new Color(245, 245, 245));
        educationField.setForeground(isDarkMode ? new Color(230, 230, 230) : Color.BLACK);
        educationField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(isDarkMode ? new Color(50, 50, 50) : new Color(150, 150, 150), 1, true),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(educationField, gbc);

        JLabel skillsLabel = new JLabel("Skills:");
        skillsLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
        skillsLabel.setForeground(isDarkMode ? new Color(100, 181, 246) : new Color(0, 0, 102));
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(skillsLabel, gbc);

        JTextField skillsField = new JTextField(15);
        skillsField.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        skillsField.setBackground(isDarkMode ? new Color(30, 30, 30) : new Color(245, 245, 245));
        skillsField.setForeground(isDarkMode ? new Color(230, 230, 230) : Color.BLACK);
        skillsField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(isDarkMode ? new Color(50, 50, 50) : new Color(150, 150, 150), 1, true),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(skillsField, gbc);

        JLabel experienceLabel = new JLabel("Experience:");
        experienceLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
        experienceLabel.setForeground(isDarkMode ? new Color(100, 181, 246) : new Color(0, 0, 102));
        gbc.gridx = 2;
        gbc.gridy = 2;
        formPanel.add(experienceLabel, gbc);

        JTextArea experienceArea = new JTextArea(4, 15);
        experienceArea.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        experienceArea.setBackground(isDarkMode ? new Color(30, 30, 30) : new Color(245, 245, 245));
        experienceArea.setForeground(isDarkMode ? new Color(230, 230, 230) : Color.BLACK);
        experienceArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(isDarkMode ? new Color(50, 50, 50) : new Color(150, 150, 150), 1, true),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        experienceArea.setLineWrap(true);
        experienceArea.setWrapStyleWord(true);
        JScrollPane experienceScroll = new JScrollPane(experienceArea);
        experienceScroll.setPreferredSize(new Dimension(150, 80));
        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.gridheight = 2;
        formPanel.add(experienceScroll, gbc);

        JButton submitButton = new JButton("Submit") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2d.setColor(isDarkMode ? new Color(66, 165, 245) : new Color(0, 20, 120));
                } else {
                    g2d.setColor(isDarkMode ? new Color(33, 150, 243) : new Color(0, 4, 80));
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        submitButton.setFont(new Font("Segoe UI Semibold", Font.BOLD, 14));
        submitButton.setForeground(Color.WHITE);
        submitButton.setContentAreaFilled(false);
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(submitButton, gbc);

        submitButton.addActionListener(e -> {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            java.util.Date dobDate = dateChooser.getDate();
            String contact = contactField.getText().trim();
            String education = educationField.getText().trim();
            String skills = skillsField.getText().trim();
            String experience = experienceArea.getText().trim();

            if (firstName.isEmpty() || lastName.isEmpty() || dobDate == null || contact.isEmpty() ||
                education.isEmpty() || skills.isEmpty() || experience.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dob = sdf.format(dobDate);
            boolean success = cvDao.saveCV(userId, firstName, lastName, dob, contact, education, skills, experience);
            if (success) {
                JOptionPane.showMessageDialog(view, "CV saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

                firstNameField.setText("");
                lastNameField.setText("");
                dateChooser.setDate(null);
                contactField.setText("");
                educationField.setText("");
                skillsField.setText("");
                experienceArea.setText("");
            } else {
                JOptionPane.showMessageDialog(view, "Failed to save CV. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel centerWrapper = new JPanel();
        centerWrapper.setBackground(isDarkMode ? new Color(25, 25, 25) : new Color(245, 245, 245));
        centerWrapper.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        centerWrapper.add(formPanel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerWrapper, BorderLayout.CENTER);
        updateContentPanel(mainPanel);
    }

    public void showCVDisplayPanel() {
        System.out.println("Navigating to CV Display");
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(isDarkMode ? new Color(25, 25, 25) : new Color(240, 242, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, isDarkMode ? new Color(33, 150, 243) : new Color(25, 118, 210),
                    0, getHeight(), isDarkMode ? new Color(66, 165, 245) : new Color(144, 202, 249));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setPreferredSize(new Dimension(0, 60));
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));

        JLabel headerLabel = new JLabel("My Resume");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(isDarkMode ? Color.WHITE : Color.WHITE);
        headerPanel.add(headerLabel);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(isDarkMode ? new Color(30, 30, 30) : Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(isDarkMode ? new Color(50, 50, 50) : new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        try (ResultSet rs = cvDao.getCVByUserId(userId)) {
            if (rs != null && rs.next()) {
                String[] labels = {"First Name", "Last Name", "Date of Birth", "Contact", "Education", "Skills", "Experience"};
                String[] values = {
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("dob"),
                    rs.getString("contact"),
                    rs.getString("education"),
                    rs.getString("skills"),
                    rs.getString("experience")
                };

                int row = 0;
                for (int i = 0; i < labels.length; i++) {
                    JLabel label = new JLabel(labels[i] + ":");
                    label.setFont(new Font("Segoe UI", Font.BOLD, 14));
                    label.setForeground(isDarkMode ? new Color(100, 181, 246) : new Color(0, 0, 102));
                    gbc.gridx = 0;
                    gbc.gridy = row;
                    gbc.gridwidth = 1;
                    formPanel.add(label, gbc);

                    if (i < 6) {
                        JLabel valueLabel = new JLabel(values[i]);
                        valueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                        valueLabel.setForeground(isDarkMode ? new Color(230, 230, 230) : Color.BLACK);
                        gbc.gridx = 1;
                        gbc.gridy = row;
                        gbc.gridwidth = 3;
                        formPanel.add(valueLabel, gbc);
                        row++;
                    } else {
                        JTextArea experienceArea = new JTextArea(values[i], 5, 25);
                        experienceArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                        experienceArea.setBackground(isDarkMode ? new Color(30, 30, 30) : new Color(240, 242, 245));
                        experienceArea.setForeground(isDarkMode ? new Color(230, 230, 230) : Color.BLACK);
                        experienceArea.setBorder(BorderFactory.createLineBorder(isDarkMode ? new Color(50, 50, 50) : new Color(200, 200, 200), 1, true));

                        experienceArea.setLineWrap(true);
                        experienceArea.setWrapStyleWord(true);
                        experienceArea.setEditable(false);
                        JScrollPane scrollPane = new JScrollPane(experienceArea);
                        scrollPane.setPreferredSize(new Dimension(300, 100));

                        gbc.gridx = 1;
                        gbc.gridy = row;
                        gbc.gridwidth = 3;
                        gbc.gridheight = 2;
                        formPanel.add(scrollPane, gbc);
                        row += 2;
                    }
                }
            } else {
                JLabel noCVLabel = new JLabel("No resume found.");
                noCVLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

                noCVLabel.setForeground(isDarkMode ? Color.WHITE : new Color(0, 4, 80));

                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.gridwidth = 4;
                formPanel.add(noCVLabel, gbc);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving CV: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error retrieving resume data.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        JPanel centerWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerWrapper.setBackground(isDarkMode ? new Color(25, 25, 25) : new Color(240, 242, 245));
        centerWrapper.add(formPanel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerWrapper, BorderLayout.CENTER);
        updateContentPanel(mainPanel);
    }

    public void showNotificationsPanel() {
        System.out.println("Navigating to Notifications");
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setLayout(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                    0, 0, new Color(0, 4, 80),
                    0, getHeight(), new Color(0, 20, 120)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setPreferredSize(new Dimension(680, 70));
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 25, 20));

        JLabel headerLabel = new JLabel("Notifications");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        JPanel notificationsPanel = new JPanel();
        notificationsPanel.setBackground(new Color(252, 252, 252));
        notificationsPanel.setLayout(new BoxLayout(notificationsPanel, BoxLayout.Y_AXIS));
        notificationsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 2, new Color(180, 180, 180, 100)),
            BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20),
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true)
            )
        ));

        if (!notificationsEnabled) {
            JLabel disabledLabel = new JLabel("Notifications are disabled. Enable them in Settings.");
            disabledLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            disabledLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            notificationsPanel.add(disabledLabel);
        } else {
            List<Notification> notifications = notificationDao.getNotificationsByUserId(userId);
            if (notifications.isEmpty()) {
                JLabel noNotificationsLabel = new JLabel("No notifications available.");
                noNotificationsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                noNotificationsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                notificationsPanel.add(noNotificationsLabel);
            } else {
                for (Notification notification : notifications) {
                    JPanel notificationCard = new JPanel();
                    notificationCard.setLayout(new BorderLayout(10, 10));
                    notificationCard.setBackground(notification.isImportant() ? new Color(255, 245, 245) : Color.WHITE);
                    notificationCard.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(notification.isImportant() ? new Color(255, 100, 100) : new Color(200, 200, 200), 1, true),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                    ));
                    notificationCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

                    JLabel messageLabel = new JLabel(notification.getMessage());
                    messageLabel.setFont(new Font("Segoe UI", notification.isImportant() ? Font.BOLD : Font.PLAIN, 12));
                    messageLabel.setForeground(notification.isImportant() ? new Color(200, 0, 0) : Color.BLACK);
                    notificationCard.add(messageLabel, BorderLayout.CENTER);

                    JLabel timestampLabel = new JLabel(notification.getTimestamp());
                    timestampLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                    timestampLabel.setForeground(Color.GRAY);
                    notificationCard.add(timestampLabel, BorderLayout.SOUTH);

                    notificationsPanel.add(notificationCard);
                    notificationsPanel.add(Box.createVerticalStrut(10));
                }
            }
        }

        JScrollPane scrollPane = new JScrollPane(notificationsPanel);
        scrollPane.setBorder(null);

        JPanel centerWrapper = new JPanel();
        centerWrapper.setBackground(new Color(245, 245, 245));
        centerWrapper.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        centerWrapper.add(scrollPane);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerWrapper, BorderLayout.CENTER);
        updateContentPanel(mainPanel);
    }

    private void simulateStatusChangeNotifications() {
        if (!notificationsEnabled) return;
        String[] statuses = {
            "Your application is under review.",
            "Your application has been shortlisted.",
            "Your application has been rejected.",
            "Interview scheduled for your application."
        };
        Random rand = new Random();
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("+0545"));
        String timestamp = zonedDateTime.format(DateTimeFormatter.ofPattern("hh:mm a zzz 'on' EEEE, MMMM dd, yyyy"));
        String message = statuses[rand.nextInt(statuses.length)];
        boolean isImportant = message.contains("rejected") || message.contains("scheduled");
        notificationDao.saveNotification(userId, message, timestamp, isImportant);
    }

    public void showSettingsPanel() {
        System.out.println("Navigating to Settings");
        JPanel settingsPanel = new JPanel();
        settingsPanel.setBackground(isDarkMode ? new Color(25, 25, 25) : new Color(245, 245, 245));
        settingsPanel.setLayout(new java.awt.BorderLayout(15, 15));
        settingsPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel titlePanel = new JPanel();

        titlePanel.setBackground(isDarkMode ? new Color(0, 4, 80) : new Color(0, 20, 90));

        titlePanel.setPreferredSize(new java.awt.Dimension(680, 70));
        titlePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 25, 20));

        JLabel titleLabel = new JLabel("Settings");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(isDarkMode ? Color.WHITE : Color.WHITE);
        titlePanel.add(titleLabel);

        JPanel contentBox = new JPanel();
        contentBox.setBackground(isDarkMode ? new Color(30, 30, 30) : Color.WHITE);
        contentBox.setBorder(BorderFactory.createLineBorder(isDarkMode ? new Color(50, 50, 50) : Color.LIGHT_GRAY));
        contentBox.setLayout(new BoxLayout(contentBox, BoxLayout.Y_AXIS));
        contentBox.setPreferredSize(new Dimension(500, 250));
        contentBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        JCheckBox darkModeCheck = new JCheckBox("Dark Mode");
        darkModeCheck.setBackground(isDarkMode ? new Color(30, 30, 30) : Color.WHITE);
        darkModeCheck.setForeground(isDarkMode ? new Color(230, 230, 230) : Color.BLACK);
        darkModeCheck.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        darkModeCheck.setAlignmentX(Component.LEFT_ALIGNMENT);
        darkModeCheck.setSelected(isDarkMode);

        JPanel darkModePanel = new JPanel();
        darkModePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        darkModePanel.setBackground(isDarkMode ? new Color(30, 30, 30) : Color.WHITE);
        darkModePanel.add(new JLabel("🌙"));

        darkModePanel.add(Box.createHorizontalStrut(10));
        darkModePanel.add(darkModeCheck);

        JCheckBox notificationCheck = new JCheckBox("Enable Notifications");
        notificationCheck.setBackground(isDarkMode ? new Color(30, 30, 30) : Color.WHITE);
        notificationCheck.setForeground(isDarkMode ? new Color(230, 230, 230) : Color.BLACK);
        notificationCheck.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        notificationCheck.setSelected(notificationsEnabled);
        notificationCheck.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel notificationPanel = new JPanel();
        notificationPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        notificationPanel.setBackground(isDarkMode ? new Color(30, 30, 30) : Color.WHITE);
        notificationPanel.add(new JLabel("🔔"));

        notificationPanel.add(Box.createHorizontalStrut(10));
        notificationPanel.add(notificationCheck);

        JLabel contactUsLabel = new JLabel("Contact Us: support@pahilopaila.com");
        contactUsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contactUsLabel.setForeground(isDarkMode ? new Color(100, 181, 246) : new Color(0, 123, 255));
        contactUsLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contactUsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel contactPanel = new JPanel();
        contactPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        contactPanel.setBackground(isDarkMode ? new Color(30, 30, 30) : Color.WHITE);
        contactPanel.add(new JLabel("📞"));
        contactPanel.add(Box.createHorizontalStrut(10));
        contactPanel.add(contactUsLabel);

        JButton updateButton = new JButton("Update Settings") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, isDarkMode ? new Color(33, 150, 243) : new Color(0, 20, 90),
                    0, getHeight(), isDarkMode ? new Color(66, 165, 245) : new Color(0, 4, 80));
                if (getModel().isRollover()) {
                    g2d.setPaint(gp);
                } else {
                    g2d.setColor(isDarkMode ? new Color(33, 150, 243) : new Color(0, 20, 90));
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };

        updateButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        updateButton.setForeground(Color.WHITE);
        updateButton.setPreferredSize(new Dimension(150, 35));
        updateButton.setFocusPainted(false);
        updateButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(isDarkMode ? new Color(30, 30, 30) : Color.WHITE);
        buttonPanel.add(updateButton);

        contentBox.add(Box.createVerticalStrut(20));
        contentBox.add(darkModePanel);
        contentBox.add(Box.createVerticalStrut(15));
        contentBox.add(notificationPanel);
        contentBox.add(Box.createVerticalStrut(15));
        contentBox.add(contactPanel);
        contentBox.add(Box.createVerticalStrut(20));
        contentBox.add(buttonPanel);
        contentBox.add(Box.createVerticalStrut(20));

        darkModeCheck.addActionListener(e -> {
            isDarkMode = darkModeCheck.isSelected();
            applyDarkModeToSettings(isDarkMode, settingsPanel);
            applyFeaturePanelTheme(); // Update featurePanel theme
            String status = isDarkMode ? "enabled" : "disabled";
            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(settingsPanel),
                "Dark mode " + status + " successfully!",
                "Dark Mode",
                JOptionPane.INFORMATION_MESSAGE);
        });

        notificationCheck.addActionListener(e -> {
            notificationsEnabled = notificationCheck.isSelected();
            String status = notificationsEnabled ? "enabled" : "disabled";
            System.out.println("Notifications " + status);
        });

        contactUsLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(settingsPanel),
                    "Contact Information:\n\n" +
                    "Email: support@pahilopaila.com\n" +
                    "Phone: +977-123-456-789\n" +
                    "Address: Kathmandu, Nepal\n\n" +
                    "We're here to help!",
                    "Contact Us",
                    JOptionPane.INFORMATION_MESSAGE);
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                contactUsLabel.setText("<html><u>Contact Us: support@pahilopaila.com</u></html>");
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                contactUsLabel.setText("Contact Us: support@pahilopaila.com");
            }
        });

        updateButton.addActionListener(e -> {
            isDarkMode = darkModeCheck.isSelected();
            notificationsEnabled = notificationCheck.isSelected();
            applyDarkModeToSettings(isDarkMode, settingsPanel);
            applyFeaturePanelTheme(); // Update featurePanel theme
            StringBuilder message = new StringBuilder("Settings Updated Successfully!\n\n");
            message.append("Dark Mode: ").append(isDarkMode ? "Enabled" : "Disabled").append("\n");
            message.append("Notifications: ").append(notificationsEnabled ? "Enabled" : "Disabled");
            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(settingsPanel),
                message.toString(),
                "Settings Updated",
                JOptionPane.INFORMATION_MESSAGE);
        });

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(isDarkMode ? new Color(30, 30, 30) : Color.WHITE);
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.add(contentBox);

        settingsPanel.add(titlePanel, BorderLayout.NORTH);
        settingsPanel.add(centerPanel, BorderLayout.CENTER);
        updateContentPanel(settingsPanel);
    }

    private void applyDarkModeToSettings(boolean isDarkMode, JPanel settingsPanel) {
        Window parentWindow = SwingUtilities.getWindowAncestor(settingsPanel);

        if (isDarkMode) {
            Color darkBackground = new Color(18, 18, 18);
            Color darkPanel = new Color(30, 30, 30);
            Color darkText = new Color(230, 230, 230);
            Color darkBorder = new Color(50, 50, 50);


            if (parentWindow != null) {
                parentWindow.setBackground(darkBackground);
                if (parentWindow instanceof JFrame) {
                    ((JFrame) parentWindow).getContentPane().setBackground(darkBackground);
                }
            }

            applyDarkThemeToComponent(settingsPanel, darkBackground, darkPanel, darkText, darkBorder);
        } else {
            Color lightBackground = Color.WHITE;
            Color lightPanel = new Color(245, 245, 245);
            Color lightText = Color.BLACK;
            Color lightBorder = Color.LIGHT_GRAY;

            if (parentWindow != null) {
                parentWindow.setBackground(lightBackground);
                if (parentWindow instanceof JFrame) {
                    ((JFrame) parentWindow).getContentPane().setBackground(lightPanel);
                }
            }

            applyLightThemeToComponent(settingsPanel, lightBackground, lightPanel, lightText, lightBorder);
        }

        if (parentWindow != null) {
            parentWindow.repaint();
        }
        settingsPanel.repaint();
    }

    private void applyDarkThemeToComponent(Container container, Color darkBg, Color darkPanel, Color darkText, Color darkBorder) {
        for (Component component : container.getComponents()) {
            if (component instanceof JPanel) {
                JPanel panel = (JPanel) component;
                if (panel.getBackground().equals(new Color(0, 20, 90)) ||
                    panel.getBackground().equals(new Color(0, 4, 80))) {
                    // Keep original dark blue for title bars
                } else if (panel.getBackground().equals(Color.WHITE) ||
                           panel.getBackground().equals(new Color(252, 252, 252)) ||
                           panel.getBackground().equals(new Color(245, 245, 245)) ||
                           panel.getBackground().equals(new Color(240, 242, 245))) {
                    panel.setBackground(darkPanel);

                }

                if (panel.getBorder() instanceof javax.swing.border.LineBorder) {
                    panel.setBorder(BorderFactory.createLineBorder(darkBorder));
                }

                applyDarkThemeToComponent(panel, darkBg, darkPanel, darkText, darkBorder);
            } else if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                if (!label.getForeground().equals(Color.WHITE) &&
                    !label.getForeground().equals(new Color(0, 123, 255)) &&
                    !label.getForeground().equals(new Color(100, 181, 246))) {

                    label.setForeground(darkText);
                }
            } else if (component instanceof JCheckBox) {
                JCheckBox checkBox = (JCheckBox) component;
                checkBox.setBackground(darkPanel);
                checkBox.setForeground(darkText);
            } else if (component instanceof JButton) {

                JButton button = (JButton) component;
                if (!button.getText().equals("Apply") && !button.getText().equals("Submit")) {
                    button.setBackground(isDarkMode ? new Color(33, 150, 243) : button.getBackground());
                }

            } else if (component instanceof JTextField || component instanceof JTextArea) {
                JTextComponent textComp = (JTextComponent) component;
                textComp.setBackground(darkPanel);
                textComp.setForeground(darkText);
                textComp.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(darkBorder, 1, true),
                    BorderFactory.createEmptyBorder(4, 8, 4, 8)
                ));
            } else if (component instanceof JDateChooser) {
                JDateChooser dateChooser = (JDateChooser) component;
                dateChooser.setBackground(darkPanel);
                dateChooser.setForeground(darkText);
            } else if (component instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) component;
                scrollPane.setBackground(darkBg);
                scrollPane.setBorder(BorderFactory.createLineBorder(darkBorder));
            } else if (component instanceof Container) {
                applyDarkThemeToComponent((Container) component, darkBg, darkPanel, darkText, darkBorder);
            }
        }
    }

    private void applyLightThemeToComponent(Container container, Color lightBg, Color lightPanel, Color lightText, Color lightBorder) {
        for (Component component : container.getComponents()) {
            if (component instanceof JPanel) {
                JPanel panel = (JPanel) component;
                if (panel.getBackground().equals(new Color(0, 20, 90)) ||
                    panel.getBackground().equals(new Color(0, 4, 80))) {
                    // Keep original dark blue for title bars

                } else if (panel.getBackground().equals(new Color(37, 38, 38))) {
                    panel.setBackground(Color.WHITE);
                } else if (panel.getBackground().equals(new Color(45, 45, 48))) {
                    panel.setBackground(new Color(245, 245, 245));
                }

                if (panel.getBorder() instanceof javax.swing.border.LineBorder) {
                    panel.setBorder(BorderFactory.createLineBorder(lightBorder));
                }

                applyLightThemeToComponent(panel, lightBg, lightPanel, lightText, lightBorder);
            } else if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                if (!label.getForeground().equals(Color.WHITE) &&
                    !label.getForeground().equals(new Color(0, 123, 255))) {
                    label.setForeground(lightText);
                }
            } else if (component instanceof JCheckBox) {
                JCheckBox checkBox = (JCheckBox) component;
                checkBox.setBackground(Color.WHITE);
                checkBox.setForeground(lightText);
            } else if (component instanceof JButton) {
                JButton button = (JButton) component;
                if (button.getText().equals("Get Started")) {
                    button.setForeground(new Color(0, 0, 102));
                    button.setBackground(null);
                } else if (button.getText().equals("Learn More")) {
                    button.setForeground(Color.WHITE);
                    button.setBackground(new Color(0, 4, 80));
                    button.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "",
                        javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                        javax.swing.border.TitledBorder.DEFAULT_POSITION,
                        new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(255, 255, 255)));
                } else if (button.getText().equals("See all")) {
                    button.setBackground(new Color(0, 4, 80));
                    button.setForeground(Color.WHITE);
                } else if (button.getText().equals("Submit Rating")) {
                    button.setBackground(new Color(0, 4, 80));
                    button.setForeground(Color.WHITE);
                } else if (!button.getText().equals("Apply") && !button.getText().equals("Submit")) {
                    button.setBackground(new Color(0, 20, 90));
                    button.setForeground(Color.WHITE);
                }
            } else if (component instanceof JTextField || component instanceof JTextArea) {
                JTextComponent textComp = (JTextComponent) component;
                textComp.setBackground(new Color(245, 245, 245));
                textComp.setForeground(lightText);
                textComp.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true),
                    BorderFactory.createEmptyBorder(4, 8, 4, 8)
                ));
            } else if (component instanceof JDateChooser) {
                JDateChooser dateChooser = (JDateChooser) component;
                dateChooser.setBackground(new Color(245, 245, 245));
                dateChooser.setForeground(lightText);
            } else if (component instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) component;
                scrollPane.setBackground(lightBg);
                scrollPane.setBorder(BorderFactory.createLineBorder(lightBorder));
            } else if (component instanceof Container) {
                applyLightThemeToComponent((Container) component, lightBg, lightPanel, lightText, lightBorder);
            }
        }
    }

    public void showMyAccountPanel() {
        System.out.println("Navigating to My Account");
        JPanel mainPanel = new JPanel(new BorderLayout(8, 8));
        mainPanel.setBackground(isDarkMode ? new Color(25, 25, 25) : new Color(245, 245, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, isDarkMode ? new Color(33, 150, 243) : new Color(0, 4, 80),
                    0, getHeight(), isDarkMode ? new Color(66, 165, 245) : new Color(0, 20, 120));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setPreferredSize(new Dimension(680, 60));
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 12));

        JLabel headerLabel = new JLabel("My Account");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        headerLabel.setForeground(isDarkMode ? Color.WHITE : Color.WHITE);
        headerPanel.add(headerLabel);

        JPanel centerWrapper = new JPanel();
        centerWrapper.setBackground(isDarkMode ? new Color(25, 25, 25) : new Color(245, 245, 245));
        centerWrapper.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(isDarkMode ? new Color(30, 30, 30) : new Color(252, 252, 252));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(isDarkMode ? new Color(50, 50, 50) : new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        formPanel.setPreferredSize(new Dimension(660, 500)); // Increased height to accommodate rating

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        UserData user = userDao.getUserById(userId);
        String usernameText = user != null ? user.getName() : view.username.getText();
        String emailText = user != null ? user.getEmail() : view.email.getText();
        if (user != null) {
            currentEmail = user.getEmail();
        }

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        usernameLabel.setForeground(isDarkMode ? new Color(100, 181, 246) : new Color(0, 0, 102));
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(usernameLabel, gbc);

        JTextField usernameField = new JTextField(18);
        usernameField.setText(usernameText);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        usernameField.setBackground(isDarkMode ? new Color(30, 30, 30) : new Color(245, 245, 245));
        usernameField.setForeground(isDarkMode ? new Color(230, 230, 230) : Color.BLACK);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(isDarkMode ? new Color(50, 50, 50) : new Color(150, 150, 150), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(usernameField, gbc);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        emailLabel.setForeground(isDarkMode ? new Color(100, 181, 246) : new Color(0, 0, 102));
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(emailLabel, gbc);

        JTextField emailField = new JTextField(18);
        emailField.setText(emailText);
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        emailField.setBackground(isDarkMode ? new Color(30, 30, 30) : new Color(245, 245, 245));
        emailField.setForeground(isDarkMode ? new Color(230, 230, 230) : Color.BLACK);
        emailField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(isDarkMode ? new Color(50, 50, 50) : new Color(150, 150, 150), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(emailField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        passwordLabel.setForeground(isDarkMode ? new Color(100, 181, 246) : new Color(0, 0, 102));
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(18);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        passwordField.setBackground(isDarkMode ? new Color(30, 30, 30) : new Color(245, 245, 245));
        passwordField.setForeground(isDarkMode ? new Color(230, 230, 230) : Color.BLACK);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(isDarkMode ? new Color(50, 50, 50) : new Color(150, 150, 150), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(passwordField, gbc);

        JLabel newPasswordLabel = new JLabel("New Password:");
        newPasswordLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        newPasswordLabel.setForeground(isDarkMode ? new Color(100, 181, 246) : new Color(0, 0, 102));
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(newPasswordLabel, gbc);

        JPasswordField newPasswordField = new JPasswordField(18);
        newPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        newPasswordField.setBackground(isDarkMode ? new Color(30, 30, 30) : new Color(245, 245, 245));
        newPasswordField.setForeground(isDarkMode ? new Color(230, 230, 230) : Color.BLACK);
        newPasswordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(isDarkMode ? new Color(50, 50, 50) : new Color(150, 150, 150), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(newPasswordField, gbc);
        // Rating Section
        JLabel ratingLabel = new JLabel("Rate Our App:");
        ratingLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        ratingLabel.setForeground(isDarkMode ? new Color(100, 181, 246) : new Color(0, 4, 80));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        formPanel.add(ratingLabel, gbc);

        ButtonGroup ratingGroup = new ButtonGroup();
        JRadioButton[] stars = new JRadioButton[5];
        final int[] currentHoverIndex = {-1};

        class StarIcon implements Icon {
            private final int width, height;
            private final Color normalColor, highlightColor;
            private final int starIndex;
            private final JRadioButton[] stars;

            public StarIcon(int width, int height, int starIndex, JRadioButton[] stars) {
                this.width = width;
                this.height = height;
                this.normalColor = isDarkMode ? new Color(255, 255, 0) : Color.YELLOW;
                this.highlightColor = isDarkMode ? new Color(255, 255, 102) : new Color(255, 215, 0);
                this.starIndex = starIndex;
                this.stars = stars;
            }

            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                boolean shouldHighlight = false;
                for (int i = 0; i < stars.length; i++) {
                    if (stars[i].isSelected() && starIndex <= i) {
                        shouldHighlight = true;
                        break;
                    }
                }
                if (currentHoverIndex[0] >= 0 && starIndex <= currentHoverIndex[0]) {
                    shouldHighlight = true;
                }

                g2d.setColor(shouldHighlight ? highlightColor : normalColor);

                int[] xPoints = new int[10];
                int[] yPoints = new int[10];
                double outerRadius = width / 2.0;
                double innerRadius = outerRadius / 2.0;
                int centerX = x + width / 2;
                int centerY = y + height / 2;

                for (int i = 0; i < 10; i++) {
                    double angle = Math.toRadians(36 * i + 18);
                    double radius = (i % 2 == 0) ? outerRadius : innerRadius;
                    xPoints[i] = (int) (centerX + radius * Math.cos(angle));
                    yPoints[i] = (int) (centerY - radius * Math.sin(angle));
                }

                g2d.fillPolygon(xPoints, yPoints, 10);
                g2d.dispose();
            }

            @Override
            public int getIconWidth() { return width; }
            @Override
            public int getIconHeight() { return height; }
        }

        JPanel ratingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        ratingPanel.setBackground(isDarkMode ? new Color(30, 30, 30) : new Color(252, 252, 252));
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        formPanel.add(ratingPanel, gbc);

        for (int i = 0; i < 5; i++) {
            stars[i] = new JRadioButton();
            stars[i].setIcon(new StarIcon(20, 20, i, stars));
            stars[i].setRolloverEnabled(true);
            stars[i].setContentAreaFilled(false);
            stars[i].setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
            stars[i].setBackground(isDarkMode ? new Color(30, 30, 30) : new Color(252, 252, 252));
            ratingGroup.add(stars[i]);
            ratingPanel.add(stars[i]);

            final int starIndex = i;
            stars[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    currentHoverIndex[0] = starIndex;
                    for (int j = 0; j < stars.length; j++) stars[j].repaint();
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    currentHoverIndex[0] = -1;
                    for (int j = 0; j < stars.length; j++) stars[j].repaint();
                }
            });
            stars[i].addActionListener(e -> { for (JRadioButton star : stars) star.repaint(); });
        }

        JButton submitRating = new JButton("Submit Rating");
        submitRating.setBackground(isDarkMode ? new Color(33, 150, 243) : new Color(0, 4, 80));
        submitRating.setForeground(Color.WHITE);
        submitRating.setFont(new Font("Segoe UI Semibold", Font.BOLD, 14));
        submitRating.setFocusPainted(false);
        ratingPanel.add(submitRating);

        submitRating.addActionListener(e -> {
            int rating = 0;
            for (int i = 0; i < 5; i++) {
                if (stars[i].isSelected()) {
                    rating = i + 1;
                    break;
                }
            }
            if (rating > 0) {
                ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("+0545"));
                String currentDateTime = zonedDateTime.format(DateTimeFormatter.ofPattern("hh:mm a zzz 'on' EEEE, MMMM dd, yyyy"));
                JOptionPane.showMessageDialog(view, "Thank you for rating us " + rating + " stars!\nSubmitted at " + currentDateTime, "Rating Submitted", JOptionPane.INFORMATION_MESSAGE);
                boolean success = saveRatingToDatabase(userId, rating);
                if (!success) {
                    JOptionPane.showMessageDialog(view, "Failed to save rating. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(view, "Please select a rating.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton saveButton = new JButton("Update") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2d.setColor(isDarkMode ? new Color(66, 165, 245) : new Color(0, 20, 120));
                } else {
                    g2d.setColor(isDarkMode ? new Color(33, 150, 243) : new Color(0, 4, 80));
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
            }
        };
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        saveButton.setForeground(Color.WHITE);
        saveButton.setContentAreaFilled(false);
        saveButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.setFocusPainted(false);
        saveButton.setPreferredSize(new Dimension(160, 40));
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(saveButton, gbc);

        saveButton.addActionListener(e -> {
            String updatedUsername = usernameField.getText().trim();
            String updatedEmail = emailField.getText().trim();
            String passwordText = new String(passwordField.getPassword()).trim();
            String newPasswordText = new String(newPasswordField.getPassword()).trim();

            if (updatedUsername.isEmpty() || updatedEmail.isEmpty() || passwordText.isEmpty() || newPasswordText.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!updatedEmail.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                JOptionPane.showMessageDialog(view, "Invalid email format.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!verifyCurrentPassword(passwordText)) {
                JOptionPane.showMessageDialog(view, "Current password is incorrect.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = updateUserInfo(updatedUsername, updatedEmail, newPasswordText);
            if (success) {
                JOptionPane.showMessageDialog(view, "User info updated successfully!\nUsername: " + updatedUsername, "Success", JOptionPane.INFORMATION_MESSAGE);
                view.setUserInfo(updatedUsername, updatedEmail);
                currentEmail = updatedEmail;
            } else {
                JOptionPane.showMessageDialog(view, "Failed to update user info. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        centerWrapper.add(formPanel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerWrapper, BorderLayout.CENTER);
        updateContentPanel(mainPanel);
    }

    private boolean verifyCurrentPassword(String password) {
        return userDao.verifyPassword(userId, password);
    }

    private boolean updateUserInfo(String username, String email, String newPassword) {
        return userDao.updateUser(userId, username, email, newPassword);
    }
    public void signOut() {
        System.out.println("Signing out");
        view.dispose();
        LoginPageview loginView = new LoginPageview();
        loginView.setVisible(true);
 new LoginController(loginView);
 } public void setUserInfo(String username, String email, int userId) {
 this.userId = userId;
 this.currentEmail = email;
 view.username.setText(username);
 view.email.setText(email);
 } private void handleGetStarted(ActionEvent e) {
 System.out.println("Get Started button clicked");
 JOptionPane.showMessageDialog(view, "Getting started with job search!");
 } private void handleLearnMore(ActionEvent e) {
 System.out.println("Learn More button clicked");
 JOptionPane.showMessageDialog(view, "Learn more about our platform!");
 } private void handleSearch(ActionEvent e) {
 System.out.println("Search button clicked");
 String query = view.Searchfield.getText().trim();
 JOptionPane.showMessageDialog(view, "Searching for: " + query);
 } private void handleSeeAll(ActionEvent e) {
 System.out.println("See All button clicked");
 showVacancyPanel();
 } private void handleFilter(ActionEvent e) {
 System.out.println("Filter button clicked");
 JOptionPane.showMessageDialog(view, "Filter options coming soon!");
 } private boolean saveRatingToDatabase(int userId, int rating) {
 if (rating < 1 || rating > 5) {
     System.err.println("Invalid rating value: " + rating + ". Must be between 1 and 5.");
     return false;
 }

 RatingDao ratingDao = new RatingDao();
 boolean success = ratingDao.saveRating(userId, rating);

 if (success) {
     System.out.println("Rating " + rating + " saved successfully for userId: " + userId);
 } else {
     System.err.println("Failed to save rating for userId: " + userId);
 }

 return success;
 }
    // Set user information
    public void setUserInfo(String username, String email, int userId) {
        this.userId = userId;
        this.currentEmail = email;
        view.username.setText(username);
        view.email.setText(email);
    }

    // Button action handlers
    private void handleGetStarted(ActionEvent e) {
        System.out.println("Get Started button clicked");
        JOptionPane.showMessageDialog(view, "Getting started with job search!");
    }

    private void handleLearnMore(ActionEvent e) {
        System.out.println("Learn More button clicked");
        JOptionPane.showMessageDialog(view, "Learn more about our platform!");
    }

    private void handleSearch(ActionEvent e) {
        System.out.println("Search button clicked");
        String query = view.Searchfield.getText().trim();
        JOptionPane.showMessageDialog(view, "Searching for: " + query);
    }

    private void handleSeeAll(ActionEvent e) {
        System.out.println("See All button clicked");
        showVacancyPanel();
    }

    private void handleFilter(ActionEvent e) {
        System.out.println("Filter button clicked");
                JPanel filterPanel = createFilterPanel();
        int result = JOptionPane.showConfirmDialog(view, filterPanel, "Filter Vacancies",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            applyFilter();
        }
    }

    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Job Type
        JLabel jobTypeLabel = new JLabel("Job Type:");
        jobTypeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(jobTypeLabel, gbc);

        String[] jobTypes = {"Full-Time", "Part-Time", "Contract", "Internship"}; // Adjust based on your data
        JComboBox<String> jobTypeCombo = new JComboBox<>(jobTypes);
        jobTypeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jobTypeCombo.setBackground(new Color(245, 245, 245));
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(jobTypeCombo, gbc);

        // Experience Level
        JLabel expLevelLabel = new JLabel("Experience Level:");
        expLevelLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(expLevelLabel, gbc);

        String[] expLevels = {"Entry", "Mid", "Senior"}; // Adjust based on your data
        JComboBox<String> expLevelCombo = new JComboBox<>(expLevels);
        expLevelCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        expLevelCombo.setBackground(new Color(245, 245, 245));
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(expLevelCombo, gbc);

        // Date Range
        JLabel dateRangeLabel = new JLabel("Date Range:");
        dateRangeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(dateRangeLabel, gbc);

        JDateChooser startDateChooser = new JDateChooser();
        startDateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        startDateChooser.setBackground(new Color(245, 245, 245));
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(startDateChooser, gbc);

        JDateChooser endDateChooser = new JDateChooser();
        endDateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        endDateChooser.setBackground(new Color(245, 245, 245));
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(endDateChooser, gbc);

        // Store filter criteria in the controller (you can use instance variables or a map)
        view.putClientProperty("filterJobType", jobTypeCombo);
        view.putClientProperty("filterExpLevel", expLevelCombo);
        view.putClientProperty("filterStartDate", startDateChooser);
        view.putClientProperty("filterEndDate", endDateChooser);

        return panel;
    }
    

    private boolean saveRatingToDatabase(int userId, int rating) {
        if (rating < 1 || rating > 5) {
            System.err.println("Invalid rating value: " + rating + ". Must be between 1 and 5.");
            return false;
        }

        RatingDao ratingDao = new RatingDao();
        boolean success = ratingDao.saveRating(userId, rating);

        if (success) {
            System.out.println("Rating " + rating + " saved successfully for userId: " + userId);
        } else {
            System.err.println("Failed to save rating for userId: " + userId);
        }

        return success;
    }
}

