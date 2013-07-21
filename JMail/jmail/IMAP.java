package jmail;

 

import com.sun.mail.pop3.*;
import com.sun.mail.imap.*;
import java.io.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import java.security.Security;
 
public class IMAP implements Defaults
{
    
    private Session session = null;
    private IMAPStore store = null;
    private String username, password;
    private IMAPFolder folder;
    private IMAPFolder inbox;
    
    public IMAP()
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
        if (usr.indexOf("@") >= 0) {
            return usr;
        }
        if (!usr.endsWith("@gmail.com")) {
            usr += "@gmail.com";
            return usr;
        }else {
            return this.username;
        }
    }
    
    public MimeMessage createMimeMessage()
    {
        return new MimeMessage(session);
    }
    
    public void saveDraft(Message msg) throws Exception
    {
        Folder drafts = store.getFolder(DRAFTS);
        Message msgs[] = { msg };
        drafts.appendMessages(msgs);
    }
    
    public void sendSSLMessage(Message msg) throws Exception
    {
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        
        Properties props = new Properties();
        props.put("mail.smtp.host", GMAIL_SMTP_HOST);
        props.put("mail.smtp.auth", "true");
//         props.put("mail.imap.fetchsize", 32000);
//         props.put("mail.debug", "true");
        props.put("mail.debug", "false");
        props.put("mail.smtp.port", "" + SMTP_SSL_PORT);
        props.put("mail.smtp.socketFactory.port", "" + SMTP_SSL_PORT);
        props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.put("mail.smtp.socketFactory.fallback", "false");
        
        Session sess = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    
//         sess.setDebug(false);
        Transport.send(msg);
    }
    
    public void connect() throws Exception
    {
        String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        
        Properties props = new Properties();
        
        props.setProperty("mail.imap.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.store.protocol", IMAP_PROTOCOL);
        props.setProperty("mail.imap.socketFactory.fallback", "false");
        props.setProperty("mail.imap.port", "" + IMAP_SSL_PORT);
        props.setProperty("mail.imap.socketFactory.port", "" + IMAP_SSL_PORT);
        
        URLName url = new URLName("imaps", GMAIL_IMAP_HOST, IMAP_SSL_PORT, "", username, password);
        
        session = Session.getInstance(props, null);
        store = (IMAPStore) session.getStore("imaps");
        store.connect(GMAIL_IMAP_HOST, username, password);
        
        this.inbox = (IMAPFolder) store.getDefaultFolder();
        this.inbox = (IMAPFolder) store.getFolder(INBOX);
        this.inbox.open(Folder.READ_ONLY);
    }
    
    public void moveToInbox(int msgNo) throws Exception
    {
        Message [] m = {folder.getMessage(msgNo)};
        folder.copyMessages(m, store.getFolder(INBOX));
    }
    
    public void moveToDrafts(int msgNo) throws Exception
    {
        Message [] m = {folder.getMessage(msgNo)};
        folder.copyMessages(m, store.getFolder(DRAFTS));
    }
    
    public void moveToSent(int msgNo) throws Exception
    {
        Message [] m = {folder.getMessage(msgNo)};
        folder.copyMessages(m, store.getFolder(SENT_MAIL));
    }
    
    public void moveToTrash(int msgNo) throws Exception
    {
        Message [] m = {folder.getMessage(msgNo)};
        folder.copyMessages(m, store.getFolder(TRASH));
    }
    
    public void moveToSpam(int msgNo) throws Exception
    {
        Message [] m = {folder.getMessage(msgNo)};
        folder.copyMessages(m, store.getFolder(SPAM));
    }
    
    public int expunge() throws Exception
    {
        if (folder.getFullName().equals(TRASH)) {
            Message [] m = folder.expunge();
            return m.length;
        }
        return -1;
    }
    
    public void markRead(int msgNo) throws Exception
    {
        Message msg = folder.getMessage(msgNo);
        msg.setFlag(Flags.Flag.SEEN, true);
    }
    
    public void markUnread(int msgNo) throws Exception
    {
        Message msg = folder.getMessage(msgNo);
        msg.setFlag(Flags.Flag.SEEN, false);
    }
    
    public void setStarred(int msgNo) throws Exception
    {
        Message msg = folder.getMessage(msgNo);
        msg.setFlag(Flags.Flag.FLAGGED, true);
    }
    
    public void setUnstarred(int msgNo) throws Exception
    {
        Message msg = folder.getMessage(msgNo);
        msg.setFlag(Flags.Flag.FLAGGED, false);
    }
    
    public void delete(int msgNo) throws Exception
    {
        Message msg = folder.getMessage(msgNo);
        msg.setFlag(Flags.Flag.DELETED, true);
    }
    
    public int getUnreadMessageCount() throws Exception
    {
        return folder.getUnreadMessageCount();
    }
    
    public int getUnreadMessageCount(String fn) throws Exception
    {
        openFolder(fn);
        int n = folder.getUnreadMessageCount();
        closeFolder();
        return n;
    }
    
    public void openFolder(String folderName) throws Exception
    {
//         if (folder != null && folder.isOpen()) {
//             folder.close(true);
//         }
        // Open the Folder
        folder = (IMAPFolder) store.getDefaultFolder();
        folder = (IMAPFolder) folder.getFolder(folderName);
        
        if (folder == null) {
            throw new Exception("Invalid folder");
        }
        
        // try to open read/write and if that fails try read-only
        try {
            folder.open(Folder.READ_WRITE);
        } catch (MessagingException ex) {
            folder.open(Folder.READ_ONLY);
        }
    }
    
    public void closeFolder() throws Exception
    {
        if (folder != null && folder.isOpen()) {
            folder.close(false);
        }
    }
    
    public String getFolderName()
    {
        if (folder != null && folder.isOpen()) {
            return folder.getName();
        }
        return "";
    }
    
    public String getFullFolderName()
    {
        if (folder != null && folder.isOpen()) {
            return folder.getFullName();
        }
        return "";
    }
    
    public int getMessageCount() throws Exception
    {
        return folder.getMessageCount();
    }
    
    public int getNewMessageCount() throws Exception
    {
        return folder.getNewMessageCount();
    }
    
    public void disconnect() throws Exception
    {
        this.inbox.close(false);
        closeFolder();
        store.close();
    }
    
    public boolean hasNewMessages(Date d) throws Exception
    {
        if (inbox != null && inbox.isOpen()) {
            Message m = inbox.getMessage(inbox.getMessageCount());
            Date rd = m.getReceivedDate();
            Flags f = m.getFlags();
            if (rd.after(d) && !f.contains(Flags.Flag.SEEN)) {
                return true;
            }
        }
        return false;
//         return folder.hasNewMessages();
    }
    
    public boolean hasNewMessages() throws Exception
    {
        if (inbox != null && inbox.isOpen()) {
            Message m = inbox.getMessage(inbox.getMessageCount());
            Flags f = m.getFlags();
            if (f.contains(Flags.Flag.RECENT)) {
                return true;
            }
        }
        return false;
    }
    
    public int getUnreadMessageCount2() throws Exception
    {
        int n = getMessageCount();
        int count = 0;
        for (int i = 1; i <= n; i++) {
            Message m = getMessage(i);
            Flags flags = m.getFlags();
            if (!flags.contains(Flags.Flag.SEEN)) {
                count++;
            }
        }
        return count;
    }
    
    public Message getMessage(int messageNo) throws Exception
    {
        try {
            Message m = folder.getMessage(messageNo);
            return m;
        } catch (IndexOutOfBoundsException iex) {
            System.out.println("Message number out of range");
            return null;
        }
    }
    
    public String getStringMessagePreviews(int from, int to) throws Exception
    {
        String preview = new String();
        String prv[] = new String[to - from];
        for (int i = from; i < to; i++) {
            Message m = getMessage(i + 1);
            Address[] a;
            // FROM
            if ((a = m.getFrom()) != null) {
                prv[i] = "From: ";
                for (int j = 0; j < a.length; j++) {
                    prv[i] += a[j].toString();
                    if (j != a.length) {
                        prv[i] += ", ";
                    }
                }
            }
            
            // TO
//             if ((a = m.getRecipients(Message.RecipientType.TO)) != null) {
//                 for (int j = 0; j < a.length; j++) {
//                     prv += "To: " + a[j].toString());                
//                 }
//             }
            
            // SUBJECT
            prv[i] += ("  Sub: " + m.getSubject());
            
            // DATE
            Date d = m.getSentDate();
            prv[i] += ("  Dated: " + (d != null ? d.toString() : "Unknown"));
            preview += prv[i] + "\n\n";
        }
        
        return preview;
    }
    
    public MessagePreview getMessagePreview(int msgNo) throws Exception
    {
        MessagePreview prv = new MessagePreview();
        Message m = folder.getMessage(msgNo + 1);
        Address[] a;
        // FROM
        if ((a = m.getFrom()) != null) {
            String frm = new String();
            for (int j = 0; j < a.length; j++) {
                frm += a[j].toString();
                if ((j + 1) != a.length) {
                    frm += ", ";
                }
            }
            prv.setFrom(frm);
        }
        
        // TO
//             if ((a = m.getRecipients(Message.RecipientType.TO)) != null) {
//                 for (int j = 0; j < a.length; j++) {
//                     prv += "To: " + a[j].toString());                
//                 }
//             }
        
        // SUBJECT
        prv.setSubject(m.getSubject());
        
        // DATE
        Date d = m.getReceivedDate();
        String date = (d != null ? d.toString() : "Unknown");
        prv.setDate(date);
        
        // IS READ?
        Flags flags = m.getFlags();
        if (flags.contains(Flags.Flag.SEEN)) {
            prv.setRead(true);
        }
        // IS STARRED?
        if (flags.contains(Flags.Flag.FLAGGED)) {
            prv.setStarred(true);
        }
        
        return prv;
    }
    
    public MessagePreview[] getMessagePreviews(int from, int to) throws Exception
    {
        MessagePreview prv[] = new MessagePreview[to - from];
        for (int k = from, i = 0; k < to; i++, k++) {
            prv[i] = new MessagePreview();
            Message m = folder.getMessage(k + 1);
            Address[] a;
            // FROM
            if ((a = m.getFrom()) != null) {
                String frm = new String();
                for (int j = 0; j < a.length; j++) {
                    frm += a[j].toString();
                    if ((j + 1) != a.length) {
                        frm += ", ";
                    }
                }
                prv[i].setFrom(frm);
            }
            
            // TO
//             if ((a = m.getRecipients(Message.RecipientType.TO)) != null) {
//                 for (int j = 0; j < a.length; j++) {
//                     prv += "To: " + a[j].toString());                
//                 }
//             }
            
            // SUBJECT
            prv[i].setSubject(m.getSubject());
            
            // DATE
            Date d = m.getSentDate();
            String date = (d != null ? d.toString() : "Unknown");
            prv[i].setDate(date);
            
            // IS READ?
            Flags flags = m.getFlags();
            if (flags.contains(Flags.Flag.SEEN)) {
                prv[i].setRead(true);
            }
            // IS STARRED?
            if (flags.contains(Flags.Flag.FLAGGED)) {
                prv[i].setStarred(true);
            }
        }
        
        return prv;
    }
}