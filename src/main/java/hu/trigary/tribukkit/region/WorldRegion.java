package hu.trigary.tribukkit.region;

import org.bukkit.Location;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WorldRegion extends Region {
	private final String world;
	
	public WorldRegion(@Nullable String world) {
		//TODO doc: null -> matches everything
		this.world = world;
	}
	
	
	
	@Override
	@Contract(pure = true)
	public boolean isInside(@NotNull Location location) {
		return world == null || location.getWorld() == null
				|| location.getWorld().getName().equals(world);
	}
}
