/**
 * @Date 06/05/2018
 * @author You Hao s3583715
 * @Description: Hashtables class. A constructor for hash tables.
 * @Version 1.0
 **/

public class Hashcode {
	String BNname; // The BN name
	int PN; // The page number

	public Hashcode(String BNname, int PN) {
		this.BNname = BNname;
		this.PN = PN;
	}

	public void SetBNname(String BNname) {
		this.BNname = BNname;
	}

	public void SetPN(int PN) {
		this.PN = PN;
	}

	// Get hash code of the BN name
	public int getHashcode() {
		return BNname.hashCode(); // Get the current usage
	}

	// Get the BN name
	public String getBNname() {
		return BNname; // Get the current usage
	}

	// Get the page number
	public int getPN() {
		return PN; // Get the current usage
	}

}
