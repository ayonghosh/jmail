package jmail.secure;

import jmail.base64.*;
import java.io.*;
import java.math.BigInteger;

public class Codec implements jmail.Defaults
{
//     public static void encrypt2(InputStream src, File dst, Key publicKey) throws Exception
//     {
//         ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(dst));
//         byte [] buff = new byte[ENC_BUFFER_SIZE];
//         while (src.read(buff) != -1) {
//             int signum = 0;
//             if (buff[0] < 0) {
//                 signum  = 1;
//             }
//             BigInteger big = RSA.encrypt(new BigInteger(1, buff), publicKey);
//             out.writeObject(big);
//         }
//         out.close();
//         src.close();
//     }
//     
//     public static void decrypt2(File src, File dst, Key privateKey) throws Exception
//     {
//         ObjectInputStream in = new ObjectInputStream(new FileInputStream(src));
//         FileOutputStream out = new FileOutputStream(dst);
//         
//         BigInteger big;
//         while (true) {
//             try {
//                 big = (BigInteger) in.readObject();
//                 byte [] buff = RSA.decrypt(big, privateKey).toByteArray();
//                 out.write(buff);
//             }catch (Exception e) {
//                 in.close();
//                 break;
//             }
//         }
//         out.close();
//     }
    
    
    public static void encrypt(InputStream src, File dst, Key publicKey) throws Exception
    {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(dst));
        File tmp = new File("encoded.b64");
        Base64.encode(src, new FileOutputStream(tmp), B64_WRAP_AT);
        
        BufferedReader bin = new BufferedReader(new FileReader(tmp));
        String line = new String();
        while ((line = bin.readLine()) != null) {
            BigInteger big = RSA.encrypt(new BigInteger(line.getBytes()), publicKey);
            
            out.writeObject(big);
        }
        bin.close();
        out.close();
        tmp.delete();
    }
    
    public static void decrypt(File src, File dst, Key privateKey) throws Exception
    {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(src));
        File tmp = new File("encoded.b64");
        PrintWriter pout = new PrintWriter(new BufferedWriter(new FileWriter(tmp)));
        
        BigInteger big;
        while (true) {
            try {
                big = (BigInteger) in.readObject();
                pout.println(new String(RSA.decrypt(big, privateKey).toByteArray()));
            }catch (Exception e) {
                in.close();
                break;
            }
        }
        pout.close();
        Base64.decode(tmp, dst);
        tmp.delete();
    }
    
    public static void main(String [] args) throws Exception
    {
        Key publicKey = new Key("105539069795668187301843347782219979272387677939278506091967032548993015773713",
                                "12246641146915474200483271191099148040739686057436874659738475164035619744233809189365535762694662368043130950787602085247679515959777934951414238669598269");
                                
        Key privateKey = new Key("8816387758261998980974938635380442968614859316780555092360770607170562318742978761879563516716577615772176041459778690387015593788563876859857006715255597", 
                                "12246641146915474200483271191099148040739686057436874659738475164035619744233809189365535762694662368043130950787602085247679515959777934951414238669598269");
    
        File src = new File("syllabus.pdf");
        File enc = new File("encrypted.rsa");
//         Base64.encode(src, enc, WRAP_AT);
        
        File dec = new File("decoded.pdf");
//         Base64.decode(enc, dec);

//         encrypt2(new FileInputStream(src), enc, publicKey);
//         decrypt2(enc, dec, privateKey);
    }
}
