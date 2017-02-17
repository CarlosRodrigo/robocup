package simple_soccer_lib;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.UnknownHostException;

/**
 * Esta classe facilita lançar um time de agentes, possivelmente junto com o servidor.
 * 
 * Para criar um time, basta implementar o metodo <b>launchPlayer()</b> para que, a cada 
 * chamada (na forma de callback), a sua classe instancie algum agente (com a classe que 
 * quiser) para atuar por meio do PlayerCommander dado como paaâmetro.
 * 
 * Em seguida, o seu time pode ser inicializado com facilidade usando os metodos 
 * <b>launchTeam()</b> ou <b>launchTeamAndServer()</b>
 * 
 * @author Pablo Sampaio
 */
public abstract class AbstractTeam {
	private String hostName;
	private int port;

	private String teamName;
	private int numPlayers;
	
	public AbstractTeam(String name, int players, String host, int port) {
		this.hostName = host;
		this.port = port;
		this.teamName = name;
		this.numPlayers = players;
	}
	
	public AbstractTeam(String name, int players) {
		this.hostName = "localhost";
		this.port = 6000;
		this.teamName = name;
		this.numPlayers = players;
	}

	/**
	 * Recebe o índice do agente. O índice zero é para o goleiro. Uma subclasse deve
	 * instanciar alguma classe para controlar o agente (provavelmente em uma thread) 
	 * por meio do PlayerCommander dado como parametro. 
	 */
	protected abstract void launchPlayer(int ag, PlayerCommander commander);
	

	public final void launchTeam() throws UnknownHostException {
		PlayerCommander commander;
		
		System.out.println(" >> Iniciando o time...");
		for (int i = 0; i < this.numPlayers; i++) {
			commander = new PlayerCommander(teamName, hostName, port);
			launchPlayer(i, commander);
		}
	}
	
	public final void launchTeamAndServer() throws UnknownHostException {
		launchServer();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		launchTeam();
	}
	
	public final void launchServer() {
		try {
			System.out.println(" >> Iniciando servidor...");
			
			Runtime r = Runtime.getRuntime();
			Process p = r.exec("cmd /c tools\\startServer.cmd");
			p.waitFor();
//			BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
//			String line = "";
//			while ((line = b.readLine()) != null) {
//			  System.out.println(line);
//			  System.out.println(".");
//			}
//			b.close();

        } catch(Exception e) {
        	e.printStackTrace();
        	System.out.println("Não pode iniciar o servidor!");
        	return;
        }
	}

}
