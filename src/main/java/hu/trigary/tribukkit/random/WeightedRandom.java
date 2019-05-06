package hu.trigary.tribukkit.random;

import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

/**
 * A collection of methods which can randomly select a single entry from a collection of entry-weight pairs.
 * This class is preferred over {@link WeightedRandomCollection}
 * when only a single entry is needed from the same source for performance reasons.
 * <br><br>
 * If the source is an empty collection, then null will be returned by these methods.
 * The entries can be null and the same entry can be present multiple times in the source.
 * The weights must be non-null positive (non-zero and non-negative) values.
 */
public class WeightedRandom {
	
	/**
	 * Randomly selects a single entry from the source, while taking the weights into consideration.
	 * All weights are guaranteed to be acquired (via the {@code weightExtractor}), but not all entries:
	 * {@code entryExtractor} might not be called for each element of {@code source}.
	 *
	 * @param source the source of the entries and weights
	 * @param entryExtractor the function which gets an entry from a source element
	 * @param weightExtractor the function which gets a weight from a source element
	 * @param <T> the type of the source elements
	 * @param <E> the type of the entry
	 * @return the randomly selected entry or null, if the source is empty
	 */
	@Contract(pure = true)
	public static <T, E> E get(@NotNull Collection<T> source,
			@NotNull Function<T, E> entryExtractor, @NotNull Function<T, Double> weightExtractor) {
		double weightSum = 0;
		E result = null;
		ThreadLocalRandom random = ThreadLocalRandom.current();
		for (T value : source) {
			Double weight = weightExtractor.apply(value);
			Validate.isTrue(weight != null && weight > 0, "Weights must be non-null positive values");
			if (random.nextDouble(weightSum + weight) >= weightSum) {
				result = entryExtractor.apply(value);
			}
			weightSum += weight;
		}
		return result;
	}
	
	/**
	 * Randomly selects a single entry from the source, while taking the weights into consideration.
	 * {@code weightExtractor} is guaranteed to be called for each source element.
	 *
	 * @param source the entries themselves and the source of the weights
	 * @param weightExtractor the function which gets a weight from a source element
	 * @param <E> the type of the entry
	 * @return the randomly selected entry or null, if the source is empty
	 */
	@Contract(pure = true)
	public static <E> E get(@NotNull Collection<E> source,
			@NotNull Function<E, Double> weightExtractor) {
		return get(source, Function.identity(), weightExtractor);
	}
	
	/**
	 * Randomly selects a single entry (key) from the source,
	 * while taking the weights (values) into consideration.
	 *
	 * @param source the map containing the entry-weight pairs
	 * @param <E> the type of the entry
	 * @return the randomly selected entry or null, if the source is empty
	 */
	@Contract(pure = true)
	public static <E> E get(@NotNull Map<E, Double> source) {
		return get(source.entrySet(), Map.Entry::getKey, Map.Entry::getValue);
	}
}
