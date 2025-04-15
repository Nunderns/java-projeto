import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class fmrCadastroAutor extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField txtNome;
    private bd objBD;

    public fmrCadastroAutor(bd objBD) {
        this.objBD = objBD;

        setTitle("Cadastro de Autor");
        setSize(400, 150);
        setLayout(new GridLayout(3, 2, 10, 10));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        add(new JLabel("Nome do Autor:"));
        txtNome = new JTextField();
        add(txtNome);

        JButton btnSalvar = new JButton("Salvar");
        add(btnSalvar);
        JButton btnCancelar = new JButton("Cancelar");
        add(btnCancelar);

        btnSalvar.addActionListener(e -> cadastrarAutor());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void cadastrarAutor() {
        String nome = txtNome.getText().trim();

        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite o nome do autor.");
            return;
        }

        String sql = "INSERT INTO autores (nome) VALUES (?)";

        try (Connection conn = objBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Autor cadastrado com sucesso!");
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar autor: " + e.getMessage());
        }
    }
}
