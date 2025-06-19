package pahilopaila.Controller;

import com.toedter.calendar.JDateChooser;
import pahilopaila.Dao.CVDao;
import pahilopaila.Dao.UserDao;
import pahilopaila.Dao.VacancyDao;
import pahilopaila.model.UserData;
import pahilopaila.model.Vacancy;
import pahilopaila.view.Dashboard_JobSeekers;
import pahilopaila.view.LoginPageview;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

/**
 * Controller for the Dashboard_JobSeekers view, handling user interactions and navigation.
 */
public class Dashboard_JobseekersController {
    private final Dashboard_JobSeekers view;
    private final CVDao cvDao;
    private final VacancyDao vacancyDao;
    private final UserDao userDao;
    private int userId;
    private String currentEmail; // Store current email for validation

    // Constructor
    public Dashboard_JobseekersController(Dashboard_JobSeekers view, int userId) {
        this.view = view;
        this.userId = userId;
        this.cvDao = new CVDao();
        this.vacancyDao = new VacancyDao();
        this.userDao = new UserDao();
        initializeListeners();
        showDashboardPanel();
    }
    // Initialize listeners for UI components
    private void initializeListeners() {
        // Sidebar navigation listeners
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

        // CV label listener is handled in createStyledLabel for popup menu
        view.uploadCVItem.addActionListener(e -> {
            System.out.println("Upload CV menu item clicked");
            showCVUploadPanel();
        });

        view.viewCVItem.addActionListener(e -> {
            System.out.println("View CV menu item clicked");
            showCVDisplayPanel();
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

        // Button listeners
        view.getStarted.addActionListener(this::handleGetStarted);
        view.learnMore.addActionListener(this::handleLearnMore);
        view.search.addActionListener(this::handleSearch);
        view.see_all.addActionListener(this::handleSeeAll);
        view.filter.addActionListener(this::handleFilter);
    }

    // Update the content panel with a new panel
    private void updateContentPanel(JPanel newPanel) {
        view.content.removeAll();
        view.content.setLayout(new java.awt.BorderLayout());
        view.content.add(newPanel, java.awt.BorderLayout.CENTER);
        view.content.revalidate();
        view.content.repaint();
    }

    // Create a vacancy card for display
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

        // Job Title
        JLabel titleLabel = new JLabel(vacancy.getJobTitle());
        titleLabel.setFont(new Font("Yu Gothic UI Semibold", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        card.add(titleLabel, gbc);

        // Job Type
        JLabel typeLabel = new JLabel(vacancy.getJobType());
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        typeLabel.setForeground(Color.WHITE);
        gbc.gridy = 1;
        card.add(typeLabel, gbc);

        // Experience Level
        JLabel experienceLabel = new JLabel(vacancy.getExperienceLevel());
        experienceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        experienceLabel.setForeground(Color.WHITE);
        gbc.gridy = 2;
        card.add(experienceLabel, gbc);

        // Days Left
        JLabel daysLeftLabel = new JLabel(vacancy.getDaysLeft() + " Days Left");
        daysLeftLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        daysLeftLabel.setForeground(Color.WHITE);
        gbc.gridy = 3;
        card.add(daysLeftLabel, gbc);

        return card;
    }

    // Navigation methods
    public void showDashboardPanel() {
        System.out.println("Navigating to Dashboard");
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBackground(new java.awt.Color(245, 245, 245));

        // Message Panel
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

        contentPanel.add(messagePanel, BorderLayout.NORTH);

        // Featured Jobs Section
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

        // Vacancies Panel
        JPanel vacanciesPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        vacanciesPanel.setBackground(new Color(245, 245, 245));
        JScrollPane scrollPane = new JScrollPane(vacanciesPanel);
        scrollPane.setBorder(null);

        // Fetch vacancies (limit to 3 for Featured Jobs)
        List<Vacancy> allVacancies = vacancyDao.getAllVacancies();
        if (allVacancies.isEmpty()) {
            JLabel noVacanciesLabel = new JLabel("No vacancies available.");
            noVacanciesLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            noVacanciesLabel.setHorizontalAlignment(SwingConstants.CENTER);
            vacanciesPanel.add(noVacanciesLabel);
        } else {
            // Select up to 3 random vacancies
            Random rand = new Random();
            int count = Math.min(3, allVacancies.size());
            for (int i = 0; i < count; i++) {
                int index = rand.nextInt(allVacancies.size());
                Vacancy vacancy = allVacancies.remove(index); // Remove to avoid duplicates
                JPanel vacancyCard = createVacancyCard(vacancy);
                vacanciesPanel.add(vacancyCard);
            }
        }

        featuredPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(featuredPanel, BorderLayout.CENTER);

        updateContentPanel(contentPanel);
    }

    public void showVacancyPanel() {
        System.out.println("Navigating to Vacancy");
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Header
        JLabel headerLabel = new JLabel("Browse Vacancies");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(new Color(0, 4, 80));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Vacancies Panel
        JPanel vacanciesPanel = new JPanel(new GridLayout(0, 3, 10, 10)); // 3 columns
        vacanciesPanel.setBackground(new Color(245, 245, 245));
        JScrollPane scrollPane = new JScrollPane(vacanciesPanel);
        scrollPane.setBorder(null);

        // Fetch all vacancies
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
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            java.util.Date dob = dateChooser.getDate();
            String contact = contactField.getText().trim();
            String education = educationField.getText().trim();
            String skills = skillsField.getText().trim();
            String experience = experienceArea.getText().trim();

            // Basic validation
            if (firstName.isEmpty() || lastName.isEmpty() || dob == null || contact.isEmpty() ||
                education.isEmpty() || skills.isEmpty() || experience.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Save to database
            boolean success = cvDao.saveCV(userId, firstName, lastName, dob, contact, education, skills, experience);
            if (success) {
                JOptionPane.showMessageDialog(view, "CV saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                // Reset fields
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

        // Center the form
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

        JLabel headerLabel = new JLabel("My CV");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        // Form panel to display CV data
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

        // Retrieve CV data
        ResultSet rs = cvDao.getCVByUserId(userId);
        try {
            if (rs != null && rs.next()) {
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String dob = rs.getString("dob");
                String contact = rs.getString("contact");
                String education = rs.getString("education");
                String skills = rs.getString("skills");
                String experience = rs.getString("experience");

                // First Name
                JLabel firstNameLabel = new JLabel("First Name:");
                firstNameLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
                gbc.gridx = 0;
                gbc.gridy = 0;
                formPanel.add(firstNameLabel, gbc);

                JLabel firstNameValue = new JLabel(firstName);
                firstNameValue.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                gbc.gridx = 1;
                gbc.gridy = 0;
                formPanel.add(firstNameValue, gbc);

                // Last Name
                JLabel lastNameLabel = new JLabel("Last Name:");
                lastNameLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
                gbc.gridx = 2;
                gbc.gridy = 0;
                formPanel.add(lastNameLabel, gbc);

                JLabel lastNameValue = new JLabel(lastName);
                lastNameValue.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                gbc.gridx = 3;
                gbc.gridy = 0;
                formPanel.add(lastNameValue, gbc);

                // Date of Birth
                JLabel dobLabel = new JLabel("Date of Birth:");
                dobLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
                gbc.gridx = 0;
                gbc.gridy = 1;
                formPanel.add(dobLabel, gbc);

                JLabel dobValue = new JLabel(dob);
                dobValue.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                gbc.gridx = 1;
                gbc.gridy = 1;
                formPanel.add(dobValue, gbc);

                // Contact
                JLabel contactLabel = new JLabel("Contact:");
                contactLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
                gbc.gridx = 2;
                gbc.gridy = 1;
                formPanel.add(contactLabel, gbc);

                JLabel contactValue = new JLabel(contact);
                contactValue.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                gbc.gridx = 3;
                gbc.gridy = 1;
                formPanel.add(contactValue, gbc);

                // Education
                JLabel educationLabel = new JLabel("Education:");
                educationLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
                gbc.gridx = 0;
                gbc.gridy = 2;
                formPanel.add(educationLabel, gbc);

                JLabel educationValue = new JLabel(education);
                educationValue.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                gbc.gridx = 1;
                gbc.gridy = 2;
                formPanel.add(educationValue, gbc);

                // Skills
                JLabel skillsLabel = new JLabel("Skills:");
                skillsLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
                gbc.gridx = 0;
                gbc.gridy = 3;
                formPanel.add(skillsLabel, gbc);

                JLabel skillsValue = new JLabel(skills);
                skillsValue.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                gbc.gridx = 1;
                gbc.gridy = 3;
                formPanel.add(skillsValue, gbc);

                // Experience
                JLabel experienceLabel = new JLabel("Experience:");
                experienceLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
                gbc.gridx = 2;
                gbc.gridy = 2;
                formPanel.add(experienceLabel, gbc);

                JTextArea experienceArea = new JTextArea(experience, 4, 15);
                experienceArea.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                experienceArea.setBackground(new Color(245, 245, 245));
                experienceArea.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true),
                    BorderFactory.createEmptyBorder(4, 8, 4, 8)
                ));
                experienceArea.setLineWrap(true);
                experienceArea.setWrapStyleWord(true);
                experienceArea.setEditable(false);
                JScrollPane experienceScroll = new JScrollPane(experienceArea);
                experienceScroll.setPreferredSize(new Dimension(150, 80));
                gbc.gridx = 3;
                gbc.gridy = 2;
                gbc.gridheight = 2;
                formPanel.add(experienceScroll, gbc);
            } else {
                JLabel noCVLabel = new JLabel("No CV found.");
                noCVLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.gridwidth = 4;
                formPanel.add(noCVLabel, gbc);
            }
            if (rs != null) rs.close();
        } catch (SQLException e) {
            System.err.println("Error retrieving CV: " + e.getMessage());
            JOptionPane.showMessageDialog(view, "Error retrieving CV data.", "Error", JOptionPane.ERROR_MESSAGE);
        }

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

    // My account Pnel to manually change and update the password 
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
}