package servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DAOTelefoneRepository;
import dao.DAOUsuarioRepository;
import model.ModelLogin;
import model.ModelTelefone;

@WebServlet("/ServletTelefone")
public class ServletTelefone extends ServletGenericUtil {

	private static final long serialVersionUID = 1L;

	private DAOUsuarioRepository daoUsuarioRepository = new DAOUsuarioRepository();
	
	private DAOTelefoneRepository daoTelefoneRepository = new DAOTelefoneRepository();

	public ServletTelefone() {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			
			String acao = request.getParameter("acao");/*Ao clicar no de acao, que no caso é o excluir, na tela */
			
			if (acao != null && !acao.isEmpty() && acao.equals("excluir")) {/*Condição do excluir*/
				
				String idfone = request.getParameter("id");/*Pega o telefone por parâmetro*/
				
				daoTelefoneRepository.deleteFone(Long.parseLong(idfone));/*Deletou*/
				
				String userpai = request.getParameter("userpai");/*Pegou o userpai*/
				
				ModelLogin modelLogin = daoUsuarioRepository.consultaUsuarioID(Long.parseLong(userpai));/*Consulta o objeto para retornar para a tela*/
				
				List<ModelTelefone> modelTelefones = daoTelefoneRepository.listFone(modelLogin.getId());/*Traz todos, menos os que foram deletados*/
				request.setAttribute("modelTelefones", modelTelefones);
				
				request.setAttribute("msg", "Telefone Excluido com sucesso");/*Mensagem que retorna na tela*/
				request.setAttribute("modelLogin", modelLogin);/*Retorna o objeto pai na tela, ou seja os telefones*/
				request.getRequestDispatcher("principal/telefone.jsp").forward(request, response);
				
				return;
			}
			
			

			String iduser = request.getParameter("iduser");

			if (iduser != null && !iduser.isEmpty()) {

				ModelLogin modelLogin = daoUsuarioRepository.consultaUsuarioID(Long.parseLong(iduser));
				
				List<ModelTelefone> modelTelefones = daoTelefoneRepository.listFone(modelLogin.getId());
				request.setAttribute("modelTelefones", modelTelefones);
				
				request.setAttribute("modelLogin", modelLogin);
				request.getRequestDispatcher("principal/telefone.jsp").forward(request, response);

			} else {
				List<ModelLogin> modelLogins = daoUsuarioRepository.consultaUsuarioList(super.getUserLogado(request));
				request.setAttribute("modelLogins", modelLogins);
				request.setAttribute("totalPagina", daoUsuarioRepository.totalPagina(this.getUserLogado(request)));/* Vai montar a paginação limitando em 5, antes de voltar para principal */
				request.getRequestDispatcher("principal/usuario.jsp").forward(request, response);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/*---------------------------------------------------------------------------------------------------------------------------*/

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
		
			String usuario_pai_id = request.getParameter("id");
			String numero = request.getParameter("numero");
			
			if (!daoTelefoneRepository.existeFone(numero, Long.valueOf(usuario_pai_id))) {/*=> Senão existir vai salvar, e vai dar msg de "Salvo com sucesso"*/
				
				ModelTelefone modelTelefone = new ModelTelefone();
				
				modelTelefone.setNumero(numero);
				modelTelefone.setUsuario_pai_id(daoUsuarioRepository.consultaUsuarioID(Long.parseLong(usuario_pai_id)));
				modelTelefone.setUsuario_cad_id(super.getUserLogadoObjt(request));
				
				daoTelefoneRepository.gravaTelefone(modelTelefone);
				
				
				request.setAttribute("msg", "Salvo com sucesso");/*Mostra a mensagem*/
				
			}else {/*Caso contrário seta a mensagem e executar o fluxo normal dele*/
				request.setAttribute("msg", "Telefone já existe");
			}
			
				List<ModelTelefone> modelTelefones = daoTelefoneRepository.listFone(Long.parseLong(usuario_pai_id));/*=> E faz o bloco de execução normal*/
				
				ModelLogin modelLogin = daoUsuarioRepository.consultaUsuarioID(Long.parseLong(usuario_pai_id));
				
				request.setAttribute("modelLogin", modelLogin);
				request.setAttribute("modelTelefones", modelTelefones);
				request.getRequestDispatcher("principal/telefone.jsp").forward(request, response);/*Redireciona para tela que queremos*/
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
