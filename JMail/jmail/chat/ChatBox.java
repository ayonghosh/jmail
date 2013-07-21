package jmail.chat;


import org.jivesoftware.smack.*;
import org.jivesoftware.smackx.*;
import org.jivesoftware.smack.packet.*;
import jmail.*;
import jmail.secure.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.awt.image.*;
import snoozesoft.systray4j.*;
// import java.text.*;


public final class ChatBox extends JFrame implements Defaults, ActionListener, KeyListener, MouseListener, SysTrayMenuListener, WindowListener//, Runnable
{
    private static final Color modeBG = new Color(255, 250, 205);
    private static final String BULLET_CHAR = "+";
    private static final String TITLE_BG_IMG = "144_preview.png";//"compaq_blue.jpg";//"Blue-Sky.jpg";//"blue_background.jpg";
    private static final Color [] carr = {Color.BLACK, Color.GRAY, Color.GREEN, Color.CYAN, Color.ORANGE, Color.YELLOW, Color.BLUE, Color.BLUE};
    private static final String SYSTRAY_ICON = "chat_blank.ico";//"chat_2.ico";//"chat_blank.ico";
    private static final String JTALK_ICON = "chat-blank-64x64.png";
    private static final Color STATUS_FG = new Color(49, 79, 79);
    private static final String LOGIN_ICON = "Chat-icon.png";
    private static final String TITLE_ICON = "Android-Gtalk-128.png";
    
    private JCheckBoxMenuItem blackMoon, blackEye, blueIce, blackStar, blueSteel, greenDream, silverMoon, swing, system;
    private JCheckBoxMenuItem nimbus;
    private JCheckBoxMenuItem acryl, aero, aluminium, bernstein, fast, graphite, hifi, luna, mcwin, mint, noire, smart;
    private JCheckBoxMenuItem quaqua, tiger, leopard, snowLeopard;
    private JMenuBar mbar;
    
    private Buddy[] friends;
    private XMPPConnection conn;
    private Roster roster;
    private boolean exiting;
    private JScrollPane scroller;
    private JScrollPane log;
    private JPanel pane;
//     private JLabel statusBar;
//     private JPanel statusBar;
    private JTextArea statusBar;
    private JLabel tLab;
    
    private JTextField user;
    private JPasswordField pass;
    private JTextField host;
    private JTextField port;
    private JTextField rsrc;
    private ChatBox selfRef;
    private JCheckBox comp;
    private JCheckBox enc;
    private JCheckBox sasl;
    private JComboBox saslM;
    private JComboBox sec;
    private JComboBox myMode;
    private JTextField myStatus;
    private JComboBox services;
    
    private JDialog loginD;
    private JDialog about;
    private SysTrayMenuItem sLogout;
    
    private boolean statusSet = false;
    private boolean loggedIn = false;
    
    private int olCount;
    
//     private Vector<Presence> logV;
//     private Thread t;
    
