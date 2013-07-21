package jmail;

 

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.mail.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import javax.swing.border.*;
import jmail.secure.*;
import java.math.BigInteger;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import jmail.digiSign.*;


class MessageViewer extends JFrame implements Defaults, ActionListener
{
    private static final int IN = 0;
    private static final int OUT = 1;
    private static final int DWIDTH = 300;
    private static final int DHEIGHT = 200;
    
//     private static final String[] TAGS = {
//         "address", "applet", "area", "a", "base", "basepoint", "big", "blockquote", "body", "br", "b", "caption", "center", "cite", "code", "dd", "dfn", "dir", "div",
//         "dl", "dt", "em", "font", "form", "h1", "h2", "h3", "h4", "h5", "h6", "head", "hr", "html", "img", "input", "isindex", "i", "kbd", "link", "li", "map", "menu", 
//         "meta", "ol", "option", "param", "pre", "p", "samp", "script", "select", "small", "strike", "string", "style", "sub", "sup", "table", "td", "textarea", "th", 
//         "title", "tr", "tt", "ul", "u", "var"
//     };
    
    private JLabel from;
    private JLabel to;
    private JLabel sdate;
    private JLabel rdate;
    private JLabel sub;
    private JEditorPane contents;
    private JButton mode;
    private JButton enc;
    private JButton dsc;
    private JScrollPane atch;
    
    private JDialog d;
    
    private Message msg;
    private Main parent;
    
    private String msgBody;
    private String text;
    private String plaintext;
    private String richText;
    private String ctype;
    
    private boolean encrypted = false;
    
    private Key privateKey = new Key("8816387758261998980974938635380442968614859316780555092360770607170562318742978761879563516716577615772176041459778690387015593788563876859857006715255597", 
                                "12246641146915474200483271191099148040739686057436874659738475164035619744233809189365535762694662368043130950787602085247679515959777934951414238669598269");;
    
    private Vector<Part> attachments;
    private Vector<Vector<TextPart>> texts = new Vector<Vector<TextPart>>();
    
    private boolean html = true;
//     private SaveMBPThread mbpThread;
//     private SaveStreamThread streamThread;
    private JFileChooser fileChooser;
    
    public MessageViewer(Message msg, Main parent) throws Exception
    {
        super("Message Viewer");
        
        setIconImage(new Factory().createImageIcon(MAIL_ICON).getImage());
        
        this.msg = msg;
        this.parent = parent;
        
        this.attachments = new Vector<Part>();
        this.msgBody = new String();
        this.fileChooser = new JFileChooser();
        
        String frm = new String();
        String t = new String();
        Address[] a;
        
        // FROM
        if ((a = this.msg.getFrom()) != null) {
//             System.out.println("from: " + a.length);
            for (int j = 0; j < a.length; j++) {
                frm += a[j].toString();
                if (j != (a.length - 1)) {
                    frm += ", ";
                }
            }
//             System.out.println(frm);
        }
            
        // TO
        if ((a = this.msg.getRecipients(Message.RecipientType.TO)) != null) {
//             System.out.println("to: " + a.length);
            for (int j = 0; j < a.length; j++) {
                t += a[j].toString();
                if (j != (a.length - 1)) {
                    t += ", ";
                }
            }
        }
        if ((a = this.msg.getRecipients(Message.RecipientType.CC)) != null) {
//             System.out.println("to cc: " + a.length);
            t += ", ";
            for (int j = 0; j < a.length; j++) {
                t += a[j].toString();
                if (j != (a.length - 1)) {
                    t += ", ";
                }
            }
        }
        if ((a = this.msg.getRecipients(Message.RecipientType.BCC)) != null) {
//             System.out.println("to cc: " + a.length);
            t += ", ";
            for (int j = 0; j < a.length; j++) {
                t += a[j].toString();
                if (j != (a.length - 1)) {
                    t += ", ";
                }
            }
        }
        
        // SUBJECT
        String sbj = this.msg.getSubject();
        
        // SENT DATE
        Date d = this.msg.getSentDate();
        String sdt = (d != null ? d.toString() : "Unknown");
        
        // RCVD DATE
        d = this.msg.getReceivedDate();
        String rdt = (d != null ? d.toString() : "Unknown");
        
        from = Factory.createLabel("<html><b>From: </b>" + frm + "</html>");
        to = Factory.createLabel("<html><b>To: </b>" + t + "</html>");
        sdate = Factory.createLabel("<html><b>Sent on: </b>" + sdt + "</html>");
        rdate = Factory.createLabel("<html><b>Received on: </b>" + rdt + "</html>");
        sub = Factory.createLabel("Subject: " + sbj);
        sub.setFont(SUB_FONT);
        
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(0, 1));
        p.add(from);
        p.add(to);
        p.add(sdate);
        p.add(rdate);
        p.add(sub);
        
