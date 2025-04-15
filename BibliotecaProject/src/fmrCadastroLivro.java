import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class fmrCadastroLivro extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField txtTitulo, txtIsbn, txtAno, txtQtd;
    private JComboBox<String> cbAutor, cbCategoria;
    private Map<String, Integer> mapaAutores = new HashMap<>();
    private Map<String, Integer> mapaCategorias = new HashMap<>();
    private bd banco;

    public fmrCadastroLivro(bd banco) {
        this.banco = banco;

        setTitle("Cadastro de Livro");
        setSize(500, 300);
        setLayout(new GridLayout(8, 2, 10, 10));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Título
        add(new JLabel("Título:"));
        txtTitulo = new JTextField();
        add(txtTitulo);

        // ISBN
        add(new JLabel("ISBN:"));
        txtIsbn = new JTextField();
        add(txtIsbn);

        // Autor
        add(new JLabel("Autor:"));
        cbAutor = new JComboBox<>();
        carregarAutores();
        add(cbAutor);

        // Categoria
        add(new JLabel("Categoria:"));
        cbCategoria = new JComboBox<>();
        carregarCategorias();
        add(cbCategoria);

        // Ano
        add(new JLabel("Ano de Publicação:"));
        txtAno = new JTextField();
        add(txtAno);

        // Quantidade
        add(new JLabel("Quantidade:"));
        txtQtd = new JTextField("1");
        add(txtQtd);

        // Botões
        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> salvarLivro());
        add(btnSalvar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        add(btnCancelar);
    }

    private void carregarAutores() {
        try (Connection conn = banco.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT id, nome FROM autores");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                cbAutor.addItem(nome);
                mapaAutores.put(nome, id);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar autores: " + e.getMessage());
        }
    }

    private void carregarCategorias() {
        try (Connection conn = banco.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT id, nome FROM categorias");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                cbCategoria.addItem(nome);
                mapaCategorias.put(nome, id);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar categorias: " + e.getMessage());
        }
    }

    private void salvarLivro() {
        try (Connection conn = banco.getConnection()) {
            String sql = "INSERT INTO livros (titulo, id_autor, id_categoria, isbn, ano_publicacao, quantidade) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            String autorSelecionado = (String) cbAutor.getSelectedItem();
            String categoriaSelecionada = (String) cbCategoria.getSelectedItem();

            Integer idAutor = mapaAutores.get(autorSelecionado);
            Integer idCategoria = mapaCategorias.get(categoriaSelecionada);

            if (idAutor == null || idCategoria == null) {
                JOptionPane.showMessageDialog(this, "Selecione um autor e uma categoria válidos!");
                return;
            }

            stmt.setString(1, txtTitulo.getText());
            stmt.setInt(2, idAutor);
            stmt.setInt(3, idCategoria);
            stmt.setString(4, txtIsbn.getText());
            stmt.setInt(5, Integer.parseInt(txtAno.getText()));
            stmt.setInt(6, Integer.parseInt(txtQtd.getText()));

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Livro cadastrado com sucesso!");

            // Limpar campos
            txtTitulo.setText("");
            txtIsbn.setText("");
            txtAno.setText("");
            txtQtd.setText("1");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar: " + e.getMessage());
        }
    }
}
