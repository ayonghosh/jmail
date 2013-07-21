package jmail.secure;


import java.math.BigInteger;
import java.security.SecureRandom;

public final class KeyGen
{
   private final static SecureRandom random = new SecureRandom();
   
   private BigInteger p, q;
   private BigInteger n;
   private BigInteger e;
   private BigInteger d;
   
   private Key publicKey;
   private Key privateKey;

   public KeyGen(int N, int eN)
   {
      this.p = BigInteger.probablePrime(N - 1, random);
      this.q = BigInteger.probablePrime(N + 1, random);
      
      this.n = calculate_n(p, q);
      this.e = generate_e(p, q, eN);
      this.d = calculate_d(p, q, e);
      
      this.publicKey = new Key(e, n);
      this.privateKey = new Key(d, n);
   }
   
   public Key getPublicKey()
   {
       return this.publicKey;
   }
   
   public Key getPrivateKey()
   {
       return this.privateKey;
   }
   
   private BigInteger generate_e(BigInteger p, BigInteger q, int bitsize) {
       BigInteger e, phi_pq;

       e = BigInteger.ZERO;
       phi_pq = q.subtract(BigInteger.ONE);
       phi_pq = phi_pq.multiply(p.subtract(BigInteger.ONE));

       int i = 0;

       do {
           e = (new BigInteger(bitsize, 0, random)).setBit(0);
           i = i + 1;
        } while( i < 100 && (e.gcd(phi_pq).compareTo(BigInteger.ONE) != 0));
    
        return e;
    }
    
    private BigInteger calculate_d(BigInteger p, BigInteger q, BigInteger e) {
        BigInteger d, phi_pq;
    
        phi_pq = q.subtract(BigInteger.ONE);
        phi_pq = phi_pq.multiply(p.subtract(BigInteger.ONE));
  
        d = e.modInverse(phi_pq);
        return d;
    }
    
    private BigInteger calculate_n(BigInteger p, BigInteger q) {
        return p.multiply(q);
    }
}
