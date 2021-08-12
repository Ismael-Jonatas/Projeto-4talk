package dao;

import modelo.Mensagem;

import java.util.List;
import com.db4o.query.Query;

public class DAOMensagem extends DAO<Mensagem> {

	public Mensagem read (Object chave) {
		Integer me = (Integer) chave;	//casting para o tipo da chave
		Query q = manager.query();
		q.constrain(Mensagem.class);
		q.descend("id").constrain(me);
		List<Mensagem> resultados = q.execute();
		if (resultados.size()>0) {
			return resultados.get(0);
		}else
			return null;
	}
	
	public  List<Mensagem> readByTermo(String termo) {
		Query q = manager.query();
		q.constrain(Mensagem.class);
		q.descend("texto").constrain(termo).like();
		List<Mensagem> result = q.execute(); 
		return result;
	}
	
}
