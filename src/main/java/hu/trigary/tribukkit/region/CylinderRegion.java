package hu.trigary.tribukkit.region;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CylinderRegion extends CircleRegion {
	private final int minY;
	private final int maxY;
	
	public CylinderRegion(@Nullable String world, int centerX, int centerZ,
			int radius, int yLimitAlpha, int yLimitBeta) {
		super(world, centerX, centerZ, radius);
		minY = Math.min(yLimitAlpha, yLimitBeta);
		maxY = Math.max(yLimitAlpha, yLimitBeta);
	}
	
	public CylinderRegion(@Nullable String world, @NotNull Vector center,
			int radius, int yLimitAlpha, int yLimitBeta) {
		this(world, center.getBlockX(), center.getBlockZ(), radius, yLimitAlpha, yLimitBeta);
	}
	
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
