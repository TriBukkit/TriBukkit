package hu.trigary.tribukkit.region;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A region which compares world names, and has a valid (circular) range for X, Z coordinates.
 * The Y coordinates have a valid range as well.
 */
public class CylinderRegion extends CircleRegion {
	private final int minY;
	private final int maxY;
	
	/**
	 * Creates a new region instance with the specified options.
	 *
	 * @param world the world name to compare
	 * @param centerX the center of the circle
	 * @param centerZ the center of the circle
	 * @param radius the radius of the circle
	 * @param yLimitAlpha one of the ends of the valid Y coordinate range
	 * @param yLimitBeta the other end of the valid Y coordinate range
	 */
	public CylinderRegion(@Nullable String world, int centerX, int centerZ,
			int radius, int yLimitAlpha, int yLimitBeta) {
		super(world, centerX, centerZ, radius);
		minY = Math.min(yLimitAlpha, yLimitBeta);
		maxY = Math.max(yLimitAlpha, yLimitBeta);
	}
	
	/**
	 * Creates a new region instance with the specified options.
	 *
	 * @param world the world name to compare
	 * @param center the X, Z center of the circle
	 * @param radius  the radius of the circle
	 * @param yLimitAlpha one of the ends of the valid Y coordinate range
	 * @param yLimitBeta the other end of the valid Y coordinate range
	 */
	public CylinderRegion(@Nullable String world, @NotNull Vector center,
			int radius, int yLimitAlpha, int yLimitBeta) {
		this(world, center.getBlockX(), center.getBlockZ(), radius, yLimitAlpha, yLimitBeta);
	}
	
	/**
	 * Creates a new region instance with the specified options.
	 *
	 * @param world the world name to compare
	 * @param center the X, Z center of the circle
	 * @param radius  the radius of the circle
	 * @param yLimitAlpha one of the ends of the valid Y coordinate range
	 * @param yLimitBeta the other end of the valid Y coordinate range
	 */
	public CylinderRegion(@Nullable String world, @NotNull Location center,
			int radius, int yLimitAlpha, int yLimitBeta) {
		this(world, center.toVector(), radius, yLimitAlpha, yLimitBeta);
	}
	
	
	
	@Override
	@Contract(pure = true)
	public boolean isInside(@NotNull Location location) {
		if (!super.isInside(location)) {
			return false;
		}
		
		int y = location.getBlockY();
		return minY <= y && maxY >= y;
	}
}
