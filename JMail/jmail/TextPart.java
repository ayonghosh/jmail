package jmail;

 

public class TextPart
{
    private String contentType;
    private String body;
    
    public TextPart(String contentType, String body)
    {
        this.contentType = new String(contentType);
        this.body = new String(body);
    }
    
    public String getContentType()
    {
        return this.contentType;
    }
    
    public String getBody()
    {
        return this.body;
    }
}
