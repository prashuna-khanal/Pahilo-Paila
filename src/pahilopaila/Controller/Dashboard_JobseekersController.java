package pahilopaila.Controller;

import com.toedter.calendar.JDateChooser;
import pahilopaila.Dao.CVDao;
import pahilopaila.Dao.RatingDao;
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
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.awt.image.BufferedImage;

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
            Random rand = new Random();
            int count = Math.min(3, allVacancies.size());
            for (int i = 0; i < count; i++) {
                int index = rand.nextInt(allVacancies.size());
                Vacancy vacancy = allVacancies.remove(index);
                JPanel vacancyCard = createVacancyCard(vacancy);
                vacanciesPanel.add(vacancyCard);
            }
        }

        featuredPanel.add(scrollPane, BorderLayout.CENTER);

        // CHANGED: Updated rating panel to fix hover effect starting from 1st star
        // Rating Panel with Graphical Stars
        JPanel ratingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        ratingPanel.setBackground(new Color(245, 245, 245));
        ratingPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel ratingLabel = new JLabel("Rate Our App:");
        ratingLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        ratingPanel.add(ratingLabel);

        ButtonGroup ratingGroup = new ButtonGroup();
        JRadioButton[] stars = new JRadioButton[5];
        // CHANGED: Track the currently hovered star index
        final int[] currentHoverIndex = {-1}; // -1 means no hover

        // CHANGED: Updated StarIcon class to handle selection and hover correctly
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

                // CHANGED: Highlight if this star is within the selected or hovered range
                boolean shouldHighlight = false;
                // Check for selection
                for (int i = 0; i < stars.length; i++) {
                    if (stars[i].isSelected() && starIndex <= i) {
                        shouldHighlight = true;
                        break;
                    }
                }
                // Check for hover
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

        // CHANGED: Updated star creation loop to handle hover from 1st star
        for (int i = 0; i < 5; i++) {
            stars[i] = new JRadioButton();
            stars[i].setIcon(new StarIcon(20, 20, i, stars));
            stars[i].setRolloverEnabled(true);
            stars[i].setContentAreaFilled(false);
            stars[i].setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
            stars[i].setBackground(new Color(245, 245, 245));
            ratingGroup.add(stars[i]);
            ratingPanel.add(stars[i]);

            // CHANGED: Updated mouse listener to highlight stars from 1st to hovered
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

            // CHANGED: Ensure repaint on selection
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

        // Combine featured and rating panels
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
    
    // Main container panel (white background like My Account)
    JPanel settingsPanel = new JPanel();
    settingsPanel.setBackground(new Color(245, 245, 245));
    settingsPanel.setLayout(new java.awt.BorderLayout(15, 15));
    settingsPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));

    // Dark blue title bar
    JPanel titlePanel = new JPanel();
    titlePanel.setBackground(new Color(0, 20, 90));  // Dark blue
    titlePanel.setPreferredSize(new java.awt.Dimension(680, 70));
    titlePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 25, 20));
    
    JLabel titleLabel = new JLabel("Settings");
    titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
    titleLabel.setForeground(Color.WHITE);
    titlePanel.add(titleLabel);

    // Content box (like the white form in My Account tab)
    JPanel contentBox = new JPanel();
    contentBox.setBackground(Color.WHITE);
    contentBox.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
    contentBox.setLayout(new BoxLayout(contentBox, BoxLayout.Y_AXIS));
    contentBox.setPreferredSize(new Dimension(500, 250));  // Increased height for more content
    contentBox.setAlignmentX(Component.LEFT_ALIGNMENT);

    // Dark Mode checkbox
    JCheckBox darkModeCheck = new JCheckBox("Dark Mode");
    darkModeCheck.setBackground(Color.WHITE);
    darkModeCheck.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    darkModeCheck.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    // Wrap checkbox inside a panel for alignment
    JPanel darkModePanel = new JPanel();
    darkModePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    darkModePanel.setBackground(Color.WHITE);
    darkModePanel.add(new JLabel("🌙")); // Moon icon
    darkModePanel.add(Box.createHorizontalStrut(10));
    darkModePanel.add(darkModeCheck);

    // Notifications checkbox
    JCheckBox notificationCheck = new JCheckBox("Enable Notifications");
    notificationCheck.setBackground(Color.WHITE);
    notificationCheck.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    notificationCheck.setSelected(true); // Default enabled
    notificationCheck.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    // Wrap notifications checkbox inside a panel for alignment
    JPanel notificationPanel = new JPanel();
    notificationPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    notificationPanel.setBackground(Color.WHITE);
    notificationPanel.add(new JLabel("🔔")); // Bell icon
    notificationPanel.add(Box.createHorizontalStrut(10));
    notificationPanel.add(notificationCheck);

    // Contact Us label
    JLabel contactUsLabel = new JLabel("Contact Us: support@pahilopaila.com");
    contactUsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    contactUsLabel.setForeground(new Color(0, 123, 255)); // Blue color for link appearance
    contactUsLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
    contactUsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    // Wrap contact label inside a panel for alignment
    JPanel contactPanel = new JPanel();
    contactPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    contactPanel.setBackground(Color.WHITE);
    contactPanel.add(new JLabel("📞")); // Phone icon
    contactPanel.add(Box.createHorizontalStrut(10));
    contactPanel.add(contactUsLabel);

    // Update button
    JButton updateButton = new JButton("Update Settings");
    updateButton.setBackground(new Color(0, 20, 90));  // Same dark blue as title
    updateButton.setForeground(Color.WHITE);
    updateButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
    updateButton.setPreferredSize(new Dimension(150, 35));
    updateButton.setFocusPainted(false);
    updateButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
    
    // Wrap update button in a panel for right alignment
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.setBackground(Color.WHITE);
    buttonPanel.add(updateButton);

    // Add all components to content box with proper spacing
    contentBox.add(Box.createVerticalStrut(20));  // Top spacing
    contentBox.add(darkModePanel);
    contentBox.add(Box.createVerticalStrut(15));  // Spacing between items
    contentBox.add(notificationPanel);
    contentBox.add(Box.createVerticalStrut(15));  // Spacing between items
    contentBox.add(contactPanel);
    contentBox.add(Box.createVerticalStrut(20));  // Spacing before button
    contentBox.add(buttonPanel);
    contentBox.add(Box.createVerticalStrut(20));  // Bottom spacing

    // Add event listeners
    darkModeCheck.addActionListener(e -> {
        applyDarkModeToSettings(darkModeCheck.isSelected(), settingsPanel);
        String status = darkModeCheck.isSelected() ? "enabled" : "disabled";
        JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(settingsPanel), 
            "Dark mode " + status + " successfully!", 
            "Dark Mode", 
            JOptionPane.INFORMATION_MESSAGE);
    });

    notificationCheck.addActionListener(e -> {
        String status = notificationCheck.isSelected() ? "enabled" : "disabled";
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
        StringBuilder message = new StringBuilder("Settings Updated Successfully!\n\n");
        message.append("Dark Mode: ").append(darkModeCheck.isSelected() ? "Enabled" : "Disabled").append("\n");
        message.append("Notifications: ").append(notificationCheck.isSelected() ? "Enabled" : "Disabled");
        
        JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(settingsPanel), 
            message.toString(), 
            "Settings Updated", 
            JOptionPane.INFORMATION_MESSAGE);
    });

    // Center wrapper for content box
    JPanel centerPanel = new JPanel();
    centerPanel.setBackground(Color.WHITE);
    centerPanel.setLayout(new GridBagLayout());
    centerPanel.add(contentBox);

    // Add title and content to main settings panel
    settingsPanel.add(titlePanel, BorderLayout.NORTH);
    settingsPanel.add(centerPanel, BorderLayout.CENTER);

    updateContentPanel(settingsPanel);
}

