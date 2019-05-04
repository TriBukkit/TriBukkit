package hu.trigary.tribukkit.timing;

public abstract class TimingStorage {
	private final String id;
	
	protected TimingStorage(String id) {
		this.id = id;
	}
	
	
	
	public String getId() {
		return id;
	}
	
	abstract void addRecord(int millis);
	
	@Override
	public abstract String toString();
}
