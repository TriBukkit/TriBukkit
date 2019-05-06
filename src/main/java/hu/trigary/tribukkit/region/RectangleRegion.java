package hu.trigary.tribukkit.region;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A region which compares world names, and has a valid (rectangular) range for X, Z coordinates.
 * Y coordinates are not checked.
 */
public class RectangleRegion extends WorldRegion {
	private final int minX;
	private final int maxX;
	private final int minZ;
	private final int maxZ;
	
	/**
	 * Creates a new region instance with the specified options.
	 *
	 * @param world the world name to compare
	 * @param xLimitAlpha one of the ends of the valid X coordinate range
	 * @param xLimitBeta the other end of the valid X coordinate range
	 * @param zLimitAlpha one of the ends of the valid Z coordinate range
	 * @param zLimitBeta the other end of the valid Z coordinate range
	 */
	public RectangleRegion(@Nullable String world, int xLimitAlpha, int xLimitBeta, int zLimitAlpha, int zLimitBeta) {
		super(world);
		minX = Math.min(xLimitAlpha, xLimitBeta);
		maxX = Math.max(xLimitAlpha, xLimitBeta);
		minZ = Math.min(zLimitAlpha, zLimitBeta);
		maxZ = Math.max(zLimitAlpha, zLimitBeta);
	}
	
	/**
	 * Creates a new region instance with the specified options.
	 *
	 * @param world the world name to compare
	 * @param cornerAlpha an object containing the min/max X and the min/max Z coordinate
	 * @param cornerBeta an object containing the min/max X and the min/max Z coordinate
	 */
	public RectangleRegion(@Nullable String world, @NotNull Vector cornerAlpha, @NotNull Vector cornerBeta) {
		this(world, cornerAlpha.getBlockX(), cornerBeta.getBlockX(), cornerAlpha.getBlockZ(), cornerBeta.getBlockZ());
	}
	
	/**
	 * Creates a new region instance with the specified options.
	 *
	 * @param world the world name to compare
	 * @param cornerAlpha an object containing the min/max X and the min/max Z coordinate
	 * @param cornerBeta an object containing the min/max X and the min/max Z coordinate
	 */
	public RectangleRegion(@Nullable String world, @NotNull Location cornerAlpha, @NotNull Location cornerBeta) {
		this(world, cornerAlpha.toVector(), cornerBeta.toVector());
	}
	
	
	
	@Override
	@Contract(pure = true)
	public boolean isInside(@NotNull Location location) {
		if (!super.isInside(location)) {
			return false;
		}
		
		int x = location.getBlockX();
		int z = location.getBlockZ();
		return minX <= x && maxX >= x && minZ <= z && maxZ >= z;
	}
}
