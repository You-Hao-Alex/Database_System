import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

public class hashload {
	public static void main(String[] args) {
		int pagesize = 0; // Store the page size
		
		try {
			pagesize = Integer.parseInt(args[1]); // Get the pagesize
			// Date start = new Date(); // Mark the starting time
			String hashfile = "hash." + pagesize; // Get the file name
			hashload input = new hashload();
			input.generate(pagesize, hashfile); // Do the operations
			// Date finish = new Date(); // Mark the ending time
			// System.out.println("Time to do search operations in milliseconds is " + (finish.getTime() - start.getTime()));
			// Compare to get the running time
		} catch (Exception e) {
			System.err.println("Can't get the input");
		}
	}

	// To print the result and implement the operations
	private void generate(int pagesize, String hashfile) {
		try {
			// System.out.println(text+""+pagesize+""+hashfile); //testing
			FileInputStream heap = new FileInputStream(new File("heap."+pagesize));
			byte[] page = new byte[pagesize]; // Get the data page by page
			int pageNo = 0; // Set the searching index
			Hashtable <String, String> table = new Hashtable<String, String>();
			FileOutputStream hash = new FileOutputStream(new File(hashfile));
			ObjectOutputStream hashout = new ObjectOutputStream (hash);
			// Read from heap file and get the first page into page array
			int pageno = 0;
			while ((heap.read(page, pageNo, pagesize)) != -1) {
				
				
				byte[] index = new byte[2]; // Create an array to save 2 bytes
				System.arraycopy(page, 0, index, 0, 2); // Copy the first 2 bytes into index[]
				int recordno = Searching.byteToShort(index); // Convert these 2 bytes into short and save it as an integer
				int[] position = new int[(recordno)]; // Store the starting position of records
				int[] length = new int[recordno]; // Store the length of records
				for (int i = 0; i < recordno; i++) { // Fill in the data of record position
					System.arraycopy(page, 2 * (i + 1), index, 0, 2);
					position[i] = Searching.byteToShort(index);
					// System.out.println(position[i]);
				}
				for (int i = 0; i < (recordno - 1); i++) { // Fill in the data of record length
					length[i] = position[i + 1] - position[i];
					// System.out.println(length[i]);
				}
				System.arraycopy(page, (position[(recordno - 2)] + length[(recordno - 2)] + 18), index, 0, 2);
				length[(recordno - 1)] = Searching.byteToShort(index); // The length of the last record in this page
				// System.out.println(length[(recordno-1)]);
				
				
				
				
				ArrayList<String> Target = new ArrayList<String>();
				for (int i = 0; i < position.length; i++) {
					int[] fieldPosition = new int[10]; // Store field position
					int[] fieldLength = new int[9]; // Store field length
					for (int j = 0; j < 10; j++) {
						byte[] indexs = new byte[2]; // Store index
						System.arraycopy(page, (position[i] + 2 * j), indexs, 0, 2);
						fieldPosition[j] = Searching.byteToShort(indexs); // Fill in field position
						// System.out.println(fieldPosition[j]);
					}
					for (int j = 0; j < 9; j++) {
						fieldLength[j] = fieldPosition[j + 1] - fieldPosition[j]; // Fill in field length
						// System.out.println(fieldLength[j]);
					}
					byte[] BN = new byte[fieldLength[1]]; // Find the info in the second field
					System.arraycopy(page, position[i] + fieldPosition[1], BN, 0, fieldLength[1]);
					String BNname = new String(BN); // Get the BN name
					table.put(BNname, ""+pageno);
				}
				pageno++;
				}	
			hashout.writeObject(table);
			hashout.flush();
			hashout.close();
		} catch (Exception e) {
			System.err.println("Incorrect input");
		}
	}
}
