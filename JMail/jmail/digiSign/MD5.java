package jmail.digiSign;


import java.io.*;

// Same as MD5.java, but left shifts on byte converted to int are handled in a safer manner due to implicit casting by Java

public class MD5 implements jmail.Defaults
{
    
    private static final int A = 0x67452301;
    private static final int B = 0xEFCDAB89;
    private static final int C = 0x98BADCFE;
    private static final int D = 0x10325476;
    
    private int length(int msg)
    {
        int len = 0;
        while ((msg <<= 1) != 0) {
            len++;
        }
        return len;
    }
    
    private static String LSBHex(int val)
    {
        String str = "";
        int vh, vl;
    
       for (int i=0; i <= 6; i += 2) {
          vh = (val >>> (i * 4 + 4)) & 0x0f;
          vl = (val >>> (i * 4)) & 0x0f;
          str += Integer.toHexString(vh) + Integer.toHexString(vl);
       }
       return str;
    }
    
    private static int leftShift(byte b, int c)
    {
        int r = (toInt(b) << c);
        return r;
    }
    
    private static int toInt(byte b)
    {
        return ((b << 24) >>> 24);
    }
    
    public static String doHash(byte msg[])
    {
        int word[] = new int[msg.length + 64];  // assign maximum possible length
        int wc = 0; // length of the word array
        for (int i = 0; i < msg.length - 3; i += 4) {
            word[wc++] = toInt(msg[i]) | leftShift(msg[i + 1], 8) | leftShift(msg[i + 2], 16) | leftShift(msg[i + 3], 24);
//             System.out.printf("%x\n", word[wc - 1]);
//             word[wc++] = msg[i] | msg[i + 1] << 8 | msg[i + 2] << 16 | msg[i + 3] << 24;
        }
        
        int p = 0;
        switch (msg.length % 4) {
            case 0: // text length was a multiple of 4 bytes, start padding
            p = 0x80; // 4 bytes padding
            break;
    
            case 1:
            // one byte of text left
            p = toInt(msg[msg.length - 1]) | 0x8000;
//             p = msg[msg.length - 1] | 0x8000; // 3 bytes padding
            break;
    
            case 2:
            // two bytes of text left
            p = toInt(msg[msg.length - 2]) | leftShift(msg[msg.length - 1], 8) | 0x800000;
//             p = msg[msg.length - 2] | msg[msg.length - 1] << 8 | 0x800000; // 2 bytes padding
            break;
    
            case 3:
            // three bytes of text left
            p = toInt(msg[msg.length - 3]) | leftShift(msg[msg.length - 2], 8) | leftShift(msg[msg.length - 1], 16) | 0x80000000;
//             p = msg[msg.length - 3] | msg[msg.length - 2] << 8 | msg[msg.length - 1] << 16 | 0x80000000; // 1 byte padding
            break;
        }
        word[wc++] = p;
//         System.out.printf("Padding: %x\n", p);
        
        while (wc % 16 != 14) {
            word[wc++] = 0;
        }

        word[wc++] = (msg.length << 3) & 0xffffffff;
        word[wc++] =  msg.length >>> 29;
        
        int a = A;
        int b = B;
        int c = C;
        int d = D;
        
        int aa, bb, cc, dd;
        int x[] = new int[16];
        
        for (int i = 0; i < wc; i += 16 ) {
            // Copy block i into X.
            for (int j = 0; j < 16; j++) {
                x[j] = word[i + j];
//                 System.out.printf(" : %x\n", x[j]);
            }
        
            // Save A as AA, B as BB, C as CC, and D as DD.
            aa = a;
            bb = b;
            cc = c;
            dd = d;
            
            
            a = FF (a, b, c, d, x[ 0],   7, 0xd76aa478); /* 1 */
            d = FF (d, a, b, c, x[ 1],  12, 0xe8c7b756); /* 2 */
            c = FF (c, d, a, b, x[ 2],  17, 0x242070db); /* 3 */
            b = FF (b, c, d, a, x[ 3],  22, 0xc1bdceee); /* 4 */
            a = FF (a, b, c, d, x[ 4],   7, 0xf57c0faf); /* 5 */
            d = FF (d, a, b, c, x[ 5],  12, 0x4787c62a); /* 6 */
            c = FF (c, d, a, b, x[ 6],  17, 0xa8304613); /* 7 */
            b = FF (b, c, d, a, x[ 7],  22, 0xfd469501); /* 8 */
            a = FF (a, b, c, d, x[ 8],   7, 0x698098d8); /* 9 */
            d = FF (d, a, b, c, x[ 9],  12, 0x8b44f7af); /* 10 */
            c = FF (c, d, a, b, x[10],  17, 0xffff5bb1); /* 11 */
            b = FF (b, c, d, a, x[11],  22, 0x895cd7be); /* 12 */
            a = FF (a, b, c, d, x[12],   7, 0x6b901122); /* 13 */
            d = FF (d, a, b, c, x[13],  12, 0xfd987193); /* 14 */
            c = FF (c, d, a, b, x[14],  17, 0xa679438e); /* 15 */
            b = FF (b, c, d, a, x[15],  22, 0x49b40821); /* 16 */
        
            /* Round 2 */
            a = GG (a, b, c, d, x[ 1],   5, 0xf61e2562); /* 17 */
            d = GG (d, a, b, c, x[ 6],   9, 0xc040b340); /* 18 */
            c = GG (c, d, a, b, x[11],  14, 0x265e5a51); /* 19 */
            b = GG (b, c, d, a, x[ 0],  20, 0xe9b6c7aa); /* 20 */
            a = GG (a, b, c, d, x[ 5],   5, 0xd62f105d); /* 21 */
            d = GG (d, a, b, c, x[10],   9, 0x02441453); /* 22 */
            c = GG (c, d, a, b, x[15],  14, 0xd8a1e681); /* 23 */
            b = GG (b, c, d, a, x[ 4],  20, 0xe7d3fbc8); /* 24 */
            a = GG (a, b, c, d, x[ 9],   5, 0x21e1cde6); /* 25 */
            d = GG (d, a, b, c, x[14],   9, 0xc33707d6); /* 26 */
            c = GG (c, d, a, b, x[ 3],  14, 0xf4d50d87); /* 27 */
            b = GG (b, c, d, a, x[ 8],  20, 0x455a14ed); /* 28 */
            a = GG (a, b, c, d, x[13],   5, 0xa9e3e905); /* 29 */
            d = GG (d, a, b, c, x[ 2],   9, 0xfcefa3f8); /* 30 */
            c = GG (c, d, a, b, x[ 7],  14, 0x676f02d9); /* 31 */
            b = GG (b, c, d, a, x[12],  20, 0x8d2a4c8a); /* 32 */
        
            /* Round 3 */
            a = HH (a, b, c, d, x[ 5],   4, 0xfffa3942); /* 33 */
            d = HH (d, a, b, c, x[ 8],  11, 0x8771f681); /* 34 */
            c = HH (c, d, a, b, x[11],  16, 0x6d9d6122); /* 35 */
            b = HH (b, c, d, a, x[14],  23, 0xfde5380c); /* 36 */
            a = HH (a, b, c, d, x[ 1],   4, 0xa4beea44); /* 37 */
            d = HH (d, a, b, c, x[ 4],  11, 0x4bdecfa9); /* 38 */
            c = HH (c, d, a, b, x[ 7],  16, 0xf6bb4b60); /* 39 */
            b = HH (b, c, d, a, x[10],  23, 0xbebfbc70); /* 40 */
            a = HH (a, b, c, d, x[13],   4, 0x289b7ec6); /* 41 */
            d = HH (d, a, b, c, x[ 0],  11, 0xeaa127fa); /* 42 */
            c = HH (c, d, a, b, x[ 3],  16, 0xd4ef3085); /* 43 */
            b = HH (b, c, d, a, x[ 6],  23, 0x04881d05); /* 44 */
            a = HH (a, b, c, d, x[ 9],   4, 0xd9d4d039); /* 45 */
            d = HH (d, a, b, c, x[12],  11, 0xe6db99e5); /* 46 */
            c = HH (c, d, a, b, x[15],  16, 0x1fa27cf8); /* 47 */
            b = HH (b, c, d, a, x[ 2],  23, 0xc4ac5665); /* 48 */
        
            /* Round 4 */
            a = II (a, b, c, d, x[ 0],   6, 0xf4292244); /* 49 */
            d = II (d, a, b, c, x[ 7],  10, 0x432aff97); /* 50 */
            c = II (c, d, a, b, x[14],  15, 0xab9423a7); /* 51 */
            b = II (b, c, d, a, x[ 5],  21, 0xfc93a039); /* 52 */
            a = II (a, b, c, d, x[12],   6, 0x655b59c3); /* 53 */
            d = II (d, a, b, c, x[ 3],  10, 0x8f0ccc92); /* 54 */
            c = II (c, d, a, b, x[10],  15, 0xffeff47d); /* 55 */
            b = II (b, c, d, a, x[ 1],  21, 0x85845dd1); /* 56 */
            a = II (a, b, c, d, x[ 8],   6, 0x6fa87e4f); /* 57 */
            d = II (d, a, b, c, x[15],  10, 0xfe2ce6e0); /* 58 */
            c = II (c, d, a, b, x[ 6],  15, 0xa3014314); /* 59 */
            b = II (b, c, d, a, x[13],  21, 0x4e0811a1); /* 60 */
            a = II (a, b, c, d, x[ 4],   6, 0xf7537e82); /* 61 */
            d = II (d, a, b, c, x[11],  10, 0xbd3af235); /* 62 */
            c = II (c, d, a, b, x[ 2],  15, 0x2ad7d2bb); /* 63 */
            b = II (b, c, d, a, x[ 9],  21, 0xeb86d391); /* 64 */
            
            a += aa;
            b += bb;
            c += cc;
            d += dd;
//             System.out.println(LSBHex(a) + LSBHex(b) + LSBHex(c) + LSBHex(d));
        }
        return new String(LSBHex(a) + LSBHex(b) + LSBHex(c) + LSBHex(d));
    }
    
