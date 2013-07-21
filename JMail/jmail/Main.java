package jmail;

 

import javax.swing.*;
import java.awt.*;
import javax.mail.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.awt.geom.*;
import javax.mail.internet.*;
import java.io.*;

import snoozesoft.systray4j.*;

public class Main extends JFrame implements Defaults, ActionListener, SysTrayMenuListener, WindowListener
{
    private JCheckBoxMenuItem blackMoon, blackEye, blueIce, blackStar, blueSteel, greenDream, silverMoon, swing, system;
    private JCheckBoxMenuItem nimbus;
    private JCheckBoxMenuItem acryl, aero, aluminium, bernstein, fast, graphite, hifi, luna, mcwin, mint, noire, smart;
    private JCheckBoxMenuItem quaqua, tiger, leopard, snowLeopard;
    
    private JTextField smtpHost, imapHost, smtpTLSport, smtpSSLport;
    
    private JButton inbox, sentmail, starred, drafts, trash, spam;
    private JMenuItem login, logout;
    private JLabel status;
    private MessageBar[] msgBar;
    private JPanel fc;
    private JPanel fcp;
    private JButton prevB, nextB;
    private JLabel dispInfo;
    private JLabel welcome;
    private JButton loginB;
    private JComboBox mc;
    
    private SysTrayMenuItem sLogout;
    
    private int ic, stc, dc, spc, tc, smc;
    
    private Rectangle2D screenRect;
    private JDialog loginD;
    
    private JTextField user;
    private JPasswordField pass;
    
    private IMAP gmail;
    private SMTP smtp;
    
    private boolean loggedIn;
    private int page, numPages;
    private int currentFrom, currentTo;
    
    private JTextArea info;
    private boolean debug = true;
    private boolean showUnread = false;
    private boolean fopening;
    private boolean checkNew;
    private static boolean running = false;

    private long now;
    
    public Main()
    {
        super("IntelliSol JMail");
        
        if (!running) {
            running = true;
        }else {
            return;
        }
        System.setProperty("mail.mime.base64.ignoreerrors", "true");
        
        addWindowListener(this);
        
        try {
            gmail = new IMAP();
            smtp = new SMTP();
            
            fcp = new JPanel();
            fcp.setLayout(new BorderLayout());
            JScrollPane scroller = new JScrollPane(fc = new JPanel());
            fc.setLayout(new GridLayout(MSG_PER_PAGE, 1));
            
            JPanel p2 = new JPanel();
            p2.setLayout(new BorderLayout());
            
            JPanel nbar = new JPanel();
            nbar.setLayout(new BorderLayout());
            nbar.add(welcome = new JLabel("", JLabel.RIGHT), BorderLayout.NORTH);
            
            JPanel toolbar = new JPanel();
            toolbar.setLayout(new FlowLayout(FlowLayout.RIGHT));
            
            this.mc = new JComboBox();
            this.mc.addActionListener(this);
            this.mc.setActionCommand("move to");
            this.mc.setFont(BUTTON_FONT);
            toolbar.add(mc);
            toolbar.add(Factory.createButton("Add Star", this));
            toolbar.add(Factory.createButton("Remove Star", this));
            toolbar.add(Factory.createButton("Mark as Read", this));
            toolbar.add(Factory.createButton("Mark as Unread", this));

            nbar.add(toolbar, BorderLayout.SOUTH);
            p2.add(nbar, BorderLayout.NORTH);
            
            JPanel selbar = new JPanel();
            JButton st = Factory.createBLButton("Starred", this);
            st.setActionCommand("*");
            selbar.setLayout(new FlowLayout(FlowLayout.LEFT));
            selbar.add(new JLabel("<html><b>Select</b></html>"));
            selbar.add(Factory.createBLButton("All", this));
            selbar.add(Factory.createBLButton("None", this));
            selbar.add(Factory.createBLButton("Read", this));
            selbar.add(Factory.createBLButton("Unread", this));
            selbar.add(st);
            selbar.add(Factory.createBLButton("Unstarred", this));
            p2.add(selbar, BorderLayout.SOUTH);
            
            JPanel navbar = new JPanel();
            navbar.setLayout(new FlowLayout(FlowLayout.RIGHT));
            navbar.add(dispInfo = new JLabel(""));
            navbar.add(prevB = Factory.createButton("Newer", this));
            navbar.add(nextB = Factory.createButton("Older", this));
            
            fcp.add(scroller, BorderLayout.CENTER);
            fcp.add(p2, BorderLayout.NORTH);
            fcp.add(navbar, BorderLayout.SOUTH);
            add(fcp);
            
            status = new JLabel(WELCOME_MSG);
            status.setFont(FONT);
            status.setBorder(new TitledBorder(""));
            add(status, BorderLayout.SOUTH);
            
            JMenuBar mbar = new JMenuBar();
            JMenu file, edit, view, con, help;
            mbar.add(file = Factory.createMenu("Action", this));
            mbar.add(con = Factory.createMenu("Contacts", this));
//             mbar.add(edit = Factory.createMenu("Edit", this));
            view = Factory.createMenu("Skin", this);
//             mbar.add(view);
            mbar.add(help = Factory.createMenu("Help", this));
            login = Factory.createMenuItem("Login", this);
            file.add(logout = Factory.createMenuItem("Logout", this));
            logout.setEnabled(false);
            file.add(Factory.createMenuItem("Exit", this));
            
//             edit.add(Factory.createMenuItem("Preferences", this));
            
            con.add(Factory.createMenuItem("Manage", this));

            blackMoon   = Factory.createCheckBoxMenuItem("Black Moon", this);
            blackEye    = Factory.createCheckBoxMenuItem("Black Eye", this);
            blueIce     = Factory.createCheckBoxMenuItem("Blue Ice", this);
            blackStar   = Factory.createCheckBoxMenuItem("Black Star", this);
            blueSteel   = Factory.createCheckBoxMenuItem("Blue Steel", this);
            greenDream  = Factory.createCheckBoxMenuItem("Green Dream", this);
            silverMoon  = Factory.createCheckBoxMenuItem("Silver Moon", this);
            
            swing       = Factory.createCheckBoxMenuItem("Swing", this);
            system      = Factory.createCheckBoxMenuItem("System", this);
            nimbus      = Factory.createCheckBoxMenuItem("Nimbus", this);
            
            acryl      = Factory.createCheckBoxMenuItem("Acryl", this);
            aero       = Factory.createCheckBoxMenuItem("Aero", this);
            aluminium  = Factory.createCheckBoxMenuItem("Aluminium", this);
            bernstein  = Factory.createCheckBoxMenuItem("Bernstein", this);
            fast       = Factory.createCheckBoxMenuItem("Fast", this);
            graphite   = Factory.createCheckBoxMenuItem("Graphite", this);
            hifi       = Factory.createCheckBoxMenuItem("HiFi", this);
            luna       = Factory.createCheckBoxMenuItem("Luna", this);
            mcwin      = Factory.createCheckBoxMenuItem("McWin", this);
            mint       = Factory.createCheckBoxMenuItem("Mint", this);
            noire      = Factory.createCheckBoxMenuItem("Noire", this);
            smart      = Factory.createCheckBoxMenuItem("Smart", this);
            
            quaqua     = Factory.createCheckBoxMenuItem("Quaqua", this);
            tiger      = Factory.createCheckBoxMenuItem("Tiger", this);
            leopard    = Factory.createCheckBoxMenuItem("Leopard", this);
            snowLeopard = Factory.createCheckBoxMenuItem("Snow Leopard", this);
            
            JMenu synth = Factory.createMenu("Synthetica", this);
            view.add(synth);
            
            synth.add(blackMoon);
            synth.add(blackStar);
            synth.add(blackEye);
            synth.add(blueIce);
            synth.add(blueSteel);
            synth.add(greenDream);
            synth.add(silverMoon);
            view.add(synth);

            JMenu jt = Factory.createMenu("JTattoo", this);
            jt.add(acryl);
            jt.add(aero);
            jt.add(aluminium);
            jt.add(bernstein);
            jt.add(fast);
            jt.add(graphite);
            jt.add(hifi);
            jt.add(luna);
            jt.add(mcwin);
            jt.add(mint);
            jt.add(noire);
            jt.add(smart);
            view.add(jt);
            
            JMenu java = Factory.createMenu("Java", this);
            java.add(nimbus);
            view.add(java);
            
            JMenu qq = Factory.createMenu("Quaqua", this);
            qq.add(quaqua);
            qq.add(tiger);
            qq.add(leopard);
            qq.add(snowLeopard);
            view.add(qq);

            help.add(Factory.createMenuItem("About", this));
//             help.add(Factory.createMenuItem("System Info", this));
            add(mbar, BorderLayout.NORTH);
            
            JPanel left = new JPanel();
            
            JPanel p3 = new JPanel();
            p3.setLayout(new BorderLayout());
            JLabel logo = new JLabel("", JLabel.CENTER);
            logo.setIcon(new Factory().createImageIcon(LOGO));
            
            Factory factory = new Factory();
            setIconImage(factory.createImageIcon(WINDOW_ICON).getImage());
            
            JPanel folders = new JPanel();
            folders.setLayout(new GridLayout(0, 1, 2, 2));
            JButton compose = Factory.createButton("Compose New Mail", this);
            compose.setIcon(factory.createImageIcon(COMPOSE_ICON));
            folders.add(compose);
            folders.add(new JSeparator());
            inbox = Factory.createButton("Inbox", this);
            inbox.setIcon(factory.createImageIcon(INBOX_ICON));
            inbox.setFont(UNSEL_FONT);
            folders.add(inbox);
            starred = Factory.createButton("Starred", this);
            starred.setIcon(factory.createImageIcon(STARRED_ICON));
            starred.setFont(UNSEL_FONT);
            folders.add(starred);
            sentmail = Factory.createButton("Sent", this);
            sentmail.setIcon(factory.createImageIcon(SENTMAIL_ICON));
            sentmail.setFont(UNSEL_FONT);
            folders.add(sentmail);
            drafts = Factory.createButton("Drafts", this);
            drafts.setIcon(factory.createImageIcon(DRAFTS_ICON));
            drafts.setFont(UNSEL_FONT);
            folders.add(drafts);
            spam = Factory.createButton("Spam", this);
            spam.setIcon(factory.createImageIcon(SPAM_ICON));
            spam.setFont(UNSEL_FONT);
            folders.add(spam);
            trash = Factory.createButton("Trash", this);
            trash.setIcon(factory.createImageIcon(TRASH_ICON));
            trash.setFont(UNSEL_FONT);
            folders.add(trash);
            
            p3.add(logo, BorderLayout.NORTH);
            p3.add(new JSeparator(), BorderLayout.CENTER);
            p3.add(folders, BorderLayout.SOUTH);
            left.add(p3, BorderLayout.NORTH);
            add(left, BorderLayout.WEST);

            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            screenRect = getGraphicsConfiguration().getBounds();
            setSize((int) screenRect.getWidth(), (int) screenRect.getHeight());
            setLocation((int) (screenRect.getWidth() - getWidth()) / 2, (int) (screenRect.getHeight() - getHeight()) / 2);
            
            // implementing system tray
            SysTrayMenuIcon sysIcon = new SysTrayMenuIcon(factory.getIconPath(SWINDOW_ICON));
            SysTrayMenu sMenu = new SysTrayMenu(sysIcon);
            sMenu.setIcon(sysIcon);
            sMenu.setToolTip("IntelliSol JMail v1.0 BETA");
            sysIcon.addSysTrayMenuListener(this);
            
            SysTrayMenuItem sExit = Factory.createSysTrayMenuItem("Exit", this);
            this.sLogout = Factory.createSysTrayMenuItem("Logout", this);
            this.sLogout.setEnabled(false);
            SysTrayMenuItem sAbt = Factory.createSysTrayMenuItem("About", this);
            
            sMenu.addItem(sExit);
            sMenu.addSeparator();
            sMenu.addItem(sLogout);
            sMenu.addItem(sAbt);
            
            CheckNewThread cnt = new CheckNewThread();
            cnt.start();
            
            if (!loggedIn) {
                showLogin();
            }
            
            
            
        }catch (Exception e) {
            showStatus("An error occurred: " + e);
            if (debug) {
                showErrorDialog(e);
            }
//             e.printStackTrace();
        }
    }
    
