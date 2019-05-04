package hu.trigary.tribukkit.copysave;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Supplier;

public class CopyingSaveableMapValues<K, V extends CopyingSaveable<D>, D> implements CopyingSaveable<Map<K, D>> {
	private final Map<K, V> source;
	private final Supplier<Map<K, D>> copyCollectionConstructor;
	
	public CopyingSaveableMapValues(@NotNull Map<K, V> source,
			@NotNull Supplier<Map<K, D>> copyCollectionConstructor) {
		//noinspection AssignmentOrReturnOfFieldWithMutableType
		this.source = source;
		this.copyCollectionConstructor = copyCollectionConstructor;
	}
	
	
	
	@Override
	public Map<K, D> createAsyncSaveCopy() {
		Map<K, D> copy = copyCollectionConstructor.get();
		source.forEach((key, value) -> copy.put(key, value.createAsyncSaveCopy()));
		return copy;
	}
}
