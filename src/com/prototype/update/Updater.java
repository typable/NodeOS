package com.prototype.update;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.prototype.Prototype;
import com.prototype.http.constants.Header;
import com.prototype.logger.Logger;


public class Updater {

	public static final String CHECKIP = "http://checkip.amazonaws.com";
	public static final String LOG_PREFIX = "[Updater] ";

	private Logger logger;

	public Updater(Prototype prototype) {

		logger = prototype.getLogger();
	}

	public void updateDomain(String domain, String auth, String server) {

		logger.info(LOG_PREFIX + "Checking domain availability...");

		try {

			String DOMAIN_IP_ADDRESS = InetAddress.getByName(domain).getHostAddress();
			String PUBLIC_IP_ADDRESS = getPublicIPAddress();

			if(!DOMAIN_IP_ADDRESS.equals(PUBLIC_IP_ADDRESS)) {

				URL url = new URL(server + "?hostname=" + domain);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				connection.setRequestProperty("Authorization", "Basic " + auth);
				connection.setRequestProperty("User-Agent", "Prototype-Studio NodeOS " + Prototype.VERSION);

				int code = connection.getResponseCode();
				String response = null;

				if(code == 200) {

					BufferedReader in = new BufferedReader(new InputStreamReader((InputStream) connection.getContent()));

					response = in.readLine();
					connection.disconnect();

					if(response.startsWith("badauth")) {

						logger.warn(LOG_PREFIX + "Authorization failed! (" + response + ")");
					}
					else if(response.startsWith("good")) {

						logger.info(LOG_PREFIX + "Domain updated successfully. (" + response + ")");
					}
					else if(response.startsWith("nochg")) {

						logger.info(LOG_PREFIX + "Nothing hast changed. (" + response + ")");
					}
					else {

						logger.warn(LOG_PREFIX + "Unknown response? (" + response + ")");
					}
				}
				else {

					logger.warn(LOG_PREFIX + "Server is not reachable! Code: " + code);
				}
			}
			else {

				logger.info(LOG_PREFIX + "Domain up to date. (" + DOMAIN_IP_ADDRESS + ")");
			}
		}
		catch(Exception ex) {

			logger.error(LOG_PREFIX + "Update domain failed!", ex);
		}
	}

	public void updateSoftware() {

		logger.info(LOG_PREFIX + "Checking for updates...");

		try {

			URL url = new URL("http://prototype-studio.de/update?version=" + Prototype.VERSION);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(5000);
			connection.setRequestProperty("User-Agent", "Prototype-Studio NodeOS " + Prototype.VERSION);

			int code = connection.getResponseCode();

			if(code == 200) {

				String download = connection.getHeaderField(Header.CONTENT_DISPOSITION.getCode());
				String length = connection.getHeaderField(Header.CONTENT_LENGTH.getCode());

				if(download != null && length != null) {

					BufferedInputStream input = new BufferedInputStream(connection.getInputStream());

					byte[] data = input.readNBytes(Integer.valueOf(length));

					Files.write(Paths.get("E:/temp/NodeOS.jar"), data);
				}
			}
		}
		catch(SocketTimeoutException ex) {

			logger.warn(LOG_PREFIX + "Server is not reachable!");
		}
		catch(Exception ex) {

			logger.error(LOG_PREFIX + "Update software failed!", ex);
		}
	}

	private String getPublicIPAddress() {

		try {

			URL url = new URL(CHECKIP);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

			return in.readLine();
		}
		catch(Exception e) {

			logger.error(LOG_PREFIX + "Server is not reachable! IP-Address: " + CHECKIP);
		}

		return null;
	}
}
