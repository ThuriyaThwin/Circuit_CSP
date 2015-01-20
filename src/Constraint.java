import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Constraint{
	
	public static HashMap<ArrayList<Integer>, ArrayList<List<Integer>>> table;
	
	
	public Constraint(HashMap<ArrayList<Integer>, ArrayList<List<Integer>>> conTable){
		 table = conTable;
	}
	
	public boolean isSatisfied(int a, int b, int avalue, int bvalue){
		// The two variables
		ArrayList<Integer> keys = new ArrayList<Integer>();
		// The values assigned to them
		ArrayList<Integer> values = new ArrayList<Integer>();
		keys.add(a);
		keys.add(b);
		values.add(avalue);
		values.add(bvalue);
		
		
		ArrayList<Integer> keys2 = new ArrayList<Integer>();
		ArrayList<Integer> values2 = new ArrayList<Integer>();
		
		keys2.add(b);
		keys2.add(a);
		values2.add(bvalue);
		values2.add(avalue);
		
		
		
		if (table.get(keys).contains(values) || table.get(keys2).contains(values2)){
					return true;}
		else{
				return false;
		}	
	}
	
}