    public ChatBox()
    {
//         super("JTalk");

//         setUndecorated(true);
//         setLookAndFeel("de.javasoft.plaf.synthetica.SyntheticaBlackMoonLookAndFeel");
        setTitle("JTalk");
//         setUndecorated(false);
        selfRef = this;
        
        
//         JFrame f = new JFrame();
//         f.setVisible(true);
//         f.setVisible(false);
//         setUndecorated(true);
        setIconImage(new Factory().createImageIcon(JTALK_ICON).getImage());
        
        
        
        tLab = new JLabel(new Factory().createImageIcon(TITLE_ICON), SwingConstants.LEFT) {
            public void paint(Graphics g)
            {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color colorA = Color.BLACK;
                Color colorB = Color.GRAY;
                g2d.setPaint(new GradientPaint(0, 0, colorA, getWidth(), getHeight(), colorB));
                g2d.setClip(0, 0, getWidth(), getHeight() - 5);
                g2d.fillRect(0, 0, getWidth(), getHeight() - 5);
//                 g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OVER, 0.5f));
                Image img = new Factory().createImageIcon(TITLE_BG_IMG).getImage();
                g2d.drawImage(img, 0, 0, null);
//                 g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OVER, 1.0f));
                super.paint(g);
            }
        };
        tLab.setText("<html><p><font size=6 color=#00B3FF>Welcome to JTalk v1.0</font></p>" + 
            "<p><font size=3 color=#157DEC><b>A secure XMPP (Jabber) </font><font color=#157DEC>&nbsp;chat client</b></font></p></html>");
        tLab.addMouseListener(this);
            
//         statusBar = new JLabel("Welcome to JTalk");
        statusBar = new JTextArea(5, 0) {
            public void paint(Graphics g)
            {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                super.paint(g2d);
            }
        };
        statusBar.setText("Welcome to JTalk v1.0 BETA");
        statusBar.setEditable(false);
        statusBar.setLineWrap(true);
        statusBar.setWrapStyleWord(true);
        statusBar.setForeground(STATUS_FG);
//         statusBar.setForeground(Color.DARK_GRAY);
        
        this.myMode = new JComboBox(new String [] {"<html><font color=#41A317<b>Available</b></font></html>", 
            "<html><font color=#F87217><b>Away</b></font></html>", 
            "<html><font color=#C11B17><b>Do Not Disturb</b></font></html>"//, 
//             "<html><font color=#736F6E><b>Invisible</b></font></html>"
        });
        this.myMode.setBackground(modeBG);
        this.myMode.addActionListener(this);
//         this.myMode.setBorder(new TitledBorder("My Mode"));
        this.myStatus = new JTextField("<Set your status message here>");
        this.myStatus.addKeyListener(this);
        this.myStatus.setForeground(Color.DARK_GRAY);
        
        
        
//         this.mbar = new JMenuBar();
//         JMenu file, edit, view, con, help;
// //         mbar.add(file = Factory.createMenu("Action", this));
// //         mbar.add(con = Factory.createMenu("Contacts", this));
// //             mbar.add(edit = Factory.createMenu("Edit", this));
//         mbar.add(view = Factory.createMenu("Skin", this));
// //         mbar.add(help = Factory.createMenu("Help", this));
// //         login = Factory.createMenuItem("Login", this);
// //         file.add(logout = Factory.createMenuItem("Logout", this));
// //         logout.setEnabled(false);
// //         file.add(Factory.createMenuItem("Exit", this));
//         
// //             edit.add(Factory.createMenuItem("Preferences", this));
//         
// //         con.add(Factory.createMenuItem("Manage", this));
// 
//         blackMoon   = Factory.createCheckBoxMenuItem("Black Moon", this);
// //         blackEye    = Factory.createCheckBoxMenuItem("Black Eye", this);
//         blueIce     = Factory.createCheckBoxMenuItem("Blue Ice", this);
//         blackStar   = Factory.createCheckBoxMenuItem("Black Star", this);
//         blueSteel   = Factory.createCheckBoxMenuItem("Blue Steel", this);
//         greenDream  = Factory.createCheckBoxMenuItem("Green Dream", this);
//         silverMoon  = Factory.createCheckBoxMenuItem("Silver Moon", this);
//         
//         swing       = Factory.createCheckBoxMenuItem("Swing", this);
//         system      = Factory.createCheckBoxMenuItem("System", this);
//         nimbus      = Factory.createCheckBoxMenuItem("Nimbus", this);
//         
//         acryl      = Factory.createCheckBoxMenuItem("Acryl", this);
//         aero       = Factory.createCheckBoxMenuItem("Aero", this);
//         aluminium  = Factory.createCheckBoxMenuItem("Aluminium", this);
//         bernstein  = Factory.createCheckBoxMenuItem("Bernstein", this);
//         fast       = Factory.createCheckBoxMenuItem("Fast", this);
//         graphite   = Factory.createCheckBoxMenuItem("Graphite", this);
// //         hifi       = Factory.createCheckBoxMenuItem("HiFi", this);
//         luna       = Factory.createCheckBoxMenuItem("Luna", this);
//         mcwin      = Factory.createCheckBoxMenuItem("McWin", this);
//         mint       = Factory.createCheckBoxMenuItem("Mint", this);
// //         noire      = Factory.createCheckBoxMenuItem("Noire", this);
//         smart      = Factory.createCheckBoxMenuItem("Smart", this);
//         
//         quaqua     = Factory.createCheckBoxMenuItem("Quaqua", this);
//         tiger      = Factory.createCheckBoxMenuItem("Tiger", this);
//         leopard    = Factory.createCheckBoxMenuItem("Leopard", this);
//         snowLeopard = Factory.createCheckBoxMenuItem("Snow Leopard", this);
//         
//         JMenu synth = Factory.createMenu("Synthetica", this);
//         view.add(synth);
//         
//         synth.add(blackMoon);
//         synth.add(blackStar);
// //         synth.add(blackEye);
//         synth.add(blueIce);
//         synth.add(blueSteel);
//         synth.add(greenDream);
//         synth.add(silverMoon);
//         view.add(synth);
// 
//         JMenu jt = Factory.createMenu("JTattoo", this);
//         jt.add(acryl);
//         jt.add(aero);
//         jt.add(aluminium);
//         jt.add(bernstein);
//         jt.add(fast);
//         jt.add(graphite);
// //         jt.add(hifi);
//         jt.add(luna);
//         jt.add(mcwin);
//         jt.add(mint);
// //         jt.add(noire);
//         jt.add(smart);
//         view.add(jt);
//         
//         JMenu java = Factory.createMenu("Java", this);
//         java.add(nimbus);
//         view.add(java);
//         
//         JMenu qq = Factory.createMenu("Quaqua", this);
//         qq.add(quaqua);
//         qq.add(tiger);
//         qq.add(leopard);
//         qq.add(snowLeopard);
//         view.add(qq);
        
        
        
        // implementing system tray
        SysTrayMenuIcon sysIcon = new SysTrayMenuIcon(new Factory().getIconPath(SYSTRAY_ICON));
        SysTrayMenu sMenu = new SysTrayMenu(sysIcon);
        sMenu.setIcon(sysIcon);
        sMenu.setToolTip("IntelliSol jTalk v1.0 BETA");
        sysIcon.addSysTrayMenuListener(this);
        
        SysTrayMenuItem sExit = Factory.createSysTrayMenuItem("Exit", this);
        this.sLogout = Factory.createSysTrayMenuItem("Logout", this);
        this.sLogout.setEnabled(false);
        SysTrayMenuItem sAbt = Factory.createSysTrayMenuItem("About", this);
        
        sMenu.addItem(sExit);
        sMenu.addSeparator();
        sMenu.addItem(sLogout);
        sMenu.addItem(sAbt);
        
        addWindowListener(this);
        
//         this.t = new Thread(this);
//         this.t.start();

//         help.add(Factory.createMenuItem("About", this));
//             help.add(Factory.createMenuItem("System Info", this));
//         add(mbar, BorderLayout.NORTH);
        
//         this.myStatus.setBorder(new TitledBorder("My Status"));
//         p4.add(new JLabel());
        
//         statusBar = new JPanel();
//         statusBar.add(new JLabel("Welcome to JTalk v1.0 BETA"));
        
        setResizable(false);
        setDefaultCloseOperation(HIDE_ON_CLOSE);//ISPOSE_ON_CLOSE);
        showLogin();
    }
    
//     public void paintComponent(Graphics g)
//     {
// //         super.paint(g);
//         g.setColor(Color.BLACK);
//         g.fillRect(0, 0, 1000, 1000);
//     }
    
//     public void run()
//     {
//         while (true) {
// //             System.out.println("*");
//             // consider synchronization in future
//             
//             if (this.logV != null) {
//                 while (!this.logV.isEmpty()) {
//                     System.out.println("*");
//                     Presence presence = this.logV.remove(this.logV.size() - 1);
//                     String from = presence.getFrom();
//                     String nom = "";
//                     String type = "";
//                     String mod = "";
//                     for (int i = 0; i < friends.length; i++) {
//                         nom = friends[i].button.getText();
//                         if (from.startsWith(friends[i].button.getActionCommand())) {
//                             type = presence.getType().toString();
//                             mod = presence.getMode().toString();
//                             break;
//                         }
//                     }
//                     
//                     statusBar.setText(statusBar.getText() + "\n " + BULLET_CHAR + " " + new Date() + " " + nom + " (" + from + ") is " + type + " (" + mod + ")");
//                     statusBar.setCaretPosition(statusBar.getText().length());
//                 }
//                 try {
//                     refresh();
//                 }catch (Exception e) {}
//             }
//         }
//     }
    
