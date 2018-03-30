package Read;

import java.io.DataOutputStream;
import java.io.FileOutputStream;

import DBS.Fields;

/**
 * @Date 28/03/2018
 * @author You Hao s3583715
 * @Description: Writing class. Convert into binary for all contents
 * @Version 1.0
 */

public class Writing {
	DataOutputStream os;

	public Writing(String Filename) { // Build a data output Stream
		try {
			os = new DataOutputStream(new FileOutputStream(Filename));
		} catch (Exception e) {
			System.err.println("Sorry, can't find the input.");
		}
	}

	public void write(Fields field) { // Write BI for a field
		try {
			// Distinguish on its type
			if (field.getType() == "String") {
				writeBinary(field.getContent().getBytes("UTF-8")); // Write BI for a String
			} else {
				os.writeLong(Long.valueOf(field.getContent())); // Write BI for a Long
			}
		} catch (Exception e) {
			System.err.println("Sorry, can't convert this field.");
		}
	}

	public void writeInt(int outInt) {
		try {
			os.writeInt(outInt); // Write BI for a Int
		} catch (Exception e) {
			System.err.println("Sorry, can't convert this int.");
		}
	}

	public void writeShort(short outShort) {
		try {
			os.writeShort(outShort); // Write BI for a Short
		} catch (Exception e) {
			System.err.println("Sorry, can't convert this Short.");
		}
	}

	public void writeBinary(byte[] bytes) {
		try {
			os.write(bytes); // Write BI for a Short
		} catch (Exception e) {
			System.err.println("Sorry, can't convert this byte.");
		}
	}

}
