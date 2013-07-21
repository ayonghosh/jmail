package jmail.chat;


import net.sf.jml.*;
import net.sf.jml.impl.*;
import net.sf.jml.event.*;
import net.sf.jml.message.*;
import net.sf.jml.message.p2p.*;

public class MsnTest implements MsnContactListListener, MsnMessageListener, MsnSwitchboardListener
{
    private MsnMessenger msn;
    
    MsnTest()
    {
        msn = MsnMessengerFactory.createMsnMessenger("theway2solstice@hotmail.com", "21091988");
        msn.addContactListListener(this);
        msn.addMessageListener(this);
        msn.addSwitchboardListener(this);
        
    }
    
    private void login()
    {
        msn.login();
        System.out.println("logged in");
//         Email e = Email.parseStr("missara@cox.net");
//         System.out.println(e);
//         msn.addFriend(e, "MelissaReynolds");
        MsnSwitchboard [] sb = msn.getActiveSwitchboards();
        System.out.println(sb.length + " active switchboards");
        for (int i = 0; i < sb.length; i++) {
            System.out.println(sb[i]);
        }
        MsnContact [] contacts = msn.getContactList().getContacts();
        System.out.println(contacts.length + " contacts");
        for (int i = 0; i < contacts.length; i++) {
            System.out.println(contacts[i]);
        }
    }
    
    private void logout()
    {
        MsnSwitchboard [] sb = msn.getActiveSwitchboards();
        System.out.println(sb.length + " active switchboards");
        for (int i = 0; i < sb.length; i++) {
            System.out.println(sb[i]);
        }
        MsnContact [] contacts = msn.getContactList().getContacts();
        System.out.println(contacts.length + " contacts");
        for (int i = 0; i < contacts.length; i++) {
            System.out.println(contacts[i]);
        }
        
        msn.logout();
    }
    
    public static void main(String [] args) throws Exception
    {
        MsnTest test = new MsnTest();
        test.login();
        Thread.sleep(5000);
        test.logout();
    }
    
    public void contactAddCompleted(MsnMessenger messenger, MsnContact contact, MsnList list) {}
    
    public void contactAddedMe(MsnMessenger messenger, MsnContact contact) {}
    
    public void contactAddedMe(MsnMessenger messenger, MsnContactPending[] pending) {}
    
    public void contactAddInGroupCompleted(MsnMessenger messenger, MsnContact contact, MsnGroup group) {}
    
    public void contactListInitCompleted(MsnMessenger messenger) {}
    
    public void contactListSyncCompleted(MsnMessenger messenger) {}
    
    public void contactPersonalMessageChanged(MsnMessenger messenger, MsnContact contact) {}
    
    public void contactRemoveCompleted(MsnMessenger messenger, MsnContact contact, MsnList list) {}
    
    public void contactRemovedMe(MsnMessenger messenger, MsnContact contact) {}
    
    public void contactRemoveFromGroupCompleted(MsnMessenger messenger, MsnContact contact, MsnGroup group) {}
    
    public void contactStatusChanged(MsnMessenger messenger, MsnContact contact) {}
    
    public void groupAddCompleted(MsnMessenger messenger, MsnGroup group) {}
    
    public void groupRemoveCompleted(MsnMessenger messenger, MsnGroup group) {}
    
    public void ownerDisplayNameChanged(MsnMessenger messenger) {}
    
    public void ownerStatusChanged(MsnMessenger messenger) {}
    
    
    public void controlMessageReceived(MsnSwitchboard switchboard, MsnControlMessage message, MsnContact contact) {}
    
    public void datacastMessageReceived(MsnSwitchboard switchboard, MsnDatacastMessage message, MsnContact contact) {}
    
    public void instantMessageReceived(MsnSwitchboard switchboard, MsnInstantMessage message, MsnContact contact) {}
    
    public void offlineMessageReceived(String body, String contentType, String encoding, MsnContact contact) {}
    
    public void p2pMessageReceived(MsnSwitchboard switchboard, MsnP2PMessage message, MsnContact contact) {}
    
    public void systemMessageReceived(MsnMessenger messenger, MsnSystemMessage message) {}
    
    public void unknownMessageReceived(MsnSwitchboard switchboard, MsnUnknownMessage message, MsnContact contact) {}
    

    public void contactJoinSwitchboard(MsnSwitchboard switchboard, MsnContact contact) {}
     
    public void contactLeaveSwitchboard(MsnSwitchboard switchboard, MsnContact contact)  {}
    
    public void switchboardClosed(MsnSwitchboard switchboard) {}
    
    public void switchboardStarted(MsnSwitchboard switchboard) {}

}
