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
import javax.activation.*;
import java.util.StringTokenizer;
import java.math.BigInteger;
import jmail.secure.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;
import java.beans.*;
import jmail.digiSign.*;


class MessageComposer extends JFrame implements Defaults, ActionListener, WindowListener, KeyListener
{
    private static final int IN = 0;
    private static final int OUT = 1;
    private static final int DWIDTH = 300;
    private static final int DHEIGHT = 200;
    
    private static ImageIcon sentIcon = new Factory().createImageIcon(MSG_SENT_ICON);
    private static ImageIcon saveIcon = new Factory().createImageIcon(MSG_SAVE_ICON);
    
    private Main parent;
    
    private AddressBookPopup pto, pcc, pbcc;
    
//     private static final String[] TAGS = {
//         "address", "applet", "area", "a", "base", "basepoint", "big", "blockquote", "body", "br", "b", "caption", "center", "cite", "code", "dd", "dfn", "dir", "div",
//         "dl", "dt", "em", "font", "form", "h1", "h2", "h3", "h4", "h5", "h6", "head", "hr", "html", "img", "input", "isindex", "i", "kbd", "link", "li", "map", "menu", 
//         "meta", "ol", "option", "param", "pre", "p", "samp", "script", "select", "small", "strike", "string", "style", "sub", "sup", "table", "td", "textarea", "th", 
//         "title", "tr", "tt", "ul", "u", "var"
//     };
    
    private JDialog ad;
    private JDialog sendD, saveD;
    
    private JLabel ccL;
    private JLabel bccL;
    private JLabel toL;
    private JLabel subL;
    private JTextField cc;
    private JTextField bcc;
    private JTextField to;
    private JTextField sub;
    private JEditorPane contents;
    private JButton mode;
    private JButton vat;
    private JButton sendB;
    private JRadioButton bmd5, bsha1, bsha256;
    private JCheckBox dsc;
    private JCheckBox encrypt;
    
    private Message msg;
    private String msgBody;
    private String text;
    private String plaintext;
    private String richText;
    private String ctype;
    
    private Key publicKey = new Key("105539069795668187301843347782219979272387677939278506091967032548993015773713",
                                "12246641146915474200483271191099148040739686057436874659738475164035619744233809189365535762694662368043130950787602085247679515959777934951414238669598269");;
    
    private Vector<Part> attachments;
    private Vector<Vector<TextPart>> texts = new Vector<Vector<TextPart>>();
    
    private boolean html = true;
    private boolean sent = false;
    private boolean saved = true;
//     private SaveMBPThread mbpThread;
//     private SaveStreamThread streamThread;
    private JFileChooser fileChooser;
    
    public MessageComposer(Main parent) throws Exception
    {
        super("Message Composer");
        
        setIconImage(new Factory().createImageIcon(MAIL_ICON2).getImage());
        addWindowListener(this);
        
//         this.msg = msg;
        this.parent = parent;
        this.attachments = new Vector<Part>();
        this.msgBody = new String();
        this.fileChooser = new JFileChooser();
        createSendingDialog();
        createSavingDialog();
        
        String frm = new String();
        String t = new String();
        Address[] a;
        
        ccL =   new JLabel("<html><b>Cc: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b></html>");
        bccL =  new JLabel("<html><b>Bcc: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b></html>");
        toL =   new JLabel("<html><b>To: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp</b></html>");
        subL =  new JLabel("<html><b>Subject: &nbsp;</b></html>");
//         subL.setFont(SUB_FONT);
        
        cc = Factory.createTextField(this);
        bcc = Factory.createTextField(this);
        to = Factory.createTextField(this);
        // to.setComponentPopupMenu(jpopupmenu);
        sub = Factory.createTextField(this);
        
        JPanel ccp = createPanel();
        ccp.add(ccL, BorderLayout.WEST);
        ccp.add(cc, BorderLayout.CENTER);
        ccp.add(Factory.createButton("...", "...cc", "Select contact(s)", this), BorderLayout.EAST);
        
        JPanel bccp = createPanel();
        bccp.add(bccL, BorderLayout.WEST);
        bccp.add(bcc, BorderLayout.CENTER);
        bccp.add(Factory.createButton("...", "...bcc", "Select contact(s)", this), BorderLayout.EAST);
        
        JPanel tp = createPanel();
        tp.add(toL, BorderLayout.WEST);
        tp.add(to, BorderLayout.CENTER);
        tp.add(Factory.createButton("...", "...to", "Select contact(s)", this), BorderLayout.EAST);
        
        JPanel sp = createPanel();
        sp.add(subL, BorderLayout.WEST);
        sp.add(sub, BorderLayout.CENTER);
        
        
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(0, 1));
        p.add(tp);
        p.add(ccp);
        p.add(bccp);
        p.add(sp);
        
        this.contents = new JEditorPane(this.ctype = "text/plain", "");
        this.contents.setFont(CONTENTS_FONT);
        this.contents.addKeyListener(this);
//         this.contents = new JLabel();
//         this.contents.setBackground(Color.WHITE);
//         this.contents.setEditable(false);
        JScrollPane scroller = new JScrollPane(this.contents);
//         displayContents();

        JPanel p2 = new JPanel();
        p2.setLayout(new FlowLayout(FlowLayout.LEFT));
//         p2.add(Factory.createButton("Reply", this));
        p2.add(Factory.createButton("Save Draft", this));
        p2.add(mode = Factory.createButton("Rich Text", this));
        p2.add(sendB = Factory.createButton("Send", this));
        
        JPanel p3 = new JPanel();
        p3.setLayout(new FlowLayout(FlowLayout.LEFT));
        p3.add(Factory.createButton("Add Attachment", this));
        p3.add(vat = Factory.createButton("View Attachments", this));
        p3.add(encrypt = Factory.createCheckbox("Use Encryption"));
        p3.add(dsc = Factory.createCheckbox("Attach Digital Signature"));
        
