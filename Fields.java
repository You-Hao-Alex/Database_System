package DBS;

public class Fields {
	
    private int Length;
    private String Content;
    private String Type;

    public Fields(String Content, int a) {
        this.Content = Content;
        if (a == 8) {
			this.Type = "long";
			this.Length = 8;
		}
		else {
			this.Type = "String";
			this.Length = Content.length();
		}
		
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
