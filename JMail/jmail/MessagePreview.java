package jmail;

 

public class MessagePreview
{
    private String from;
    private String sub;
    private String dated;
    private String preview;
    private boolean read;
    private boolean starred;
    
    public MessagePreview()
    {
        
    }
    
    public MessagePreview(String from, String sub, String dated, String preview)
    {
        setFrom(from);
        setSubject(sub);
        setDate(dated);
        setPreview(preview);
    }
    
    public void setFrom(String from)
    {
        if (from == null) {
            from = "";
        }
        this.from = new String(from);
    }
    
    public void setSubject(String sub)
    {
        if (sub == null) {
            sub = "";
        }
        this.sub = new String(sub);
    }
    
    public void setDate(String date)
    {
        if (date == null) {
            date = "";
        }
        this.dated = new String(date);
    }
    
    public void setPreview(String preview)
    {
        if (preview == null) {
            preview = "";
        }
        this.preview = new String(preview);
    }
    
    public void setRead(boolean read)
    {
        this.read = read;
    }
    
    public void setStarred(boolean starred)
    {
        this.starred = starred;
    }
    
    public String getFrom()
    {
        return from;
    }
    
    public String getSubject()
    {
        return sub;
    }
    
    public String getDate()
    {
        return dated;
    }
    
    public String getPreview()
    {
        return preview;
    }
    
    public boolean isRead()
    {
        return read;
    }
    
    public boolean isStarred()
    {
        return starred;
    }
}
