package hu.trigary.tribukkit.region;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A region which compares world names, and has a valid (rectangular) range for X, Z and Y coordinates.
 */
public class BoxRegion extends RectangleRegion {
	private final int minY;
	private final int maxY;
	
	/**
	 * Creates a new region instance with the specified options.
	 *
	 * @param world the world name to compare
	 * @param xLimitAlpha one of the ends of the valid X coordinate range
	 * @param xLimitBeta the other end of the valid X coordinate range
	 * @param yLimitAlpha one of the ends of the valid Y coordinate range
	 * @param yLimitBeta the other end of the valid Y coordinate range
	 * @param zLimitAlpha one of the ends of the valid Z coordinate range
	 * @param zLimitBeta the other end of the valid Z coordinate range
	 */
	public BoxRegion(@Nullable String world, int xLimitAlpha, int xLimitBeta,
			int yLimitAlpha, int yLimitBeta, int zLimitAlpha, int zLimitBeta) {
		super(world, xLimitAlpha, xLimitBeta, zLimitAlpha, zLimitBeta);
		minY = Math.min(yLimitAlpha, yLimitBeta);
		maxY = Math.max(yLimitAlpha, yLimitBeta);
	}
	
	/**
	 * Creates a new region instance with the specified options.
	 *
	 * @param world the world name to compare
	 * @param cornerAlpha an object containing the min/max X, the min/max Z and the min/max Y coordinate
	 * @param cornerBeta an object containing the min/max X, the min/max Z and the min/max Y coordinate
	 */
	public BoxRegion(@Nullable String world, @NotNull Vector cornerAlpha, @NotNull Vector cornerBeta) {
		this(world, cornerAlpha.getBlockX(), cornerBeta.getBlockX(), cornerAlpha.getBlockY(),
				cornerBeta.getBlockY(), cornerAlpha.getBlockZ(), cornerBeta.getBlockZ());
	}
	
	/**
	 * Creates a new region instance with the specified options.
	 *
	 * @param world the world name to compare
	 * @param cornerAlpha an object containing the min/max X, the min/max Z and the min/max Y coordinate
	 * @param cornerBeta an object containing the min/max X, the min/max Z and the min/max Y coordinate
	 */
	public BoxRegion(@Nullable String world, @NotNull Location cornerAlpha, @NotNull Location cornerBeta) {
		this(world, cornerAlpha.toVector(), cornerBeta.toVector());
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
