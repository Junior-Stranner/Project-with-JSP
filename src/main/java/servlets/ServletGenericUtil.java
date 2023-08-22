package servlets;

import java.io.Serializable;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import dao.DAOUsuarioRepository;
import model.ModelLogin;

public class ServletGenericUtil extends HttpServlet implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	
	private DAOUsuarioRepository daoUsuarioRepository = new DAOUsuarioRepository();/*Objeto DAOUsuarioRepository*/
	
	
	/*Método que retorna o código do usuário*/
	public Long getUserLogado(HttpServletRequest request) throws Exception {
		
		HttpSession session = request.getSession();
		
		String usuarioLogado = (String) session.getAttribute("usuario");
		
		return daoUsuarioRepository.consultaUsuarioLogado(usuarioLogado).getId();
		
	}
	
	/*Método que retorna o objeto inteiro*/
	public ModelLogin getUserLogadoObjt(HttpServletRequest request) throws Exception {
		
		HttpSession session = request.getSession();
		
		String usuarioLogado = (String) session.getAttribute("usuario");
		
		return daoUsuarioRepository.consultaUsuarioLogado(usuarioLogado);
		
	}

}
