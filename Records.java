package DBS;

public class Records {
	private int Length;
    private String Content;

    public Records(int Length, String Content) {
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