    private void setLookAndFeel(String laf)
    {
        try {
            UIManager.setLookAndFeel(laf);
            SwingUtilities.updateComponentTreeUI(this);
            this.validate();
            if (friends != null) {
                for (int i = 0; i < friends.length; i++) {
                    if (friends[i].cwin != null) {
                        friends[i].cwin.validate();
                    }
                }
            }
        }catch (Exception e) {}
    }
    
    private void initBuddyList()
    {
        olCount = 0;
        roster = null;
        roster = conn.getRoster();
        roster.addRosterListener(new RosterListener() {
            // Ignored events 
            public void entriesAdded(Collection<String> addresses) {}
            
            public void entriesDeleted(Collection<String> addresses) {}
            
            public void entriesUpdated(Collection<String> addresses) {}
            
            public void presenceChanged(Presence presence)
            {
//                 final Presence presence = presc;
//                 (new Thread() {
//                     public synchronized void run() {
//                         String from = presence.getFrom();
//                         String nom = "";
//                         String type = "";
//                         String mod = "";
//                         for (int i = 0; i < friends.length; i++) {
//                             nom = friends[i].button.getText();
//                             if (from.startsWith(friends[i].button.getActionCommand())) {
//                                 type = presence.getType().toString();
//                                 mod = presence.getMode().toString();
//                                 break;
//                             }
//                         }
//                         
//                         statusBar.setText(statusBar.getText() + "\n " + BULLET_CHAR + " " + new Date() + " " + nom + " (" + from + ") is " + type + " (" + mod + ")");
//                     }
//                 }).start();
                
//                    logV.add(presence);
//                    System.out.println(presence);
                   
                statusBar.setText(statusBar.getText() + "\n " + BULLET_CHAR + " " + new Date() + " " + presence.getFrom() + ": " + presence);
                statusBar.setCaretPosition(statusBar.getText().length());
                refresh();
            }
        });
        
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(0, 1));
        
        Collection<RosterEntry> entries = roster.getEntries();
//         int n = entries.size();
//         System.out.println(entries.size());
        friends = null;
        friends = new Buddy[entries.size()];
        
        int i = 0;
        for (RosterEntry entry : entries) {
            String name = entry.getName();
            if (name == null || name.length() == 0) {
                name = entry.toString();
            }
            
            friends[i] = new Buddy();
            friends[i].button = new JButton(name) {
                public void paint(Graphics g)
                {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    super.paint(g2d);
                }
            };
            friends[i].button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            friends[i].button.setBorderPainted(false);
            friends[i].button.setHorizontalAlignment(SwingConstants.LEFT);
            friends[i].button.setIcon(new Factory().createImageIcon(CHAT_OFFLINE_ICON));
            
            Presence pr = roster.getPresence(entry.getUser());
            String status = "Offline";
            if (pr.getType().equals(Presence.Type.available)) {
                olCount++;
                status = "Available";
                friends[i].button.setIcon(new Factory().createImageIcon(CHAT_ONLINE_ICON));
            }
            
            if (pr.getMode() != null) {    
                if (pr.getMode().equals(Presence.Mode.dnd)) {
                    status = "Do Not Disturb";
                    friends[i].button.setIcon(new Factory().createImageIcon(CHAT_BUSY_ICON));
                }else if (pr.getMode().equals(Presence.Mode.away)) {
                    status = "Away";
                    friends[i].button.setIcon(new Factory().createImageIcon(CHAT_IDLE_ICON));
                }
            }
            
            String statusMsg = pr.getStatus();
            if (statusMsg == null || statusMsg.length() == 0) {
                statusMsg = "";
            }else {
                statusMsg = "<p>" + statusMsg + "</p>";
            }
            friends[i].button.setToolTipText("<html>" + //"<img src=" + getClass().getResource(CHAT_ONLINE_ICON) + ">" + 
              "<p><b>" + entry.getUser() + "</b></p><p><i>" + status + "</i></p>" + statusMsg + "</html>");
            friends[i].button.addActionListener(this);
            friends[i].button.setActionCommand(entry.getUser());
            
            JPanel p2 = new JPanel();
            i++;
        }
        
