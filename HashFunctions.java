import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Hashtable;

public class HashFunctions {
	public static int gethashcode(String hashcode, int hashtablesize) {
		int hash = (Math.abs(hashcode.hashCode())) % hashtablesize;
		return hash;
	}

	public static int getpageno(String file, String text) {
		int pagenumber = 0;
		try {
			FileInputStream hashtable = new FileInputStream(new File("hash4096/" + file));
			ObjectInputStream hash = new ObjectInputStream(hashtable);
			Hashtable<String, String> table = (Hashtable<String, String>) hash.readObject();
			String pageno = table.get(text);
			pagenumber = Integer.parseInt(pageno);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pagenumber;
	}

	public static void showresults(int pagesize, int pageno, String text) {
		ArrayList<String[]> result = new ArrayList<String[]>();
		
		try {
			FileInputStream heap = new FileInputStream(new File("heap."+pagesize));
			byte[] page = new byte[pagesize]; // Get the data page by page
			for (int i=0;i<pageno;i++) {
				heap.read(page, 0, pagesize);
			}
			heap.read(page, 0, pagesize);
			// Read from heap file and get the first page into page array
			
				result = getRecord(text, page);
				
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static ArrayList<String[]> getRecord(String text, byte[] page) {
		
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
		ArrayList<Integer> resultIndex = Searching.getField(page, position, length, text);
		// Get positions of target records
		ArrayList<String[]> result = Searching.getResult(page, position, length, resultIndex);
		return result;
	}
}