        this.contents = new JEditorPane("text/html", "");
        this.contents.setFont(CONTENTS_FONT);
//         this.contents = new JLabel();
//         this.contents.setBackground(Color.WHITE);
        this.contents.setEditable(false);
        JScrollPane scroller = new JScrollPane(this.contents);
//         displayContents();

        JPanel p2 = new JPanel();
        p2.setLayout(new FlowLayout(FlowLayout.LEFT));
        p2.add(Factory.createButton("Reply", this));
        p2.add(Factory.createButton("Reply To All", this));
        p2.add(Factory.createButton("Forward", this));
        p2.add(Factory.createButton("Save", this));
        p2.add(mode = Factory.createButton("Plain Text", this));
        p2.add(dsc = Factory.createButton("Verify Digital Signature", this));
        dsc.setEnabled(false);

//         p2.add(enc = Factory.createButton("Decrypt Message", this));
//         enc.setEnabled(false);
//         enc.setToolTipText("This message is encrypted. Please use your private key to decrypt it.");
        
        if (this.msg.getHeader(ENC_HEADER_NAME) != null) {
//             enc.setEnabled(true);
            encrypted = true;
        }

        parseMIME();
        
        JPanel p3 = new JPanel();
        p3.setLayout(new GridLayout(attachments.size(), 1));
//         JScrollPane atch = null;
        
        JPanel ap = new JPanel();
        JPanel p4 = new JPanel();
        p4.setLayout(new BorderLayout());
        p4.add(p2, BorderLayout.SOUTH);
        
        
        if (attachments.size() > 0) {
            for (int i = 0; i < attachments.size(); i++) {
                if (attachments.elementAt(i).getFileName().equalsIgnoreCase(DIGI_SIGN_FILE)) {
                    p3.add(new Factory().createAttachment(attachments.elementAt(i), i, this, true));
                }else {
                    p3.add(new Factory().createAttachment(attachments.elementAt(i), i, this));
                }
            }
            atch = new JScrollPane(p3);
            atch.setBorder(new TitledBorder("" + attachments.size() + ((attachments.size() > 1) ? " Attachments" : " Attachment")));
            
            ap.setLayout(new FlowLayout(FlowLayout.LEFT));
            JButton va = Factory.createButton("View Attachments", this);
            ap.add(va);
            ap.setBorder(new TitledBorder("" + attachments.size() + ((attachments.size() > 1) ? " Attachments" : " Attachment")));
            
            p4.add(ap, BorderLayout.CENTER);
            
            this.d = new JDialog(this, "Attachments");
            this.d.setIconImage(new Factory().createImageIcon(ATTACHMENT_WINDOW_ICON).getImage());
            this.d.setLayout(new BorderLayout());
            this.d.add(atch, BorderLayout.CENTER);
            this.d.pack();
            Rectangle2D rect = getGraphicsConfiguration().getBounds();
            if (this.d.getWidth() > (int) rect.getWidth() || this.d.getHeight() > (int) rect.getHeight()) {
                this.d.setSize(500, 300);
            }
            this.d.setDefaultCloseOperation(HIDE_ON_CLOSE);
            this.d.setLocationRelativeTo(null);
        }
        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(p, BorderLayout.NORTH);
        panel.add(scroller, BorderLayout.CENTER);
        panel.add(p4, BorderLayout.SOUTH);
        
