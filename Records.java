package DBS;

public class Records {
	private int Length; // The length of the record
    private Fields[] record= new Fields[9]; // To store 9 fields for a record
    
    
    
    public Records(Fields[] field) { // Get 9 fields as a record
        this.Length = 18; // 2 for each fields' index, 2*9 in total
        record = field;
        for (int i = 0;i < 9;i++) {
        	this.Length += field[i].getLength();
        }
    }

    public int getLength() {
        return Length;
    }
    
    public void setLength(int Length) {
    		this.Length = Length;
    }

	public Fields[] getRecord() {
		return record;
	}

	public void setRecord(Fields[] record) {
		this.record = record;
	}
    
    
}
