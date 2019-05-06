package hu.trigary.tribukkit.copysave;

/** @noinspection InterfaceMayBeAnnotatedFunctional*/
public interface CopyingSaveable<T> {
	T createAsyncSaveCopy();
	
	T createSyncSaveCopy();
}
