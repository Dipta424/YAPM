package org.YAPM;
import org.backend.*;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.util.Map;

public class LoginPanel extends JPanel {

    public LoginPanel(MainUI mainUI) {
        setLayout(new BorderLayout());

        // Use FlatLaf Nord colors from UIManager
        Color darkBg = UIManager.getColor("Panel.background");
        Color formBorderColor = UIManager.getColor("Component.borderColor");
        Color textColor = UIManager.getColor("Label.foreground");
        Color labelColor = UIManager.getColor("Label.disabledForeground");
        Color accentColor = UIManager.getColor("Component.focusColor");

        // Header
        JLabel header = new JLabel("YAPM - Login", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.setOpaque(true);
        header.setBackground(darkBg.darker());
        header.setForeground(textColor);
        header.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(header, BorderLayout.NORTH);

        // Center wrapper
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(darkBg);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(darkBg);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(formBorderColor),
                new EmptyBorder(20, 20, 20, 20))
        );
        formPanel.setMaximumSize(new Dimension(500, Integer.MAX_VALUE));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Username/Email label
        JLabel emailLabel = new JLabel("Username/Email:");
        emailLabel.setForeground(labelColor);
        emailLabel.setAlignmentX(LEFT_ALIGNMENT);
        emailLabel.setFont(emailLabel.getFont().deriveFont(Font.PLAIN, 18f));
        emailLabel.setBorder(new EmptyBorder(0, 0, 5, 0));

        // Username/Email field
        JTextField usernameEmailField = new JTextField();
        usernameEmailField.putClientProperty(FlatClientProperties.STYLE, "font: 18");
        usernameEmailField.setFont(usernameEmailField.getFont().deriveFont(18f));
        usernameEmailField.setPreferredSize(new Dimension(450, 40));
        usernameEmailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        usernameEmailField.setAlignmentX(LEFT_ALIGNMENT);
        usernameEmailField.setBorder(BorderFactory.createEmptyBorder());
        usernameEmailField.setMargin(new Insets(0, 0, 0, 0));

        formPanel.add(emailLabel);
        formPanel.add(usernameEmailField);
        formPanel.add(Box.createVerticalStrut(10));

        // Password label
        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(labelColor);
        passLabel.setAlignmentX(LEFT_ALIGNMENT);
        passLabel.setFont(passLabel.getFont().deriveFont(Font.PLAIN, 18f));
        passLabel.setBorder(new EmptyBorder(0, 0, 5, 0));

        // Password field
        JPasswordField passField = new JPasswordField();
        passField.putClientProperty(FlatClientProperties.STYLE, "font: 18");
        passField.setFont(passField.getFont().deriveFont(18f));
        passField.setPreferredSize(new Dimension(450, 40));
        passField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        passField.setAlignmentX(LEFT_ALIGNMENT);
        passField.setBorder(BorderFactory.createEmptyBorder());
        passField.setMargin(new Insets(0, 0, 0, 0));

        formPanel.add(passLabel);
        formPanel.add(passField);
        formPanel.add(Box.createVerticalStrut(20));

        // Add this after passField and the vertical strut, before loginButton in formPanel

        JLabel rulesLabel = new JLabel("View username and password rules");
        rulesLabel.setForeground(accentColor);
        rulesLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        rulesLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        rulesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        rulesLabel.addMouseListener(new MouseAdapter() {
            Font originalFont = rulesLabel.getFont();

            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(
                        LoginPanel.this,
                        """
                        Username/Email Rules:
                        - Cannot be empty
                        
                        Password Rules:
                        - Minimum 8 characters
                        - Include uppercase and lowercase letters
                        - Include digits and special characters
                        """,
                        "Login Rules",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

            @Override
            @SuppressWarnings("unchecked")
            public void mouseEntered(MouseEvent e) {
                Map<TextAttribute, Object> attributes = (Map<TextAttribute, Object>) originalFont.getAttributes();
                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                rulesLabel.setFont(originalFont.deriveFont(attributes));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                rulesLabel.setFont(originalFont);
            }
        });

        formPanel.add(rulesLabel);
        formPanel.add(Box.createVerticalStrut(10));


        // Login Button
        JButton loginButton = new JButton("Login");
        loginButton.setAlignmentX(LEFT_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        loginButton.setPreferredSize(new Dimension(450, 35));
        loginButton.addActionListener(e -> {
            String uname = usernameEmailField.getText().trim();
            String email = usernameEmailField.getText().trim(); // Email field is not separate in UI, so reuse
            String pwd = new String(passField.getPassword()).trim();



            DBConnection db = new DBConnection();
            LoginUser log = new LoginUser(db, uname, pwd);

            BackendError resp = log.login();
            if (resp != null) {
                JOptionPane.showMessageDialog(
                        LoginPanel.this,
                        "Failed to login: " + resp.getErrorType(),
                        "Login Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            String dbPath;
            resp = log.verifyDbFilePath();

            if (resp != null && resp.getErrorType() == BackendError.ErrorTypes.DbFileDoesNotExist) {
                BackendError newDbCreationResponse = log.getNewDbFilePath();
                if (newDbCreationResponse != null) {
                    JOptionPane.showMessageDialog(
                            LoginPanel.this,
                            "Failed to create vault file: " + newDbCreationResponse.getErrorType(),
                            "Vault Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
            } else if (resp != null) {
                JOptionPane.showMessageDialog(
                        LoginPanel.this,
                        "Failed to verify db file path: " + resp.getErrorType(),
                        "Vault Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            dbPath = log.getDbFilePath();

            mainUI.setVaultCredentials(dbPath, pwd);
            mainUI.showPage("home");

        });

        formPanel.add(loginButton);

        formPanel.add(Box.createVerticalStrut(15));

        // Register label
        JLabel promptLabel = new JLabel("Don't have an account?");
        promptLabel.setForeground(labelColor);
        promptLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        promptLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(promptLabel);

        // "Register now" link-style label
        JLabel registerNowLabel = new JLabel("Register now");
        registerNowLabel.setForeground(accentColor);
        registerNowLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerNowLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerNowLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Manual underline toggle with TextAttribute
        registerNowLabel.addMouseListener(new MouseAdapter() {
            Font originalFont = registerNowLabel.getFont();

            @Override
            public void mouseClicked(MouseEvent e) {
                mainUI.showPage("register");
            }

            @Override
            @SuppressWarnings("unchecked")
            public void mouseEntered(MouseEvent e) {
                Map<TextAttribute, Object> attributes = (Map<TextAttribute, Object>) originalFont.getAttributes();
                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                registerNowLabel.setFont(originalFont.deriveFont(attributes));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                registerNowLabel.setFont(originalFont);
            }
        });

        formPanel.add(registerNowLabel);

        centerWrapper.add(formPanel);
        add(centerWrapper, BorderLayout.CENTER);

        // Footer
        JLabel footer = new JLabel("© 2025 All rights reserved.", SwingConstants.CENTER);
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footer.setOpaque(true);
        footer.setBackground(darkBg.darker());
        footer.setForeground(textColor);
        footer.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(footer, BorderLayout.SOUTH);
    }
}
