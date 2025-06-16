package pahilopaila.Controller; // Matches pahilopaila/controller directory

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import pahilopaila.view.Dashboard_Recruiters;

// Added for LayoutStyle.ComponentPlacement
import javax.swing.*;
import java.awt.* ;
import java.text.SimpleDateFormat;
import com.toedter.calendar.JDateChooser;
import java.awt.event.ActionEvent;
import javax.swing.LayoutStyle; // Added for LayoutStyle.ComponentPlacement
import java.util.Date;
import javax.swing.table.DefaultTableModel;
import com.toedter.calendar.JDateChooser;
import java.text.ParseException;


/**
 * Controller for the Dashboard_Recruiters view, handling user interactions and navigation. Function Methods added to write codes.
 */
public class Dashboard_RecruitersController {
    private final Dashboard_Recruiters view;

    // Constructor to accept the view
    public Dashboard_RecruitersController(Dashboard_Recruiters view) {
        this.view = view;
        initializeListeners();
    }

    // Initialize listeners for UI components (private as it's an internal setup method)
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

        view.appliccation.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showApplicationsPanel();
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
                view.showMyAccountPanel();
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

        view.jButton1.addActionListener((ActionEvent e) -> {
            handleSearch();
        });

        view.jButton2.addActionListener((ActionEvent e) -> {
            handleFilter();
        });
    }

    // Navigation methods made public for potential external access
    public void showDashboardPanel() {
        System.out.println("Navigating to Dashboard");
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new java.awt.Color(245, 245, 245));
        contentPanel.setLayout(new java.awt.BorderLayout());

        JPanel messagePanel = new JPanel();
        messagePanel.setBackground(new java.awt.Color(0, 4, 80));
        messagePanel.setLayout(new javax.swing.GroupLayout(messagePanel));

        JLabel find = new JLabel("Find the right people");
        find.setFont(new java.awt.Font("Segoe UI Symbol", 1, 24));
        find.setForeground(new java.awt.Color(255, 255, 255));

        JLabel right = new JLabel("for the right Job");
        right.setFont(new java.awt.Font("Segoe UI Symbol", 1, 24));
        right.setForeground(new java.awt.Color(255, 255, 255));

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

        JLabel imageLabel = new JLabel();
        try {
            imageLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/logo/3man.png")));
        } catch (Exception e) {
            System.out.println("Error loading 3man icon: " + e.getMessage());
        }

        // Layout for messagePanel
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(messagePanel);
        messagePanel.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(39, 39, 39)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(find, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(right, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(getStarted, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(learnMore, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 165, Short.MAX_VALUE)
                    .addComponent(imageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(22, 22, 22))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(find)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(right)
                            .addGap(18, 18, 18)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(learnMore, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(getStarted, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(25, 25, 25)
                            .addComponent(imageLabel)))
                    .addContainerGap(12, Short.MAX_VALUE))
        );

        contentPanel.add(messagePanel, java.awt.BorderLayout.CENTER);
        updateContentPanel(contentPanel);
    }

