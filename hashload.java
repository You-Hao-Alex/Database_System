import java.util.ArrayList;
import java.util.Date;

/**
 * @Date 04/05/2018
 * @author You Hao s3583715
 * @Description: hashload class. The main class to implement the load function.
 *               It will read from heap file and export into hash tables.
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
		int recordNo = 2024631; // The number of records in the heap file
		double percent = 0.995; // The occupancy
		int bucketsize = (int) (recordNo / hashtablesize / percent); // Set the bucket size
		ArrayList<String> hashdata = new ArrayList<String>(); // Save all the fields from heap file
		int bucketnumber = 0; // The number of records saved in bucket
		int overflownumber = 0; // The number of records saved as overflow
		int content = 3; // The number of array
		int bnnameposition = 1; // The position of BNname in array list
		int pagenumberposition = 2; // The position of page number in array list
		int lasthash = hashtablesize - 1; // The index of last hash table

		try {
			HashLoadFunctions.readheap(pagesize, hashdata, hashtablesize);
			ArrayList<Hashtables> tables = new ArrayList<Hashtables>();
			// Create tables in array list to store content of table
			for (int i = 0; i < hashtablesize; i++)
				tables.add(new Hashtables(new ArrayList<String[]>()));
			for (int j = 0; j < (hashdata.size() / content); j++) {
				// Save in current bucket
				if ((tables.get(Integer.parseInt(hashdata.get(content * j)))).size() < bucketsize) {
					(tables.get(Integer.parseInt(hashdata.get(content * j)))).addtable(
							hashdata.get(content * j + bnnameposition), hashdata.get(content * j + pagenumberposition));
					bucketnumber++;
				}
				// Overflow to next bucket
				else {
					int number = Integer.parseInt(hashdata.get(content * j));
					do {
						if (number == lasthash)
							number = 0;
						else
							number++;
					} while ((tables.get(number)).size() == bucketsize);
					(tables.get(number)).addtable(hashdata.get(content * j + bnnameposition),
							hashdata.get(content * j + pagenumberposition));
					System.out.println(hashdata.get(content * j + bnnameposition) + " "
							+ hashdata.get(content * j + pagenumberposition));
					overflownumber++;
				}
			}
			HashLoadFunctions.Storing(hashtablesize, pagesize, tables);
			System.out.println("The number of records stored in bucket is " + bucketnumber);
			System.out.println("The number of records stored as overflow is " + overflownumber);
			System.out.println(hashtablesize + " hash files have been created");
		} catch (Exception e) {
			System.err.println("Incorrect input");
		}

	}
}
