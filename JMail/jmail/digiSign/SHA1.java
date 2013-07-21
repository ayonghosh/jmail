package jmail.digiSign;


import java.io.*;

public class SHA1
{
    private static final int BLOCK_SIZE = 64;
    
    private static int leftrotate(int x, int c)
    {
        return ((x << c) | (x >>> (32 - c)));
    }
    
    private static String LSBHex(int n)
    {
        String s = "";
        int v;
        for (int i = 7; i >= 0; i--) {
            v = (n >>> ( i * 4)) & 0xf;
            s += Integer.toHexString(v);
        }
        return s;
    }
    
    public static String hash(byte [] msg)
    {
       int word[] = new int[msg.length + 64];  // assign maximum possible length
        int wc = 0; // length of the word array
        for (int i = 0; i < msg.length - 3; i += 4) {
            word[wc++] = (msg[i] << 24) | (msg[i + 1] << 16) | (msg[i + 2] << 8) | msg[i + 3];
//             word[wc++] = msg[i] | msg[i + 1] << 8 | msg[i + 2] << 16 | msg[i + 3] << 24;
        }
        
        int p = 0;
        switch (msg.length % 4) {
            case 0: // text length was a multiple of 4 bytes, start padding
            p = 0x80000000; // 4 bytes padding
            break;
    
            case 1:
            // one byte of text left
            p = msg[msg.length - 1] << 24 | 0x800000;
            break;
    
            case 2:
            // two bytes of text left
            p = msg[msg.length - 2] << 24 | msg[msg.length - 1] << 16 | 0x8000;
            break;
    
            case 3:
            // three bytes of text left
            p = msg[msg.length - 3] << 24 | msg[msg.length - 2] << 16 | msg[msg.length - 1] << 8 | 0x80;
            break;
        }
        word[wc++] = p;
        
        while (wc % 16 != 14) { // pad remaining with zeros
            word[wc++] = 0;
        }
        
        word[wc++] = (int) (Math.floor((msg.length) * 8) / Math.pow(2, 32));
        word[wc++] = ((msg.length) * 8) & 0xffffffff;
        
        int x[] = new int[16];
        
        int h0 = 0x67452301;
        int h1 = 0xEFCDAB89;
        int h2 = 0x98BADCFE;
        int h3 = 0x10325476;
        int h4 = 0xC3D2E1F0;
        
        for (int ii = 0; ii < wc; ii += 16) {
            int w[] = new int[80];
            for (int i = 0; i < 16; i++) {
                w[i] = word[ii + i];
            }
            for (int i = 16; i < 80; i++) {
                w[i] = leftrotate((w[i - 3] ^ w[i - 8] ^ w[i - 14] ^ w[i - 16]), 1);
            }
            
            int a = h0;
            int b = h1;
            int c = h2;
            int d = h3;
            int e = h4;
            
            int f, k;
            
            for (int i = 0; i < 80; i++) {
                if (i >= 0 && i <= 19) {
                    f = (b & c) | (~b & d);
                    k = 0x5A827999;
                }else if (i >= 20 && i <= 39) {
                    f = b ^ c ^ d;
                    k = 0x6ED9EBA1;
                }else if (i >= 40 && i <= 59) {
                    f = b & c | b & d | c & d;
                    k = 0x8F1BBCDC;
                }else {
                    f = b ^ c ^ d;
                    k = 0xCA62C1D6;
                }
                
                int temp = leftrotate(a, 5) + f + e + k + w[i] & 0xffffffff;
                e = d;
                d = c;
                c = leftrotate(b, 30);
                b = a;
                a = temp;
            }
            h0 = (h0 + a) & 0xffffffff;
            h1 = (h1 + b) & 0xffffffff;
            h2 = (h2 + c) & 0xffffffff;
            h3 = (h3 + d) & 0xffffffff;
            h4 = (h4 + e) & 0xffffffff;
        }
        return (LSBHex(h0) + LSBHex(h1) + LSBHex(h2) + LSBHex(h3) + LSBHex(h4)); // return output as a string
    }
    
