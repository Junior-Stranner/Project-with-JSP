package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import beandto.BeanDtoGraficoSalarioUser;
import connection.SingleConnectionBanco;
import model.ModelLogin;
import model.ModelTelefone;

public class DAOUsuarioRepository {
	
	/*Injeção de dependencia do banco de dados dentro da classe*/
	private Connection connection;
	
	public DAOUsuarioRepository() {
		connection = SingleConnectionBanco.getConnection();
	}
	
	
	/*---------------------------------------------------------------------------------------------------------------------------*/
	
	
	/*Método que faz a consulta no banco de dados que traz media salarial por perfil, com data inicial e data final*/
	public BeanDtoGraficoSalarioUser montarGraficoMediaSalario(Long userLogado, String dataInicial, String dataFinal) throws Exception {

		String sql = "select avg(rendamensal) as media_salarial, perfil from model_login where usuario_id = ? and datanascimento >= ? and datanascimento <= ? group by perfil";

		PreparedStatement preparedStatement = connection.prepareStatement(sql);

		preparedStatement.setLong(1, userLogado);
		preparedStatement.setDate(2, Date.valueOf(new SimpleDateFormat("yyyy-mm-dd").format(new SimpleDateFormat("dd/mm/yyyy").parse(dataInicial))));/*Conversões de data para o banco entender*/
		preparedStatement.setDate(3, Date.valueOf(new SimpleDateFormat("yyyy-mm-dd").format(new SimpleDateFormat("dd/mm/yyyy").parse(dataFinal))));/*Conversões de data para o banco entender*/


		ResultSet resultSet = preparedStatement.executeQuery();

		List<String> perfils = new ArrayList<String>();
		List<Double> salarios = new ArrayList<Double>();

		BeanDtoGraficoSalarioUser beanDtoGraficoSalarioUser = new BeanDtoGraficoSalarioUser();

		while (resultSet.next()) {
			Double media_salarial = resultSet.getDouble("media_salarial");
			String perfil = resultSet.getString("perfil");

			perfils.add(perfil);
			salarios.add(media_salarial);

		}

		beanDtoGraficoSalarioUser.setPerfils(perfils);
		beanDtoGraficoSalarioUser.setSalarios(salarios);

		return beanDtoGraficoSalarioUser;
	}
	
	
	/*---------------------------------------------------------------------------------------------------------------------------*/
	
	
	/*Método que faz a consulta no banco de dados que traz media salarial por perfil*/
	public BeanDtoGraficoSalarioUser montarGraficoMediaSalario(Long userLogado) throws Exception {
		
		String sql = "select avg(rendamensal) as media_salarial, perfil from model_login where usuario_id = ? group by perfil";
		
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		
		preparedStatement.setLong(1, userLogado);
		
		ResultSet resultSet = preparedStatement.executeQuery();
		
		List<String> perfils = new ArrayList<String>();
		List<Double> salarios = new ArrayList<Double>();
		
		BeanDtoGraficoSalarioUser beanDtoGraficoSalarioUser = new BeanDtoGraficoSalarioUser();
		
		while (resultSet.next()) {
			Double media_salarial = resultSet.getDouble("media_salarial");
			String perfil = resultSet.getString("perfil");
			
			perfils.add(perfil);
			salarios.add(media_salarial);
			
		}
		
		beanDtoGraficoSalarioUser.setPerfils(perfils);
		beanDtoGraficoSalarioUser.setSalarios(salarios);
		
		return beanDtoGraficoSalarioUser;	
		
	}
	
	
	/*---------------------------------------------------------------------------------------------------------------------------*/
	
