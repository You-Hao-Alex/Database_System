import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;

public class hashload {
	public static void main(String[] args) {
		int pagesize = 0; // Store the page size
		
		try {
			pagesize = Integer.parseInt(args[1]); // Get the pagesize
			// Date start = new Date(); // Mark the starting time
			String datafile = "hash." + pagesize; // Get the file name
			hashload input = new hashload();
			input.search(pagesize, datafile); // Do the operations
			// Date finish = new Date(); // Mark the ending time
			// System.out.println("Time to do search operations in milliseconds is " + (finish.getTime() - start.getTime()));
			// Compare to get the running time
		} catch (Exception e) {
			System.err.println("Can't get the input");
		}
	}

	// To print the result and implement the operations
	private void search(int pagesize, String datafile) {
		try {
			// System.out.println(text+""+pagesize+""+datafile); //testing
			FileInputStream heap = new FileInputStream(new File(datafile));
			byte[] page = new byte[pagesize]; // Get the data page by page
			int pageNo = 0; // Set the searching index
			// Read from heap file and get the first page into page array
			while ((heap.read(page, pageNo, pagesize)) != -1) {
				ArrayList<String[]> Result = Searching.getRecord(text, page);
				if (Result.size() == 0)
					System.out.println("No matching results in current page");
			}
		} catch (Exception e) {
			System.err.println("Incorrect input");
		}
	}
}