    private void showAboutDialog()
    {
        JDialog about = new JDialog(this, "About JMail");
        about.setLayout(new BorderLayout());
        about.add(new JLabel(new Factory().createImageIcon(Defaults.ABOUT_ICON)) {
                                    public void paint(Graphics g)
                                    {
                                        Graphics2D g2d = (Graphics2D) g;
                                        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                                        super.paint(g);
                                    }
                                }, BorderLayout.WEST);
        
        JLabel info = new JLabel("<html><font size=5>IntelliSol JMail</font><br>" + 
                                "<font size=4><i>A Secure IMAP User Agent for Gmail<sup><font size=2>TM</font></sup></i><br><br>Release: 1.1.2 BETA<br>" + 
                                "<font size=4>Authors: Ayon Ghosh, Kaustav Chattopadhyay & Arindam Guha<br><br>Powered by JavaMail 1.4.3</font><br><br>" + 
                                "<font size=2>http://theway2solstice.webs.com/code.htm<br>theway2solstice@gmail.com</font><br><br></html>") {
                                    public void paint(Graphics g)
                                    {
                                        Graphics2D g2d = (Graphics2D) g;
                                        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                                        super.paint(g);
                                    }
                                };
        info.setBorder(new TitledBorder(""));
        
        JLabel credits = new JLabel("<html>JTatto Look & Feel<br>Copyright © 2002-2007 by MH Software-Entwicklung<br>All Rights Reserved.<br>" + 
                                    "<br>Quaqua Look And Feel (Version 6.4.1)<br>Copyright © 2003-2010<br>" + 
                                    "Werner Randelshofer, Hausmatt 10, Immensee, CH-6405, Switzerland<br>" + 
                                    "http://www.randelshofer.ch/<br>werner.randelshofer@bluewin.ch<br>All Rights Reserved.<br>" + 
                                    "<br>Synthetica - the Enterprise Look and Feel<br>Copyright © Jyloo Software<br>All rights reserved.<br>" + 
                                    "<br>SysTray for Java v2.4.1 (Win32 & KDE)<br>by daveselinger & ribizli<br>Copyright © 2002-2004 SnoozeSoft<br>" + 
                                    "Distributed under LGPL license<br>http://systray.sourceforge.net<br></html>"

                         );
        credits.setBorder(new TitledBorder("Thanks to"));
        JScrollPane cs = new JScrollPane(credits);
        
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        p.add(info, BorderLayout.NORTH);
        p.add(cs, BorderLayout.CENTER);
        
        about.add(new JLabel("<html><br></html>"), BorderLayout.NORTH);
        about.add(p, BorderLayout.CENTER);
        about.add(new JLabel("<html><br><br></html>"), BorderLayout.SOUTH);
        about.pack();
        about.setSize(about.getWidth() + 30, 500);
        about.setResizable(false);
        about.setLocationRelativeTo(null);
        about.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        about.setVisible(true);
    }
    