    public static String hash(InputStream msgStream) throws Exception
    {
        int n;
        int msgLen = 0;
        byte [] msg = new byte[BLOCK_SIZE];
        int h0 = 0, h1 = 0, h2 = 0, h3 = 0, h4 = 0;
        
        while ((n = msgStream.read(msg)) != -1) {
            msgLen += n;
            
            int word[] = new int[n + 64];  // assign maximum possible length
            int wc = 0; // length of the word array
            
            for (int i = 0; i < n - 3; i += 4) {
                word[wc++] = (msg[i] << 24) | (msg[i + 1] << 16) | (msg[i + 2] << 8) | msg[i + 3];
            }
            
            if (n < BLOCK_SIZE) {
                int p = 0;
                switch (n % 4) {
                    case 0: // text length was a multiple of 4 bytes, start padding
                    p = 0x80000000; // 4 bytes padding
                    break;
            
                    case 1:
                    // one byte of text left
                    p = msg[n - 1] << 24 | 0x800000;
                    break;
            
                    case 2:
                    // two bytes of text left
                    p = msg[n - 2] << 24 | msg[n - 1] << 16 | 0x8000;
                    break;
            
                    case 3:
                    // three bytes of text left
                    p = msg[n - 3] << 24 | msg[n - 2] << 16 | msg[n - 1] << 8 | 0x80;
                    break;
                }
                word[wc++] = p;
                    
                
                while (wc % 16 != 14) { // pad remaining with zeros
                    word[wc++] = 0;
                }
                
                word[wc++] = (int) (Math.floor((msgLen) * 8) / Math.pow(2, 32));
                word[wc++] = ((msgLen) * 8) & 0xffffffff;
            }else {
                
            }
                
            
            int x[] = new int[16];
            
            h0 = 0x67452301;
            h1 = 0xEFCDAB89;
            h2 = 0x98BADCFE;
            h3 = 0x10325476;
            h4 = 0xC3D2E1F0;
            
            for (int ii = 0; ii < wc; ii += 16) {
                int w[] = new int[80];
                for (int i = 0; i < 16; i++) {
                    w[i] = word[ii + i];
                }
                for (int i = 16; i < 80; i++) {
                    w[i] = leftrotate((w[i - 3] ^ w[i - 8] ^ w[i - 14] ^ w[i - 16]), 1);
                }
                
                int a = h0;
                int b = h1;
                int c = h2;
                int d = h3;
                int e = h4;
                
                int f, k;
                
                for (int i = 0; i < 80; i++) {
                    if (i >= 0 && i <= 19) {
                        f = (b & c) | (~b & d);
                        k = 0x5A827999;
                    }else if (i >= 20 && i <= 39) {
                        f = b ^ c ^ d;
                        k = 0x6ED9EBA1;
                    }else if (i >= 40 && i <= 59) {
                        f = b & c | b & d | c & d;
                        k = 0x8F1BBCDC;
                    }else {
                        f = b ^ c ^ d;
                        k = 0xCA62C1D6;
                    }
                    
                    int temp = leftrotate(a, 5) + f + e + k + w[i] & 0xffffffff;
                    e = d;
                    d = c;
                    c = leftrotate(b, 30);
                    b = a;
                    a = temp;
                }
                h0 = (h0 + a) & 0xffffffff;
                h1 = (h1 + b) & 0xffffffff;
                h2 = (h2 + c) & 0xffffffff;
                h3 = (h3 + d) & 0xffffffff;
                h4 = (h4 + e) & 0xffffffff;
            }
        }
        return (LSBHex(h0) + LSBHex(h1) + LSBHex(h2) + LSBHex(h3) + LSBHex(h4)); // return output as a string
    }
    
    public static void main(String [] args) throws Exception
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter filename: ");
        String file = br.readLine();
//         byte [] msg = text.getBytes();
        
        String sha1 = hash(new FileInputStream(file));
        
        System.out.println(sha1);
    }
}
