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
	private static FileInputStream heap = null;
	private static int pageno = 0;
	private static int savebyte = 2; // The size of byte array to save index
	private static int nextindex = 1; // The position for next index
	private static int empty = -1; // The empty number of a heap
	private static byte[] index = new byte[savebyte]; // Create an array to save 2 bytes

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
				// e.printStackTrace();
				System.err.println("Can't find the " + hashfolder + "folder");
			}
		}
	}

	// This method is to read information from heap file
	public static ArrayList<Hashcode> readheap(int pagesize, int hashtablesize, ArrayList<Hashtables> tables, byte[] BN,
			ArrayList<Hashcode> hashdata, byte[] page) {
		int pageNo = 0; // Set the searching index

		if (heap == null) {
			try {
				heap = new FileInputStream(new File("heap." + pagesize));
			} catch (FileNotFoundException e) {
				System.out.println("Can't find the heap file");
			}
		}
		try {
			if ((heap.read(page, pageNo, pagesize)) != empty) {
				hashdata = generate(page, pageno, hashtablesize, BN);
				pageno++;
			} else {
				heap.close(); // Close the heap
				return null;// reach the end of file
			}
		} catch (Exception e) {
			System.err.println("Can't read from the heap file.");
		} // Read from heap file
		return hashdata;
	}

	private static ArrayList<Hashcode> generate(byte[] page, int pageno, int hashtablesize, byte[] BN) {
		ArrayList<Hashcode> hashdata = new ArrayList<>();
		System.arraycopy(page, 0, index, 0, savebyte); // Copy the index into index[]
		int recordno = Searching.byteToShort(index); // Convert into short and save it as an integer
		int positiona = 0; // The index of position
		for (int i = 0; i < recordno; i++) {
			System.arraycopy(page, savebyte * (i + nextindex), index, 0, savebyte);
			positiona = Searching.byteToShort(index);
			BN = new byte[getfield(nextindex + nextindex, page, positiona, i)
					- getfield(nextindex, page, positiona, i)];
			// Find the info in the second field
			System.arraycopy(page, positiona + getfield(nextindex, page, positiona, i), BN, 0,
					getfield(nextindex + nextindex, page, positiona, i) - getfield(nextindex, page, positiona, i));
			// System.out.println(new String(BN));
			addhash(hashdata, new String(BN), pageno);
		}
		return hashdata;
	}

	// To add a new record in hash data
	private static void addhash(ArrayList<Hashcode> hashdata, String BNname, int pageno) {
		hashdata.add(new Hashcode(BNname, pageno));
	}

	// To get the field position
	private static int getfield(int nextindex, byte[] page, int position, int i) {
		System.arraycopy(page, (position + savebyte * nextindex), index, 0, savebyte);
		return Searching.byteToShort(index);
	}
}