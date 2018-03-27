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

		try {
			BufferedReader file = new BufferedReader(new FileReader(args[2]));
			String line = null;
			ArrayList<Pages> pages = new ArrayList<Pages>(); // Use arraylist to save records
			Pages page = new Pages(Integer.parseInt(args[1]));
			boolean space = true;
			file.readLine();
			while ((line = file.readLine()) != null) {
				
				
				String item[] = line.split("\t"); // Use item to store all the information in the csv
				
				if (item.length < 9)
					continue; // For the record with lack of information, we will skip recording it
				
				Fields[] field = new Fields[9]; // Use 9 fields to store one record
				int fieldIndex=0;

				while (fieldIndex < 9){
					
					
					field[fieldIndex] = new Fields(item[fieldIndex],fieldIndex);
					
					
					
					fieldIndex++;
				
				}
				
				Records record = new Records(field); // Create record to save lengths and fields (one tier)
				
				if ((page.getCurrent() + record.getLength()) <= Integer.parseInt(args[1])) {
				page.addRecord(record);
				}
				else {
					pages.add(page);
					page = new Pages(Integer.parseInt(args[1]));
					page.addRecord(record);
					
					
					
					if (pages.size()==5) {
						for (int i=0; i<pages.size();i++) {
							for(int n=0;n<pages.get(i).getRecord().size();n++) {
								for(int m=0;m<pages.get(i).getRecord().get(n).getRecord().length;m++) {
									System.out.print(pages.get(i).getRecord().get(n).getRecord()[m].getContent()+",");
								}
								System.out.println("");
							}
						}
						break;
					}
					
					
					
				} // Use 'pages' to store all pages within required pagesize
				
			
			}
			
			Readfile heap = new Readfile("heap.4096");
			
			for (int i=0; i<pages.size(); i++) {
				heap.writeShort ((short)pages.get(i).getRecord().size());
				int index = 2 + pages.get(i).getRecord().size() * 2; // The total length for this page
				
				for (int n=0; n<pages.get(i).getRecord().size(); n++) {
					heap.writeInt (index); // The start index for the current record
					index += pages.get(i).getRecord().get(n).getLength(); // The start index for next record
				}
				
				for (int n=0; n<pages.get(i).getRecord().size(); n++) {
					for(int m=0;m<pages.get(i).getRecord().get(n).getRecord().length;m++) {
					heap.write (pages.get(i).getRecord().get(n).getRecord()[m]); // Write BI for a field
				}
				}
				
				
				
			}
		}catch (Exception e) {
			e.getStackTrace();
			System.err.println("Sorry, file can't be read.");
		}
		// load the csv data into 'file'	
	}
}
