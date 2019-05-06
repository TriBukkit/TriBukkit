package hu.trigary.tribukkit.timing;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Deque;

/**
 * A class which measures the elapsed time since it was acquired
 * (using {@link #of(String)}) until it is released (using {@link #close()}).
 * This delta time is then reported to {@link TimingManager}
 * together with the ID specified in {@link #of(String)}.
 * This class can be used on any thread, but it is not thread-safe.
 * Non-null instances of this class can only be acquired when the {@link TimingManager} is enabled.
 * This also means that everything will work normally (no extra steps are needed)
 * when the {@link #close()} method is called by try-with-resources.
 */
public class TimingRegion implements AutoCloseable {
	static Deque<TimingRegion> cache;
	private String id;
	private long startNano;
	
	private TimingRegion() {}
	
	/**
	 * Gets a new or internally cached instance with the specified ID set.
	 * Also marks the current timestamp as the start of the measurement.
	 *
	 * @param id the id of action being measured
	 * @return an instance or null, if {@link TimingManager} is not enabled
	 */
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
	
	/**
	 * Ends the measurement and sends the result (together with the
	 * previously specified ID) to the {@link TimingManager}.
	 * Should only be called once (eg. by try-with-resources) for each acquired instance.
	 */
	@Override
	public void close() {
		TimingManager.addRecord(id, startNano);
		cache.push(this);
	}
}
