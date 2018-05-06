import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * @Date 04/05/2018
 * @author You Hao s3583715
 * @Description: HashLoadFunctions class. Most hash loading methods are
 *               implemented in this class.
 * @Version 1.0
 **/

public class HashLoadFunctions {
	// This method is creating hash files to save records' BNnames and page numbers
	public static void Storing(int hashtablesize, int pagesize, ArrayList<Hashtables> tables) {
		// Create a file to store the hash files
		String hashfolder = "hash4096";
		File folder = new File(hashfolder);
		if (!folder.exists())
			folder.mkdirs();
		// Write all the records into hash file
		for (int i = 0; i < hashtablesize; i++) {
			File hashfile = new File(hashfolder + "/hash" + i + "." + pagesize);
			FileOutputStream hash;
			try {
				hash = new FileOutputStream(hashfile);
				ObjectOutputStream hashout = new ObjectOutputStream(hash); // Write into Hash file
				hashout.writeObject(tables.get(i).getList());
				hashout.flush();
				hashout.close();
			} catch (Exception e) {
				System.err.println("Can't find the " + hashfolder + "folder");
			}
		}
	}

	// This method is to read information from heap file
	public static void readheap(int pagesize, ArrayList<String> hashdata, int hashtablesize) {
		FileInputStream heap;
		try {
			heap = new FileInputStream(new File("heap." + pagesize));
			byte[] page = new byte[pagesize]; // Get the data page by page
			int pageNo = 0; // Set the searching index
			int pageno = 0; // Mark the page number of each company name
			int empty = -1; // The empty number of a heap
			int startposition = 0; // The start position of array copy
			int savebyte = 2; // The size of byte array to save index
			int nextindex = 1; // The position for next index
			int indexlength = 18; // The length of index in a record
			int recordlength = 10; // The number of field position array

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
					int[] fieldPosition = new int[recordlength]; // Store field position
					int[] fieldLength = new int[recordlength - nextindex]; // Store field length
					for (int j = 0; j < recordlength; j++) {
						byte[] indexs = new byte[savebyte]; // Store index
						System.arraycopy(page, (position[i] + savebyte * j), indexs, startposition, savebyte);
						fieldPosition[j] = Searching.byteToShort(indexs); // Fill in field position
					}
					for (int j = 0; j < (recordlength - nextindex); j++) {
						fieldLength[j] = fieldPosition[j + nextindex] - fieldPosition[j]; // Fill in field length
					}
					byte[] BN = new byte[fieldLength[nextindex]]; // Find the info in the second field
					System.arraycopy(page, position[i] + fieldPosition[nextindex], BN, 0, fieldLength[nextindex]);
					String BNname = new String(BN); // Get the BN name
					int hashcode = HashFunctions.gethashcode(BNname, hashtablesize);
					hashdata.add("" + hashcode);
					hashdata.add(BNname);
					hashdata.add("" + pageno);
				}
				pageno++;
			}
			heap.close(); // Close the heap
		} catch (Exception e) {
			System.err.println("Can't find the heap file.");
		} // Read from heap file
	}

}