        add(new JScrollPane(panel), BorderLayout.CENTER);
        
//         add(p, BorderLayout.NORTH);
//         add(scroller, BorderLayout.CENTER);
//         add(p4, BorderLayout.SOUTH);
//         
        
//         add(ap, BorderLayout.SOUTH);
        if (atch != null) {
            
        }
        
//         Thread thread = new Thread(this);
//         thread.start();
        
        setSize(600, 500);
        setDefaultLookAndFeelDecorated(true);
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        
        setVisible(true);
        
//         try {
//             setVisible(true);
//         }catch (Exception e) {
//             this.contents.setContentType("text/plain");
//             forcedDisplayContents();
//             setVisible(true);
//         }
    }
    
    void setPrivateKey(Key key)
    {
        this.privateKey = new Key(key.getExponent(), key.getModulus());
    }
    
    private Key getKey()
    {
        return this.privateKey;
    }
    
    void noVerifyDigitalSignature()
    {
        this.dsc.setEnabled(false);
    }
    
//     private void decrypt(String e, String m)
//     {
//         String ct = this.contents.getText();
//         privateKey = new Key(e, m);
//         BigInteger dm = RSA.decrypt(new BigInteger(ct), privateKey);
//         String s = new String(dm.toByteArray());
//         this.contents.setText(s);
//     }
    
    private void parse(Part part, boolean alternative, Vector<TextPart> altTexts, String contentType) throws Exception
    {
        Object content = part.getContent();
        if (content instanceof Multipart) {
            Multipart mp = (Multipart) content;
            int n = mp.getCount();
            boolean altTextsFlag = false;
            for (int i = 0; i < n; i++) {
                BodyPart p = mp.getBodyPart(i);
                String ctype = p.getContentType().toLowerCase();
                String disp = "" + p.getDisposition();
                
                if (!ctype.startsWith("multipart/alternative")) {
                    alternative = true;
                    if (!altTextsFlag) {
                        altTexts = new Vector<TextPart>();
                        altTextsFlag = true;
                    }
                }else {
                    alternative = false;
                    altTexts = null;
                }
                // treat anything other than text as an attachment, CANNOT render inline images, etc...
                if (!ctype.startsWith("multipart") && !ctype.startsWith("text") || disp.equalsIgnoreCase(Part.ATTACHMENT)) {
                    if (p.getFileName().equalsIgnoreCase(DIGI_SIGN_FILE)) {
                        dsc.setEnabled(true);
                    }
                    attachments.addElement(p);
                }else {
                    parse(p, alternative, altTexts, ctype);
                    if (alternative) {
                        texts.add(altTexts);
                    }
                }
            }
        }else if (content instanceof String) {
            TextPart tpart = new TextPart(contentType, (String) content);
            if (alternative) {
                altTexts.addElement(tpart);
                msgBody += ((String) content);
            }else {
                Vector<TextPart> v = new Vector<TextPart>();
                v.addElement(tpart);
                texts.addElement(v);
            }
//             System.out.println((String) content + "\n<End of message------------------------------------------------------------------------>");
        }
    }
    
    private void parseMIME()
    {
        Object contents = null;
        try {
            parse(this.msg, false, null, "text/plain");
        }catch (Exception e) {
            e.printStackTrace();
        }
        int n = texts.size();
        boolean textSet = (n == 0) ? true : false;
        boolean htmlError = false;
        for (int i = 0; i < n; i++) {
            Vector<TextPart> va = texts.elementAt(i);
            int k = va.size();
//             if (k == 1) {
//                 this.contents.setContentType("text/plain");
//             }
            for (int j = k - 1; j >= 0; j--) {
                TextPart text = va.elementAt(j);
                try {
                    this.richText = text.getBody();
                    this.ctype = text.getContentType().toLowerCase();
                    this.contents.setContentType(this.ctype);
//                     System.out.println(ctype);
                    this.contents.setText(this.richText);
                    if (this.ctype.startsWith("text/html") && stripHTML(this.contents.getText()).trim().length() == 0) { // if cannot display HTML (JEditorPane sucks!!!)
//                         System.out.println("<--->");
//                         this.contents.setContentType(this.ctype);
                        htmlError = true;
                        continue;
                    }
//                     System.out.println(text);
                    textSet = true;
//                     System.out.println(this.contents.getText());
                    return;
                }catch (Exception e) {
                    continue;
                }
            }
        }
        if (!textSet) {
            showErrorDialog("Error parsing message. Could not display contents.", "Invalid Message Format");
            this.mode.setEnabled(false);
            this.ctype = "text/plain";
            this.contents.setContentType(this.ctype);
            this.contents.setText(this.richText = this.msgBody);
            if (htmlError) {
                this.contents.setText(this.richText = stripHTML(this.msgBody));
            }
            mode.setEnabled(false);
        }
    }
    
    
