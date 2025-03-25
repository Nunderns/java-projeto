import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class fmrCadLivro extends JFrame {

	private static final long serialVersionUID = 1L;
	private bd objBD;
	private int Acao;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					fmrCadLivro frame = new fmrCadLivro();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public fmrCadLivro() {
		
	    this.objBD = new bd("projeto_biblioteca", "root", "");

	    if (objBD.testConnection()) {
	        System.out.println("Banco de dados pronto para uso.");
	    } else {
	        System.err.println("Erro ao conectar ao banco de dados.");
	    }
		
		
		setTitle("Gestor de Livros");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu MenuCadastrar = new JMenu("Cadastrar ");
		menuBar.add(MenuCadastrar);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Livro");
		MenuCadastrar.add(mntmNewMenuItem);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("E-book");
		MenuCadastrar.add(mntmNewMenuItem_1);
		
		JMenu MenuConsultar = new JMenu("Consultar");
		menuBar.add(MenuConsultar);
		
		JMenuItem ItemLivro = new JMenuItem("Livro");
		MenuConsultar.add(ItemLivro);
		
		JMenu MenuConta = new JMenu("Conta");
		menuBar.add(MenuConta);
		
		JMenuItem ItemConfiguracao = new JMenuItem("Configurações");
		MenuConta.add(ItemConfiguracao);
		
		JMenuItem mntmNewMenuItem_2 = new JMenuItem("Sair");
		MenuConta.add(mntmNewMenuItem_2);
		
		JMenu MenuAjuda = new JMenu("Ajuda");
		menuBar.add(MenuAjuda);
		
		JMenuItem ItemWelcome = new JMenuItem("Bem-vindo");
		MenuAjuda.add(ItemWelcome);
		
		JMenuItem mntmNewMenuItem_3 = new JMenuItem("Conteudo de ajuda");
		MenuAjuda.add(mntmNewMenuItem_3);
	}

}
