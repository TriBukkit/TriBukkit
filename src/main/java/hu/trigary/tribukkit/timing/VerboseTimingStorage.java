package hu.trigary.tribukkit.timing;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class VerboseTimingStorage extends TimingStorage {
	private final List<Integer> records = new ArrayList<>();
	
	VerboseTimingStorage(String id) {
		super(id);
	}
	
	
	
	public IntStream getRawMillis() {
		return records.stream().mapToInt(i -> i);
	}
	
	public int getMeanMillis() {
		return (int) Math.round(getRawMillis().average().orElse(-1));
	}
	
	public int getMedianMillis() {
		return getRawMillis().sorted().skip(records.size() / 2).findFirst().orElse(-1);
	}
	
	public int getMinMillis() {
		return getRawMillis().min().orElse(-1);
	}
	
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
