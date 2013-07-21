package jmail.chat;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import jmail.*;
import jmail.secure.*;
import javax.swing.border.*;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.*;
import java.util.*;
// import javax.swing.text.*;

public class ChatWin extends JFrame implements ActionListener, KeyListener, Defaults
{
    private static final int I = 5;
    private static final int U = 6;
    private static final int N = 256;
    private static final int EN = 256;
    private static final String CMD_PK_ADV = "_cmd_advpk";
    private static final String CMD_DELIM = ":";
    private static final String CMD_UNENCRYPT = "_cmd_unenc";
    private static final Color ENC_FG = new Color(180, 35, 35);
    private static final String ENCRYPT_ENABLE_MSG = "Enable Chat Encryption";
    private static final String ENCRYPT_DISABLE_MSG = "Disable Chat Encryption";
    private static final String BUDDY_BG_IMG = "blue_background.jpg";
    
    private JTextField msg;
    private JEditorPane transcript;
    private String user;
    private String name;
    private Chat chat;
    private String tscript;
    private int turn;
    private JScrollPane scroller;
    private JButton enc;
//     private JLabel encStatus;
    
    private KeyPair myKey;
    private Key yourPublicKey;
    private boolean encryptionOn = false;
    
    private static Vector<String> activeList = new Vector<String>();
    
    
    ChatWin(JFrame parent, String name, String s, XMPPConnection conn)
    {
//         super(name + " (" + s + ")");//parent, s);
        this.name = new String(name);
        if (name.equals(s)) {
            setTitle(s);
        }else {
            setTitle(name + " (" + s + ")");
        }
        setIconImage(new Factory().createImageIcon(CHAT_WINDOW_ICON).getImage());
        
        this.user = new String(s);
        activeList.add(this.user);
                
        this.tscript = new String();
        this.msg = new JTextField();
//         DefaultCaret caret = (DefaultCaret) this.msg.getCaret();
//         caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        this.msg.addKeyListener(this);
        this.transcript = new JEditorPane();
        this.transcript.setEditable(false);
        this.transcript.setContentType("text/html");
//         this.transcript.setText("<html>");
        this.transcript.setBorder(new TitledBorder(""));
        this.chat = conn.getChatManager().createChat(user, null);
        
        this.enc = new JButton(ENCRYPT_ENABLE_MSG);
        this.enc.addActionListener(this);
//         this.encStatus = new JLabel("This chat is not encrypted");
        this.enc.setForeground(ENC_FG);
        this.enc.setToolTipText("Enable/disable end-to-end RSA encryption of chat messages");
        JPanel px = new JPanel();
        px.setLayout(new FlowLayout(FlowLayout.CENTER));
//         px.setLayout(new BorderLayout());
//         px.setLayout(new GridLayout(1, 2));
        px.add(this.enc);
//         px.add(new JButton("Save Transcript"));
//         px.add(this.encStatus);
        add(px, BorderLayout.NORTH);
        
//         PacketCollector collector = conn.createPacketCollector(null);
//         
//         Packet packet = collector.pollResult();
        
//         PacketListener myListener = new PacketListener() {
//             public void processPacket(Packet packet) {
//                 if (packet instanceof Message) {
//                     Message msg = (Message) packet;
//                     if(msg != null && msg.getBody() != null && msg.getFrom().indexOf(user) == 0) {// && !conversationList.contains("<b>website chat admin:</b> "+msg.getBody())){
//                     // Process message by adding to conversationList  
//             //         conversationList.add("<b>website chat admin:</b> "+msg.getBody());  
//             //             System.out.println(msg.getBody());
//             //             transcript.setText("<html>" + transcript.getText() + "<p><b>" + user + ": </b>" + msg.getBody() + "</p></html>");
//             //             transcript.setText(transcript.getText() + user + ": " +msg.getBody() + "\n");
//                         
//                         if (turn == U) {
//                             tscript += "<p>" + msg.getBody() + "</p>";
//                         }else {
//                             tscript += "<p><b>" + user + ": </b>" + msg.getBody() + "</p>";
//                         }
//                         transcript.setText("<html>" + tscript + "</html>");
//                         turn = U;
//                         scrollToBottom();
//                         setVisible(true);
//                     }
//                 }
//             }
//         };
//         // Register the listener.  
//         conn.addPacketListener(myListener, null);
        
        JPanel p = new JPanel();
//         p.setLayout(new FlowLayout());
//         p.add(msg);
        p.setLayout(new BorderLayout());
        p.add(msg, BorderLayout.SOUTH);//CENTER);
//         JButton b = Factory.createButton("Send", this);
//         p.add(b, BorderLayout.EAST);
        add(p, BorderLayout.SOUTH);
        this.scroller = new JScrollPane(this.transcript);
        add(scroller);
        this.msg.setBorder(new TitledBorder(""));
//         scroller.setBorder(new TitledBorder("Chat transcript:"));
        this.msg.requestFocus();
        this.msg.requestFocusInWindow();
        
        setSize(400, 400);
        setLocationRelativeTo(null);
//         setUndecorated(true);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setVisible(true);
        this.msg.requestFocusInWindow();
    }
    
    private void genKey()
    {
        KeyGen keyGen = new KeyGen(N, EN);
        this.myKey = new KeyPair(keyGen.getPublicKey(), keyGen.getPrivateKey());
    }
    
