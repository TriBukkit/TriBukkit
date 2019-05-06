package hu.trigary.tribukkit.timing;

/**
 * A {@link TimingStorage} implementation which has an O(1) space complexity.
 * Should be enough for most cases, but for graphs, etc. {@link VerboseTimingStorage} can be used.
 */
public class CompactTimingStorage extends TimingStorage {
	private int sum;
	private int count;
	private int min;
	private int max;
	
	CompactTimingStorage(String id) {
		super(id);
	}
	
	
	
	/**
	 * Gets the sum of all measured elapsed times in milliseconds.
	 *
	 * @return the sum of delta times
	 */
	public int getMillisSum() {
		return sum;
	}
	
	/**
	 * Gets the count of measurements associated with this ID which took place.
	 *
	 * @return the count of measurements linked to this ID
	 */
	public int getRecordCount() {
		return count;
	}
	
	/**
	 * Gets the mean average of all measured elapsed times in milliseconds.
	 *
	 * @return the mean of delta times
	 */
	public int getMeanMillis() {
		return Math.round((float) sum / count);
	}
	
	/**
	 * Gets the lowest measured elapsed time in milliseconds.
	 *
	 * @return the lowest delta time
	 */
	public int getMinMillis() {
		return min;
	}
	
	/**
	 * Gets the highest measured elapsed time in milliseconds.
	 *
	 * @return the highest delta time
	 */
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
