package jmail;

 

import java.awt.*;

public interface Defaults
{
    public static final String WELCOME_MSG = "Welcome to JMail v1.0 - The GMail client for Java";
    public static final String AUTHENTICATION_FAILED_MSG = "Authentication failed. Bad username/password";
    public static final int MSG_PER_PAGE = 10;//20;
    public static final long NOTIFICATION_INTERVAL = 5000;
    
    public static final String ENC_HEADER_NAME = "X-Jmail-Encrypted";
    public static final String ENC_HEADER_VALUE = "RSA";
    public static final int KEY_BASE = 10;  // hex
    public static final int ENC_BUFFER_SIZE = 128;
    public static final char ENC_SEPARATOR = 'x';
    public static final int B64_WRAP_AT = 128;
    public static final String ENC_TEMPFILE = "temp.rsa";
    public static final String GTALK_AUTH_MECH = "SASL PLAIN";  // authentication mechanism
    
    public static final String CONTACTS_FILE = "contacts.csv";
    
    public static final String LAF_FILE = "laf.properties";
    public static final String DEFAULT_LAF = "de.javasoft.plaf.synthetica.SyntheticaBlackEyeLookAndFeel";
    
    public static final String ICON_PATH = "icons/";
    public static final String READ_ICON = "read.png";//"read.gif";
    public static final String UNREAD_ICON = "unread.png";//"unread.gif";
    public static final String LOGO = "jmaillogo2.png";
    public static final String GMAIL_ICON = "mail.gif";//"email.png";//"gmail.gif";
    public static final String STAR_ICON = "star.gif";
    public static final String INBOX_ICON = "inbox.png";//"3D-Mail-icon.png";//"inbox.gif";
    public static final String SENTMAIL_ICON = "outbox.png";//"Uploads-icon.png";//"outbox.gif";
    public static final String STARRED_ICON = "starred.png";//"Star-icon.png";//"starred.gif";
    public static final String DRAFTS_ICON = "drafts.png";//"3D-Documents-icon.png";//"drafts.gif";
    public static final String SPAM_ICON = "spam.png";//"Ekisho-Deep-Ocean-Private-icon.png";//"spam.gif";
    public static final String TRASH_ICON = "trash.png";//"Trash-Empty-icon.png";//"trash.gif";
    public static final String COMPOSE_ICON = "compose.png";//"Ekisho-Deep-Ocean-Documents-icon.png";//"compose.gif";
    public static final String WINDOW_ICON = "icon.gif";
    public static final String ATTACHMENT_ICON = "attachment.png";//"attachment.gif";
    public static final String MAIL_ICON = "gmail.png";//"gmail.jpg";
    public static final String MAIL_ICON2 = "compose_icon.png";//"mail.gif";
    public static final String READ_STARRED_ICON = "read_starred.png";//"read_starred.gif";
    public static final String UNREAD_STARRED_ICON = "unread_starred.png";//"unread_starred.gif";
    public static final String ATTACHMENT_WINDOW_ICON = "attachment_icon.png";
    public static final String MSG_SENT_ICON = "sending.png";
    public static final String MSG_SAVE_ICON = "saving.png";
    public static final String DOWNLOADING_ICON = "downloading.png";
    public static final String DOWNLOADING_WINDOW_ICON = "downloading_icon.png";
    public static final String ABOUT_ICON = "logo.gif";
    public static final String ONLINE_ICON = "online.gif";
    public static final String SWINDOW_ICON = "gmail.ico";
    public static final String NEW_MAIL_ICON = "new.png";
    public static final String ENCRYPT_ICON = "lock-128.png";
    public static final String CHAT_OFFLINE_ICON = "MSN-32.png";
    public static final String CHAT_ONLINE_ICON = "Msn-green-32.png";
    public static final String CHAT_WINDOW_ICON = "Chat-64.png";
    public static final String GTALK_ICON = "Chat-icon.gif";//"Chat-icon2.png";//"Android-Gtalk-256.png";//"Gtalk-green-64.png"
    public static final String CHAT_BUSY_ICON = "Msn_Buddy-Offline-32.png";
    public static final String CHAT_IDLE_ICON = "Msn_Buddy-mobile-32.png";
    public static final String TALK_ABOUT_ICON = "chat-symbols.png";
    public static final String ADDRESS_BOOK_ICON = "Address-book-256.png";
    
