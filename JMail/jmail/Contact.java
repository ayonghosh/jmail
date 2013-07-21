package jmail;

import jmail.secure.*;

public class Contact implements java.io.Serializable
{
    private String name;
    private String email;
    private String phone;
    private String address;
    private Key publicKey;
    
    Contact()
    {
        name = new String();
        email = new String();
        phone = new String();
        address = new String();
        publicKey = new Key("0", "0");
    }
    
    Contact(String name, String email, String phone, String address, Key key)
    {
        this.name = new String(name);
        this.email = new String(email);
        this.phone = new String(phone);
        this.address = new String(address);
        this.publicKey = new Key(key);
    }
    
    public String getName()
    {
        return name;
    }
    
    public String getEmail()
    {
        return email;
    }
    
    public String getPhone()
    {
        return phone;
    }
    
    public String getAddress()
    {
        return address;
    }
    
    public Key getPublicKey()
    {
        return this.publicKey;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public void setEmail(String email)
    {
        this.email = email;
    }
    
    public void setAddress(String address)
    {
        this.address = address;
    }
    
    public void setPhone(String phone)
    {
        this.phone = phone;
    }
    
    public void setPublicKeyExponent(String exp)
    {
        this.publicKey.setExponent(exp);
    }
    
    public void setPublicKeyModulus(String mod)
    {
        this.publicKey.setModulus(mod);
    }
}

