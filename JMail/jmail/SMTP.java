package jmail;

 

import com.sun.mail.pop3.*;
import com.sun.mail.imap.*;
import java.io.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import java.security.Security;
 
public class SMTP implements Defaults
{
    
    private Session session = null;
    private IMAPStore store = null;
    private String username, password;
    private boolean connected = false;
    
    public SMTP()
    {
        
    }
    
    public void setUserPass(String username, String password)
    {
        this.username = username;
        this.password = password;
    }
    
    String getUsername()
    {
        String usr = this.username.toLowerCase();
        if (!usr.endsWith("@gmail.com")) {
            usr += "@gmail.com";
            return usr;
        }else {
            return this.username;
        }
    }
    
    public void connect()
    {
        Properties props = new Properties();
        props.put("mail.smtp.host", GMAIL_SMTP_HOST);
        props.put("mail.smtp.auth", "true");
//         props.put("mail.debug", "true");
//         props.put("mail.debug", "false");
//         props.put("mail.smtp.port", "" + SMTP_SSL_PORT);
        props.put("mail.smtp.starttls.enable","true");
//         props.put("mail.smtp.socketFactory.port", "" + SMTP_SSL_PORT);
//         props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
//         props.put("mail.smtp.socketFactory.fallback", "false");
        
        this.session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        connected = true;
    }
    
    public MimeMessage createMimeMessage()
    {
        if (!connected) {
            connect();
        }
        return new MimeMessage(session);
    }
    
    public void sendMessage(Message msg) throws Exception
    {
//         Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
//         sess.setDebug(false);
        Transport.send(msg);
    }
}

    