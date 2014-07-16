package gui;

import game.GameSession;
import game.NetOptions;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.Socket;
import java.net.UnknownHostException;

import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import net.ClientBody;
import net.Net;
import net.Server;

import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import util.Constants;
import util.Logger;
import util.Mode;

/**
 * @author Administrator
 * 
 */
public class GameFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private GameMenue gameMenue = null;
	private GamePanel gamePanel = null;
	private EditorPanel editorPanel = null;
	private Server serverThread = null;
	private NetOptions netOptions = null;
	private Socket clientSocket = null;
	private PrintWriter netOut;
	private BufferedReader netIn;

	/**
	 * Konstruktor.
	 */
	public GameFrame() {
		super();
		setContentPane(new StartPanel());
		pack();
		gameMenue = new GameMenue(this);
		setJMenuBar(gameMenue.getGameMenuBar());
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

	/**
	 * Neues Spiel initialisieren. Falls es ein Netzwerk-Spiel werden soll, wird
	 * ein {@link Server Server-Prozess} {@link Server#run gestartet}.
	 * Anschliessend wird zusaetzlich ein {@link #joinNetGame(NetOptions)
	 * Client} gestartet, der sich mit dem Server verbindet.
	 * 
	 * @param mode
	 *            Mode
	 */
	public void init(final Mode mode) {
		if (serverThread != null) {
			// Wenn ein Server laeuft wird dieser gestoppt
			serverThread.beenden();
			serverThread = null;
		}
		if (GameSession.gameOptions.getNetwork() == 1) {
			hostNetGame();
			netOptions = new NetOptions();
			netOptions.setHost(GameSession.gameOptions.getHost());
			netOptions.setPort(GameSession.gameOptions.getPort());
			netOptions.setPlayerName(GameSession.gameOptions.getPlayer(0)
					.getPlayerName());
			netOptions.setPlayerColor(GameSession.gameOptions.getPlayer(0)
					.getPlayerColor());
			joinNetGame(netOptions);
		} else {
			start(null, false, mode);
		}
	}

	/**
	 * Server starten.
	 */
	private void hostNetGame() {
		serverThread = new Server();
		serverThread.setDaemon(true);
		serverThread.start();
		System.out.println("Server gestartet...");
	}

	/**
	 * Client starten. Hier wird eine Verbindung zu einem bestehenden Server
	 * aufgebaut.
	 * 
	 * @param netOptions
	 *            NetOptions
	 */
	public void joinNetGame(final NetOptions netOptions) {
		if (!"".equals(netOptions.getPlayerName())) {
			try {
				this.netOptions = netOptions;
				// Verbindung zum Server aufbauen
				clientSocket = new Socket(netOptions.getHost(),
						netOptions.getPort());
				netOut = new PrintWriter(new DataOutputStream(
						clientSocket.getOutputStream()));
				netIn = new BufferedReader(new InputStreamReader(System.in));
				// Am Server anmelden
				netOut.println(netOptions.getPlayerName()
						+ Constants.NET_DIVIDER + netOptions.getPlayerColor());
				System.out.println("Verbindung wird aufgebaut...");
				netOut.flush();
				new ClientBody(clientSocket.getInputStream(), this).start();
			} catch (final UnknownHostException exception) {
				Logger.getInstance().log(exception.getMessage());
			} catch (final IOException exception) {
				Logger.getInstance().log(exception.getMessage());
			}
		}
	}

	/**
	 * Gespeichertes Spiel starten.
	 * 
	 * @param filename
	 *            String
	 * @param mode
	 *            Mode
	 */
	protected void load(final String filename, final Mode mode) {
		FileReader filereader = null;
		BufferedReader bufferedreader = null;
		try {
			filereader = new FileReader(filename);
			bufferedreader = new BufferedReader(filereader);
			String line = bufferedreader.readLine();
			final StringBuilder stringBuilder = new StringBuilder();
			while (line != null) {
				stringBuilder.append(line);
				line = bufferedreader.readLine();
			}
			start(stringBuilder.toString(), true, mode);
		} catch (final IOException exception) {
			Logger.getInstance().log(exception.getMessage());
		} finally {
			if (bufferedreader != null) {
				try {
					bufferedreader.close();
				} catch (final IOException exception) {
					Logger.getInstance().log(exception.getMessage());
				}
			}
			if (filereader != null) {
				try {
					filereader.close();
				} catch (final IOException exception) {
					Logger.getInstance().log(exception.getMessage());
				}
			}
		}
	}

	/**
	 * Spiel starten.
	 * 
	 * @param xmlData
	 *            String
	 * @param updateTree
	 *            boolean
	 * @param mode
	 *            Mode
	 */
	protected void start(final String xmlData, final boolean updateTree,
			final Mode mode) {
		JPanel panel = null;
		if (mode == Mode.MODE_PLAY) {
			panel = new GamePanel(this, xmlData);
			editorPanel = null;
			gamePanel = (GamePanel) panel;
			gamePanel.setMode(mode);
		}
		if (mode == Mode.MODE_EDIT) {
			panel = new EditorPanel(this, xmlData);
			gamePanel = null;
			editorPanel = (EditorPanel) panel;
			editorPanel.setMode(mode);
		}
		setContentPane(panel);
		pack();
		setJMenuBar(gameMenue.getGameMenuBar());
		if (updateTree) {
			SwingUtilities.updateComponentTreeUI(this);
		}

		if (mode == Mode.MODE_PLAY) {
			gamePanel.start();
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Overridden to stop the thread if window is closed.
	 * 
	 * @see javax.swing.JFrame#processWindowEvent(java.awt.event.WindowEvent)
	 */
	@Override
	protected void processWindowEvent(final WindowEvent event) {
		super.processWindowEvent(event);
		if (event.getID() == WindowEvent.WINDOW_CLOSING) {
			exitGame();
		}
	}

	/**
	 * 
	 */
	private void closeConnection() {
		if (netIn != null) {
			System.out.println("Client gestoppt...");
			try {
				netIn.close();
				netIn = null;
			} catch (final IOException exception) {
				Logger.getInstance().log(exception.getMessage());
			}
		}
		if (netOut != null) {
			netOut.close();
			netOut = null;
		}
		if (serverThread != null) {
			serverThread.beenden();
			serverThread = null;
		}
		if (gamePanel != null) {
			gamePanel.stop();
			gamePanel = null;
		}
		if (editorPanel != null) {
			editorPanel = null;
		}
	}

	/**
	 * 
	 */
	protected void quitGame() {
		closeConnection();
		setContentPane(new StartPanel());
		pack();
		gameMenue = new GameMenue(this);
		setJMenuBar(gameMenue.getGameMenuBar());
		SwingUtilities.updateComponentTreeUI(this);
	}

	/**
	 * 
	 */
	protected void exitGame() {
		closeConnection();
		System.exit(0);
	}

	/**
	 * Empfaengt eine Nachricht. Dabei kann es sich um eine Systemnachricht
	 * handeln oder einfache Chat-Nachrichten.
	 * 
	 * @param message
	 *            String
	 */
	public void receive(final String message) {
		String buffer;
		if (message.startsWith(Net.ZUG.name() + Constants.NET_DIVIDER)) {
			// Gesendeter Spielzug wurde empfangen
			buffer = message.substring((Net.ZUG.name() + Constants.NET_DIVIDER)
					.length());
			final String[] zug = buffer.split(Constants.NET_DIVIDER_ESCAPED);
			gamePanel.netBoardClick(Integer.parseInt(zug[0]),
					Integer.parseInt(zug[1]));
		} else if (message.startsWith(Net.MODE.name() + Constants.NET_DIVIDER)) {
			// Gesendeter Moduswechsel wurde empfangen
			buffer = message
					.substring((Net.MODE.name() + Constants.NET_DIVIDER)
							.length());
			gamePanel.setNetMode(buffer);
		} else if (message.startsWith(Net.OK.name() + Constants.NET_DIVIDER)) {
			// Gesendete Anmeldebestaetigung wurde empfangen
			buffer = message.substring((Net.OK.name() + Constants.NET_DIVIDER)
					.length());
			netOptions.setId(buffer);
			System.out.println("Verbindung mit ID " + buffer + " bestaetigt");
		} else if (message.startsWith(Net.ERROR.name() + Constants.NET_DIVIDER)) {
			// Gesendete Anmeldung wurde abgelehnt
			buffer = message
					.substring((Net.ERROR.name() + Constants.NET_DIVIDER)
							.length());
			System.out.println("Fehler: " + buffer);
		} else if (message.startsWith(Net.GAME.name() + Constants.NET_DIVIDER)) {
			// Gesendete Spieleinstellungen wurden empfangen
			buffer = message
					.substring((Net.GAME.name() + Constants.NET_DIVIDER)
							.length());
			try {
				final StringReader reader = new StringReader(buffer);
				final SAXBuilder builder = new SAXBuilder();
				GameSession.gameOptions.setData(builder.build(reader)
						.getRootElement());
				start(null, false, Mode.MODE_PLAY);
			} catch (final IOException exception) {
				Logger.getInstance().log(exception.getMessage());
			} catch (final JDOMException exception) {
				Logger.getInstance().log(exception.getMessage());
			}
		} else if (message.startsWith(Net.USERLIST.name()
				+ Constants.NET_DIVIDER)) {
			// Gesendete Useriste wurde empfangen
			buffer = message
					.substring((Net.USERLIST.name() + Constants.NET_DIVIDER_DOUBLE)
							.length());
			final String[] clients = buffer
					.split(Constants.NET_DIVIDER_DOUBLE_ESCAPED);
			for (final String client : clients) {
				final String[] clientData = client
						.split(Constants.NET_DIVIDER_ESCAPED);
				gamePanel.activatePlayer(Integer.parseInt(clientData[0]),
						clientData[1], Integer.parseInt(clientData[2]));
			}
		} else if (message.startsWith(Net.ADD.name() + Constants.NET_DIVIDER)) {
			// Neuer Spieler hat sich angemeldet
			buffer = message.substring((Net.ADD.name() + Constants.NET_DIVIDER)
					.length());
			final String[] clientData = buffer
					.split(Constants.NET_DIVIDER_ESCAPED);
			if (!netOptions.getId().equals(clientData[0])) {
				gamePanel.activatePlayer(Integer.parseInt(clientData[0]),
						clientData[1], Integer.parseInt(clientData[2]));
				gamePanel.getChatPanel().appendChatMessage(
						clientData[1] + " hat sich angemeldet\n");
			}
		} else if (message
				.startsWith(Net.REMOVE.name() + Constants.NET_DIVIDER)) {
			// Alter Spieler hat sich abgemeldet
			buffer = message
					.substring((Net.REMOVE.name() + Constants.NET_DIVIDER)
							.length());
			final String[] clientData = buffer
					.split(Constants.NET_DIVIDER_ESCAPED);
			if (Net.SERVER.name().equals(clientData[0])) {
				gamePanel.getChatPanel().appendChatMessage(
						"Server wurde beendet!\n");
			} else if (!netOptions.getId().equals(clientData[0])) {
				gamePanel.deactivatePlayer(Integer.parseInt(clientData[0]));
				gamePanel.getChatPanel().appendChatMessage(
						clientData[1] + " hat sich abgemeldet\n");
			}
		} else if (message.startsWith(Net.CHAT.name() + Constants.NET_DIVIDER)) {
			// Gesendete Chatnachricht wurde empfangen
			buffer = message
					.substring((Net.CHAT.name() + Constants.NET_DIVIDER)
							.length());
			gamePanel.getChatPanel().appendChatMessage(buffer + "\n");
		}
	}

	/**
	 * Sendet eine Nachricht an einen bestimmten Client.
	 * 
	 * @param targetId
	 *            int
	 * @param message
	 *            String
	 */
	public void send(final int targetId, final String message) {
		if (targetId < -1 || message.trim().length() == 0) {
			receive("Bitte erst Empfaenger auswaehlen!\n");
		} else {
			netOut.println(targetId + Constants.NET_DIVIDER + message);
			netOut.flush();
		}
	}

	/**
	 * @return int
	 */
	protected int getEigeneId() {
		return Integer.parseInt(netOptions.getId());
	}

	/**
	 * @return String
	 */
	public String getEigenerName() {
		return netOptions.getPlayerName();
	}

	/**
	 * @return GamePanel
	 */
	protected GamePanel getGamePanel() {
		return gamePanel;
	}

	/**
	 * @return EditorPanel
	 */
	protected EditorPanel getEditorPanel() {
		return editorPanel;
	}
}