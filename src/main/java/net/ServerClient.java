package net;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import util.Constants;
import util.PseudoLogger;

/**
 * @author Administrator
 * 
 */
public class ServerClient extends Thread {

	private final Socket clientSocket;
	private final Server server;
	private PrintWriter printWriter;
	private int serverId;
	private String playerName;
	private String playerColor;

	/**
	 * @param clientSocket
	 *            Socket
	 * @param server
	 *            Server
	 */
	protected ServerClient(final Socket clientSocket, final Server server) {
		super();
		this.clientSocket = clientSocket;
		this.server = server;
		setDaemon(true);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		BufferedReader reader;
		int targetid;
		String msg;
		int dividerPos;
		try {
			reader = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
			printWriter = new PrintWriter(new DataOutputStream(
					clientSocket.getOutputStream()));
			final String line = reader.readLine();
			if (line != null) {
				final String[] playerInfos = line
						.split(Constants.NET_DIVIDER_ESCAPED);
				playerName = playerInfos[0];
				playerColor = playerInfos[1];
				serverId = server.addClient(this);
				if (serverId > -1) {
					// Anmeldung bestaetigen
					server.sendLogin(serverId);
					System.out.println("+ Client " + getClientString()
							+ " hat sich angemeldet!");
					// Server beginnt bei diesem Client zu "horchen"
					for (String buffer; (buffer = reader.readLine()) != null;) {
						dividerPos = buffer.indexOf(Constants.NET_DIVIDER, 0);
						targetid = Integer.parseInt(buffer.substring(0,
								dividerPos));
						msg = buffer.substring(dividerPos + 1, buffer.length());
						server.send(serverId, targetid, msg);
					}
					// Abmeldung bestaetigen
					server.sendLogout(serverId);
					server.removeClient(serverId);
					System.out.println("- Client " + getClientString()
							+ " hat sich abgemeldet!");
				} else {
					// Anmeldung ablehnen
					sendError("Kein Slot frei!");
				}
				reader.close();
				printWriter.close();
			}
		} catch (final IOException exception) {
			PseudoLogger.getInstance().log(exception.getMessage());
		}
	}

	/**
	 * @param message
	 *            String
	 */
	protected void send(final String message) {
		printWriter.println(message);
		printWriter.flush();
	}

	/**
	 * @param message
	 *            String
	 */
	protected void sendError(final String message) {
		printWriter.println(Net.ERROR.name() + Constants.NET_DIVIDER
				+ Net.SERVER.name() + ": " + message);
		printWriter.flush();
	}

	/**
	 * @return String
	 */
	protected String getClientString() {
		return serverId + Constants.NET_DIVIDER + playerName
				+ Constants.NET_DIVIDER + playerColor;
	}

	/**
	 * @return String
	 */
	protected String getClientName() {
		return playerName;
	}
}