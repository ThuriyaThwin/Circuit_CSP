import java.util.Comparator;



public class lcvNode implements Comparable<lcvNode>{
	// Integer representation of the value
	private int value;
	// "Score"
	private int goodness;
	public lcvNode(int v){
		value = v;
		goodness = 0;
	}
	public int getValue(){
		return value;
	}
	public int getGood(){
		return goodness;
	}
	public void incGood(){
		goodness++;
	}
	@Override
	public int compareTo(lcvNode o) {
		return -(this.goodness - o.getValue());
	}
}