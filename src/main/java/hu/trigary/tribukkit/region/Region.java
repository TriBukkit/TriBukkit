package hu.trigary.tribukkit.region;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public abstract class Region {
	
	@Contract(pure = true)
	public abstract boolean isInside(@NotNull Location location);
	
	@Contract(pure = true)
	public final boolean isInside(@NotNull Entity entity) {
		return isInside(entity.getLocation());
	}
}
