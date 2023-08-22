package connection;


import java.sql.Connection;
import java.sql.DriverManager;

public class SingleConnectionBanco {
	
	private static String banco = "jdbc:postgresql://ec2-44-210-36-247.compute-1.amazonaws.com:5432/dasa0blrjqe14v?sslmode=require&autoReconnect=true";
	private static String user = "gpgyqdalkqnhns";
	private static String senha = "a89aff2cf1a175c24d34f016d34aa193cfef826df99e9fa76c08c7d920095a77";
	private static Connection connection = null;
	
	public static Connection getConnection() {
		return connection;
	}
	
	static {/*Chama a classe direto*/
		conectar();
	}
	
	public SingleConnectionBanco() {/*Qunado tiver uma instância vai conectar*/
		conectar();
	}
	
	private static void conectar() {
		
		try {
			
			if (connection == null) {
				Class.forName("org.postgresql.Driver");/*Carrega o driver de conexão do banco*/
				connection = DriverManager.getConnection(banco, user, senha);
				connection.setAutoCommit(false);/*Para não efetuar alterações no banco sem nosso comando*/
			}
			
		} catch (Exception e) {
			e.printStackTrace();/*Mostar qualquer erro no momento de conectar*/
		}
	}

}
