package hu.trigary.tribukkit.region;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A region which compares world names, and has a valid (circular) range for X, Z coordinates.
 */
public class CircleRegion extends WorldRegion {
	private final int centerX;
	private final int centerZ;
	private final float radiusSquared;
	
	/**
	 * Creates a new region instance with the specified options.
	 *
	 * @param world the world name to compare
	 * @param centerX the center of the circle
	 * @param centerZ the center of the circle
	 * @param radius the radius of the circle
	 */
	public CircleRegion(@Nullable String world, int centerX, int centerZ, float radius) {
		super(world);
		this.centerX = centerX;
		this.centerZ = centerZ;
		radiusSquared = radius * radius;
	}
	
	/**
	 * Creates a new region instance with the specified options.
	 *
	 * @param world the world name to compare
	 * @param center the X, Z center of the circle
	 * @param radius  the radius of the circle
	 */
	public CircleRegion(@Nullable String world, @NotNull Vector center, float radius) {
		this(world, center.getBlockX(), center.getBlockZ(), radius);
	}
	
	/**
	 * Creates a new region instance with the specified options.
	 *
	 * @param world the world name to compare
	 * @param center the X, Z center of the circle
	 * @param radius  the radius of the circle
	 */
	public CircleRegion(@Nullable String world, @NotNull Location center, float radius) {
		this(world, center.toVector(), radius);
	}
	
	
	
	@Override
	@Contract(pure = true)
	public boolean isInside(@NotNull Location location) {
		if (!super.isInside(location)) {
			return false;
		}
		
		int x = location.getBlockX() - centerX;
		int z = location.getBlockZ() - centerZ;
		return x * x + z * z <= radiusSquared;
	}
}
