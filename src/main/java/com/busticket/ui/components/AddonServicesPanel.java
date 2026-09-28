package com.busticket.ui.components;

import com.busticket.patterns.decorator.ExtraLuggageDecorator;
import com.busticket.patterns.decorator.InsuranceDecorator;
import com.busticket.patterns.decorator.MealDecorator;
import com.busticket.patterns.decorator.PriorityBoardingDecorator;
import com.busticket.ui.theme.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Modular component for selecting optional add-on services (Decorator Pattern).
 */
public class AddonServicesPanel extends JPanel {
    private final Runnable onAddonChanged;
    private JCheckBox insuranceCheck;
    private JCheckBox mealCheck;
    private JCheckBox luggageCheck;
    private JCheckBox priorityCheck;
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("vi-VN"));

    public AddonServicesPanel(Runnable onAddonChanged) {
        this.onAddonChanged = onAddonChanged;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(6, 6));
        setBackground(UITheme.BG_CARD);
        setBorder(UITheme.createCardBorder());

        JLabel title = new JLabel("4. Dịch Vụ Tiện Ích Gia Tăng (Decorator Pattern)");
        title.setFont(UITheme.FONT_SUBHEADER);
        title.setForeground(UITheme.PRIMARY);
        add(title, BorderLayout.NORTH);

        JPanel checkGrid = new JPanel(new GridLayout(2, 2, 10, 8));
        checkGrid.setOpaque(false);
        checkGrid.setBorder(new EmptyBorder(6, 0, 0, 0));

        insuranceCheck = createCheckBox("Bảo hiểm chuyến đi", InsuranceDecorator.PRICE);
        mealCheck = createCheckBox("Suất ăn & Nước ngọt", MealDecorator.PRICE);
        luggageCheck = createCheckBox("Thêm hành lý ký gửi (10kg)", ExtraLuggageDecorator.PRICE);
        priorityCheck = createCheckBox("Phòng chờ VIP & Ưu tiên", PriorityBoardingDecorator.PRICE);

        checkGrid.add(insuranceCheck);
        checkGrid.add(mealCheck);
        checkGrid.add(luggageCheck);
        checkGrid.add(priorityCheck);

        add(checkGrid, BorderLayout.CENTER);
    }

    private JCheckBox createCheckBox(String label, double price) {
        String fullText = String.format("<html><b>%s</b> <font color='#16A34A'>(+%s)</font></html>",
                label, CURRENCY_FORMAT.format(price));
        JCheckBox cb = new JCheckBox(fullText);
        cb.setFont(UITheme.FONT_REGULAR);
        cb.setOpaque(false);
        cb.setFocusPainted(false);
        cb.addActionListener(e -> {
            if (onAddonChanged != null) {
                onAddonChanged.run();
            }
        });
        return cb;
    }

    public boolean isInsuranceSelected() {
        return insuranceCheck.isSelected();
    }

    public boolean isMealSelected() {
        return mealCheck.isSelected();
    }

    public boolean isExtraLuggageSelected() {
        return luggageCheck.isSelected();
    }

    public boolean isPriorityBoardingSelected() {
        return priorityCheck.isSelected();
    }

    public void reset() {
        insuranceCheck.setSelected(false);
        mealCheck.setSelected(false);
        luggageCheck.setSelected(false);
        priorityCheck.setSelected(false);
    }
}
