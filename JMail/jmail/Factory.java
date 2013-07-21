package jmail;

 

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.border.*;
import javax.mail.*;
import java.util.StringTokenizer;

import snoozesoft.systray4j.*;


public class Factory implements Defaults
{
    public static String getFolderName(String str)
    {
        if (str.equals(INBOX)) {
            return "Inbox";
        }else if (str.equals(SENT_MAIL)) {
            return "Sent Mail";
        }else if (str.equals(SPAM)) {
            return "Spam";
        }else if (str.equals(STARRED)) {
            return "Starred";
        }else if (str.equals(TRASH)) {
            return "Trash";
        }else if (str.equals(DRAFTS)) {
            return "Drafts";
        }
        return "";
    }
    
    public static JButton createButton(String text, ActionListener parent)
    {
        JButton b = new JButton(text);
        b.setFont(BUTTON_FONT);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
//         b.setForeground(BUTTON_FG);
        b.addActionListener(parent);
        
        return b;
    }
    
    public static JButton createButton(String text, String tooltip, ActionListener parent)
    {
        JButton b = new JButton(text);
        b.setFont(BUTTON_FONT);
        b.setToolTipText(tooltip);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
//         b.setForeground(BUTTON_FG);
        b.addActionListener(parent);
        
        return b;
    }
    
    public static JButton createButton(String text, String ac, String tooltip, ActionListener parent)
    {
        JButton b = new JButton(text);
        b.setFont(BUTTON_FONT);
        b.setActionCommand(ac);
        b.setToolTipText(tooltip);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
//         b.setForeground(BUTTON_FG);
        b.addActionListener(parent);
        
        return b;
    }
    
    public static JButton createBLButton(String text, ActionListener parent)
    {
        JButton b = new JButton(text);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
//         b.setFont(BUTTON_FONT);
        b.setBorderPainted(false);
//         b.setForeground(BUTTON_FG);
        b.addActionListener(parent);
        
        return b;
    }
    
    public static JTextField createTextField(KeyListener parent)
    {
        JTextField jtf = new JTextField();
        jtf.addKeyListener(parent);
        
        return jtf;
    }
    
    public static JMenuItem createMenuItem(String text, ActionListener parent)
    {
        JMenuItem m = new JMenuItem(text);
        m.setFont(MENUITEM_FONT);
//         m.setForeground(MENUITEM_FG);
        m.addActionListener(parent);
        
        return m;
    }
    
    public static JMenu createMenu(String text, ActionListener parent)
    {
        JMenu m = new JMenu(text);
        m.setFont(MENU_FONT);
//         m.setForeground(MENU_FG);
        m.addActionListener(parent);
        
        return m;
    }
    
    public static JCheckBoxMenuItem createCheckBoxMenuItem(String text, ActionListener parent)
    {
        JCheckBoxMenuItem m = new JCheckBoxMenuItem(text);
        m.setFont(CBMENUITEM_FONT);
//         m.setForeground(CBMENUITEM_FG);
        m.addActionListener(parent);
        
        return m;
    }
    
    public static JCheckBox createCheckbox(String text)
    {
        JCheckBox jcb = new JCheckBox(text);
        jcb.setFont(CBMENUITEM_FONT);
        
        return jcb;
    }
    
    public ImageIcon createImageIcon(String filename)
    {
        String path = ICON_PATH + filename;
        return new ImageIcon(getClass().getResource(path)); 
    }
    
    public java.net.URL getIconPath(String filename)
    {
        String path = ICON_PATH + filename;
        return getClass().getResource(path); 
    }
    
    public static SysTrayMenuItem createSysTrayMenuItem(String str, SysTrayMenuListener parent)
    {
        SysTrayMenuItem stmi = new SysTrayMenuItem(str, str);
        stmi.addSysTrayMenuListener(parent);
        
        return stmi;
    }
    
