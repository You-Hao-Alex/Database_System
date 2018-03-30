package Read;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class dbquery {
	public static void main(String[] args) {
		try {
			String text = args[0];
			int pagesize = Integer.parseInt(args[1]);
			String datafile = "heap." + args[1];
			dbquery input = new dbquery();
			input.search(text, pagesize, datafile);
		} catch (Exception e) {
			System.err.println("Can't get the input");
		}
	}

	private void search(String text, int pagesize, String datafile) {
		try {
			// System.out.println(text+""+pagesize+""+datafile); //testing
			FileInputStream heap = new FileInputStream(new File(datafile));
			byte[] page = new byte[pagesize]; // Get the data page by page
			int pageNo = 0; // Set the searching index
			heap.read(page, pageNo, pagesize); // Read from heap file and get the first page into page array
			ArrayList<String[]> Result = Searching.getRecord(text, page);

		} catch (Exception e) {
			System.err.println("Can't find a result");
		}
	}

}
