import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class bd {
    private String url;
    private String usuario;
    private String senha;

    public bd(String banco, String usuario, String senha) {
        this.usuario = usuario;
        this.senha = senha;
        this.url = "jdbc:mysql://localhost:3306/" + banco;
    }

    public boolean testConnection() {
        try (Connection conexao = DriverManager.getConnection(url, usuario, senha)) {
            System.out.println("✅ Conexão bem-sucedida!");
            return true;
        } catch (SQLException e) {
            System.err.println("❌ Erro ao conectar: " + e.getMessage());
            return false;
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, usuario, senha);
    }
}
