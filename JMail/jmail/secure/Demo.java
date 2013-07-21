package jmail.secure;

import java.math.BigInteger;
import java.io.*;

class Demo
{
    private static final int ENC_BUFFER_SIZE = 128;//1024;
    
//     static Key publicKey = new Key("85564934370542612084846886385429470646512156667724618158284933081114848978449",
//     "10461337114021426254048641197552673590264591274546359201505078095890918315446098651991845238209218868135022125001728010468782933994751636261335343788198939");
//     
//     static Key privateKey = new Key("7999433792277718789424482888036863461721972356240631732776712994103297521753375463500881903338489512390499818577984571700067200807133750612719195512526249",
//     "10461337114021426254048641197552673590264591274546359201505078095890918315446098651991845238209218868135022125001728010468782933994751636261335343788198939");
    
    static Key publicKey = new Key("105539069795668187301843347782219979272387677939278506091967032548993015773713",
                                "12246641146915474200483271191099148040739686057436874659738475164035619744233809189365535762694662368043130950787602085247679515959777934951414238669598269");
                                
    static Key privateKey = new Key("8816387758261998980974938635380442968614859316780555092360770607170562318742978761879563516716577615772176041459778690387015593788563876859857006715255597", 
                                "12246641146915474200483271191099148040739686057436874659738475164035619744233809189365535762694662368043130950787602085247679515959777934951414238669598269");
//     
//     private static void encrypt(InputStream in, File f) throws Exception
//     {
//         FileOutputStream fout = new FileOutputStream(f);
//         ObjectOutputStream out = new ObjectOutputStream(fout);
//         byte [] buff = new byte[ENC_BUFFER_SIZE];
//         while (in.read(buff) != -1) {
// //             System.out.println(buff.length);
// //             printArray(buff);
//             BigInteger b = RSA.encrypt(new BigInteger(buff), publicKey);
// //             BigInteger bi = RSA.decrypt(b, privateKey);
// //             byte [] outBuff = b.toByteArray();
// //             System.out.println("*");
// //             printArray(buff);
//             
//             System.out.println(b + "|");
//             out.writeObject(b);
//         }
//         fout.close();
//         out.close();
//         in.close();
//     }
//     
//     private static void decrypt(InputStream is, File f) throws Exception
//     {
//         FileOutputStream fout = new FileOutputStream(f);
//         ObjectInputStream in = new ObjectInputStream(is);
//         
//         byte [] outBuff = new byte[ENC_BUFFER_SIZE];
//         BigInteger b;
//         while (true) {
//             try {
//                 b = (BigInteger) in.readObject();
//                 System.out.println(b + "|");
//             }catch (Exception e) {
//                 fout.close();
//                 in.close();
//                 is.close();
//                 return;
//             }
//             BigInteger bi = RSA.decrypt(b, privateKey);
//             outBuff = bi.toByteArray();
// //             System.out.println(outBuff.length);
//             printArray(outBuff);
//             fout.write(outBuff);
//         }
//         
//     }
    
    private static void encrypt(InputStream in, File f) throws Exception
    {
//         FileOutputStream fout = new FileOutputStream(out);
//         FileInputStream  in = new FileInputStream(is);
        ObjectOutputStream oout = new ObjectOutputStream(new FileOutputStream(f));
        
//         byte [] outBuff = new byte[ENC_BUFFER_SIZE];
        String line = new String();
        byte [] buff = new byte[ENC_BUFFER_SIZE];
        int len = 0;
        while ((len = in.read(buff)) != -1) {
            BigInteger c = new BigInteger(1, buff);
            boolean flag = false;
            if (c.compareTo(BigInteger.ZERO) < 0) {
                c = c.abs();
                flag = true;
            }
            BigInteger b = RSA.encrypt(c, publicKey);
            BigInt bigInt = new BigInt(b, flag, len);
            
//             oout.writeObject(b);
//             oout.writeBoolean(flag);
            oout.writeObject(bigInt);
        }
        oout.close();
        in.close();
    }
    
    private static void decrypt(File is, File f) throws Exception
    {
        FileOutputStream fout = new FileOutputStream(f);
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(is));
        
        byte [] outBuff = new byte[ENC_BUFFER_SIZE];
        BigInt big;
        BigInteger b;
        boolean flag;
        String line = new String();
        while (true) {
            try {
//                 b = (BigInteger) in.readObject();
//                 flag = in.readBoolean();
                big = (BigInt) in.readObject();
//                 System.out.println(b + "|");
            }catch (Exception e) {
                fout.close();
                in.close();
//                 e.printStackTrace();
                return;
            }
            b = big.getBigInteger();
            BigInteger bi = RSA.decrypt(b, privateKey);
            if (big.getNotFlag()) {
                bi = bi.not();
            }
            outBuff = bi.toByteArray();
            outBuff = leadZero(outBuff, big.getLength());
//             if (big.getNotFlag()) {
//                 notArray(outBuff);
//             }
            
            fout.write(outBuff);
        }
    }
    
    static void fileComp(File f, File g) throws Exception
    {
        FileInputStream fin = new FileInputStream(f);
        FileInputStream gin = new FileInputStream(g);
        int x, y;
        int i = 0, j = 0;
        while ((x = fin.read()) != -1 && (y = gin.read()) != -1) {
            i++;
            if (x != y) {
                System.out.println(i + ": " + x + "\t" + y);
                j++;
            }
        }
        fin.close();
        gin.close();
        System.out.println("Number of mismatches: " + j);
    }
    
    static byte[] leadZero(byte [] arr, int length)
    {
        int diff = length - arr.length;
        if (diff <= 0) {
            return arr;
        }
        byte [] b = new byte[length];
        int i = 0;
        for ( ; i < diff; i++) {
            b[i] = 0;
        }
        for (int j = 0; j < arr.length; j++) {
            b[i++] = arr[j];
        }
        return b;
    }
    
    static boolean equals(byte [] a, byte [] b)
    {
        if (a.length == b.length) {
            for (int i = 0; i < a.length; i++) {
                if (a[i] != b[i]) {
//                     System.out.println("" + i);
                    return false;
                }
            }
            return true;
        }
//         System.out.println(a.length + " != " + b.length);
        return false;
    }
    
    static BigInteger RSAencrypt(BigInteger b, Key k)
    {
        return b.modPow(k.getExponent(), k.getModulus());
    }
    
    static BigInteger RSAdecrypt(BigInteger b, Key k)
    {
        return b.modPow(k.getExponent(), k.getModulus());
    }
    
