import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * @Date 04/05/2018
 * @author You Hao s3583715
 * @Description: HashFunctions class. Most hash functions are operated in this
 *               class. Including the function of generating hash code.
 * @Version 1.0
 **/

public class HashFunctions {
	// This method is to generate the hash code for a text
	public static int gethashcode(String hashcode, int hashtablesize) {
		// Get the absolute value and then mod the hash table size
		int hash = (Math.abs(hashcode.hashCode())) % hashtablesize;
		return hash;
	}

	// This method is to find the page number of target text in the heap file
	public static int getpageno(int hashcode, int pagesize, String text, int times, int fail, int hashtablesize) {
		int overflow = 10; // Check for 10 more pages if there's an overflow
		int pagenumber = 0; // To store the page number of key word in heap file
		String hashfile = "hash4096/" + "hash" + hashcode + "." + pagesize; // The name of the target hash file
		int lasttable = hashtablesize - 1; // The index of last table in hash table file
		try {
			FileInputStream hashtable = new FileInputStream(new File(hashfile));
			ObjectInputStream hash = new ObjectInputStream(hashtable);
			Hashtable<String, String> table = (Hashtable<String, String>) hash.readObject(); // Get the table
			String pageno = table.get(text); // Get the page number in String

			// If we didn't find the page number in current table
			if (pageno == null) {
				// If this is the last hash table
				if (hashcode == lasttable) {
					times++;
					// If it checked many times for the overflow, return fail to show 'no results'
					if (times == overflow)
						pagenumber = fail;
					// Else, keep searching it in next page
					else
						pagenumber = getpageno(0, pagesize, text, times, fail, hashtablesize);
				} else {
					hashcode++;
					times++;
					if (times == overflow)
						pagenumber = fail;
					else
						pagenumber = getpageno(hashcode, pagesize, text, times, fail, hashtablesize);
				}
			}
			// If we find it, get the page number and close the hash
			else {
				pagenumber = Integer.parseInt(pageno); // Get the page number in Integer
				hash.close(); // Close the hash
			}
		} catch (Exception e) {
			System.err.println("Can't find the hash file");
		}
		return pagenumber;
	}

	// This method is to find out the result by using the key words
	// Then, print all the result out on the screen
	public static void showresults(int pagesize, int pageno, String text) {
		ArrayList<String[]> result = new ArrayList<String[]>(); // Use array list to store the result
		String heapfile = "heap." + pagesize; // Set the heap file's name
		int pageNo = 0; // Set the start position when reading the heap file

		try {
			FileInputStream heap = new FileInputStream(new File(heapfile));
			byte[] page = new byte[pagesize]; // Get the data page by page
			// Until the page with the key words
			for (int i = 0; i < pageno; i++) {
				heap.read(page, pageNo, pagesize);
			}
			heap.read(page, pageNo, pagesize); // Read the record with key word
			result = Searching.getRecord(text, page); // Get the result and print
			heap.close(); // Close the heap
		} catch (Exception e) {
			System.err.println("Can't find the heap file!");
			// e.printStackTrace();
		}
	}

	// This method is to search the text in heap file and print it out
	public static void searchtext(String text, int pagesize, int hashtablesize) {
		int times = 0; // Record the time of checking overflow
		int fail = -1; // Return -1 to show no results
		int hashcode = gethashcode(text, hashtablesize); // Get the hash code
		int pageno = getpageno(hashcode, pagesize, text, times, fail, hashtablesize); // Get the page number
		// If no result matched, print the alert
		if (pageno == fail)
			System.err.println("Sorry, the result is not found.");
		// If the page number is found, search for the record in heap file
		else
			showresults(pagesize, pageno, text);
	}
}
