package hu.trigary.tribukkit.random;

import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class WeightedRandom {
	
	@Contract(pure = true)
	public static <T, E> E get(@NotNull Collection<T> source,
			@NotNull Function<T, E> entryExtractor, @NotNull Function<T, Double> weightExtractor) {
		double weightSum = 0;
		E result = null;
		ThreadLocalRandom random = ThreadLocalRandom.current();
		for (T value : source) {
			Double weight = weightExtractor.apply(value);
			Validate.isTrue(weight != null && weight > 0);
			if (random.nextDouble(weightSum + weight) >= weightSum) {
				result = entryExtractor.apply(value);
			}
			weightSum += weight;
		}
		return result;
	}
	
	@Contract(pure = true)
	public static <E> E get(@NotNull Collection<E> source,
			@NotNull Function<E, Double> weightExtractor) {
		return get(source, Function.identity(), weightExtractor);
	}
	
	@Contract(pure = true)
	public static <E> E get(@NotNull Map<E, Double> source) {
		return get(source.entrySet(), Map.Entry::getKey, Map.Entry::getValue);
	}
}
