package jmail;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import jmail.secure.Key;
import javax.swing.table.*;


public class AddressBook extends JFrame implements ActionListener, Defaults
{
    MessageComposer parent;
    
//     private InteractiveTableModel tableModel;
    private Vector<String> cols;
    private Vector<Vector> rdata;
    
    private static Vector<Contact> contacts;
    private boolean changed = false;
    private JTable ctab;
    
    AddressBook()
    {
        super("Address Book");
        
        loadContacts();
        
//         String [] cols = {"Name", "Email", "Phone", "Address", "Public Key Exponent", "Public Key Modulus"};
        this.cols = new Vector<String>();
        cols.add("Name");
        cols.add("Email");
        cols.add("Phone");
        cols.add("Address");
        cols.add("Public Key Exponent");
        cols.add("Public Key Modulus");
        
        updateContacts();
        
//         JMenuBar mb = new JMenuBar();
//         JMenu n = new JMenu("Add");
//         n.add(Factory.createMenuItem("New", this));
//         mb.add(n);
        
        JPanel mb = new JPanel();
        mb.setLayout(new FlowLayout(FlowLayout.LEFT));
        mb.add(Factory.createButton("Add New", this));
        mb.add(Factory.createButton("Edit", this));
        mb.add(Factory.createButton("Delete", this));
        mb.add(Factory.createButton("Save Changes", this));
        
        
        
//         String [][] val = {{"", "", "", "", "", ""}};
        this.ctab = new JTable(rdata, cols);//100, 6);
//         this.ctab.setTableHeader
//         this.tableModel = new InteractiveTableModel(cols);
//         this.ctab.setModel(tableModel);
        JScrollPane scroller = new JScrollPane(ctab);
        
//         loadContacts();

        refreshTable();
        
        add(mb, BorderLayout.NORTH);
        add(scroller);
        
        
        setIconImage(new Factory().createImageIcon(ADDRESS_BOOK_ICON).getImage());
        setSize(650, 500);
        setLocationRelativeTo(null);
//         setExtendedState(MAXIMIZED_BOTH);
        setVisible(true);
            
        
//         loadContacts();
//         JPanel p = new JPanel();
//         p.setLayout(new GridLayout(0, 5));
        
    }
    
    public String toString()
    {
        return new String();
    }
    
    private void updateContacts()
    {
        this.rdata = new Vector<Vector>();
        for (int i = 0; i < contacts.size(); i++) {
            Contact c = contacts.elementAt(i);
            Vector<String> rv = new Vector<String>();
            rv.add(c.getName());
            rv.add(c.getEmail());
            rv.add(c.getPhone());
            rv.add(c.getAddress());
            rv.add(c.getPublicKey().getExponent().toString());
            rv.add(c.getPublicKey().getModulus().toString());
            
            rdata.add(rv);
        }
        
        this.ctab = null;
        this.ctab = new JTable(this.rdata, this.cols);
        this.ctab.validate();
    }
    
