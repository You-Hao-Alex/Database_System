import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
	private static FileInputStream heap=null;
	private static int pageno=0;
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
				hashout.writeObject(tables.get(i));
				hashout.flush();
				hashout.close();
			} catch (Exception e) {
				e.printStackTrace();
				
				System.err.println("Can't find the " + hashfolder + "folder");
			}

		}
	}

	// This method is to read information from heap file
	public static ArrayList<Hashcode> readheap(int pagesize, int hashtablesize, ArrayList<Hashtables> tables, byte[] BN, ArrayList<Hashcode> hashdata, byte[] page) {
		int pageNo = 0; // Set the searching index
		//int pageno = 0; // Mark the page number of each company name
		int empty = -1; // The empty number of a heap
		int startposition = 0; // The start position of array copy
		int savebyte = 2; // The size of byte array to save index
		int nextindex = 1; // The position for next index
		int indexlength = 18; // The length of index in a record
		int recordlength = 10; // The number of field position array
		byte[] index = new byte[savebyte]; // Create an array to save 2 bytes
		if(heap==null) {
			try {
				heap = new FileInputStream(new File("heap." + pagesize));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			
			if((heap.read(page, pageNo, pagesize)) != empty) {
				hashdata = generate(savebyte, page, startposition, nextindex, indexlength, recordlength, pageno,
						hashtablesize, BN, index);
				pageno++;
			}
			else 
				{
					heap.close(); // Close the heap
					return null;//reach the end of file
				
				}
			
		} catch (Exception e) {
			//System.err.println("Can't find the heap file.");
			e.printStackTrace();
		} // Read from heap file
		return hashdata;
	}

	private static ArrayList<Hashcode> generate(int savebyte, byte[] page, int startposition, int nextindex,
			int indexlength, int recordlength, int pageno, int hashtablesize, byte[] BN, byte[] index) {
		ArrayList<Hashcode> hashdata=new ArrayList<>();
		System.arraycopy(page, startposition, index, startposition, savebyte); // Copy the index into index[]
		int recordno = Searching.byteToShort(index); // Convert into short and save it as an integer
		
		//int[] position = new int[Searching.byteToShort(index)]; // Store the starting position of records
		//int[] length = new int[Searching.byteToShort(index)]; // Store the length of records
		
		int positiona = 0;
		/**
		// Fill in the data of record position
		for (int i = 0; i < recordno; i++) {
			System.arraycopy(page, savebyte * (i + nextindex), index, startposition, savebyte);
			position[i] = Searching.byteToShort(index);
		}
		// Fill in the data of record length
		for (int i = 0; i < (recordno - nextindex); i++) {
			length[i] = position[i + nextindex] - position[i];
		}
		
		
		
		
		System.arraycopy(page, (position[(recordno - savebyte)] + length[(recordno - savebyte)] + indexlength), index,
				startposition, savebyte);
		length[(recordno - nextindex)] = Searching.byteToShort(index); // Length of the last record in this page
		
			**/
		for (int i = 0; i < recordno; i++) {
			if (i<(recordno-1)){
			System.arraycopy(page, savebyte * (i + nextindex), index, startposition, savebyte);
			positiona = Searching.byteToShort(index);
			BN = new byte[getfield(nextindex + nextindex, savebyte, page, positiona, startposition, i,index) 
			              - getfield(nextindex, savebyte, page, positiona, startposition, i,index)]; // Find the info in the second field
			System.arraycopy(page, positiona + getfield(nextindex, savebyte, page, positiona, startposition, i,index), BN, 0, getfield(nextindex + nextindex, savebyte, page, positiona, startposition, i,index) - getfield(nextindex, savebyte, page, positiona, startposition, i,index));
			
			addhash(hashdata,String.valueOf(HashFunctions.gethashcode(new String(BN), hashtablesize)),new String(BN),pageno);
			}
			else {
				System.arraycopy(page, savebyte * (i), index, startposition, savebyte); //record -1
				int firstposition = Searching.byteToShort(index);
				System.arraycopy(page, savebyte * (i-nextindex), index, startposition, savebyte); //record -2
				int secondposition = Searching.byteToShort(index);
				int firstlength = firstposition-secondposition; // record -2
				System.arraycopy(page, (secondposition + firstlength + indexlength), index,
						startposition, savebyte);
				int lastlength = Searching.byteToShort(index);
				
				System.arraycopy(page, savebyte * (i + nextindex), index, startposition, savebyte);
				positiona = Searching.byteToShort(index);
				BN = new byte[getfield(nextindex + nextindex, savebyte, page, positiona, startposition, i,index) 
				              - getfield(nextindex, savebyte, page, positiona, startposition, i,index)]; // Find the info in the second field
				System.arraycopy(page, positiona + getfield(nextindex, savebyte, page, positiona, startposition, i,index), BN, 0, getfield(nextindex + nextindex, savebyte, page, positiona, startposition, i,index) - getfield(nextindex, savebyte, page, positiona, startposition, i,index));
				
				addhash(hashdata,String.valueOf(HashFunctions.gethashcode(new String(BN), hashtablesize)),new String(BN),pageno);
			}
		}
		
		
		
		return hashdata;

	}

	private static void addhash(ArrayList<Hashcode> hashdata, String string, String string2, int pageno) {
		
		hashdata.add(new Hashcode(string2, pageno));
		
	}

	private static int getfield(int nextindex, int savebyte, byte[] page, int position, int startposition, int i, byte[] index) {
		//byte[] indexs = new byte[savebyte]; // Store index
		System.arraycopy(page, (position + savebyte * nextindex), index, startposition, savebyte);
		return Searching.byteToShort(index);
	}

}
