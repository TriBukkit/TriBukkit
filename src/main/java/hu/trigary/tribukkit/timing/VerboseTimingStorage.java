package hu.trigary.tribukkit.timing;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * A {@link TimingStorage} implementation which has an O(N) space complexity.
 * {@link CompactTimingStorage} should be enough for most cases,
 * but this class is useful for graphs, etc.
 */
public class VerboseTimingStorage extends TimingStorage {
	private final List<Integer> records = new ArrayList<>();
	
	VerboseTimingStorage(String id) {
		super(id);
	}
	
	
	
	/**
	 * Gets all measured elapsed times in milliseconds.
	 * The entries are ordered.
	 *
	 * @return all measured delta times
	 */
	public IntStream getRawMillis() {
		return records.stream().mapToInt(i -> i);
	}
	
	/**
	 * Gets the mean average of all measured elapsed times in milliseconds.
	 *
	 * @return the mean of delta times
	 */
	public int getMeanMillis() {
		return (int) Math.round(getRawMillis().average().orElse(-1));
	}
	
	/**
	 * Gets the median average of all measured elapsed times in milliseconds.
	 *
	 * @return the median of delta times
	 */
	public int getMedianMillis() {
		return getRawMillis().sorted().skip(records.size() / 2).findFirst().orElse(-1);
	}
	
	/**
	 * Gets the lowest measured elapsed time in milliseconds.
	 *
	 * @return the lowest delta time
	 */
	public int getMinMillis() {
		return getRawMillis().min().orElse(-1);
	}
	
	/**
	 * Gets the highest measured elapsed time in milliseconds.
	 *
	 * @return the highest delta time
	 */
	public int getMaxMillis() {
		return getRawMillis().max().orElse(-1);
	}
	
	
	
	@Override
	void addRecord(int millis) {
		records.add(millis);
	}
	
	
	
	@Override
	public String toString() {
		return "Timing region: " + getId() + System.lineSeparator()
				+ " - Mean:   " + getMeanMillis() + System.lineSeparator()
				+ " - Median: " + getMedianMillis() + System.lineSeparator()
				+ " - Min: " + getMinMillis() + System.lineSeparator()
				+ " - Max: " + getMaxMillis();
	}
}
