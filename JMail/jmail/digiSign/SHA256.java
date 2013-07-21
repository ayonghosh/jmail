package jmail.digiSign;


import java.io.*;

public class SHA256 implements jmail.Defaults
{
    private static final int[] k = {
       0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5, 0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5,
       0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3, 0x72be5d74, 0x80deb1fe, 0x9bdc06a7, 0xc19bf174,
       0xe49b69c1, 0xefbe4786, 0x0fc19dc6, 0x240ca1cc, 0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da,
       0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7, 0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967,
       0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13, 0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85,
       0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3, 0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070,
       0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3,
       0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208, 0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2
    };


    private static int rightrotate(int x, int n)
    {
        return (x >>> n) | (x << (32 - n));
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
        
        int h0 = 0x6a09e667;
        int h1 = 0xbb67ae85;
        int h2 = 0x3c6ef372;
        int h3 = 0xa54ff53a;
        int h4 = 0x510e527f;
        int h5 = 0x9b05688c;
        int h6 = 0x1f83d9ab;
        int h7 = 0x5be0cd19;

        
        for (int ii = 0; ii < wc; ii += 16) {
            int w[] = new int[64];
            for (int i = 0; i < 16; i++) {
                w[i] = word[ii + i];
            }
            for (int i = 16; i < 64; i++) {
                int s0 = rightrotate(w[i - 15], 7) ^ rightrotate(w[i - 15], 18) ^ (w[i - 15] >>> 3);
                int s1 = rightrotate(w[i - 2], 17) ^ rightrotate(w[i - 2], 19) ^ (w[i-2] >>> 10);
                w[i] = w[i - 16] + s0 + w[i - 7] + s1;
            }
            
            int a = h0;
            int b = h1;
            int c = h2;
            int d = h3;
            int e = h4;
            int f = h5;
            int g = h6;
            int h = h7;

            for (int i = 0; i < 64; i++) {
                int s0 = rightrotate(a, 2) ^ rightrotate(a, 13) ^ rightrotate(a, 22);
                int maj = (a & b) ^ (a & c) ^ (b & c);
                int t2 = s0 + maj;
                int s1 = rightrotate(e, 6) ^ rightrotate(e, 11) ^ rightrotate(e, 25);
                int ch = (e & f) ^ (~e & g);
                int t1 = h + s1 + ch + k[i] + w[i];
        
                h = g;
                g = f;
                f = e;
                e = (d + t1) & 0xffffffff;
                d = c;
                c = b;
                b = a;
                a = (t1 + t2) & 0xffffffff;
            }
            h0 = (h0 + a) & 0xffffffff;
            h1 = (h1 + b) & 0xffffffff;
            h2 = (h2 + c) & 0xffffffff;
            h3 = (h3 + d) & 0xffffffff;
            h4 = (h4 + e) & 0xffffffff;
            h5 = (h5 + f) & 0xffffffff;
            h6 = (h6 + g) & 0xffffffff;
            h7 = (h7 + h) & 0xffffffff;
        }
        return (LSBHex(h0) + LSBHex(h1) + LSBHex(h2) + LSBHex(h3) + LSBHex(h4) + LSBHex(h5) + LSBHex(h6) + LSBHex(h7)); // return output as a string
    }
    
    public static String hash(InputStream msgStream) throws IOException
    {
        int n;
        int msgLen = 0;
        byte [] msg = new byte[BLOCK_SIZE];
        int h0 = 0, h1 = 0, h2 = 0, h3 = 0, h4 = 0, h5 = 0, h6 = 0, h7 = 0;
        
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
            }
            
            int x[] = new int[16];
            
            h0 = 0x6a09e667;
            h1 = 0xbb67ae85;
            h2 = 0x3c6ef372;
            h3 = 0xa54ff53a;
            h4 = 0x510e527f;
            h5 = 0x9b05688c;
            h6 = 0x1f83d9ab;
            h7 = 0x5be0cd19;
    
            
            for (int ii = 0; ii < wc; ii += 16) {
                int w[] = new int[64];
                for (int i = 0; i < 16; i++) {
                    w[i] = word[ii + i];
                }
                for (int i = 16; i < 64; i++) {
                    int s0 = rightrotate(w[i - 15], 7) ^ rightrotate(w[i - 15], 18) ^ (w[i - 15] >>> 3);
                    int s1 = rightrotate(w[i - 2], 17) ^ rightrotate(w[i - 2], 19) ^ (w[i-2] >>> 10);
                    w[i] = w[i - 16] + s0 + w[i - 7] + s1;
                }
                
                int a = h0;
                int b = h1;
                int c = h2;
                int d = h3;
                int e = h4;
                int f = h5;
                int g = h6;
                int h = h7;
    
                for (int i = 0; i < 64; i++) {
                    int s0 = rightrotate(a, 2) ^ rightrotate(a, 13) ^ rightrotate(a, 22);
                    int maj = (a & b) ^ (a & c) ^ (b & c);
                    int t2 = s0 + maj;
                    int s1 = rightrotate(e, 6) ^ rightrotate(e, 11) ^ rightrotate(e, 25);
                    int ch = (e & f) ^ (~e & g);
                    int t1 = h + s1 + ch + k[i] + w[i];
            
                    h = g;
                    g = f;
                    f = e;
                    e = (d + t1) & 0xffffffff;
                    d = c;
                    c = b;
                    b = a;
                    a = (t1 + t2) & 0xffffffff;
                }
                h0 = (h0 + a) & 0xffffffff;
                h1 = (h1 + b) & 0xffffffff;
                h2 = (h2 + c) & 0xffffffff;
                h3 = (h3 + d) & 0xffffffff;
                h4 = (h4 + e) & 0xffffffff;
                h5 = (h5 + f) & 0xffffffff;
                h6 = (h6 + g) & 0xffffffff;
                h7 = (h7 + h) & 0xffffffff;
            }
        }
        return (LSBHex(h0) + LSBHex(h1) + LSBHex(h2) + LSBHex(h3) + LSBHex(h4) + LSBHex(h5) + LSBHex(h6) + LSBHex(h7)); // return output as a string
    }
    
    public static void main(String [] args) throws Exception
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter some text: ");
        String text = br.readLine();
        byte [] msg = text.getBytes();
        
        String sha1 = hash(msg);
        
        System.out.println(sha1);
    }
}
