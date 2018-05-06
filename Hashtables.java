import java.util.ArrayList;

/**
 * @Date 06/05/2018
 * @author You Hao s3583715
 * @Description: Hashtables class. A constructor for hash tables.
 * @Version 1.0
 **/

public class Hashtables {
	int bnname = 0; // The position of BNname in the array
	int pageno = 1; // The position of page number in the arrays
	ArrayList<String[]> table = new ArrayList<String[]>(); // To store all the records

	// Create a hash table
	public Hashtables(ArrayList<String[]> table) {
		this.table = table;
	}

	// Get the array list in this hash table
	public ArrayList<String[]> getList() {
		return table; // Get the current usage
	}

	// Add a new record in this table
	public void addtable(String BNname, String pagenumber) {
		String[] next = new String[2];
		next[bnname] = BNname;
		next[pageno] = pagenumber;
		table.add(next);
	}

	// Get the size of this table
	public int size() {
		return table.size();
	}

	// Get the page number of this table
	public String getPageno(String text) {
		String returning = null;
		for (int i = 0; i < table.size(); i++) {
			if (table.get(i)[bnname].equals(text))
				returning = table.get(i)[1];
		}
		return returning;
	}
}
