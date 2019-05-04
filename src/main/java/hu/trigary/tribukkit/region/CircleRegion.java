package hu.trigary.tribukkit.region;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CircleRegion extends WorldRegion {
	private final int centerX;
	private final int centerZ;
	private final float radiusSquared;
	
	public CircleRegion(@Nullable String world, int centerX, int centerZ, float radius) {
		super(world);
		this.centerX = centerX;
		this.centerZ = centerZ;
		radiusSquared = radius * radius;
	}
	
	public CircleRegion(@Nullable String world, @NotNull Vector center, float radius) {
		this(world, center.getBlockX(), center.getBlockZ(), radius);
	}
	
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
