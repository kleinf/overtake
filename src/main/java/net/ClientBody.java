package net;

import gui.GameFrame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketException;

import util.Logger;

/**
 * @author Administrator
 * 
 */
public class ClientBody extends Thread {

	private final InputStream inputStream;
	private final GameFrame parentFrame;

	/**
	 * @param inputStream
	 *            InputStream
	 * @param parentFrame
	 *            GameFrame
	 */
	public ClientBody(final InputStream inputStream, final GameFrame parentFrame) {
		super();
		this.inputStream = inputStream;
		this.parentFrame = parentFrame;
		setDaemon(true);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		BufferedReader bufferedReader;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(
					inputStream));
			String buffer = bufferedReader.readLine();
			while (buffer != null) {
				parentFrame.receive(buffer);
				buffer = bufferedReader.readLine();
			}

		} catch (final SocketException socketException) {
			// Socket closed
		} catch (final IOException exception) {
			Logger.getInstance().log(exception.getMessage());
		}
	}
}