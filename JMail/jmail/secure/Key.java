package jmail.secure;


import java.math.BigInteger;
import jmail.Defaults;

public class Key implements java.io.Serializable
{
    private BigInteger exp;
    private BigInteger mod;
    
    public Key(BigInteger exp, BigInteger mod)
    {
        this.exp = new BigInteger(exp.toString());
        this.mod = new BigInteger(mod.toString());
    }
    
    public Key(String exp, String mod)
    {
        this.exp = new BigInteger(exp);
        this.mod = new BigInteger(mod);
    }
    
    public Key(Key key)
    {
        this.exp = key.getExponent();
        this.mod = key.getModulus();
    }
    
    public BigInteger getExponent()
    {
        return this.exp;
    }
    
    public BigInteger getModulus()
    {
        return this.mod;
    }
    
    public void setExponent(String exp)
    {
        this.exp = new BigInteger(exp);
    }
    
    public void setModulus(String mod)
    {
        this.mod = new BigInteger(mod);
    }
    
    public String toString()
    {
        return new String("Exponent: " + exp.toString() + "\nModulus: " + mod.toString());
    }
}



