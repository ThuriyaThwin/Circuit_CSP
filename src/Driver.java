import java.util.HashSet;
import java.util.List;

public class Driver{
	
	
	
	public static void main(String[] args) {
		CircuitCSP mcsp = new CircuitCSP();
		mcsp.setUp();
		
		List<varD> map = mcsp.getvarDom();
		
		
		mcsp.fillTable();
		int[] blah;
		blah = mcsp.solve();

		for(int j : blah){
			System.out.println(j);
		}
		
		
		
		
		
	}
	
	
}