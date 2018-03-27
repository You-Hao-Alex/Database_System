package Read;

import java.io.DataOutputStream;
import java.io.FileOutputStream;

import DBS.Fields;

public class Readfile {
	DataOutputStream os;
	
	public Readfile (String Filename) {
		try {
		os = new DataOutputStream (new FileOutputStream(Filename));
		}
		catch (Exception e){
			System.err.println("Sorry, file can't be output.");
		}
	}
	
	public void write (Fields field) {
		try {
		if (field.getType()=="String") {
			os.writeChars(field.getContent());
		}
		else {
			os.writeLong(Long.valueOf(field.getContent()).longValue());
		}
		}
		catch (Exception e){
			System.err.println("Sorry, file can't be output.");
		}
	}
	
	public void writeInt(int outInt){
		try {
		os.writeInt(outInt);
		}
		catch (Exception e){
			System.err.println("Sorry, file can't be output.");
		}
	}

	public void writeShort(short outShort){
		try {
			os.writeInt(outShort);
			}
			catch (Exception e){
				System.err.println("Sorry, file can't be output.");
			}
	}

}
