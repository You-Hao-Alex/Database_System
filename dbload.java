
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;

/**
 * @Date 28/03/2018
 * @author You Hao s3583715
 * @Description: dbload main class. Read the CSV file and export into heap file
 * @Version 1.0
 */

public class dbload {
	public static void main(String[] args) {
		int pagesize; // The input pagesize
		String datafile; // The input filename
		int recordNo = 0; // Count the number of records loaded
		int pageNo = 0; // Count the number of pages used
		Long createTime = null; // Count the number of milliseconds to create heap file

		try {
			if (args[0].equals("-p")) {
				// To distinguish the input information
				pagesize = Integer.parseInt(args[1]);
				datafile = args[2];
			} else {
				pagesize = Integer.parseInt(args[2]);
				datafile = args[0];
			}
			BufferedReader file = new BufferedReader(new FileReader(datafile));
			String line = null; // Use line to store the content in this line
			ArrayList<Pages> pages = new ArrayList<Pages>(); // Use ArrayList to save records
			Pages page = new Pages(pagesize); // Start with the first page
			file.readLine();
			while ((line = file.readLine()) != null) {
				String item[] = line.split("\t"); // Use item to store all the information in the CSV
				if (item.length < 9)
					continue; // For the record with lack of information, we will skip recording it
				Fields[] field = new Fields[9]; // Use 9 fields to store one record
				int fieldIndex = 0; // The field index in a record
				while (fieldIndex < 9) {
					// To store 9 fields into a record
					field[fieldIndex] = new Fields(item[fieldIndex], fieldIndex);
					fieldIndex++;
				}

				Records record = new Records(field); // Create record to save lengths and fields (one tier)
				recordNo++;
				if ((page.getCurrent() + record.getLength() + 2) <= pagesize) {
					// If capacity is enough, store this record in the current page
					page.addRecord(record);
				} else {
					// Otherwise, Store it in a new page
					/**
					 * if (pages.size() == 2) // For testing 
					 * break;
					 **/
					pages.add(page);
					page = new Pages(pagesize);
					pageNo++;
					page.addRecord(record);
				}
			}
			Date start = new Date(); // Mark the starting time
			Writing heap = new Writing("heap." + pagesize); // To transfer into heap file
			for (int i = 0; i < pages.size(); i++) {
				heap.writeShort((short) pages.get(i).getRecord().size()); // Store the number of records for this page
				int index = 2 + pages.get(i).getRecord().size() * 2; // The starting index of first record
																		// 2 for total record number

				for (int n = 0; n < pages.get(i).getRecord().size(); n++) { // Write the record index
					heap.writeShort((short) index); // The start index for the current record
					index += pages.get(i).getRecord().get(n).getLength(); // The start index for next record
				}

				for (int n = 0; n < pages.get(i).getRecord().size(); n++) {
					int indexRecord = 20; // 2*9 to store the index of 9 fields in a single record
					for (int l = 0; l < 9; l++) { // Write the field index
						heap.writeShort((short) indexRecord); // Write the index for each field
						// System.out.println(indexRecord);
						indexRecord += pages.get(i).getRecord().get(n).getRecord()[l].getLength();
					}
					heap.writeShort((short) indexRecord); // Write an ending index

					for (int m = 0; m < 9; m++) { // Write the field
						heap.write(pages.get(i).getRecord().get(n).getRecord()[m]); // Write BI for a field
					}
				}
				int restCapacity = pages.get(i).getRest(); // Get the rest capacity of the page
				byte rest[] = new byte[restCapacity];
				heap.writeBinary(rest); // Fill in " " to achieve page size
			}
			Date finish = new Date(); // Mark the ending time
			createTime = finish.getTime() - start.getTime(); // Compare to get the running time
		} catch (Exception e) {
			System.err.println("Sorry, file can't be read.");
		}
		// load the CSV data into 'heap'
		System.out.println("The number of records loaded is " + recordNo);
		System.out.println("The number of pages used is " + pageNo);
		System.out.println("The number of milliseconds to create the heap file is " + createTime);
	}
}
