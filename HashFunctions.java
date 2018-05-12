import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * @Date 04/05/2018
 * @author You Hao s3583715
 * @Description: HashFunctions class. Most query functions are operated in this
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
	public static ArrayList<Integer> getpageno(int hashcode, int pagesize, String text, int fail, int hashtablesize,
			ArrayList<Integer> pagenumber) {
		String hashfile = "hash4096/" + "hash" + hashcode + "." + pagesize; // The name of the target hash file
		int lasttable = hashtablesize - 1; // The index of last table in hash table file
		int recordNo = 2024631; // The number of records in the heap file
		double percent = 0.995; // The occupancy
		int bucketsize = (int) (recordNo / hashtablesize / percent); // Set the bucket size
		try {
			FileInputStream hashtable = new FileInputStream(new File(hashfile));
			ObjectInputStream hash = new ObjectInputStream(hashtable);
			// ArrayList temptable = (ArrayList) hash.readObject(); // Get the table
			Hashtables table = (Hashtables) hash.readObject();
			int pageno = table.getPageno(text); // Get the page number in String
			Boolean check = false; // Check if this bucket is full
			if (table.size() != bucketsize)
				check = true;
			// If we didn't find the page number in current table
			if (pageno == fail) {
				// If this is the last hash table
				if (hashcode == lasttable) {
					// If it checked many times for the overflow, return fail to show 'no results'
					if (check)
						pagenumber.add(fail);
					// Else, keep searching it in next page
					else {
						System.out.println("There is no result in hash" + hashcode + ".4096");
						pagenumber = getpageno(0, pagesize, text, fail, hashtablesize, pagenumber);
					}
				} else {
					if (check)
						pagenumber.add(fail);
					else {
						System.out.println("There is no result in hash" + hashcode + ".4096");
						hashcode++;
						pagenumber = getpageno(hashcode, pagesize, text, fail, hashtablesize, pagenumber);
					}
				}
			}
			// If we find it, get the page number and close the hash
			else {
				if (check) {
					pagenumber.add(pageno); // Get the page number in Integer
					System.out.println("We found the result in hash" + hashcode + ".4096");
				} else {
					pagenumber.add(pageno); // Get the page number in Integer
					System.out.println("We found the result in hash" + hashcode + ".4096");
					hashcode++;
					pagenumber = getpageno(hashcode, pagesize, text, fail, hashtablesize, pagenumber);
				}
			}
			hash.close(); // Close the hash
		} catch (Exception e) {
			System.err.println("Can't find the hash file");
		}
		return pagenumber;
	}

	// This method is to find out the result by using the key words
	// Then, print all the result out on the screen
	public static void showresults(int pagesize, ArrayList<Integer> pageno, String text) {
		ArrayList<String[]> result = new ArrayList<String[]>(); // Use array list to store the result
		String heapfile = "heap." + pagesize; // Set the heap file's name
		int pageNo = 0; // Set the start position when reading the heap file
		try {
			FileInputStream heap = new FileInputStream(new File(heapfile));
			byte[] page = new byte[pagesize]; // Get the data page by page
			// Until the page with the key words
			for (int j = 0; j < pageno.size(); j++) {
				for (int i = 0; i < pageno.get(j); i++)
					heap.read(page, pageNo, pagesize);
				heap.read(page, pageNo, pagesize); // Read the record with key word
				result = Searching.getRecord(text, page); // Get the result and print
			}
			heap.close(); // Close the heap
		} catch (Exception e) {
			System.err.println("Can't find the heap file!");
		}
	}

	// This method is to search the text in heap file and print it out
	public static void searchtext(String text, int pagesize, int hashtablesize) {
		int fail = -1; // Return -1 to show no results
		int hashcode = gethashcode(text, hashtablesize); // Get the hash code
		ArrayList<Integer> pagenumber = new ArrayList<Integer>();
		ArrayList<Integer> pageno = getpageno(hashcode, pagesize, text, fail, hashtablesize, pagenumber);
		// If no result matched, print the alert
		if (pageno.get(0) == fail)
			System.err.println("Sorry, the result is not found.");
		// If the page number is found, search for the record in heap file
		else
			showresults(pagesize, pageno, text);
	}
}
