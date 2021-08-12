package aplicacaoConsole;
/**********************************
 * IFPB - Curso Superior de Tec. em Sist. para Internet
 * POB - Persistencia de Objetos
 * Prof. Fausto Ayres
 *
 */

import javax.swing.JOptionPane;

import fachada.Fachada;


public class ConsultarMensagem {

	public ConsultarMensagem(){
		Fachada.inicializar();
		try {
			String termo = JOptionPane.showInputDialog("digite o termo");
			System.out.println("Buscar mensagens contendo:"+ termo);
			System.out.println(Fachada.buscarMensagens(termo));
			
			System.out.println("\nMensagens De um Usuario Logado!!!!");
			String nome = JOptionPane.showInputDialog("digite o nome");
			String senha = JOptionPane.showInputDialog(null,"digite a senha","123");
			Fachada.login(nome, senha);
			System.out.println("\nUsuario logado:"+ Fachada.getLogado().getNome());
			System.out.println(Fachada.listarMensagensUsuario());
			System.out.println("\nTotal de mensagens do Usuario logado: "+Fachada.totalMensagensUsuario());
			Fachada.logoff();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}finally {
			Fachada.finalizar();
		}
		System.out.println("\nfim do programa");
	}


	//=================================================
	public static void main(String[] args) {
		new ConsultarMensagem();
	}
}