        JPanel dsp = new JPanel();
        dsp.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.bmd5 = new JRadioButton("MD5", true);
        this.bsha1 = new JRadioButton("SHA-1");
        this.bsha256 = new JRadioButton("SHA-256");
        ButtonGroup bg = new ButtonGroup();
        bg.add(this.bmd5);
        bg.add(this.bsha1);
        bg.add(this.bsha256);
        
        dsp.add(this.bmd5);
        dsp.add(this.bsha1);
        dsp.add(this.bsha256);
        dsp.setBorder(new TitledBorder("Signature Hash Algorithm"));
        
        p3.add(dsp);
        
//         p3.add(Factory.createButton("Digital Signature Options", this));
        vat.setEnabled(false);
        p3.setBorder(new TitledBorder(""));
        
        JPanel p4 = new JPanel();
        p4.setLayout(new BorderLayout());
        p4.add(p2, BorderLayout.SOUTH);
        p4.add(p3, BorderLayout.CENTER);
        
        add(p, BorderLayout.NORTH);
        add(scroller, BorderLayout.CENTER);
        add(p4, BorderLayout.SOUTH);
        
//         Thread thread = new Thread(this);
//         thread.start();
        
        setSize(600, 500);
        setDefaultLookAndFeelDecorated(true);
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setVisible(true);
        
//         try {
//             setVisible(true);
//         }catch (Exception e) {
//             this.contents.setContentType("text/plain");
//             forcedDisplayContents();
//             setVisible(true);
//         }
    }
    
    public MessageComposer(Message m, Main parent) throws Exception
    {
        super("Message Composer");
        
        setIconImage(new Factory().createImageIcon(MAIL_ICON2).getImage());
        addWindowListener(this);
        
        this.parent = parent;
        this.msg = m;
        this.attachments = new Vector<Part>();
        this.msgBody = new String();
        this.fileChooser = new JFileChooser();
        createSendingDialog();
        createSavingDialog();
        
        String frm = new String();
        String t = new String();
        Address[] a;
        
        ccL =   new JLabel("<html><b>Cc: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b></html>");
        bccL =  new JLabel("<html><b>Bcc: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b></html>");
        toL =   new JLabel("<html><b>To: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp</b></html>");
        subL =  new JLabel("<html><b>Subject: </b></html>");
//         subL.setFont(SUB_FONT);
        
        cc = Factory.createTextField(this);
        bcc = Factory.createTextField(this);
        to = Factory.createTextField(this);
        sub = Factory.createTextField(this);
        
        JPanel ccp = createPanel();
        ccp.add(ccL, BorderLayout.WEST);
        ccp.add(cc, BorderLayout.CENTER);
        ccp.add(Factory.createButton("...", "...cc", "Select contact(s)", this), BorderLayout.EAST);
        
        JPanel bccp = createPanel();
        bccp.add(bccL, BorderLayout.WEST);
        bccp.add(bcc, BorderLayout.CENTER);
        bccp.add(Factory.createButton("...", "...bcc", "Select contact(s)", this), BorderLayout.EAST);
        
        JPanel tp = createPanel();
        tp.add(toL, BorderLayout.WEST);
        tp.add(to, BorderLayout.CENTER);
        tp.add(Factory.createButton("...", "...to", "Select contact(s)", this), BorderLayout.EAST);
        
        JPanel sp = createPanel();
        sp.add(subL, BorderLayout.WEST);
        sp.add(sub, BorderLayout.CENTER);
        
        
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(0, 1));
        p.add(tp);
        p.add(ccp);
        p.add(bccp);
        p.add(sp);
        
        this.contents = new JEditorPane(this.ctype = "text/plain", "");
        this.contents.setFont(CONTENTS_FONT);
        this.contents.addKeyListener(this);
//         this.contents = new JLabel();
//         this.contents.setBackground(Color.WHITE);
//         this.contents.setEditable(false);
        JScrollPane scroller = new JScrollPane(this.contents);
//         displayContents();
        
        JPanel p2 = new JPanel();
        p2.setLayout(new FlowLayout(FlowLayout.LEFT));
        p2.add(Factory.createButton("Reply", this));
        p2.add(Factory.createButton("Save Draft", this));
        p2.add(mode = Factory.createButton("Rich Text", this));
        p2.add(sendB = Factory.createButton("Send", this));
        
        JPanel p3 = new JPanel();
        p3.setLayout(new FlowLayout(FlowLayout.LEFT));
        p3.add(Factory.createButton("Add Attachment", this));
        p3.add(vat = Factory.createButton("View Attachments", this));
        p3.add(dsc = Factory.createCheckbox("Attach Digital Signature"));
        p3.add(encrypt = Factory.createCheckbox("Use Encryption"));
//         p3.add(Factory.createButton("Encryption Options", this));
        vat.setEnabled(false);
        p3.setBorder(new TitledBorder(""));
        
        parseMIME();
        
        JPanel p4 = new JPanel();
        p4.setLayout(new BorderLayout());
        p4.add(p2, BorderLayout.SOUTH);
        p4.add(p3, BorderLayout.CENTER);
        
        add(p, BorderLayout.NORTH);
        add(scroller, BorderLayout.CENTER);
        add(p4, BorderLayout.SOUTH);
        
//         Thread thread = new Thread(this);
//         thread.start();
        
        setSize(600, 500);
        setDefaultLookAndFeelDecorated(true);
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setVisible(true);
        
