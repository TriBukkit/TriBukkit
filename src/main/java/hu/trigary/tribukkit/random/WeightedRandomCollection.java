package hu.trigary.tribukkit.random;

import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

/**
 * A collection which can retrieve random entries from a collection of entry-weight pairs.
 * This class is preferred over {@link WeightedRandom}
 * when multiple random entries are needed from the same source for performance reasons.
 * <br><br>
 * The source must not be an empty collection.
 * The entries can be null and the same entry can be present multiple times in the source.
 * The weights must be non-null positive (non-zero and non-negative) values.
 *
 * @param <E> the type of the entry
 */
public class WeightedRandomCollection<E> {
	private final NavigableMap<Double, E> entries = new TreeMap<>();
	private final double weightSum;
	
	/**
	 * Constructs a new collection from the specified source.
	 * All weights are guaranteed to be acquired (via the {@code weightExtractor}), but not all entries:
	 * {@code entryExtractor} might not be called for each element of {@code source}.
	 *
	 * @param source the source of the entries and weights
	 * @param entryExtractor the function which gets an entry from a source element
	 * @param weightExtractor the function which gets a weight from a source element
	 * @param <T> the type of the source elements
	 */
	public <T> WeightedRandomCollection(@NotNull Collection<T> source,
			@NotNull Function<T, E> entryExtractor, @NotNull Function<T, Double> weightExtractor) {
		Validate.isTrue(!source.isEmpty(), "Source must not be empty");
		double sum = 0;
		for (T value : source) {
			entries.put(sum, entryExtractor.apply(value));
			Double weight = weightExtractor.apply(value);
			Validate.isTrue(weight != null && weight > 0, "Weights must be non-null positive values");
			sum += weight;
		}
		weightSum = sum;
	}
	
	/**
	 * Constructs a new collection from the specified source.
	 * {@code weightExtractor} is guaranteed to be called for each source element.
	 *
	 * @param source the entries themselves and the source of the weights
	 * @param weightExtractor the function which gets a weight from a source element
	 */
	public WeightedRandomCollection(@NotNull Collection<E> source,
			@NotNull Function<E, Double> weightExtractor) {
		this(source, Function.identity(), weightExtractor);
	}
	
	/**
	 * Constructs a new collection from the specified source.
	 *
	 * @param source the map containing the entry-weight pairs
	 */
	public WeightedRandomCollection(@NotNull Map<E, Double> source) {
		this(source.entrySet(), Map.Entry::getKey, Map.Entry::getValue);
	}
	
	
	
	/**
	 * Gets a random entry from this collection, while taking the weights into consideration.
	 *
	 * @return the randomly selected entry
	 */
	@Contract(pure = true)
	public E getRandom() {
		return entries.floorEntry(ThreadLocalRandom.current().nextDouble(weightSum)).getValue();
	}
	
	/**
	 * Gets an unmodifiable collection containing all stored entries.
	 *
	 * @return the immutable collection of entries
	 */
	@NotNull
	@Contract(pure = true)
	public Collection<E> getEntries() {
		return Collections.unmodifiableCollection(entries.values());
	}
}
