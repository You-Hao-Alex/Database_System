package DBS;

import java.util.ArrayList;

/**
 * @Date 28/03/2018
 * @author You Hao s3583715
 * @Description: Pages class. For creating a page to store records
 * @Version 1.0
 */

public class Pages {
	private int max; // The capacity of this page
	private int current = 2; // Current size of the page
	private int rest; // The rest size of the page
	ArrayList<Records> pages = new ArrayList<Records>(); // To store all the records

	public Pages(int pagesize) {
		this.max = pagesize; // Set the capacity
	}

	public int getCurrent() {
		return current; // Get the current usage
	}

	public int getRest() {
		return (max - current); // Get the rest capacity
	}

	public void addRecord(Records record) {
		pages.add(record); // Add a record into page
		this.current += (record.getLength() + 2); // Set the current capacity again
	}

	public ArrayList<Records> getRecord() {
		return pages; // Get the record list
	}

}
