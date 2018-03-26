package DBS;

public class Pages {
	private int Length;
    private String Content;

    public Pages(int Length, String Content) {
        this.Length = Length;
        this.Content = Content;
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
}
