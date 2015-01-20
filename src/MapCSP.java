import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MapCSP implements CSP {

	public HashMap<Integer, List<Integer>> aL;
	public String[] countries = { "WA", "NT", "SA", "Q", "NSW", "V", "T" };
	public String[] colors = { "Red", "Green", "Blue" };
	public int[] variables = { 0, 1, 2, 3, 4, 5, 6 };
	public ArrayList<Integer> domains = new ArrayList<Integer>();
	public Constraint constraints;

	public List<varD> varDom;

	public void setUp() {

		domains.add(1);
		domains.add(2);
		domains.add(3);

		aL = new HashMap<Integer, List<Integer>>();
		aL.put(0, Arrays.asList(1, 2));
		aL.put(1, Arrays.asList(0, 2, 3));
		aL.put(2, Arrays.asList(0, 1, 3, 4, 5));
		aL.put(3, Arrays.asList(1, 2, 4));
		aL.put(4, Arrays.asList(3, 2, 5));
		aL.put(5, Arrays.asList(4, 2));

		varDom = new ArrayList<varD>();

		for (int i : variables) {

			varD varDomain = new varD(i);
			for (int j : domains) {
				varDomain.addD(j);

			}

			varDom.add(varDomain);
		}

	}

	@Override
	public void fillTable() {
		HashMap<ArrayList<Integer>, ArrayList<List<Integer>>> constrainTable = new HashMap<ArrayList<Integer>, ArrayList<List<Integer>>>();
		for (int i : aL.keySet()) {
			for (int j : variables) {
				if (aL.get(i) == null) {
					if (i != j) {
						ArrayList<Integer> keyz = new ArrayList<Integer>();
						keyz.add(i);
						keyz.add(j);
						ArrayList<List<Integer>> valuez = new ArrayList<List<Integer>>();
						valuez.add(Arrays.asList(1, 1));
						valuez.add(Arrays.asList(1, 2));
						valuez.add(Arrays.asList(1, 3));
						valuez.add(Arrays.asList(2, 1));
						valuez.add(Arrays.asList(2, 2));
						valuez.add(Arrays.asList(2, 3));
						valuez.add(Arrays.asList(3, 1));
						valuez.add(Arrays.asList(3, 2));
						valuez.add(Arrays.asList(3, 3));
						constrainTable.put(keyz, valuez);
						continue;
					} else {
						continue;
					}
				}
				if (i == j || aL.get(i).contains(j))
					continue;
				ArrayList<Integer> keyz = new ArrayList<Integer>();
				keyz.add(i);
				keyz.add(j);
				ArrayList<List<Integer>> valuez = new ArrayList<List<Integer>>();
				valuez.add(Arrays.asList(1, 1));
				valuez.add(Arrays.asList(1, 2));
				valuez.add(Arrays.asList(1, 3));
				valuez.add(Arrays.asList(2, 1));
				valuez.add(Arrays.asList(2, 2));
				valuez.add(Arrays.asList(2, 3));
				valuez.add(Arrays.asList(3, 1));
				valuez.add(Arrays.asList(3, 2));
				valuez.add(Arrays.asList(3, 3));
				constrainTable.put(keyz, valuez);
			}
			if (aL.get(i) != null) {
				for (int k : aL.get(i)) {
					ArrayList<Integer> keyz = new ArrayList<Integer>();
					keyz.add(i);
					keyz.add(k);
					ArrayList<List<Integer>> valuez = new ArrayList<List<Integer>>();
					valuez.add(Arrays.asList(1, 2));
					valuez.add(Arrays.asList(1, 3));
					valuez.add(Arrays.asList(2, 1));
					valuez.add(Arrays.asList(2, 3));
					valuez.add(Arrays.asList(3, 1));
					valuez.add(Arrays.asList(3, 2));
					constrainTable.put(keyz, valuez);
				}
			}
		}
		constraints = new Constraint(constrainTable);
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