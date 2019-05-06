package hu.trigary.tribukkit.timing;

/**
 * A class which holds all measured data associated with a specific ID.
 */
public abstract class TimingStorage {
	private final String id;
	
	protected TimingStorage(String id) {
		this.id = id;
	}
	
	
	
	/**
	 * Gets the ID whose data this instance stores.
	 *
	 * @return the associated ID
	 */
	public String getId() {
		return id;
	}
	
	abstract void addRecord(int millis);
	
	/**
	 * Formats the contents of this instance into a {@link String}.
	 *
	 * @return the nicely formatted contents
	 */
	@Override
	public abstract String toString();
}
