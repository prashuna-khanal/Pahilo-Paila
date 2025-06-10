package pahilopaila.Controller; // Matches pahilopaila/controller directory

import pahilopaila.view.Dashboard_Recruiters;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.LayoutStyle; // Added for LayoutStyle.ComponentPlacement

/**
 * Controller for the Dashboard_Recruiters view, handling user interactions and navigation.
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

        view.application.addMouseListener(new java.awt.event.MouseAdapter() {
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
        vacancyPanel.setLayout(new java.awt.BorderLayout());
        JLabel title = new JLabel("Manage Vacancies");
        title.setFont(new java.awt.Font("Segoe UI", 1, 18));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        vacancyPanel.add(title, java.awt.BorderLayout.NORTH);
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