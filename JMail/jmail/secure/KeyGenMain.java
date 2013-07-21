package jmail.secure;


import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import jmail.*;
import javax.swing.border.*;
import java.math.BigInteger;

public class KeyGenMain extends JFrame implements ActionListener
{
    private JTextField publicE;
    private JTextField publicM;
    private JTextField privateE;
    private JTextField privateM;
    
    private JTextField primeBitSize;
    private JTextField expBitSize;
    
    KeyGenMain()
    {
        super("KeyGen");
        setLayout(new BorderLayout());
        
        JPanel p2 = new JPanel();
        p2.setLayout(new GridLayout(0, 1));
        
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(0, 2));
        p.add(new JLabel("Average bit size of primes:"));
        p.add(primeBitSize = Factory.createTextField(null));
        primeBitSize.setText("1024");
        p.add(new JLabel("Bit size of public key exponent:"));
        p.add(expBitSize = Factory.createTextField(null));
        expBitSize.setText("1024");
        p.setBorder(new TitledBorder("Bit Length Options"));
        
        
        JPanel pub = new JPanel();
        pub.setLayout(new GridLayout(0, 1));
        pub.add(new JLabel("Exponent (base " + Defaults.KEY_BASE + "):"));
        pub.add(publicE = Factory.createTextField(null));
        pub.add(new JLabel("Modulus (base " + Defaults.KEY_BASE + "):"));
        pub.add(publicM = Factory.createTextField(null));
        pub.setBorder(new TitledBorder("Public Key"));
        
        JPanel pri = new JPanel();
        pri.setLayout(new GridLayout(0, 1));
        pri.add(new JLabel("Exponent (base " + Defaults.KEY_BASE + "):"));
        pri.add(privateE = Factory.createTextField(null));
        pri.add(new JLabel("Modulus (base " + Defaults.KEY_BASE + "):"));
        pri.add(privateM = Factory.createTextField(null));
        pri.setBorder(new TitledBorder("Private Key"));
        
        JPanel b = new JPanel();
        b.setLayout(new FlowLayout(FlowLayout.CENTER));
        b.add(Factory.createButton("Generate Keys", this));
        
        
        p2.add(pub);
        p2.add(pri);
        
        add(p, BorderLayout.NORTH);
        add(p2, BorderLayout.CENTER);
        add(b, BorderLayout.SOUTH);
        
        setLookAndFeel(Defaults.DEFAULT_LAF);
        pack();
        setSize(500, getHeight());
//         setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultLookAndFeelDecorated(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    private void setLookAndFeel(String laf)
    {
        try {
            UIManager.setLookAndFeel(laf);
            SwingUtilities.updateComponentTreeUI(this);
        }catch (Exception e) {
        }
    }
    
    public void actionPerformed(ActionEvent ae)
    {
        if (ae.getActionCommand().equalsIgnoreCase("generate keys")) {
            int n = 0;
            int en = 0;
            try {
                n = Integer.parseInt(primeBitSize.getText().trim());
                en = Integer.parseInt(expBitSize.getText().trim());
            }catch (Exception e) {
                
            }
            KeyGen keyGen = new KeyGen(n, en);
            Key pri = keyGen.getPrivateKey();
            Key pub = keyGen.getPublicKey();
            
            publicE.setText(pub.getExponent().toString(Defaults.KEY_BASE));
            publicM.setText(pub.getModulus().toString(Defaults.KEY_BASE));
            
            privateE.setText(pri.getExponent().toString(Defaults.KEY_BASE));
            privateM.setText(pri.getModulus().toString(Defaults.KEY_BASE));
        }
    }
    
    public static void main(String [] args)
    {
        KeyGenMain kg = new KeyGenMain();
//         kg.setLookAndFeel(Defaults.DEFAULT_LAF);
    }
}
