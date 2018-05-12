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
		ArrayList<Hashcode> hashdata = new ArrayList<Hashcode>(); // Save all the fields from heap file
		int bucketnumber = 0; // The number of records saved in bucket
		int overflownumber = 0; // The number of records saved as overflow
		int lasthash = hashtablesize - 1; // The index of last hash table
		byte[] BN = null;

		try {
			ArrayList<Hashtables> tables = new ArrayList<Hashtables>(2500);
			// Create tables in array list to store content of table
			for (int i = 0; i < hashtablesize; i++)
				tables.add(new Hashtables());

			byte[] page = new byte[pagesize]; // Get the data page by page

			hashdata = HashLoadFunctions.readheap(pagesize, hashtablesize, tables, BN, hashdata, page);

			while (hashdata != null) {
				for (int j = 0; j < (hashdata.size()); j++) {
					// Save in current bucket
					if(hashdata.get(j).getBNname().equals("AAA Pizza")) {
						System.out.println((hashdata.get(j)).getHashcode());
						System.out.println(HashFunctions.gethashcode(""+((hashdata.get(j)).getHashcode()),hashtablesize));
					}
					
					if ((tables.get(HashFunctions.gethashcode(((hashdata.get(j)).getBNname()),hashtablesize)).size() < bucketsize)) {
						(tables.get(HashFunctions.gethashcode(((hashdata.get(j)).getBNname()),hashtablesize)))
								.addtable((hashdata.get(j)).getBNname(),(hashdata.get(j)).getPN());
						// System.out.println((Integer.parseInt((hashdata.get(j))[0])));
						bucketnumber++;
					}
					// Overflow to next bucket
					else {
						int number = HashFunctions.gethashcode(""+((hashdata.get(j)).getHashcode()),hashtablesize);
						do {
							if (number == lasthash)
								number = 0;
							else
								number++;
						} while ((tables.get(number)).size() == bucketsize);
						(tables.get(number)).addtable((hashdata.get(j)).getBNname(), (hashdata.get(j)).getPN());
						// System.out.println((hashdata.get(j))[bnnameposition] + " " +
						// (hashdata.get(j))[pagenumberposition]);
						overflownumber++;
					}
				}
				hashdata = HashLoadFunctions.readheap(pagesize, hashtablesize, tables, BN, hashdata, page);
			}
			HashLoadFunctions.Storing(hashtablesize, pagesize, tables);
			System.out.println("The number of records stored in bucket is " + bucketnumber);
			System.out.println("The number of records stored as overflow is " + overflownumber);
			System.out.println(hashtablesize + " hash files have been created");
		} catch (Exception e) {
			//System.err.println("Incorrect input");
			e.printStackTrace();
		}

	}
}