//     private static void crypt(File is, File f, File out) throws Exception
//     {
//         FileOutputStream fout = new FileOutputStream(out);
//         FileInputStream  in = new FileInputStream(is);
//         ObjectOutputStream oout = new ObjectOutputStream(new FileOutputStream(f));
// //         PrintWriter pout = new PrintWriter(new BufferedWriter(new FileWriter(f)));
//         
//         byte [] outBuff = new byte[ENC_BUFFER_SIZE];
//         String line = new String();
//         byte [] buff = new byte[ENC_BUFFER_SIZE];
//         
//         while (in.read(buff) != -1) {
//             BigInteger d = new BigInteger(buff);
//             BigInteger c = new BigInteger(buff);
//             boolean flag = false;
//             if (d.compareTo(BigInteger.ZERO) < 0) {
//                 c = c.abs();
//                 flag = true;
//             }
//             BigInteger b = RSA.encrypt(c, publicKey);
//             BigInt bigInt = new BigInt(b, flag);
//             
//             BigInteger bi = RSA.decrypt(b, privateKey);
//             outBuff = bi.toByteArray();
//             outBuff = leadZero(outBuff);
//             if (bigInt.getNotFlag()) {
//                 notArray(outBuff);
//             }
//             
//             oout.writeObject(bigInt);
// //             printArray(pout, buff);
// //             printArray(pout, outBuff);
//             
// //             pout.println(c);
// //             pout.println(bi);
// //             pout.println();
// //             System.out.println(bi + "|\n" + c + "\n");
// //             if (!bi.equals(c)) {
// //                 System.out.println(false);
// //             }else {
// //                 System.out.println(true);
// //             }
//             if (!equals(outBuff, buff)) {
//                 System.out.println(false);
//                 System.out.println(bi);
// //                 printArray(buff);
// //                 printArray(outBuff);
//             }else {
//                 System.out.println(true);
//             }
//             fout.write(outBuff);
//         }
//         oout.close();
//         fout.close();
//         in.close();
//     }
    
    static void notArray(byte [] b)
    {
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte) ~b[i];
            if (i >= b.length - 1) {
                b[i]++;
            }
        }
    }
    
    static void printArray(byte [] b)
    {
        for (int i = 0; i < b.length; i++) {
            System.out.print(b[i] + "\t");
        }
        System.out.println();
    }
    
    static void printArray(PrintWriter pout, byte [] b)
    {
        for (int i = 0; i < b.length; i++) {
            pout.print(b[i] + ", ");
        }
        pout.println();
    }
    
    public static void main(String [] args) throws Exception
    {
//         RSA rsa = new RSA();
// //         RSA rsa = new RSA(256, 256);
// //         Key privateKey = rsa.getPrivateKey();
// //         Key publicKey = rsa.getPublicKey();
//         int n = 20;
// //         String msg = "Hello world!";
//         String msg = "Hello world!\nI am Chitti - the Robo.\nSpeed: 1 terahertz; Memory: 1 zetabyte.";
// //         System.out.println(rsa.getPublicKey());
// //         System.out.println(rsa.getPrivateKey());
//         
       
//                                 
//         
//         BigInteger m = new BigInteger(msg.getBytes());
//         BigInteger c = rsa.encrypt(m, publicKey);
//         System.out.println("Plaintext: " + m);
// //         BigInteger c = rsa.encrypt(m, new BigInteger("" + n), publicKey);
// //         String s = new String(rsa.decrypt(c, new BigInteger("" + n), privateKey).toByteArray());
//         BigInteger dm = rsa.decrypt(c, privateKey);
//         System.out.println("Decoded:   " + dm);
//         String s = new String(dm.toByteArray());
//         System.out.println(s);
//         Key k = new Key("4d55", "6cd5");
//         System.out.println(k);

        System.out.println("Encrypting...");
        File fin = new File("syllabus.pdf");//"demo.mp3");//"syllabus.pdf");//pdfresult.pdf");//"w3c-vcss.gif");
        File ftemp = new File("temp");
        File fout = new File("decrypted.pdf");//"dec.mp3");//"decrypted.pdf");//("decrypted.gif");
//         crypt(fin, ftemp, fout);
        encrypt(new FileInputStream(fin), ftemp);
//         System.out.println("  * * * ");
        decrypt(ftemp, fout);
        
        System.out.println("Comparing files...");
//         fileComp(fin, fout);
//         crypt(fin, ftemp, fout);
    }
}
