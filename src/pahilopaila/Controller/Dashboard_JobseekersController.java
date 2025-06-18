package pahilopaila.Controller;

import com.toedter.calendar.JDateChooser;
import pahilopaila.Dao.CVDao;
import pahilopaila.Dao.VacancyDao;
import pahilopaila.model.Vacancy;
import pahilopaila.view.Dashboard_JobSeekers;
import pahilopaila.view.LoginPageview;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private int userId; // Store userId

    // Constructor to accept the view and userId
    public Dashboard_JobseekersController(Dashboard_JobSeekers view, int userId) {
        this.view = view;
        this.userId = userId;
        this.cvDao = new CVDao();
        this.vacancyDao = new VacancyDao();
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
         // [CHANGED] Rating Panel with Star Icons
        JPanel ratingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        ratingPanel.setBackground(new Color(245, 245, 245));
        ratingPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel ratingLabel = new JLabel("Rate Our App:");
        ratingLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        ratingPanel.add(ratingLabel);

        ButtonGroup ratingGroup = new ButtonGroup();
        JRadioButton[] stars = new JRadioButton[5];
        ImageIcon starIcon = new ImageIcon(getClass().getResource("/Image/star.png")); // Ensure star.png exists
        if (starIcon.getImage() == null) {
            starIcon = new ImageIcon(new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB)); // Fallback
        }
        Image scaledStar = starIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        for (int i = 0; i < 5; i++) {
            stars[i] = new JRadioButton();
            stars[i].setIcon(new ImageIcon(scaledStar));
            stars[i].setSelectedIcon(new ImageIcon(scaledStar)); // Same icon for selected state
            stars[i].setRolloverIcon(new ImageIcon(scaledStar)); // Optional: Hover effect
            stars[i].setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
            stars[i].setBackground(new Color(245, 245, 245));
            ratingGroup.add(stars[i]);
            ratingPanel.add(stars[i]);
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
                String currentDateTime = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("hh:mm a zzz 'on' EEEE, MMMM dd, yyyy"));
                JOptionPane.showMessageDialog(view, "Thank you for rating us " + rating + " stars!\nSubmitted at " + currentDateTime, "Rating Submitted", JOptionPane.INFORMATION_MESSAGE);
                // [CHANGED] Placeholder for saving rating to database
                boolean success = saveRatingToDatabase(userId, rating);
                if (!success) {
                    JOptionPane.showMessageDialog(view, "Failed to save rating. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(view, "Please select a rating.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        ratingPanel.add(submitRating);

        // [CHANGED] Combine featured and rating panels with better layout
        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));
        centerPanel.setBackground(new Color(245, 245, 245));
        centerPanel.add(featuredPanel, BorderLayout.CENTER);
        centerPanel.add(ratingPanel, BorderLayout.SOUTH);

        contentPanel.add(centerPanel, BorderLayout.CENTER);

        updateContentPanel(contentPanel);
    }

    // [CHANGED] Method to save rating to database (placeholder)
    private boolean saveRatingToDatabase(int userId, int rating) {
        // Placeholder implementation - Replace with actual database logic
        try {
            // Example: Assume cvDao has a method to save rating
            // return cvDao.saveRating(userId, rating);
            System.out.println("Saving rating " + rating + " for userId " + userId);
            return true; // Simulate success
        } catch (Exception e) {
            System.err.println("Error saving rating: " + e.getMessage());
            return false;
        }

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
        usernameField.setText(view.username.getText());
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

        JButton saveButton = new JButton("Save Changes") {
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
        saveButton.setFont(new Font("Segoe UI Semibold", Font.BOLD, 14));
        saveButton.setForeground(Color.WHITE);
        saveButton.setContentAreaFilled(false);
        saveButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.setFocusPainted(false);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(saveButton, gbc);

        saveButton.addActionListener(e -> {
            String newUsername = usernameField.getText().trim();
            String newPassword = new String(passwordField.getPassword()).trim();
            if (newUsername.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Username cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Placeholder for updating username/password in the database
            JOptionPane.showMessageDialog(view, "Account details updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            view.username.setText(newUsername);
        });

        centerWrapper.add(formPanel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerWrapper, BorderLayout.CENTER);
        updateContentPanel(mainPanel);
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