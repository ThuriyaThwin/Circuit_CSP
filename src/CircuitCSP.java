import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class CircuitCSP implements CSP {

	public HashMap<Integer, List<Integer>> aL;
	public String[] components = { "a", "b", "c", "e" };
	public int[] variables = { 0, 1, 2, 3 };
	public Constraint constraints;
	public ArrayList<Integer> domains = new ArrayList<Integer>();
	public List<varD> varDom;

	public HashMap<Integer, int[]> varKey;
	public HashMap<Integer, List<Integer>> domainKey;
	public int n = 10;
	public int m = 3;

	public void setUp() {

		// Fill up the domain with integers, 1 to n*m
		for (int i = 1; i <= n * m; i++) {
			domains.add(i);
		}

		// Create the adjacency list; each component can be anothers neighbor
		aL = new HashMap<Integer, List<Integer>>();
		aL.put(0, Arrays.asList(1, 2, 3));
		aL.put(1, Arrays.asList(0, 2, 3));
		aL.put(2, Arrays.asList(0, 1, 3));
		aL.put(3, Arrays.asList(0, 1, 2));

		// Map domain integers to their coordinates to aid in printing solution
		domainKey = new HashMap<Integer, List<Integer>>();
		int key = 0;
		for (int row = 1; row <= m; row++) {
			for (int col = 1; col <= n; col++) {
				key++;
				domainKey.put(key, Arrays.asList(col, row));
			}
		}

		// Map variables to and their dimensions
		varKey = new HashMap<Integer, int[]>();
		// number of col, rows
		varKey.put(0, new int[] { 3, 2 });
		varKey.put(1, new int[] { 5, 2 });
		varKey.put(2, new int[] { 2, 3 });
		varKey.put(3, new int[] { 7, 1 });

		// Initial processing stage, generate the domain for each variable, making sure the component will not be off the board
		varDom = new ArrayList<varD>();
		for (int i : variables) {
			varD varDomain = new varD(i);
			int[] pair = varKey.get(i);
			int dkey = 0;
			for (int j = 1; j <= m; j++) {
				for (int k = 1; k <= n; k++) {
					dkey++;
					if (pair[1] + j - 1 <= m) {
						if (pair[0] + k - 1 <= n) {
							varDomain.addD(dkey);
						}
					}
				}
			}
			varDom.add(varDomain);
		}
	}

	@Override
	public void fillTable() {

		// Table containing the constraints
		HashMap<ArrayList<Integer>, ArrayList<List<Integer>>> conTable = new HashMap<ArrayList<Integer>, ArrayList<List<Integer>>>();

		// Pick two components
		for (int i : aL.keySet()) {
			for (int j : aL.get(i)) {

				// Get their dimensions from hashmap
				int[] pair = varKey.get(i);
				int[] pair2 = varKey.get(j);
				
				// Prepare the key and value variables for insertion to table
				ArrayList<Integer> keyz = new ArrayList<Integer>();
				keyz.add(i);
				keyz.add(j);
				ArrayList<List<Integer>> valuez = new ArrayList<List<Integer>>();

				//Get the domain of both components
				for (int k : varDom.get(i).getAllD()) {
					for (int l : varDom.get(j).getAllD()) {

						// Got the domain
						List<Integer> icor = domainKey.get(k);
						List<Integer> jcor = domainKey.get(l);

						// Use hash map to find coordinates represented by the integers
						Integer[] icord = icor.toArray(new Integer[2]);
						Integer[] jcord = jcor.toArray(new Integer[2]);

					//Get top left and bottom right of each component
						int P1x = icord[0];
						int P1y = icord[1] + pair[1] - 1;
						int P2x = icord[0] + pair[0] -1;
						int P2y = icord[1];
						int P3x = jcord[0];
						int P3y = jcord[1] + pair2[1] - 1;
						int P4x = jcord[0] + pair2[0] -1;
						int P4y = jcord[1];
						
						// Check for overlap
						if(!(P4x < P1x || P4y > P1y || P3x > P2x || P3y < P2y))
							continue;
						
						
						valuez.add(Arrays.asList(k, l));

					}
				}
				conTable.put(keyz, valuez);
			}
		}
		constraints = new Constraint(conTable);
	}

	@Override
	public varD getvarD(int i) {
		for (varD j : varDom) {
			if (j.getV() == i)
				return j;
		}
		return null;
	}

	@Override
	public List<varD> getvarDom() {
		return varDom;
	}

	@Override
	public void setvarDom(List<varD> copyThis) {
		varDom = copyThis;
	}

	@Override
	public int[] solve() {
		BackTracker solve = new BackTracker();
		 long s = System.nanoTime();

  

     

		int[] solution = solve.backTrackingSearch(this);
		
		long ss = System.nanoTime();
		System.out.println(solve.nodeCount);
		System.out.println(ss-s);

		return solution;
	}
	
	
	@Override
	public int[] getVariables() {
		return variables;
	}

	@Override
	public Constraint getConstraints() {
		return constraints;
	}

	@Override
	public HashMap<Integer, List<Integer>> getAdjacencyList() {
		return aL;
	}


}