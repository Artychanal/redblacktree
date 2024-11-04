import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class DatabaseGUI extends JFrame {
    private RedBlackTree tree;
    private JTextField emailField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextArea outputArea;

    public DatabaseGUI() {
        tree = new RedBlackTree();
        setTitle("СУБД на основі червоно-чорного дерева");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));

        inputPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        inputPanel.add(emailField);

        inputPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        inputPanel.add(usernameField);

        inputPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        inputPanel.add(passwordField);

        JButton addButton = new JButton("Додати");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RedBlackTree.User user = new RedBlackTree.User(emailField.getText(), usernameField.getText(), passwordField.getText());
                tree.insert(user);
                outputArea.append("Запис додано: " + user.toString() + "\n");
            }
        });

        JButton searchButton = new JButton("Пошук за ID");
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String idStr = JOptionPane.showInputDialog("Введіть ID для пошуку:");
                if (idStr != null) {
                    int key = Integer.parseInt(idStr);
                    RedBlackTree.User result = tree.search(key);
                    if (result != null) {
                        outputArea.append("Знайдено: " + result.toString() + "\n");
                    } else {
                        outputArea.append("Запис не знайдено.\n");
                    }
                }
            }
        });

        JButton deleteButton = new JButton("Видалити за ID");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String idStr = JOptionPane.showInputDialog("Введіть ID для видалення:");
                if (idStr != null) {
                    int key = Integer.parseInt(idStr);
                    tree.delete(key);  // Видалення запису за ID
                    outputArea.append("Запис з ID " + key + " видалено.\n");
                }
            }
        });

        JButton updateButton = new JButton("Оновити за ID");
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String idStr = JOptionPane.showInputDialog("Введіть ID для оновлення:");
                if (idStr != null) {
                    int key = Integer.parseInt(idStr);
                    RedBlackTree.User user = tree.search(key);
                    if (user != null) {
                        // Оновлення полів користувача новими значеннями
                        user.setEmail(emailField.getText());
                        user.setUsername(usernameField.getText());
                        user.setPassword(passwordField.getText());
                        outputArea.append("Запис оновлено: " + user.toString() + "\n");
                    } else {
                        outputArea.append("Запис з ID " + key + " не знайдено.\n");
                    }
                }
            }
        });
        JButton showAllButton = new JButton("Показати всіх користувачів");
        showAllButton.addActionListener(e -> displayAllUsers());


        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(showAllButton);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        displayAllUsers();
    }
    private void displayAllUsers() {
        outputArea.setText("Список всіх користувачів:\n");
        List<RedBlackTree.User> users = tree.getAllUsers();
        for (RedBlackTree.User user : users) {
            outputArea.append(user.toString() + "\n");
        }
        outputArea.append("\n");
    }

    public static void main(String[] args) {
        DatabaseGUI gui = new DatabaseGUI();
        gui.setVisible(true);
    }
}
