import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

public class ModernCalculator extends JFrame implements ActionListener {

    private JTextField displayField;
    private double num1 = 0, num2 = 0, result = 0;
    private char operator;

    // Colors
    private final Color COLOR_BG = new Color(33, 37, 41); // Dark Gray
    private final Color COLOR_DISPLAY = new Color(52, 58, 64); // Slightly lighter gray
    private final Color COLOR_BTN_NUM = new Color(73, 80, 87); // Gray for numbers
    private final Color COLOR_BTN_OP = new Color(255, 107, 107); // Vibrant Red/Orange for Ops
    private final Color COLOR_BTN_EQ = new Color(51, 154, 240); // Vibrant Blue for Equals
    private final Color COLOR_TEXT = Color.WHITE;

    public ModernCalculator() {
        setTitle("Koketso Calculator");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // Display Screen
        displayField = new JTextField();
        displayField.setFont(new Font("Segoe UI", Font.BOLD, 48));
        displayField.setEditable(false);
        displayField.setHorizontalAlignment(SwingConstants.RIGHT);
        displayField.setBackground(COLOR_DISPLAY);
        displayField.setForeground(COLOR_TEXT);
        displayField.setBorder(new EmptyBorder(20, 20, 20, 20));
        add(displayField, BorderLayout.NORTH);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 4, 10, 10));
        buttonPanel.setBackground(COLOR_BG);
        buttonPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        String[] buttons = {
                "C", "+/-", "%", "/",
                "7", "8", "9", "*",
                "4", "5", "6", "-",
                "1", "2", "3", "+",
                "0", ".", "=", ""
        };

        for (String text : buttons) {
            if (text.isEmpty()) continue; // Skip empty slots

            JButton btn = createModernButton(text);
            
            // Assign functionality based on text (for special button spanning if needed, but grid makes it even)
            if (text.equals("=")) {
                btn.setBackground(COLOR_BTN_EQ);
            } else if ("C".equals(text) || "+/-".equals(text) || "%".equals(text) || "/".equals(text) || "*".equals(text) || "-".equals(text) || "+".equals(text)) {
                btn.setBackground(COLOR_BTN_OP);
            }

            btn.addActionListener(this);
            buttonPanel.add(btn);
        }

        add(buttonPanel, BorderLayout.CENTER);
        getContentPane().setBackground(COLOR_BG);
    }

    private JButton createModernButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 24));
        btn.setFocusPainted(false);
        btn.setForeground(COLOR_TEXT);
        btn.setBackground(COLOR_BTN_NUM);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover Effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(btn.getBackground().brighter());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (text.equals("=")) {
                    btn.setBackground(COLOR_BTN_EQ);
                } else if ("C".equals(text) || "+/-".equals(text) || "%".equals(text) || "/".equals(text) || "*".equals(text) || "-".equals(text) || "+".equals(text)) {
                    btn.setBackground(COLOR_BTN_OP);
                } else {
                    btn.setBackground(COLOR_BTN_NUM);
                }
            }
        });

        return btn;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        String currentText = displayField.getText();

        try {
            // Number and decimal point input
            if ((command.charAt(0) >= '0' && command.charAt(0) <= '9')) {
                displayField.setText(currentText + command);
            } 
            else if (command.equals(".")) {
                // Only add decimal if there isn't one already
                if (!currentText.contains(".")) {
                    if (currentText.isEmpty()) {
                        displayField.setText("0.");
                    } else {
                        displayField.setText(currentText + ".");
                    }
                }
            }
            // Clear button
            else if (command.equals("C")) {
                displayField.setText("");
                num1 = num2 = result = 0;
                operator = '\0';
            }
            // Equals button
            else if (command.equals("=")) {
                if (!currentText.isEmpty() && operator != '\0') {
                    num2 = Double.parseDouble(currentText);
                    
                    switch (operator) {
                        case '+': result = num1 + num2; break;
                        case '-': result = num1 - num2; break;
                        case '*': result = num1 * num2; break;
                        case '/': 
                            if (num2 != 0) {
                                result = num1 / num2;
                            } else {
                                JOptionPane.showMessageDialog(this, "Cannot divide by zero", "Error", JOptionPane.ERROR_MESSAGE);
                                displayField.setText("");
                                return;
                            }
                            break;
                        case '%': result = num1 % num2; break;
                    }
                    
                    // Format the result to remove unnecessary decimals
                    if (result == (long) result) {
                        displayField.setText(String.valueOf((long) result));
                    } else {
                        displayField.setText(String.format("%.8f", result).replaceAll("0*$", "").replaceAll("\\.$", ""));
                    }
                    
                    num1 = result;
                    operator = '\0';
                }
            }
            // Plus/Minus toggle
            else if (command.equals("+/-")) {
                if (!currentText.isEmpty() && !currentText.equals("0")) {
                    double val = Double.parseDouble(currentText);
                    val *= -1;
                    
                    // Format the result
                    if (val == (long) val) {
                        displayField.setText(String.valueOf((long) val));
                    } else {
                        displayField.setText(String.valueOf(val));
                    }
                }
            }
            // Operator buttons (+, -, *, /, %)
            else {
                if (!currentText.isEmpty()) {
                    // If there's a pending operation, calculate it first
                    if (operator != '\0' && num1 != 0) {
                        num2 = Double.parseDouble(currentText);
                        
                        switch (operator) {
                            case '+': result = num1 + num2; break;
                            case '-': result = num1 - num2; break;
                            case '*': result = num1 * num2; break;
                            case '/': 
                                if (num2 != 0) {
                                    result = num1 / num2;
                                } else {
                                    JOptionPane.showMessageDialog(this, "Cannot divide by zero", "Error", JOptionPane.ERROR_MESSAGE);
                                    displayField.setText("");
                                    operator = '\0';
                                    num1 = 0;
                                    return;
                                }
                                break;
                            case '%': result = num1 % num2; break;
                        }
                        
                        num1 = result;
                        
                        // Format and display the intermediate result
                        if (result == (long) result) {
                            displayField.setText(String.valueOf((long) result));
                        } else {
                            displayField.setText(String.format("%.8f", result).replaceAll("0*$", "").replaceAll("\\.$", ""));
                        }
                    } else {
                        num1 = Double.parseDouble(currentText);
                    }
                    
                    operator = command.charAt(0);
                    displayField.setText("");
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
            displayField.setText("");
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {}
        
        SwingUtilities.invokeLater(() -> {
            new ModernCalculator().setVisible(true);
        });
    }
}