    public static final String IMAP_PROTOCOL = "imaps";
    public static final String POP_PROTOCOL = "pop";
    
    public static final String GMAIL_POP3_HOST = "pop.gmail.com";
    public static final String GMAIL_IMAP_HOST = "imap.gmail.com";
    public static final String GMAIL_SMTP_HOST = "smtp.gmail.com";
    public static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
    public static final String GTALK_HOST = "talk.google.com";
    public static final String GMAIL_SERV_NAME = "gmail.com";   // Gmail service name
    
    public static final int POP3_PORT = 995;
    public static final int SMTP_TLS_PORT = 587;
    public static final int SMTP_SSL_PORT = 465;
    public static final int IMAP_SSL_PORT = 993;
    public static final int GTALK_PORT = 5222;
    
    public static final String USERNAME_HINT = "";
    public static final String PASSWORD_HINT = "";
    
    public static final String INBOX = "INBOX";
    public static final String SENT_MAIL = "[Gmail]/Sent Mail";
    public static final String SPAM = "[Gmail]/Spam";
    public static final String STARRED = "[Gmail]/Starred";
    public static final String DRAFTS = "[Gmail]/Drafts";
//     public static final String CHATS = "[Gmail]/Chats";
    public static final String TRASH = "[Gmail]/Trash";
    
    public static final Font FONT = new Font("sans serif", Font.PLAIN, 12);
    public static final Color FG = Color.BLACK;
    
    public static final Font BUTTON_FONT = new Font("sans serif", Font.BOLD, 12);
    public static final Color BUTTON_FG = Color.BLUE;
    
    public static final Font MENUITEM_FONT = new Font("arial", Font.BOLD, 12);
    public static final Color MENUITEM_FG = Color.BLUE;
    
    public static final Font MENU_FONT = new Font("arial", Font.BOLD, 12);
    public static final Color MENU_FG = Color.BLUE;
    
    public static final Font CBMENUITEM_FONT = new Font("sans serif", Font.BOLD, 12);
    public static final Color CBMENUITEM_FG = Color.BLUE;
    
    public static final Font CONTENTS_FONT = new Font("sans serif", Font.PLAIN, 14);
    
    public static final Color UNREAD_MSG_BG = Color.WHITE;
    public static final Color UNREAD_MSG_FG = Color.BLACK;
    public static final Color DOWNLOAD_FG = new Color(100, 100, 255);
    
    public static final Color SEL_FG = new Color(252, 156, 12);
    
    public static final Font SEL_FONT = new Font("sans serif", Font.BOLD, 16);
    public static final Font UNSEL_FONT = new Font("sans serif", Font.PLAIN, 12);
    public static final Font SUB_FONT = new Font("sans serif", Font.BOLD, 14);
    public static final Font CTYPE_FONT = new Font("sans serif", Font.ITALIC, 11);
    
    public static final String LAF = "com.jtattoo.plaf.acryl.AcrylLookAndFeel";
    public static final String CHAT_LAF = "com.jtattoo.plaf.acryl.AcrylLookAndFeel";//"ch.randelshofer.quaqua.tiger.Quaqua15TigerCrossPlatformLookAndFeel";
    
    public static final int BLOCK_SIZE = 512;   // block size for stream-based hash function
    public static final String DIGI_SIGN_FILE = "ds.xml";
    public static final int NO_DIGI_SIGN = 123;
    public static final int DIGI_SIGN_MISMATCH = 124;
    public static final int DIGI_SIGN_OK = 125;
    
    public static final int AMD5 = 56;
    public static final int ASHA1 =  57;
    public static final int ASHA256 = 58;
    
    public static final String MD5_ID = "MD5";
    public static final String SHA1_ID = "SHA-1";
    public static final String SHA256_ID = "SHA-256";
    
    // XML Digital Signature Tags
    public static final String ROOT_ELEM = "DigitalSignature";  // document root
    public static final String HASH_ALGO_TAG = "HashAlgorithm"; // hash algorithm used for signing
    public static final String BODY_TEXT_TAG = "BodyText";      // encrypted hash of the message text, i.e., no attachments
    public static final String CRYPTO_HASH_TAG = "CryptoHash";  // tag encapsulating an attachment hash
    public static final String FILE_NAME_TAG = "fileName";      // file name of the attachment
    public static final String HASH_VALUE_TAG = "hashValue";    // encrypted hash of the attachment contents
}
