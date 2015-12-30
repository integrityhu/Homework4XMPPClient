package hu.infokristaly.homework.xmpp;

import hu.infokristaly.homework.xmpp.quickstart.JabberSmackAPI;

import java.io.IOException;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

public class Homework4XMPP {

	public static void main(String[] args) {
		try {
	        JabberSmackAPI.main(args);
        } catch (XMPPException | IOException e) {
	        e.printStackTrace();
        } catch (SmackException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        }
	}

}