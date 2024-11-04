
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RedBlackTree {
    private static final String FILE_PATH = "users.dat";
    private Node root;
    private Node TNULL;

    private int idCounter = 1;

    public static class User implements Serializable {
        private static final long serialVersionUID = 1L;

        private int id;
        private String email;
        private String username;
        private String password;

        // Конструктор
        public User(String email, String username, String password) {
            this.id = id;
            this.email = email;
            this.username = username;
            this.password = password;
        }


        // Геттери і сеттери
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    ", email='" + email + '\'' +
                    ", username='" + username + '\'' +
                    '}';
        }
    }
    private class Node {
        User user;
        boolean color; // True for red, false for black
        Node left, right, parent;

        Node(User user) {
            this.user = user;
            this.left = TNULL;
            this.right = TNULL;
            this.parent = null;
            this.color = true; // new node is red
        }
    }

    // Constructor
    public RedBlackTree() {
        TNULL = new Node(null);
        TNULL.color = false; // TNULL is black
        root = TNULL;

        List<User> users = readUsersFromFile();
        for (User user : users) {
            insert(user);
        }
    }

    // Balance the tree after deletion
    private void fixDelete(Node x) {
        Node s;
        while (x != root && x.color == false) {
            if (x == x.parent.left) {
                s = x.parent.right;
                if (s.color == true) {
                    s.color = false;
                    x.parent.color = true;
                    leftRotate(x.parent);
                    s = x.parent.right;
                }

                if (s.left.color == false && s.right.color == false) {
                    s.color = true;
                    x = x.parent;
                } else {
                    if (s.right.color == false) {
                        s.left.color = false;
                        s.color = true;
                        rightRotate(s);
                        s = x.parent.right;
                    }

                    s.color = x.parent.color;
                    x.parent.color = false;
                    s.right.color = false;
                    leftRotate(x.parent);
                    x = root;
                }
            } else {
                s = x.parent.left;
                if (s.color == true) {
                    s.color = false;
                    x.parent.color = true;
                    rightRotate(x.parent);
                    s = x.parent.left;
                }

                if (s.right.color == false && s.left.color == false) {
                    s.color = true;
                    x = x.parent;
                } else {
                    if (s.left.color == false) {
                        s.right.color = false;
                        s.color = true;
                        leftRotate(s);
                        s = x.parent.left;
                    }

                    s.color = x.parent.color;
                    x.parent.color = false;
                    s.left.color = false;
                    rightRotate(x.parent);
                    x = root;
                }
            }
        }
        x.color = false;
    }

    // Balance the tree after insertion
    private void fixInsert(Node k) {
        Node u;
        while (k.parent.color == true) {
            if (k.parent == k.parent.parent.left) {
                u = k.parent.parent.right;
                if (u.color == true) {
                    k.parent.color = false;
                    u.color = false;
                    k.parent.parent.color = true;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.right) {
                        k = k.parent;
                        leftRotate(k);
                    }
                    k.parent.color = false;
                    k.parent.parent.color = true;
                    rightRotate(k.parent.parent);
                }
            } else {
                u = k.parent.parent.left;
                if (u.color == true) {
                    k.parent.color = false;
                    u.color = false;
                    k.parent.parent.color = true;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.left) {
                        k = k.parent;
                        rightRotate(k);
                    }
                    k.parent.color = false;
                    k.parent.parent.color = true;
                    leftRotate(k.parent.parent);
                }
            }
            if (k == root) {
                break;
            }
        }
        root.color = false;
    }

    private void leftRotate(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != TNULL) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    private void rightRotate(Node x) {
        Node y = x.left;
        x.left = y.right;
        if (y.right != TNULL) {
            y.right.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        y.right = x;
        x.parent = y;
    }

    public void insert(User user) {
        user.setId(idCounter++);
        Node node = new Node(user);
        node.parent = null;
        node.user = user;
        node.left = TNULL;
        node.right = TNULL;
        node.color = true;

        Node y = null;
        Node x = root;

        while (x != TNULL) {
            y = x;
            if (node.user.getId() < x.user.getId()) {
                x = x.left;
            } else {
                x = x.right;
            }
        }

        node.parent = y;
        if (y == null) {
            root = node;
        } else if (node.user.getId() < y.user.getId()) {
            y.left = node;
        } else {
            y.right = node;
        }

        if (node.parent == null) {
            node.color = false;
            return;
        }

        if (node.parent.parent == null) {
            return;
        }

        fixInsert(node);
        writeUsersToFile(getAllUsers(root));
    }

    public void delete(int id) {
        deleteNodeHelper(root, id);
        writeUsersToFile(getAllUsers(root));
    }

    private void deleteNodeHelper(Node node, int key) {
        Node z = TNULL;
        Node x, y;
        while (node != TNULL) {
            if (node.user.getId() == key) {
                z = node;
            }

            if (node.user.getId() <= key) {
                node = node.right;
            } else {
                node = node.left;
            }
        }

        if (z == TNULL) {
            System.out.println("Couldn't find key in the tree");
            return;
        }

        y = z;
        boolean yOriginalColor = y.color;
        if (z.left == TNULL) {
            x = z.right;
            rbTransplant(z, z.right);
        } else if (z.right == TNULL) {
            x = z.left;
            rbTransplant(z, z.left);
        } else {
            y = minimum(z.right);
            yOriginalColor = y.color;
            x = y.right;
            if (y.parent == z) {
                x.parent = y;
            } else {
                rbTransplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }

            rbTransplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }
        if (yOriginalColor == false) {
            fixDelete(x);
        }
    }

    private void rbTransplant(Node u, Node v) {
        if (u.parent == null) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        v.parent = u.parent;
    }

    private Node minimum(Node node) {
        while (node.left != TNULL) {
            node = node.left;
        }
        return node;
    }

    public User search(int id) {
        return searchNode(root, id);
    }

    private User searchNode(Node node, int key) {
        if (node == TNULL || key == node.user.getId()) {
            return node.user;
        }

        if (key < node.user.getId()) {
            return searchNode(node.left, key);
        }
        return searchNode(node.right, key);
    }

    public void update(User updatedUser) {
        User existingUser = search(updatedUser.getId());
        if (existingUser != null) {
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setPassword(updatedUser.getPassword());
            writeUsersToFile(getAllUsers(root));
        }
    }
    public List<User> getAllUsers() {
        return getAllUsers(root); // Викликаємо допоміжний метод
    }

    private List<User> getAllUsers(Node node) {
        List<User> allUsers = new ArrayList<>();
        if (node != TNULL) {
            allUsers.addAll(getAllUsers(node.left));
            allUsers.add(node.user);
            allUsers.addAll(getAllUsers(node.right));
        }
        return allUsers;
    }

    private List<User> readUsersFromFile() {
        List<User> users = new ArrayList<>();
        if (!new File(FILE_PATH).exists()) {
            try {
                new File(FILE_PATH).createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return users;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            while (true) {
                User user = (User) ois.readObject();
                users.add(user);
            }
        } catch (EOFException e) {
            // End of file reached
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return users;
    }

    private void writeUsersToFile(List<User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            for (User user : users) {
                oos.writeObject(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
