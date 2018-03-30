package Read;

import java.util.ArrayList;

public class Searching {

	public static ArrayList<String[]> getRecord(String text, byte[] page) {
		byte[] index = new byte[2]; // Create an array to save 2 bytes

		System.arraycopy(page, 0, index, 0, 2); // Copy the first 2 bytes into index[]
		int recordno = Searching.byteToShort(index); // Convert these 2 bytes into short and save it as an integer

		int[] position = new int[(recordno)];
		int[] length = new int[recordno];
		for (int i = 0; i < recordno; i++) {
			System.arraycopy(page, 2 * (i + 1), index, 0, 2);
			position[i] = Searching.byteToShort(index);
			// System.out.println(position[i]);
		}
		for (int i = 0; i < (recordno - 1); i++) {
			length[i] = position[i + 1] - position[i];
			// System.out.println(length[i]);
		}
		System.arraycopy(page, (position[(recordno - 2)] + length[(recordno - 2)] + 18), index, 0, 2);
		length[(recordno - 1)] = Searching.byteToShort(index);
		// System.out.println(length[(recordno-1)]);
		ArrayList<Integer> resultIndex = Searching.getField(page, position, length, text);
		ArrayList<String[]> result = Searching.getResult(page, position, length, resultIndex);
		for(int i = 0; i<result.size();i++) {
			System.out.println(result.get(i)[0]);
			System.out.println(result.get(i)[1]);
		}
		return result;
	}

	private static ArrayList<String[]> getResult(byte[] page, int[] position, int[] length, ArrayList<Integer> resultIndex) {
		
		ArrayList<String[]> getResult = new ArrayList<String[]>();
		String[] a = new String[2];
		
		for (int i = 0; i < resultIndex.size(); i++) {
			byte[] index = new byte[(length[resultIndex.get(i)]-8-20)];
			System.arraycopy(page, position[resultIndex.get(i)]+20, index, 0, (length[resultIndex.get(i)]-8-20));
			String Eight = new String(index);
			byte[] nine = new byte[8];
			System.arraycopy(page, (position[resultIndex.get(i)+1]-8+20), nine, 0, 8);
			long Nine = Searching.byteToLong(nine);
			System.out.println(Nine+"abc");
			String Ninely = ""+Nine;
			a[0] = Eight;
			a[1] = Ninely;
			getResult.add(a);
			
		}

		return getResult;
	}

	private static ArrayList<Integer> getField(byte[] page, int[] position, int[] length, String text) {
		ArrayList<Integer> Target = new ArrayList<Integer>();
		for (int i = 0; i < 1; i++) {
			int[] fieldPosition = new int[10];
			int[] fieldLength = new int[9];
			for (int j = 0; j < 10; j++) {
				byte[] index = new byte[2];
				System.arraycopy(page, (position[i] + 2 * j), index, 0, 2);
				fieldPosition[j] = Searching.byteToShort(index);
				// System.out.println(fieldPosition[j]);
			}
			for (int j = 0; j < 9; j++) {
				fieldLength[j] = fieldPosition[j + 1] - fieldPosition[j];
				// System.out.println(fieldLength[j]);
			}
			byte[] BN = new byte[fieldLength[1]];
			System.arraycopy(page, position[i] + fieldPosition[1], BN, 0, fieldLength[1]);
			String BNname = new String(BN);

			if (BNname.contains(text))
				Target.add(i);
		}

		for (int j = 0; j < Target.size(); j++) {
			System.out.println(Target.get(j));
		}
		return Target;
	}

	public static short byteToShort(byte[] b) {
		short result;
		result = (short) (b[0] << 8 | b[1] & 0xff);
		return result;
	}

	public static long byteToLong(byte[] b) {
		long result = 0;
		for (int i = 0; i < 8; i++) {
			result <<= 8;
			result |= (b[i] & 0xff);
		}
		return result;
	}

}
