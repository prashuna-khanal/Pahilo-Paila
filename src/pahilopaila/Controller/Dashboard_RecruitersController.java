package pahilopaila.Controller;

import pahilopaila.view.Dashboard_Recruiters;
import pahilopaila.view.LoginPageview;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Dashboard_RecruitersController {
    private final Dashboard_Recruiters view;
    private boolean isVacancyPosted = false;

    public Dashboard_RecruitersController(Dashboard_Recruiters view, int i) {
        this.view = view;
        initializeListeners();
        showDashboardPanel();
    }

    private void initializeListeners() {
        // Handled in view via createStyledLabel
        view.Searchfield.addActionListener(this::searchFieldActionPerformed);
        view.jButton1.addActionListener(this::searchButtonActionPerformed);
        view.jButton2.addActionListener(this::filterButtonActionPerformed);
        view.getStarted.addActionListener(this::getStartedActionPerformed);
        view.learnMore.addActionListener(this::learnMoreActionPerformed);
    }

    private void updateContentPanel(JPanel panel) {
        view.getContentPanel().removeAll();
        view.getContentPanel().setLayout(new BorderLayout());
        view.getContentPanel().add(panel, BorderLayout.CENTER);
        view.getContentPanel().revalidate();
        view.getContentPanel().repaint();
    }

    public void showDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));

        JPanel messagePanel = new JPanel();
        messagePanel.setBackground(new Color(0, 4, 80));
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel find = new JLabel("Find the right people");
        find.setFont(new Font("Segoe UI Symbol", Font.BOLD, 24));
        find.setForeground(Color.WHITE);
        messagePanel.add(find);

        JLabel right = new JLabel("for the right Job");
        right.setFont(new Font("Segoe UI Symbol", Font.BOLD, 24));
        right.setForeground(Color.WHITE);
        messagePanel.add(right);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(new Color(0, 4, 80));
        JButton getStarted = new JButton("Get Started");
        getStarted.setForeground(new Color(0, 0, 102));
        getStarted.addActionListener(this::getStartedActionPerformed);
        buttonPanel.add(getStarted);

        JButton learnMore = new JButton("Learn More");
        learnMore.setForeground(Color.WHITE);
        learnMore.setBackground(new Color(0, 4, 80));
        learnMore.setBorder(BorderFactory.createTitledBorder(""));
        learnMore.addActionListener(this::learnMoreActionPerformed);
        buttonPanel.add(learnMore);

        messagePanel.add(buttonPanel);
        panel.add(messagePanel, BorderLayout.NORTH);

        updateContentPanel(panel);
    }

    public void showVacancyPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

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
        headerPanel.setPreferredSize(new Dimension(680, 70));
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 25, 20));

        JLabel headerLabel = new JLabel("Vacancy Management");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        JPanel centerWrapper = new JPanel();
        centerWrapper.setBackground(new Color(245, 245, 245));
        centerWrapper.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(252, 252, 252));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(20, 20, 20, 20),
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel jobTitleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        jobTitleRow.setBackground(new Color(252, 252, 252));
        JLabel jobTitleIcon = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/Image/logo/job.png"));
            Image scaledImage = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            jobTitleIcon.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            System.out.println("Error loading job title icon: " + e.getMessage());
            jobTitleIcon.setText("J");
        }
        jobTitleRow.add(jobTitleIcon);
        JLabel jobTitleLabel = new JLabel("Job Title:");
        jobTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        jobTitleLabel.setForeground(new Color(0, 0, 102));
        jobTitleRow.add(jobTitleLabel);
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(jobTitleRow, gbc);

        JTextField jobTitleField = new JTextField(25);
        jobTitleField.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        jobTitleField.setBackground(new Color(245, 245, 245));
        jobTitleField.setPreferredSize(new Dimension(jobTitleField.getPreferredSize().width, 25));
        jobTitleField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(jobTitleField, gbc);

        JLabel statusLabel = new JLabel(isVacancyPosted ? "Vacancy posted successfully!" : "Vacancy not posted yet.");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(isVacancyPosted ? Color.GREEN : Color.RED);
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(statusLabel, gbc);

        JButton postButton = new JButton("Post Vacancy") {
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
        postButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        postButton.setForeground(Color.WHITE);
        postButton.setContentAreaFilled(false);
        postButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        postButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        postButton.setFocusPainted(false);
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(postButton, gbc);

        postButton.addActionListener(e -> {
            String jobTitle = jobTitleField.getText().trim();
            if (!jobTitle.isEmpty()) {
                isVacancyPosted = true;
                statusLabel.setText("Vacancy posted successfully!");
                statusLabel.setForeground(Color.GREEN);
                System.out.println("Vacancy posted: " + jobTitle);
            } else {
                statusLabel.setText("Please enter a job title.");
                statusLabel.setForeground(Color.RED);
            }
            formPanel.revalidate();
            formPanel.repaint();
        });

        centerWrapper.add(formPanel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerWrapper, BorderLayout.CENTER);

        updateContentPanel(mainPanel);
    }

    public void showApplicationsPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(245, 245, 245));
        panel.add(new JLabel("Applications Panel"));
        updateContentPanel(panel);
    }

    public void showSettingsPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(245, 245, 245));
        panel.add(new JLabel("Settings Panel"));
        updateContentPanel(panel);

    }
}

    public void showMyAccountPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setPreferredSize(new Dimension(660, 500));

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
        headerPanel.setPreferredSize(new Dimension(680, 70));
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 25, 20));

        JLabel headerLabel = new JLabel("My Account");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        JPanel centerWrapper = new JPanel();
        centerWrapper.setBackground(new Color(245, 245, 245));
        centerWrapper.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(252, 252, 252));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 2, new Color(180, 180, 180, 100)),
            BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20),
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true)
            )
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel usernameRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        usernameRow.setBackground(new Color(252, 252, 252));
        JLabel usernameIcon = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/Image/profile-user.png"));
            Image scaledImage = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            usernameIcon.setIcon(new ImageIcon(scaledImage));
            usernameIcon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        } catch (Exception e) {
            System.out.println("Error loading username icon: " + e.getMessage());
            usernameIcon.setText("U");
        }
        usernameRow.add(usernameIcon);
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        usernameLabel.setForeground(new Color(0, 0, 102));
        usernameRow.add(usernameLabel);
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(usernameRow, gbc);

        JTextField usernameField = new JTextField(25);
        usernameField.setText(view.username.getText());
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        usernameField.setBackground(new Color(245, 245, 245));
        usernameField.setPreferredSize(new Dimension(usernameField.getPreferredSize().width, 25));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(usernameField, gbc);

        JPanel passwordRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        passwordRow.setBackground(new Color(252, 252, 252));
        JLabel passwordIcon = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/Image/locked-computer.png"));
            Image scaledImage = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            passwordIcon.setIcon(new ImageIcon(scaledImage));
            passwordIcon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        } catch (Exception e) {
            System.out.println("Error loading password icon: " + e.getMessage());
            passwordIcon.setText("P");
        }
        passwordRow.add(passwordIcon);
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        passwordLabel.setForeground(new Color(0, 0, 102));
        passwordRow.add(passwordLabel);
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(passwordRow, gbc);

        JPasswordField passwordField = new JPasswordField(25);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        passwordField.setBackground(new Color(245, 245, 245));
        passwordField.setPreferredSize(new Dimension(passwordField.getPreferredSize().width, 25));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(passwordField, gbc);

        JPanel newPasswordRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        newPasswordRow.setBackground(new Color(252, 252, 252));
        JLabel newPasswordIcon = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/Image/locked-computer.png"));
            Image scaledImage = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            newPasswordIcon.setIcon(new ImageIcon(scaledImage));
            newPasswordIcon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        } catch (Exception e) {
            System.out.println("Error loading new password icon: " + e.getMessage());
            newPasswordIcon.setText("NP");
        }
        newPasswordRow.add(newPasswordIcon);
        JLabel changePasswordLabel = new JLabel("New Password:");
        changePasswordLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        changePasswordLabel.setForeground(new Color(0, 0, 102));
        newPasswordRow.add(changePasswordLabel);
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(newPasswordRow, gbc);

        JPasswordField changePasswordField = new JPasswordField(25);
        changePasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        changePasswordField.setBackground(new Color(245, 245, 245));
        changePasswordField.setPreferredSize(new Dimension(changePasswordField.getPreferredSize().width, 25));
        changePasswordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(changePasswordField, gbc);

        JButton updateButton = new JButton("Update") {
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
        updateButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        updateButton.setForeground(Color.WHITE);
        updateButton.setContentAreaFilled(false);
        updateButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        updateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        updateButton.setFocusPainted(false);
        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(updateButton, gbc);

        updateButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String newPassword = new String(changePasswordField.getPassword());
            if (username.isEmpty() || password.isEmpty() || newPassword.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Add database update logic here
            JOptionPane.showMessageDialog(view, "User info updated!\nUsername: " + username);
        });

        centerWrapper.add(formPanel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerWrapper, BorderLayout.CENTER);

        updateContentPanel(mainPanel);
    }

    public void logout() {
        view.dispose();
        LoginPageview loginView = new LoginPageview();
        LoginController loginController = new LoginController(loginView);
        loginController.open();
    }

    public void searchFieldActionPerformed(ActionEvent evt) {
        System.out.println("Search: " + view.Searchfield.getText());
    }

    public void searchButtonActionPerformed(ActionEvent evt) {
        System.out.println("Search button clicked");
    }

    public void filterButtonActionPerformed(ActionEvent evt) {
        System.out.println("Filter button clicked");
    }

    public void getStartedActionPerformed(ActionEvent evt) {
        System.out.println("Get Started clicked");
    }

    public void learnMoreActionPerformed(ActionEvent evt) {
        System.out.println("Learn More clicked");
    }

    public void open() {
        view.setVisible(true);
    }

    public void setUserInfo(String username, String email) {
        view.setUserInfo(username, email);
    }
}