    public MimeMessage createMimeMessage()
    {
        return smtp.createMimeMessage();
    }
    
    public MimeMessage getMimeMessage()
    {
        return gmail.createMimeMessage();
    }
    
    public void sendMail(Message msg) throws Exception
    {
        smtp.sendMessage(msg);
    }
    
    private void refreshFolderDisplay()
    {
        if (ic > 0) {
            inbox.setText("Inbox (" + ic + ")");
        }
        if (stc > 0) {
            starred.setText("Starred (" + stc + ")");
        }
//         if (dc > 0) {
//             drafts.setText("Drafts (" +dc + ")");
//         }
        if (spc > 0) {
            spam.setText("Spam (" + spc + ")");
        }
        if (tc > 0) {
            trash.setText("Trash (" + tc + ")");
        }
        for (int i = 0; i < msgBar.length; i++) {
            msgBar[i].setSelected(false);
            msgBar[i].refresh();
            msgBar[i].validate();
        }
    }
    
    public void setVisible(boolean flag)
    {
        super.setVisible(flag);
        setExtendedState(MAXIMIZED_BOTH);
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
    
    private void showLogin()
    {
        loginD = new JDialog(this, "Login");
        
//         this.smtpHost = new JTextField(GMAIL_SMTP_HOST);
//         this.smtpHost.setBorder(new TitledBorder("SMTP Host:"));
//         this.smtpHost.setHorizontalAlignment(JTextField.CENTER);
//         this.imapHost = new JTextField(GMAIL_IMAP_HOST);
//         this.imapHost.setBorder(new TitledBorder("IMAP Host:"));
//         this.imapHost.setHorizontalAlignment(JTextField.CENTER);
//         this.smtpTLSport = new JTextField("" + SMTP_TLS_PORT);
//         this.smtpTLSport.setBorder(new TitledBorder("SMTP TLS Port:"));
//         this.smtpTLSport.setHorizontalAlignment(JTextField.CENTER);
//         this.smtpSSLport = new JTextField("" + SMTP_SSL_PORT);
//         this.smtpSSLport.setBorder(new TitledBorder("SMTP SSL Port:"));
//         this.smtpSSLport.setHorizontalAlignment(JTextField.CENTER);
        
        this.user = new JTextField(USERNAME_HINT);
        this.user.setHorizontalAlignment(JTextField.CENTER);
        this.user.setBorder(new TitledBorder("Username"));
        this.pass = new JPasswordField(PASSWORD_HINT);
        this.pass.setEchoChar('*');
        this.pass.setHorizontalAlignment(JPasswordField.CENTER);
        this.pass.setBorder(new TitledBorder("Password"));
        this.loginB = Factory.createButton("Sign in", this);
        this.loginB.setEnabled(true);
        this.user.setEditable(true);
        this.pass.setEditable(true);
        
        loginD.getRootPane().setDefaultButton(loginB);
        
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout(FlowLayout.CENTER));
        p.add(loginB);
        p.add(Factory.createButton("Exit", this));
        
//         JPanel p5 = new JPanel();
//         p5.setBorder(new TitledBorder("Settings"));
//         p5.setLayout(new GridLayout(0,1));
//         p5.add(this.smtpHost);
//         p5.add(this.imapHost);
//         p5.add(this.smtpSSLport);
        
        JPanel p2 = new JPanel();
        p2.setLayout(new BorderLayout());
        p2.add(this.user, BorderLayout.NORTH);
        p2.add(this.pass, BorderLayout.SOUTH);
        p2.setBorder(new TitledBorder("Enter your Gmail login information:"));
        
        JPanel p4 = new JPanel();
        p4.add(new JLabel(), BorderLayout.NORTH);
        
        JPanel p3 = new JPanel();
        p3.setLayout(new BorderLayout());
        JLabel pic = new JLabel("");
        pic.setIcon(new Factory().createImageIcon(GMAIL_ICON));
        p3.add(pic, BorderLayout.CENTER);
        
        JPanel pp = new JPanel();
        pp.setLayout(new BorderLayout());
//         pp.add(p5, BorderLayout.NORTH);
        pp.add(p2, BorderLayout.CENTER);
//         loginD.add(p5, BorderLayout.NORTH);
        loginD.add(pp, BorderLayout.CENTER);
        loginD.add(p, BorderLayout.SOUTH);
        loginD.add(p3, BorderLayout.WEST);

        loginD.setSize(400, 230);
        loginD.setResizable(false);
//         loginD.pack();
        loginD.setLocation((int) (screenRect.getWidth() - loginD.getWidth()) / 2, (int) (screenRect.getHeight() - loginD.getHeight()) / 2);
        loginD.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        loginD.setVisible(true);
    }
    
