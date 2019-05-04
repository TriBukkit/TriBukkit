package hu.trigary.tribukkit.timing;

public class CompactTimingStorage extends TimingStorage {
	private int sum;
	private int count;
	private int min;
	private int max;
	
	CompactTimingStorage(String id) {
		super(id);
	}
	
	
	
	public int getMillisSum() {
		return sum;
	}
	
	public int getRecordCount() {
		return count;
	}
	
	public int getMeanMillis() {
		return Math.round((float) sum / count);
	}
	
	public int getMinMillis() {
		return min;
	}
	
	public int getMaxMillis() {
		return max;
	}
	
	
	
	@Override
	void addRecord(int millis) {
		sum += millis;
		count++;
		min = Math.min(min, millis);
		max = Math.max(max, millis);
	}
	
	
	
	@Override
	public String toString() {
		return "Timing region: " + getId() + System.lineSeparator()
				+ " - Mean: " + getMeanMillis() + System.lineSeparator()
				+ " - Min: " + min + System.lineSeparator()
				+ " - Max: " + max;
	}
}
