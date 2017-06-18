package net;

import java.io.IOException;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Hashtable;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.output.XMLOutputter;

import game.GameSession;
import util.Constants;
import util.PseudoLogger;

/**
 * Server-Komponente. Hier wird fuer jeden Client ein Server-Prozess gestartet,
 * der vom Client gesendete Nachrichten annimmt und sie an die anderen Clients
 * weiterschickt. Die Clients werden als ServerClient-Objekte in einer ArrayList
 * mit einer laufenden Nummer als ID abgelegt.
 */
public class Server extends Thread {

	private final Map<String, ServerClient> clients;

	private ServerSocket socket;

	/**
	 * Startet einen Server-Thread der darauf wartet, dass sich Clients
	 * verbinden und startet fuer jeden Client einen ServerClient der auf
	 * Serverseite fuer die Kommunikation zustaendig ist und vom Client
	 * gesendete Nachrichten an den (oder die) entsprechenden Client(s)
	 * weiterleitet.
	 */
	public Server() {
		super();
		// Wert auf "Client" setzen, das diese Options an eingeloggte Spieler
		// ausgegeben werden
		GameSession.gameOptions.setNetwork(2);
		clients = new Hashtable<>(GameSession.gameOptions.getMaxPlayers());

		try {
			socket = new ServerSocket(GameSession.gameOptions.getPort());
		} catch (final IOException exception) {
			PseudoLogger.getInstance().log(exception.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			while (true) {
				new ServerClient(socket.accept(), this).start();
			}
		} catch (final SocketException exception) {
			// Socket closed
		} catch (final IOException exception) {
			PseudoLogger.getInstance().log(exception.getMessage());
		}
	}

	/**
	 * Hinzufuegen eines Clients.
	 * 
	 * @param client
	 *            ServerClient
	 * @return int
	 */
	protected synchronized int addClient(final ServerClient client) {
		for (int id = 0; id < GameSession.gameOptions.getPlayers().size(); id++) {
			if (!GameSession.gameOptions.getPlayer(id).isActive()) {
				GameSession.gameOptions.getPlayer(id).setActive(true);
				clients.put(Integer.toString(id), client);
				return id;
			}
		}
		return -1;
	}

	/**
	 * Entfernen eines Clients.
	 * 
	 * @param playerId
	 *            int
	 */
	protected void removeClient(final int playerId) {
		GameSession.gameOptions.getPlayer(playerId).setActive(false);
		clients.remove(Integer.toString(playerId));
	}

	/**
	 * Verschicken der Login-Bestaetigung.
	 * 
	 * @param targetId
	 *            int
	 */
	protected void sendLogin(final int targetId) {
		// Anmeldung bestaetigen
		send(targetId, targetId, Net.OK.name() + Constants.NET_DIVIDER + targetId);
		// Spieleinstellungen versenden, um das Spiel zu starten
		StringWriter writer = null;
		try {
			final XMLOutputter serializer = new XMLOutputter();
			writer = new StringWriter();
			serializer.output(new Document(GameSession.gameOptions.getData()), writer);
			writer.flush();
			send(targetId, targetId, Net.GAME.name() + Constants.NET_DIVIDER + writer.getBuffer().toString());
		} catch (final IOException exception) {
			PseudoLogger.getInstance().log(exception.getMessage());
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (final IOException exception) {
				PseudoLogger.getInstance().log(exception.getMessage());
			}
		}

		// Liste aller angemeldeten User (Format: Userid=Username) versenden
		final StringBuilder users = new StringBuilder(Net.USERLIST.name());
		for (final String key : clients.keySet()) {
			final ServerClient target = clients.get(key);
			users.append(Constants.NET_DIVIDER_DOUBLE + target.getClientString());
		}
		if (!Net.USERLIST.name().equals(users.toString())) {
			send(targetId, targetId, users.toString());
		}
		// Allen Clients neuen User mitteilen
		send(targetId, Constants.NET_ALL,
				Net.ADD.name() + Constants.NET_DIVIDER + getServerClient(targetId).getClientString());
	}

	/**
	 * Verschicken der Logout-Bestaetigung.
	 * 
	 * @param targetId
	 *            int
	 */
	protected void sendLogout(final int targetId) {
		// Allen Clients ausgeloggten User mitteilen
		send(targetId, Constants.NET_ALL,
				Net.REMOVE.name() + Constants.NET_DIVIDER + getServerClient(targetId).getClientString());
	}

	/**
	 * Verschickt eine Nachricht.
	 * 
	 * @param senderId
	 *            Absender
	 * @param targetId
	 *            Empfaenger
	 * @param message
	 *            Nachricht
	 */
	protected void send(final int senderId, final int targetId, final String message) {
		final ServerClient sender = getServerClient(senderId);
		// Zeilenumbrueche entfernen, da sonst der Text nicht vollstaendig
		// uebertragen wird
		String msg = message.replaceAll("\r\n", " ");
		msg = msg.replaceAll("\n", " ");
		if (targetId == Constants.NET_ALL) {
			// An alle schreiben
			for (final String key : clients.keySet()) {
				final ServerClient target = clients.get(key);
				target.send(msg);
			}
		} else if (clients.get(Integer.toString(targetId)) == null) {
			// Empfaenger existiert nicht
			sender.sendError("Client " + getServerClient(targetId).getClientName() + " existiert nicht!");
		} else {
			// An einen bestimmten Empfaenger schreiben
			if (senderId != targetId) {
				sender.send(msg);
			}
			final ServerClient target = getServerClient(targetId);
			target.send(msg);
		}
	}

	/**
	 * Gibt den ServerClient mit der ID clientId zurueck.
	 * 
	 * @param clientId
	 *            ID des ServerClients
	 * @return ServerClient
	 */
	private ServerClient getServerClient(final int clientId) {
		return clients.get(Integer.toString(clientId));
	}

	/**
	 * Server beenden.
	 */
	public void beenden() {
		// Allen Clients die Beendigung des Servers mitteilen
		for (final String key : clients.keySet()) {
			final ServerClient target = clients.get(key);
			target.send(Net.REMOVE.name() + Constants.NET_DIVIDER + Net.SERVER.name());
		}
		try {
			if (socket != null) {
				socket.close();
			}
		} catch (final IOException exception) {
			PseudoLogger.getInstance().log(exception.getMessage());
		} finally {
			socket = null;
		}
	}
}