    private void loadContacts()
    {
        if (contacts != null) {
            return;
        }
        Comparator comp = new CComp();
        contacts = new Vector<Contact>();
        try {
            BufferedReader bin = new BufferedReader(new FileReader(CONTACTS_FILE));
            String line = bin.readLine();
            while ((line = bin.readLine()) != null) {
//                 System.out.println(line);
                String [] cd = line.split(",");
                contacts.add(new Contact(cd[0], cd[1], cd[2], cd[3], new Key(cd[4], cd[5])));
            }
            bin.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        Collections.sort(contacts, comp);
    }
    
    private void refreshTable()
    {
//         if (contacts != null) {
//             return;
//         }
        
//         System.out.println("refreshing");
        
        Comparator comp = new CComp();
        Collections.sort(contacts, comp);
        
        Contact c;
        for (int i = 0; i < contacts.size(); i++) {
            c = contacts.elementAt(i);
            this.ctab.setValueAt(c.getName(), i, 0);
            this.ctab.setValueAt(c.getEmail(), i, 1);
            this.ctab.setValueAt(c.getPhone(), i, 2);
            this.ctab.setValueAt(c.getAddress(), i, 3);
            this.ctab.setValueAt(c.getPublicKey().getExponent().toString(), i, 4);
            this.ctab.setValueAt(c.getPublicKey().getModulus().toString(), i, 5);
        }
        
            
//         Comparator comp = new CComp();
//         contacts = new Vector<Contact>();
//         try {
//             BufferedReader bin = new BufferedReader(new FileReader(CONTACTS_FILE));
//             String line = bin.readLine();
//             int i = 0;
//             while ((line = bin.readLine()) != null) {
//                 String [] cd = line.split(",");
// //                 contacts.add(new Contact(cd[0], cd[1], cd[2], cd[3], new Key(cd[4], cd[5])));
//                 this.ctab.setValueAt(cd[0], i, 0);
//                 this.ctab.setValueAt(cd[0], i, 0);
//                 this.ctab.setValueAt(cd[1], i, 1);
//                 this.ctab.setValueAt(cd[2], i, 2);
//                 this.ctab.setValueAt(cd[3], i, 3);
//                 this.ctab.setValueAt(cd[4], i, 4);
//                 this.ctab.setValueAt(cd[5], i, 5);
//                 i++;
//             }
//             bin.close();
//         }catch (Exception e) {
//             e.printStackTrace();
//         }
//         Collections.sort(contacts, comp);
    }
    
    private void save()
    {
        try {
            PrintWriter pout = new PrintWriter(new BufferedWriter(new FileWriter(CONTACTS_FILE)));
            pout.println("Name,Email,Phone,Address,PKE,PKM");
            for (int i = 0; i < this.ctab.getRowCount(); i++) {
                pout.println(this.ctab.getValueAt(i, 0) + "," + 
                             this.ctab.getValueAt(i, 1) + "," + 
                             this.ctab.getValueAt(i, 2) + "," + 
                             this.ctab.getValueAt(i, 3) + "," + 
                             this.ctab.getValueAt(i, 4) + "," + 
                             this.ctab.getValueAt(i, 5)
                );
            }
            pout.close();
        }catch (Exception e) {
            
        }
    }
    
    public void actionPerformed(ActionEvent ae)
    {
        if (ae.getActionCommand().equalsIgnoreCase("add new")) {
            new FieldsDialog();
//             this.ctab.getModel().addEmptyRow();
        }else if (ae.getActionCommand().equalsIgnoreCase("edit")) {
            int i = this.ctab.getSelectedRow();
            if (i >= 0) {
                contacts.remove(i);
                new FieldsDialog((String) this.ctab.getValueAt(i, 0), 
                                 (String) this.ctab.getValueAt(i, 1),
                                 (String) this.ctab.getValueAt(i, 2),
                                 (String) this.ctab.getValueAt(i, 3),
                                 (String) this.ctab.getValueAt(i, 4),
                                 (String) this.ctab.getValueAt(i, 5)
                );
//                 refreshTable();
            }
        }else if (ae.getActionCommand().equalsIgnoreCase("delete")) {
            int [] si = this.ctab.getSelectedRows();
            if (si.length > 0) {
                int e = JOptionPane.showConfirmDialog(this, "Are you sure you want to permanently delete the selected contact(s)?", "Delete Contact", JOptionPane.YES_NO_OPTION, 
                                                      JOptionPane.QUESTION_MESSAGE);
                if (e == JOptionPane.YES_OPTION) {
                    for (int i = 0; i < si.length; i++) {
                        contacts.remove(si[i]);
                    }
                    refreshTable();
                }
            }
        }else if (ae.getActionCommand().equalsIgnoreCase("save changes")) {
            save();
        }
    }
    
    public static void main(String args[])
    {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch (Exception e) {}
        new AddressBook();
    }
    
    private class FieldsDialog extends JDialog implements ActionListener
    {
        private JTextField name, email, address, phone, pkm, pke;
        private JButton ok, cancel;
//         boolean edit;
        
        
        FieldsDialog(String n, String e, String phn, String addr, String pm, String pe)
        {
//             super("Edit Contact");
            setTitle("Edit Contact");
            
            this.name = new JTextField(n);
            this.email = new JTextField(e);
            this.address = new JTextField(addr);
            this.phone = new JTextField(phn);
            this.pkm = new JTextField(pm);
            this.pke = new JTextField(pe);
            
            getContentPane().setLayout(new BorderLayout());
            
            JPanel p = new JPanel();
            p.setLayout(new GridLayout(0, 2));
            p.add(new JLabel("Name:"));
            p.add(this.name);
            
            p.add(new JLabel("Email:"));
            p.add(this.email);
            
            p.add(new JLabel("Address:"));
            p.add(this.address);
            
            p.add(new JLabel("Phone:"));
            p.add(this.phone);
            
            p.add(new JLabel("Public Key Exponent:"));
            p.add(this.pke);
            
            p.add(new JLabel("Public Key Modulus:"));
            p.add(this.pkm);
            
            JPanel pp = new JPanel();
            pp.setLayout(new FlowLayout(FlowLayout.CENTER));
            pp.add(ok = Factory.createButton("Save Changes", this));
            pp.add(cancel = Factory.createButton("Cancel", this));
            
            getContentPane().setLayout(new BorderLayout());
            getContentPane().add(p, BorderLayout.CENTER);
            getContentPane().add(pp, BorderLayout.SOUTH);
            
            pack();
            setSize(400, getHeight());
            setLocationRelativeTo(null);
            setVisible(true);
            
        }
        
        FieldsDialog()
        {
            setTitle("Add New Contact");
            
            this.name = new JTextField("");
            this.email = new JTextField("");
            this.address = new JTextField("");
            this.phone = new JTextField("");
            this.pkm = new JTextField("0");
            this.pke = new JTextField("0");
            
            getContentPane().setLayout(new BorderLayout());
            
            JPanel p = new JPanel();
            p.setLayout(new GridLayout(0, 2));
            p.add(new JLabel("Name:"));
            p.add(this.name);
            
            p.add(new JLabel("Email:"));
            p.add(this.email);
            
            p.add(new JLabel("Address:"));
            p.add(this.address);
            
            p.add(new JLabel("Phone:"));
            p.add(this.phone);
            
            p.add(new JLabel("Public Key Exponent:"));
            p.add(this.pke);
            
            p.add(new JLabel("Public Key Modulus:"));
            p.add(this.pkm);
            
            JPanel pp = new JPanel();
            pp.setLayout(new FlowLayout(FlowLayout.CENTER));
            pp.add(ok = Factory.createButton("Add Contact", this));
            pp.add(cancel = Factory.createButton("Cancel", this));
            
            getContentPane().setLayout(new BorderLayout());
            getContentPane().add(p, BorderLayout.CENTER);
            getContentPane().add(pp, BorderLayout.SOUTH);
            
            pack();
            setLocationRelativeTo(null);
            setVisible(true);
        }
        
        public void actionPerformed(ActionEvent ae)
        {
            if (ae.getSource().equals(this.cancel)) {
                dispose();
            }else if (ae.getSource().equals(this.ok)) {
//                 System.out.println("ok");
                String pm = this.pkm.getText().trim();
                String pe = this.pke.getText().trim();
                if (pm.length() == 0) {
                    pm = "0";
                }
                if (pe.length() == 0) {
                    pe = "0";
                }
                contacts.add(new Contact(this.name.getText().trim(), 
                                         this.email.getText().trim(), 
                                         this.phone.getText().trim(), 
                                         this.address.getText().trim(), 
                                         new Key(pm, pe))
                );
                setVisible(false);
                if (ae.getActionCommand().toLowerCase().startsWith("add")) {
                    DefaultTableModel model = (DefaultTableModel) ctab.getModel();
                    model.setRowCount(model.getRowCount() + 1);
                }
                refreshTable();
            }
        }
    }
}



class InteractiveTableModel extends AbstractTableModel {
     public static final int NAME_INDEX = 0;
     public static final int EMAIL_INDEX = 1;
     public static final int PHONE_INDEX = 2;
     public static final int ADDRESS_INDEX = 3;
     public static final int PKE_INDEX = 4;
     public static final int PKM_INDEX = 5;

