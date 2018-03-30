package Read;

import java.io.File;
import java.io.FileInputStream;

public class dbquery {
	public static void main(String[] args) {
		try {
			String text = args[0];
			int pagesize = Integer.parseInt(args[1]);
			String datafile = "heap." + args[1];
			dbquery a = new dbquery();
			a.search(text, pagesize, datafile);
		} catch (Exception e) {
			System.err.println("Can't get the input");
		}
	}

	private void search(String text, int pagesize, String datafile) {
		try {
			FileInputStream heap = new FileInputStream(new File(datafile));
		} catch (Exception e) {
			System.err.println("Can't find a result");
		}

	}
}
