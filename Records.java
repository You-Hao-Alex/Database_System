package DBS;

/**
 * @Date 28/03/2018
 * @author You Hao s3583715
 * @Description: Records class. For creating a record to store fields
 * @Version 1.0
 */

public class Records {
	private int Length; // The length of the record
	private Fields[] record = new Fields[9]; // To store 9 fields for a record

	public Records(Fields[] field) { // Get 9 fields as a record
		this.Length = 20; // 2 for each fields' index and ending index
		record = field;
		for (int i = 0; i < 9; i++) {
			this.Length += field[i].getLength(); // Set its length
		}
	}

	public int getLength() {
		return Length; // Get its length
	}

	public void setLength(int Length) {
		this.Length = Length; // Set its length
	}

	public Fields[] getRecord() {
		return record; // Get 9 fields array
	}

	public void setRecord(Fields[] record) {
		this.record = record; // Set the record
	}

}
