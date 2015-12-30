/**
 * Copyright (C) 2011  Adam Hocek. Contact: ahocek@gmail.com,  Udaya K Ghattamaneni. 
 * Contact: ghattamaneni.uday@gmail.com 
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301 USA
 */
package hu.infokristaly.homework.xmpp.quickstart;

import javax.security.auth.callback.CallbackHandler;

import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.sasl.SASLMechanism;

public class MySASLDigestMD5Mechanism extends SASLMechanism {



	public MySASLDigestMD5Mechanism(SASLAuthentication saslAuthentication) {

        super();
        

    }

	@Override
    protected void authenticateInternal(CallbackHandler cbh) throws SmackException {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    protected byte[] getAuthenticationText() throws SmackException {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public String getName() {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public int getPriority() {
	    // TODO Auto-generated method stub
	    return 0;
    }

	@Override
    public void checkIfSuccessfulOrThrow() throws SmackException {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    protected SASLMechanism newInstance() {
	    // TODO Auto-generated method stub
	    return null;
    }

}