	/*Método para gravar usuários*/
	public ModelLogin gravarUsuario(ModelLogin objeto, Long userLogado) throws Exception {
		
		if (objeto.isNovo()) {/*Grava um novo contato*/
			
		
		String sql = "INSERT INTO model_login(login, senha, nome, email, usuario_id, perfil, sexo, cep, logradouro, bairro, localidade, uf, numero, datanascimento, rendamensal) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		PreparedStatement preparaSql = connection.prepareStatement(sql);
		
		preparaSql.setString(1, objeto.getLogin());
		preparaSql.setString(2, objeto.getSenha());
		preparaSql.setString(3, objeto.getNome());
		preparaSql.setString(4, objeto.getEmail());
		preparaSql.setLong(5, userLogado);
		preparaSql.setString(6, objeto.getPerfil());
		preparaSql.setString(7, objeto.getSexo());
		
		preparaSql.setString(8, objeto.getCep());
		preparaSql.setString(9, objeto.getLogradouro());
		preparaSql.setString(10, objeto.getBairro());
		preparaSql.setString(11, objeto.getLocalidade());
		preparaSql.setString(12, objeto.getUf());
		preparaSql.setString(13, objeto.getNumero());
		preparaSql.setDate(14, objeto.getDataNascimento());
		preparaSql.setDouble(15, objeto.getRendamensal());
		
		
		preparaSql.execute();
		
		connection.commit();
		
		
			if (objeto.getFotouser() != null && !objeto.getFotouser().isEmpty()) {/*Aqui ainda não temos o "id", então grava a imagem de acordo com o "id"*/
				sql = "update model_login set fotouser =?, extensaofotouser =? where login  =?";
				
				preparaSql = connection.prepareStatement(sql);
				
				preparaSql.setString(1, objeto.getFotouser());
				preparaSql.setString(2, objeto.getExtensaofotouser());
				preparaSql.setString(3, objeto.getLogin());
				
				preparaSql.execute();
				
				connection.commit();
			}
		
		/*---------------------------------------------------------------------------------------------------------------------------*/
		
		}else {/*Senão faz a atualização dos dados*/
			
			String sql = "UPDATE model_login SET login=?, senha=?, nome=?, email=?, perfil=?, sexo=?, cep=?, logradouro=?, bairro=?, localidade=?, uf=?, numero=?, datanascimento=?, rendamensal=? WHERE id = "+objeto.getId()+";";
			
			PreparedStatement prepareSql = connection.prepareStatement(sql);
			
			prepareSql.setString(1, objeto.getLogin());
			prepareSql.setString(2, objeto.getSenha());
			prepareSql.setString(3, objeto.getNome());
			prepareSql.setString(4, objeto.getEmail());
			prepareSql.setString(5, objeto.getPerfil());
			prepareSql.setString(6, objeto.getSexo());
			
			prepareSql.setString(7, objeto.getCep());
			prepareSql.setString(8, objeto.getLogradouro());
			prepareSql.setString(9, objeto.getBairro());
			prepareSql.setString(10, objeto.getLocalidade());
			prepareSql.setString(11, objeto.getUf());
			prepareSql.setString(12, objeto.getNumero());
			prepareSql.setDate(13, objeto.getDataNascimento());
			prepareSql.setDouble(14, objeto.getRendamensal());
			
			prepareSql.executeUpdate();
			
			connection.commit();
			
			if (objeto.getFotouser() != null && !objeto.getFotouser().isEmpty()) {/*Trabalha usando o "id", fazendo a atualização, faz a atualização da imagem*/
				sql = "update model_login set fotouser =?, extensaofotouser =? where id =?";
				
				prepareSql = connection.prepareStatement(sql);
				
				prepareSql.setString(1, objeto.getFotouser());
				prepareSql.setString(2, objeto.getExtensaofotouser());
				prepareSql.setLong(3, objeto.getId());
				
				prepareSql.execute();
				
				connection.commit();
			}
			
		}
		
		return this.consultaUsuario(objeto.getLogin(), userLogado);/*Vai consultar o usuário pelo login e retornar para gente*/
		
	}
	
	
	/*---------------------------------------------------------------------------------------------------------------------------*/
	
	/*Método que busca todos os usuário do banco de dados, com a lista paginada*/
	public List<ModelLogin> consultaUsuarioListPaginada(Long userLogado, Integer offset) throws Exception {
		
		List<ModelLogin> retorno = new ArrayList<ModelLogin>();
		
		String sql = "select * from model_login where useradmin is false and usuario_id = " + userLogado + "order by nome offset "+ offset +" limit 5";
		PreparedStatement statement = connection.prepareStatement(sql);
		
		ResultSet resultado = statement.executeQuery();
		
		while (resultado.next()) {/*Vai percorrer as linhas do resultado do SQL*/
			
			ModelLogin modelLogin = new ModelLogin();
			
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setNome(resultado.getString("nome"));
			//modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setPerfil(resultado.getString("sexo"));
			
			retorno.add(modelLogin);
			
		}
		
		
		return retorno;
		
	}
	
	
	/*---------------------------------------------------------------------------------------------------------------------------*/
	
	
	/*Método que vai trazer do Banco de dados de 5 em 5 contatos*/
	public int totalPagina(Long userLongado) throws Exception {
		
		String sql = "select count(1) as total from model_login  where usuario_id = " + userLongado;
		
		PreparedStatement statement = connection.prepareStatement(sql);
		
		ResultSet resultado = statement.executeQuery();
		
		resultado.next();
		
		Double cadastros = resultado.getDouble("total");
		
		Double porpagina = 5.0;
				
		Double pagina = cadastros / porpagina;
				
		Double resto = pagina % 2;
				
			if (resto > 0) {
				pagina ++;
			}
			
			return pagina.intValue();
			
		}
	
	
	
