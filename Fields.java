package DBS;

/**
 * @Date 28/03/2018
 * @author You Hao s3583715
 * @Description: Fields class. For creating a field to store all its info
 * @Version 1.0
 */

public class Fields {

	private int Length; // Length of the field
	private String Content; // Content of the field
	private String Type; // Type of the field (String or Long)

	public Fields(String Content, int fieldNo) {
		this.Content = Content; // Set the content of the field
		// Set the type and length of the field
		if (fieldNo == 8) {
			this.Type = "long";
			this.Length = 8;
		} else {
			this.Type = "String";
			this.Length = Content.length();
		}
	}

	public int getLength() {
		return Length; // Get its length
	}

	public void setLength(int Length) {
		this.Length = Length; // Set its length
	}

	public String getContent() {
		return Content; // Get its content
	}

	public void setContent(String Content) {
		this.Content = Content; // Set its content
	}

	public String getType() {
		return Type; // Get its type
	}

	public void setType(String Type) {
		this.Type = Type; // Set its type
	}

}
