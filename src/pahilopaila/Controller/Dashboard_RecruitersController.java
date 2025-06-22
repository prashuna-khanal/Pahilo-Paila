package pahilopaila.Controller;

import com.toedter.calendar.JDateChooser;
import pahilopaila.Dao.VacancyDao;
import pahilopaila.Dao.ApplicationDao;
import pahilopaila.Dao.UserDao;
import pahilopaila.Dao.NotificationDao;
import pahilopaila.model.Application;
import pahilopaila.model.Cv;
import pahilopaila.model.UserData;
import pahilopaila.model.Vacancy;
import pahilopaila.model.Notification;
import pahilopaila.view.Dashboard_Recruiters;
import pahilopaila.view.LoginPageview;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Date;
import java.time.temporal.ChronoUnit;
import javax.swing.text.JTextComponent;
import java.util.Timer;
import java.util.TimerTask;

public class Dashboard_RecruitersController {
    private final Dashboard_Recruiters view;
    private final VacancyDao vacancyDao;
    private final ApplicationDao applicationDao;
    private final NotificationDao notificationDao;
    private final int recruiterId;
    private boolean isVacancyPosted = false;
    private final UserDao userDao;
    private int userId;
    private String currentEmail;
    private static boolean notificationsEnabled = true;
    private static boolean isDarkMode = false;
    private int lastNotificationCount = 0;

    public Dashboard_RecruitersController(Dashboard_Recruiters view, int recruiterId) {
        this.view = view;
        this.recruiterId = recruiterId;
        this.vacancyDao = new VacancyDao();
        this.applicationDao = new ApplicationDao();
        this.notificationDao = new NotificationDao();
        this.userId = recruiterId;
        this.userDao = new UserDao();
        initializeListeners();
        applyFeaturePanelTheme();
        view.setController(this);
    }