	/*---------------------------------------------------------------------------------------------------------------------------*/
	
	
	/*Método que busca todos os usuário do banco de dados*/
	public List<ModelLogin> consultaUsuarioListRel(Long userLogado) throws Exception {
			
			List<ModelLogin> retorno = new ArrayList<ModelLogin>();
			
			String sql = "select * from model_login where useradmin is false and usuario_id = " + userLogado;
			PreparedStatement statement = connection.prepareStatement(sql);
			
			ResultSet resultado = statement.executeQuery();
			
			while (resultado.next()) {/*Vai percorrer as linhas do resultado do SQL*/
				
				ModelLogin modelLogin = new ModelLogin();
				
				modelLogin.setEmail(resultado.getString("email"));
				modelLogin.setId(resultado.getLong("id"));
				modelLogin.setLogin(resultado.getString("login"));
				modelLogin.setNome(resultado.getString("nome"));
				//modelLogin.setSenha(resultado.getString("senha"));
				modelLogin.setPerfil(resultado.getString("perfil"));
				modelLogin.setPerfil(resultado.getString("sexo"));
				modelLogin.setDataNascimento(resultado.getDate("datanascimento"));
				modelLogin.setTelefones(this.listFone(modelLogin.getId()));
				
				retorno.add(modelLogin);
				
			}
			
			
			return retorno;
			
		}
	
	
	/*---------------------------------------------------------------------------------------------------------------------------*/
	
	
	/*Método que busca todos os usuário do banco de dados, passando datas inicial e final*/
	public List<ModelLogin> consultaUsuarioListRel(Long userLogado, String dataInicial, String dataFinal) throws Exception {
			
			List<ModelLogin> retorno = new ArrayList<ModelLogin>();
			
			String sql = "select * from model_login where useradmin is false and usuario_id = " + userLogado + "and datanascimento >= ? and datanascimento <= ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setDate(1, Date.valueOf(new SimpleDateFormat("yyyy-mm-dd").format(new SimpleDateFormat("dd/mm/yyyy").parse(dataInicial))));/*Conversões de data para o banco entender*/
			statement.setDate(2, Date.valueOf(new SimpleDateFormat("yyyy-mm-dd").format(new SimpleDateFormat("dd/mm/yyyy").parse(dataFinal))));/*Conversões de data para o banco entender*/

			
			ResultSet resultado = statement.executeQuery();
			
			while (resultado.next()) {/*Vai percorrer as linhas do resultado do SQL*/
				
				ModelLogin modelLogin = new ModelLogin();
				
				modelLogin.setEmail(resultado.getString("email"));
				modelLogin.setId(resultado.getLong("id"));
				modelLogin.setLogin(resultado.getString("login"));
				modelLogin.setNome(resultado.getString("nome"));
				//modelLogin.setSenha(resultado.getString("senha"));
				modelLogin.setPerfil(resultado.getString("perfil"));
				modelLogin.setPerfil(resultado.getString("sexo"));
				modelLogin.setDataNascimento(resultado.getDate("datanascimento"));
				modelLogin.setTelefones(this.listFone(modelLogin.getId()));
				
				retorno.add(modelLogin);
				
			}
			
			
			return retorno;
			
		}

	
	
	/*---------------------------------------------------------------------------------------------------------------------------*/
	
	
	/*Método que busca todos os usuário do banco de dados*/
	public List<ModelLogin> consultaUsuarioList(Long userLogado) throws Exception {
			
			List<ModelLogin> retorno = new ArrayList<ModelLogin>();
			
			String sql = "select * from model_login where useradmin is false and usuario_id = " + userLogado + "limit 5";
			PreparedStatement statement = connection.prepareStatement(sql);
			
			ResultSet resultado = statement.executeQuery();
			
			while (resultado.next()) {/*Vai percorrer as linhas do resultado do SQL*/
				
				ModelLogin modelLogin = new ModelLogin();
				
				modelLogin.setEmail(resultado.getString("email"));
				modelLogin.setId(resultado.getLong("id"));
				modelLogin.setLogin(resultado.getString("login"));
				modelLogin.setNome(resultado.getString("nome"));
				//modelLogin.setSenha(resultado.getString("senha"));
				modelLogin.setPerfil(resultado.getString("perfil"));
				modelLogin.setPerfil(resultado.getString("sexo"));
				
				retorno.add(modelLogin);
				
			}
			
			
			return retorno;
			
		}
	
