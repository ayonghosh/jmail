package jmail;

 

import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;
import java.awt.event.*;

public class MessageBar extends JPanel implements Defaults
{
    private MessagePreview preview;
    private JPanel prv;
    private JCheckBox checkBox;
    private JButton read;
    private JButton star;
    private int msgNum;
    private boolean isStarred;
    private boolean isRead;
    private JLabel fromL, subL, dateL;
    
    public MessageBar(MessagePreview preview, int msgNum, ActionListener parent)
    {
        this.preview = preview;
        this.msgNum = msgNum;
        
        this.prv = new JPanel();
        this.prv.setLayout(new GridLayout(1, 4, 10, 0));
        
        this.read = Factory.createBLButton("", parent);
        this.read.setToolTipText("Read message");
        this.read.setActionCommand("_" + msgNum);
        this.star = Factory.createButton("", parent);
        this.star.setToolTipText("Starred");
        this.star.setActionCommand("*" + msgNum);
        
        if (!this.preview.isRead()) {
            this.prv.setBackground(new JPanel().getBackground().brighter());//UNREAD_MSG_BG);
//             setBackground(new JPanel().getBackground().brighter());//UNREAD_MSG_BG);
            this.prv.add(fromL = new JLabel("<html><b>From: " + this.preview.getFrom() + "</b></html>"));
//             fromL.setForeground(UNREAD_MSG_FG);
            this.prv.add(subL = new JLabel("<html><b>  Sub: " + this.preview.getSubject() + "</b></html>"));
//             subL.setForeground(UNREAD_MSG_FG);
            this.prv.add(dateL = new JLabel("<html><b>  Dated: " + this.preview.getDate() + "</b></html>"));
//             dateL.setForeground(UNREAD_MSG_FG);
            if (this.preview.isStarred()) {
                this.read.setIcon(new Factory().createImageIcon(UNREAD_STARRED_ICON));
            }else {
                this.read.setIcon(new Factory().createImageIcon(UNREAD_ICON));
            }
        }else {
            this.prv.add(fromL = new JLabel("<html><b>From: </b>" + this.preview.getFrom() + "</html>"));
            this.prv.add(subL = new JLabel("<html><b>  Sub: </b>" + this.preview.getSubject() + "</html>"));
            this.prv.add(dateL = new JLabel("<html><b>  Dated: </b>" + this.preview.getDate() + "</html>"));
            if (this.preview.isStarred()) {
                this.read.setIcon(new Factory().createImageIcon(READ_STARRED_ICON));
            }else {
                this.read.setIcon(new Factory().createImageIcon(READ_ICON));
            }
        }
        
        this.prv.setBorder(new TitledBorder(""));
//         this.prv.setEditable(false);
        
        this.checkBox = new JCheckBox();
        
        setLayout(new BorderLayout());
        
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout());
        p.add(this.checkBox);
        p.add(this.read);
        
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(p, BorderLayout.WEST);
        add(this.prv, BorderLayout.CENTER);
    }
    
    public MessagePreview getPreview()
    {
        return this.preview;
    }
    
    public void refresh()
    {
        
        if (!this.preview.isRead()) {
//             setBackground(new JPanel().getBackground().brighter());//UNREAD_MSG_BG);
            this.prv.setBackground(new JPanel().getBackground().brighter());//UNREAD_MSG_BG);
            
            fromL.setText("<html><b>From: " + this.preview.getFrom() + "</b></html>");
            subL.setText("<html><b>  Sub: " + this.preview.getSubject() + "</b></html>");
            dateL.setText("<html><b>  Dated: " + this.preview.getDate() + "</b></html>");
            
            setBackground(new JPanel().getBackground().brighter());//UNREAD_MSG_BG);
//             fromL.setForeground(UNREAD_MSG_FG);
//             subL.setForeground(UNREAD_MSG_FG);
//             dateL.setForeground(UNREAD_MSG_FG);
            if (this.preview.isStarred()) {
                this.read.setIcon(new Factory().createImageIcon(UNREAD_STARRED_ICON));
            }else {
                this.read.setIcon(new Factory().createImageIcon(UNREAD_ICON));
            }
        }else {
            setBackground(new JPanel().getBackground());
            this.prv.setBackground(new JPanel().getBackground());
            
            fromL.setText("<html><b>From: </b>" + this.preview.getFrom() + "</html>");
            subL.setText("<html><b>  Sub: </b>" + this.preview.getSubject() + "</html>");
            dateL.setText("<html><b>  Dated: </b>" + this.preview.getDate() + "</html>");
            
//             fromL.setForeground(new JLabel().getForeground());
//             subL.setForeground(new JLabel().getForeground());
//             dateL.setForeground(new JLabel().getForeground());
            
            fromL.setBackground(new JLabel().getBackground());
            subL.setBackground(new JLabel().getBackground());
            dateL.setBackground(new JLabel().getBackground());
            if (this.preview.isStarred()) {
                this.read.setIcon(new Factory().createImageIcon(READ_STARRED_ICON));
            }else {
                this.read.setIcon(new Factory().createImageIcon(READ_ICON));
            }
        }
    }
    
    public boolean isSelected()
    {
        return checkBox.isSelected();
    }
    
    public void setSelected(boolean b)
    {
        checkBox.setSelected(b);
    }
    
    public boolean isRead()
    {
        return this.preview.isRead();
    }
    
    public boolean isStarred()
    {
        return this.preview.isStarred();
    }
    
    public void markRead()
    {
        this.preview.setRead(true);
    }
    
    public void markUnread()
    {
        this.preview.setRead(false);
    }
    
    public void setStarred()
    {
        this.preview.setStarred(true);
    }
    
    public void setUnstarred()
    {
        this.preview.setStarred(false);
    }
    
    public int getMessageNumber()
    {
        return this.msgNum;
    }
    
    public JButton getReadButton()
    {
        return this.read;
    }
}