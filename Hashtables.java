import java.io.Serializable;
import java.util.ArrayList;

/**
 * @Date 06/05/2018
 * @author You Hao s3583715
 * @Description: Hashtables class. A constructor for hash tables.
 * @Version 1.0
 **/

public class Hashtables implements Serializable {
	int bnname = 0; // The position of BNname in the array
	int pageno = 1; // The position of page number in the arrays
	ArrayList<String> BNname = new ArrayList<String>(); // To store all the records
	ArrayList<Integer> PageNo = new ArrayList<Integer>();

	// Get the BNname list
	public ArrayList<String> getBNnamelist() {
		return BNname;
	}

	// Get the page number list
	public ArrayList<Integer> getPageNolist() {
		return PageNo;
	}

	// Add a new record in this table
	public void addtable(String BNname, int PageNo) {
		this.BNname.add(BNname);
		this.PageNo.add(PageNo);
	}

	// Get the size of this table
	public int size() {
		return BNname.size();
	}

	// Get the page number of this table
	public int getPageno(String text) {
		int fail = -1;
		for (int i = 0; i < BNname.size(); i++) {
			
			if (BNname.get(i).equals(text))
				return PageNo.get(i);
		}
		return fail;
	}
}
