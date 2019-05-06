package hu.trigary.tribukkit.region;

import org.bukkit.Location;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A region which only compares world names.
 * For more information, see {@link Region} JavaDocs.
 */
public class WorldRegion extends Region {
	private final String world;
	
	/**
	 * Creates a region which only compares world names.
	 * For more information, see {@link Region} JavaDocs.
	 *
	 * @param world the world name to compare {@link Location}s with.
	 */
	public WorldRegion(@Nullable String world) {
		this.world = world;
	}
	
	
	
	@Override
	@Contract(pure = true)
	public boolean isInside(@NotNull Location location) {
		return world == null || location.getWorld() == null
				|| location.getWorld().getName().equals(world);
	}
}
