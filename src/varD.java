import java.util.ArrayList;
import java.util.List;

public class varD {
	private int variable;
	private ArrayList<Integer> domains;
	// Constructor
	public varD(int var) {
		variable = var;
		domains = new ArrayList<Integer>();
	}
	// Get the variable
	public int getV() {
		return variable;
	}
	// Size of domain
	public int getdSize() {
		return domains.size();
	}
	// Get list of domain
	public ArrayList<Integer> getAllD() {
		return domains;
	}
	// Add value to domain
	public void addD(int dom) {
		domains.add(dom);
	}
	// Remove a value from domain
	public void removeD(int dom) {
		if (domains.contains(dom)) {
			domains.remove(domains.indexOf(dom));
		}
	}
	// Reset the domain to a another version - "undo"
	public void reset(varD copy) {
		variable = copy.getV();
		domains.clear();
		domains.addAll(copy.getAllD());
	}
}