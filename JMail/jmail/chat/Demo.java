package jmail.chat;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.*;
import jmail.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

public class Demo extends JFrame implements Defaults, ActionListener
{
    private JButton[] friends;
    private Connection conn;
    private Roster roster;
    private boolean exiting;
    private JScrollPane scroller;
    private JPanel pane;
    private JTextArea statusBar;
    
    public Demo()
    {
        super("JTalk");
        
        setIconImage(new Factory().createImageIcon(GTALK_ICON).getImage());
        
        try {
            // Create a connection to the jabber.org server on a specific port.
            ConnectionConfiguration config = new ConnectionConfiguration(GTALK_HOST, GTALK_PORT, GMAIL_SERV_NAME);
//             ConnectionConfiguration config = new ConnectionConfiguration("chat.facebook.com", 5222, "chat.facebook.com");
//             config.setSASLAuthenticationEnabled(false);
            config.setSecurityMode(ConnectionConfiguration.SecurityMode.required);
//             config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
            config.setCompressionEnabled(true);
            config.setSASLAuthenticationEnabled(true);
    
            conn = new XMPPConnection(config);
            conn.connect();
            SASLAuthentication.supportSASLMechanism("PLAIN", 0);
            conn.login("", "");
            System.out.println("Connected");
    //         Thread.sleep(5000);
    //         conn.disconnect();
            
    
            
//                 System.out.println(entry);
    //             Presence p = roster.getPresence(entry.getUser());
    //             System.out.print("" + entry.getUser() + " : ");
    //             if (p == null) {
    //                 System.out.println("Unavailable");
    //             }else {
    //                 System.out.println(p);
    //             }
//             }
            
            
//             this.pane = new JPanel();
//             this.pane.setLayout(new BorderLayout());

            statusBar = new JTextArea(3, 0);
            statusBar.setText("Welcome to JTalk");
//             statusBar.setForeground(Color.BLUE);
            
            addList();
    
    
            
    //         Chat chat = conn.getChatManager().createChat("theway2solstice@gmail.com", new MessageListener() {
    //  
    //              public void processMessage(Chat chat, Message message) {
    //                  // Print out any messages we get back to standard out.
    //                  System.out.println("Received message: " + message);
    //              }
    //          });
    //          chat.sendMessage("Howdy!");
             // Disconnect from the server
//              conn.disconnect();
//              System.out.println("Disconnected");


            
//             JButton b = new JButton("refresh");
//             b.addActionListener(this);
//             add(b, BorderLayout.SOUTH);
            
            
            UIManager.setLookAndFeel("de.javasoft.plaf.synthetica.SyntheticaBlackMoonLookAndFeel");//"de.javasoft.plaf.synthetica.SyntheticaBlackEyeLookAndFeel");//
            SwingUtilities.updateComponentTreeUI(this);
            
        }catch (Exception e) {
            e.printStackTrace();
        }
        
//         add(this.pane);
        
        
        
        setSize(400, 600);
//         pack();
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    private void addList()
    {
        if (roster == null) {
            roster = conn.getRoster();
            roster.addRosterListener(new RosterListener() {
                // Ignored events 
                public void entriesAdded(Collection<String> addresses) {}
                public void entriesDeleted(Collection<String> addresses) {}
                public void entriesUpdated(Collection<String> addresses) {}
                public void presenceChanged(Presence presence) {
                    statusBar.setText(statusBar.getText() + "\n" + new Date() + " " + presence.getFrom() + ": " + presence);
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
        friends = new JButton[entries.size()];
        int i = 0;
        for (RosterEntry entry : entries) {
            String name = entry.getName();
            if (name == null || name.length() == 0) {
                name = entry.toString();
            }
            
            friends[i] = new JButton(name);
//             friends[i].setBorder(new TitledBorder(""));
            friends[i].setBorderPainted(false);
            friends[i].setHorizontalAlignment(SwingConstants.LEFT);
            friends[i].setIcon(new Factory().createImageIcon(CHAT_OFFLINE_ICON));
            
            Presence pr = roster.getPresence(entry.getUser());
            String status = "Offline";
            if (pr.getType().equals(Presence.Type.available)) {
                status = "Available";
                friends[i].setIcon(new Factory().createImageIcon(CHAT_ONLINE_ICON));
            }
            
//             System.out.println(pr.getMode());
            
            if (pr.getMode() != null) {    
                if (pr.getMode().equals(Presence.Mode.dnd)) {
                    status = "Do Not Disturb";
                    friends[i].setIcon(new Factory().createImageIcon(CHAT_BUSY_ICON));
                }else if (pr.getMode().equals(Presence.Mode.away)) {
                    status = "Away";
                    friends[i].setIcon(new Factory().createImageIcon(CHAT_IDLE_ICON));
                }
            }
            
            
//             
//                 if (pr != null) {
//                     status = pr.getType().toString();
//                 }
            
//                 String status = (pr != null && pr.isAvailable()) ? "Available" : "Unavailable";//pr.getMode().toString();
            String statusMsg = pr.getStatus();
            if (statusMsg == null || statusMsg.length() == 0) {
                statusMsg = "";
            }else {
                statusMsg = "<p>" + statusMsg + "</p>";
            }
            friends[i].setToolTipText("<html>" + //"<img src=" + getClass().getResource(CHAT_ONLINE_ICON) + ">" + 
              "<p><b>" + entry.getUser() + "</b></p><p><i>" + status + "</i></p>" + statusMsg + "</html>");
            friends[i].addActionListener(this);
            friends[i].setActionCommand(entry.getUser());
            
            JPanel p2 = new JPanel();
//                 p.add(friends[i]);
            i++;
        }
        
        sortList();
        for (i = 0; i < friends.length; i++) {
            p.add(friends[i]);
        }
        
        this.scroller = new JScrollPane(p);//, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        getContentPane().removeAll();
//         scroller.revalidate();
//         getContentPane().remove(scroller);
//         pane.removeAll();
//         pane.add(scroller);
        getContentPane().add(scroller);
//         add(statusBar, BorderLayout.SOUTH);
            //new JScrollPane(statusBar, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS), BorderLayout.SOUTH);
        getContentPane().validate();
        getContentPane().repaint();
        
    }
    
    public void actionPerformed(ActionEvent ae)
    {
        String ac = ae.getActionCommand();
        Chat chat = conn.getChatManager().createChat(ae.getActionCommand(), new MessageListener() {
             public void processMessage(Chat chat, Message message) {
                 // Print out any messages we get back to standard out.
                 System.out.println("Received message: " + message);
             }
         });
         ChatWin cwin = new ChatWin(this, ac, this.conn);
//         System.out.println(ae.getActionCommand());
//         refresh();
    }
    
    private void refresh()
    {
        if (exiting) {
            return;
        }
        addList();
//         roster = conn.getRoster();
//         roster.addRosterListener(new RosterListener() {
//             // Ignored events 
//             public void entriesAdded(Collection<String> addresses) {}
//             public void entriesDeleted(Collection<String> addresses) {}
//             public void entriesUpdated(Collection<String> addresses) {}
//             public void presenceChanged(Presence presence) {
//                 System.out.println("Presence changed: " + presence.getFrom() + " " + presence);
//                 refresh();
//             }
//         });
//         Collection<RosterEntry> entries = roster.getEntries();
// //         friends = new JLabel[entries.size()];
//         int i = 0;
//         for (RosterEntry entry : entries) {
//             Presence pr = roster.getPresence(entry.getUser());
//             String status = "Offline";
//             if (pr != null) {
// //                 status = pr.getType().toString();
// //                 if (pr.getStatus() != null) {
// //                     status += ": " + pr.getStatus();
// //                 }
//                 if (pr.getType().equals(Presence.Type.available)) {
//                     status = "Available";
//                     friends[i].setToolTipText("<html><p><i>" + status + "</i></p></html>");//"<html><p>" + entry.getUser() + "</p><p><i>" + status + "</i></p></html">);
// //                     status = status.toUpperCase();
// //                     friends[i].setForeground(Color.GREEN);
//                     friends[i].setIcon(new Factory().createImageIcon(CHAT_ONLINE_ICON));
//                 }
//             }
//             String name = entry.getName();
//             if (name == null || name.length() == 0) {
//                 name = entry.toString();
//             }
// //             String status = (p == null) ? "Unavailable" : p.getMode().toString();
//             friends[i].setText(name);
//             friends[i].setToolTipText(status);
//             i++;
//         }
//         sortList();
//         
        
    }
    
    public void sortList()
    {
        for (int i = 0; i < friends.length; i++) {
            for (int j = i + 1; j < friends.length; j++) {
                String x = friends[i].getText();
                String y = friends[j].getText();
                if (x.compareToIgnoreCase(y) > 0) {
                    JButton temp = friends[i];
                    friends[i] = friends[j];
                    friends[j] = temp;
                }
            }
        }
    }
    
    public void dispose()
    {
        exiting = true;
        if (conn != null) {
            conn.disconnect();
        }
        System.out.println("Disconnected");
        super.dispose();
        System.exit(0);
    }
    
    public static void main(String [] args) throws Exception
    {
        new Demo();
        
    }
}
