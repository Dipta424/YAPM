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

public class RegisterPanel extends JPanel {

    public RegisterPanel(MainUI mainUI) {
        setLayout(new BorderLayout());

        Color darkBg = UIManager.getColor("Panel.background");
        Color formBorderColor = UIManager.getColor("Component.borderColor");
        Color textColor = UIManager.getColor("Label.foreground");
        Color labelColor = UIManager.getColor("Label.disabledForeground");
        Color accentColor = UIManager.getColor("Component.focusColor");

        JLabel header = new JLabel("YAPM - Register", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.setOpaque(true);
        header.setBackground(darkBg.darker());
        header.setForeground(textColor);
        header.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(header, BorderLayout.NORTH);

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(darkBg);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(darkBg);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(formBorderColor),
                new EmptyBorder(20, 20, 20, 20)));
        formPanel.setMaximumSize(new Dimension(500, Integer.MAX_VALUE));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(labelColor);
        usernameLabel.setAlignmentX(LEFT_ALIGNMENT);
        usernameLabel.setFont(usernameLabel.getFont().deriveFont(Font.PLAIN, 18f));
        usernameLabel.setBorder(new EmptyBorder(0, 0, 5, 0));

        JTextField usernameField = new JTextField();
        usernameField.putClientProperty(FlatClientProperties.STYLE, "font: 18");
        usernameField.setFont(usernameField.getFont().deriveFont(18f));
        usernameField.setPreferredSize(new Dimension(450, 40));
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        usernameField.setAlignmentX(LEFT_ALIGNMENT);
        usernameField.setBorder(BorderFactory.createEmptyBorder());
        usernameField.setMargin(new Insets(0, 0, 0, 0));

        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(Box.createVerticalStrut(10));

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(labelColor);
        emailLabel.setAlignmentX(LEFT_ALIGNMENT);
        emailLabel.setFont(emailLabel.getFont().deriveFont(Font.PLAIN, 18f));
        emailLabel.setBorder(new EmptyBorder(0, 0, 5, 0));

        JTextField emailField = new JTextField();
        emailField.putClientProperty(FlatClientProperties.STYLE, "font: 18");
        emailField.setFont(emailField.getFont().deriveFont(18f));
        emailField.setPreferredSize(new Dimension(450, 40));
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        emailField.setAlignmentX(LEFT_ALIGNMENT);
        emailField.setBorder(BorderFactory.createEmptyBorder());
        emailField.setMargin(new Insets(0, 0, 0, 0));

        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(Box.createVerticalStrut(10));

        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(labelColor);
        passLabel.setAlignmentX(LEFT_ALIGNMENT);
        passLabel.setFont(passLabel.getFont().deriveFont(Font.PLAIN, 18f));
        passLabel.setBorder(new EmptyBorder(0, 0, 5, 0));

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

        JButton registerButton = new JButton("Register");
        registerButton.setAlignmentX(LEFT_ALIGNMENT);
        registerButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        registerButton.setPreferredSize(new Dimension(450, 35));

        registerButton.addActionListener(e -> {
            String uname = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String pwd = new String(passField.getPassword()).trim();

            DBConnection db = new DBConnection();
            RegisterUser reg = new RegisterUser(db);

            BackendError response = reg.setUsername(uname);
            if (response != null) {
                JOptionPane.showMessageDialog(
                        RegisterPanel.this,
                        "Error setting username: " + response.getErrorType(),
                        "Registration Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            response = reg.setEmail(email);
            if (response != null) {
                JOptionPane.showMessageDialog(
                        RegisterPanel.this,
                        "Error setting email: " + response.getErrorType(),
                        "Registration Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            response = reg.setPassword(pwd);
            if (response != null) {
                JOptionPane.showMessageDialog(
                        RegisterPanel.this,
                        "Error setting password: " + response.getErrorType(),
                        "Registration Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            response = reg.register();
            if (response == null) {
                JOptionPane.showMessageDialog(
                        RegisterPanel.this,
                        "Successfully registered!",
                        "Registration Successful",
                        JOptionPane.INFORMATION_MESSAGE
                );
                mainUI.showPage("login");
            } else {
                JOptionPane.showMessageDialog(
                        RegisterPanel.this,
                        "Failed to register: " + response.getErrorType(),
                        "Registration Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        formPanel.add(registerButton);

        formPanel.add(Box.createVerticalStrut(15));

        JLabel promptLabel = new JLabel("Already have an account?");
        promptLabel.setForeground(labelColor);
        promptLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        promptLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(promptLabel);

        JLabel loginNowLabel = new JLabel("Login now");
        loginNowLabel.setForeground(accentColor);
        loginNowLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginNowLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginNowLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        loginNowLabel.addMouseListener(new MouseAdapter() {
            Font originalFont = loginNowLabel.getFont();

            @Override
            public void mouseClicked(MouseEvent e) {
                mainUI.showPage("login");
            }

            @Override
            @SuppressWarnings("unchecked")
            public void mouseEntered(MouseEvent e) {
                Map<TextAttribute, Object> attributes = (Map<TextAttribute, Object>) originalFont.getAttributes();
                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                loginNowLabel.setFont(originalFont.deriveFont(attributes));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginNowLabel.setFont(originalFont);
            }
        });

        formPanel.add(loginNowLabel);

        centerWrapper.add(formPanel);
        add(centerWrapper, BorderLayout.CENTER);

        JLabel footer = new JLabel("© 2025 All rights reserved.", SwingConstants.CENTER);
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footer.setOpaque(true);
        footer.setBackground(darkBg.darker());
        footer.setForeground(textColor);
        footer.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(footer, BorderLayout.SOUTH);
    }
}