    public void actionPerformed(ActionEvent ae)
    {
        try {
            String cmd = ae.getActionCommand();
            if (cmd.equals("Black Moon")) {
                setLookAndFeel("de.javasoft.plaf.synthetica.SyntheticaBlackMoonLookAndFeel");
                setStates(blackMoon);
            }else if (cmd.equals("Black Eye")) {
                setLookAndFeel("de.javasoft.plaf.synthetica.SyntheticaBlackEyeLookAndFeel");
                setStates(blackEye);
            }else if (cmd.equals("Blue Ice")) {
                setLookAndFeel("de.javasoft.plaf.synthetica.SyntheticaBlueIceLookAndFeel");
                setStates(blueIce);
            }else if (cmd.equals("Black Star")) {
                setLookAndFeel("de.javasoft.plaf.synthetica.SyntheticaBlackStarLookAndFeel");
                setStates(blackStar);
            }else if (cmd.equals("Blue Steel")) {
                setLookAndFeel("de.javasoft.plaf.synthetica.SyntheticaBlueSteelLookAndFeel");
                setStates(blueSteel);
            }else if (cmd.equals("Green Dream")) {
                setLookAndFeel("de.javasoft.plaf.synthetica.SyntheticaGreenDreamLookAndFeel");
                setStates(greenDream);
            }else if (cmd.equals("Silver Moon")) {
                setLookAndFeel("de.javasoft.plaf.synthetica.SyntheticaSilverMoonLookAndFeel");
                setStates(silverMoon);
            }else if (cmd.equals("Nimbus")) {
                setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
                setStates(nimbus);
            }else if (cmd.equals("Acryl")) {
                setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
                setStates(acryl);
            }else if (cmd.equals("Aero")) {
                setLookAndFeel("com.jtattoo.plaf.aero.AeroLookAndFeel");
                setStates(aero);
            }else if (cmd.equals("Aluminium")) {
                setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
                setStates(aluminium);
            }else if (cmd.equals("Bernstein")) {
                setLookAndFeel("com.jtattoo.plaf.bernstein.BernsteinLookAndFeel");
                setStates(bernstein);
            }else if (cmd.equals("Fast")) {
                setLookAndFeel("com.jtattoo.plaf.fast.FastLookAndFeel");
                setStates(fast);
            }else if (cmd.equals("Graphite")) {
                setLookAndFeel("com.jtattoo.plaf.graphite.GraphiteLookAndFeel");
                setStates(graphite);
            }else if (cmd.equals("HiFi")) {
                setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
                setStates(hifi);
            }else if (cmd.equals("Luna")) {
                setLookAndFeel("com.jtattoo.plaf.luna.LunaLookAndFeel");
                setStates(luna);
            }else if (cmd.equals("McWin")) {
                setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
                setStates(mcwin);
            }else if (cmd.equals("Mint")) {
                setLookAndFeel("com.jtattoo.plaf.mint.MintLookAndFeel");
                setStates(mint);
            }else if (cmd.equals("Noire")) {
                setLookAndFeel("com.jtattoo.plaf.noire.NoireLookAndFeel");
                setStates(noire);
            }else if (cmd.equals("Smart")) {
                setLookAndFeel("com.jtattoo.plaf.smart.SmartLookAndFeel");
                setStates(smart);
            }else if (cmd.equals("Quaqua")) {
                setLookAndFeel("ch.randelshofer.quaqua.QuaquaLookAndFeel");
                setStates(quaqua);
            }else if (cmd.equals("Tiger")) {
                setLookAndFeel("ch.randelshofer.quaqua.tiger.Quaqua15TigerCrossPlatformLookAndFeel");
                setStates(tiger);
            }else if (cmd.equals("Leopard")) {
                setLookAndFeel("ch.randelshofer.quaqua.leopard.Quaqua15LeopardCrossPlatformLookAndFeel");
                setStates(leopard);
            }else if (cmd.equals("Snow Leopard")) {
                setLookAndFeel("ch.randelshofer.quaqua.leopard.Quaqua15LeopardCrossPlatformLookAndFeel");
                setStates(snowLeopard);
            }else if (cmd.startsWith("Inbox")) {
                if (!fopening) {
                    fopening = true;
                    (new Thread() {
                        public void run()
                        {
                            openFolder(INBOX);
                        }
                    }).start();
                }
            }else if (cmd.startsWith("Sent")) {
                if (!fopening) {
                    fopening = true;
                    (new Thread() {
                        public void run()
                        {
                            openFolder(SENT_MAIL);
                            }
                    }).start();
                }
            }else if (cmd.startsWith("Starred")) {
                if (!fopening) {
                    fopening = true;
                    (new Thread() {
                        public void run()
                        {
                            openFolder(STARRED);
                            }
                    }).start();
                }
            }else if (cmd.startsWith("Drafts")) {
                if (!fopening) {
                    fopening = true;
                    (new Thread() {
                        public void run()
                        {
                            openFolder(DRAFTS);
                            }
                    }).start();
                }
            }else if (cmd.startsWith("Spam")) {
                if (!fopening) {
                    fopening = true;
                    (new Thread() {
                        public void run()
                        {
                            openFolder(SPAM);
                            }
                    }).start();
                }
            }else if (cmd.startsWith("Trash")) {
                if (!fopening) {
                    fopening = true;
                    (new Thread() {
                        public void run()
                        {
                            openFolder(TRASH);
                            }
                    }).start();
                }
            }else if (cmd.equalsIgnoreCase("sign in")) {
                loginB.setEnabled(false);
                user.setEditable(false);
                pass.setEditable(false);
//                 loginD.setVisible(false);
                showStatus("Logging in. Please wait...");
//                 infoD.setMessage("Connecting through IMAP. Please wait...", "Connecting");
//                 infoD.setVisible(true);
//                 loginD.setVisible(false);
                
//                 setCursor(new Cursor(Cursor.WAIT_CURSOR));
                (new Thread() {
                    public void run()
                    {
                        login();
                    }
                }).start();
//                 setVisible(true);
//                 infoD.setVisible(false);
//                 setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                showStatus("Logged in");
            }else if (cmd.equalsIgnoreCase("login")) {
                if (!loggedIn) {
                    showLogin();
                }
            }else if (cmd.equalsIgnoreCase("logout")) {
                showStatus("Logging out");
                logout();
            }else if (cmd.equalsIgnoreCase("exit")) {
                logout();
                System.exit(0);
            }else if (cmd.equalsIgnoreCase("older")) {
                if (fopening) {
                    return;
                }
                if (currentFrom > 0) {
                    fopening = true;
                    page--;
                    currentFrom = Math.max(currentFrom - MSG_PER_PAGE, 0);
                    currentTo = Math.min(currentFrom + MSG_PER_PAGE, gmail.getMessageCount());
                    (new Thread() {
                        public void run()
                        {
                            showFolderContents();
                        }
                    }).start();
                }
            }else if (cmd.equalsIgnoreCase("newer")) {
                if (fopening) {
                    return;
                }
                if (currentTo < gmail.getMessageCount()) {
                    fopening = true;
                    page++;
                    currentFrom = Math.min(currentFrom + MSG_PER_PAGE, gmail.getMessageCount());
                    currentTo = Math.min(currentFrom + MSG_PER_PAGE, gmail.getMessageCount());
                    (new Thread() {
                        public void run()
                        {
                            showFolderContents();
                        }
                    }).start();
                }
            }else if (cmd.startsWith("_")) {
                if (fopening) {
                    return;
                }
//                 refreshFolderDisplay();
                int msgNo = Integer.parseInt(cmd.substring(1, cmd.length()));
                showStatus("Opening message number " + msgNo + ". Please wait...");
                if (gmail.getFolderName().equalsIgnoreCase("drafts")) {
                    new MessageComposer(gmail.getMessage(msgNo), this);
                }else {
                    showMessageContents(msgNo);
                }
                for (int i = 0; i < msgBar.length; i++) {
                    if (ae.getSource().equals(msgBar[i].getReadButton())) {
                        msgBar[i].markRead();
                    }
                }
//                 msgBar[i].markRead();
                showStatus("Opened message number " + msgNo);
                refreshFolderDisplay();
            }else if (cmd.equalsIgnoreCase("compose new mail")) {
                new MessageComposer(this);
            }else if (cmd.equalsIgnoreCase("delete")) {
                if (fopening) {
                    return;
                }
                for (int i = 0; i < msgBar.length; i++) {
                    if (msgBar[i].isSelected()) {
                        int msgNo = msgBar[i].getMessageNumber();
                        gmail.delete(msgNo);
                        openFolder(gmail.getFolderName());
                    }
                }
                showStatus("Deleted selected message(s)");
                refreshFolder();
//                 refreshFolderDisplay();
            }else if (cmd.equalsIgnoreCase("add star")) {
                if (fopening) {
                    return;
                }
                boolean flag = false;
                for (int i = 0; i < msgBar.length; i++) {
                    if (msgBar[i].isSelected()) {
                        msgBar[i].setStarred();
                        int msgNo = msgBar[i].getMessageNumber();
                        gmail.setStarred(msgNo);
                        flag = true;
                    }
                }
                if (flag) {
                    showStatus("Applied changes to selected message(s)");
                    refreshFolderDisplay();
                }else {
                    showStatus("No messages selected");
                }
            }else if (cmd.equalsIgnoreCase("remove star")) {
                if (fopening) {
                    return;
                }
                boolean flag = false;
                for (int i = 0; i < msgBar.length; i++) {
                    if (msgBar[i].isSelected()) {
                        msgBar[i].setUnstarred();
                        int msgNo = msgBar[i].getMessageNumber();
                        gmail.setUnstarred(msgNo);
                        flag = true;
                    }
                }
                if (flag) {
                    showStatus("Applied changes to selected message(s)");
                    refreshFolderDisplay();
                }else {
                    showStatus("No messages selected");
                }
            }else if (cmd.equalsIgnoreCase("mark as read")) {
                if (fopening) {
                    return;
                }
                boolean flag = false;
                for (int i = 0; i < msgBar.length; i++) {
                    if (msgBar[i].isSelected()) {
                        msgBar[i].markRead();
                        int msgNo = msgBar[i].getMessageNumber();
                        gmail.markRead(msgNo);
                        flag = true;
                    }
                }
                if (flag) {
                    showStatus("Applied changes to selected message(s)");
                    refreshFolderDisplay();
                }else {
                    showStatus("No messages selected");
                }
            }else if (cmd.equalsIgnoreCase("mark as unread")) {
                if (fopening) {
                    return;
                }
                boolean flag = false;
                for (int i = 0; i < msgBar.length; i++) {
                    if (msgBar[i].isSelected()) {
                        msgBar[i].markUnread();
                        int msgNo = msgBar[i].getMessageNumber();
                        gmail.markUnread(msgNo);
                        flag = true;
                    }
                }
                if (flag) {
                    showStatus("Applied changes to selected message(s)");
                    refreshFolderDisplay();
                }else {
                    showStatus("No messages selected");
                }
            }else if (cmd.equalsIgnoreCase("all")) {
                if (fopening) {
                    return;
                }
                for (int i = 0; i < msgBar.length; i++) {
                    msgBar[i].setSelected(true);
                }
            }else if (cmd.equalsIgnoreCase("none")) {
                if (fopening) {
                    return;
                }
                for (int i = 0; i < msgBar.length; i++) {
                    msgBar[i].setSelected(false);
                }
            }else if (cmd.equalsIgnoreCase("read")) {
                if (fopening) {
                    return;
                }
                for (int i = 0; i < msgBar.length; i++) {
                    if (msgBar[i].isRead()) {
                        msgBar[i].setSelected(true);
                    }else {
                        msgBar[i].setSelected(false);
                    }
                }
            }else if (cmd.equalsIgnoreCase("unread")) {
                if (fopening) {
                    return;
                }
                for (int i = 0; i < msgBar.length; i++) {
                    if (msgBar[i].isRead()) {
                        msgBar[i].setSelected(false);
                    }else {
                        msgBar[i].setSelected(true);
                    }
                }
            }else if (cmd.equalsIgnoreCase("*")) {
                if (fopening) {
                    return;
                }
                for (int i = 0; i < msgBar.length; i++) {
                    if (msgBar[i].isStarred()) {
                        msgBar[i].setSelected(true);
                    }else {
                        msgBar[i].setSelected(false);
                    }
                }
            }else if (cmd.equalsIgnoreCase("unstarred")) {
                if (fopening) {
                    return;
                }
                for (int i = 0; i < msgBar.length; i++) {
                    if (msgBar[i].isStarred()) {
                        msgBar[i].setSelected(false);
                    }else {
                        msgBar[i].setSelected(true);
                    }
                }
            }else if (cmd.equalsIgnoreCase("about")) {
                showAboutDialog();
            }else if (cmd.equalsIgnoreCase("move to")) {
                if (fopening) {
                    return;
                }
                String s = (String) mc.getSelectedItem();
                boolean flag = false;
                if (s.equalsIgnoreCase("move to trash")) {
                    for (int i = 0; i < msgBar.length; i++) {
                        if (msgBar[i].isSelected()) {
                            int msgNo = msgBar[i].getMessageNumber();
                            gmail.moveToTrash(msgNo);
                            flag = true;
                            msgBar[i].setSelected(false);
                        }
                    }
                    if (flag) {
                        showStatus("Moved selected message(s) to Trash");
                        (new Thread() {
                            public void run()
                            {
                                refreshFolder();
                                fopening = false;
                            }
                        }).start();
                    }else {
                        showStatus("No messages selected");
                    }
                    
                }else if (s.equalsIgnoreCase("move to inbox")) {
                    for (int i = 0; i < msgBar.length; i++) {
                        if (msgBar[i].isSelected()) {
                            int msgNo = msgBar[i].getMessageNumber();
                            gmail.moveToInbox(msgNo);
                            flag = true;
                            msgBar[i].setSelected(false);
                        }
                    }
                    if (flag) {
                        showStatus("Moved selected message(s) to Inbox");
                        (new Thread() {
                            public void run()
                            {
                                refreshFolder();
                                fopening = false;
                            }
                        }).start();
                    }else {
                        showStatus("No messages selected");
                    }
                    
                }else if (s.equalsIgnoreCase("move to spam")) {
                    for (int i = 0; i < msgBar.length; i++) {
                        if (msgBar[i].isSelected()) {
                            int msgNo = msgBar[i].getMessageNumber();
                            gmail.moveToSpam(msgNo);
                            flag = true;
                            msgBar[i].setSelected(false);
                        }
                    }
                    if (flag) {
                        showStatus("Moved selected message(s) to Spam");
                        (new Thread() {
                            public void run()
                            {
                                refreshFolder();
                                fopening = false;
                            }
                        }).start();
                    }else {
                        showStatus("No messages selected");
                    }
                    
                }else if (s.equalsIgnoreCase("move to sent mail")) {
                    for (int i = 0; i < msgBar.length; i++) {
                        if (msgBar[i].isSelected()) {
                            int msgNo = msgBar[i].getMessageNumber();
                            gmail.moveToSent(msgNo);
                            flag = true;
                            msgBar[i].setSelected(false);
                        }
                    }
                    if (flag) {
                        showStatus("Moved selected message(s) to Sent Mail");
                        (new Thread() {
                            public void run()
                            {
                                refreshFolder();
                                fopening = false;
                            }
                        }).start();
                    }else {
                        showStatus("No messages selected");
                    }
                    
                }else if (s.equalsIgnoreCase("move to drafts")) {
                    for (int i = 0; i < msgBar.length; i++) {
                        if (msgBar[i].isSelected()) {
                            int msgNo = msgBar[i].getMessageNumber();
                            gmail.moveToDrafts(msgNo);
                            flag = true;
                            msgBar[i].setSelected(false);
                        }
                    }
                    if (flag) {
                        showStatus("Moved selected message(s) to Drafts");
                        (new Thread() {
                            public void run()
                            {
                                refreshFolder();
                                fopening = false;
                            }
                        }).start();
                    }else {
                        showStatus("No messages selected");
                    }
                    
                }else if (s.equalsIgnoreCase("delete selected")) {
                    int ec = JOptionPane.showConfirmDialog(this, "This will delete the selected messages permanetly. Proceed?", "Expunge", JOptionPane.YES_NO_OPTION);
                    if (ec == JOptionPane.YES_OPTION) {
                        for (int i = 0; i < msgBar.length; i++) {
                            if (msgBar[i].isSelected()) {
                                int msgNo = msgBar[i].getMessageNumber();
                                gmail.delete(msgNo);
                                flag = true;
                                msgBar[i].setSelected(false);
                            }
                            
                        }
                        if (flag) {
                            int n = gmail.expunge();
                            showStatus("Permanently cleared " + n + " selected message(s) from Trash");
                            (new Thread() {
                                public void run()
                                {
                                    refreshFolder();
                                    fopening = false;
                                }
                            }).start();
                        }else {
                            showStatus("No messages selected");
                        }
                        
                    }
                }else if (s.equalsIgnoreCase("empty trash")) {
                
                    int ec = JOptionPane.showConfirmDialog(this, "This will delete all messages permanetly from Trash. Proceed?", "Expunge", JOptionPane.YES_NO_OPTION);
                    if (ec == JOptionPane.YES_OPTION) {
                        for (int i = 0; i < msgBar.length; i++) {
                            int msgNo = msgBar[i].getMessageNumber();
                            gmail.delete(msgNo);
                            msgBar[i].setSelected(false);
                        }
                        int n = gmail.expunge();
                        showStatus("Permanently cleared " + n + " messages from Trash");
                    }
                    (new Thread() {
                        public void run()
                        {
                            refreshFolder();
                            fopening = false;
                        }
                    }).start();
                }
            }else if (cmd.equalsIgnoreCase("preferences")) {
                showPrefs();
            }else if (cmd.equalsIgnoreCase("manage")) {
                new AddressBook();
            }
        }catch (Exception e) {
            if (e instanceof AuthenticationFailedException) {
                showErrorDialog(AUTHENTICATION_FAILED_MSG);
                return;
            }
            if (debug) {
                showErrorDialog(e);
            }
//             e.printStackTrace();
        }
    }
    
