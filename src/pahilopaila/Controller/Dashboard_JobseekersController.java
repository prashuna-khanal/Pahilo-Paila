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
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
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

    // Constructor
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
        startNotificationPolling();
        updateNotificationBadge();
        showDashboardPanel();
    }

    // Initialize listeners for UI components
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

    // Helper method to apply theme to individual components
    private void applyThemeToComponent(Component component) {
        if (isDarkMode) {
            Color darkBackground = new Color(45, 45, 48);
            Color darkPanel = new Color(37, 37, 38);
            Color darkText = new Color(220, 220, 220);
            Color darkBorder = new Color(60, 60, 60);
            if (component instanceof JList) {
                component.setBackground(darkPanel);
                component.setForeground(darkText);
            } else if (component instanceof JScrollPane) {
                ((JScrollPane) component).setBackground(darkBackground);
                ((JScrollPane) component).setBorder(BorderFactory.createLineBorder(darkBorder));
            }
        } else {
            Color lightBackground = new Color(245, 245, 245);
            Color lightPanel = new Color(255, 255, 255);
            Color lightText = Color.BLACK;
            Color lightBorder = new Color(200, 200, 200);
            if (component instanceof JList) {
                component.setBackground(lightPanel);
                component.setForeground(lightText);
            } else if (component instanceof JScrollPane) {
                ((JScrollPane) component).setBackground(lightBackground);
                ((JScrollPane) component).setBorder(BorderFactory.createLineBorder(lightBorder));
            }
        }
    }

    // Create a vacancy card for display of vacancies
    private JPanel createVacancyCard(Vacancy vacancy) {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(new Color(0, 4, 80));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setPreferredSize(new Dimension(200, 200));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel titleLabel = new JLabel(vacancy.getJobTitle());
        titleLabel.setFont(new Font("Yu Gothic UI Semibold", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        card.add(titleLabel, gbc);

        JLabel typeLabel = new JLabel(vacancy.getJobType());
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        typeLabel.setForeground(Color.WHITE);
        gbc.gridy = 1;
        card.add(typeLabel, gbc);

        JLabel experienceLabel = new JLabel(vacancy.getExperienceLevel());
        experienceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        experienceLabel.setForeground(Color.WHITE);
        gbc.gridy = 2;
        card.add(experienceLabel, gbc);

        JLabel daysLeftLabel = new JLabel(vacancy.getDaysLeft() + " Days Left");
        daysLeftLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        daysLeftLabel.setForeground(Color.WHITE);
        gbc.gridy = 3;
        card.add(daysLeftLabel, gbc);

        JButton applyButton = new JButton("Apply") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2d.setColor(new Color(0, 20, 120));
                } else {
                    g2d.setColor(new Color(0, 4, 80));
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

    // Navigation methods
    public void showDashboardPanel() {
        System.out.println("Navigating to Dashboard");
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBackground(new java.awt.Color(245, 245, 245));

        JPanel messagePanel = new JPanel();
        messagePanel.setBackground(new java.awt.Color(0, 4, 80));
        messagePanel.setLayout(new javax.swing.GroupLayout(messagePanel));

        JLabel discover = new JLabel("Discover Opportunities That");
        discover.setFont(new java.awt.Font("Segoe UI Symbol", 1, 24));
        discover.setForeground(new java.awt.Color(255, 255, 255));

        JLabel match = new JLabel("Match Your Skill");
        match.setFont(new java.awt.Font("Segoe UI Symbol", 1, 24));
        match.setForeground(new java.awt.Color(255, 255, 255));

        JButton getStarted = new JButton("Get Started");
        getStarted.setForeground(new java.awt.Color(0, 0, 102));
        getStarted.addActionListener(this::handleGetStarted);

        JButton learnMore = new JButton("Learn More");
        learnMore.setForeground(new java.awt.Color(255, 255, 255));
        learnMore.setBackground(new java.awt.Color(0, 4, 80));
        learnMore.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "",
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

        contentPanel.add(messagePanel, BorderLayout.NORTH);

        JPanel featuredPanel = new JPanel(new BorderLayout());
        featuredPanel.setBackground(new Color(245, 245, 245));

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(245, 245, 245));
        JLabel featuredLabel = new JLabel("Featured Jobs");
        featuredLabel.setFont(new Font("Microsoft Himalaya", Font.BOLD, 36));
        headerPanel.add(featuredLabel);

        JButton seeAllButton = new JButton("See all");
        seeAllButton.setBackground(new Color(0, 4, 80));
        seeAllButton.setForeground(Color.WHITE);
        seeAllButton.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        seeAllButton.addActionListener(this::handleSeeAll);
        headerPanel.add(Box.createHorizontalGlue());
        headerPanel.add(seeAllButton);
        featuredPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel vacanciesPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        vacanciesPanel.setBackground(new Color(245, 245, 245));
        JScrollPane scrollPane = new JScrollPane(vacanciesPanel);
        scrollPane.setBorder(null);

        List<Vacancy> allVacancies = vacancyDao.getAllVacancies();
        if (allVacancies.isEmpty()) {
            JLabel noVacanciesLabel = new JLabel("No vacancies available.");
            noVacanciesLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
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
        }

        featuredPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(featuredPanel, BorderLayout.CENTER);

        
        JPanel ratingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        ratingPanel.setBackground(new Color(245, 245, 245));
        ratingPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel ratingLabel = new JLabel("Rate Our App:");
        ratingLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        ratingPanel.add(ratingLabel);

        ButtonGroup ratingGroup = new ButtonGroup();
        JRadioButton[] stars = new JRadioButton[5];
        final int[] currentHoverIndex = {-1};

        class StarIcon implements Icon {
            private final int width;
            private final int height;
            private final Color normalColor;
            private final Color highlightColor;
            private final int starIndex;
            private final JRadioButton[] stars;

            public StarIcon(int width, int height, int starIndex, JRadioButton[] stars) {
                this.width = width;
                this.height = height;
                this.normalColor = Color.YELLOW;
                this.highlightColor = new Color(255, 215, 0);
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
            public int getIconWidth() {
                return width;
            }

            @Override
            public int getIconHeight() {
                return height;
            }
        }

        for (int i = 0; i < 5; i++) {
            stars[i] = new JRadioButton();
            stars[i].setIcon(new StarIcon(20, 20, i, stars));
            stars[i].setRolloverEnabled(true);
            stars[i].setContentAreaFilled(false);
            stars[i].setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
            stars[i].setBackground(new Color(245, 245, 245));
            ratingGroup.add(stars[i]);
            ratingPanel.add(stars[i]);

            final int starIndex = i;
            stars[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    currentHoverIndex[0] = starIndex;
                    for (int j = 0; j < stars.length; j++) {
                        stars[j].repaint();
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    currentHoverIndex[0] = -1;
                    for (int j = 0; j < stars.length; j++) {
                        stars[j].repaint();
                    }
                }
            });

            stars[i].addActionListener(e -> {
                for (JRadioButton star : stars) {
                    star.repaint();
                }
            });
        }

        JButton submitRating = new JButton("Submit Rating");
        submitRating.setBackground(new Color(0, 4, 80));
        submitRating.setForeground(Color.WHITE);
        submitRating.setFont(new Font("Segoe UI Semibold", Font.BOLD, 14));
        submitRating.setFocusPainted(false);
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
        ratingPanel.add(submitRating);

        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));
        centerPanel.setBackground(new Color(245, 245, 245));
        centerPanel.add(featuredPanel, BorderLayout.CENTER);
        centerPanel.add(ratingPanel, BorderLayout.SOUTH);

        contentPanel.add(centerPanel, BorderLayout.CENTER);

        updateContentPanel(contentPanel);
    }

    public void showVacancyPanel() {
        System.out.println("Navigating to Vacancy");
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel headerLabel = new JLabel("Browse Vacancies");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(new Color(0, 4, 80));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        JPanel vacanciesPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        vacanciesPanel.setBackground(new Color(245, 245, 245));
        JScrollPane scrollPane = new JScrollPane(vacanciesPanel);
        scrollPane.setBorder(null);

        List<Vacancy> vacancies = vacancyDao.getAllVacancies();
        if (vacancies.isEmpty()) {
            JLabel noVacanciesLabel = new JLabel("No vacancies available.");
            noVacanciesLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
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

        JLabel headerLabel = new JLabel("CV Upload");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        JPanel formPanel = new JPanel();
        formPanel.setBackground(new Color(252, 252, 252));
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 2, new Color(180, 180, 180, 100)),
            BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20),
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true)
            )
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(firstNameLabel, gbc);

        JTextField firstNameField = new JTextField(15);
        firstNameField.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        firstNameField.setBackground(new Color(245, 245, 245));
        firstNameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(firstNameField, gbc);

        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
        gbc.gridx = 2;
        gbc.gridy = 0;
        formPanel.add(lastNameLabel, gbc);

        JTextField lastNameField = new JTextField(15);
        lastNameField.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lastNameField.setBackground(new Color(245, 245, 245));
        lastNameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        gbc.gridx = 3;
        gbc.gridy = 0;
        formPanel.add(lastNameField, gbc);

        JLabel dobLabel = new JLabel("Date of Birth:");
        dobLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(dobLabel, gbc);

        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        dateChooser.setBackground(new Color(245, 245, 245));
        dateChooser.setPreferredSize(new Dimension(150, 25));
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(dateChooser, gbc);

        JLabel contactLabel = new JLabel("Contact:");
        contactLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
        gbc.gridx = 2;
        gbc.gridy = 1;
        formPanel.add(contactLabel, gbc);

        JTextField contactField = new JTextField(15);
        contactField.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        contactField.setBackground(new Color(245, 245, 245));
        contactField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        gbc.gridx = 3;
        gbc.gridy = 1;
        formPanel.add(contactField, gbc);

        JLabel educationLabel = new JLabel("Education:");
        educationLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(educationLabel, gbc);

        JTextField educationField = new JTextField(15);
        educationField.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        educationField.setBackground(new Color(245, 245, 245));
        educationField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(educationField, gbc);

        JLabel skillsLabel = new JLabel("Skills:");
        skillsLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(skillsLabel, gbc);

        JTextField skillsField = new JTextField(15);
        skillsField.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        skillsField.setBackground(new Color(245, 245, 245));
        skillsField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(skillsField, gbc);

        JLabel experienceLabel = new JLabel("Experience:");
        experienceLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
        gbc.gridx = 2;
        gbc.gridy = 2;
        formPanel.add(experienceLabel, gbc);

        JTextArea experienceArea = new JTextArea(4, 15);
        experienceArea.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        experienceArea.setBackground(new Color(245, 245, 245));
        experienceArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true),
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
                    g2d.setColor(new Color(0, 20, 120));
                } else {
                    g2d.setColor(new Color(0, 4, 80));
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
                ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("+0545"));
                String timestamp = zonedDateTime.format(DateTimeFormatter.ofPattern("hh:mm a zzz 'on' EEEE, MMMM dd, yyyy"));
                String message = "Your CV has been submitted successfully.";
                notificationDao.saveNotification(userId, message, timestamp, false);
                simulateStatusChangeNotifications();
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
        centerWrapper.setBackground(new Color(245, 245, 245));
        centerWrapper.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        centerWrapper.add(formPanel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerWrapper, BorderLayout.CENTER);
        updateContentPanel(mainPanel);
    }

    public void showCVDisplayPanel() {
        System.out.println("Navigating to CV Display");
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(240, 242, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(25, 118, 210), 0, getHeight(), new Color(144, 202, 249));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setPreferredSize(new Dimension(0, 60));
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));

        JLabel headerLabel = new JLabel("My Resume");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
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
                    gbc.gridx = 0;
                    gbc.gridy = row;
                    gbc.gridwidth = 1;
                    formPanel.add(label, gbc);

                    if (i < 6) {
                        JLabel valueLabel = new JLabel(values[i]);
                        valueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                        gbc.gridx = 1;
                        gbc.gridy = row;
                        gbc.gridwidth = 3;
                        formPanel.add(valueLabel, gbc);
                        row++;
                    } else {
                        JTextArea experienceArea = new JTextArea(values[i], 5, 25);
                        experienceArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                        experienceArea.setBackground(new Color(240, 242, 245));
                        experienceArea.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
                        experienceArea.setLineWrap(true);
                        experienceArea.setWrapStyleWord(true);
                        experienceArea.setEditable(false);
                        JScrollPane scrollPane = new JScrollPane(experienceArea);
                        scrollPane.setPreferredSize(new Dimension(400, 120));
                        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
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
        centerWrapper.setBackground(new Color(240, 242, 245));
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
        settingsPanel.setBackground(new Color(245, 245, 245));
        settingsPanel.setLayout(new java.awt.BorderLayout(15, 15));
        settingsPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(0, 20, 90));
        titlePanel.setPreferredSize(new java.awt.Dimension(680, 70));
        titlePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 25, 20));

        JLabel titleLabel = new JLabel("Settings");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);

        JPanel contentBox = new JPanel();
        contentBox.setBackground(Color.WHITE);
        contentBox.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        contentBox.setLayout(new BoxLayout(contentBox, BoxLayout.Y_AXIS));
        contentBox.setPreferredSize(new Dimension(500, 250));
        contentBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        JCheckBox darkModeCheck = new JCheckBox("Dark Mode");
        darkModeCheck.setBackground(Color.WHITE);
        darkModeCheck.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        darkModeCheck.setAlignmentX(Component.LEFT_ALIGNMENT);
        darkModeCheck.setSelected(isDarkMode);

        JPanel darkModePanel = new JPanel();
        darkModePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        darkModePanel.setBackground(Color.WHITE);
        darkModePanel.add(new JLabel("🌙"));
        darkModePanel.add(Box.createHorizontalStrut(10));
        darkModePanel.add(darkModeCheck);

        JCheckBox notificationCheck = new JCheckBox("Enable Notifications");
        notificationCheck.setBackground(Color.WHITE);
        notificationCheck.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        notificationCheck.setSelected(notificationsEnabled);
        notificationCheck.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel notificationPanel = new JPanel();
        notificationPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        notificationPanel.setBackground(Color.WHITE);
        notificationPanel.add(new JLabel("🔔"));
        notificationPanel.add(Box.createHorizontalStrut(10));
        notificationPanel.add(notificationCheck);

        JLabel contactUsLabel = new JLabel("Contact Us: support@pahilopaila.com");
        contactUsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contactUsLabel.setForeground(new Color(0, 123, 255));
        contactUsLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contactUsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel contactPanel = new JPanel();
        contactPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        contactPanel.setBackground(Color.WHITE);
        contactPanel.add(new JLabel("📞"));
        contactPanel.add(Box.createHorizontalStrut(10));
        contactPanel.add(contactUsLabel);

        JButton updateButton = new JButton("Update Settings");
        updateButton.setBackground(new Color(0, 20, 90));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        updateButton.setPreferredSize(new Dimension(150, 35));
        updateButton.setFocusPainted(false);
        updateButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
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
            StringBuilder message = new StringBuilder("Settings Updated Successfully!\n\n");
            message.append("Dark Mode: ").append(isDarkMode ? "Enabled" : "Disabled").append("\n");
            message.append("Notifications: ").append(notificationsEnabled ? "Enabled" : "Disabled");
            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(settingsPanel),
                message.toString(),
                "Settings Updated",
                JOptionPane.INFORMATION_MESSAGE);
        });

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.add(contentBox);

        settingsPanel.add(titlePanel, BorderLayout.NORTH);
        settingsPanel.add(centerPanel, BorderLayout.CENTER);

        updateContentPanel(settingsPanel);
    }

    private void applyDarkModeToSettings(boolean isDarkMode, JPanel settingsPanel) {
        Window parentWindow = SwingUtilities.getWindowAncestor(settingsPanel);

        if (isDarkMode) {
            Color darkBackground = new Color(45, 45, 48);
            Color darkPanel = new Color(37, 37, 38);
            Color darkText = new Color(220, 220, 220);
            Color darkBorder = new Color(60, 60, 60);

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
                    panel.getBackground().equals(new Color(0, 4, 80)) ||
                    (panel.getBorder() != null && panel.getBorder().toString().contains("TitledBorder"))) {
                } else if (panel.getBackground().equals(Color.WHITE)) {
                    panel.setBackground(darkPanel);
                } else if (panel.getBackground().equals(new Color(245, 245, 245)) ||
                           panel.getBackground().equals(new Color(240, 242, 245)) ||
                           panel.getBackground().equals(new Color(252, 252, 252))) {
                    panel.setBackground(darkBg);
                }

                if (panel.getBorder() instanceof javax.swing.border.LineBorder) {
                    panel.setBorder(BorderFactory.createLineBorder(darkBorder));
                }

                applyDarkThemeToComponent(panel, darkBg, darkPanel, darkText, darkBorder);
            } else if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                if (!label.getForeground().equals(Color.WHITE) &&
                    !label.getForeground().equals(new Color(0, 123, 255))) {
                    label.setForeground(darkText);
                }
            } else if (component instanceof JCheckBox) {
                JCheckBox checkBox = (JCheckBox) component;
                checkBox.setBackground(darkPanel);
                checkBox.setForeground(darkText);
            } else if (component instanceof JButton) {
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
                    panel.getBackground().equals(new Color(0, 4, 80)) ||
                    (panel.getBorder() != null && panel.getBorder().toString().contains("TitledBorder"))) {
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
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

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
        headerPanel.setPreferredSize(new Dimension(680, 60));
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 12));

        JLabel headerLabel = new JLabel("My Account");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        JPanel centerWrapper = new JPanel();
        centerWrapper.setBackground(new Color(245, 245, 245));
        centerWrapper.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(252, 252, 252));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        formPanel.setPreferredSize(new Dimension(660, 360));

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
        usernameLabel.setForeground(new Color(0, 0, 102));
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(usernameLabel, gbc);

        JTextField usernameField = new JTextField(18);
        usernameField.setText(usernameText);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        usernameField.setBackground(new Color(245, 245, 245));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(usernameField, gbc);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        emailLabel.setForeground(new Color(0, 0, 102));
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(emailLabel, gbc);

        JTextField emailField = new JTextField(18);
        emailField.setText(emailText);
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        emailField.setBackground(new Color(245, 245, 245));
        emailField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(emailField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        passwordLabel.setForeground(new Color(0, 0, 102));
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(18);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        passwordField.setBackground(new Color(245, 245, 245));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(passwordField, gbc);

        JLabel newPasswordLabel = new JLabel("New Password:");
        newPasswordLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        newPasswordLabel.setForeground(new Color(0, 0, 102));
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(newPasswordLabel, gbc);

        JPasswordField newPasswordField = new JPasswordField(18);
        newPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        newPasswordField.setBackground(new Color(245, 245, 245));
        newPasswordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(newPasswordField, gbc);

        JButton saveButton = new JButton("Update") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2d.setColor(new Color(0, 20, 120));
                } else {
                    g2d.setColor(new Color(0, 4, 80));
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
            }
        };
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        saveButton.setContentAreaFilled(false);
        saveButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.setFocusPainted(false);
        saveButton.setPreferredSize(new Dimension(160, 40));
        gbc.gridx = 1;
        gbc.gridy = 4;
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
        JOptionPane.showMessageDialog(view, "Filter options coming soon!");
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