        sortBuddyList();
        for (i = 0; i < friends.length; i++) {
            p.add(friends[i].button);
        }
        this.scroller = null;
        this.scroller = new JScrollPane(p);//, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.scroller.setBorder(new TitledBorder("My Buddies (" + olCount + "/" + friends.length + ")"));
        this.scroller.getVerticalScrollBar().setUnitIncrement(16);
        
        addComponents();
        setSize(500, 600);
        setLocationRelativeTo(null);
        setVisible(true);
        
    }
    
    private void refreshBuddyList()
    {
        olCount = 0;
//         roster = conn.getRoster();
//         roster.addRosterListener(new RosterListener() {
//             // Ignored events 
//             public void entriesAdded(Collection<String> addresses) {}
//             
//             public void entriesDeleted(Collection<String> addresses) {}
//             
//             public void entriesUpdated(Collection<String> addresses) {}
//             
//             public void presenceChanged(Presence presence)
//             {
//                 statusBar.setText(statusBar.getText() + "\n" + new Date() + " " + presence.getFrom() + ": " + presence);
//                 statusBar.setCaretPosition(statusBar.getText().length());
//                 refresh();
//             }
//         });
//         
        JPanel p = new JPanel();// {
//             public void paintComponent(Graphics g)
//             {
//                 super.paintComponent(g);
//                 
//                 Graphics2D g2d = (Graphics2D) g;
//                 g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//                 Image img = new Factory().createImageIcon(BUDDY_BG_IMG).getImage();
// //                 BufferedImage bimg = new BufferedImage(BufferedImage.TYPE_INT_ARGB, getWidth(), getHeight());
// //                 Graphics2D g2 = (Graphics2D) bimg.getGraphics();
//                 g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OVER, 0.5f));
// //                 g2.drawImage(img, 0, 0, null);
//                 g2d.setColor(Color.BLUE);
//                 g2d.fillRect(0, 0, 500, 500);
// //                 g2d.drawImage(img, 0, 0, null);
//             }
//         };
        p.setLayout(new GridLayout(0, 1));
        
        Collection<RosterEntry> entries = roster.getEntries();
//         int n = entries.size();
//         System.out.println(entries.size());
        
        int i = 0;
        for (RosterEntry entry : entries) {
//             String name = entry.getName();
//             if (name == null || name.length() == 0) {
//                 name = entry.toString();
//             }
            
            for (i = 0; i < friends.length; i++) {
                if (friends[i].button.getActionCommand().equals(entry.getUser())) {
                    break;
                }
            }
            
//             friends[i] = new Buddy();
//             friends[i].button = new JButton(name);
//             friends[i].button.setBorderPainted(false);
//             friends[i].button.setHorizontalAlignment(SwingConstants.LEFT);
            friends[i].button.setIcon(new Factory().createImageIcon(CHAT_OFFLINE_ICON));
            
            Presence pr = roster.getPresence(entry.getUser());
            String status = "Offline";
            if (pr.getType().equals(Presence.Type.available)) {
                olCount++;
                status = "Available";
                friends[i].button.setIcon(new Factory().createImageIcon(CHAT_ONLINE_ICON));
            }
            
            if (pr.getMode() != null) {    
                if (pr.getMode().equals(Presence.Mode.dnd)) {
                    status = "Do Not Disturb";
                    friends[i].button.setIcon(new Factory().createImageIcon(CHAT_BUSY_ICON));
                }else if (pr.getMode().equals(Presence.Mode.away)) {
                    status = "Away";
                    friends[i].button.setIcon(new Factory().createImageIcon(CHAT_IDLE_ICON));
                }
            }
            
            String statusMsg = pr.getStatus();
            if (statusMsg == null || statusMsg.length() == 0) {
                statusMsg = "";
            }else {
                statusMsg = "<p>" + statusMsg + "</p>";
            }
            friends[i].button.setToolTipText("<html>" + //"<img src=" + getClass().getResource(CHAT_ONLINE_ICON) + ">" + 
              "<p><b>" + entry.getUser() + "</b></p><p><i>" + status + "</i></p>" + statusMsg + "</html>");
            friends[i].button.addActionListener(this);
            friends[i].button.setActionCommand(entry.getUser());
            
            JPanel p2 = new JPanel();
        }
        
        sortBuddyList();
        for (i = 0; i < friends.length; i++) {
            p.add(friends[i].button);
        }
        this.scroller = new JScrollPane(p);
        this.scroller.setBorder(new TitledBorder("My Buddies (" + olCount + "/" + friends.length + ")"));
        this.scroller.getVerticalScrollBar().setUnitIncrement(15);
        
        addComponents();
//         setVisible(true);
    }
    
    private void buildBuddyList()
    {
        if (roster == null) {
            roster = conn.getRoster();
            roster.addRosterListener(new RosterListener() {
                // Ignored events 
                public void entriesAdded(Collection<String> addresses) {}
                
                public void entriesDeleted(Collection<String> addresses) {}
                
                public void entriesUpdated(Collection<String> addresses) {}
                
                public void presenceChanged(Presence presence)
                {
                    statusBar.setText(statusBar.getText() + "\n " + BULLET_CHAR + " " + new Date() + " " + presence.getFrom() + ": " + presence);
                    statusBar.setCaretPosition(statusBar.getText().length());
//                     log.scrollRectToVisible(new Rectangle(0,log.getHeight()));
//                     JLabel lab = new JLabel("" + new Date() + " " + presence.getFrom() + ": " + presence);
//                     lab.setToolTipText(lab.getText());
//                     statusBar.add(lab);
//                     statusBar.repaint();
//                     statusBar.revalidate();
//                     statusBar.setText(statusBar.getText() + new Date() + " " + presence.getFrom() + ": " + presence);
//                     statusBar.setToolTipText(presence.getFrom() + ": " + presence);
//                     System.out.println("Presence changed: " + presence.getFrom() + " " + presence);
//                     try {
//                         Thread.sleep(1000);
//                     }catch (Exception e) {}
                    refresh();
                }
            });
        }
        
        
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(0, 1));
        
        Collection<RosterEntry> entries = roster.getEntries();
        if (friends == null) {            
            friends = new Buddy[entries.size()];
        }
        int i = 0;
        for (RosterEntry entry : entries) {
            String name = entry.getName();
            if (name == null || name.length() == 0) {
                name = entry.toString();
            }
            
            if (friends[i] == null) {
                friends[i] = new Buddy();
            }else {
                for (i = 0; i < friends.length; i++) {
                    if (friends[i].button.getText().equals(entry.getUser())) {
                        break;
                    }
                }
            }
//             friends[i] = new Buddy();
            friends[i].button = new JButton(name);
            friends[i].button.setBorderPainted(false);
            friends[i].button.setHorizontalAlignment(SwingConstants.LEFT);
            friends[i].button.setIcon(new Factory().createImageIcon(CHAT_OFFLINE_ICON));
            
            Presence pr = roster.getPresence(entry.getUser());
            String status = "Offline";
            if (pr.getType().equals(Presence.Type.available)) {
                status = "Available";
                friends[i].button.setIcon(new Factory().createImageIcon(CHAT_ONLINE_ICON));
            }
            
            if (pr.getMode() != null) {    
                if (pr.getMode().equals(Presence.Mode.dnd)) {
                    status = "Do Not Disturb";
                    friends[i].button.setIcon(new Factory().createImageIcon(CHAT_BUSY_ICON));
                }else if (pr.getMode().equals(Presence.Mode.away)) {
                    status = "Away";
                    friends[i].button.setIcon(new Factory().createImageIcon(CHAT_IDLE_ICON));
                }
            }
            
            String statusMsg = pr.getStatus();
            if (statusMsg == null || statusMsg.length() == 0) {
                statusMsg = "";
            }else {
                statusMsg = "<p>" + statusMsg + "</p>";
            }
            friends[i].button.setToolTipText("<html>" + //"<img src=" + getClass().getResource(CHAT_ONLINE_ICON) + ">" + 
              "<p><b>" + entry.getUser() + "</b></p><p><i>" + status + "</i></p>" + statusMsg + "</html>");
            friends[i].button.addActionListener(this);
            friends[i].button.setActionCommand(entry.getUser());
            
            JPanel p2 = new JPanel();
            i++;
        }
        
        sortBuddyList();
        for (i = 0; i < friends.length; i++) {
            p.add(friends[i].button);
        }
        this.scroller = new JScrollPane(p);
        this.scroller.setBorder(new TitledBorder("My Buddies"));
        this.scroller.getVerticalScrollBar().setUnitIncrement(16);
        
        addComponents();
        
