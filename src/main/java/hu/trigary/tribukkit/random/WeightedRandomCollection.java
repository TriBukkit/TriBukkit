package hu.trigary.tribukkit.random;

import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class WeightedRandomCollection<E> {
	private final NavigableMap<Double, E> entries = new TreeMap<>();
	private final double weightSum;
	
	public <T> WeightedRandomCollection(@NotNull Collection<T> source,
			@NotNull Function<T, E> entryExtractor, @NotNull Function<T, Double> weightExtractor) {
		Validate.isTrue(!source.isEmpty());
		double sum = 0;
		for (T value : source) {
			entries.put(sum, entryExtractor.apply(value));
			Double weight = weightExtractor.apply(value);
			Validate.isTrue(weight != null && weight > 0);
			sum += weight;
		}
		weightSum = sum;
	}
	
	public WeightedRandomCollection(@NotNull Collection<E> source,
			@NotNull Function<E, Double> weightExtractor) {
		this(source, Function.identity(), weightExtractor);
	}
	
	public WeightedRandomCollection(@NotNull Map<E, Double> source) {
		this(source.entrySet(), Map.Entry::getKey, Map.Entry::getValue);
	}
	
	
	
	@Contract(pure = true)
	public E getRandom() {
		return entries.floorEntry(ThreadLocalRandom.current().nextDouble(weightSum)).getValue();
	}
	
	@NotNull
	@Contract(pure = true)
	public Collection<E> getEntries() {
		return Collections.unmodifiableCollection(entries.values());
	}
}