// Method to apply dark mode to the current settings panel and find parent frame
private void applyDarkModeToSettings(boolean isDarkMode, JPanel settingsPanel) {
    // Find the parent frame/window
    Window parentWindow = SwingUtilities.getWindowAncestor(settingsPanel);
    
    if (isDarkMode) {
        // Dark mode colors
        Color darkBackground = new Color(45, 45, 48);
        Color darkPanel = new Color(37, 37, 38);
        Color darkText = new Color(220, 220, 220);
        Color darkBorder = new Color(60, 60, 60);
        
        // Apply to parent window if it exists
        if (parentWindow != null) {
            parentWindow.setBackground(darkBackground);
            if (parentWindow instanceof JFrame) {
                ((JFrame) parentWindow).getContentPane().setBackground(darkBackground);
            }
        }
        
        // Apply dark theme to current panel
        applyDarkThemeToComponent(settingsPanel, darkBackground, darkPanel, darkText, darkBorder);
        
    } else {
        // Light mode colors (original)
        Color lightBackground = Color.WHITE;
        Color lightPanel = new Color(245, 245, 245);
        Color lightText = Color.BLACK;
        Color lightBorder = Color.LIGHT_GRAY;
        
        // Apply to parent window if it exists
        if (parentWindow != null) {
            parentWindow.setBackground(lightBackground);
            if (parentWindow instanceof JFrame) {
                ((JFrame) parentWindow).getContentPane().setBackground(lightPanel);
            }
        }
        
        // Apply light theme to current panel
        applyLightThemeToComponent(settingsPanel, lightBackground, lightPanel, lightText, lightBorder);
    }
    
    // Force repaint
    if (parentWindow != null) {
        parentWindow.repaint();
    }
    settingsPanel.repaint();
}

