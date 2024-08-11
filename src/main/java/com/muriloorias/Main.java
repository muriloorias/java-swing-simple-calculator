package com.muriloorias;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Stack;

public class Main {
    public static void main(String[] args) {
        String titleTxt = "Calculadora";
        String descriptionTxt = "Olá usuário, coloque a conta no input e aperte no botão e veja a mágica acontecer!";
        String submitTxt = "Submeter conta";
        int width = 800;
        int height = 600;
        int titleSize = 30;
        JFrame frame = new JFrame(titleTxt);
        frame.setMinimumSize(new Dimension(width, height));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel titleLbl = new JLabel(titleTxt);
        titleLbl.setFont(new Font("Arial", Font.BOLD, titleSize));
        titleLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descriptionLbl = new JLabel(descriptionTxt);
        descriptionLbl.setFont(new Font("Arial", Font.PLAIN, 20));
        descriptionLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(titleLbl);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(descriptionLbl);

        JTextField accountField = new JTextField();
        accountField.setPreferredSize(new Dimension(200, 30));
        accountField.setMaximumSize(accountField.getPreferredSize());

        JButton submitBtn = new JButton(submitTxt);
        submitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel resultLbl = new JLabel("");
        resultLbl.setFont(new Font("Arial", Font.PLAIN, 20));
        resultLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        submitBtn.addActionListener(e -> {
            String input = accountField.getText();
            try {
                double result = evaluateExpression(input);
                resultLbl.setText("Resultado: " + result);
            } catch (Exception ex) {
                resultLbl.setText("Erro na expressão");
            }
        });

        panel.add(accountField);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(submitBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(resultLbl);

        frame.add(panel);
        frame.setVisible(true);
    }

    private static double evaluateExpression(String expression) throws Exception {
        // Handle simple arithmetic expressions only
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);

            if (Character.isWhitespace(ch)) {
                continue;
            }

            if (Character.isDigit(ch)) {
                StringBuilder sb = new StringBuilder();
                while (i < expression.length() && Character.isDigit(expression.charAt(i))) {
                    sb.append(expression.charAt(i++));
                }
                i--;
                numbers.push(Double.parseDouble(sb.toString()));
            } else if (ch == '+' || ch == '-' || ch == '*' || ch == '/') {
                while (!operators.isEmpty() && precedence(ch) <= precedence(operators.peek())) {
                    processOperator(numbers, operators);
                }
                operators.push(ch);
            }
        }

        while (!operators.isEmpty()) {
            processOperator(numbers, operators);
        }

        return numbers.pop();
    }

    private static void processOperator(Stack<Double> numbers, Stack<Character> operators) throws Exception {
        char operator = operators.pop();
        double right = numbers.pop();
        double left = numbers.pop();
        switch (operator) {
            case '+':
                numbers.push(left + right);
                break;
            case '-':
                numbers.push(left - right);
                break;
            case '*':
                numbers.push(left * right);
                break;
            case '/':
                if (right == 0) {
                    throw new ArithmeticException("Divisão por zero");
                }
                numbers.push(left / right);
                break;
            default:
                throw new Exception("Operador inválido");
        }
    }

    private static int precedence(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return -1;
        }
    }
}