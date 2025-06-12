package pahilopaila.Controller; // Matches pahilopaila/controller directory

import pahilopaila.view.Dashboard_Recruiters;

// Added for LayoutStyle.ComponentPlacement
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.table.DefaultTableModel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
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
        System.out.println("Navigating to Vacancy");
        JPanel vacancyPanel = new JPanel();
        vacancyPanel.setBackground(new java.awt.Color(245, 245, 245));
        vacancyPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel title = new JLabel("Manage Vacancies");
        title.setFont(new java.awt.Font("Segoe UI", 1, 18));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        vacancyPanel.add(title, gbc);

        // NEW: Add Vacancy Button
        JButton addVacancyButton = new JButton("Add New Vacancy");
        addVacancyButton.setForeground(new java.awt.Color(0, 0, 102));
        addVacancyButton.addActionListener(e -> handleAddVacancy());
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        vacancyPanel.add(addVacancyButton, gbc);

        // NEW: Search Field and Button
        JTextField vacancySearchField = new JTextField(20);
        JButton searchVacancyButton = new JButton("Search Vacancies");
        searchVacancyButton.addActionListener(e -> handleVacancySearch(vacancySearchField.getText()));
        JPanel searchPanel = new JPanel();
        searchPanel.add(vacancySearchField);
        searchPanel.add(searchVacancyButton);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        vacancyPanel.add(searchPanel, gbc);

        // NEW: Vacancy Table
        String[] columns = {"ID", "Title", "Department", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        // Sample data (replace with actual data source in production)
        Object[][] sampleData = {
            {1, "Software Engineer", "Engineering", "Open"},
            {2, "Project Manager", "Management", "Closed"},
            {3, "Data Analyst", "Analytics", "Open"}
        };
        for (Object[] row : sampleData) {
            tableModel.addRow(row);
        }
        JTable vacancyTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(vacancyTable);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        vacancyPanel.add(tableScrollPane, gbc);

        // NEW: Filter ComboBox
        String[] filterOptions = {"All", "Open", "Closed"};
        JComboBox<String> statusFilter = new JComboBox<>(filterOptions);
        statusFilter.addActionListener(e -> handleVacancyFilter((String) statusFilter.getSelectedItem()));
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0.0;
        vacancyPanel.add(statusFilter, gbc);

        updateContentPanel(vacancyPanel);
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
        JPanel settingsPanel = new JPanel();
        settingsPanel.setBackground(new java.awt.Color(245, 245, 245));
        settingsPanel.setLayout(new java.awt.BorderLayout());
        JLabel title = new JLabel("Settings");
        title.setFont(new java.awt.Font("Segoe UI", 1, 18));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        settingsPanel.add(title, java.awt.BorderLayout.NORTH);
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