    private void initializeListeners() {
        view.Searchfield.addActionListener(this::searchFieldActionPerformed);
        view.getStarted.addActionListener(this::getStartedActionPerformed);
        view.learnMore.addActionListener(this::learnMoreActionPerformed);

        view.dashboard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                showDashboardPanel();
            }
        });
        view.vacancy.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                showVacancyPanel();
            }
        });
        view.application.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                showApplicationsPanel();
            }
        });
        view.notifications.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                List<Notification> notifications = loadNotifications();
                view.showNotificationPopup(notifications);
                try {
                    java.lang.reflect.Field pressedField = Dashboard_Recruiters.class.getDeclaredField("notificationsPressed");
                    java.lang.reflect.Field hoveredField = Dashboard_Recruiters.class.getDeclaredField("notificationsHovered");
                    pressedField.setAccessible(true);
                    hoveredField.setAccessible(true);
                    pressedField.setBoolean(view, false);
                    hoveredField.setBoolean(view, false);
                    view.notifications.repaint();
                } catch (Exception e) {
                    System.err.println("Error resetting notification states: " + e.getMessage());
                }
            }
        });
        view.settings.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                showSettingsPanel();
            }
        });
        view.myAccount.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                showMyAccountPanel();
            }
        });
        view.signOut.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                logout();
            }
        });

        view.notificationList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 1) {
                    Notification selected = view.notificationList.getSelectedValue();
                    if (selected != null && !selected.isRead()) {
                        markNotificationAsRead(selected.getId());
                        showNotificationsPanel();
                    }
                }
            }
        });
        view.markAllReadButton.addActionListener(e -> markAllNotificationsAsRead());
    }

    public void open() {
        view.setVisible(true);
        showDashboardPanel();
        loadNotifications();
        checkForNewNotifications();
    }

    public List<Notification> loadNotifications() {
        List<Notification> notifications = notificationDao.getNotificationsByUserId(recruiterId);
        System.out.println("Retrieved " + notifications.size() + " notifications for recruiterId: " + recruiterId);
        int unreadCount = (int) notifications.stream().filter(n -> !n.isRead()).count();
        lastNotificationCount = notifications.size();
        SwingUtilities.invokeLater(() -> {
            view.updateNotifications(notifications);
            view.setUnreadNotificationCount(unreadCount);
        });
        return notifications;
    }

    public void markNotificationAsRead(int notificationId) {
        boolean success = notificationDao.markNotificationRead(notificationId);
        if (success) {
            loadNotifications();
        } else {
            JOptionPane.showMessageDialog(view, "Failed to mark notification as read.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void markAllNotificationsAsRead() {
        notificationDao.markAllNotificationsRead(recruiterId);
        List<Notification> notifications = loadNotifications();
        view.showNotificationPopup(notifications);
        showNotificationsPanel();
        JOptionPane.showMessageDialog(view, "All notifications marked as read.", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void checkForNewNotifications() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                List<Notification> notifications = loadNotifications();
                System.out.println("Polling: Found " + notifications.size() + " notifications, last count: " + lastNotificationCount);
                if (notificationsEnabled && !notifications.isEmpty() && notifications.size() > lastNotificationCount) {
                    System.out.println("New notifications detected: " + notifications.size());
                    lastNotificationCount = notifications.size();
                    SwingUtilities.invokeLater(() -> view.showNotificationPopup(notifications));
                }
            }
        }, 0, 60000);
    }

    private void updateContentPanel(JPanel panel) {
        view.getContentPanel().removeAll();
        view.getContentPanel().setLayout(new BorderLayout());
        view.getContentPanel().add(panel, BorderLayout.CENTER);
        applyThemeToPanel(panel);
        addButtonHoverEffects(panel);
        view.getContentPanel().revalidate();
        view.getContentPanel().repaint();
    }

    private void addButtonHoverEffects(JPanel panel) {
        Component[] components = panel.getComponents();
        applyHoverEffectsRecursively(components);
    }

    private void applyHoverEffectsRecursively(Component[] components) {
        for (Component component : components) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                if (!button.getText().equals("Post Vacancy") && !button.getText().equals("Accept") && 
                    !button.getText().equals("Reject") && !button.getText().equals("Update") &&
                    !button.getText().equals("Mark All as Read")) {
                    button.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            button.setBackground(isDarkMode ? new Color(66, 165, 245) : new Color(0, 20, 120));
                        }
                        @Override
                        public void mouseExited(MouseEvent e) {
                            button.setBackground(isDarkMode ? new Color(33, 150, 243) : new Color(0, 4, 80));
                        }
                    });
                }
            } else if (component instanceof Container) {
                applyHoverEffectsRecursively(((Container) component).getComponents());
            }
        }
    }

    private void applyThemeToPanel(JPanel panel) {
        if (isDarkMode) {
            applyDarkThemeToComponent(panel, new Color(18, 18, 18), new Color(30, 30, 30), 
                                     new Color(230, 230, 230), new Color(50, 50, 50));
            view.notificationPanel.setBackground(new Color(30, 30, 30));
            view.notificationPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(new Color(50, 50, 50), 1, true),
                    "Notifications",
                    TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.TOP,
                    new Font("Segoe UI", Font.BOLD, 14),
                    new Color(230, 230, 230)
                ),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
            view.notificationList.setBackground(new Color(30, 30, 30));
            view.notificationList.setForeground(new Color(230, 230, 230));
            view.markAllReadButton.setBackground(new Color(33, 150, 243));
            view.markAllReadButton.setForeground(Color.WHITE);
        } else {
            applyLightThemeToComponent(panel, Color.WHITE, new Color(245, 245, 245), 
                                       Color.BLACK, Color.LIGHT_GRAY);
            view.notificationPanel.setBackground(new Color(245, 245, 245));
            view.notificationPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true),
                    "Notifications",
                    TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.TOP,
                    new Font("Segoe UI", Font.BOLD, 14),
                    new Color(0, 0, 102)
                ),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
            view.notificationList.setBackground(Color.WHITE);
            view.notificationList.setForeground(Color.BLACK);
            view.markAllReadButton.setBackground(new Color(0, 4, 80));
            view.markAllReadButton.setForeground(Color.WHITE);
        }
        view.notificationList.repaint();
        view.notificationPanel.revalidate();
    }

    private void applyDarkThemeToComponent(Container container, Color darkBg, Color darkPanel, 
                                          Color darkText, Color darkBorder) {
        for (Component component : container.getComponents()) {
            if (component instanceof JPanel) {
                JPanel panel = (JPanel) component;
                if (panel.getBackground().equals(new Color(0, 4, 80)) || 
                    panel.getBackground().equals(new Color(0, 20, 90))) {
                } else {
                    panel.setBackground(darkPanel);
                }
                if (panel.getBorder() instanceof javax.swing.border.LineBorder) {
                    panel.setBorder(BorderFactory.createLineBorder(darkBorder));
                } else if (panel.getBorder() instanceof TitledBorder) {
                    TitledBorder oldBorder = (TitledBorder) panel.getBorder();
                    panel.setBorder(BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(darkBorder, 1, true),
                        oldBorder.getTitle(),
                        TitledBorder.DEFAULT_JUSTIFICATION,
                        TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 14),
                        darkText
                    ));
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
                JButton button = (JButton) component;
                if (!button.getText().equals("Get Started") && !button.getText().equals("Learn More") &&
                    !button.getText().equals("Post Vacancy") && !button.getText().equals("Accept") &&
                    !button.getText().equals("Reject") && !button.getText().equals("Update") &&
                    !button.getText().equals("Mark All as Read")) {
                    button.setBackground(new Color(33, 150, 243));
                    button.setForeground(Color.WHITE);
                }
            } else if (component instanceof JTextField || component instanceof JTextArea) {
                JTextComponent textComp = (JTextComponent) component;
                textComp.setBackground(darkPanel);
                textComp.setForeground(darkText);
                textComp.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(darkBorder, 1, true),
                    BorderFactory.createEmptyBorder(6, 12, 6, 12)
                ));
            } else if (component instanceof JComboBox) {
                JComboBox<?> comboBox = (JComboBox) component;
                comboBox.setBackground(darkPanel);
                comboBox.setForeground(darkText);
            } else if (component instanceof JDateChooser) {
                JDateChooser dateChooser = (JDateChooser) component;
                dateChooser.setBackground(darkPanel);
                dateChooser.setForeground(darkText);
                dateChooser.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(darkBorder, 1, true),
                    BorderFactory.createEmptyBorder(6, 12, 6, 12)
                ));
            } else if (component instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) component;
                scrollPane.setBackground(darkBg);
                scrollPane.setBorder(BorderFactory.createLineBorder(darkBorder));
            } else if (component instanceof JList) {
                JList<?> list = (JList<?>) component;
                list.setBackground(darkPanel);
                list.setForeground(darkText);
            } else if (component instanceof Container) {
                applyDarkThemeToComponent((Container) component, darkBg, darkPanel, darkText, darkBorder);
            }
        }
    }

    private void applyLightThemeToComponent(Container container, Color lightBg, Color lightPanel, 
                                           Color lightText, Color lightBorder) {
        for (Component component : container.getComponents()) {
            if (component instanceof JPanel) {
                JPanel panel = (JPanel) component;
                if (panel.getBackground().equals(new Color(0, 4, 80)) || 
                    panel.getBackground().equals(new Color(0, 20, 90))) {
                } else {
                    panel.setBackground(lightPanel);
                }
                if (panel.getBorder() instanceof javax.swing.border.LineBorder) {
                    panel.setBorder(BorderFactory.createLineBorder(lightBorder));
                } else if (panel.getBorder() instanceof TitledBorder) {
                    TitledBorder oldBorder = (TitledBorder) panel.getBorder();
                    panel.setBorder(BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(lightBorder, 1, true),
                        oldBorder.getTitle(),
                        TitledBorder.DEFAULT_JUSTIFICATION,
                        TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 14),
                        new Color(0, 0, 102)
                    ));
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
                checkBox.setBackground(lightPanel);
                checkBox.setForeground(lightText);
            } else if (component instanceof JButton) {
                JButton button = (JButton) component;
                if (button.getText().equals("Get Started")) {
                    button.setForeground(new Color(0, 0, 102));
                    button.setBackground(null);
                } else if (button.getText().equals("Learn More")) {
                    button.setForeground(Color.WHITE);
                    button.setBackground(new Color(0, 4, 80));
                    button.setBorder(BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.WHITE, 2),
                        "",
                        TitledBorder.DEFAULT_JUSTIFICATION,
                        TitledBorder.DEFAULT_POSITION,
                        new Font("Segoe UI", Font.PLAIN, 11),
                        Color.WHITE
                    ));
                } else if (!button.getText().equals("Post Vacancy") && 
                           !button.getText().equals("Accept") && 
                           !button.getText().equals("Reject") && 
                           !button.getText().equals("Update") &&
                           !button.getText().equals("Mark All as Read")) {
                    button.setBackground(new Color(0, 4, 80));
                    button.setForeground(Color.WHITE);
                }
            } else if (component instanceof JTextField || component instanceof JTextArea) {
                JTextComponent textComp = (JTextComponent) component;
                textComp.setBackground(lightPanel);
                textComp.setForeground(lightText);
                textComp.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true),
                    BorderFactory.createEmptyBorder(6, 12, 6, 12)
                ));
            } else if (component instanceof JComboBox) {
                JComboBox<?> comboBox = (JComboBox) component;
                comboBox.setBackground(lightPanel);
                comboBox.setForeground(lightText);
            } else if (component instanceof JDateChooser) {
                JDateChooser dateChooser = (JDateChooser) component;
                dateChooser.setBackground(lightPanel);
                dateChooser.setForeground(lightText);
                dateChooser.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true),
                    BorderFactory.createEmptyBorder(6, 12, 6, 12)
                ));
            } else if (component instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) component;
                scrollPane.setBackground(lightBg);
                scrollPane.setBorder(BorderFactory.createLineBorder(lightBorder));
            } else if (component instanceof JList) {
                JList<?> list = (JList<?>) component;
                list.setBackground(lightPanel);
                list.setForeground(lightText);
            } else if (component instanceof Container) {
                applyLightThemeToComponent((Container) component, lightBg, lightPanel, lightText, lightBorder);
            }
        }
    }

    private void applyFeaturePanelTheme() {
        view.featurePanel.setBackground(isDarkMode ? new Color(40, 40, 40) : new Color(245, 245, 245));
        Component[] components = view.featurePanel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                label.setForeground(isDarkMode ? new Color(230, 230, 230) : Color.BLACK);
            }
        }
        view.featurePanel.revalidate();
        view.featurePanel.repaint();
    }

    private JPanel createVacancyCard(Vacancy vacancy) {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(isDarkMode ? new Color(0, 4, 80) : new Color(245, 245, 245));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(isDarkMode ? new Color(50, 50, 50) : new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setPreferredSize(new Dimension(200, 200));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel titleLabel = new JLabel("<html>" + vacancy.getJobTitle().replaceAll("\n", "<br>") + "</html>");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(isDarkMode ? Color.WHITE : new Color(0, 4, 80));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        card.add(titleLabel, gbc);

        JButton daysLeftButton = new JButton(vacancy.getDaysLeft() + " days left");
        styleButton(daysLeftButton);
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        card.add(daysLeftButton, gbc);

        JButton jobTypeButton = new JButton(vacancy.getJobType());
        styleButton(jobTypeButton);
        gbc.gridy = 2;
        card.add(jobTypeButton, gbc);

        JButton experienceButton = new JButton(vacancy.getExperienceLevel());
        styleButton(experienceButton);
        gbc.gridy = 3;
        card.add(experienceButton, gbc);

        return card;
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI Semibold", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);

        button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            protected void paintButtonPressed(Graphics g, AbstractButton b) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0, 20, 120));
                g2d.fillRoundRect(0, 0, b.getWidth(), b.getHeight(), 10, 10);
                super.paintButtonPressed(g, b);
            }

            protected void paintBackground(Graphics g, AbstractButton b, Color color) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(b.getModel().isRollover() ? new Color(0, 20, 120) : new Color(0, 4, 80));
                g2d.fillRoundRect(0, 0, b.getWidth(), b.getHeight(), 10, 10);
            }

            @Override
            public void paint(Graphics g, JComponent c) {
                AbstractButton b = (AbstractButton) c;
                paintBackground(g, b, b.getBackground());
                super.paint(g, c);
            }
        });
    }

    public void showDashboardPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(8, 8));
        mainPanel.setBackground(isDarkMode ? new Color(40, 40, 40) : new Color(245, 245, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel messagePanel = new JPanel();
        messagePanel.setBackground(new Color(0, 4, 80));
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        messagePanel.setPreferredSize(new Dimension(680, 140));

        JLabel find = new JLabel("Find the right people");
        find.setFont(new Font("Segoe UI Symbol", Font.BOLD, 22));
        find.setForeground(Color.WHITE);
        find.setAlignmentX(Component.LEFT_ALIGNMENT);
        messagePanel.add(find);

        messagePanel.add(Box.createVerticalStrut(6));

        JLabel right = new JLabel("for the right Job");
        right.setFont(new Font("Segoe UI Symbol", Font.BOLD, 22));
        right.setForeground(Color.WHITE);
        right.setAlignmentX(Component.LEFT_ALIGNMENT);
        messagePanel.add(right);

        messagePanel.add(Box.createVerticalStrut(12));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buttonPanel.setBackground(new Color(0, 4, 80));
        JButton getStarted = new JButton("Get Started");
        getStarted.setFont(new Font("Segoe UI", Font.BOLD, 12));
        getStarted.setForeground(isDarkMode ? Color.WHITE : new Color(0, 0, 102));
        getStarted.setBackground(isDarkMode ? new Color(33, 150, 243) : null);
        getStarted.setPreferredSize(new Dimension(120, 30));
        getStarted.addActionListener(this::getStartedActionPerformed);
        buttonPanel.add(getStarted);

        JButton learnMore = new JButton("Learn More");
        learnMore.setFont(new Font("Segoe UI", Font.BOLD, 12));
        learnMore.setForeground(Color.WHITE);
        learnMore.setBackground(isDarkMode ? new Color(10, 10, 50) : new Color(0, 4, 80));
        learnMore.setBorder(isDarkMode ? BorderFactory.createEmptyBorder() : BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.WHITE, 2),
            "",
            TitledBorder.DEFAULT_JUSTIFICATION,
            TitledBorder.DEFAULT_POSITION,
            new Font("Segoe UI", Font.PLAIN, 11),
            Color.WHITE
        ));
        learnMore.setPreferredSize(new Dimension(120, 30));
        learnMore.addActionListener(this::learnMoreActionPerformed);
        buttonPanel.add(learnMore);

        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        messagePanel.add(buttonPanel);

        JPanel vacanciesPanel = new JPanel(new GridBagLayout());
        vacanciesPanel.setBackground(isDarkMode ? new Color(40, 40, 40) : new Color(245, 245, 245));
        vacanciesPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        GridBagConstraints cardGbc = new GridBagConstraints();
        cardGbc.insets = new Insets(8, 8, 8, 8);
        cardGbc.fill = GridBagConstraints.NONE;
        cardGbc.anchor = GridBagConstraints.NORTHWEST;

        JScrollPane scrollPane = new JScrollPane(vacanciesPanel);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        List<Vacancy> vacancies = vacancyDao.getVacanciesByRecruiterId(recruiterId);
        if (vacancies.isEmpty()) {
            JLabel noVacanciesLabel = new JLabel("No vacancies posted yet.");
            noVacanciesLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            noVacanciesLabel.setForeground(isDarkMode ? new Color(230, 230, 230) : Color.BLACK);
            noVacanciesLabel.setHorizontalAlignment(SwingConstants.CENTER);
            cardGbc.gridx = 0;
            cardGbc.gridy = 0;
            vacanciesPanel.add(noVacanciesLabel, cardGbc);
        } else {
            int gridx = 0;
            int gridy = 0;
            for (Vacancy vacancy : vacancies) {
                JPanel vacancyCard = createVacancyCard(vacancy);
                cardGbc.gridx = gridx;
                cardGbc.gridy = gridy;
                vacanciesPanel.add(vacancyCard, cardGbc);
                gridx++;
                if (gridx > 2) {
                    gridx = 0;
                    gridy++;
                }
            }
        }

        JPanel contentPanel = new JPanel(new BorderLayout(8, 8));
        contentPanel.setBackground(isDarkMode ? new Color(40, 40, 40) : new Color(245, 245, 245));
        contentPanel.add(messagePanel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        updateContentPanel(mainPanel);
    }

    public void showVacancyPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(12, 12));
        mainPanel.setBackground(isDarkMode ? new Color(40, 40, 40) : new Color(245, 245, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

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

        JLabel headerLabel = new JLabel("Post New Vacancy");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(isDarkMode ? new Color(30, 30, 30) : new Color(252, 252, 252));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(isDarkMode ? new Color(50, 50, 50) : new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        formPanel.setPreferredSize(new Dimension(660, 360));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 20, 12, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JPanel jobTitleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        jobTitleRow.setBackground(isDarkMode ? new Color(30, 30, 30) : new Color(252, 252, 252));
        JLabel jobTitleIcon = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/Image/logo/job.png"));
            Image scaledImage = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            jobTitleIcon.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            System.out.println("Error loading job title icon: " + e.getMessage());
        }
        jobTitleRow.add(jobTitleIcon);
        JLabel jobTitleLabel = new JLabel("Job Title:");
        jobTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        jobTitleLabel.setForeground(isDarkMode ? new Color(100, 181, 246) : new Color(0, 0, 102));
        jobTitleRow.add(jobTitleLabel);
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(jobTitleRow, gbc);

        JTextField jobTitleField = new JTextField(25);
        jobTitleField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jobTitleField.setBackground(isDarkMode ? new Color(30, 30, 30) : new Color(245, 245, 245));
        jobTitleField.setForeground(isDarkMode ? new Color(230, 230, 230) : Color.BLACK);
        jobTitleField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(isDarkMode ? new Color(50, 50, 50) : new Color(150, 150, 150), 1, true),
            BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(jobTitleField, gbc);

        JLabel jobTypeLabel = new JLabel("Job Type:");
        jobTypeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        jobTypeLabel.setForeground(isDarkMode ? new Color(100, 181, 246) : new Color(0, 0, 102));
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(jobTypeLabel, gbc);

        JComboBox<String> jobTypeCombo = new JComboBox<>(new String[]{"Full time", "Part time", "Contract"});
        jobTypeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jobTypeCombo.setBackground(isDarkMode ? new Color(30, 30, 30) : new Color(245, 245, 245));
        jobTypeCombo.setForeground(isDarkMode ? new Color(230, 230, 230) : Color.BLACK);
        jobTypeCombo.setPreferredSize(new Dimension(280, 35));
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(jobTypeCombo, gbc);

        JLabel experienceLabel = new JLabel("Experience Level:");
        experienceLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        experienceLabel.setForeground(isDarkMode ? new Color(100, 181, 246) : new Color(0, 0, 102));
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(experienceLabel, gbc);

        JComboBox<String> experienceCombo = new JComboBox<>(new String[]{"Junior-Level", "Mid-Level", "Senior-Level"});
        experienceCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        experienceCombo.setBackground(isDarkMode ? new Color(30, 30, 30) : new Color(245, 245, 245));
        experienceCombo.setForeground(isDarkMode ? new Color(230, 230, 230) : Color.BLACK);
        experienceCombo.setPreferredSize(new Dimension(280, 35));
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(experienceCombo, gbc);

        JLabel deadlineLabel = new JLabel("Deadline Date:");
        deadlineLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        deadlineLabel.setForeground(isDarkMode ? new Color(100, 181, 246) : new Color(0, 0, 102));
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(deadlineLabel, gbc);

        JDateChooser deadlineDateChooser = new JDateChooser();
        deadlineDateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        deadlineDateChooser.setBackground(isDarkMode ? new Color(30, 30, 30) : new Color(245, 245, 245));
        deadlineDateChooser.setForeground(isDarkMode ? new Color(230, 230, 230) : Color.BLACK);
        deadlineDateChooser.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(isDarkMode ? new Color(50, 50, 50) : new Color(150, 150, 150), 1, true),
            BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));
        deadlineDateChooser.setDateFormatString("yyyy-MM-dd");
        deadlineDateChooser.setPreferredSize(new Dimension(280, 35));
        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(deadlineDateChooser, gbc);

        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        descriptionLabel.setForeground(isDarkMode ? new Color(100, 181, 246) : new Color(0, 0, 102));
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(descriptionLabel, gbc);

        JTextArea descriptionArea = new JTextArea(6, 25);
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descriptionArea.setBackground(isDarkMode ? new Color(30, 30, 30) : new Color(245, 245, 245));
        descriptionArea.setForeground(isDarkMode ? new Color(230, 230, 230) : Color.BLACK);
        descriptionArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(isDarkMode ? new Color(50, 50, 50) : new Color(150, 150, 150), 1, true),
            BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);
        descriptionScroll.setPreferredSize(new Dimension(300, 120));
        gbc.gridx = 1;
        gbc.gridy = 4;
        formPanel.add(descriptionScroll, gbc);

        JLabel statusLabel = new JLabel("");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(isDarkMode ? new Color(230, 230, 230) : Color.BLACK);
        gbc.gridx = 1;
        gbc.gridy = 5;
        formPanel.add(statusLabel, gbc);

        JButton postButton = new JButton("Post Vacancy") {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
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
        postButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        postButton.setForeground(Color.WHITE);
        postButton.setContentAreaFilled(false);
        postButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        postButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        postButton.setFocusPainted(false);
        postButton.setPreferredSize(new Dimension(200, 50));
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(postButton, gbc);

        postButton.addActionListener(e -> {
            String jobTitle = jobTitleField.getText().trim();
            String jobType = (String) jobTypeCombo.getSelectedItem();
            String experienceLevel = (String) experienceCombo.getSelectedItem();
            Date deadlineDate = deadlineDateChooser.getDate();
            String description = descriptionArea.getText().trim();

            if (jobTitle.isEmpty() || jobType == null || experienceLevel == null || deadlineDate == null) {
                statusLabel.setText("Please fill in all required fields.");
                statusLabel.setForeground(Color.RED);
                return;
            }

            LocalDate currentDate = LocalDate.now();
            LocalDate selectedDate = deadlineDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            long daysLeft = ChronoUnit.DAYS.between(currentDate, selectedDate);
            if (daysLeft <= 0) {
                statusLabel.setText("Deadline must be a future date.");
                statusLabel.setForeground(Color.RED);
                return;
            }

            Vacancy vacancy = new Vacancy();
            vacancy.setRecruiterId(recruiterId);
            vacancy.setJobTitle(jobTitle);
            vacancy.setJobType(jobType);
            vacancy.setExperienceLevel(experienceLevel);
            vacancy.setDaysLeft((int) daysLeft);
            vacancy.setDescription(description);

            boolean success = vacancyDao.saveVacancy(vacancy);
            if (success) {
                isVacancyPosted = true;
                statusLabel.setText("Vacancy posted successfully!");
                statusLabel.setForeground(Color.GREEN);
                jobTitleField.setText("");
                jobTypeCombo.setSelectedIndex(0);
                experienceCombo.setSelectedIndex(0);
                deadlineDateChooser.setDate(null);
                descriptionArea.setText("");
                showDashboardPanel();
            } else {
                statusLabel.setText("Failed to post vacancy.");
                statusLabel.setForeground(Color.RED);
            }
        });

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        updateContentPanel(mainPanel);
    }

    public void showApplicationsPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(12, 12));
        mainPanel.setBackground(isDarkMode ? new Color(40, 40, 40) : new Color(245, 245, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

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

        JLabel headerLabel = new JLabel("Applications");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        JPanel applicationsPanel = new JPanel(new GridBagLayout());
        applicationsPanel.setBackground(isDarkMode ? new Color(40, 40, 40) : new Color(245, 245, 245));
        JScrollPane scrollPane = new JScrollPane(applicationsPanel);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        List<Application> applications = applicationDao.getApplicationsByRecruiterId(recruiterId);
        if (applications.isEmpty()) {
            JLabel noApplicationsLabel = new JLabel("No applications received yet.");
            noApplicationsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            noApplicationsLabel.setForeground(isDarkMode ? new Color(230, 230, 230) : Color.BLACK);
            noApplicationsLabel.setHorizontalAlignment(SwingConstants.CENTER);
            gbc.gridx = 0;
            gbc.gridy = 0;
            applicationsPanel.add(noApplicationsLabel, gbc);
        } else {
            int gridy = 0;
            for (Application app : applications) {
                JPanel appCard = createApplicationCard(app);
                gbc.gridx = 0;
                gbc.gridy = gridy;
                applicationsPanel.add(appCard, gbc);
                gridy++;
            }
        }

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        updateContentPanel(mainPanel);
    }

    private JPanel createApplicationCard(Application app) {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(isDarkMode ? new Color(30, 30, 30) : new Color(245, 245, 245));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(isDarkMode ? new Color(50, 50, 50) : new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setPreferredSize(new Dimension(600, 250));
        System.out.println("Creating card for application: jobSeekerId=" + app.getJobSeekerId() + ", recruiterId=" + recruiterId);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel nameLabel = new JLabel("Applicant: " + app.getJobSeekerName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLabel.setForeground(isDarkMode ? new Color(100, 181, 246) : new Color(0, 0, 102));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        card.add(nameLabel, gbc);

        JLabel emailLabel = new JLabel("Email: " + app.getJobSeekerEmail());
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailLabel.setForeground(isDarkMode ? new Color(230, 230, 230) : Color.BLACK);
        gbc.gridy = 1;
        card.add(emailLabel, gbc);

        Cv cv = app.getCv();
        JLabel cvDetailsLabel = new JLabel("<html><b>CV Details:</b><br>" +
            "First Name: " + cv.getFirstName() + "<br>" +
            "Last Name: " + cv.getLastName() + "<br>" +
            "Date of Birth: " + cv.getDob() + "<br>" +
            "Contact: " + cv.getContact() + "<br>" +
            "Education: " + cv.getEducation() + "<br>" +
            "Skills: " + cv.getSkills() + "<br>" +
            "Experience: " + cv.getExperience() + "</html>");
        cvDetailsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cvDetailsLabel.setForeground(isDarkMode ? new Color(230, 230, 230) : Color.BLACK);
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        card.add(cvDetailsLabel, gbc);

        JLabel statusLabel = new JLabel("Status: " + app.getStatus());
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(isDarkMode ? new Color(230, 230, 230) : Color.BLACK);
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        card.add(statusLabel, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(isDarkMode ? new Color(30, 30, 30) : new Color(245, 245, 245));

        JButton acceptButton = new JButton("Accept") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2d.setColor(new Color(0, 100, 0));
                } else {
                    g2d.setColor(new Color(0, 128, 0));
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        acceptButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        acceptButton.setForeground(Color.WHITE);
        acceptButton.setContentAreaFilled(false);
        acceptButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        acceptButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        acceptButton.setFocusPainted(false);
        acceptButton.setEnabled(!app.getStatus().equals("Accepted") && !app.getStatus().equals("Rejected"));

        JButton rejectButton = new JButton("Reject") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2d.setColor(new Color(139, 0, 0));
                } else {
                    g2d.setColor(new Color(178, 34, 34));
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        rejectButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        rejectButton.setForeground(Color.WHITE);
        rejectButton.setContentAreaFilled(false);
        rejectButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        rejectButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rejectButton.setFocusPainted(false);
        rejectButton.setEnabled(!app.getStatus().equals("Accepted") && !app.getStatus().equals("Rejected"));

        acceptButton.addActionListener(e -> {
            boolean success = applicationDao.updateApplicationStatus(app.getId(), "Accepted");
            if (success) {
                statusLabel.setText("Status: Accepted");
                acceptButton.setEnabled(false);
                rejectButton.setEnabled(false);
                JOptionPane.showMessageDialog(view, "Application accepted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                System.out.println("Notifications enabled: " + notificationsEnabled);
                if (notificationsEnabled) {
                    String message = "Your application for " + app.getVacancyTitle() + " has been accepted.";
                    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    boolean jobSeekerSaved = notificationDao.saveNotification(app.getJobSeekerId(), message, timestamp, true);
                    String recruiterMessage = "You accepted an application for " + app.getVacancyTitle() + " from " + app.getJobSeekerName();
                    boolean recruiterSaved = notificationDao.saveNotification(recruiterId, recruiterMessage, timestamp, true);
                    System.out.println("JobSeeker notification saved: " + jobSeekerSaved);
                    System.out.println("Recruiter notification saved: " + recruiterSaved);
                    loadNotifications();
                }
            } else {
                JOptionPane.showMessageDialog(view, "Failed to update application status.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        rejectButton.addActionListener(e -> {
            boolean success = applicationDao.updateApplicationStatus(app.getId(), "Rejected");
            if (success) {
                statusLabel.setText("Status: Rejected");
                acceptButton.setEnabled(false);
                rejectButton.setEnabled(false);
                JOptionPane.showMessageDialog(view, "Application rejected successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                System.out.println("Notifications enabled: " + notificationsEnabled);
                if (notificationsEnabled) {
                    String message = "Your application for " + app.getVacancyTitle() + " has been rejected.";
                    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    boolean jobSeekerSaved = notificationDao.saveNotification(app.getJobSeekerId(), message, timestamp, true);
                    String recruiterMessage = "You rejected an application for " + app.getVacancyTitle() + " from " + app.getJobSeekerName();
                    boolean recruiterSaved = notificationDao.saveNotification(recruiterId, recruiterMessage, timestamp, true);
                    System.out.println("JobSeeker notification saved: " + jobSeekerSaved);
                    System.out.println("Recruiter notification saved: " + recruiterSaved);
                    loadNotifications();
                }
            } else {
                JOptionPane.showMessageDialog(view, "Failed to update application status.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(acceptButton);
        buttonPanel.add(rejectButton);
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        card.add(buttonPanel, gbc);

        return card;
    }

    public void showNotificationsPanel() {
        List<Notification> notifications = loadNotifications();

        JPanel mainPanel = new JPanel(new BorderLayout(12, 12));
        mainPanel.setBackground(isDarkMode ? new Color(40, 40, 40) : new Color(245, 245, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

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

        JLabel headerLabel = new JLabel("Notifications");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(view.notificationPanel, BorderLayout.CENTER);

        updateContentPanel(mainPanel);
    }

    public void showSettingsPanel() {
        System.out.println("Navigating to Settings");
        JPanel settingsPanel = new JPanel();
        settingsPanel.setBackground(isDarkMode ? new Color(40, 40, 40) : new Color(245, 245, 245));
        settingsPanel.setLayout(new BorderLayout(15, 15));
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(isDarkMode ? new Color(0, 4, 80) : new Color(0, 20, 90));
        titlePanel.setPreferredSize(new Dimension(680, 70));
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 25, 20));

        JLabel titleLabel = new JLabel("Settings");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
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
        darkModeCheck.setSelected(isDarkMode);
        darkModeCheck.setAlignmentX(Component.LEFT_ALIGNMENT);

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
            view.toggleDarkMode(isDarkMode);
            applyDarkModeToSettings(isDarkMode, settingsPanel);
            applyFeaturePanelTheme();
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

        contactUsLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
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
            public void mouseEntered(MouseEvent e) {
                contactUsLabel.setText("<html><u>Contact Us: support@pahilopaila.com</u></html>");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                contactUsLabel.setText("Contact Us: support@pahilopaila.com");
            }
        });

        updateButton.addActionListener(e -> {
            isDarkMode = darkModeCheck.isSelected();
            notificationsEnabled = notificationCheck.isSelected();
            view.toggleDarkMode(isDarkMode);
            applyDarkModeToSettings(isDarkMode, settingsPanel);
            applyFeaturePanelTheme();
            StringBuilder message = new StringBuilder("Settings Updated Successfully!\n\n");
            message.append("Dark Mode: ").append(isDarkMode ? "Enabled" : "Disabled").append("\n");
            message.append("Notifications: ").append(notificationsEnabled ? "Enabled" : "Disabled");
            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(settingsPanel),
                message.toString(),
                "Settings Updated",
                JOptionPane.INFORMATION_MESSAGE);
        });

        settingsPanel.add(titlePanel, BorderLayout.NORTH);
        settingsPanel.add(contentBox, BorderLayout.CENTER);

        updateContentPanel(settingsPanel);
    }

    private void applyDarkModeToSettings(boolean isDarkMode, JPanel settingsPanel) {
        view.toggleDarkMode(isDarkMode);
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
    public void showMyAccountPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(8, 8));
        mainPanel.setBackground(isDarkMode ? new Color(40, 40, 40) : new Color(245, 245, 245));
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
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        JPanel centerWrapper = new JPanel();
        centerWrapper.setBackground(isDarkMode ? new Color(40, 40, 40) : new Color(245, 245, 245));
        centerWrapper.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(isDarkMode ? new Color(30, 30, 30) : new Color(252, 252, 252));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(isDarkMode ? new Color(50, 50, 50) : new Color(200, 200, 200), 1, true),
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

    public void setUserInfo(String username, String email, int userId) {
        this.userId = userId;
        this.currentEmail = email;
        view.setUserInfo(username, email);
    }

    public void logout() {
        view.dispose();
        LoginPageview loginView = new LoginPageview();
        LoginController loginController = new LoginController(loginView);
        loginController.open();
    }

    public void searchFieldActionPerformed(ActionEvent e) {
        System.out.println("Search: " + view.Searchfield.getText());
    }

    public void getStartedActionPerformed(ActionEvent e) {
        System.out.println("Get Started clicked");
    }

    public void learnMoreActionPerformed(ActionEvent e) {
        System.out.println("Learn More clicked");
    }      

    public void setUserInfo(String username, String email) {
        view.setUserInfo(username, email);
    }
}