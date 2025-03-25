import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LoginUser extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private JPanel contentPane;
    private CardLayout cardLayout;
    private JPanel panelContainer;
    
    private JTextField txtNome, txtEmailRegister;
    private JPasswordField txtSenhaRegister;
    private JComboBox<String> cbTipo;

    private JTextField txtEmailLogin;
    private JPasswordField txtSenhaLogin;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                LoginUser frame = new LoginUser();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public LoginUser() {
        setTitle("Gerenciador de Livros - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        panelContainer = new JPanel(cardLayout);

        JPanel loginPanel = createLoginPanel();
        JPanel registerPanel = createRegisterPanel();

        panelContainer.add(loginPanel, "login");
        panelContainer.add(registerPanel, "register");

        contentPane.add(panelContainer, BorderLayout.CENTER);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));

        JLabel lblEmail = new JLabel("Email:");
        txtEmailLogin = new JTextField();

        JLabel lblSenha = new JLabel("Senha:");
        txtSenhaLogin = new JPasswordField();

        JButton btnLogin = new JButton("Entrar");
        JButton btnRegister = new JButton("Criar conta");

        btnLogin.addActionListener(e -> loginUser());
        btnRegister.addActionListener(e -> cardLayout.show(panelContainer, "register"));

        panel.add(lblEmail);
        panel.add(txtEmailLogin);
        panel.add(lblSenha);
        panel.add(txtSenhaLogin);
        panel.add(btnLogin);
        panel.add(btnRegister);

        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridLayout(7, 1, 10, 10));

        JLabel lblNome = new JLabel("Nome:");
        txtNome = new JTextField();

        JLabel lblEmail = new JLabel("Email:");
        txtEmailRegister = new JTextField();

        JLabel lblSenha = new JLabel("Senha:");
        txtSenhaRegister = new JPasswordField();

        JLabel lblTipo = new JLabel("Tipo de Usuário:");
        String[] tipos = { "Estudante", "Funcionário" };
        cbTipo = new JComboBox<>(tipos);

        JButton btnRegistrar = new JButton("Registrar");
        JButton btnVoltar = new JButton("Já tenho conta");

        btnRegistrar.addActionListener(e -> registerUser());
        btnVoltar.addActionListener(e -> cardLayout.show(panelContainer, "login"));

        panel.add(lblNome);
        panel.add(txtNome);
        panel.add(lblEmail);
        panel.add(txtEmailRegister);
        panel.add(lblSenha);
        panel.add(txtSenhaRegister);
        panel.add(lblTipo);
        panel.add(cbTipo);
        panel.add(btnRegistrar);
        panel.add(btnVoltar);

        return panel;
    }

    // Conexão com MySQL
    private Connection conectar() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/projeto_biblioteca", "root", "");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar com o banco de dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    // Registrar usuário
    private void registerUser() {
        String nome = txtNome.getText();
        String email = txtEmailRegister.getText();
        String senha = new String(txtSenhaRegister.getPassword());
        String tipo = (String) cbTipo.getSelectedItem();

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Connection conn = conectar();
        if (conn == null) return;

        try {
            String sql = "INSERT INTO usuarios (nome, email, senha, tipo) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setString(2, email);
            stmt.setString(3, senha);
            stmt.setString(4, tipo);
            stmt.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Usuário registrado com sucesso!");
            cardLayout.show(panelContainer, "login"); // Voltar para login
            
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao registrar usuário!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Login do usuário
    private void loginUser() {
        String email = txtEmailLogin.getText();
        String senha = new String(txtSenhaLogin.getPassword());

        if (email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Connection conn = conectar();
        if (conn == null) return;

        try {
            String sql = "SELECT * FROM usuarios WHERE email = ? AND senha = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login realizado com sucesso!");

                // Fecha a tela de login
                this.dispose();

                // Abre a tela fmrCadLivro
                EventQueue.invokeLater(() -> {
                    try {
                        fmrCadLivro frame = new fmrCadLivro();
                        frame.setVisible(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

            } else {
                JOptionPane.showMessageDialog(this, "Usuário ou senha incorretos!", "Erro", JOptionPane.ERROR_MESSAGE);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao fazer login!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
