import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;



public interface CSP {
	// Solves the problem
	public int[] solve();
	// Get the variables in the problem
	public int[] getVariables();
	// Fill our constraint hash table
	public void fillTable();
	// Get our constraint class holding the hashtable
	public Constraint getConstraints();
	// Get the domains belonging to variable i
	public varD getvarD(int i);
	// Return the adjacency list for the variables
	public HashMap<Integer, List<Integer>> getAdjacencyList();
	// Get the list of all variables and their domain mapping
	public List<varD> getvarDom();
	// For use in "undo"-ing
	public void setvarDom(List<varD> copyThis);
}