	/*---------------------------------------------------------------------------------------------------------------------------*/
	
	
	/*Método que faz a consulta de usuário por login*/
	public int consultaUsuarioListTotalPaginaPaginacao(String nome, Long userLogado) throws Exception {
		
		
		String sql = "select count(1) as total from model_login where upper(nome) like upper(?) and useradmin is false and usuario_id = ? ";
		
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, "%" + nome + "%");
		statement.setLong(2, userLogado);
		
		ResultSet resultado = statement.executeQuery();
		
		resultado.next();
		
		Double cadastros = resultado.getDouble("total");
		
		Double porpagina = 5.0;
				
		Double pagina = cadastros / porpagina;
				
		Double resto = pagina % 2;
				
			if (resto > 0) {
				pagina ++;
			}
			
			return pagina.intValue();
		
	}
	
	
	/*---------------------------------------------------------------------------------------------------------------------------*/
	
	/**/
	public List<ModelLogin> consultaUsuarioListOffSet(String nome, Long userLogado, int offset) throws Exception {
		
		List<ModelLogin> retorno = new ArrayList<ModelLogin>();
		
		String sql = "select * from model_login where upper(nome) like upper(?) and useradmin is false and usuario_id = ? offset "+offset+" limit 5";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, "%" + nome + "%");
		statement.setLong(2, userLogado);
		
		ResultSet resultado = statement.executeQuery();
		
		while (resultado.next()) {/*Vai percorrer as linhas do resultado do SQL*/
			
			ModelLogin modelLogin = new ModelLogin();
			
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setNome(resultado.getString("nome"));
			//modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			
			retorno.add(modelLogin);
			
		}
		return retorno;
	}
	
	
	/*---------------------------------------------------------------------------------------------------------------------------*/
	
	/*Método que faz a consulta de usuário por login*/
	public List<ModelLogin> consultaUsuarioList(String nome, Long userLogado) throws Exception {
		
		List<ModelLogin> retorno = new ArrayList<ModelLogin>();
		
		String sql = "select * from model_login where upper(nome) like upper(?) and useradmin is false and usuario_id = ? limit 5";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, "%" + nome + "%");
		statement.setLong(2, userLogado);
		
		ResultSet resultado = statement.executeQuery();
		
		while (resultado.next()) {/*Vai percorrer as linhas do resultado do SQL*/
			
			ModelLogin modelLogin = new ModelLogin();
			
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setNome(resultado.getString("nome"));
			//modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			
			retorno.add(modelLogin);
			
		}
		return retorno;
	}
	
	/*---------------------------------------------------------------------------------------------------------------------------*/
	
	/*Método  consultar se o usuário está logado, passando somente login como atributo, pegando no BD o usuário "admin"*/
	public ModelLogin consultaUsuarioLogado(String login) throws Exception {
		
		ModelLogin modelLogin = new ModelLogin();
		
		String sql = "select * from model_login where upper(login) = upper('"+login+"')";
		
		PreparedStatement statement = connection.prepareStatement(sql);
		
		ResultSet resultado = statement.executeQuery();
		
		while (resultado.next()) {/*Se tem resultado*/
			
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setUseradmin(resultado.getBoolean("useradmin"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			modelLogin.setFotouser(resultado.getString("fotouser"));
			modelLogin.setCep(resultado.getString("cep"));
			modelLogin.setLogradouro(resultado.getString("logradouro"));
			modelLogin.setBairro(resultado.getString("bairro"));
			modelLogin.setLocalidade(resultado.getString("localidade"));
			modelLogin.setUf(resultado.getString("uf"));
			modelLogin.setNumero(resultado.getString("numero"));
			modelLogin.setDataNascimento(resultado.getDate("datanascimento"));
			modelLogin.setRendamensal(resultado.getDouble("rendamensal"));
			
		}
		
		return modelLogin;
		
	}
	
	
	/*---------------------------------------------------------------------------------------------------------------------------*/
	
	/*Método  consultar se o usuário está logado, passando somente login como atributo, pegando no BD os outros usuários, menos o "admin"*/
	public ModelLogin consultaUsuario(String login) throws Exception {
			
			ModelLogin modelLogin = new ModelLogin();
			
			String sql = "select * from model_login where upper(login) = upper('"+login+"') and useradmin is false ";
			
			PreparedStatement statement = connection.prepareStatement(sql);
			
			ResultSet resultado = statement.executeQuery();
			
			while (resultado.next()) {/*Se tem resultado*/
				
				modelLogin.setId(resultado.getLong("id"));
				modelLogin.setEmail(resultado.getString("email"));
				modelLogin.setLogin(resultado.getString("login"));
				modelLogin.setSenha(resultado.getString("senha"));
				modelLogin.setNome(resultado.getString("nome"));
				modelLogin.setUseradmin(resultado.getBoolean("useradmin"));
				modelLogin.setPerfil(resultado.getString("perfil"));
				modelLogin.setSexo(resultado.getString("sexo"));
				modelLogin.setFotouser(resultado.getString("fotouser"));
				modelLogin.setCep(resultado.getString("cep"));
				modelLogin.setLogradouro(resultado.getString("logradouro"));
				modelLogin.setBairro(resultado.getString("bairro"));
				modelLogin.setLocalidade(resultado.getString("localidade"));
				modelLogin.setUf(resultado.getString("uf"));
				modelLogin.setNumero(resultado.getString("numero"));
				modelLogin.setDataNascimento(resultado.getDate("datanascimento"));
				modelLogin.setRendamensal(resultado.getDouble("rendamensal"));
				
			}
			
			return modelLogin;
			
		}
	
	
	/*---------------------------------------------------------------------------------------------------------------------------*/
	
	/*Método de consultar no banco de dados pelo login*/
	public ModelLogin consultaUsuario(String login, Long userLogado) throws Exception {
		
		ModelLogin modelLogin = new ModelLogin();
		
		String sql = "select * from model_login where upper(login) = upper('"+login+"') and useradmin is false and usuario_id = " + userLogado;
		
		PreparedStatement statement = connection.prepareStatement(sql);
		
		ResultSet resultado = statement.executeQuery();
		
		while (resultado.next()) {/*Se tem resultado*/
			
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			modelLogin.setFotouser(resultado.getString("fotouser"));
			modelLogin.setCep(resultado.getString("cep"));
			modelLogin.setLogradouro(resultado.getString("logradouro"));
			modelLogin.setBairro(resultado.getString("bairro"));
			modelLogin.setLocalidade(resultado.getString("localidade"));
			modelLogin.setUf(resultado.getString("uf"));
			modelLogin.setNumero(resultado.getString("numero"));
			modelLogin.setDataNascimento(resultado.getDate("datanascimento"));
			modelLogin.setRendamensal(resultado.getDouble("rendamensal"));
			
		}
		
		return modelLogin;
		
	}
	
	
/*---------------------------------------------------------------------------------------------------------------------------*/
	
	/*Método que faz a consulta por "ID"*/
	public ModelLogin consultaUsuarioID(Long id) throws Exception {
			
			ModelLogin modelLogin = new ModelLogin();
			
			String sql = "select * from model_login where id = ? and useradmin is false";
			
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setLong(1, id);
			
			ResultSet resultado = statement.executeQuery();
			
			while (resultado.next()) {/*Se tem resultado*/
				
				modelLogin.setId(resultado.getLong("id"));
				modelLogin.setEmail(resultado.getString("email"));
				modelLogin.setLogin(resultado.getString("login"));
				modelLogin.setSenha(resultado.getString("senha"));
				modelLogin.setNome(resultado.getString("nome"));
				modelLogin.setPerfil(resultado.getString("perfil"));
				modelLogin.setSexo(resultado.getString("sexo"));
				modelLogin.setFotouser(resultado.getString("fotouser"));
				modelLogin.setExtensaofotouser(resultado.getString("extensaofotouser"));
				modelLogin.setCep(resultado.getString("cep"));
				modelLogin.setLogradouro(resultado.getString("logradouro"));
				modelLogin.setBairro(resultado.getString("bairro"));
				modelLogin.setLocalidade(resultado.getString("localidade"));
				modelLogin.setUf(resultado.getString("uf"));
				modelLogin.setNumero(resultado.getString("numero"));
				modelLogin.setDataNascimento(resultado.getDate("datanascimento"));
				modelLogin.setRendamensal(resultado.getDouble("rendamensal"));
				
			}
			
			return modelLogin;
			
		}
	
	
	/*---------------------------------------------------------------------------------------------------------------------------*/
	
	/*Método que faz a consulta por "ID" se o usuário estiver logado*/
	public ModelLogin consultaUsuarioID(String id, Long userLogado) throws Exception {
			
			ModelLogin modelLogin = new ModelLogin();
			
			String sql = "select * from model_login where id = ? and useradmin is false and usuario_id = ?";
			
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setLong(1, Long.parseLong(id));
			statement.setLong(2, userLogado);
			
			ResultSet resultado = statement.executeQuery();
			
			while (resultado.next()) {/*Se tem resultado*/
				
				modelLogin.setId(resultado.getLong("id"));
				modelLogin.setEmail(resultado.getString("email"));
				modelLogin.setLogin(resultado.getString("login"));
				modelLogin.setSenha(resultado.getString("senha"));
				modelLogin.setNome(resultado.getString("nome"));
				modelLogin.setPerfil(resultado.getString("perfil"));
				modelLogin.setSexo(resultado.getString("sexo"));
				modelLogin.setFotouser(resultado.getString("fotouser"));
				modelLogin.setExtensaofotouser(resultado.getString("extensaofotouser"));
				modelLogin.setCep(resultado.getString("cep"));
				modelLogin.setLogradouro(resultado.getString("logradouro"));
				modelLogin.setBairro(resultado.getString("bairro"));
				modelLogin.setLocalidade(resultado.getString("localidade"));
				modelLogin.setUf(resultado.getString("uf"));
				modelLogin.setNumero(resultado.getString("numero"));
				modelLogin.setDataNascimento(resultado.getDate("datanascimento"));
				modelLogin.setRendamensal(resultado.getDouble("rendamensal"));
				
			}
			
			return modelLogin;
			
		}
	
	
	/*---------------------------------------------------------------------------------------------------------------------------*/
	
	/*Método para ver se não tem login repitido*/
	public boolean validarLogin(String login) throws Exception {
		
		String sql = "select count(1) > 0 as existe from model_login where upper(login) = upper('"+login+"');";
		
		PreparedStatement statement = connection.prepareStatement(sql);
		
		ResultSet resultado = statement.executeQuery();
		
		resultado.next();/*Para ele entrar nos resultados do sql*/
		return resultado.getBoolean("existe");
		
	}
	
	
	/*---------------------------------------------------------------------------------------------------------------------------*/
	
	/*Método de deletar usuários*/
	public void deletarUser(String idUser) throws Exception {
		
		String sql = "DELETE FROM model_login WHERE id = ? and useradmin is false;";
		
		PreparedStatement prepareSql = connection.prepareStatement(sql);
		
		prepareSql.setLong(1, Long.parseLong(idUser));
		
		prepareSql.executeUpdate();
		
		connection.commit();
	}
	
	
	/*---------------------------------------------------------------------------------------------------------------------------*/
	
	
	public List<ModelTelefone> listFone(Long idUserPai) throws Exception {
			
			List<ModelTelefone> retorno = new ArrayList<ModelTelefone>();
			
			String sql = "select * from telefone where usuario_pai_id = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			
			preparedStatement.setLong(1, idUserPai);
			
			ResultSet rs = preparedStatement.executeQuery();
			
			while (rs.next()) {
				
				ModelTelefone modelTelefone = new ModelTelefone();
				
				modelTelefone.setId(rs.getLong("id"));
				modelTelefone.setNumero(rs.getString("numero"));
				modelTelefone.setUsuario_cad_id(this.consultaUsuarioID(rs.getLong("usuario_cad_id")));/*Carregando não é um campo e sim um objeto, então precisa ser feito o relacionamento entre eles, no caso está vindo consultaUsuarioID, que está em daoUsusrioRepository*/
				modelTelefone.setUsuario_pai_id(this.consultaUsuarioID(rs.getLong("usuario_pai_id")));/*Carregando não é um campo e sim um objeto, então precisa ser feito o relacionamento entre eles, no caso está vindo consultaUsuarioID, que está em daoUsusrioRepository*/
				
				retorno.add(modelTelefone);
				
			}
			
			return retorno;
			
		}


	

}