    public void recvMessage(Message msg)
    {
        if(msg != null && msg.getBody() != null && msg.getFrom().indexOf(user) == 0) {
            if (msg.getBody().startsWith(CMD_PK_ADV)) {
                StringTokenizer stok = new StringTokenizer(msg.getBody(), CMD_DELIM);
                stok.nextToken();
                this.yourPublicKey = new Key(stok.nextToken(), stok.nextToken());
                if (this.myKey == null) {
                    genKey();
                    sendMessage(CMD_PK_ADV + CMD_DELIM + myKey.getPublicKey().getExponent() + CMD_DELIM + myKey.getPublicKey().getModulus());
                }
                encryptionOn = true;
                tscript += "<p><b><i>This chat is encrypted</b></i></p>";
                transcript.setText("<html>" + tscript + "</html>");
                this.enc.setText(ENCRYPT_DISABLE_MSG);
//                 this.encStatus.setText("This chat is encrypted");
                return;
            }else if (msg.getBody().startsWith(CMD_UNENCRYPT)) {
                encryptionOn = false;
                myKey = null;
                yourPublicKey = null;
                this.enc.setText(ENCRYPT_ENABLE_MSG);
                tscript += "<p><b><i>This chat is no longer encrypted</b></i></p>";
                transcript.setText("<html>" + tscript + "</html>");
                return;
            }
            String pt = "";
            if (encryptionOn) {
                String ct = msg.getBody();
//                 System.out.println(ct);
                pt = RSA.decrypt(ct, myKey.getPrivateKey());
            }else {
                pt = msg.getBody();
            }
            if (turn == U) {
                tscript += "<p>" + pt + "</p>";
            }else {
                tscript += "<p><b>" + name/*user*/ + ": </b>" + pt + "</p>";
            }
            transcript.setText("<html>" + tscript + "</html>");
//             this.transcript.setCaretPosition(this.transcript.getCaretPosition());
//             this.transcript.scrollRectToVisible(new Rectangle(0,transcript.getHeight()));
            turn = U;
            setVisible(true);
            scrollToBottom();
        }
    }
    
    public static boolean isActive(String usr)
    {
        for (int i = 0; i < activeList.size(); i++) {
            if (activeList.elementAt(i).equals(usr)) {
                return true;
            }
        }
        return false;
    }
    
    ChatWin(JFrame parent, String name, String s, XMPPConnection conn, Message msg)
    {
        this(parent, name, s, conn);
        tscript += "<p><b>" + user + ": </b>" + msg.getBody() + "</p>";
        transcript.setText("<html>" + tscript + "</html>");
        turn = U;
        scrollToBottom();
    }
    
    public void keyTyped(KeyEvent ke)
    {
        
    }
    
    public void keyPressed(KeyEvent ke)
    {
        
    }
    
    public void keyReleased(KeyEvent ke)
    {
        if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
               sendMessage();
            }catch (Exception e) {}
        }
    }
    
    public void actionPerformed(ActionEvent ae)
    {
        if (ae.getSource().equals(this.enc)) {
            if (this.enc.getText().equals(ENCRYPT_ENABLE_MSG)) {
                genKey();
                sendMessage(CMD_PK_ADV + CMD_DELIM + myKey.getPublicKey().getExponent() + CMD_DELIM + myKey.getPublicKey().getModulus());
            }else if (this.enc.getText().equals(ENCRYPT_DISABLE_MSG)) {
                unencrypt();
                this.enc.setText(ENCRYPT_ENABLE_MSG);
            }
        }else {
            try {
                sendMessage();
            }catch (Exception e) {}
        }
    }
    
    public void dispose()
    {
//         for (int i = 0; i < activeList.size(); i++) {
//             if (activeList.elementAt(i).equals(this.user)) {
//                 activeList.remove(i);
//                 return;
//             }
//         }
    }
    
    private void sendMessage()
    {
        try {
            if (encryptionOn) {
                String pt = msg.getText();
                String ct = RSA.encrypt(pt, yourPublicKey);
//                 System.out.println(new java.math.BigInteger(pt.getBytes()));
//                 System.out.println(RSA.encrypt(new BigInteger(pt.getBytes()), yourPublicKey));
                chat.sendMessage(ct);
            }else {
                chat.sendMessage(msg.getText());
            }
            
        //         this.transcript.setText(this.transcript.getText() + "\nMe: " + msg.getText() + "\n");
            if (turn == I) {
                this.tscript += "<p>" + msg.getText() + "</p>";
            }else {
                this.tscript += "<p><b>Me: </b>" + msg.getText() + "</p>";
            }
            this.transcript.setText("<html>" + this.tscript + "</html>");
            scrollToBottom();
            turn = I;
        
        //         this.transcript.setText("<html>" + this.transcript.getText() + "<p><b>Me: </b>" + msg.getText() + "</p></html>");
            msg.setText("");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void sendMessage(String msg)
    {
        try {
            chat.sendMessage(msg);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void unencrypt()
    {
        encryptionOn = false;
        myKey = null;
        yourPublicKey = null;
        sendMessage(CMD_UNENCRYPT);
        tscript += "<p><b><i>This chat is no longer encrypted</b></i></p>";
        transcript.setText("<html>" + tscript + "</html>");
//         this.encStatus.setText("This chat is not encrypted");
    }
    
    private void scrollToBottom()
    {
        JScrollBar vert = this.scroller.getVerticalScrollBar();
        this.scroller.getVerticalScrollBar().setValue(vert.getMaximum());
//         this.transcript.setCaretPosition(this.transcript.getText().length());
//         this.transcript.scrollRectToVisible(new Rectangle(0,transcript.getHeight()));
//         this.transcript.scrollRectToVisible(new Rectangle(0,transcript.getHeight()-2, 1, 1));
    }
}
