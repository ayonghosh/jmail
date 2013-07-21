package jmail.chat;


import net.sf.jfacebookiml.*;

public class FBTest
{
    public static void main(String [] args)
    {
        FacebookAdapter fba = new FacebookAdapter();
        fba.initialize("ayon.ghosh", "21091988");
        FacebookBuddyList buddy = new FacebookBuddyList(fba);
        
    }
}
