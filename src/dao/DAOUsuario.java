package dao;

import modelo.Usuario;
import java.util.List;
import com.db4o.query.Query;

public class DAOUsuario extends DAO<Usuario> {

	@Override
	public Usuario read(Object chave) {
		String nomesenha = (String) chave;
		Query q = manager.query();
		q.constrain(Usuario.class);
		q.descend("nomesenha").constrain(nomesenha);
		List<Usuario> resultados = q.execute();
		if (resultados.size()>0)
			return resultados.get(0);
		else
			return null;
	}
	
}
