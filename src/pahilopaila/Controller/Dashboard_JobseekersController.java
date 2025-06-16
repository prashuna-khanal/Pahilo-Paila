package pahilopaila.Controller;

import com.toedter.calendar.JDateChooser;
import pahilopaila.view.Dashboard_JobSeekers;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Controller for the Dashboard_JobSeekers view, handling user interactions and navigation.
 */
public class Dashboard_JobseekersController {
    private final Dashboard_JobSeekers view;

    // Constructor to accept the view
    public Dashboard_JobseekersController(Dashboard_JobSeekers view) {
        this.view = view;
        initializeListeners();
    }

    // Initialize listeners for UI components
    private void initializeListeners() {
        // Navigation menu listeners
        view.dashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showDashboardPanel();
            }
        });

        view.vacancy.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showVacancyPanel();
            }
        });

        view.CV.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showCVUploadPanel();
            }
        });

        view.settings.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showSettingsPanel();
            }
        });

        view.myAccount.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showMyAccountPanel(); // Call the new method in the controller
            }
        });

        view.signOut.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                signOut();
            }
        });

        // Button listeners
        view.getStarted.addActionListener((ActionEvent e) -> {
            handleGetStarted();
        });

        view.learnMore.addActionListener((ActionEvent e) -> {
            handleLearnMore();
        });

        view.search.addActionListener((ActionEvent e) -> {
            handleSearch();
        });

        view.see_all.addActionListener((ActionEvent e) -> {
            handleSeeAll();
        });

        view.filter.addActionListener((ActionEvent e) -> {
            handleFilter();
        });
    }

    // Navigation methods
    public void showDashboardPanel() {
        System.out.println("Navigating to Dashboard");
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new java.awt.Color(245, 245, 245));
        contentPanel.setLayout(new java.awt.BorderLayout());

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
        getStarted.addActionListener(e -> handleGetStarted());

        JButton learnMore = new JButton("Learn More");
        learnMore.setForeground(new java.awt.Color(255, 255, 255));
        learnMore.setBackground(new java.awt.Color(0, 4, 80));
        learnMore.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(255, 255, 255)));
        learnMore.addActionListener(e -> handleLearnMore());

        // Layout for messagePanel
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

        contentPanel.add(messagePanel, java.awt.BorderLayout.CENTER);
        updateContentPanel(contentPanel);
    }

    public void showVacancyPanel() {
        System.out.println("Navigating to Vacancy");
        JPanel vacancyPanel = new JPanel();
        vacancyPanel.setBackground(new java.awt.Color(245, 245, 245));
        vacancyPanel.setLayout(new java.awt.BorderLayout());
        JLabel title = new JLabel("Browse Vacancies");
        title.setFont(new java.awt.Font("Segoe UI", 1, 18));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        vacancyPanel.add(title, java.awt.BorderLayout.NORTH);
        updateContentPanel(vacancyPanel);
    }

    public void showCVUploadPanel() {
    System.out.println("Navigating to CV Upload");
    JPanel mainPanel = new JPanel();
    mainPanel.setBackground(new Color(245, 245, 245));
    mainPanel.setLayout(new BorderLayout(15, 15));
    mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

    // Header panel
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

    // Form panel
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

    // First Name
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

    // Last Name
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

    // Date of Birth
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

    // Contact
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

    // Education
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

    // Skills
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

    // Experience
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

    // Submit Button
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
    submitButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
    submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    submitButton.setFocusPainted(false);
    gbc.gridx = 1;
    gbc.gridy = 4;
    gbc.gridwidth = 2;
    gbc.gridheight = 1;
    gbc.anchor = GridBagConstraints.CENTER;
    formPanel.add(submitButton, gbc);

    // Submit button action
    submitButton.addActionListener(e -> {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        Date dob = dateChooser.getDate();
        String contact = contactField.getText();
        String education = educationField.getText();
        String skills = skillsField.getText();
        String experience = experienceArea.getText();

        // Basic validation
        if (firstName.isEmpty() || lastName.isEmpty() || dob == null || contact.isEmpty() ||
            education.isEmpty() || skills.isEmpty() || experience.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Format the date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dobString = sdf.format(dob);

        // Display confirmation (replace with actual CV storage logic)
        String cvDetails = String.format("CV Submitted!\nFirst Name: %s\nLast Name: %s\nDate of Birth: %s\nContact: %s\nEducation: %s\nSkills: %s\nExperience: %s",
            firstName, lastName, dobString, contact, education, skills, experience);
        JOptionPane.showMessageDialog(view, cvDetails);

        // Optionally, reset fields
        firstNameField.setText("");
        lastNameField.setText("");
        dateChooser.setDate(null);
        contactField.setText("");
        educationField.setText("");
        skillsField.setText("");
        experienceArea.setText("");
    });

    // Center the form
    JPanel centerWrapper = new JPanel();
    centerWrapper.setBackground(new Color(245, 245, 245));
    centerWrapper.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
    centerWrapper.add(formPanel);

    mainPanel.add(headerPanel, BorderLayout.NORTH);
    mainPanel.add(centerWrapper, BorderLayout.CENTER);
    updateContentPanel(mainPanel);
}

    public void showSettingsPanel() {
        System.out.println("Navigating to Settings");
        JPanel settingsPanel = new JPanel();
        settingsPanel.setBackground(new java.awt.Color(245, 245, 245));
        settingsPanel.setLayout(new java.awt.BorderLayout());
        JLabel title = new JLabel("Settings");
        title.setFont(new java.awt.Font("Segoe UI", 1, 18));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        settingsPanel.add(title, java.awt.BorderLayout.NORTH);
        updateContentPanel(settingsPanel);
    }

    public void showMyAccountPanel() {
        System.out.println("Navigating to My Account");
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setLayout(new java.awt.BorderLayout(15, 15));
        mainPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
                g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                java.awt.GradientPaint gp = new java.awt.GradientPaint(
                    0, 0, new Color(0, 4, 80),
                    0, getHeight(), new Color(0, 20, 120)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setPreferredSize(new java.awt.Dimension(680, 70));
        headerPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 25, 20));

        JLabel headerLabel = new JLabel("My Account");
        headerLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        JPanel centerWrapper = new JPanel();
        centerWrapper.setBackground(new Color(245, 245, 245));
        centerWrapper.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        JPanel formPanel = new JPanel();
        formPanel.setBackground(new Color(252, 252, 252));
        formPanel.setLayout(new java.awt.GridBagLayout());
        formPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 2, new Color(180, 180, 180, 100)),
            javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20),
                javax.swing.BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true)
            )
        ));

        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.insets = new java.awt.Insets(15, 15, 15, 15);
        gbc.fill = java.awt.GridBagConstraints.NONE;
        gbc.anchor = java.awt.GridBagConstraints.CENTER;

        JPanel usernameRow = new JPanel();
        usernameRow.setBackground(new Color(252, 252, 252));
        usernameRow.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));

        JLabel usernameIcon = new JLabel();
        try {
            javax.swing.ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("/Image/profile-user.png"));
            java.awt.Image scaledImage = icon.getImage().getScaledInstance(24, 24, java.awt.Image.SCALE_SMOOTH);
            usernameIcon.setIcon(new javax.swing.ImageIcon(scaledImage));
            usernameIcon.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 5));
        } catch (Exception e) {
            System.out.println("Error loading username icon: " + e.getMessage());
            usernameIcon.setText("U");
        }
        usernameRow.add(usernameIcon);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        usernameLabel.setForeground(new Color(0, 0, 102));
        usernameRow.add(usernameLabel);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(usernameRow, gbc);

        JTextField usernameField = new JTextField(25);
        usernameField.setText(view.username.getText()); // Sync with current username
        usernameField.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11));
        usernameField.setBackground(new Color(245, 245, 245));
        usernameField.setPreferredSize(new java.awt.Dimension(usernameField.getPreferredSize().width, 25));
        usernameField.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true),
            javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(usernameField, gbc);

        JPanel passwordRow = new JPanel();
        passwordRow.setBackground(new Color(252, 252, 252));
        passwordRow.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));

        JLabel passwordIcon = new JLabel();
        try {
            javax.swing.ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("/Image/locked-computer.png"));
            java.awt.Image scaledImage = icon.getImage().getScaledInstance(24, 24, java.awt.Image.SCALE_SMOOTH);
            passwordIcon.setIcon(new javax.swing.ImageIcon(scaledImage));
            passwordIcon.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 5));
        } catch (Exception e) {
            System.out.println("Error loading password icon: " + e.getMessage());
            passwordIcon.setText("P");
        }
        passwordRow.add(passwordIcon);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        passwordLabel.setForeground(new Color(0, 0, 102));
        passwordRow.add(passwordLabel);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(passwordRow, gbc);

        JPasswordField passwordField = new JPasswordField(25);
        passwordField.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11));
        passwordField.setBackground(new Color(245, 245, 245));
        passwordField.setPreferredSize(new java.awt.Dimension(passwordField.getPreferredSize().width, 25));
        passwordField.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true),
            javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(passwordField, gbc);

        JPanel newPasswordRow = new JPanel();
        newPasswordRow.setBackground(new Color(252, 252, 252));
        newPasswordRow.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));

        JLabel newPasswordIcon = new JLabel();
        try {
            javax.swing.ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("/Image/locked-computer.png"));
            java.awt.Image scaledImage = icon.getImage().getScaledInstance(24, 24, java.awt.Image.SCALE_SMOOTH);
            newPasswordIcon.setIcon(new javax.swing.ImageIcon(scaledImage));
            newPasswordIcon.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 5));
        } catch (Exception e) {
            System.out.println("Error loading new password icon: " + e.getMessage());
            newPasswordIcon.setText("NP");
        }
        newPasswordRow.add(newPasswordIcon);

        JLabel changePasswordLabel = new JLabel("New Password:");
        changePasswordLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        changePasswordLabel.setForeground(new Color(0, 0, 102));
        newPasswordRow.add(changePasswordLabel);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(newPasswordRow, gbc);

        JPasswordField changePasswordField = new JPasswordField(25);
        changePasswordField.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11));
        changePasswordField.setBackground(new Color(245, 245, 245));
        changePasswordField.setPreferredSize(new java.awt.Dimension(changePasswordField.getPreferredSize().width, 25));
        changePasswordField.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true),
            javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(changePasswordField, gbc);

        JButton updateButton = new JButton("Update") {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
                g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2d.setColor(new Color(0, 20, 120));
                } else {
                    g2d.setColor(new Color(0, 4, 80));
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        updateButton.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        updateButton.setForeground(Color.WHITE);
        updateButton.setContentAreaFilled(false);
        updateButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 20, 8, 20));
        updateButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        updateButton.setFocusPainted(false);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = java.awt.GridBagConstraints.NONE;
        gbc.anchor = java.awt.GridBagConstraints.CENTER;
        formPanel.add(updateButton, gbc);

        updateButton.addActionListener(e -> {
            System.out.println("Update button clicked!");
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String newPassword = new String(changePasswordField.getPassword());
            // Add validation and update logic here
            JOptionPane.showMessageDialog(view, "User info updated!\nUsername: " + username + "\nPassword: " + password + "\nNew Password: " + newPassword);
            view.username.setText(username); // Update the view's username
            view.email.setText(username.toLowerCase().replace(" ", "") + "@example.com"); // Example email update
        });

        centerWrapper.add(formPanel);
        mainPanel.add(headerPanel, java.awt.BorderLayout.NORTH);
        mainPanel.add(centerWrapper, java.awt.BorderLayout.CENTER);
        updateContentPanel(mainPanel);
    }

    public void signOut() {
        System.out.println("Signing out...");
        int confirm = JOptionPane.showConfirmDialog(view, "Are you sure you want to sign out?", "Sign Out", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            view.dispose();
        }
    }

    // Action handlers
    public void handleGetStarted() {
        System.out.println("Get Started button clicked");
        JOptionPane.showMessageDialog(view, "Get Started clicked! Implement further actions.");
    }

    public void handleLearnMore() {
        System.out.println("Learn More button clicked");
        JOptionPane.showMessageDialog(view, "Learn More clicked! Implement further actions.");
    }

    public void handleSearch() {
        String searchText = view.Searchfield.getText();
        System.out.println("Search button clicked with query: " + searchText);
        JOptionPane.showMessageDialog(view, "Search query: " + searchText);
    }

    public void handleSeeAll() {
        System.out.println("See All button clicked");
        JOptionPane.showMessageDialog(view, "See All clicked! Implement further actions.");
    }

    public void handleFilter() {
        System.out.println("Filter button clicked");
        JOptionPane.showMessageDialog(view, "Filter clicked! Implement filter options.");
    }

    // Internal utility method
    private void updateContentPanel(JPanel newPanel) {
        view.content.removeAll();
        view.content.setLayout(new java.awt.BorderLayout());
        view.content.add(newPanel, java.awt.BorderLayout.CENTER);
        view.content.setPreferredSize(new java.awt.Dimension(680, 430)); // Fixed size to match original content
        view.content.revalidate();
        view.content.repaint();
    }

    // Public method to update user information
    public void setUserInfo(String username, String email) {
        view.username.setText(username);
        view.email.setText(email);
    }
}