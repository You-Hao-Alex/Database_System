package DBS;

import java.util.ArrayList;

public class Pages {
	private int max;
	private int current = 2;
	private int rest;
	ArrayList<Records> pages = new ArrayList<Records>();
	
    public Pages(int pagesize) {
        this.max = pagesize;
    }
    
    public int getCurrent() {
        return current;
    }
    
    public int getRest() {
        return (max-current);
    }
    
    public void addRecord(Records record) {
    		pages.add(record);
    		this.current += record.getLength()+2;
    }
    
    public ArrayList<Records> getRecord() {
    	return pages;
    }

}