    private void showPrefs()
    {
        
    }
    
    private void savePrefs()
    {
        
    }
    
    public void saveDraft(Message msg)
    {
        try {
            gmail.saveDraft(msg);
        }catch (Exception e) {
            showErrorDialog("Failed saving to Drafts", "Error");
        }
    }
    
    private void showMessageContents(int msgNum)
    {
        if (!msgBar[msgNum - currentFrom - 1].getPreview().isRead()) {
            String s = gmail.getFolderName().toLowerCase();
            if (s.endsWith("inbox")) {
                ic--;
            }else if (s.endsWith("starred")) {
                stc--;
            }else if (s.endsWith("spam")) {
                spc--;
            }else if (s.endsWith("trash")) {
                tc--;
            }else if (s.endsWith("drafts")) {
                dc--;
            }else if (s.endsWith("sent mail")) {
                smc--;
            }
        }
        msgBar[msgNum - currentFrom - 1].refresh();
        
        try {
            Message m = gmail.getMessage(msgNum);
            
            // check for encryption
//             String isEnc = this.msg.getHeader(ENC_HEADER_NAME);
//             if (isEnc != null && isEnc.equals(ENC_HEADER_VALUE)) {
//                 decrypt
            
            if (gmail.getFolderName().toLowerCase().endsWith(Factory.getFolderName(DRAFTS))) {
                MessageComposer mc = new MessageComposer(m, this);
            }else {
                if (m.getHeader(ENC_HEADER_NAME) != null) {
//                     System.out.println("*** This message is encoded ***");
                    new DecDialog(m, this);
                }else {
                    MessageViewer mview = new MessageViewer(m, this);
                }
            }
        }catch (Exception e) {
            if (debug) {
                showErrorDialog(e);
//                 e.printStackTrace();
            }
        }
    }
    
