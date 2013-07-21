package jmail.chat;

import javax.swing.*;
import java.awt.*;

public class Main extends Frame implements jmail.Defaults
{
    Main()
    {
        setLookAndFeel(CHAT_LAF);//"de.javasoft.plaf.synthetica.SyntheticaBlackMoonLookAndFeel");
        //"com.jtattoo.plaf.graphite.GraphiteLookAndFeel"//"com.nilo.plaf.nimrod.NimRODLookAndFeel");//
    }
    
    public static void main(String [] args)
    {
       Main mn = new Main();
       try {
           new ChatBox();
        }catch (Exception e) {
            mn.showErrorDialog(e);
        }catch (Error e) {
            mn.showErrorDialog(e.toString());
        }
    }
    
    private void setLookAndFeel(String laf)
    {
        try {
            UIManager.setLookAndFeel(laf);
            SwingUtilities.updateComponentTreeUI(this);
//             this.validate();
//             if (friends != null) {
//                 for (int i = 0; i < friends.length; i++) {
//                     if (friends[i].cwin != null) {
//                         friends[i].cwin.validate();
//                     }
//                 }
//             }
        }catch (Exception e) {
            e.printStackTrace();
        }
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
