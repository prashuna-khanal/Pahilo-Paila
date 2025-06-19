package pahilopaila.Controller;

import pahilopaila.Dao.VacancyDao;
import pahilopaila.Dao.UserDao;
import pahilopaila.model.UserData;
import pahilopaila.model.Vacancy;
import pahilopaila.view.Dashboard_Recruiters;
import pahilopaila.view.LoginPageview;
import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Dashboard_RecruitersController {
    private final Dashboard_Recruiters view;
    private final VacancyDao vacancyDao;
    private final int recruiterId;
    private boolean isVacancyPosted = false;
    private final UserDao userDao;
    private int userId;
    private String currentEmail;

    public Dashboard_RecruitersController(Dashboard_Recruiters view, int recruiterId) {
        this.view = view;
        this.recruiterId = recruiterId;
        this.vacancyDao = new VacancyDao();
        this.userId=recruiterId;
        this.userDao = new UserDao();
        initializeListeners();
        showDashboardPanel();
    }

    private void initializeListeners() {
        view.Searchfield.addActionListener(this::searchFieldActionPerformed);
        view.jButton1.addActionListener(this::searchButtonActionPerformed);
        view.jButton2.addActionListener(this::filterButtonActionPerformed);
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
    }

    private void updateContentPanel(JPanel panel) {
        view.getContentPanel().removeAll();
        view.getContentPanel().setLayout(new BorderLayout());
        view.getContentPanel().add(panel, BorderLayout.CENTER);
        view.getContentPanel().revalidate();
        view.getContentPanel().repaint();
    }

    private JPanel createVacancyCard(Vacancy vacancy) {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(new Color(0, 10, 100));
        card.setPreferredSize(new Dimension(220, 190));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel titleLabel = new JLabel("<html>" + vacancy.getJobTitle().replaceAll("\n", "<br>") + "</html>");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(Color.WHITE);
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
        button.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        button.setForeground(new Color(0, 10, 100));
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        button.setPreferredSize(new Dimension(140, 30));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public void showDashboardPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(8, 8));
        mainPanel.setBackground(new Color(245, 245, 245));
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
        getStarted.setForeground(new Color(0, 0, 102));
        getStarted.setPreferredSize(new Dimension(120, 30));
        getStarted.addActionListener(this::getStartedActionPerformed);
        buttonPanel.add(getStarted);

        JButton learnMore = new JButton("Learn More");
        learnMore.setFont(new Font("Segoe UI", Font.BOLD, 12));
        learnMore.setForeground(Color.WHITE);
        learnMore.setBackground(new Color(0, 4, 80));
        learnMore.setBorder(BorderFactory.createTitledBorder(
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
        vacanciesPanel.setBackground(new Color(245, 245, 245));
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
        contentPanel.setBackground(new Color(245, 245, 245));
        contentPanel.add(messagePanel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        updateContentPanel(mainPanel);
    }

    public void showVacancyPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(12, 12)); // Increased from 8
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12)); // Increased from 8

        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(0, 4, 80), 0, getHeight(), new Color(0, 20, 120));
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
        formPanel.setBackground(new Color(252, 252, 252));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                "Vacancy Details",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14)
            ),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        formPanel.setPreferredSize(new Dimension(660, 360)); // Increased from 320

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 20, 12, 20); // Increased from 8,12
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JPanel jobTitleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        jobTitleRow.setBackground(new Color(252, 252, 252));
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
        jobTitleLabel.setForeground(new Color(0, 0, 102));
        jobTitleRow.add(jobTitleLabel);
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(jobTitleRow, gbc);

        JTextField jobTitleField = new JTextField(25); // Increased from 22
        jobTitleField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jobTitleField.setBackground(new Color(245, 245, 245));
        jobTitleField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true),
            BorderFactory.createEmptyBorder(6, 12, 6, 12) // Increased padding
        ));
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(jobTitleField, gbc);

        JLabel jobTypeLabel = new JLabel("Job Type:");
        jobTypeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        jobTypeLabel.setForeground(new Color(0, 0, 102));
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(jobTypeLabel, gbc);

        JComboBox<String> jobTypeCombo = new JComboBox<>(new String[]{"Full time", "Part time", "Contract"});
        jobTypeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jobTypeCombo.setBackground(new Color(245, 245, 245));
        jobTypeCombo.setPreferredSize(new Dimension(280, 35)); // Increased from 260x30
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(jobTypeCombo, gbc);

        JLabel experienceLabel = new JLabel("Experience Level:");
        experienceLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        experienceLabel.setForeground(new Color(0, 0, 102));
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(experienceLabel, gbc);

        JComboBox<String> experienceCombo = new JComboBox<>(new String[]{"Junior-Level", "Mid-Level", "Senior-Level"});
        experienceCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        experienceCombo.setBackground(new Color(245, 245, 245));
        experienceCombo.setPreferredSize(new Dimension(280, 35)); // Increased from 180x30
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(experienceCombo, gbc);
        experienceCombo.revalidate();
        experienceCombo.repaint();

        JLabel deadlineLabel = new JLabel("Deadline Date:");
        deadlineLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        deadlineLabel.setForeground(new Color(0, 0, 102));
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(deadlineLabel, gbc);

        JDateChooser deadlineDateChooser = new JDateChooser();
        deadlineDateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        deadlineDateChooser.setBackground(new Color(245, 245, 245));
        deadlineDateChooser.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true),
            BorderFactory.createEmptyBorder(6, 12, 6, 12) // Increased padding
        ));
        deadlineDateChooser.setDateFormatString("yyyy-MM-dd");
        deadlineDateChooser.setPreferredSize(new Dimension(280, 35)); // Increased from 260x30
        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(deadlineDateChooser, gbc);

        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        descriptionLabel.setForeground(new Color(0, 0, 102));
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(descriptionLabel, gbc);

        JTextArea descriptionArea = new JTextArea(6, 25); // Increased from 4,22
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descriptionArea.setBackground(new Color(245, 245, 245));
        descriptionArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true),
            BorderFactory.createEmptyBorder(6, 12, 6, 12) // Increased padding
        ));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);
        descriptionScroll.setPreferredSize(new Dimension(300, 120)); // Increased from 260x80
        gbc.gridx = 1;
        gbc.gridy = 4;
        formPanel.add(descriptionScroll, gbc);

        JLabel statusLabel = new JLabel("");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
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
                    g2d.setColor(new Color(0, 20, 120));
                } else {
                    g2d.setColor(new Color(0, 4, 80));
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10); // Increased rounding
                super.paintComponent(g);
            }
        };
        postButton.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Increased from 12
        postButton.setForeground(Color.WHITE);
        postButton.setContentAreaFilled(false);
        postButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Increased padding
        postButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        postButton.setFocusPainted(false);
        postButton.setPreferredSize(new Dimension(200, 50)); // Increased from 160x40
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
        JPanel panel = new JPanel();
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JLabel label = new JLabel("Applications Panel");
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label);
        panel.setPreferredSize(new Dimension(680, 320));
        updateContentPanel(panel);
    }

    public void showSettingsPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JLabel label = new JLabel("Settings Panel");
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label);
        panel.setPreferredSize(new Dimension(680, 320));
        updateContentPanel(panel);

    }

    
     public void showMyAccountPanel() {
        System.out.println("Navigating to My Account");
        JPanel mainPanel = new JPanel(new BorderLayout(8, 8));
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // Header Panel with Gradient
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

        // Center Wrapper for Form
        JPanel centerWrapper = new JPanel();
        centerWrapper.setBackground(new Color(245, 245, 245));
        centerWrapper.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        // Form Panel
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

        // Fetch user data from database
        UserData user = userDao.getUserById(userId);
        String usernameText = user != null ? user.getName() : view.username.getText();
        String emailText = user != null ? user.getEmail() : view.email.getText();
        // Ensure currentEmail is set correctly
        if (user != null) {
            currentEmail = user.getEmail();
        }

        // Username
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

        // Email
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

        // Password
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

        // New Password
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

        // Update Button
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

        // Button Action
        saveButton.addActionListener(e -> {
            String updatedUsername = usernameField.getText().trim();
            String updatedEmail = emailField.getText().trim();
            String passwordText = new String(passwordField.getPassword()).trim();
            String newPasswordText = new String(newPasswordField.getPassword()).trim();

            // Validate inputs
            if (updatedUsername.isEmpty() || updatedEmail.isEmpty() || passwordText.isEmpty() || newPasswordText.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate email format
            if (!updatedEmail.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                JOptionPane.showMessageDialog(view, "Invalid email format.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verify current password
            if (!verifyCurrentPassword(passwordText)) {
                JOptionPane.showMessageDialog(view, "Current password is incorrect.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Update user info in database
            boolean success = updateUserInfo(updatedUsername, updatedEmail, newPasswordText);
            if (success) {
                JOptionPane.showMessageDialog(view, "User info updated successfully!\nUsername: " + updatedUsername, "Success", JOptionPane.INFORMATION_MESSAGE);
                view.setUserInfo(updatedUsername, updatedEmail); // Update view
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

    // Verify current password using UserDao
    private boolean verifyCurrentPassword(String password) {
        return userDao.verifyPassword(userId, password);
    }

    // Update user info using UserDao
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

    public void searchButtonActionPerformed(ActionEvent e) {
        System.out.println("Search button clicked");
    }

    public void filterButtonActionPerformed(ActionEvent e) {
        System.out.println("Filter button clicked");
    }

    public void getStartedActionPerformed(ActionEvent e) {
        System.out.println("Get Started clicked");
    }

    public void learnMoreActionPerformed(ActionEvent e) {
        System.out.println("Learn More clicked");
    }

    public void open() {
        view.setVisible(true);
    }

    public void setUserInfo(String username, String email) {
        view.setUserInfo(username, email);
    }
}