    String getMyAddress()
    {
        return gmail.getUsername();
    }
    
    private void setStates(JCheckBoxMenuItem m)
    {
        showStatus("Current look and feel: " + m.getText());
        if (!m.equals(blackMoon)) {
            blackMoon.setSelected(false);
        }
        if (!m.equals(blackEye)) {
            blackEye.setSelected(false);
        }
        if (!m.equals(blueIce)) {
            blueIce.setSelected(false);
        }
        if (!m.equals(blackStar)) {
            blackStar.setSelected(false);
        }
        if (!m.equals(blueSteel)) {
            blueSteel.setSelected(false);
        }
        if (!m.equals(greenDream)) {
            greenDream.setSelected(false);
        }
        if (!m.equals(silverMoon)) {
            silverMoon.setSelected(false);
        }
        if (!m.equals(nimbus)) {
            nimbus.setSelected(false);
        }
        if (!m.equals(acryl)) {
            acryl.setSelected(false);
        }
        if (!m.equals(aero)) {
            aero.setSelected(false);
        }
        if (!m.equals(aluminium)) {
            aluminium.setSelected(false);
        }
        if (!m.equals(bernstein)) {
            bernstein.setSelected(false);
        }
        if (!m.equals(fast)) {
            fast.setSelected(false);
        }
        if (!m.equals(graphite)) {
            graphite.setSelected(false);
        }
        if (!m.equals(hifi)) {
            hifi.setSelected(false);
        }
        if (!m.equals(luna)) {
            luna.setSelected(false);
        }
        if (!m.equals(mcwin)) {
            mcwin.setSelected(false);
        }
        if (!m.equals(mint)) {
            mint.setSelected(false);
        }
        if (!m.equals(noire)) {
            noire.setSelected(false);
        }
        if (!m.equals(smart)) {
            smart.setSelected(false);
        }
        if (!m.equals(quaqua)) {
            quaqua.setSelected(false);
        }
        if (!m.equals(tiger)) {
            tiger.setSelected(false);
        }
        if (!m.equals(leopard)) {
            leopard.setSelected(false);
        }
        if (!m.equals(snowLeopard)) {
            snowLeopard.setSelected(false);
        }
    }
    
    private void setLookAndFeel(String laf)
    {
        try {
            UIManager.setLookAndFeel(laf);
            SwingUtilities.updateComponentTreeUI(this);
            exportLAF(laf);
            for (int i = 0; i < msgBar.length; i++) {
                msgBar[i].refresh();
            }
        }catch (Exception e) {
            showStatus("Error: " + e);
            if (debug) {
                showErrorDialog(e);
            }
//             System.err.println(e);
        }
    }
    
    private void showStatus(String stext)
    {
        if (status != null) {
            status.setText(stext);
        }
    }
    
    private void refresh() throws Exception
    {
        gmail.openFolder(INBOX);
        int n = gmail.getUnreadMessageCount();
        if (n != 0) {
            inbox.setText("Inbox (" + n + ")");
        }
        gmail.closeFolder();
        
        gmail.openFolder(SPAM);
        n = gmail.getUnreadMessageCount();
        if (n != 0) {
            spam.setText("Spam (" + n + ")");
        }
        gmail.closeFolder();
    }
    
    private void showFolderContents()
    {
        try {
            fc.removeAll();
            dispInfo.setText("Retrieving messages. Please wait...");
            MessagePreview preview[] = new MessagePreview[currentTo - currentFrom];
            msgBar = null;
            msgBar = new MessageBar[preview.length];
            for (int i = msgBar.length - 1; i >= 0; i--) {
                preview[i] = gmail.getMessagePreview(currentFrom + i);
                msgBar[i] = new MessageBar(preview[i], currentFrom + i + 1, this);
                fc.add(msgBar[i]);
                fc.validate();
                validate();
//                 repaint();
            }
            fc.validate();
//             fc.repaint();
            if (gmail.getMessageCount() > 0) {
                dispInfo.setText("Displaying messages " + (currentFrom + 1) + " to " + currentTo + " of " + gmail.getMessageCount());
            }else {
                dispInfo.setText("Folder is empty. No messages to display.");
            }
            fopening = false;
            
        }catch (Exception e) {
//             e.printStackTrace();
            showStatus("Error: Cannot display folder contents");
            e.printStackTrace();
            if (e instanceof AuthenticationFailedException)
            {
                showErrorDialog(AUTHENTICATION_FAILED_MSG);
                return;
            }
            if (debug) {
                showErrorDialog(e);
            }
//             e.printStackTrace();
        }
    }
    