    public static JLabel createLabel(String text)
    {
        JLabel lab = new JLabel(text);
        lab.setBorder(new TitledBorder(""));
        
        return lab;
    }
    
    public JPanel createAttachment(Part attachment, int i, ActionListener parent) throws Exception
    {
        JPanel p = new JPanel();
        String filename = attachment.getFileName();
        if (filename == null || filename.length() == 0) {
            filename = "Unknown";
        }
        
        String type = attachment.getContentType().toLowerCase();
        StringTokenizer stok = new StringTokenizer(type, "; ");
        String shortType = "<" + stok.nextToken() + ">";
//         String shortType = "<html><i><<" + stok.nextToken() + ">><i></html>";
        JLabel lt = new JLabel(shortType);
        lt.setFont(CTYPE_FONT);
        
        JLabel lab = new JLabel(filename);
        lab.setFont(BUTTON_FONT);
        p.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        JLabel pic = new JLabel(createImageIcon(ATTACHMENT_ICON));
        p.add(pic);
//         p.setBorder(new TitledBorder(""));
        p.add(lab);
//         lab.setToolTipText(type);
        p.add(lt);
//         lt.setToolTipText(type);
        JButton b = createBLButton("Download", parent);
        b.setForeground(DOWNLOAD_FG);
        b.setActionCommand("$" + i);
        p.add(b);
        
        return p;
    }
    
    public JPanel createAttachment(Part attachment, int i, ActionListener parent, boolean digiSign) throws Exception
    {
        JPanel p = new JPanel();
        String filename = attachment.getFileName();
        if (filename == null || filename.length() == 0) {
            filename = "Unknown";
        }
        
        String type = attachment.getContentType().toLowerCase();
        StringTokenizer stok = new StringTokenizer(type, "; ");
        String shortType = "<" + stok.nextToken() + ">";
//         String shortType = "<html><i><<" + stok.nextToken() + ">><i></html>";
        JLabel lt = new JLabel(shortType);
        lt.setFont(CTYPE_FONT);
        
        JLabel lab = new JLabel(filename);
        lab.setFont(BUTTON_FONT);
        p.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        JLabel pic = new JLabel(createImageIcon(ATTACHMENT_ICON));
        p.add(pic);
//         p.setBorder(new TitledBorder(""));
        p.add(lab);
//         lab.setToolTipText(type);
        p.add(lt);
//         lt.setToolTipText(type);
//         JButton b = createBLButton("Download", parent);
//         b.setForeground(DOWNLOAD_FG);
//         b.setActionCommand("$" + i);
//         p.add(b);
        
        return p;
    }
    
    public JPanel createEAttachment(Part attachment, int i, ActionListener parent) throws Exception
    {
        JPanel p = new JPanel();
        String filename = attachment.getFileName();
        if (filename == null || filename.length() == 0) {
            filename = "Unknown";
        }
        
        String type = attachment.getContentType().toLowerCase();
        StringTokenizer stok = new StringTokenizer(type, "; ");
        String shortType = "<" + stok.nextToken() + ">";
//         String shortType = "<html><i><<" + stok.nextToken() + ">><i></html>";
        JLabel lt = new JLabel(shortType);
        lt.setFont(CTYPE_FONT);
        
        JLabel lab = new JLabel(filename);
        lab.setFont(BUTTON_FONT);
        p.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        JLabel pic = new JLabel(createImageIcon(ATTACHMENT_ICON));
        p.add(pic);
//         p.setBorder(new TitledBorder(""));
        p.add(lab);
//         lab.setToolTipText(type);
        p.add(lt);
//         lt.setToolTipText(type);
        JButton b = createBLButton("Download", parent);
        b.setForeground(DOWNLOAD_FG);
        b.setActionCommand("$" + i);
        p.add(b);
        
        JButton del = createBLButton("Remove", parent);
        del.setForeground(DOWNLOAD_FG);
        del.setActionCommand("#" + i);
        p.add(del);
        
        return p;
    }
}
