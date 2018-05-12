import java.util.Date;

/**
 * @Date 04/05/2018
 * @author You Hao s3583715
 * @Description: hashquery class. The main class to implement the query
 *               function. It will get the page number of target text from hash
 *               tables, then use the page number to get whole record from heap
 *               file.
 * @Version 1.0
 **/

public class hashquery {
	public static void main(String[] args) {
		try {
			int length = args.length; // Store the page size
			int pagesizeindex = length - 1; // Store the position of page size
			int pagesize = Integer.parseInt(args[pagesizeindex]); // Get the page size from input
			int hashtablesize = 1024 - 1; // Set the hash table size
			String text = args[0]; // Store the target text
			// Get all the content of text
			for (int i = 1; i < pagesizeindex; i++)
				text = text + " " + args[i];
			Date start = new Date(); // Mark the starting time
			HashFunctions.searchtext(text, pagesize, hashtablesize); // Implementation of searching
			Date finish = new Date(); // Mark the ending time
			System.out.println("Time to generate hash in milliseconds is " + (finish.getTime() - start.getTime()));
			// Compare to get the running time
		} catch (Exception e) {
			System.err.println("Incorret Input!");
		}
	}
}