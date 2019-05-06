package hu.trigary.tribukkit.copysave;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Supplier;

public class CopyingSaveableMapKeys<K extends CopyingSaveable<D>, V, D> implements CopyingSaveable<Map<D, V>> {
	private final Map<K, V> source;
	private final Supplier<Map<D, V>> copyCollectionConstructor;
	
	public CopyingSaveableMapKeys(@NotNull Map<K, V> source,
			@NotNull Supplier<Map<D, V>> copyCollectionConstructor) {
		//noinspection AssignmentOrReturnOfFieldWithMutableType
		this.source = source;
		this.copyCollectionConstructor = copyCollectionConstructor;
	}
	
	
	
	@Override
	public Map<D, V> createAsyncSaveCopy() {
		Map<D, V> copy = copyCollectionConstructor.get();
		source.forEach((key, value) -> copy.put(key.createAsyncSaveCopy(), value));
		return copy;
	}
	
	@Override
	public Map<D, V> createSyncSaveCopy() {
		Map<D, V> copy = copyCollectionConstructor.get();
		source.forEach((key, value) -> copy.put(key.createSyncSaveCopy(), value));
		return copy;
	}
}