    private void login()
    {
        if (gmail != null && !loggedIn) {
            gmail.setUserPass(this.user.getText().trim(), new String(this.pass.getPassword()).trim());
            smtp.setUserPass(this.user.getText().trim(), new String(this.pass.getPassword()).trim());
            try {
                gmail.connect();
                now = System.currentTimeMillis();
//                 initFolderDisplay();
//                 gmail.closeFolder();
                loginD.setVisible(false);
                this.setVisible(true);
//                 refresh();
//                 System.out.println(gmail.getMessageCount());
//                 inbox.setText("Inbox (" + gmail.getMessageCount() + ")");

                this.welcome.setText("<html>Welcome <b>" + gmail.getUsername() + "&nbsp&nbsp&nbsp&nbsp</b></html>");
                showStatus("Logged in");
                loggedIn = true;
                login.setEnabled(false);
                logout.setEnabled(true);
                sLogout.setEnabled(true);
//                 infoD.setMessage("Retreiving messages", "Connected");
                openFolder(INBOX);
                
                refreshFolderDisplay();
                
                checkNew = true;
//                 CheckNewThread check = new CheckNewThread();
//                 check.start();

//                 showFolderContents();
            }catch (Exception e) {
                loginB.setEnabled(true);
                user.setEditable(true);
                pass.setEditable(true);
                if (e instanceof AuthenticationFailedException)
                {
                    showErrorDialog(AUTHENTICATION_FAILED_MSG);
                    return;
                }
//                 e.printStackTrace();
//                 System.out.println(e);
                showStatus("Login failed");
                if (debug) {
                    showErrorDialog(e);
                }
            }
        }
//         new jmail.chat.Demo();
    }
    
    private void logout()
    {
        if (gmail != null && loggedIn) {
            try {
                checkNew = false;
                gmail.disconnect();
                inbox.setText("Inbox");
                inbox.setFont(UNSEL_FONT);
                starred.setFont(UNSEL_FONT);
                drafts.setFont(UNSEL_FONT);
                spam.setFont(UNSEL_FONT);
                trash.setFont(UNSEL_FONT);
                sentmail.setFont(UNSEL_FONT);
                fc.removeAll();
                showStatus("Logged out");
                dispInfo.setText("");
                loggedIn = false;
                login.setEnabled(true);
                logout.setEnabled(false);
                sLogout.setEnabled(false);
                setVisible(false);
                showLogin();
            }catch (Exception e) {
                showStatus("Logout failed");
                if (debug) {
                    showErrorDialog(e);
                }
            }
        }
    }
    
    void refreshFolder()
    {
        try {
            if (!fopening) {
                openFolder(gmail.getFullFolderName());
            }
        }catch (Exception e) {
            if (debug) {
                showErrorDialog("An error occurred: " + e);
//                 e.printStackTrace();
            }
        }
    }
    
    boolean draftsOpen()
    {
        return gmail.getFolderName().equalsIgnoreCase("drafts");
    }
    
    private void openFolder(String folder)
    {
        try {
            fopening = true;
            
            mc.removeAllItems();
            if (folder.equals(INBOX)) {
                mc.addItem("Move to Spam");
                mc.addItem("Move to Trash");
            }else if (folder.equals(DRAFTS)) {
                mc.addItem("Move to Trash");
            }else if (folder.equals(STARRED)) {
                mc.addItem("Move to Trash");
            }else if (folder.equals(SPAM)) {
                mc.addItem("Move to Inbox");
                mc.addItem("Move to Trash");
            }else if (folder.equals(SENT_MAIL)) {
                mc.addItem("Move to Trash");
            }else if (folder.equals(TRASH)) {
                mc.addItem("Move to Inbox");
                mc.addItem("Move to Drafts");
                mc.addItem("Move to Sent Mail");
                mc.addItem("Move to Spam");
                mc.addItem("Empty Trash");
                mc.addItem("Delete selected");
            }
            
            
            showStatus("Opening " + Factory.getFolderName(folder) + ". Please wait...");
            dispInfo.setText("");
            if (gmail != null && loggedIn) {
                gmail.closeFolder();
                gmail.openFolder(folder);
                numPages = gmail.getMessageCount() / MSG_PER_PAGE;
                currentFrom = Math.max(gmail.getMessageCount() - MSG_PER_PAGE, 0);//0
                currentTo = Math.min(currentFrom + MSG_PER_PAGE, gmail.getMessageCount());
                
                inbox.setFont(UNSEL_FONT);
                starred.setFont(UNSEL_FONT);
                drafts.setFont(UNSEL_FONT);
                spam.setFont(UNSEL_FONT);
                trash.setFont(UNSEL_FONT);
                sentmail.setFont(UNSEL_FONT);
                
                Color ffg = new JButton().getForeground();
                inbox.setForeground(ffg);
                starred.setForeground(ffg);
                drafts.setForeground(ffg);
                spam.setForeground(ffg);
                trash.setForeground(ffg);
                sentmail.setForeground(ffg);
                
                if (folder.equalsIgnoreCase(INBOX)) {
                    inbox.setFont(SEL_FONT);
                    inbox.setForeground(SEL_FG);
                }else if (folder.equalsIgnoreCase(SENT_MAIL)) {
                    sentmail.setFont(SEL_FONT);
                    sentmail.setForeground(SEL_FG);
                }else if (folder.equalsIgnoreCase(STARRED)) {
                    starred.setFont(SEL_FONT);
                    starred.setForeground(SEL_FG);
                }else if (folder.equalsIgnoreCase(DRAFTS)) {
                    drafts.setFont(SEL_FONT);
                    drafts.setForeground(SEL_FG);
                }else if (folder.equalsIgnoreCase(SPAM)) {
                    spam.setFont(SEL_FONT);
                    spam.setForeground(SEL_FG);
                }else if (folder.equalsIgnoreCase(TRASH)) {
                    trash.setFont(SEL_FONT);
                    trash.setForeground(SEL_FG);
                }
                
                showFolderContents();
            }
            showStatus("Opened " + Factory.getFolderName(folder));
//             fopening = false;
        }catch (Exception e) {
            if (debug) {
                showErrorDialog("An error occurred: " + e);
//                 e.printStackTrace();
            }
        }
    }
    
    public static void main(String args[])
    {
        if (running) {
            return;
        }
//         String laf = importLAF();
        try {
//             if (laf != null && laf.length() != 0) {
//                 UIManager.setLookAndFeel("ch.randelshofer.quaqua.tiger.Quaqua15TigerCrossPlatformLookAndFeel");
//                 UIManager.setLookAndFeel("com.jtattoo.plaf.mint.MintLookAndFeel");
//                 UIManager.setLookAndFeel("com.jtattoo.plaf.luna.LunaLookAndFeel");
//                 UIManager.setLookAndFeel("com.jtattoo.plaf.fast.FastLookAndFeel");
//                 UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
//                 UIManager.setLookAndFeel("com.jtattoo.plaf.aero.AeroLookAndFeel");
                UIManager.setLookAndFeel(LAF);
//                 UIManager.getSystemLookAndFeelClassName());//laf);
//             }
        }catch (Exception e) {
            
        }
        Main _main = new Main();
//         _main.initLAF(laf);
    }
    