     protected String[] columnNames;
     protected Vector dataVector;

     public InteractiveTableModel(String[] columnNames) {
         this.columnNames = columnNames;
         dataVector = new Vector();
     }

     public String getColumnName(int column) {
         return columnNames[column];
     }

     public boolean isCellEditable(int row, int column) {
         return true;
     }

     public Class getColumnClass(int column) {
         switch (column) {
//              case TITLE_INDEX:
//              case ARTIST_INDEX:
//              case ALBUM_INDEX:
//                 return String.class;
             default:
                return String.class;
         }
     }

     public Object getValueAt(int row, int column) {
         Contact record = (Contact)dataVector.get(row);
         switch (column) {
             case NAME_INDEX:
                return record.getName();
             case EMAIL_INDEX:
                return record.getEmail();
             case PHONE_INDEX:
                return record.getPhone();
             case ADDRESS_INDEX:
                return record.getAddress();
             case PKE_INDEX:
                return record.getPublicKey().getExponent();
             case PKM_INDEX:
                return record.getPublicKey().getModulus();
             default:
                return new Object();
         }
     }

//      public void setValueAt(Object value, int row, int column) {
//          Contact record = (Contact)dataVector.get(row);
//          switch (column) {
//              case NAME_INDEX:
//                 record.setName((String)value);
//                 break;
//              case EMAIL_INDEX:
//                 record.setEmail((String)value);
//                 break;
//              case PHONE_INDEX:
//                 record.setPhone((String)value);
//                 break;
//              case ADDRESS_INDEX:
//                 record.setAddress((String) value);
//                 break;
//              case PKE_INDEX:
//                 record.setPublicKeyExponent((String) value);
//                 break;
//              case PKM_INDEX:
//                 record.setPublicKeyModulus((String) value);
//                 break;
//              default:
//                 System.out.println("invalid index");
//          }
//          fireTableCellUpdated(row, column);
//      }

