package hu.infokristaly.homework.xmpp.quickstart;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.sasl.SASLMechanism;
import org.jivesoftware.smack.sasl.javax.SASLDigestMD5Mechanism;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.util.TLSUtils;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;

import java.io.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class JabberSmackAPI implements ChatMessageListener, ConnectionListener {

	XMPPTCPConnection con;
	Chat chat;
	String threadId;

	public void login(String userName, String password) throws XMPPException, SmackException, IOException {
		XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder();
		configBuilder.setUsernameAndPassword(userName, password);
		configBuilder.setHost("localdebian");
		configBuilder.setServiceName("localdebian");
		configBuilder.setSecurityMode(ConnectionConfiguration.SecurityMode.required);
		configBuilder.setConnectTimeout(30000);
		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			configBuilder.setCustomSSLContext(ctx);
			TrustManager[] myTMs = new TrustManager[] { getTrustManager() };
			ctx.init(null, myTMs, null);

			configBuilder.setCompressionEnabled(false);
			configBuilder.setCustomSSLContext(ctx);
			configBuilder.setPort(5222);
			configBuilder.setSendPresence(true);
			configBuilder.setHostnameVerifier(new HostnameVerifier() {

				@Override
				public boolean verify(String host, SSLSession session) {
					return true;
				}

			});

			configBuilder.setSecurityMode(XMPPTCPConnectionConfiguration.SecurityMode.required);
			SASLMechanism mechanism = new SASLDigestMD5Mechanism();
			SASLAuthentication.registerSASLMechanism(mechanism);
			SASLAuthentication.blacklistSASLMechanism("SCRAM-SHA-1");
			SASLAuthentication.unBlacklistSASLMechanism("DIGEST-MD5");

			TLSUtils.acceptAllCertificates(configBuilder);

			con = new XMPPTCPConnection(configBuilder.build());
			con.addConnectionListener(this);
			con.connect();
			con.login();
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			e.printStackTrace();
		}
		// configBuilder.setCustomSSLContext(context);
	}

	public void disconnect() {
		con.disconnect();
	}

	@Override
	public void processMessage(Chat chat, Message message) {
		if (message.getType() == Message.Type.chat)
			System.out.println(chat.getParticipant() + " says: " + message.getBody());
	}

	public void sendMessage(String msg) {
		try {
			chat.sendMessage(msg);
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}
	}

	public void createChat() {
		ChatManager chatManager = ChatManager.getInstanceFor(con);
		chat = chatManager.createChat("touser@localdebian", "mythread", new ChatMessageListener() {

			@Override
			public void processMessage(Chat chat, Message message) {
				if (message.getBody() != null) {
					System.out.println(
							"Got XMPP message from chat " + chat.getParticipant() + " message - " + message.getBody());
				}
			}

		});
		threadId = chat.getThreadID();
		String usr = chat.getParticipant();
		System.out.println(usr + " " + threadId);
	}

	public static void main(String args[]) throws XMPPException, IOException, SmackException {
		JabberSmackAPI smack = new JabberSmackAPI();
		smack.login("pzoli", "password");
		Presence.Type type = Type.available;
		Presence presence = new Presence(type);
		presence.setMode(Presence.Mode.available);
		presence.setStatus("available");
		smack.createChat();
		smack.con.sendStanza(presence);

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String msg;

		System.out.println("Enter your message in the console:");
		System.out.println("-----\n");

		while (!(msg = br.readLine()).equals("bye")) {
			smack.sendMessage(msg);
		}

		smack.disconnect();
		System.exit(0);
	}

	protected static X509TrustManager getTrustManager() {
		X509TrustManager tm = new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
					throws java.security.cert.CertificateException {

				System.out.println("checkClientTrusted");

			}

			public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
					throws java.security.cert.CertificateException {

				System.out.println("checkServerTrusted:" + authType);

			}
		};

		return tm;
	}

	@Override
	public void connected(XMPPConnection connection) {
		logme("connected");
	}

	@Override
	public void authenticated(XMPPConnection connection, boolean resumed) {
		logme("authenticated");
	}

	@Override
	public void connectionClosed() {
		logme("connection closed");
	}

	@Override
	public void connectionClosedOnError(Exception e) {
		logme("connection closed on error");
	}

	@Override
	public void reconnectionSuccessful() {
		logme("reconnected");
	}

	@Override
	public void reconnectingIn(int seconds) {
		logme("reconnecting");
	}

	@Override
	public void reconnectionFailed(Exception e) {
		logme("reconnectionFailed" + e.getMessage());
	}

	private void logme(String msg) {
		System.out.println(msg);
	}
}
