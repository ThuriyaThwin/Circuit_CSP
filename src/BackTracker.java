import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BackTracker {

	public int nodeCount;

	public int[] backTrackingSearch(CSP csp) {
		nodeCount = 0;
		int[] assignment = new int[csp.getVariables().length];
		Arrays.fill(assignment, 0);
		assignment = backTrack(assignment, csp);
		return assignment;
	}

	public int[] backTrack(int[] assignment, CSP csp) {

		// If the assignment is complete then return assignment
		if (complete(assignment)) {
			return assignment;
		}
		// Var -> select an unassigned variable

		// For each value in the domain

		// save domain values

		List<varD> save1 = new ArrayList<varD>();
		save1 = copyD(csp.getvarDom());

		int var;
		var = selectUnassignedVariableMRV(assignment, csp, save1);

		for (int value : orderDomainValuesLCV(var, assignment, csp)) {
			nodeCount++;
			// save domain values

			List<varD> save2 = new ArrayList<varD>();
			save2 = copyD(csp.getvarDom());

			// Is value consistent with assignment
			assignment[var] = value;

			// If value is consistent with assignment
			if (consistentCheck(csp, assignment, var)) {

				// change the domain of var
				csp.getvarDom().get(var).getAllD().clear();
				csp.getvarDom().get(var).getAllD().add(value);

				// Add value to assignment, done
				// Inferences section

				boolean inferences = Inference(csp, var, assignment);

				// not false = not failure
				if (inferences) {
					// add inferences to assignment
					int[] result;
					result = backTrack(assignment, csp);
					if (result != null)
						return result;
				}
			}
			// Not consistent
			assignment[var] = 0;
			// Remove inferences from assignment

			// return domains

			csp.setvarDom(save2);

		}

		// return domains

		csp.setvarDom(save1);

		return null;
	}

	public int selectUnassignedVariable(int[] assignment, CSP csp) {
		int var = 0;
		// Find the first unassigned variable
		for (int i = 0; i < assignment.length; i++) {
			if (assignment[i] == 0) {
				var = i;
				break;
			}
		}
		return var;
	}

	// MRV heuristic, find variable with least values
	public int selectUnassignedVariableMRV(int[] assignment, CSP csp,
			List<varD> saved) {
		int var = 0;
		int tempsize = Integer.MAX_VALUE;
		// Search through all non-assigned variables
		for (int i = 0; i < saved.size(); i++) {
			if (assignment[i] != 0)
				continue;
			// Check the size of their domain, find smallest
			if (saved.get(i).getdSize() < tempsize) {
				tempsize = saved.get(i).getdSize();
				var = i;
			}

		}
		return var;
	}

	public ArrayList<Integer> orderDomainValues(int var, int[] assignment, CSP csp) {

		ArrayList<Integer> blah = new ArrayList<Integer>();
		blah.addAll(csp.getvarDom().get(var).getAllD());

		Collections.shuffle(blah);
		return blah;
	}

	public ArrayList<Integer> orderDomainValuesLCV(int var, int[] assignment,
			CSP csp) {
		// Find the neighbors of current variable
		List<Integer> neighbors = csp.getAdjacencyList().get(var);
		// No neighbors
		if (neighbors == null) {
			return csp.getvarDom().get(var).getAllD();
		}
		// Copy the domains as a lcvNode
		ArrayList<lcvNode> domains = new ArrayList<lcvNode>();
		for (int i : csp.getvarDom().get(var).getAllD()) {
			lcvNode lcv = new lcvNode(i);
			domains.add(lcv);
		}
		// Check each domain and see what each value's score is
		// We want to find the least constraining variable
		for (lcvNode node : domains) {
			// Go to each neighbor's domains
			for (int neighbor : neighbors) {
				// Get the domain
				ArrayList<Integer> nDom = csp.getvarDom().get(neighbor).getAllD();
				for (int ndomain : nDom) {
					// Check to see if is satisfied
					if (csp.getConstraints().isSatisfied(var, neighbor, node.getValue(),
							ndomain))
						// If it is, add one to score
						node.incGood();
				}
			}
		}
		// Sort the list of domains
		Collections.sort(domains);
		// Create new ordered list of domains to return
		ArrayList<Integer> orderedDomain = new ArrayList<Integer>();
		for (lcvNode ordered : domains) {
			orderedDomain.add(ordered.getValue());
		}
		return orderedDomain;
	}

	public boolean complete(int[] assignment) {
		// Check how many variables are assigned 0
		int zeroCount = 0;
		for (int i = 0; i < assignment.length; i++) {
			if (assignment[i] == 0)
				zeroCount++;
		}
		// Any 0s means there is unassigned variable
		if (zeroCount > 0)
			return false;
		else
			return true;
	}

	public boolean consistentCheck(CSP csp, int[] assignment, int var) {

		// Check each variable
		for (int i = 0; i < assignment.length; i++) {
			// As long as the variable isn't unassigned or the same
			if (assignment[i] != 0 && i != var) {
				// Check to see if the two are consistent
				if (!csp.getConstraints().isSatisfied(i, var, assignment[i],
						assignment[var]))
					return false;
			}
		}
		return true;
	}

	public boolean Inference(CSP csp, int var, int[] assignment) {
		// Get neighbors and find the unassigned ones
		List<Integer> neighbors = csp.getAdjacencyList().get(var);
		List<Integer> unassignedNeighbors = new ArrayList<Integer>();
		// No neighbors
		if (neighbors == null) {
			return true;
		}
		// Get unassigned neighbors and pass to MAC3
		for (int neighbor : neighbors) {
			if (assignment[neighbor] != 0)
				continue;
			unassignedNeighbors.add(neighbor);
		}
		return MAC3(csp, var, unassignedNeighbors);
	}

	// returns false if failure
	public boolean MAC3(CSP csp, int xi, List<Integer> xj) {

		Queue<int[]> fringe = new LinkedList<int[]>();
		// Add the initial unassigned neighbors
		for (int k : xj) {
			fringe.add(new int[] { xi, k });
		}
		// Simple AC-3 algorithm
		while (!fringe.isEmpty()) {
			int[] pair = fringe.poll();
			if (revise(csp, pair[0], pair[1])) {
				if (csp.getvarD(pair[0]).getdSize() == 0)
					return false;
				List<Integer> neighbors = csp.getAdjacencyList().get(pair[0]);
				for (int neighbor : neighbors) {
					if (neighbor == pair[1])
						continue;
					fringe.add(new int[] { neighbor, pair[0] });
				}
			}
		}
		return true;
	}

	public boolean revise(CSP csp, int xi, int xj) {
		// Revised or not
		boolean revised = false;
		// Get all values in its domain
		ArrayList<Integer> copy = new ArrayList<Integer>();
		copy.addAll(csp.getvarD(xi).getAllD());
		// Check to see constraints involving x are satisfied
		for (int x : copy) {
			boolean satisfied = false;
			for (int y : csp.getvarD(xj).getAllD()) {
				if (!csp.getConstraints().isSatisfied(xi, xj, x, y))
					satisfied = false;
				else {
					satisfied = true;
					break;
				}
			}
			// If not remove it
			if (!satisfied) {
				csp.getvarD(xi).removeD(x);
				revised = true;
			}
		}
		return revised;
	}

	public List<varD> copyD(List<varD> copyThis) {
		// Save the arraylist
		List<varD> saveD = new ArrayList<varD>();
		// Copy each of the domains
		for (varD i : copyThis) {
			varD newD = new varD(i.getV());
			newD.reset(i);
			saveD.add(newD);
		}
		return saveD;
	}

}