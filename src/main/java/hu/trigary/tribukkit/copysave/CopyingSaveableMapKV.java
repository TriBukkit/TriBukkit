package hu.trigary.tribukkit.copysave;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Supplier;

public class CopyingSaveableMapKV<K extends CopyingSaveable<DK>, V extends CopyingSaveable<DV>, DK, DV>
		implements CopyingSaveable<Map<DK, DV>> {
	private final Map<K, V> source;
	private final Supplier<Map<DK, DV>> copyCollectionConstructor;
	
	public CopyingSaveableMapKV(@NotNull Map<K, V> source,
			@NotNull Supplier<Map<DK, DV>> copyCollectionConstructor) {
		//noinspection AssignmentOrReturnOfFieldWithMutableType
		this.source = source;
		this.copyCollectionConstructor = copyCollectionConstructor;
	}
	
	
	
	@Override
	public Map<DK, DV> createAsyncSaveCopy() {
		Map<DK, DV> copy = copyCollectionConstructor.get();
		source.forEach((key, value) -> copy.put(key.createAsyncSaveCopy(), value.createAsyncSaveCopy()));
		return copy;
	}
}