//         try {
//             setVisible(true);
//         }catch (Exception e) {
//             this.contents.setContentType("text/plain");
//             forcedDisplayContents();
//             setVisible(true);
//         }
    }
    
    void setTo(String str)
    {
        to.setText(str);
    }
    
    void setCC(String str)
    {
        cc.setText(str);
    }
    
    void setBCC(String str)
    {
        bcc.setText(str);
    }
    
    private void setKey(String e, String m)
    {
        try {
            publicKey = new Key(e, m);
        }catch (Exception exp) {
            
        }
    }
    
    private Key getKey()
    {
        return this.publicKey;
    }
    
    private void save() throws Exception
    {
        MimeMessage msg = parent.getMimeMessage();
                
        String tos = to.getText();
        StringTokenizer tok = new StringTokenizer(tos, ", ");
        
        InternetAddress[] addressTo = new InternetAddress[tok.countTokens()];
        for (int i = 0; tok.hasMoreTokens(); i++) {
            addressTo[i] = new InternetAddress(tok.nextToken());
        }
        msg.setRecipients(Message.RecipientType.TO, addressTo);
        
        String ccs = cc.getText();
        tok = new StringTokenizer(ccs, ",");
        InternetAddress[] addressCC = new  InternetAddress[tok.countTokens()];
        for (int i = 0; tok.hasMoreTokens(); i++) {
            addressCC[i] = new InternetAddress(tok.nextToken());
        }
        msg.setRecipients(Message.RecipientType.CC, addressCC);
        
        String bccs = bcc.getText();
        tok = new StringTokenizer(bccs, ",");
        InternetAddress[] addressBCC = new InternetAddress[tok.countTokens()];
        for (int i = 0; tok.hasMoreTokens(); i++) {
            addressBCC[i] = new InternetAddress(tok.nextToken());
        }
        msg.setRecipients(Message.RecipientType.BCC, addressBCC);
        
        MimeBodyPart body = new MimeBodyPart();
        body.setContent(this.contents.getText(), detectContentType());
        
    
        Multipart multi = new MimeMultipart();
        multi.addBodyPart(body);
        
        int n = attachments.size();
        for (int i = 0; i < n; i++) {
            BodyPart p = (BodyPart) attachments.elementAt(i);
            multi.addBodyPart(p);
        }
        msg.setSubject(sub.getText());
        msg.setContent(multi);
        
        this.saveD.setVisible(true);
        SaveThread svt = new SaveThread(msg, this.saveD);
        svt.start();
        
        saved = true;
//         parent.refreshFolder();
//         parent.saveDraft(msg);
    }
    
    private void createSavingDialog()
    {
        saveD = new JDialog(this, "Saving...");
        saveD.add(new JLabel("Saving draft. Please wait.", saveIcon, JLabel.CENTER));
        saveD.setDefaultCloseOperation(HIDE_ON_CLOSE);
        saveD.pack();
        saveD.setResizable(false);
        saveD.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        saveD.setLocationRelativeTo(null);
    }
    
    private void createSendingDialog()
    {
        this.sendD = new JDialog(this, "Sending...");
        this.sendD.add(new JLabel("Sending message. Please wait.", sentIcon, JLabel.CENTER));
        this.sendD.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.sendD.pack();
        this.sendD.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.sendD.setResizable(false);
        this.sendD.setLocationRelativeTo(null);
    }
    
    private void sendMessage()
    {
        MimeMessage mimeMsg = parent.createMimeMessage();
    }
    
    private JPanel createPanel()
    {
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        p.setBorder(new TitledBorder(""));
        
        return p;
    }
    
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
                    attachments.addElement(p);
                    vat.setEnabled(true);
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
//             e.printStackTrace();
        }
        
        String frm = new String();
        String t = new String();
        Address[] a;
        
        try {
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
            to.setText(t);
            
            t = "";
            if ((a = this.msg.getRecipients(Message.RecipientType.CC)) != null) {
    //             System.out.println("to cc: " + a.length);
//                t = ", ";
                for (int j = 0; j < a.length; j++) {
                    t += a[j].toString();
                    if (j != (a.length - 1)) {
                        t += ", ";
                    }
                }
            }
            cc.setText(t);
            
            t = "";
            if ((a = this.msg.getRecipients(Message.RecipientType.BCC)) != null) {
    //             System.out.println("to cc: " + a.length);
//                 t = ", ";
                for (int j = 0; j < a.length; j++) {
                    t += a[j].toString();
                    if (j != (a.length - 1)) {
                        t += ", ";
                    }
                }
            }
            bcc.setText(t);
            
            // SUBJECT
            String sbj = this.msg.getSubject();
            sub.setText(sbj);
            
        }catch (Exception e) {
            showErrorDialog("Error retrieving information from message header", "Error");
        }
        
        int n = texts.size();
        boolean textSet = (n == 0) ? true : false;
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
                    if (this.richText.toLowerCase().startsWith("<htm>")) {
                        this.ctype = "text/html";
                        this.contents.setContentType(this.ctype);
                    }
//                     this.ctype = text.getContentType().toLowerCase();
//                     this.contents.setContentType(this.ctype);
//                     System.out.println(ctype);
                    this.contents.setText(this.richText);
