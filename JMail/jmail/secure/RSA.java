package jmail.secure;

import java.math.BigInteger;
import java.security.SecureRandom;


public final class RSA
{
    public static final BigInteger TWO = new BigInteger("2");
    
    public static String encrypt(String m, Key key)
    {
        return encrypt(new BigInteger(m.getBytes()), key).toString();
    }
    
    public static String decrypt(String c, Key key)
    {
        return new String(decrypt(new BigInteger(c), key).toByteArray());
    }
    
    public static BigInteger encrypt(BigInteger m, Key key) {
        BigInteger c, bitmask;
        c = BigInteger.ZERO;
        BigInteger n = key.getModulus();
        BigInteger e = key.getExponent();
        int i = 0;
        bitmask = TWO.pow(n.bitLength()-1).subtract(BigInteger.ONE);
        while (m.compareTo(bitmask) == 1) {
            c = m.and(bitmask).modPow(e,n).shiftLeft(i*n.bitLength()).or(c);
            m = m.shiftRight(n.bitLength()-1);
            i = i+1;
        }
        c = m.modPow(e,n).shiftLeft(i*n.bitLength()).or(c);
        return c;
    }
    
    public static BigInteger decrypt(BigInteger c, Key key) {
        BigInteger m, bitmask;
        m = BigInteger.ZERO;
        BigInteger n = key.getModulus();
        BigInteger d = key.getExponent();
        int i = 0;
        bitmask = TWO.pow(n.bitLength()).subtract(BigInteger.ONE);
        while (c.compareTo(bitmask) == 1) {
            m = c.and(bitmask).modPow(d,n).shiftLeft(i*(n.bitLength()-1)).or(m);
            c = c.shiftRight(n.bitLength());
            i = i+1;
        }
        m = c.modPow(d,n).shiftLeft(i*(n.bitLength()-1)).or(m);
        
        return m;
    }
}
