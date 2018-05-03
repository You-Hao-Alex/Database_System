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
		int inputarray = 1; // The input array position of pagesize

		try {

			pagesize = Integer.parseInt(args[inputarray]); // Get the pagesize
			Date start = new Date(); // Mark the starting time
			hashload input = new hashload();
			input.generate(pagesize); // Do the operations
			Date finish = new Date(); // Mark the ending time
			System.out.println("Time to generate hash in milliseconds is " + (finish.getTime() - start.getTime()));
			// Compare to get the running time
		} catch (Exception e) {
			// System.err.println("Can't get the input");
			e.printStackTrace();
		}
	}

	// To print the result and implement the operations
	private void generate(int pagesize) {
		int hashtablesize = 	1023; // Set the hash table size
		int recordNo = 2000000; // The number of records in the heap file
		double percent = 0.7; // The occupancy

		int bucketsize = 2000;//(int) (recordNo/hashtablesize/percent); // Set the bucket size
		ArrayList<String> hashdata = new ArrayList<String>(); // Set
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
				for (int i = 0; i < recordno; i++) { // Fill in the data of record position
					System.arraycopy(page, savebyte * (i + nextindex), index, startposition, savebyte);
					position[i] = Searching.byteToShort(index);
					// System.out.println(position[i]);
				}
				for (int i = 0; i < (recordno - nextindex); i++) { // Fill in the data of record length
					length[i] = position[i + nextindex] - position[i];
					// System.out.println(length[i]);
				}
				System.arraycopy(page, (position[(recordno - 2)] + length[(recordno - 2)] + indexlength), index, 0, 2);
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
					// System.out.println(BNname+pageno);

					int hashcode = (Math.abs(BNname.hashCode())) % hashtablesize;
					// System.out.println(hashcode);
					hashdata.add("" + hashcode);
					hashdata.add(BNname);
					hashdata.add("" + pageno);

				}

				pageno++;
				//if (pageno == 5)
				//	break;

			}
			int no = 0; // Mark the number of records in the hash table
			int content =3; // The number of array 
			Hashtable<String, String> table = new Hashtable<String, String>(); // Hash to store the (text, page number)
			ArrayList tables = new ArrayList(); // Create tables in array list to store content of table
			
			for (int i = 0; i < hashtablesize; i++) {
			tables.add(new Hashtable<String, String>()); 
			}
		
				for (int j = 0; j < (hashdata.size() / content); j++) {
					// Save in current bucket
					if (((Hashtable<String, String>) tables.get(Integer.parseInt(hashdata.get(content * j)))).size()<bucketsize) {
						Hashtable<String, String> a = new Hashtable <String, String>();
						a = (Hashtable<String, String>) tables.get(Integer.parseInt(hashdata.get(content * j)));
						// System.out.println(a.size());
					a.put(hashdata.get(content * j + 1), hashdata.get(content * j+2));
					no++;
					}
					// Overflow to next bucket
					else { 
						int number = Integer.parseInt(hashdata.get(content * j));
						do {
							if (number == 1022) {
								number = 0;
							}
							else {
							number++;
							}
						}while (((Hashtable<String, String>) tables.get(number)).size()==bucketsize);
						((Hashtable<String, String>) tables.get(number)).put(hashdata.get(content * j + 1), hashdata.get(content * j+2));
						no++;
						}
				}
				/**
			
			for (int j = 0; j < (hashdata.size() / content); j++) {
				// Save in current bucket
				((Hashtable<String, String>) tables.get(Integer.parseInt(hashdata.get(content * j)))).put(hashdata.get(content * j + 1), hashdata.get(content * j+2));
				no++;
				}
			 **/
				
					
					for (int i =0;i<hashtablesize;i++) {
						File hashfile = new File("hash4096/" + "hash" + i + "." + pagesize);
						FileOutputStream hash = new FileOutputStream(hashfile);
						ObjectOutputStream hashout = new ObjectOutputStream(hash); // Write into Hash file
						hashout.writeObject((Hashtable<String, String>)tables.get(i));
						Hashtable<String, String> a = new Hashtable <String, String>();
						a = (Hashtable<String, String>) tables.get(i);
						System.out.println(a.size());
						hashout.flush();
						hashout.close();	
					}
					
					
			System.out.println(no);

		} catch (Exception e) {
			// System.err.println("Incorrect input");
			e.printStackTrace();
		}

	}
}