// Helper method to apply dark theme recursively
private void applyDarkThemeToComponent(Container container, Color darkBg, Color darkPanel, Color darkText, Color darkBorder) {
    for (Component component : container.getComponents()) {
        if (component instanceof JPanel) {
            JPanel panel = (JPanel) component;
            // Keep title panels with original dark blue
            if (panel.getBackground().equals(new Color(0, 20, 90))) {
                // Keep original dark blue for title bars
            } else if (panel.getBackground().equals(Color.WHITE)) {
                panel.setBackground(darkPanel);
            } else if (panel.getBackground().equals(new Color(245, 245, 245))) {
                panel.setBackground(darkBg);
            }
            
            // Update border if it exists
            if (panel.getBorder() instanceof javax.swing.border.LineBorder) {
                panel.setBorder(BorderFactory.createLineBorder(darkBorder));
            }
            
            // Recursively apply to child components
            applyDarkThemeToComponent(panel, darkBg, darkPanel, darkText, darkBorder);
            
        } else if (component instanceof JLabel) {
            JLabel label = (JLabel) component;
            // Don't change white text (title labels) or blue link text
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
            // Keep original button styling for consistency
            
        } else if (component instanceof Container) {
            applyDarkThemeToComponent((Container) component, darkBg, darkPanel, darkText, darkBorder);
        }
    }
}

// Helper method to apply light theme recursively
private void applyLightThemeToComponent(Container container, Color lightBg, Color lightPanel, Color lightText, Color lightBorder) {
    for (Component component : container.getComponents()) {
        if (component instanceof JPanel) {
            JPanel panel = (JPanel) component;
            // Restore original colors based on what they should be
            if (panel.getBackground().equals(new Color(0, 20, 90))) {
                // Keep original dark blue for title bars
            } else if (panel.getBackground().equals(new Color(37, 37, 38))) {
                // This was a white panel, restore to white
                panel.setBackground(Color.WHITE);
            } else if (panel.getBackground().equals(new Color(45, 45, 48))) {
                // This was the main background, restore to light gray
                panel.setBackground(lightPanel);
            }
            
            // Update border if it exists
            if (panel.getBorder() instanceof javax.swing.border.LineBorder) {
                panel.setBorder(BorderFactory.createLineBorder(lightBorder));
            }
            
            // Recursively apply to child components
            applyLightThemeToComponent(panel, lightBg, lightPanel, lightText, lightBorder);
            
        } else if (component instanceof JLabel) {
            JLabel label = (JLabel) component;
            // Restore original text colors
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
            // Keep original button styling
            
        } else if (component instanceof Container) {
            applyLightThemeToComponent((Container) component, lightBg, lightPanel, lightText, lightBorder);
        }
    }
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
    private boolean saveRatingToDatabase(int userId, int rating) {
    // Check if the rating is valid (must be between 1 and 5)
    if (rating < 1 || rating > 5) {
        System.err.println("Invalid rating value: " + rating + ". Must be between 1 and 5.");
        return false;
    }

    // Create a new RatingDao instance and try to save the rating
    RatingDao ratingDao = new RatingDao();
    boolean success = ratingDao.saveRating(userId, rating);

    // Log the result
    if (success) {
        System.out.println("Rating " + rating + " saved successfully for userId: " + userId);
    } else {
        System.err.println("Failed to save rating for userId: " + userId);
    }

    // Return the result of the save operation
    return success;
}
}
