package hu.trigary.tribukkit.region;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RectangleRegion extends WorldRegion {
	private final int minX;
	private final int maxX;
	private final int minZ;
	private final int maxZ;
	
	public RectangleRegion(@Nullable String world, int xLimitAlpha, int xLimitBeta, int zLimitAlpha, int zLimitBeta) {
		super(world);
		minX = Math.min(xLimitAlpha, xLimitBeta);
		maxX = Math.max(xLimitAlpha, xLimitBeta);
		minZ = Math.min(zLimitAlpha, zLimitBeta);
		maxZ = Math.max(zLimitAlpha, zLimitBeta);
	}
	
	public RectangleRegion(@Nullable String world, @NotNull Vector cornerAlpha, @NotNull Vector cornerBeta) {
		this(world, cornerAlpha.getBlockX(), cornerBeta.getBlockX(), cornerAlpha.getBlockZ(), cornerBeta.getBlockZ());
	}
	
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