//     private boolean tagAt(String str, int i)
//     {
//         for (int j = 0; j < TAGS.length; j++) {
//             if (TAGS[j].regionMatches(true, 0, str.toLowerCase(), i, TAGS[j].length()) || ("/" + TAGS[j]).regionMatches(true, 0, str.toLowerCase(), i, TAGS[j].length() + 1)) {
//                 return true;
//             }
//         }
//         return false;
//     }
    
    private String stripHTML(String str)
    {      
        String txt = new String();
        int n = str.length();
        int pos = OUT;
        for (int i = 0; i < n; i++) {
            char c = str.charAt(i);
            if (c == '<') {// && tagAt(str, i + 1)) {
                pos = IN;
            }
            if (pos == OUT) {
                txt += c;
            }
            if (c == '>') {
                pos = OUT;
            }
        }
        return txt;
    }
    
    private void showErrorDialog(String msg, String title)
    {
        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.ERROR_MESSAGE);
    }
    
    public boolean isEncrypted()
    {
        return this.encrypted;
    }
    
    private void saveAttachment(int i)
    {
        Part part = attachments.elementAt(i);
        while (true) {
//             fileChooser = new JFileChooser();
            int ec = fileChooser.showSaveDialog(this);
            if (ec == JFileChooser.APPROVE_OPTION) {
                File f = fileChooser.getSelectedFile();
                if (f.exists()) {
                    ec = JOptionPane.showConfirmDialog(this, "File exists. Overwrite?", "Overwrite File", JOptionPane.YES_NO_OPTION);
                    if (ec != JOptionPane.YES_OPTION) {
                        continue;
                    }
                }
                try {
                    
                    SaveFileThread sft = new SaveFileThread(part.getInputStream(), f);
                    sft.start();
                    return;
                        
//                     if (part instanceof MimeBodyPart) {
//                         MimeBodyPart mbp = (MimeBodyPart) part;
//                         
//                         
//                         
// //                         mbpThread = new SaveMBPThread(mbp, f);
// //                         mbpThread.start();
// //                         return;
//                     }else {
//                         InputStreamReader fin = new InputStreamReader(part.getInputStream());
//                         
//                         streamThread = new SaveStreamThread(fin, f);
//                         streamThread.start();
//                         return;
//                     }
                }catch (Exception e) {
                    showErrorDialog("Error saving attachment", "Error");
//                         System.err.println(e);
                }
            }else {
                return;
            }
        }
    }
    
    private Key getPublicKey(InternetAddress addr)
    {
        String email = addr.getAddress();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(CONTACTS_FILE)));
            String line = new String();
            while ((line = br.readLine()) != null) {
                String [] tokens = line.split(",");
                if (email.equalsIgnoreCase(tokens[1])) {
                    String pke = tokens[4];
                    String pkm = tokens[5];
                    
                    return new Key(pke, pkm);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private int verifyDigitalSignature() throws Exception
    {
        Address a = null;
        Key pk = null;
        
        if (this.msg.getFrom() != null && (a = this.msg.getFrom()[0]) != null) {
            pk = getPublicKey((InternetAddress) a); // obtain public key here
        }
        if (pk == null) {
            KeyDialog kd = new KeyDialog(this);
            pk = kd.getKey();
        }
        
        for (int i = 0; i < attachments.size(); i++) {
            if (attachments.elementAt(i).getFileName().equalsIgnoreCase(DIGI_SIGN_FILE)) {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(attachments.elementAt(i).getInputStream());
                doc.getDocumentElement().normalize();
                
                int algo = -1;
                
                NodeList nodeList = doc.getElementsByTagName(HASH_ALGO_TAG);//"HashAlgorithm");
                String alg = nodeList.item(0).getChildNodes().item(0).getNodeValue();
                if (alg.equalsIgnoreCase(MD5_ID)) {//"md5")) {
                    algo = AMD5;
                }else if (alg.equalsIgnoreCase(SHA1_ID)) {//"sha-1")) {
                    algo = ASHA1;
                }else if (alg.equalsIgnoreCase(SHA256_ID)) {//"sha-256")) {
                    algo = ASHA256;
                }else {
                    JOptionPane.showMessageDialog(this, "The digital signature uses a hash algorithm that is either unknown or currently unsupported by the application.", 
                        "Unsupported Hash Algorithm", JOptionPane.ERROR_MESSAGE);
                    return NO_DIGI_SIGN;
                }
                
                nodeList = null;
                nodeList = doc.getElementsByTagName(BODY_TEXT_TAG);//"BodyText");
                String encBodyTextHash = nodeList.item(0).getChildNodes().item(0).getNodeValue();
                String bodyHash = RSA.decrypt(encBodyTextHash, pk);
                String myBodyHash = new String();
                
                if (algo == AMD5) {
                    myBodyHash = MD5.doHash(msgBody.getBytes());
                }else if (algo == ASHA1) {
                    myBodyHash = SHA1.hash(msgBody.getBytes());
                }else {
                    myBodyHash = SHA256.hash(msgBody.getBytes());
                }
                
//                 System.out.println(bodyHash + "\n" + myBodyHash);
                
                if (!bodyHash.equalsIgnoreCase(myBodyHash)) {
                    return DIGI_SIGN_MISMATCH;
                }
                
                nodeList = null;
                nodeList = doc.getElementsByTagName(CRYPTO_HASH_TAG);//"cryptoHash");
//                 System.out.println(nodeList.getLength());
                for (int j = 0; j < nodeList.getLength(); j++) {
                    Node n = nodeList.item(j);
                    if (n.getNodeType() == Node.ELEMENT_NODE) {
                        Element e = (Element) n;
                        NodeList fn = e.getElementsByTagName(FILE_NAME_TAG);//"fileName");
                        String fileName = fn.item(0).getChildNodes().item(0).getNodeValue();
                        fn = e.getElementsByTagName(HASH_VALUE_TAG);//"hashValue");
                        String encHash = fn.item(0).getChildNodes().item(0).getNodeValue();
                        String hash = RSA.decrypt(encHash, pk);
                        Part p = attachments.elementAt(j);
                        String myHash = new String();
                        if (p.getFileName().equalsIgnoreCase(fileName)) {
                            if (algo == AMD5) {
                                myHash = MD5.doHash(p.getInputStream());
                            }else if (algo == ASHA1) {
                                myHash = SHA1.hash(p.getInputStream());
                            }else {
                                myHash = SHA256.hash(p.getInputStream());
                            }
                        }else {
                            for (int k = 0; k < attachments.size(); k++) {
                                p = attachments.elementAt(k);
                                if (isEncrypted()) {
                                    File tmp = new File(ENC_TEMPFILE);
                                    Codec.decrypt(tmp, new File("" + Math.random()), privateKey);
                                    if (algo == AMD5) {
                                        myHash = MD5.doHash(new FileInputStream(tmp));
                                    }else if (algo == ASHA1) {
                                        myHash = SHA1.hash(new FileInputStream(tmp));
                                    }else {
                                        myHash = SHA256.hash(new FileInputStream(tmp));
                                    }
                                    tmp.delete();
                                    break;
                                }else {
                                    if (p.getFileName().equalsIgnoreCase(fileName)) {
                                        if (algo == AMD5) {
                                            myHash = MD5.doHash(p.getInputStream());
                                        }else if (algo == ASHA1) {
                                            myHash = SHA1.hash(p.getInputStream());
                                        }else {
                                            myHash = SHA256.hash(p.getInputStream());
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                        if (!hash.equalsIgnoreCase(myHash)) {
                            return  DIGI_SIGN_MISMATCH;
                        }
//                         System.out.println(fileName + "\n" + hash + "\n" + myHash + "\n");
                    }
                }
            }
        }
        
        return DIGI_SIGN_OK;
    }
    
    public void actionPerformed(ActionEvent ae)
    {
        String cmd = ae.getActionCommand();
        if (cmd.equalsIgnoreCase("save")) {
            while (true) {
//                 fileChooser = new JFileChooser();
                int ec = fileChooser.showSaveDialog(this);
                if (ec == JFileChooser.APPROVE_OPTION) {
                    File f = fileChooser.getSelectedFile();
                    if (f.exists()) {
                        ec = JOptionPane.showConfirmDialog(this, "File exists. Overwrite?", "Overwrite File", JOptionPane.YES_NO_OPTION);
                        if (ec != JOptionPane.YES_OPTION) {
                            continue;
                        }
                    }
                    try {
                        PrintWriter pout = new PrintWriter(f);
                        pout.println(msgBody);
                        pout.close();
                        JOptionPane.showMessageDialog(this, "The message has been saved to " + f.getName(), "Message saved", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }catch (Exception e) {
                        System.err.println(e);
                    }
                }else {
                    return;
                }
            }
        }else if (cmd.startsWith("$")) {
            saveAttachment(Integer.parseInt(cmd.substring(1, cmd.length())));
        }else if (cmd.equalsIgnoreCase("plain text")) {
            mode.setText("Rich Text");
            mode.setActionCommand("Rich Text");
            this.contents.setContentType("text/plain");
            this.contents.setText(this.msgBody);
            if (encrypted) {
                enc.setEnabled(true);
            }
        }else if (cmd.equalsIgnoreCase("rich text")) {
            mode.setText("Plain Text");
            mode.setActionCommand("Plain text");
            this.contents.setContentType(this.ctype);
            this.contents.setText(this.richText);
        }else if (cmd.equalsIgnoreCase("view attachments")) {
            d.setVisible(true);
        }else if (cmd.equalsIgnoreCase("reply")) {
            try {
                MimeMessage reply = (MimeMessage)msg.reply(false);
                reply.setFrom(new InternetAddress(parent.getMyAddress()));
                new MessageComposer(reply, parent);
            }catch (Exception e) {
                showErrorDialog("An error occurred: " + e, "Error");
            }

        }else if (cmd.equalsIgnoreCase("reply to all")) {
            try {
                MimeMessage reply = (MimeMessage)msg.reply(true);
                reply.setFrom(new InternetAddress(parent.getMyAddress()));
                new MessageComposer(reply, parent);
            }catch (Exception e) {
                showErrorDialog("An error occurred: " + e, "Error");
            }
        }else if (cmd.equalsIgnoreCase("forward")) {
            try {
                Message forward = parent.createMimeMessage();
    
                // Fill in header
                forward.setSubject("Fwd: " + msg.getSubject());
                forward.setFrom(msg.getFrom()[0]);
                forward.setContent(msg.getContent(), msg.getContentType());
                
                new MessageComposer(forward, parent);
            }catch (Exception e) {
                showErrorDialog("An error occurred: " + e, "Error");
            }
        }else if (cmd.equalsIgnoreCase("decrypt message")) {
            new DecDialog(this);
            enc.setEnabled(false);
        }else if (cmd.equalsIgnoreCase("verify digital signature")) {
            try {
                int vds = verifyDigitalSignature();
                if (vds == DIGI_SIGN_MISMATCH) {
                    JOptionPane.showMessageDialog(this, "The digital signature with this email does not match its contents.", "Digital Signature Mismatch", JOptionPane.ERROR_MESSAGE);
                }else if (vds == DIGI_SIGN_OK) {
                    JOptionPane.showMessageDialog(this, "Digital signature verified. This mail appears to be authentic and its contents were not tampered with during transit.", 
                        "Digital Signature OK", JOptionPane.INFORMATION_MESSAGE);
                }else if (vds == NO_DIGI_SIGN) {
                    JOptionPane.showMessageDialog(this, "This email does not contain a digital signature.", "No Digital Signature Found", JOptionPane.WARNING_MESSAGE);
                }
            }catch (Exception e) {
                showErrorDialog("Error", e.toString());
            }   
        }
    }
    
    private void showWaitDialog(JDialog dd, ActionListener parent, JLabel dl)
    {
        JButton jb;
        dd.setIconImage(new Factory().createImageIcon(DOWNLOADING_WINDOW_ICON).getImage());
        dd.setLayout(new BorderLayout());
        dd.add(new JLabel(new Factory().createImageIcon(DOWNLOADING_ICON)), BorderLayout.WEST);
        dd.add(dl = new JLabel("<html><p align=\"center\">Downloading attachment. Please wait...</p></html>", JLabel.CENTER),
          BorderLayout.CENTER);
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout(FlowLayout.CENTER));
        p.add(jb = Factory.createButton("Cancel", parent));
        dd.add(p, BorderLayout.SOUTH);
//         jb.setToolTipText("Cancelling this operation may be potentially dangerous. Proceed at your own risk");
        dd.pack();
        dd.setResizable(false);
//         dd.setSize(DWIDTH, DHEIGHT);
        dd.setLocationRelativeTo(null);
        dd.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        dd.setVisible(true);
    }
    
    private class SaveFileThread extends Thread implements ActionListener
    {
        private InputStream in;
        private FileOutputStream fout;
        private File f;
        private JDialog dd;
        private JLabel dl;
        private volatile boolean cancelled = false;
        
        SaveFileThread(InputStream in, File f)
        {
            this.dd = new JDialog((JFrame) null, "Downloading");
            this.in = in;
            try {
                if (isEncrypted()) {
                    this.fout = new FileOutputStream(new File(ENC_TEMPFILE));
                    this.f = f;
                }else {
                    this.fout = new FileOutputStream(this.f = f);
                }
            }catch (Exception e) {
                showErrorDialog("Error saving attachment", "Error");
            }
        }
        
        public void cancel()
        {
            cancelled = true;
        }
        
        public void run()
        {
            try {
                if (in == null || fout == null) {
                    showErrorDialog("Error saving attachment", "Error");
                    return;
                }
                
                OutputStream out = null;
//                 InputStream in = null;
                out = new BufferedOutputStream(this.fout);
//                 in = this.fin;
                byte[] buf = new byte[8192];
                int len;
                
                showWaitDialog(dd, this, dl);
                
//                     long n = 0, total = 0;
//                     try {
//                         n = 0;
//                     }catch (Exception e) {}
                while (!cancelled && (len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
//                         n += 8192;
//                         dl.setText("<html><p align=\"center\">Downloading attachment. Please wait...</p>" + 
//                                    "<p align=\"center\">Transferred " + n + " bytes</p></html>");
                }
                in.close();
                out.close();
                this.fout.close();
//                 this.fin.close();
                
                if (isEncrypted()) {
                    File tmp = new File(ENC_TEMPFILE);
                    Codec.decrypt(tmp, this.f, privateKey);
                    tmp.delete();
                }

                dd.setVisible(false);
                if (!cancelled) {
                    JOptionPane.showMessageDialog(null, "" + f.getName() + " saved successfully", "File Saved", JOptionPane.INFORMATION_MESSAGE);
                }
            }catch (Exception e) {
                dd.setVisible(false);
                showErrorDialog("Error saving attachment", "Error");
            }
        }
        
        public void actionPerformed(ActionEvent ae)
        {
            if (ae.getActionCommand().equalsIgnoreCase("cancel")) {
                try {
                    cancelled = true;
                    dd.setVisible(false);
//                     destroy();
//                     interrupt();
                }catch (Exception e) {
                    showErrorDialog("Sorry, could not cancel process", "Error");
                }
            }
        }
    }
}
    
class DecDialog extends JDialog implements ActionListener
{
//     MessageViewer parent;
    private JTextField exp;
    private JTextField mod;
    private Message msg;
    private Main m;
    
    
    DecDialog(MessageViewer mv)
    {
        super(mv, "Enter Private Key");
//         this.parent = mv;
        setLayout(new GridLayout(0, 1));
        exp = Factory.createTextField(null);
        mod = Factory.createTextField(null);
        add(new JLabel("Enter your PRIVATE KEY exponent (base " + Defaults.KEY_BASE + "):"));
        add(exp);
        add(new JLabel("Enter your PRIVATE KEY modulus (base " + Defaults.KEY_BASE + "):"));
        add(mod);
//         Key key;
//         if ((key = parent.getKey()) != null) {
//             exp.setText(key.getExponent().toString());
//             mod.setText(key.getModulus().toString());
//         }
//             add(new JLabel(""));
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout(FlowLayout.CENTER));
        p.add(Factory.createButton("Decrypt", this));
        add(p);
        setSize(425, 250);
//             pack();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    DecDialog(Message m, Main mn)
    {
        super(mn, "Message Encrypted");
        this.msg = m;
        this.m = mn;
        setLayout(new GridLayout(0, 1));
        exp = Factory.createTextField(null);
        mod = Factory.createTextField(null);
        
        JPanel lp = new JPanel();
        lp.setLayout(new FlowLayout(FlowLayout.CENTER));
        lp.add(new JLabel("<html><p align=\"center\"><font color=red><b>This message appears to be encrypted. Please enter your PRIVATE KEY<br>" + 
            "details below to decrypt and view.</b></font></p></html>"));
        add(lp);
        add(new JLabel(" Enter your PRIVATE KEY exponent (base " + Defaults.KEY_BASE + "):"));
        add(exp);
        add(new JLabel(" Enter your PRIVATE KEY modulus (base " + Defaults.KEY_BASE + "):"));
        add(mod);
//         Key key;
//         if ((key = parent.getKey()) != null) {
//             exp.setText(key.getExponent().toString());
//             mod.setText(key.getModulus().toString());
//         }
//             add(new JLabel(""));
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout(FlowLayout.CENTER));
        p.add(Factory.createButton("Decrypt", this));
        p.add(Factory.createButton("Show Original", this));
        add(p);
        setSize(425, 300);
//             pack();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private Message decrypt(Message m, Key key) throws Exception
    {
        if (m instanceof MimeMessage && m.getContent() instanceof Multipart) {
            MimeMessage msg = new MimeMessage((MimeMessage) m);

            Multipart mm = (Multipart) m.getContent();
            int n = mm.getCount();
            for (int i = 0; i < n; i++) {
                MimeBodyPart mbp = (MimeBodyPart) mm.getBodyPart(i);
                boolean isAttachment = mbp.getDisposition() == null || mbp.getDisposition().equals(Part.ATTACHMENT) && !mbp.getDisposition().equals(Part.INLINE);
                if (mbp.getContent() instanceof String && isAttachment) {
//                     System.out.println("***");
                    String ct = (String) mbp.getContent();
                    BigInteger dm = RSA.decrypt(new BigInteger(ct), key);
                    String s = new String(dm.toByteArray());
                    mm.removeBodyPart(i);
                    MimeBodyPart nmbp = new MimeBodyPart();
                    nmbp.setContent(s, mbp.getContentType());
                    mm.addBodyPart(nmbp, i);
                }
            }
            msg.setContent(mm);
            return msg;
        }
        
        return m;
    }
    
    private void decryptPart(Message msg, Message m, Key key)
    {
        
    }
    
    public void actionPerformed(ActionEvent ae)
    {
        String es = exp.getText().trim();
        String ms = mod.getText().trim();
        if (ae.getActionCommand().equalsIgnoreCase("decrypt") && es.length() > 0 && ms.length() > 0) {
            try {
                Key priKey = new Key(es, ms);
                MessageViewer nmv = new MessageViewer(decrypt(msg, priKey), this.m);
                nmv.setPrivateKey(priKey);
            }catch (Exception e) {
                showErrorDialog(e);
            }
//             parent.decrypt(es, ms);
        }else {
            try {
                MessageViewer nmv = new MessageViewer(msg, this.m);
                nmv.noVerifyDigitalSignature();
            }catch (Exception e) {
                showErrorDialog(e);
            }
        }
        setVisible(false);
        dispose();
    }
    
    private void showErrorDialog(Exception e)
    {
        JOptionPane.showMessageDialog(this, "An error occurred: " + e, "Error", JOptionPane.ERROR_MESSAGE);
//         e.printStackTrace();
    }
    
    private void showErrorDialog(String msg, String title)
    {
        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.ERROR_MESSAGE);
    }
    
    private void showErrorDialog(String msg)
    {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}