//         try {
//             setUndecorated(true);
//         }catch (Exception e) {
//             setUndecorated(false);
//         }
        setVisible(true);
        
    }
    
    private void addComponents()
    {   
        getContentPane().removeAll();
        getContentPane().add(scroller);
//         getContentPane().add(tLab, BorderLayout.NORTH);
        
        JPanel p = new JPanel();
        JPanel q = new JPanel();
        q.setLayout(new BorderLayout());
        p.setBorder(new TitledBorder("Log"));
        p.setLayout(new BorderLayout());
        this.log = new JScrollPane(statusBar);
        this.log.setBorder(new BevelBorder(BevelBorder.LOWERED));
        p.add(this.log, BorderLayout.CENTER);
        
        JPanel p3 = new JPanel();
        p3.setLayout(new BorderLayout());
        p3.add(tLab, BorderLayout.CENTER);
//         p3.add(mbar, BorderLayout.NORTH);
        JPanel p4 = new JPanel();
        p4.setLayout(new GridLayout(1, 0));
        
        p4.add(this.myMode);
        p4.add(this.myStatus);
        p4.setBorder(new TitledBorder("My Status"));
        q.add(p, BorderLayout.CENTER);
        q.add(p4, BorderLayout.NORTH);
//         JPanel p5 = new JPanel();
//         p5.setLayout(new BorderLayout());
//         p5.add(p4, BorderLayout.EAST);
//         p3.add(p4, BorderLayout.SOUTH);
        
        getContentPane().add(p3, BorderLayout.NORTH);
        
        getContentPane().add(q, BorderLayout.SOUTH);
        getContentPane().validate();
        getContentPane().repaint();
        
        this.scroller.requestFocus();
    }
    
    public void sortBuddyList()
    {
        for (int i = 0; i < friends.length; i++) {
            for (int j = i + 1; j < friends.length; j++) {
                String x = friends[i].button.getText();
                String y = friends[j].button.getText();
                if (x.compareToIgnoreCase(y) > 0) {
                    Buddy temp = friends[i];
                    friends[i] = friends[j];
                    friends[j] = temp;
                }
            }
        }
    }
    
    private void showLogin()
    {
        this.loginD = new JDialog(this, "JTalk Login");
        
        this.services = new JComboBox(new String [] {"Custom", "GTalk", "Facebook"});
        this.services.setForeground(Color.BLUE);
        this.services.setToolTipText("<html>Select from one of the services below to <br>automatically fill in the host, port and resource fields.<br>" + 
            "Select Custom to manually enter your own settings.</html>");
        ListCellRenderer renderer = new DefaultListCellRenderer();
        ((JLabel) renderer).setHorizontalAlignment(SwingConstants.CENTER);
        this.services.setRenderer(renderer);
        
//         this.services.setHorizontalAlignment(SwingConstants.RIGHT);
        this.services.setBorder(new TitledBorder("Import settings for"));
        this.services.addActionListener(this);
        
        this.host = new JTextField();//GTALK_HOST);
        this.host.setHorizontalAlignment(JTextField.CENTER);
        this.host.setBorder(new TitledBorder("Host"));
        
//         this.port = new JFormattedTextField(NumberFormat.getInstance());
        this.port = new JTextField("5222");//"" + GTALK_PORT);
        this.port.setHorizontalAlignment(JTextField.CENTER);
        this.port.setBorder(new TitledBorder("Port"));
        
        this.rsrc = new JTextField();//GMAIL_SERV_NAME);
        this.rsrc.setHorizontalAlignment(JTextField.CENTER);
        this.rsrc.setBorder(new TitledBorder("Resource"));
        
        this.comp = new JCheckBox("Use Compression", true);
        this.comp.setToolTipText("<html><p>Sets if the connection is going to use stream compression. </p>" + 
            "<p>Stream compression will be requested after TLS was established </p>" + 
            "<p>(if TLS was enabled) and only if the server offered stream compression. </p>" + 
            "<p>With stream compression network traffic can be reduced up to 90%</p></html>"); 
        this.sasl = new JCheckBox("Use SASL Authentication", true);
        this.sec = new JComboBox(new String [] {"TLS Encryption Required", "Use TLS Encryption If Available", "No TLS Encryption"});
        this.sec.setBorder(new TitledBorder("Security Mode"));
        this.saslM = new JComboBox(new String [] {"PLAIN", "DIGEST-MD5", "KERBEROS V4"});
//         this.enc = new JCheckBox("Use Encryption", true);
        
        this.user = new JTextField(USERNAME_HINT);
        this.user.setHorizontalAlignment(JTextField.CENTER);
        this.user.setBorder(new TitledBorder("Username"));
        this.pass = new JPasswordField(PASSWORD_HINT);
        this.pass.setEchoChar('*');
        this.pass.setHorizontalAlignment(JPasswordField.CENTER);
        this.pass.setBorder(new TitledBorder("Password"));
        JButton loginB = Factory.createButton("Sign in", this);
        loginB.setEnabled(true);
        user.setEditable(true);
        pass.setEditable(true);
        
        loginD.getRootPane().setDefaultButton(loginB);
        
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout(FlowLayout.CENTER));
        p.add(loginB);
        p.add(Factory.createButton("Exit", this));
