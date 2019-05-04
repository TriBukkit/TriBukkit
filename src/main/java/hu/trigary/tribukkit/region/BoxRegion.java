package hu.trigary.tribukkit.region;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BoxRegion extends RectangleRegion {
	private final int minY;
	private final int maxY;
	
	public BoxRegion(@Nullable String world, int xLimitAlpha, int xLimitBeta,
			int yLimitAlpha, int yLimitBeta, int zLimitAlpha, int zLimitBeta) {
		super(world, xLimitAlpha, xLimitBeta, zLimitAlpha, zLimitBeta);
		minY = Math.min(yLimitAlpha, yLimitBeta);
		maxY = Math.max(yLimitAlpha, yLimitBeta);
	}
	
	public BoxRegion(@Nullable String world, @NotNull Vector cornerAlpha, @NotNull Vector cornerBeta) {
		this(world, cornerAlpha.getBlockX(), cornerBeta.getBlockX(), cornerAlpha.getBlockY(),
				cornerBeta.getBlockY(), cornerAlpha.getBlockZ(), cornerBeta.getBlockZ());
	}
	
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
