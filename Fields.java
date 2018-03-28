package DBS;

public class Fields {
	
    private int Length; // Length of the field
    private String Content; // Content of the field
    private String Type; // Type of the field (String or int)

    public Fields(String Content, int fieldNo) {
        this.Content = Content; // Set the content of the field
        if (fieldNo == 8) { // Set the type of the field
			this.Type = "long";
			this.Length = 8;
		}
		else {
			this.Type = "String";
			this.Length = Content.length();
		}
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