    public static String doHash(InputStream msgStream) throws Exception
    {
        int n;
        int msgLen = 0;
        byte [] msg = new byte[BLOCK_SIZE];
        int a = 0, b = 0, c = 0, d = 0;
        int aa, bb, cc, dd;
        
         while ((n = msgStream.read(msg)) != -1) {
            msgLen += n;
            int word[] = new int[msg.length + 64];  // assign maximum possible length
            int wc = 0; // length of the word array
            for (int i = 0; i < n - 3; i += 4) {
                word[wc++] = msg[i] | msg[i + 1] << 8 | msg[i + 2] << 16 | msg[i + 3] << 24;
            }
            
            if (n < BLOCK_SIZE) {
                int p = 0;
                switch (n % 4) {
                    case 0: // text length was a multiple of 4 bytes, start padding
                    p = 0x80; // 4 bytes padding
                    break;
            
                    case 1:
                    // one byte of text left
                    p = msg[n - 1] | 0x8000; // 3 bytes padding
                    break;
            
                    case 2:
                    // two bytes of text left
                    p = msg[n - 2] | msg[n - 1] << 8 | 0x800000; // 2 bytes padding
                    break;
            
                    case 3:
                    // three bytes of text left
                    p = msg[n - 3] | msg[n - 2] << 8 | msg[n - 1] << 16 | 0x80000000; // 1 byte padding
                    break;
                }
                word[wc++] = p;
                
                while (wc % 16 != 14) { // pad remaining with zeros
                    word[wc++] = 0;
                }
        
                word[wc++] = (msgLen << 3) & 0xffffffff;
                word[wc++] =  msgLen >>> 29;
            }
            
            a = A;
            b = B;
            c = C;
            d = D;
            
            int x[] = new int[16];
            
            for (int i = 0; i < wc; i += 16 ) {
                // Copy block i into X.
                for (int j = 0; j < 16; j++) {
                    x[j] = word[i + j];
                }
            
                // Save A as AA, B as BB, C as CC, and D as DD.
                aa = a;
                bb = b;
                cc = c;
                dd = d;
                
                
                a = FF (a, b, c, d, x[ 0],   7, 0xd76aa478); /* 1 */
                d = FF (d, a, b, c, x[ 1],  12, 0xe8c7b756); /* 2 */
                c = FF (c, d, a, b, x[ 2],  17, 0x242070db); /* 3 */
                b = FF (b, c, d, a, x[ 3],  22, 0xc1bdceee); /* 4 */
                a = FF (a, b, c, d, x[ 4],   7, 0xf57c0faf); /* 5 */
                d = FF (d, a, b, c, x[ 5],  12, 0x4787c62a); /* 6 */
                c = FF (c, d, a, b, x[ 6],  17, 0xa8304613); /* 7 */
                b = FF (b, c, d, a, x[ 7],  22, 0xfd469501); /* 8 */
                a = FF (a, b, c, d, x[ 8],   7, 0x698098d8); /* 9 */
                d = FF (d, a, b, c, x[ 9],  12, 0x8b44f7af); /* 10 */
                c = FF (c, d, a, b, x[10],  17, 0xffff5bb1); /* 11 */
                b = FF (b, c, d, a, x[11],  22, 0x895cd7be); /* 12 */
                a = FF (a, b, c, d, x[12],   7, 0x6b901122); /* 13 */
                d = FF (d, a, b, c, x[13],  12, 0xfd987193); /* 14 */
                c = FF (c, d, a, b, x[14],  17, 0xa679438e); /* 15 */
                b = FF (b, c, d, a, x[15],  22, 0x49b40821); /* 16 */
            
                /* Round 2 */
                a = GG (a, b, c, d, x[ 1],   5, 0xf61e2562); /* 17 */
                d = GG (d, a, b, c, x[ 6],   9, 0xc040b340); /* 18 */
                c = GG (c, d, a, b, x[11],  14, 0x265e5a51); /* 19 */
                b = GG (b, c, d, a, x[ 0],  20, 0xe9b6c7aa); /* 20 */
                a = GG (a, b, c, d, x[ 5],   5, 0xd62f105d); /* 21 */
                d = GG (d, a, b, c, x[10],   9, 0x02441453); /* 22 */
                c = GG (c, d, a, b, x[15],  14, 0xd8a1e681); /* 23 */
                b = GG (b, c, d, a, x[ 4],  20, 0xe7d3fbc8); /* 24 */
                a = GG (a, b, c, d, x[ 9],   5, 0x21e1cde6); /* 25 */
                d = GG (d, a, b, c, x[14],   9, 0xc33707d6); /* 26 */
                c = GG (c, d, a, b, x[ 3],  14, 0xf4d50d87); /* 27 */
                b = GG (b, c, d, a, x[ 8],  20, 0x455a14ed); /* 28 */
                a = GG (a, b, c, d, x[13],   5, 0xa9e3e905); /* 29 */
                d = GG (d, a, b, c, x[ 2],   9, 0xfcefa3f8); /* 30 */
                c = GG (c, d, a, b, x[ 7],  14, 0x676f02d9); /* 31 */
                b = GG (b, c, d, a, x[12],  20, 0x8d2a4c8a); /* 32 */
            
                /* Round 3 */
                a = HH (a, b, c, d, x[ 5],   4, 0xfffa3942); /* 33 */
                d = HH (d, a, b, c, x[ 8],  11, 0x8771f681); /* 34 */
                c = HH (c, d, a, b, x[11],  16, 0x6d9d6122); /* 35 */
                b = HH (b, c, d, a, x[14],  23, 0xfde5380c); /* 36 */
                a = HH (a, b, c, d, x[ 1],   4, 0xa4beea44); /* 37 */
                d = HH (d, a, b, c, x[ 4],  11, 0x4bdecfa9); /* 38 */
                c = HH (c, d, a, b, x[ 7],  16, 0xf6bb4b60); /* 39 */
                b = HH (b, c, d, a, x[10],  23, 0xbebfbc70); /* 40 */
                a = HH (a, b, c, d, x[13],   4, 0x289b7ec6); /* 41 */
                d = HH (d, a, b, c, x[ 0],  11, 0xeaa127fa); /* 42 */
                c = HH (c, d, a, b, x[ 3],  16, 0xd4ef3085); /* 43 */
                b = HH (b, c, d, a, x[ 6],  23, 0x04881d05); /* 44 */
                a = HH (a, b, c, d, x[ 9],   4, 0xd9d4d039); /* 45 */
                d = HH (d, a, b, c, x[12],  11, 0xe6db99e5); /* 46 */
                c = HH (c, d, a, b, x[15],  16, 0x1fa27cf8); /* 47 */
                b = HH (b, c, d, a, x[ 2],  23, 0xc4ac5665); /* 48 */
            
                /* Round 4 */
                a = II (a, b, c, d, x[ 0],   6, 0xf4292244); /* 49 */
                d = II (d, a, b, c, x[ 7],  10, 0x432aff97); /* 50 */
                c = II (c, d, a, b, x[14],  15, 0xab9423a7); /* 51 */
                b = II (b, c, d, a, x[ 5],  21, 0xfc93a039); /* 52 */
                a = II (a, b, c, d, x[12],   6, 0x655b59c3); /* 53 */
                d = II (d, a, b, c, x[ 3],  10, 0x8f0ccc92); /* 54 */
                c = II (c, d, a, b, x[10],  15, 0xffeff47d); /* 55 */
                b = II (b, c, d, a, x[ 1],  21, 0x85845dd1); /* 56 */
                a = II (a, b, c, d, x[ 8],   6, 0x6fa87e4f); /* 57 */
                d = II (d, a, b, c, x[15],  10, 0xfe2ce6e0); /* 58 */
                c = II (c, d, a, b, x[ 6],  15, 0xa3014314); /* 59 */
                b = II (b, c, d, a, x[13],  21, 0x4e0811a1); /* 60 */
                a = II (a, b, c, d, x[ 4],   6, 0xf7537e82); /* 61 */
                d = II (d, a, b, c, x[11],  10, 0xbd3af235); /* 62 */
                c = II (c, d, a, b, x[ 2],  15, 0x2ad7d2bb); /* 63 */
                b = II (b, c, d, a, x[ 9],  21, 0xeb86d391); /* 64 */
                
                a += aa;
                b += bb;
                c += cc;
                d += dd;
            }
        }
        return (LSBHex(a) + LSBHex(b) + LSBHex(c) + LSBHex(d)); // return output as a string
    }
    
    
    private static int F(int x, int y, int z)
    {
        return (x & y | ~x & z);
    }
    
    private static int G(int x, int y, int z)
    {
        return (x & z | y & ~z);
    }
    
    private static int H(int x, int y, int z)
    {
        return (x ^ y ^ z);
    }
    
    private static int I(int x, int y, int z)
    {
        return (y ^ (x | ~z));
    }
    
    private static int FF(int a, int b, int c, int d, int k, int s, int i)
    {
        return (b + rotateLeft((a + F(b,c,d) + k + i), s));
    }
    
    private static int GG(int a, int b, int c, int d, int k, int s, int i)
    {
        return (b + rotateLeft((a + G(b,c,d) + k + i), s));
    }
    
    private static int HH(int a, int b, int c, int d, int k, int s, int i)
    {
        return (b + rotateLeft((a + H(b,c,d) + k + i), s));
    }
    
    private static int II(int a, int b, int c, int d, int k, int s, int i)
    {
        return (b + rotateLeft((a + I(b,c,d) + k + i), s));
    }
    
    private static int rotateLeft(int x, int c)
    {
        return ((x << c) | (x >>> (32 - c)));
    }

    
    public static void main(String [] args) throws Exception
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter some text: ");
        String text = br.readLine();
        byte [] msg = text.getBytes();
        
        doHash(msg);
    }
}
