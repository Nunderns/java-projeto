import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class fmrCadastroLivro extends JFrame {

    private JTextField txtTitulo, txtIsbn, txtAno, txtQtd, txtIdAutor, txtIdCategoria;
    private bd banco;

    public fmrCadastroLivro(bd banco) {
        this.banco = banco;

        setTitle("Cadastro de Livro");
        setSize(500, 300);
        setLayout(new GridLayout(8, 2, 10, 10));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        add(new JLabel("Título:"));
        txtTitulo = new JTextField();
        add(txtTitulo);

        add(new JLabel("ISBN:"));
        txtIsbn = new JTextField();
        add(txtIsbn);

        add(new JLabel("ID do Autor:"));
        txtIdAutor = new JTextField();
        add(txtIdAutor);

        add(new JLabel("ID da Categoria:"));
        txtIdCategoria = new JTextField();
        add(txtIdCategoria);

        add(new JLabel("Ano de Publicação:"));
        txtAno = new JTextField();
        add(txtAno);

        add(new JLabel("Quantidade:"));
        txtQtd = new JTextField("1");
        add(txtQtd);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> salvarLivro());
        add(btnSalvar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        add(btnCancelar);
    }

    private void salvarLivro() {
        String sql = "INSERT INTO livros (titulo, id_autor, id_categoria, isbn, ano_publicacao, quantidade) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = banco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, txtTitulo.getText());
            stmt.setInt(2, Integer.parseInt(txtIdAutor.getText()));
            stmt.setInt(3, Integer.parseInt(txtIdCategoria.getText()));
            stmt.setString(4, txtIsbn.getText());
            stmt.setInt(5, Integer.parseInt(txtAno.getText()));
            stmt.setInt(6, Integer.parseInt(txtQtd.getText()));

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Livro cadastrado com sucesso!");

            // Limpar os campos
            txtTitulo.setText("");
            txtIsbn.setText("");
            txtIdAutor.setText("");
            txtIdCategoria.setText("");
            txtAno.setText("");
            txtQtd.setText("1");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar: " + e.getMessage());
        }
    }
}
