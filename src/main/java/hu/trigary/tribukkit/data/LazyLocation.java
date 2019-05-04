package hu.trigary.tribukkit.data;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class LazyLocation {
	private final String world;
	private final double x;
	private final double y;
	private final double z;
	private final float yaw;
	private final float pitch;
	
	public LazyLocation(@Nullable String world, double x, double y, double z, float yaw, float pitch) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}
	
	public LazyLocation(@Nullable String world, double x, double y, double z) {
		this(world, x, y, z, 0, 0);
	}
	
	public LazyLocation(@NotNull Location location) {
		this(location.getWorld() == null ? null : location.getWorld().getName(),
				location.getX(), location.getY(), location.getZ(),
				location.getYaw(), location.getPitch());
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
	public double getX() {
		return x;
	}
	
	@Contract(pure = true)
	public double getY() {
		return y;
	}
	
	@Contract(pure = true)
	public double getZ() {
		return z;
	}
	
	@Contract(pure = true)
	public float getYaw() {
		return yaw;
	}
	
	@Contract(pure = true)
	public float getPitch() {
		return pitch;
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	public Location toLocation() {
		return new Location(getWorld(), x, y, z);
	}
	
	@Override
	public String toString() {
		return world + "@" + x + ";" + y + ";" + z + "#" + yaw + ";" + pitch;
	}
	
	
	
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof LazyLocation)) {
			return false;
		}
		
		LazyLocation other = (LazyLocation) object;
		return Double.compare(x, other.x) == 0
				&& Double.compare(y, other.y) == 0
				&& Double.compare(z, other.z) == 0
				&& Float.compare(yaw, other.yaw) == 0
				&& Float.compare(pitch, other.pitch) == 0
				&& Objects.equals(world, other.world);
	}
	
	@Override
	public int hashCode() {
		long lx = Double.doubleToLongBits(x);
		long ly = Double.doubleToLongBits(y);
		long lz = Double.doubleToLongBits(z);
		return (world == null ? 51 : world.hashCode())
				^ (int) lx ^ (int) (lx >> 32)
				^ (int) ly ^ (int) (ly >> 32)
				^ (int) lz ^ (int) (lz >> 32)
				^ Float.floatToIntBits(yaw) ^ Float.floatToIntBits(pitch);
	}
}
