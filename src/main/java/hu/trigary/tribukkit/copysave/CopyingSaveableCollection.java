package hu.trigary.tribukkit.copysave;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Supplier;

public class CopyingSaveableCollection<S extends CopyingSaveable<D>, D> implements CopyingSaveable<Collection<D>> {
	private final Collection<S> source;
	private final Supplier<Collection<D>> copyCollectionConstructor;
	
	public CopyingSaveableCollection(@NotNull Collection<S> source,
			@NotNull Supplier<Collection<D>> copyCollectionConstructor) {
		//noinspection AssignmentOrReturnOfFieldWithMutableType
		this.source = source;
		this.copyCollectionConstructor = copyCollectionConstructor;
	}
	
	
	
	@Override
	public Collection<D> createAsyncSaveCopy() {
		Collection<D> copy = copyCollectionConstructor.get();
		for (S element : source) {
			copy.add(element.createAsyncSaveCopy());
		}
		return copy;
	}
	
	@Override
	public Collection<D> createSyncSaveCopy() {
		Collection<D> copy = copyCollectionConstructor.get();
		for (S element : source) {
			copy.add(element.createSyncSaveCopy());
		}
		return copy;
	}
}
