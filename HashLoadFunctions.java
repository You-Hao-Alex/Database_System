
public class HashLoadFunctions {
	public void abc(int page, int pageNo,int pagesize, int empty, String heap) {
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
				int[] fieldPosition = new int[10]; // Store field position
				int[] fieldLength = new int[9]; // Store field length
				for (int j = 0; j < 10; j++) {
					byte[] indexs = new byte[2]; // Store index
					System.arraycopy(page, (position[i] + 2 * j), indexs, 0, 2);
					fieldPosition[j] = Searching.byteToShort(indexs); // Fill in field position
				}
				for (int j = 0; j < 9; j++) {
					fieldLength[j] = fieldPosition[j + 1] - fieldPosition[j]; // Fill in field length
				}
				byte[] BN = new byte[fieldLength[1]]; // Find the info in the second field
				System.arraycopy(page, position[i] + fieldPosition[1], BN, 0, fieldLength[1]);
				String BNname = new String(BN); // Get the BN name
				int hashcode = HashFunctions.gethashcode(BNname, hashtablesize);
				hashdata.add("" + hashcode);
				hashdata.add(BNname);
				hashdata.add("" + pageno);
			}
			pageno++;
		}
	}
}
