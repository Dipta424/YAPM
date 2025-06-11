package org.YAPM;

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

        // Login Button
        JButton loginButton = new JButton("Login");
        loginButton.setAlignmentX(LEFT_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        loginButton.setPreferredSize(new Dimension(450, 35));
        //loginButton.addActionListener(e -> mainUI.showPage("home"));
        loginButton.addActionListener(e -> {
            String input = usernameEmailField.getText().trim();
            String password = new String(passField.getPassword()).trim();

            if (input.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both username/email and password.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean isEmail = input.contains("@") && input.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");


            boolean isUsername = input.matches("^[a-zA-Z0-9_]{3,20}$");

            if (!isEmail && !isUsername) {
                JOptionPane.showMessageDialog(this, "Enter a valid email or alphanumeric username (3-20 chars).", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (password.length() < 6) {
                JOptionPane.showMessageDialog(this, "Password must be at least 6 characters.", "Invalid Password", JOptionPane.WARNING_MESSAGE);
                return;
            }



            // At this point, input is valid
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
