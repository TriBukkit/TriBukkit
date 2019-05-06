package hu.trigary.tribukkit.copysave;

public interface CopyingSaveable<T> {
	T createAsyncSaveCopy();
	
	T createSyncSaveCopy();
}
