package jmail.secure;



public class KeyPair
{
    private Key publicKey;
    private Key privateKey;
    
    public KeyPair(Key publicKey, Key privateKey)
    {
        this.publicKey  = new Key(publicKey);
        this.privateKey = new Key(privateKey);
    }
    
    public Key getPublicKey()
    {
        return this.publicKey;
    }
    
    public Key getPrivateKey()
    {
        return this.privateKey;
    }
}
