package dao;

public class LivroDAO {
    private bd objBD;
    private Connection conexao;

    public void inserir(Livro livro) {
        try {
            objBD = new bd("biblioteca", "root", "");
            conexao = objBD.connected();

            String sql = "INSERT INTO livros (titulo, autor, isbn, ano) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getAutor());
            stmt.setString(3, livro.getIsbn());
            stmt.setInt(4, livro.getAno());

            stmt.execute();
            stmt.close();
            conexao.close();
        } catch (SQLException e) {
            System.out.println("Erro ao inserir livro: " + e.getMessage());
        }
    }
}

