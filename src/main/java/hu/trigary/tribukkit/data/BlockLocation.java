package hu.trigary.tribukkit.data;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class BlockLocation {
	private final String world;
	private final int x;
	private final int y;
	private final int z;
	
	public BlockLocation(@Nullable String world, int x, int y, int z) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public BlockLocation(@NotNull Location location) {
		this(location.getWorld() == null ? null : location.getWorld().getName(),
				location.getBlockX(), location.getBlockY(), location.getBlockZ());
	}
	
	
	
	@Nullable
	@Contract(pure = true)
	public String getWorldName() {
		return world;
	}
	
	@Nullable
	@Contract(pure = true)
	public World getWorld() {
		return world == null ? null : Bukkit.getWorld(world);
	}
	
	@Contract(pure = true)
	public boolean isWorldReady() {
		return world == null || Bukkit.getWorld(world) != null;
	}
	
	
	
	@Contract(pure = true)
	public int getX() {
		return x;
	}
	
	@Contract(pure = true)
	public int getY() {
		return y;
	}
	
	@Contract(pure = true)
	public int getZ() {
		return z;
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	public Location toLocation() {
		return new Location(getWorld(), x, y, z);
	}
	
	@Override
	public String toString() {
		return world + "@" + x + ";" + y + ";" + z;
	}
	
	
	
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof BlockLocation)) {
			return false;
		}
		
		BlockLocation other = (BlockLocation) object;
		return x == other.x && y == other.y && z == other.z && Objects.equals(world, other.world);
	}
	
	@Override
	public int hashCode() {
		return (world == null ? 51 : world.hashCode()) ^ x ^ y ^ z;
	}
}
