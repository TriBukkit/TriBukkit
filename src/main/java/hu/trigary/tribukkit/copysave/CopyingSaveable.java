package hu.trigary.tribukkit.copysave;

/** @noinspection InterfaceMayBeAnnotatedFunctional*/
public interface CopyingSaveable<T> {
	T createAsyncSaveCopy();
	
	default T createSyncSaveCopy() {
		return createAsyncSaveCopy();
	}
}