    private static String importLAF()
    {
        try {
            File f = new File(LAF_FILE);
            if (!f.exists()) {
                return DEFAULT_LAF;
            }else {
                BufferedReader bin = new BufferedReader(new FileReader(f));
                String laf = bin.readLine();
                bin.close();
                return laf;
            }
        }catch (Exception e) {
            return null;
        }
    }
    
    private void initLAF(String laf)
    {
        if (laf.equals("de.javasoft.plaf.synthetica.SyntheticaBlackMoonLookAndFeel")) {
            blackMoon.setSelected(true);
        }else if (laf.equals("de.javasoft.plaf.synthetica.SyntheticaBlackEyeLookAndFeel")) {
            blackEye.setSelected(true);
        }else if (laf.equals("de.javasoft.plaf.synthetica.SyntheticaBlueIceLookAndFeel")) {
            blueIce.setSelected(true);
        }else if (laf.equals("de.javasoft.plaf.synthetica.SyntheticaBlackStarLookAndFeel")) {
            blackStar.setSelected(true);
        }else if (laf.equals("de.javasoft.plaf.synthetica.SyntheticaBlueSteelLookAndFeel")) {
            blueSteel.setSelected(true);
        }else if (laf.equals("de.javasoft.plaf.synthetica.SyntheticaGreenDreamLookAndFeel")) {
            greenDream.setSelected(true);
        }else if (laf.equals("de.javasoft.plaf.synthetica.SyntheticaSilverMoonLookAndFeel")) {
            silverMoon.setSelected(true);
        }else if (laf.equals("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel")) {
            nimbus.setSelected(true);
        }else if (laf.equals("com.jtattoo.plaf.acryl.AcrylLookAndFeel")) {
            acryl.setSelected(true);
        }else if (laf.equals("com.jtattoo.plaf.aero.AeroLookAndFeel")) {
            aero.setSelected(true);
        }else if (laf.equals("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel")) {
            aluminium.setSelected(true);
        }else if (laf.equals("com.jtattoo.plaf.bernstein.BernsteinLookAndFeel")) {
            bernstein.setSelected(true);
        }else if (laf.equals("com.jtattoo.plaf.fast.FastLookAndFeel")) {
            fast.setSelected(true);
        }else if (laf.equals("com.jtattoo.plaf.graphite.GraphiteLookAndFeel")) {
            graphite.setSelected(true);
        }else if (laf.equals("com.jtattoo.plaf.hifi.HiFiLookAndFeel")) {
            hifi.setSelected(true);
        }else if (laf.equals("com.jtattoo.plaf.luna.LunaLookAndFeel")) {
            luna.setSelected(true);
        }else if (laf.equals("com.jtattoo.plaf.mcwin.McWinLookAndFeel")) {
            mcwin.setSelected(true);
        }else if (laf.equals("com.jtattoo.plaf.mint.MintLookAndFeel")) {
            mint.setSelected(true);
        }else if (laf.equals("com.jtattoo.plaf.noire.NoireLookAndFeel")) {
            noire.setSelected(true);
        }else if (laf.equals("com.jtattoo.plaf.smart.SmartLookAndFeel")) {
            smart.setSelected(true);
        }else if (laf.equals("ch.randelshofer.quaqua.QuaquaLookAndFeel")) {
            quaqua.setSelected(true);
        }else if (laf.equals("ch.randelshofer.quaqua.tiger.Quaqua15TigerCrossPlatformLookAndFeel")) {
            tiger.setSelected(true);
        }else if (laf.equals("ch.randelshofer.quaqua.leopard.Quaqua15LeopardCrossPlatformLookAndFeel")) {
            leopard.setSelected(true);
        }else if (laf.equals("ch.randelshofer.quaqua.leopard.Quaqua15LeopardCrossPlatformLookAndFeel")) {
            snowLeopard.setSelected(true);
        }
    }
    
    private void exportLAF(String cname)
    {
        try {
            File f = new File(LAF_FILE);
            PrintWriter pout = new PrintWriter(f);
            pout.println(cname);
            pout.close();
        }catch (Exception e) {
            showErrorDialog("Failed to export look and feel");
        }
    }
    
    private void showNewMailPopup()
    {
        JButton jb;
        JLabel lab;
        JDialog dd = new JDialog(this, "New Mail Notification");
        dd.setLayout(new BorderLayout());
        dd.add(new JLabel(new Factory().createImageIcon(NEW_MAIL_ICON)), BorderLayout.WEST);
        dd.add(lab = new JLabel("<html><p align=\"center\"><font size=5>You've got new mail!&nbsp;&nbsp;&nbsp;</font></p></html>", JLabel.CENTER),
          BorderLayout.CENTER);
        lab.setForeground(SEL_FG);
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout(FlowLayout.CENTER));
//         p.add(jb = Factory.createButton("Ok", this));
        dd.add(p, BorderLayout.SOUTH);
        dd.pack();
        dd.setResizable(false);
        dd.setLocationRelativeTo(null);
        dd.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dd.setVisible(true);
    }
    
    public void iconLeftClicked(SysTrayMenuEvent evt)
    {
        if (isVisible()) {
            setVisible(false);
        }else if (loggedIn) {
            setVisible(true);
        }
    }
    
    public void iconLeftDoubleClicked(SysTrayMenuEvent evt)
    {
        
    }
    
    public void windowActivated(WindowEvent we)
    {
        
    }
    
    public void windowClosed(WindowEvent we)
    {
        
    }
    
    public void windowClosing(WindowEvent we)
    {
        setVisible(false);
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
    
    public void menuItemSelected(SysTrayMenuEvent evt)
    {
        String cmd = evt.getActionCommand();
        if (cmd.equalsIgnoreCase("exit")) {
            logout();
            System.exit(0);
        }else if (cmd.equalsIgnoreCase("logout")) {
            logout();
        }else if (cmd.equalsIgnoreCase("about")) {
            showAboutDialog();
        }
    }
 
    public class PopupAuthenticator extends Authenticator {
     
      public PasswordAuthentication getPasswordAuthentication() {
        String username, password;
     
        String result = JOptionPane.showInputDialog(
          "Enter 'username,password'");
     
        StringTokenizer st = new StringTokenizer(result, ",");
        username = st.nextToken();
        password = st.nextToken();
     
        return new PasswordAuthentication(username, password);
      }
     
    }
    
    private class CheckNewThread extends Thread
    {
        public void run()
        {
            while (true) {
                try {
                    Date d = new Date(now);
//                     System.out.println("checking for new messages" + gmail.getNewMessageCount());
                    if (checkNew && loggedIn && gmail.hasNewMessages(d)) {
                        now = System.currentTimeMillis();
                        showNewMailPopup();
                        if (gmail.getFolderName().equalsIgnoreCase("inbox")) {
                            refreshFolder();
                        }
                    }
                    Thread.sleep(NOTIFICATION_INTERVAL);
                }catch (Exception e) {
                    
                }
            }
        }
    }
}

    