     public int getRowCount() {
         return dataVector.size();
     }

     public int getColumnCount() {
         return columnNames.length;
     }

     public boolean hasEmptyRow() {
         if (dataVector.size() == 0) return false;
         Contact audioRecord = (Contact)dataVector.get(dataVector.size() - 1);
         if (audioRecord.getName().trim().equals("") &&
            audioRecord.getEmail().trim().equals(""))
         {
            return true;
         }
         else return false;
     }

     public void addEmptyRow() {
         dataVector.add(new Contact());
         fireTableRowsInserted(
            dataVector.size() - 1,
            dataVector.size() - 1);
     }
 }


class AddressBookPopup extends JDialog implements ActionListener, Defaults
{
    public static final int TO = 1;
    public static final int CC = 2;
    public static final int BCC = 3;
    
    private MessageComposer parent;
    private static Vector<Contact> contacts;
    private JCheckBox [] cb;
    private int ac;
    
    AddressBookPopup(MessageComposer parent, int ac)
    {
        super(parent, "Select Contact(s)");
        this.parent = parent;
        this.ac = ac;
        
        loadContacts();
        
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(0, 1));
        this.cb = new JCheckBox[contacts.size()];
        
//         p.add(new JLabel("Select"));
//         p.add(new JLabel("<html><b>Name</b></html>"));
//         p.add(new JLabel("<html><b>Email</b></html>"));
        for (int i = 0; i < contacts.size(); i++) {
            JPanel jp = new JPanel();
            jp.setLayout(new FlowLayout(FlowLayout.LEFT));
            jp.add(cb[i] = new JCheckBox(""));
            Contact c = contacts.elementAt(i);
            JLabel lab = new JLabel("<html><b>" + c.getName() + "</b> &lt;<i>" + c.getEmail() + "</i>&gt;</html>");
            lab.setToolTipText("<html>Telephone: " + c.getPhone() + "<br>Address: " + c.getAddress() + "<html>");
            jp.add(lab);
            p.add(jp);
//             p.add(cb[i] = new JCheckBox(""));
//             p.add(new JLabel("<html><b>" + contacts.elementAt(i).getName() + "</b></html>"));
//             p.add(new JLabel("<html><i>" + contacts.elementAt(i).getEmail() + "</i></html>"));
        }
        
        JPanel p2 = new JPanel();
        p2.setLayout(new FlowLayout(FlowLayout.CENTER));
        p2.add(Factory.createButton("Done", this));
        
        JScrollPane scroller = new JScrollPane(p);
        setLayout(new BorderLayout());
        add(scroller, BorderLayout.CENTER);
        add(p2, BorderLayout.SOUTH);
        
        pack();
        if (getHeight() > 400) {
            setSize(getWidth(), 300);
        }
        if (getWidth() > 500) {
            setSize(400, getHeight());
        }
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void loadContacts()
    {
        if (contacts != null) {
            return;
        }
        Comparator comp = new CComp();
        this.contacts = new Vector<Contact>();
        try {
            BufferedReader bin = new BufferedReader(new FileReader(CONTACTS_FILE));
            String line = bin.readLine();
            while ((line = bin.readLine()) != null) {
                String [] cd = line.split(",");
                contacts.add(new Contact(cd[0], cd[1], cd[2], cd[3], new Key(cd[4], cd[5])));
            }
            bin.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        Collections.sort(contacts, comp);
    }
    
    public String toString()
    {
        String str = new String();
        boolean first = false;
        for (int i = 0; i < cb.length; i++) {
            if (cb[i].isSelected()) {
                if (first) {
                    str += ", ";
                }else {
                    first = true;
                }
                str += contacts.elementAt(i).getEmail();
//                 str += "\"" + contacts.elementAt(i).getName() + "\" <" + contacts.elementAt(i).getEmail() + ">";
            }
        }
        return str;
    }
    
    public void actionPerformed(ActionEvent ae)
    {
        if (this.ac == TO) {
            this.parent.setTo(toString());
        }else if (this.ac == CC) {
            this.parent.setCC(toString());
        }else if (this.ac == BCC) {
            this.parent.setBCC(toString());
        }
        setVisible(false);
    }
}

final class CComp implements Comparator<Contact>
{
    public int compare(Contact c, Contact d)
    {
        return c.getName().compareToIgnoreCase(d.getName());
    }
}

        