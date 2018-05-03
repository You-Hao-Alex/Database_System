import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;

public class hashquery {
	public static void main(String[] args) {
		try {
			int length = args.length; // Store the page size
			int pagesizeindex = length--; // Store the position of pagesize
			int pagesize = Integer.parseInt(args[pagesizeindex]); // Get the pagesize from input
			String text = args[0]; // Store the target text
			// Get all the content of text
			for (int i = 1; i < pagesizeindex; i++) {
				text = text + " " + args[i];
			}
			// System.out.println(text+" "+pagesize);
			Date start = new Date(); // Mark the starting time
			searchtext(text, pagesize); // Implementation of searching
			Date finish = new Date(); // Mark the ending time
			System.out.println("Time to generate hash in milliseconds is " + (finish.getTime() - start.getTime()));
			// Compare to get the running time
		} catch (Exception e) {
			System.err.println("Error");
		}
	}

	private static void searchtext(String text, int pagesize) {
		

	}
}
