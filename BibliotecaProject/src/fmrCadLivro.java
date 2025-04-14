import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class fmrCadLivro extends JFrame {
    private static final long serialVersionUID = 1L;
    private bd objBD;
    private JTable tabelaLivros;
    private DefaultTableModel modeloTabela;

    public fmrCadLivro() {
        this.objBD = new bd("projeto_biblioteca", "root", "");

        if (objBD.testConnection()) {
            System.out.println("Banco de dados pronto para uso.");
        } else {
            System.err.println("Erro ao conectar ao banco de dados.");
        }

        setTitle("Gestor de Livros");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ✅ Menu
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu MenuCadastrar = new JMenu("Cadastrar");
        menuBar.add(MenuCadastrar);
        JMenuItem mntmNewMenuItem = new JMenuItem("Livro");
        MenuCadastrar.add(mntmNewMenuItem);
        mntmNewMenuItem.addActionListener(e -> {
            fmrCadastroLivro cadastro = new fmrCadastroLivro(objBD);
            cadastro.setVisible(true);
        });

        JMenu MenuConsultar = new JMenu("Consultar");
        menuBar.add(MenuConsultar);
        JMenu MenuConta = new JMenu("Conta");
        menuBar.add(MenuConta);
        JMenu MenuAjuda = new JMenu("Ajuda");
        menuBar.add(MenuAjuda);

        // ✅ Tabela de livros
        modeloTabela = new DefaultTableModel(new String[]{"ID", "Título", "Autor", "Categoria"}, 0);
        tabelaLivros = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabelaLivros);
        add(scrollPane, BorderLayout.CENTER);

        JButton btnEmprestar = new JButton("Emprestar livro selecionado");
        add(btnEmprestar, BorderLayout.SOUTH);

        btnEmprestar.addActionListener(e -> emprestarLivro());

        carregarLivros();
    }

    private void carregarLivros() {
        modeloTabela.setRowCount(0);
        String sql = """
                SELECT livros.id, livros.titulo, autores.nome AS autor, categorias.nome AS categoria
                FROM livros
                JOIN autores ON livros.id_autor = autores.id
                JOIN categorias ON livros.id_categoria = categorias.id
            """;

        try (Connection conn = objBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                modeloTabela.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getString("categoria")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar livros: " + e.getMessage());
        }
    }

    private void emprestarLivro() {
        int linhaSelecionada = tabelaLivros.getSelectedRow();

        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um livro primeiro!");
            return;
        }

        int idLivro = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
        String idUsuarioStr = JOptionPane.showInputDialog(this, "ID do usuário:");
        String dataDevolucao = JOptionPane.showInputDialog(this, "Data de devolução prevista (AAAA-MM-DD):");

        try {
            int idUsuario = Integer.parseInt(idUsuarioStr);

            String sql = "INSERT INTO emprestimos (id_usuario, id_livro, data_emprestimo, data_devolucao_prevista) VALUES (?, ?, CURDATE(), ?)";
            try (Connection conn = objBD.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, idUsuario);
                stmt.setInt(2, idLivro);
                stmt.setString(3, dataDevolucao);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Empréstimo registrado com sucesso!");

            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID do usuário inválido.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao registrar empréstimo: " + e.getMessage());
        }
    }
}
