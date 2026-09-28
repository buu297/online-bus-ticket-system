package com.busticket.ui.components;

import com.busticket.model.Passenger;
import com.busticket.ui.theme.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Modular component for collecting passenger contact and pickup info.
 */
public class PassengerFormPanel extends JPanel {
    private JTextField nameField;
    private JTextField phoneField;
    private JTextField emailField;
    private JTextField pickupField;

    public PassengerFormPanel() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(6, 6));
        setBackground(UITheme.BG_CARD);
        setBorder(UITheme.createCardBorder());

        JLabel title = new JLabel("3. Thông Tin Hành Khách");
        title.setFont(UITheme.FONT_SUBHEADER);
        title.setForeground(UITheme.PRIMARY);
        add(title, BorderLayout.NORTH);

        JPanel formGrid = new JPanel(new GridLayout(4, 2, 8, 8));
        formGrid.setOpaque(false);
        formGrid.setBorder(new EmptyBorder(6, 0, 0, 0));

        formGrid.add(createLabel("Họ và tên (*):"));
        nameField = new JTextField("Lê Văn Bửu");
        nameField.setFont(UITheme.FONT_REGULAR);
        formGrid.add(nameField);

        formGrid.add(createLabel("Số điện thoại (*):"));
        phoneField = new JTextField("0987654321");
        phoneField.setFont(UITheme.FONT_REGULAR);
        formGrid.add(phoneField);

        formGrid.add(createLabel("Email:"));
        emailField = new JTextField("levanbuu2907@gmail.com");
        emailField.setFont(UITheme.FONT_REGULAR);
        formGrid.add(emailField);

        formGrid.add(createLabel("Điểm đón:"));
        pickupField = new JTextField("Bến xe trung tâm (hoặc đón tận nơi)");
        pickupField.setFont(UITheme.FONT_REGULAR);
        formGrid.add(pickupField);

        add(formGrid, BorderLayout.CENTER);
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(UITheme.FONT_BOLD);
        lbl.setForeground(UITheme.TEXT_DARK);
        return lbl;
    }

    public Passenger getPassenger() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String pickup = pickupField.getText().trim();

        if (name.isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập họ và tên hành khách!");
        }
        if (phone.isEmpty() || !phone.matches("^[0-9+]{9,12}$")) {
            throw new IllegalArgumentException("Số điện thoại không hợp lệ (9-12 chữ số)!");
        }

        return new Passenger(name, phone, email, pickup.isEmpty() ? "Bến xe chính" : pickup);
    }

    public void clear() {
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        pickupField.setText("");
    }
}
