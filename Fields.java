package DBS;

public class Fields {
	
    private int Length;
    private String Content;
    private String Type;

    public Fields(int Length, String Content, String Type) {
        this.Length = Length;
        this.Content = Content;
        this.Type = Type;
    }

    public int getLength() {
        return Length;
    }
    
    public void setLength(int Length) {
    		this.Length = Length;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

   
}