//         p.add(Factory.createButton("About", this));
        
        JPanel p2 = new JPanel();
        p2.setLayout(new GridLayout(0, 1));
        p2.add(this.services);
        p2.add(this.host);
        p2.add(this.port);
        p2.add(this.rsrc);
        p2.add(this.comp);
        p2.add(this.sec);
//         p2.add(this.enc);
        JPanel ps = new JPanel();
        ps.setLayout(new FlowLayout(FlowLayout.LEFT));
        ps.add(this.sasl);
        ps.add(this.saslM);
        p2.add(ps);
        p2.add(this.user);
        p2.add(this.pass);
        p2.setBorder(new TitledBorder("Enter settings and login information:"));
        
        JPanel p4 = new JPanel();
        p4.add(new JLabel(), BorderLayout.NORTH);
        
        JPanel p3 = new JPanel();
        p3.setLayout(new BorderLayout());
        JLabel pic = new JLabel("");
        pic.setIcon(new Factory().createImageIcon(LOGIN_ICON));
        p3.add(pic, BorderLayout.CENTER);
        
        loginD.add(new JLabel(" "), BorderLayout.NORTH);
        loginD.add(p2, BorderLayout.CENTER);
        loginD.add(p, BorderLayout.SOUTH);
        loginD.add(p3, BorderLayout.WEST);

        loginD.setSize(430, 550);
        loginD.setResizable(false);