//                     if (this.ctype.startsWith("text/html") && stripHTML(this.contents.getText()).trim().length() == 0) { // if cannot display HTML (JEditorPane sucks!!!)
// //                         System.out.println("<--->");
// //                         this.contents.setContentType(this.ctype);
//                         continue;
//                     }
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
    
    private void showDialog(String msg, String title)
    {
        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showDialog(String msg, String title, Icon icon)
    {
        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.INFORMATION_MESSAGE, icon);
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
//                         mbpThread = new SaveMBPThread(mbp, f);
//                         mbpThread.start();
//                         return;
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
    
//     private void encrypt(InputStream in, File f) throws Exception
//     {
//         FileOutputStream fout = new FileOutputStream(f);
//         byte [] buff = new byte[ENC_BUFFER_SIZE];
//         byte [] outBuff;
//         while (in.read(buff) != -1) {
//             BigInteger b = RSA.encrypt(new BigInteger(buff), this.publicKey);
//             outBuff = b.toByteArray();
//             fout.write(outBuff);
//             fout.write(ENC_SEPARATOR);
//         }
//         fout.close();
//         in.close();
//     }
    
    public void actionPerformed(ActionEvent ae)
    {
        String cmd = ae.getActionCommand().toLowerCase();
        if (cmd.equalsIgnoreCase("save draft")) {
            try {
                save();
            }catch (Exception e) {
                showErrorDialog("Error saving draft", "Error");
                e.printStackTrace();
            }
//             while (true) {
// //                 fileChooser = new JFileChooser();
//                 int ec = fileChooser.showSaveDialog(this);
//                 if (ec == JFileChooser.APPROVE_OPTION) {
//                     File f = fileChooser.getSelectedFile();
//                     if (f.exists()) {
//                         ec = JOptionPane.showConfirmDialog(this, "File exists. Overwrite?", "Overwrite File", JOptionPane.YES_NO_OPTION);
//                         if (ec != JOptionPane.YES_OPTION) {
//                             continue;
//                         }
//                     }
//                     try {
//                         PrintWriter pout = new PrintWriter(f);
//                         pout.println(msgBody);
//                         pout.close();
//                         return;
//                     }catch (Exception e) {
//                         showErrorDialog("Error saving draft", "Error");
// //                         System.err.println(e);
//                     }
//                 }else {
//                     return;
//                 }
//             }
        }else if (cmd.startsWith("$")) {
            saveAttachment(Integer.parseInt(cmd.substring(1, cmd.length())));
        }else if (cmd.equalsIgnoreCase("plain text")) {
            mode.setText("Rich Text");
            mode.setActionCommand("Rich Text");
            this.msgBody = this.contents.getText();
            this.contents.setContentType("text/plain");
            this.contents.setText(this.msgBody);
        }else if (cmd.equalsIgnoreCase("rich text")) {
            mode.setText("Plain Text");
            mode.setActionCommand("Plain text");
            this.msgBody = this.contents.getText();
            this.contents.setContentType(this.ctype = detectContentType());
            this.contents.setText(msgBody);
        }else if (cmd.equals("...to")) {
            if (pto == null) {
                pto = new AddressBookPopup(this, AddressBookPopup.TO);
            }else {
                pto.setVisible(true);
            }
        }else if (cmd.equals("...bcc")) {
            if (pbcc == null) {
                pbcc = new AddressBookPopup(this, AddressBookPopup.BCC);
            }else {
                pbcc.setVisible(true);
            }
        }else if (cmd.equals("...cc")) {
            if (pcc == null) {
                pcc = new AddressBookPopup(this, AddressBookPopup.CC);
            }else {
                pcc.setVisible(true);
            }
        }else if (cmd.equalsIgnoreCase("add attachment")) {
            try {
                int ec = fileChooser.showOpenDialog(this);
                if (ec == JFileChooser.APPROVE_OPTION) {
                    File f = fileChooser.getSelectedFile();
                    MimeBodyPart mbp = new MimeBodyPart();
                    FileDataSource fds = new FileDataSource(f);
                    mbp.setDataHandler(new DataHandler(fds));
                    mbp.setFileName(f.getName());
                    attachments.add(mbp);
                    if (!vat.isEnabled()) {
                        vat.setEnabled(true);
                    }
                }
            }catch (Exception e) {
                showErrorDialog("Could not add the requested attachment.", "Error");
            }
        }else if (cmd.equalsIgnoreCase("view attachments")) {
            try {
                showAttachments();
            }catch (Exception e) {
                showErrorDialog("Could not display attachment(s)", "Error");
            }
        }else if (cmd.equalsIgnoreCase("encryption options")) {
//             EncDialog ed = new EncDialog(this);
        }else if (cmd.startsWith("send")) {
            try {
                if (encrypt.isSelected()) {
                    sendB.setEnabled(false);
                    
                    String tos = to.getText();
                    StringTokenizer tok = new StringTokenizer(tos, ", ");
                    
                    String ccs = cc.getText();
                    StringTokenizer tok2 = new StringTokenizer(ccs, ",");
                    
                    String bccs = bcc.getText();
                    StringTokenizer tok3 = new StringTokenizer(bccs, ",");
                    
                    InternetAddress [] addressTo = new InternetAddress[tok.countTokens() + tok2.countTokens() + tok3.countTokens()];
                    int ii = 0;
                    for (; tok.hasMoreTokens(); ii++) {
                        addressTo[ii] = new InternetAddress(tok.nextToken());
                    }
                    
                    for (; tok2.hasMoreTokens(); ii++) {
                        addressTo[ii] = new InternetAddress(tok2.nextToken());
                    }
                    
                    for (; tok3.hasMoreTokens(); ii++) {
                        addressTo[ii] = new InternetAddress(tok3.nextToken());
                    }
                    
                    for (int j = 0; j < addressTo.length; j++) {
                        MimeMessage msg = parent.createMimeMessage();
                        
                        msg.setRecipient(Message.RecipientType.TO, addressTo[j]);
                        Key publicKey = getPublicKey(addressTo[j]); // obtain public key here
                        if (publicKey == null) {
                            showErrorDialog("" + addressTo[j] + " does not have a public key.", "Missing Public Key");
                            KeyDialog kd = new KeyDialog(this);
                            publicKey = kd.getKey();
                            // prompt to enter public key
//                             continue;
                        }
                        
                        MimeBodyPart body = new MimeBodyPart();
                        
                        String pt = this.contents.getText();
                        
        //                 JDialog dd = new JDialog(this);
        //                 showEncDialog(dd);
                       
                        msg.addHeader(ENC_HEADER_NAME, ENC_HEADER_VALUE);
                        
                        BigInteger c = RSA.encrypt(new BigInteger(pt.getBytes()), publicKey);
                        body.setContent(c.toString(), detectContentType());
                    
                        Multipart multi = new MimeMultipart();
                        multi.addBodyPart(body);
                        
                        boolean dsReqd = dsc.isSelected(); // check if digital signature is required
                        File dsf = null;
                        Key privateKey = null;
                        Document document = null;
                        Element rootElement = null;
                        if (dsReqd) {
                            KeyDialog kd = new KeyDialog(this, true);
                            privateKey = kd.getKey();
                            
                            dsf = new File(DIGI_SIGN_FILE);
                            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                            document = documentBuilder.newDocument();
                            rootElement = document.createElement(ROOT_ELEM);//"DigitalSignature");
                            document.appendChild(rootElement);
                            
                            String hash = new String();
                            String ha = new String();
                            byte [] msgB = this.contents.getText().getBytes();
                            if (bmd5.isSelected()) {
                                hash = MD5.doHash(msgB);
                                ha = MD5_ID;//"MD5";
                            }else if (bsha1.isSelected()) {
                                hash = SHA1.hash(msgB);
                                ha = SHA1_ID;//"SHA-1";
                            }else {
                                hash = SHA256.hash(msgB);
                                ha = SHA256_ID;//"SHA-256";
                            }
                            
                            Element hashAlgo = document.createElement(HASH_ALGO_TAG);//"HashAlgorithm");
                            hashAlgo.appendChild(document.createTextNode(ha));
                            rootElement.appendChild(hashAlgo);
                            
                            Element bodyHash = document.createElement(BODY_TEXT_TAG);//"BodyText");
                            
                            String encHash = RSA.encrypt(hash, privateKey);
                            bodyHash.appendChild(document.createTextNode(encHash));
                            rootElement.appendChild(bodyHash);
                        }
                        
                        int n = attachments.size();
                        for (int i = 0; i < n; i++) {
                            // add encrypted file
                            MimeBodyPart mbp = (MimeBodyPart) attachments.elementAt(i);
                            File f = new File(mbp.getFileName());
                            Codec.encrypt(mbp.getInputStream(), f, this.publicKey);
                            MimeBodyPart p = new MimeBodyPart();
                            FileDataSource fds = new FileDataSource(f);
                            p.setDataHandler(new DataHandler(fds));
                            p.setFileName(f.getName());
                            
                            if (dsReqd) {
                                String hash = new String();
                                if (bmd5.isSelected()) {
                                    hash = MD5.doHash(p.getInputStream());
                                }else if (bsha1.isSelected()) {
                                    hash = SHA1.hash(p.getInputStream());
                                }else {
                                    hash = SHA256.hash(p.getInputStream());
                                }
                                
                                String encHash = RSA.encrypt(hash, privateKey);
                                
                                Element attHash = document.createElement(CRYPTO_HASH_TAG);//"cryptoHash");
                                
                                Element attF = document.createElement(FILE_NAME_TAG);//"fileName");
                                attF.appendChild(document.createTextNode(f.getName()));
                                
                                Element attH = document.createElement(HASH_VALUE_TAG);//"hashValue");
                                attH.appendChild(document.createTextNode(encHash));
                                
                                attHash.appendChild(attF);
                                attHash.appendChild(attH);
//                                 attHash.appendChild(document.createTextNode(encHash));
                                rootElement.appendChild(attHash);
                            }
                            
                            multi.addBodyPart(p);
                            
                            
                        }
                        
                        // attach digital signature
                        if (dsReqd) {
                            TransformerFactory transformerFactory = TransformerFactory.newInstance();
                            Transformer transformer = transformerFactory.newTransformer();
                            DOMSource source = new DOMSource(document);
                            StreamResult result =  new StreamResult(new FileOutputStream(dsf));
                            transformer.transform(source, result);
                            
                            // add digital signature as attachment
                            MimeBodyPart p = new MimeBodyPart();
                            FileDataSource fds = new FileDataSource(dsf);
                            p.setDataHandler(new DataHandler(fds));
                            p.setFileName(dsf.getName());
                            multi.addBodyPart(p);
                        }
                        
                        
        //                 dd.setVisible(false);
                        
                        msg.setSubject(sub.getText());
                        msg.setContent(multi);
                        
                        sendD.setVisible(true);
                        SendThread std = new SendThread(msg, sendD);
                        std.start();
        //                 std.join();
        //                 (new Thread() {
        //                     public void run()
        //                     {
        //                         sendD.setVisible(true);
        //                         parent.sendMail(msg);
        //                         sendD.setVisible(false);
        //                     }
        //                 }).start();
        //                 this.parent.sendMail(msg);
                        sent = true;
                    }
                }else {
                    sendB.setEnabled(false);
                    
                    
                    MimeMessage msg = parent.createMimeMessage();
                    
                    
                    String tos = to.getText();
                    StringTokenizer tok = new StringTokenizer(tos, ", ");
                    
                    InternetAddress[] addressTo = new InternetAddress[tok.countTokens()];
                    for (int i = 0; tok.hasMoreTokens(); i++) {
                        addressTo[i] = new InternetAddress(tok.nextToken());
                    }
                    
                    msg.setRecipients(Message.RecipientType.TO, addressTo);
                    
                    String ccs = cc.getText();
                    tok = new StringTokenizer(ccs, ",");
                    InternetAddress[] addressCC = new  InternetAddress[tok.countTokens()];
                    for (int i = 0; tok.hasMoreTokens(); i++) {
                        addressCC[i] = new InternetAddress(tok.nextToken());
                    }
                    msg.setRecipients(Message.RecipientType.CC, addressCC);
                    
                    String bccs = bcc.getText();
                    tok = new StringTokenizer(bccs, ",");
                    InternetAddress[] addressBCC = new InternetAddress[tok.countTokens()];
                    for (int i = 0; tok.hasMoreTokens(); i++) {
                        addressBCC[i] = new InternetAddress(tok.nextToken());
                    }
                    msg.setRecipients(Message.RecipientType.BCC, addressBCC);
                    
                    MimeBodyPart body = new MimeBodyPart();
                    
                    String pt = this.contents.getText();
                    
    //                 JDialog dd = new JDialog(this);
    //                 showEncDialog(dd);
                    
                    body.setContent(pt, detectContentType());
                    
                
                    Multipart multi = new MimeMultipart();
                    multi.addBodyPart(body);
                    
                    
                    boolean dsReqd = dsc.isSelected(); // check if digital signature is required
                    File dsf = null;
                    Key privateKey = null;
                    Document document = null;
                    Element rootElement = null;
                    if (dsReqd) {
                        KeyDialog kd = new KeyDialog(this, true);
                        privateKey = kd.getKey();
                        
                        dsf = new File(DIGI_SIGN_FILE);
                        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                        document = documentBuilder.newDocument();
                        rootElement = document.createElement(ROOT_ELEM);//"DigitalSignature");
                        document.appendChild(rootElement);
                        
                        byte [] msgB = this.contents.getText().getBytes();
                        String hash = new String();
                        String ha = new String();
                        if (bmd5.isSelected()) {
                            hash = MD5.doHash(msgB);
                            ha = MD5_ID;//"MD5";
                        }else if (bsha1.isSelected()) {
                            hash = SHA1.hash(msgB);
                            ha = SHA1_ID;//"SHA-1";
                        }else {
                            hash = SHA256.hash(msgB);
                            ha = SHA256_ID;//"SHA-256";
                        }
                        
                        Element hashAlgo = document.createElement(HASH_ALGO_TAG);//"HashAlgorithm");
                        hashAlgo.appendChild(document.createTextNode(ha));
                        rootElement.appendChild(hashAlgo);
                        
                        Element bodyHash = document.createElement(BODY_TEXT_TAG);//"BodyText");
                        
                        String encHash = RSA.encrypt(hash, privateKey);
                        bodyHash.appendChild(document.createTextNode(encHash));
                        rootElement.appendChild(bodyHash);
                    }
                    
                    int n = attachments.size();
                    for (int i = 0; i < n; i++) {
                        // add attachment
                        BodyPart p = (BodyPart) attachments.elementAt(i);
                        File f = new File(p.getFileName());
                        
                        if (dsReqd) {
                            String hash = new String();
                            if (bmd5.isSelected()) {
                                hash = MD5.doHash(p.getInputStream());
                            }else if (bsha1.isSelected()) {
                                hash = SHA1.hash(p.getInputStream());
                            }else {
                                hash = SHA256.hash(p.getInputStream());
                            }
                            String encHash = RSA.encrypt(hash, privateKey);
                            
                            Element attHash = document.createElement(CRYPTO_HASH_TAG);//"cryptoHash");
                            
                            Element attF = document.createElement(FILE_NAME_TAG);//"fileName");
                            attF.appendChild(document.createTextNode(f.getName()));
                            
                            Element attH = document.createElement(HASH_VALUE_TAG);//"hashValue");
                            attH.appendChild(document.createTextNode(encHash));
                            
                            attHash.appendChild(attF);
                            attHash.appendChild(attH);
//                                 attHash.appendChild(document.createTextNode(encHash));
                            rootElement.appendChild(attHash);
                        }
                        
                        multi.addBodyPart(p);
                        
                    }
                    
                    // attach digital signature
                    if (dsReqd) {
                        TransformerFactory transformerFactory = TransformerFactory.newInstance();
                        Transformer transformer = transformerFactory.newTransformer();
                        DOMSource source = new DOMSource(document);
                        StreamResult result =  new StreamResult(new FileOutputStream(dsf));
                        transformer.transform(source, result);
                        
                        // add digital signature as attachment
                        MimeBodyPart p = new MimeBodyPart();
                        FileDataSource fds = new FileDataSource(dsf);
                        p.setDataHandler(new DataHandler(fds));
                        p.setFileName(dsf.getName());
                        multi.addBodyPart(p);
                    }
                    
    //                 dd.setVisible(false);
                    
                    msg.setSubject(sub.getText());
                    msg.setContent(multi);
                    
                    sendD.setVisible(true);
                    SendThread std = new SendThread(msg, sendD);
                    std.start();
    //                 std.join();
    //                 (new Thread() {
    //                     public void run()
    //                     {
    //                         sendD.setVisible(true);
    //                         parent.sendMail(msg);
    //                         sendD.setVisible(false);
    //                     }
    //                 }).start();
    //                 this.parent.sendMail(msg);
                    sent = true;
                }
                
            }catch (Exception e) {
                showErrorDialog("Error sending mail: " + e, "Error");
//                 e.printStackTrace();
            }
            sendB.setEnabled(true);
        }else if (cmd.startsWith("#")) {
            attachments.remove(Integer.parseInt(cmd.substring(1, cmd.length())));
            ad.setVisible(false);
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
                
//                 StringTokenizer tok = new StringTokenizer(line, ",");
//                 String nom = tok.nextToken();
//                 String em = tok.nextToken();
// //                 System.out.println("|" + email + "|\n|" + em + "|" + em.equalsIgnoreCase(email));
//                 if (em.equalsIgnoreCase(email)) {
//                     System.out.println("match");
//                     tok.nextToken();
//                     tok.nextToken();
//                     String pke = tok.nextToken();
// //                     System.out.println(pke);
//                     String pkm = tok.nextToken();
// //                     System.out.println(pkm);
// //                     System.out.println("match: " + pke + " : " + pkm);
//                     return new Key(pke, pkm);
//                 }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private void showAttachments() throws Exception
    {
        ad = new JDialog(this, "Attachments");
        ad.setIconImage(new Factory().createImageIcon(ATTACHMENT_WINDOW_ICON).getImage());
        ad.setLayout(new BorderLayout());
        
        JPanel p3 = new JPanel();
        p3.setLayout(new GridLayout(0, 1));
        JScrollPane atch = null;
        if (attachments.size() > 0) {
            for (int i = 0; i < attachments.size(); i++) {
                p3.add(new Factory().createEAttachment(attachments.elementAt(i), i, this));
            }
            atch = new JScrollPane(p3);
            atch.setBorder(new TitledBorder("" + attachments.size() + ((attachments.size() > 1) ? " Attachments" : " Attachment")));
        }
        Rectangle2D rect = getGraphicsConfiguration().getBounds();
        if (ad.getWidth() > (int) rect.getWidth() || ad.getHeight() > (int) rect.getHeight()) {
            ad.setSize(500, 300);
        }
        
        ad.add(atch, BorderLayout.CENTER);
        ad.pack();
        
        ad.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        ad.setLocationRelativeTo(null);
        ad.setVisible(true);
    }
    
    private String detectContentType()
    {
        String text = this.contents.getText().toLowerCase();
        if (text.startsWith("<html")) {
            return new String("text/html");
        }
        return new String("text/plain");
    }
    
    public void windowActivated(WindowEvent we)
    {
        
    }
    
    public void windowClosed(WindowEvent we)
    {
        
    }
    
    public void windowClosing(WindowEvent we)
    {
        // write save code here
        if (!saved) {
            int ec = JOptionPane.showConfirmDialog(this, "Your draft has been modified. Save?", "Save Draft", JOptionPane.YES_NO_CANCEL_OPTION);
            if (ec == JOptionPane.YES_OPTION) {
                try {
                    save();
                }catch (Exception e) {
                    showErrorDialog("Error saving message", "Error");
                }
                setVisible(false);
            }else if (ec == JOptionPane.NO_OPTION) {
                setVisible(false);
            }
        }else {
            setVisible(false);
        }
    }
    
    public void windowDeactivated(WindowEvent we)
    {
        
    }
    
    public void windowDeiconified(WindowEvent we)
    {
        
    }
    
    public void windowIconified(WindowEvent we)
    {
        
    }
    
    public void windowOpened(WindowEvent we)
    {
        
    }
    
    public void keyPressed(KeyEvent ke)
    {
        saved = false;
    }
    
    public void keyTyped(KeyEvent ke)
    {
        saved = false;
    }
    
    public void keyReleased(KeyEvent ke)
    {
        saved = false;
    }
    
    private void showWaitDialog(JDialog dd, ActionListener parent, JLabel dl)
    {
//         JDialog dd = new JDialog((JFrame) null, "Please wait");
        JButton jb;
        dd.setIconImage(new Factory().createImageIcon(DOWNLOADING_WINDOW_ICON).getImage());
        dd.setLayout(new BorderLayout());
        dd.add(new JLabel(new Factory().createImageIcon(DOWNLOADING_ICON)), BorderLayout.WEST);
        dd.add(dl = new JLabel("<html><p align=\"center\">Saving attachment to disk. Please wait...<br></p></html>", JLabel.CENTER),
          BorderLayout.CENTER);
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout(FlowLayout.CENTER));
        p.add(jb = Factory.createButton("Cancel", parent));
        dd.add(p, BorderLayout.SOUTH);
//         jb.setToolTipText("Cancelling this operation may be potentially dangerous. Proceed at your own risk");
        dd.pack();
//         dd.setSize(DWIDTH, DHEIGHT);
        dd.setLocationRelativeTo(null);
        dd.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        dd.setVisible(true);
    }
    
    private void showEncDialog(JDialog dd)
    {
//         JDialog dd = new JDialog((JFrame) null, "Please wait");
        JButton jb;
        dd.setIconImage(new Factory().createImageIcon(ENCRYPT_ICON).getImage());
        dd.setLayout(new BorderLayout());
        dd.add(new JLabel(new Factory().createImageIcon(ENCRYPT_ICON)), BorderLayout.WEST);
        dd.add(new JLabel("<html><p align=\"center\">Encrypting message...<br></p></html>", JLabel.CENTER),
          BorderLayout.CENTER);
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout(FlowLayout.CENTER));
        p.add(jb = Factory.createButton("Cancel", parent));
        dd.add(p, BorderLayout.SOUTH);
//         jb.setToolTipText("Cancelling this operation may be potentially dangerous. Proceed at your own risk");
        dd.pack();
//         dd.setSize(DWIDTH, DHEIGHT);
        dd.setLocationRelativeTo(null);
        dd.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        dd.setVisible(true);
    }
    
    private class SendThread extends Thread
    {
        private JDialog sd;
        private Message msg;
        
        SendThread(Message m, JDialog d)
        {
            msg = m;
            sd = d;
        }
        
        public void run()
        {
            try {
                parent.sendMail(msg);
                sd.setVisible(false);
                showDialog("Message sent successfully", "Message sent");
//                 parent.refreshFolder();
            }catch (Exception e) {
                sd.setVisible(false);
                showErrorDialog("Error sending message: " + e, "Error");
            }
        }
    }
    
    private class SaveThread extends Thread
    {
        private JDialog sd;
        private Message msg;
        
        SaveThread(Message m, JDialog d)
        {
            msg = m;
            sd = d;
        }
        
        public void run()
        {
            try {
                parent.saveDraft(msg);
                sd.setVisible(false);
                showDialog("Message saved to Drafts", "Message saved");
                if (parent.draftsOpen()) {
                    parent.refreshFolder();
                }
            }catch (Exception e) {
                sd.setVisible(false);
                showErrorDialog("Error saving message: " + e, "Error");
            }
        }
    }
    
    private class SaveFileThread extends Thread implements ActionListener
    {
        private InputStream fin;
        private FileOutputStream fout;
        private File f;
        private JDialog dd;
        private JLabel dl;
        private volatile boolean cancelled = false;
        
        SaveFileThread(InputStream fin, File f)
        {
            this.dd = new JDialog((JFrame) null, "Downloading");
            this.fin = fin;
            try {
                this.fout = new FileOutputStream(this.f = f);
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
                if (fin == null || fout == null) {
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
                while (!cancelled && (len = fin.read(buf)) > 0) {
                    out.write(buf, 0, len);
//                         n += 8192;
//                         dl.setText("<html><p align=\"center\">Downloading attachment. Please wait...</p>" + 
//                                    "<p align=\"center\">Transferred " + n + " bytes</p></html>");
                }
                fin.close();
                out.close();
                this.fout.close();
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
    
//     private class EncDialog extends JDialog implements ActionListener
//     {
//         MessageComposer parent;
//         JTextField exp;
//         JTextField mod;
//         
//         EncDialog(MessageComposer mc)
//         {
//             super(mc, "Encryption Options");
//             this.parent = mc;
//             setLayout(new GridLayout(0, 1));
//             exp = Factory.createTextField(null);
//             mod = Factory.createTextField(null);
//             add(new JLabel("Enter recipient's public key exponent (base " + KEY_BASE + "):"));
//             add(exp);
//             add(new JLabel("Enter recipient's public key modulus (base " + KEY_BASE + "):"));
//             add(mod);
//             if (publicKey != null) {
//                 exp.setText(publicKey.getExponent().toString());
//                 mod.setText(publicKey.getModulus().toString());
//             }
// //             add(new JLabel(""));
//             JPanel p = new JPanel();
//             p.setLayout(new FlowLayout(FlowLayout.CENTER));
//             p.add(Factory.createButton("Ok", this));
//             add(p);
//             setSize(300, 250);
// //             pack();
//             setDefaultCloseOperation(DISPOSE_ON_CLOSE);
//             setLocationRelativeTo(null);
//             setVisible(true);
//         }
//         
//         public void actionPerformed(ActionEvent ae)
//         {
//             String es = exp.getText().trim();
//             String ms = mod.getText().trim();
//             if (es.length() > 0 && ms.length() > 0) {
//                 parent.setKey(es, ms);
//             }
//             setVisible(false);
//             dispose();
//         }
//     }
}


class KeyDialog extends JDialog implements ActionListener
{
//     MessageViewer parent;
    private JTextField exp;
    private JTextField mod;

    private Key publicKey;
    
    
    KeyDialog(MessageComposer mc)
    {
        super(mc, "Enter Public Key", true);
//         this.parent = mv;
        setLayout(new GridLayout(0, 1));
        exp = Factory.createTextField(null);
        mod = Factory.createTextField(null);
        add(new JLabel(" Enter recipient's PUBLIC KEY exponent (base " + Defaults.KEY_BASE + "):"));
        add(exp);
        add(new JLabel(" Enter recipient's PUBLIC KEY modulus (base " + Defaults.KEY_BASE + "):"));
        add(mod);
//         Key key;
//         if ((key = parent.getKey()) != null) {
//             exp.setText(key.getExponent().toString());
//             mod.setText(key.getModulus().toString());
//         }
//             add(new JLabel(""));
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout(FlowLayout.CENTER));
        p.add(Factory.createButton("Ok", this));
        add(p);
        setSize(300, 250);
//             pack();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    KeyDialog(MessageViewer mv)
    {
        super(mv, "Enter Public Key", true);
//         this.parent = mv;
        setLayout(new GridLayout(0, 1));
        exp = Factory.createTextField(null);
        mod = Factory.createTextField(null);
        add(new JLabel(" Enter sender's PUBLIC KEY exponent (base " + Defaults.KEY_BASE + "):"));
        add(exp);
        add(new JLabel(" Enter sender's PUBLIC KEY modulus (base " + Defaults.KEY_BASE + "):"));
        add(mod);
//         Key key;
//         if ((key = parent.getKey()) != null) {
//             exp.setText(key.getExponent().toString());
//             mod.setText(key.getModulus().toString());
//         }
//             add(new JLabel(""));
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout(FlowLayout.CENTER));
        p.add(Factory.createButton("Ok", this));
        add(p);
        setSize(300, 250);
//             pack();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    KeyDialog(MessageComposer mc, boolean priKey)
    {
        super(mc, "Enter Private Key", true);
//         this.parent = mv;
        setLayout(new GridLayout(0, 1));
        exp = Factory.createTextField(null);
        mod = Factory.createTextField(null);
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
        p.add(Factory.createButton("Ok", this));
        add(p);
        setSize(300, 250);
//             pack();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent ae)
    {
        String es = exp.getText().trim();
        String ms = mod.getText().trim();
        if (es.length() > 0 && ms.length() > 0) {
            try {
                this.publicKey = new Key(es, ms);
            }catch (Exception e) {
                this.publicKey = new Key("0", "0");
                showErrorDialog(e);
            }
//             parent.decrypt(es, ms);
        }
        setVisible(false);
        dispose();
    }
    
    public Key getKey()
    {
        return this.publicKey;
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