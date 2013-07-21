package jmail.secure;


import java.math.BigInteger;

public class BigInt implements java.io.Serializable
{
    BigInteger b;
    boolean notFlag;
    int length;
    
    BigInt(BigInteger b, boolean notFlag, int length)
    {
        this.b = b;
        this.notFlag = notFlag;
        this.length = length;
    }
    
    public String toString()
    {
        return b.toString();
    }
    
    public BigInteger getBigInteger()
    {
        return this.b;
    }
    
    public int getLength()
    {
        return this.length;
    }
    
    public boolean getNotFlag()
    {
        return this.notFlag;
    }
}
