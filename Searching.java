

import java.util.ArrayList;

/**
 * @Date 31/03/2018
 * @author You Hao s3583715
 * @Description: Searching class. Most functions are operated in this class. Also converting functions
 * @Version 1.0
 */

public class Searching {
	public static ArrayList<String[]> getRecord(String text, byte[] page) {
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

	// Get the result and store them in an array list
	private static ArrayList<String[]> getResult(byte[] page, int[] position, int[] length,
			ArrayList<Integer> resultIndex) {
		ArrayList<String[]> getResult = new ArrayList<String[]>();
		String[] a = new String[2]; // To store the items in each field
		for (int i = 0; i < resultIndex.size(); i++) {
			byte[] index = new byte[(length[resultIndex.get(i)] - 8 - 20)];
			System.arraycopy(page, position[resultIndex.get(i)] + 20, index, 0, (length[resultIndex.get(i)] - 8 - 20));
			// Get the copy of the first 8 fields in this record
			String Eight = new String(index); // Store the first 8 fields' contents
			byte[] nine = new byte[8];
			System.arraycopy(page, (position[resultIndex.get(i)] + length[resultIndex.get(i)] - 8), nine, 0, 8);
			// Get the 9th field which is in Long
			long Nine = Searching.byteToLong(nine);
			String Ninely = "" + Nine; // Convert into String
			a[0] = Eight;
			a[1] = Ninely;
			System.out.println(a[0] + "" + a[1]); // Print the result
			getResult.add(a);
		}
		return getResult;
	}

	// Get the position of targeting records
	private static ArrayList<Integer> getField(byte[] page, int[] position, int[] length, String text) {
		ArrayList<Integer> Target = new ArrayList<Integer>();
		for (int i = 0; i < position.length; i++) {
			int[] fieldPosition = new int[10]; // Store field position
			int[] fieldLength = new int[9]; // Store field length
			for (int j = 0; j < 10; j++) {
				byte[] index = new byte[2]; // Store index
				System.arraycopy(page, (position[i] + 2 * j), index, 0, 2);
				fieldPosition[j] = Searching.byteToShort(index); // Fill in field position
				// System.out.println(fieldPosition[j]);
			}
			for (int j = 0; j < 9; j++) {
				fieldLength[j] = fieldPosition[j + 1] - fieldPosition[j]; // Fill in field length
				// System.out.println(fieldLength[j]);
			}
			byte[] BN = new byte[fieldLength[1]]; // Find the info in the second field
			System.arraycopy(page, position[i] + fieldPosition[1], BN, 0, fieldLength[1]);
			String BNname = new String(BN); // Get the BN name
			if (BNname.contains(text))
				Target.add(i); // Add the index to target ArrayList
		}
		return Target;
	}

	// Convert byte[] to short
	public static short byteToShort(byte[] index) {
		short result;
		result = (short) (index[0] << 8 | index[1] & 0xff);
		return result;
	}

	// Convert byte[] to Long
	public static long byteToLong(byte[] index) {
		long result = 0;
		for (int i = 0; i < 8; i++) {
			result <<= 8;
			result |= (index[i] & 0xff);
		}
		return result;
	}
}