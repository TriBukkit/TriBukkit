package hu.trigary.tribukkit.timing;

import hu.trigary.tribukkit.TriJavaPlugin;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TimingManager {
	private static Map<String, TimingStorage> storageMap;
	private static Function<String, TimingStorage> storageConstructor;
	
	public static synchronized void enable(boolean verbose) {
		Validate.isTrue(storageMap == null, "TimingManager is already enabled");
		storageMap = new HashMap<>();
		storageConstructor = verbose ? VerboseTimingStorage::new : CompactTimingStorage::new;
		TimingRegion.cache = new ArrayDeque<>();
	}
	
	
	
	public static synchronized void printData(@NotNull String id) {
		if (assertEnabled()) {
			TriJavaPlugin.getInstance().getLogger().info(getStorage(id).toString());
		}
	}
	
	public static synchronized void printAllData() {
		if (assertEnabled()) {
			TriJavaPlugin.getInstance().getLogger().info("Printing all timing region data..."
					+ storageMap.values().stream().map(TimingStorage::toString)
					.collect(Collectors.joining(System.lineSeparator())));
		}
	}
	
	public static synchronized void handleData(@NotNull String id, @NotNull Consumer<TimingStorage> handler) {
		if (assertEnabled()) {
			handler.accept(getStorage(id));
		}
	}
	
	
	
	static synchronized void addRecord(String id, long startNano) {
		getStorage(id).addRecord((int) ((System.nanoTime() - startNano) / 1000000));
	}
	
	private static boolean assertEnabled() {
		if (storageMap != null) {
			return true;
		}
		TriJavaPlugin.getInstance().getLogger().severe("Attempted to access the TimingManager, but it is not enabled");
		return false;
	}
	
	private static TimingStorage getStorage(String id) {
		return storageMap.computeIfAbsent(id, storageConstructor);
	}
}