public void showVacancyPanel() {
    System.out.println("Navigating to Vacancy Posting");
    JPanel mainPanel = new JPanel();
    mainPanel.setBackground(new Color(245, 245, 245));
    mainPanel.setLayout(new BorderLayout(10, 10));
    mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    mainPanel.setPreferredSize(new Dimension(680, 480)); // Adjusted height to fit smaller fields

    // Header panel
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
    headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));

    JLabel headerLabel = new JLabel("Post a Job Vacancy");
    headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
    headerLabel.setForeground(Color.WHITE);
    headerPanel.add(headerLabel);

    // Form panel
    JPanel formPanel = new JPanel();
    formPanel.setBackground(new Color(252, 252, 252));
    formPanel.setLayout(new GridBagLayout());
    formPanel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
        BorderFactory.createEmptyBorder(15, 15, 15, 15)
    ));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(8, 8, 8, 8);
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.anchor = GridBagConstraints.WEST;

    // Job Title
    JLabel jobTitleLabel = new JLabel("Job Title:");
    jobTitleLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
    gbc.gridx = 0;
    gbc.gridy = 0;
    formPanel.add(jobTitleLabel, gbc);

    JTextField jobTitleField = new JTextField(15);
    jobTitleField.setFont(new Font("Segoe UI", Font.PLAIN, 11));
    jobTitleField.setBackground(new Color(245, 245, 245));
    jobTitleField.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true));
    gbc.gridx = 1;
    gbc.gridy = 0;
    formPanel.add(jobTitleField, gbc);

    // Company Name
    JLabel companyNameLabel = new JLabel("Company Name:");
    companyNameLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
    gbc.gridx = 2;
    gbc.gridy = 0;
    formPanel.add(companyNameLabel, gbc);

    JTextField companyNameField = new JTextField(15);
    companyNameField.setFont(new Font("Segoe UI", Font.PLAIN, 11));
    companyNameField.setBackground(new Color(245, 245, 245));
    companyNameField.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true));
    gbc.gridx = 3;
    gbc.gridy = 0;
    formPanel.add(companyNameField, gbc);

    // Job Type
    JLabel jobTypeLabel = new JLabel("Job Type:");
    jobTypeLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
    gbc.gridx = 0;
    gbc.gridy = 1;
    formPanel.add(jobTypeLabel, gbc);

    JComboBox<String> jobTypeCombo = new JComboBox<>(new String[]{"Full-time", "Part-time", "Contract"});
    jobTypeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
    jobTypeCombo.setBackground(new Color(245, 245, 245));
    jobTypeCombo.setPreferredSize(new Dimension(150, 25));
    gbc.gridx = 1;
    gbc.gridy = 1;
    formPanel.add(jobTypeCombo, gbc);

    // Experience Level
    JLabel experienceLevelLabel = new JLabel("Experience Level:");
    experienceLevelLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
    gbc.gridx = 2;
    gbc.gridy = 1;
    formPanel.add(experienceLevelLabel, gbc);

    JComboBox<String> experienceLevelCombo = new JComboBox<>(new String[]{"Junior", "Mid", "Senior"});
    experienceLevelCombo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
    experienceLevelCombo.setBackground(new Color(245, 245, 245));
    experienceLevelCombo.setPreferredSize(new Dimension(150, 25));
    gbc.gridx = 3;
    gbc.gridy = 1;
    formPanel.add(experienceLevelCombo, gbc);

    // Location
    JLabel locationLabel = new JLabel("Location:");
    locationLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
    gbc.gridx = 0;
    gbc.gridy = 2;
    formPanel.add(locationLabel, gbc);

    JTextField locationField = new JTextField(15);
    locationField.setFont(new Font("Segoe UI", Font.PLAIN, 11));
    locationField.setBackground(new Color(245, 245, 245));
    locationField.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true));
    gbc.gridx = 1;
    gbc.gridy = 2;
    formPanel.add(locationField, gbc);

    // Salary Range
    JLabel salaryLabel = new JLabel("Salary Range (Optional):");
    salaryLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
    gbc.gridx = 2;
    gbc.gridy = 2;
    formPanel.add(salaryLabel, gbc);

    JTextField salaryField = new JTextField(15);
    salaryField.setFont(new Font("Segoe UI", Font.PLAIN, 11));
    salaryField.setBackground(new Color(245, 245, 245));
    salaryField.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true));
    gbc.gridx = 3;
    gbc.gridy = 2;
    formPanel.add(salaryField, gbc);

    // Application Deadline
    JLabel deadlineLabel = new JLabel("Application Deadline:");
    deadlineLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
    gbc.gridx = 0;
    gbc.gridy = 3;
    formPanel.add(deadlineLabel, gbc);

    // Initialize deadlineChooser once
    final JDateChooser deadlineChooser = createDateChooser();
    final JTextField deadlineTextField = new JTextField("Enter date (yyyy-MM-dd)", 15);
    deadlineTextField.setFont(new Font("Segoe UI", Font.PLAIN, 11));
    deadlineTextField.setBackground(new Color(245, 245, 245));
    deadlineTextField.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true));
    
    gbc.gridx = 1;
    gbc.gridy = 3;
    formPanel.add(deadlineChooser != null ? deadlineChooser : deadlineTextField, gbc);

    // Job Description (reduced size)
    JLabel descriptionLabel = new JLabel("Job Description:");
    descriptionLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
    gbc.gridx = 0;
    gbc.gridy = 4;
    formPanel.add(descriptionLabel, gbc);

    JTextArea descriptionArea = new JTextArea(4, 15);
    descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 11));
    descriptionArea.setBackground(new Color(245, 245, 245));
    descriptionArea.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true));
    descriptionArea.setLineWrap(true);
    descriptionArea.setWrapStyleWord(true);
    JScrollPane descriptionScroll = new JScrollPane(descriptionArea);
    descriptionScroll.setPreferredSize(new Dimension(150, 50)); // Reduced from 200, 80 to 150, 50
    gbc.gridx = 1;
    gbc.gridy = 4;
    gbc.gridwidth = 3;
    formPanel.add(descriptionScroll, gbc);

    // Requirements (reduced size)
    JLabel requirementsLabel = new JLabel("Requirements:");
    requirementsLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
    gbc.gridx = 0;
    gbc.gridy = 5;
    gbc.gridwidth = 1;
    formPanel.add(requirementsLabel, gbc);

    JTextArea requirementsArea = new JTextArea(4, 15);
    requirementsArea.setFont(new Font("Segoe UI", Font.PLAIN, 11));
    requirementsArea.setBackground(new Color(245, 245, 245));
    requirementsArea.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true));
    requirementsArea.setLineWrap(true);
    requirementsArea.setWrapStyleWord(true);
    JScrollPane requirementsScroll = new JScrollPane(requirementsArea);
    requirementsScroll.setPreferredSize(new Dimension(150, 50)); // Reduced from 200, 80 to 150, 50
    gbc.gridx = 1;
    gbc.gridy = 5;
    gbc.gridwidth = 3;
    formPanel.add(requirementsScroll, gbc);

    // Submit Button
    JButton submitButton = new JButton("Post Vacancy") {
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
    submitButton.setPreferredSize(new Dimension(130, 30));
    gbc.gridx = 1;
    gbc.gridy = 6;
    gbc.gridwidth = 2;
    gbc.gridheight = 1;
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.NONE;
    formPanel.add(submitButton, gbc);

    // Submit button action
    submitButton.addActionListener(e -> {
        String jobTitle = jobTitleField.getText().trim();
        String companyName = companyNameField.getText().trim();
        String jobType = (String) jobTypeCombo.getSelectedItem();
        String experienceLevel = (String) experienceLevelCombo.getSelectedItem();
        String location = locationField.getText().trim();
        String salary = salaryField.getText().trim();
        Date deadline;
        
        // Handle deadline input
        if (deadlineChooser != null) {
            deadline = deadlineChooser.getDate();
        } else {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                sdf.setLenient(false);
                deadline = sdf.parse(deadlineTextField.getText().trim());
                if (deadline.before(new Date())) {
                    JOptionPane.showMessageDialog(view, "Please select a future date.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (ParseException ex) {
                System.err.println("Error parsing date: " + ex.getMessage());
                JOptionPane.showMessageDialog(view, "Invalid date format. Use yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        String description = descriptionArea.getText().trim();
        String requirements = requirementsArea.getText().trim();

        // Validation
        if (jobTitle.isEmpty() || companyName.isEmpty() || location.isEmpty() || deadline == null ||
            description.isEmpty() || requirements.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Please fill in all required fields, including a valid deadline.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Format deadline
        String deadlineString;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            deadlineString = sdf.format(deadline);
        } catch (Exception ex) {
            System.err.println("Error formatting deadline: " + ex.getMessage());
            JOptionPane.showMessageDialog(view, "Invalid date format. Please select a valid deadline.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Placeholder for vacancy submission logic
        String vacancyDetails = String.format(
            "Vacancy Posted!\nJob Title: %s\nCompany: %s\nJob Type: %s\nExperience Level: %s\nLocation: %s\nSalary: %s\nDeadline: %s\nDescription: %s\nRequirements: %s",
            jobTitle, companyName, jobType, experienceLevel, location, salary.isEmpty() ? "Not specified" : salary,
            deadlineString, description, requirements
        );
        JOptionPane.showMessageDialog(view, vacancyDetails);

        // Reset fields
        jobTitleField.setText("");
        companyNameField.setText("");
        jobTypeCombo.setSelectedIndex(0);
        experienceLevelCombo.setSelectedIndex(0);
        locationField.setText("");
        salaryField.setText("");
        if (deadlineChooser != null) {
            deadlineChooser.setDate(null);
        } else {
            deadlineTextField.setText("Enter date (yyyy-MM-dd)");
        }
        descriptionArea.setText("");
        requirementsArea.setText("");
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

// Helper method to create JDateChooser
private JDateChooser createDateChooser() {
    try {
        JDateChooser chooser = new JDateChooser();
        chooser.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        chooser.setBackground(new Color(245, 245, 245));
        chooser.setPreferredSize(new Dimension(150, 25));
        chooser.setDateFormatString("yyyy-MM-dd");
        chooser.setMinSelectableDate(new Date()); // Prevent past dates
        return chooser;
    } catch (Exception e) {
        System.err.println("Error initializing JDateChooser: " + e.getMessage());
        JOptionPane.showMessageDialog(view, "Date picker initialization failed. Please ensure jcalendar library is included.", "Error", JOptionPane.ERROR_MESSAGE);
        return null;
    }
}

    public void showApplicationsPanel() {
        System.out.println("Navigating to Applications");
        JPanel applicationsPanel = new JPanel();
        applicationsPanel.setBackground(new java.awt.Color(245, 245, 245));
        applicationsPanel.setLayout(new java.awt.BorderLayout());
        JLabel title = new JLabel("View Applications");
        title.setFont(new java.awt.Font("Segoe UI", 1, 18));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        applicationsPanel.add(title, java.awt.BorderLayout.NORTH);
        updateContentPanel(applicationsPanel);
    }

    public void showSettingsPanel() {
        System.out.println("Navigating to Settings");
        
        // Main container panel (white background like My Account)
    JPanel settingsPanel = new JPanel();
    settingsPanel.setBackground(Color.WHITE);
    settingsPanel.setLayout(new BorderLayout());

    // Dark blue title bar
    JPanel titlePanel = new JPanel();
    titlePanel.setBackground(new Color(0, 32, 96));  // Dark blue
    titlePanel.setPreferredSize(new java.awt.Dimension(680,70 ));
    
    JLabel titleLabel = new JLabel("Settings");
    titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
    titleLabel.setForeground(Color.WHITE);
    titlePanel.add(titleLabel);

    // Content box (like the white form in My Account tab)
    JPanel contentBox = new JPanel();
    contentBox.setBackground(Color.WHITE);
    contentBox.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
    contentBox.setLayout(new BoxLayout(contentBox, BoxLayout.Y_AXIS));
    contentBox.setPreferredSize(new Dimension(400, 100));
    contentBox.setAlignmentX(Component.CENTER_ALIGNMENT);

    // Add a checkbox
    JCheckBox darkModeCheck = new JCheckBox("Dark Mode");
    darkModeCheck.setBackground(Color.WHITE);
    darkModeCheck.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    darkModeCheck.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    // Wrap checkbox inside a panel for alignment
    JPanel checkboxPanel = new JPanel();
    checkboxPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    checkboxPanel.setBackground(Color.WHITE);
    checkboxPanel.add(darkModeCheck);

    contentBox.add(Box.createVerticalStrut(20));  // Spacing
    contentBox.add(checkboxPanel);
    contentBox.add(Box.createVerticalStrut(20));

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

    public void signOut() {
        System.out.println("Signing out...");
        view.dispose();
    }

    // Action handlers made public for potential external triggering
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

    public void handleFilter() {
        System.out.println("Filter button clicked");
        JOptionPane.showMessageDialog(view, "Filter clicked! Implement filter options.");
    }
    // NEW: Handler for adding a new vacancy
    public void handleAddVacancy() {
        System.out.println("Add New Vacancy button clicked");
        JOptionPane.showMessageDialog(view, "Add New Vacancy clicked! Implement form to add vacancy.");
    }

    // NEW: Handler for vacancy search
    public void handleVacancySearch(String query) {
        System.out.println("Vacancy search with query: " + query);
        JOptionPane.showMessageDialog(view, "Searching vacancies for: " + query);
    }

    // NEW: Handler for vacancy filter
    public void handleVacancyFilter(String status) {
        System.out.println("Filtering vacancies by status: " + status);
        JOptionPane.showMessageDialog(view, "Filtering by status: " + status);
    }

    // Internal utility method remains private
    private void updateContentPanel(JPanel newPanel) {
        view.content.removeAll();
        view.content.setLayout(new java.awt.BorderLayout());
        view.content.add(newPanel, java.awt.BorderLayout.CENTER);
        view.content.revalidate();
        view.content.repaint();
    }

    // Public method to update user information
    public void setUserInfo(String username, String email) {
        view.username.setText(username);
        view.email.setText(email);
    }
}