package Read;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.ArrayList;

import DBS.Fields;
import DBS.Pages;
import DBS.Records;

public class dbload {
	public static void main(String[] args) {
		int currentPageSize = 0;
		int addtionalPageSize = 0;
		int pagesize;
		String datafile;

		try {
			if (args[0].equals("-p")) {
				pagesize = Integer.parseInt(args[1]);
				datafile = args[2];
			} else {
				pagesize = Integer.parseInt(args[2]);
				datafile = args[0];
			}

			BufferedReader file = new BufferedReader(new FileReader(datafile));
			String line = null;
			ArrayList<Pages> pages = new ArrayList<Pages>(); // Use arraylist to save records
			Pages page = new Pages(pagesize);
			file.readLine();
			while ((line = file.readLine()) != null) {
				String item[] = line.split("\t"); // Use item to store all the information in the csv
				if (item.length < 9)
					continue; // For the record with lack of information, we will skip recording it
				Fields[] field = new Fields[9]; // Use 9 fields to store one record
				int fieldIndex = 0;

				while (fieldIndex < 9) {
					field[fieldIndex] = new Fields(item[fieldIndex], fieldIndex);
					fieldIndex++;
				}

				Records record = new Records(field); // Create record to save lengths and fields (one tier)

				if ((page.getCurrent() + record.getLength() + 2) <= pagesize) {
					page.addRecord(record);
				} else {

					pages.add(page);
					page = new Pages(pagesize);
					page.addRecord(record);

				} // Use 'pages' to store all pages within required pagesize

			}

			Readfile heap = new Readfile("heap." + pagesize);

			for (int i = 0; i < pages.size(); i++) {
				if (pages.get(i).getRecord().size() < 30)
					System.out.println("");

				heap.writeShort((short) pages.get(i).getRecord().size());
				int index = 2 + pages.get(i).getRecord().size() * 2; // The total length for this page

				for (int n = 0; n < pages.get(i).getRecord().size(); n++) { // Write the page index
					heap.writeShort((short) index); // The start index for the current record
					index += pages.get(i).getRecord().get(n).getLength(); // The start index for next record
				}

				for (int n = 0; n < pages.get(i).getRecord().size(); n++) {
					int indexRecord = 18;
					heap.writeShort((short) indexRecord);
					for (int l = 0; l < 8; l++) { // Write the record
						indexRecord += pages.get(i).getRecord().get(l).getRecord().length;
						heap.writeShort((short) indexRecord);
					}

					for (int m = 0; m < 9; m++) { // Write the field
						heap.write(pages.get(i).getRecord().get(n).getRecord()[m]); // Write BI for a field
					}
				}
				int restCapacity = (pagesize - pages.get(i).getCurrent());
				if (restCapacity < 0)
					System.out.println("");
				byte rest[] = new byte[restCapacity];
				heap.writeBinary(rest);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println(e.get);
			System.err.println("Sorry, file can't be read.");
		}
		// load the csv data into 'file'
	}
}
