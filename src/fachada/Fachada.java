package fachada;

import java.util.List;

import javax.swing.JOptionPane;

import com.db4o.query.Query;

import dao.DAO;
import dao.DAOLog;
import dao.DAOMensagem;
import dao.DAOUsuario;
import modelo.Log;
import modelo.Mensagem;

import modelo.Usuario;

public class Fachada {
	private static DAOUsuario daousuario = new DAOUsuario();  
	private static DAOMensagem daomensagem = new DAOMensagem();  
	private static DAOLog daolog = new DAOLog();  

	private static Usuario usuariologado=null;


	public static void inicializar() {
		DAO.open();
	}

	public static void finalizar(){
		DAO.close();
	}

	public static List<Usuario> listarUsuarios() {
		// nao precisa estar logado
		return daousuario.readAll();	
	}
	public static List<Mensagem> listarMensagens() {
		// nao precisa estar logado
		return daomensagem.readAll();	
	}

	public static List<Log> listarLogs() {
		// nao precisa estar logado
		return daolog.readAll();	
	}
	public static List<Mensagem> buscarMensagens(String termo) throws  Exception{

		return daomensagem.readByTermo(termo);

	}

	public static Usuario criarUsuario(String nome, String senha) throws  Exception{
		// nao precisa estar logado
		DAO.begin();	
		Usuario u = daousuario.read(nome+"/"+senha);	
		if(u != null) {
			DAO.rollback();	
			throw new Exception("criar usuario - usuario existente:" + nome);
		}

		u = new Usuario(nome+"/"+senha);
		daousuario.create(u);		
		DAO.commit();
		return u;
	}


	public static void login(String nome, String senha) throws Exception{		
		//verificar se ja existe um usuario logada
		if(usuariologado!=null)
			throw new Exception ("ja existe um usuario logado "+getLogado().getNome());

		DAO.begin();	
		Usuario u = daousuario.read(nome+"/"+senha);	
		if(u == null) {
			DAO.rollback();	
			throw new Exception("login - usuario inexistente:" + nome);
		}
		if(!u.ativo()) {
			DAO.rollback();	
			throw new Exception("login - usuario nao ativo:" + nome);
		}
		usuariologado = u;		//altera o logado na fachada

		Log log = new Log(usuariologado.getNome());
		daolog.create(log);
		DAO.commit();
	}
	public static void logoff() {		
		usuariologado = null; 		//altera o logado na fachada
	}

	public static Usuario getLogado() {
		return usuariologado;
	}



	public static Mensagem criarMensagem(String texto) throws Exception{

		
		if (usuariologado == null) {
			throw new Exception("É necessário estar logado!");
		}else {
			DAO.begin();
			int id = daomensagem.obterUltimoId();
			id++;
			Mensagem me = new Mensagem(id, usuariologado, texto);
			usuariologado.adicionar(me);
			daomensagem.create(me);
			daousuario.update(usuariologado);
			DAO.commit();
			return me;
		}
	}


	public static List<Mensagem> listarMensagensUsuario() throws Exception{

		if (usuariologado == null) {
			throw new Exception("É necessário estar logado!");
		}else {
			return usuariologado.getMensagens();
		}
		
	}


	public static void apagarMensagens(int... ids) throws  Exception{
		DAO.begin();
		if (usuariologado == null) {
			throw new Exception("É necessário estar logado!");
			
		}else {		
			List<Mensagem> mensUsuario = usuariologado.getMensagens();
			for (int i:ids) {
				for(Mensagem mens: mensUsuario) {
					
						if(mens.getId() == i) {	
							usuariologado.remover(mens);
							Mensagem mensagem = daomensagem.read(i);
							daomensagem.delete(mensagem);
							daousuario.update(usuariologado);
							DAO.commit();
							System.out.println("Mensagem Excluida");
						}else {
							System.out.println("ID inexistente!");
						}
				}

			}
		}
	}

	public static void sairDoGrupo() throws  Exception{

		if (usuariologado == null) {
			throw new Exception("É necessário estar logado!");
			
		}else {
			DAO.begin();
			usuariologado.setStatus(false);
			daousuario.update(usuariologado);
			DAO.commit();
			logoff();
		}	
		
	}


	public static int totalMensagensUsuario() throws Exception{

		
		if (usuariologado == null) {
			throw new Exception("É necessário estar logado!");
			
		}else {		
			List<Mensagem> mensUsuario = usuariologado.getMensagens();
			return mensUsuario.size();
			}
		
	}

	public static void esvaziar() throws Exception{
		DAO.clear();
	}

}

