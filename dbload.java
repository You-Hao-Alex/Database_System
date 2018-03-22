package Read;

import java.io.BufferedReader;
import java.io.FileReader;

public class dbload {
	public static void main(String[] args) {
		String last;
		try {
			BufferedReader file = new BufferedReader(new FileReader("BUSINESS_NAMES_201803.csv"));
			String line = null;
			while ((line = file.readLine()) != null) {
				String item[] = line.split("/t");
				last = item[item.length-1];
				System.out.println(last);
			}
		}catch (Exception e) {
			System.err.println("Sorry, file can't be read.");
		}
		// load the csv data into 'file'
	}
}
