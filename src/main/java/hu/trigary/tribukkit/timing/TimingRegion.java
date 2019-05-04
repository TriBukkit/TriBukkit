package hu.trigary.tribukkit.timing;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Deque;

public class TimingRegion implements AutoCloseable {
	static Deque<TimingRegion> cache;
	private String id;
	private long startNano;
	
	private TimingRegion() {}
	
	@Nullable
	public static TimingRegion of(@NotNull String id) {
		if (cache == null) {
			return null;
		}
		
		TimingRegion region = cache.poll();
		if (region == null) {
			//noinspection resource
			region = new TimingRegion();
		}
		
		region.id = id;
		region.startNano = System.nanoTime();
		return region;
	}
	
	@Override
	public void close() {
		TimingManager.addRecord(id, startNano);
		cache.push(this);
	}
}