//         loginD.pack();
        loginD.setLocationRelativeTo(null);
        loginD.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        loginD.setVisible(true);
        user.requestFocusInWindow();
        requestFocus();
    }
    
    public void authenticate()
    {
        try {
            String host = this.host.getText().trim();
            int port = 5222;
            try {
                port = Integer.parseInt(this.port.getText().trim());
            }catch (NumberFormatException e) {
                showErrorDialog("Port number should be an integer ranging from 0 to 65535");
                this.port.setText("");
                return;
            }
            String resource = this.rsrc.getText().trim();
            String username = this.user.getText().trim();
            String password = this.pass.getText().trim();
            
            if (host == null || host.length() == 0) {
                showErrorDialog("Please enter host");
                this.host.requestFocusInWindow();
                return;
            }
            
            if (username.length() == 0) {
                showErrorDialog("Please enter your username");
                this.user.requestFocusInWindow();
                return;
            }
//             if (username.indexOf('@') < 0) {
//                 username += "@" + resource;
//             }
            
            ConnectionConfiguration config = new ConnectionConfiguration(host, port, resource);
            if (this.sec.getSelectedIndex() == 0) {
                config.setSecurityMode(ConnectionConfiguration.SecurityMode.required);
            }else if (this.sec.getSelectedIndex() == 1) {
                config.setSecurityMode(ConnectionConfiguration.SecurityMode.enabled);
            }else {
                config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
            }
            if (this.comp.isSelected()) {
                config.setCompressionEnabled(true);
            }
            if (this.sasl.isSelected()) {
                config.setSASLAuthenticationEnabled(true);
                String mech = "";
                if (this.saslM.getSelectedIndex() == 0) {
                    mech = "PLAIN";
                }else if (this.saslM.getSelectedIndex() == 1) {
                    mech = "DIGEST_MD5";
                }else {
                    mech = "KERBEROS_V4";
                }
                SASLAuthentication.supportSASLMechanism(mech);
            }else {
                config.setSASLAuthenticationEnabled(false);
            }
            
            conn = null;
            conn = new XMPPConnection(config);
            conn.connect();
            
            conn.login(username, password);
            this.statusBar.setText(this.statusBar.getText() + "\n " + BULLET_CHAR + " Logged in as " + username);
//             this.logV = null;
//             this.logV = new Vector<Presence>();
            
//             System.out.println("Connected");
            loginD.setVisible(false);
            this.sLogout.setEnabled(true);
            loggedIn = true;
            
//             OfflineMessageManager offline = new OfflineMessageManager(conn);
//             System.out.println(offline.getMessageCount());
            
            // start listening for incoming chats
            PacketListener myListener = new PacketListener() {
                public void processPacket(Packet packet) {
                    if (packet instanceof Message) {
                        Message msg = (Message) packet;
                        if (msg != null && msg.getBody() != null) {
                            for (int i = 0; i < friends.length; i++) {
                                if (msg.getFrom().startsWith(friends[i].button.getActionCommand()) && !ChatWin.isActive(friends[i].button.getActionCommand())) {
                                    friends[i].cwin = new ChatWin(selfRef, friends[i].button.getText(), friends[i].button.getActionCommand(), conn);
                                    friends[i].cwin.recvMessage(msg);
//                                     friends[i].cwin = new ChatWin(selfRef, friends[i].button.getText(), friends[i].button.getActionCommand(), conn, msg);
                                }else if (msg.getFrom().startsWith(friends[i].button.getActionCommand()) && ChatWin.isActive(friends[i].button.getActionCommand())) {
                                    friends[i].cwin.recvMessage(msg);
                                    friends[i].cwin.setVisible(true);
                                }
                            }
                        }
//                         System.out.println(msg.getFrom());
//                         
//                         System.out.println(msg.getFrom() + ": " + msg.getBody());
    //                     if(msg != null && msg.getBody() != null && msg.getFrom().indexOf(user) == 0) {
    //                         if (turn == U) {
    //                             tscript += "<p>" + msg.getBody() + "</p>";
    //                         }else {
    //                             tscript += "<p><b>" + user + ": </b>" + msg.getBody() + "</p>";
    //                         }
    //                         transcript.setText("<html>" + tscript + "</html>");
    //                         turn = U;
    //                         scrollToBottom();
    //                     }
                    }
                }
            };
            // Register the listener.  
            conn.addPacketListener(myListener, null);  
        }catch (Exception e) {
            showErrorDialog(e);
//             setVisible(false);
//             loginD.setVisible(true);
            return;
        }
        
//         buildBuddyList();
        initBuddyList();
    }
    
    private void showAboutDialog()
    {
        if (about != null && about.isVisible()) {
            return;
        }
        about = new JDialog(this, "About JTalk");
        about.setLayout(new BorderLayout());
        about.add(new JLabel(new Factory().createImageIcon(Defaults.TALK_ABOUT_ICON)) {
                    public void paint(Graphics g)
                    {
                        Graphics2D g2d = (Graphics2D) g;
                        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        super.paint(g);
                    }
                }, BorderLayout.WEST);
        
        JLabel info = new JLabel("<html><font size=5>IntelliSol JTalk</font><br>" + 
                                "<font size=4><i>A Secure XMPP Chat Client</i><br><br>Release: 1.0.0 BETA<br>" + 
                                "<font size=3>(A part of the JMail package)</font><br><br>" + 
                                "<font size=4>Authors: Ayon Ghosh, Kaustav Chattopadhyay & Arindam Guha<br><br>Powered by <i>Smack</i></font><br><br>" + 
                                "<font size=2>http://theway2solstice.webs.com/code.htm<br>theway2solstice@gmail.com</font><br><br></html>") {
                                    public void paint(Graphics g)
                                    {
                                        Graphics2D g2d = (Graphics2D) g;
                                        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                                        super.paint(g);
                                    }
                                };
        info.setBorder(new TitledBorder(""));
        
        JLabel credits = new JLabel("<html>" + 
                                    "<b>Smack API 3.2.0 Beta</b><br>Copyright 2002-2008 Jive Software.<br>http://www.igniterealtime.org/projects/smack/<br><br>" + 
                                    "<b>aSmack</b><br>Android build environment and patches for Smack<br>http://code.google.com/p/asmack/<br><br>" + 
                                    "<b>Synthetica - the Enterprise Look and Feel</b><br>Copyright © Jyloo Software<br>All rights reserved.<br><br>" + 
                                    "<b>SysTray for Java v2.4.1 (Win32 & KDE)</b><br>by daveselinger & ribizli<br>Copyright © 2002-2004 SnoozeSoft<br>" + 
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
        about.setSize(about.getWidth() + 50, 500);
        about.setResizable(false);
        about.setLocationRelativeTo(null);
        about.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        about.setVisible(true);
    }
    
    private void setStates(JCheckBoxMenuItem m)
    {
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
    
    public void actionPerformed(ActionEvent ae)
    {
        String cmd = ae.getActionCommand();
        if (ae.getSource().equals(this.services)) {
            int i = this.services.getSelectedIndex();
            if (i == 1) {
                this.host.setText("talk.google.com");
                this.port.setText("5222");
                this.rsrc.setText("gmail.com");
                this.comp.setSelected(true);
                this.sec.setSelectedIndex(0);
                this.sasl.setSelected(true);
                this.saslM.setSelectedIndex(0);
            }else if (i == 2) {
                this.host.setText("chat.facebook.com");
                this.port.setText("5222");
                this.rsrc.setText("chat.facebook.com");
                this.comp.setSelected(true);
                this.sec.setSelectedIndex(1);
                this.sasl.setSelected(true);
                this.saslM.setSelectedIndex(1);
            }
            return;
        }
        if (cmd.equalsIgnoreCase("exit")) {
            exit();
        }else if (cmd.equalsIgnoreCase("sign in")) {
            try {
                authenticate();
            }catch (Exception e) {
                showErrorDialog(e);
                setVisible(false);
                return;
            }
        }else if (cmd.equalsIgnoreCase("about")) {
            showAboutDialog();
        }else if (ae.getSource().equals(this.myMode)) {
            if (this.myMode.getSelectedIndex() == 0) {
                Presence presence = new Presence(Presence.Type.available);
                presence.setMode(Presence.Mode.chat);
                if (statusSet) {
                    presence.setStatus(this.myStatus.getText());
                }
//                 presence.setStatus("this code is working...");
//                 presence.setPriority(24);
        //         presence.setMode(Presence.Mode.away);
                conn.sendPacket(presence);
            }else if (this.myMode.getSelectedIndex() == 1) {
                Presence presence = new Presence(Presence.Type.available);
                presence.setMode(Presence.Mode.away);
                if (statusSet) {
                    presence.setStatus(this.myStatus.getText());
                }
//                 presence.setStatus("this code is working...");
//                 presence.setPriority(24);
        //         presence.setMode(Presence.Mode.away);
                conn.sendPacket(presence);
            }else if (this.myMode.getSelectedIndex() == 2) {
                Presence presence = new Presence(Presence.Type.available);
                presence.setMode(Presence.Mode.dnd);
                if (statusSet) {
                    presence.setStatus(this.myStatus.getText());
                }
//                 presence.setStatus("this code is working...");
//                 presence.setPriority(24);
        //         presence.setMode(Presence.Mode.away);
                conn.sendPacket(presence);
            }
//             else {
// //                 Presence presence = new Presence(new Presence.Type("invisible"));
//                 presence.setMode(new Presence.Mode("invisible"));
// //                 if (statusSet) {
// //                     presence.setStatus(this.myStatus.getText());
// //                 }
// //                 presence.setStatus("this code is working...");
// //                 presence.setPriority(24);
//         //         presence.setMode(Presence.Mode.away);
//                 conn.sendPacket(presence);
//             }
        }else if (cmd.equals("Black Moon")) {
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
        }else {
            String name = "";
            for (int i = 0; i < friends.length; i++) {
                if (ae.getSource().equals(friends[i].button)) {
                    name = friends[i].button.getText();
//                     System.out.println(name);
                    if (!ChatWin.isActive(cmd)) {
//                         System.out.println("not active");
                        ChatWin cwin = new ChatWin(this, name, cmd, this.conn);
                        friends[i].cwin = cwin;
                    }else if (friends[i].cwin != null) {
                        friends[i].cwin.setVisible(true);
//                         System.out.println("active");
                    }
                    break;
                }
            }
            
        }
    }
    
    public void keyPressed(KeyEvent ke)
    {
        
    }
    
    public void keyReleased(KeyEvent ke)
    {
        if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
            Presence presence = new Presence(Presence.Type.available);
//             presence.setMode(Presence.Mode.dnd);
            presence.setStatus(this.myStatus.getText());
//                 presence.setPriority(24);
    //         presence.setMode(Presence.Mode.away);
            conn.sendPacket(presence);
            statusSet = true;
        }
    }
    
    public void mouseEntered(MouseEvent me)
    {
        
    }
    
    public void mouseExited(MouseEvent me)
    {
        
    }
    
    public void mousePressed(MouseEvent me)
    {
        
    }
    
    public void mouseReleased(MouseEvent me)
    {
        
    }
    
    public void mouseClicked(MouseEvent me)
    {
        showAboutDialog();
    }
    
    public void keyTyped(KeyEvent ke)
    {
        
    }
    
    private void exit()
    {
        System.exit(0);
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
    
    private void refresh()
    {
        if (exiting) {
            return;
        }
        refreshBuddyList();
//         buildBuddyList();
    }
    
    public void dispose()
    {
        exiting = true;
        if (conn != null) {
            conn.disconnect();
        }
        this.statusBar.setText(statusBar.getText() + "\n " + BULLET_CHAR + " Logged out");
//         System.out.println("Disconnected");
        super.dispose();
        loggedIn = false;
        exiting = false;
//         System.exit(0);
    }
    
    public void iconLeftClicked(SysTrayMenuEvent evt)
    {
        if (isVisible()) {
            setVisible(false);
        }else if (loggedIn) {
            setVisible(true);
            repaint();
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
            dispose();
//             logout();
            System.exit(0);
        }else if (cmd.equalsIgnoreCase("logout")) {
            dispose();
            showLogin();
//             this.loginD.setVisible(true);
//             logout();
        }else if (cmd.equalsIgnoreCase("about")) {
            showAboutDialog();
        }
    }
    
    public static void main(String [] args) throws Exception
    {
        new ChatBox();
    }
}