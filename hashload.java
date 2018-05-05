import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

/**
 * @Date 04/05/2018
 * @author You Hao s3583715
 * @Description: hashload class. The main class to implement the load function.
 *               It will read from
 * @Version 1.0
 **/

public class hashload {
	public static void main(String[] args) {
		int pagesize = 0; // Store the page size
		int inputarray = 0; // The input array position of page size
		try {
			pagesize = Integer.parseInt(args[inputarray]); // Get the page size
			Date start = new Date(); // Mark the starting time
			hashload input = new hashload();
			input.generate(pagesize); // Do the Implementation
			Date finish = new Date(); // Mark the ending time
			System.out.println("Time to generate hash in milliseconds is " + (finish.getTime() - start.getTime()));
			// Compare to get the running time
		} catch (Exception e) {
			System.err.println("Can't get the input");
		}
	}

	// To print the result and implement the operations
	private void generate(int pagesize) {
		int hashtablesize = 1024 - 1; // Set the hash table size
		int recordNo = 2000000; // The number of records in the heap file
		double percent = 0.7; // The occupancy
		int bucketsize = (int) (recordNo / hashtablesize / percent); // Set the bucket size
		ArrayList<String> hashdata = new ArrayList<String>(); // Save all the fields from heap file

		try {
			FileInputStream heap = new FileInputStream(new File("heap." + pagesize)); // Read from heap file
			byte[] page = new byte[pagesize]; // Get the data page by page
			int pageNo = 0; // Set the searching index
			int pageno = 0; // Mark the page number of each company name
			int empty = -1; // The empty number of a heap
			int startposition = 0; // The start position of array copy
			int savebyte = 2; // The size of byte array to save index
			int nextindex = 1; // The position for next index
			int indexlength = 18; // The length of index in a record

			while ((heap.read(page, pageNo, pagesize)) != empty) {
				byte[] index = new byte[savebyte]; // Create an array to save 2 bytes
				System.arraycopy(page, startposition, index, startposition, savebyte); // Copy the index into index[]
				int recordno = Searching.byteToShort(index); // Convert into short and save it as an integer
				int[] position = new int[(recordno)]; // Store the starting position of records
				int[] length = new int[recordno]; // Store the length of records
				// Fill in the data of record position
				for (int i = 0; i < recordno; i++) {
					System.arraycopy(page, savebyte * (i + nextindex), index, startposition, savebyte);
					position[i] = Searching.byteToShort(index);
				}
				// Fill in the data of record length
				for (int i = 0; i < (recordno - nextindex); i++) {
					length[i] = position[i + nextindex] - position[i];
				}
				System.arraycopy(page, (position[(recordno - savebyte)] + length[(recordno - savebyte)] + indexlength),
						index, startposition, savebyte);
				length[(recordno - nextindex)] = Searching.byteToShort(index); // Length of the last record in this page
				for (int i = 0; i < position.length; i++) {
					int[] fieldPosition = new int[10]; // Store field position
					int[] fieldLength = new int[9]; // Store field length
					for (int j = 0; j < 10; j++) {
						byte[] indexs = new byte[2]; // Store index
						System.arraycopy(page, (position[i] + 2 * j), indexs, 0, 2);
						fieldPosition[j] = Searching.byteToShort(indexs); // Fill in field position
					}
					for (int j = 0; j < 9; j++) {
						fieldLength[j] = fieldPosition[j + 1] - fieldPosition[j]; // Fill in field length
					}
					byte[] BN = new byte[fieldLength[1]]; // Find the info in the second field
					System.arraycopy(page, position[i] + fieldPosition[1], BN, 0, fieldLength[1]);
					String BNname = new String(BN); // Get the BN name
					int hashcode = HashFunctions.gethashcode(BNname, hashtablesize);
					hashdata.add("" + hashcode);
					hashdata.add(BNname);
					hashdata.add("" + pageno);
				}
				pageno++;
			}
			heap.close(); // Close the heap

			int content = 3; // The number of array
			ArrayList<Hashtable<String, String>> tables = new ArrayList<Hashtable<String, String>>();
			// Create tables in array list to store content of table
			for (int i = 0; i < hashtablesize; i++) {
				tables.add(new Hashtable<String, String>());
			}
			for (int j = 0; j < (hashdata.size() / content); j++) {
				// Save in current bucket
				if ((tables.get(Integer.parseInt(hashdata.get(content * j)))).size() < bucketsize) {
					(tables.get(Integer.parseInt(hashdata.get(content * j)))).put(hashdata.get(content * j + 1),
							hashdata.get(content * j + 2));
				}
				// Overflow to next bucket
				else {
					int number = Integer.parseInt(hashdata.get(content * j));
					do {
						if (number == 1022) {
							number = 0;
						} else {
							number++;
						}
					} while ((tables.get(number)).size() == bucketsize);
					(tables.get(number)).put(hashdata.get(content * j + 1), hashdata.get(content * j + 2));
				}
			}

			File folder = new File("hash4096");
			if (!folder.exists())
				folder.mkdirs();

			for (int i = 0; i < hashtablesize; i++) {
				File hashfile = new File("hash4096/" + "hash" + i + "." + pagesize);
				FileOutputStream hash = new FileOutputStream(hashfile);
				ObjectOutputStream hashout = new ObjectOutputStream(hash); // Write into Hash file
				hashout.writeObject(tables.get(i));
				hashout.flush();
				hashout.close();
			}
		} catch (Exception e) {
			System.err.println("Incorrect input");
		}

	}
}
