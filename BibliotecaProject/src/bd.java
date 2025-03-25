import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class bd {
    private String url = "jdbc:mysql://localhost:3306/projeto_biblioteca";
    private String usuario;
    private String senha;
    private Connection conexao;

    public bd(String banco, String usuario, String senha) {
        this.usuario = usuario;
        this.senha = senha;
        this.url = "jdbc:mysql://localhost:3306/" + banco;
    }

    // Método para testar conexão
    public boolean testConnection() {
        try {
            conexao = DriverManager.getConnection(url, usuario, senha);
            System.out.println("✅ Conexão bem-sucedida!");
            return true;
        } catch (SQLException e) {
            System.err.println("❌ Erro ao conectar: " + e.getMessage());
            return false;
        } finally {
            try {
                if (conexao != null) {
                    conexao.close();
                }
            } catch (SQLException e) {
                System.err.println("❌ Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }
}
