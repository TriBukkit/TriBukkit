package hu.trigary.tribukkit.region;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Regions are used to check whether a {@link Location} is inside or not.
 * <br><br>
 * The implementations of this class use integer-based coordinates wherever possible,
 * therefore the {@link Location#getBlockX()} and other similar methods are used
 * on the specified {@link Location} instance as well.
 * All of the coordinates are inclusive.
 * <br><br>
 * Whenever a world name can be specified, null is allowed and it stands for "matching any world".
 * When a non-null value is provided, but {@link Location#getWorld()} returns null,
 * then it will also be considered a match.
 * When both values are non-null, then the world names will be compared (case-sensitive).
 */
public abstract class Region {
	
	/**
	 * Gets whether the specified (block) position is inside this region.
	 *
	 * @param location the position to check
	 * @return whether the position is inside
	 */
	@Contract(pure = true)
	public abstract boolean isInside(@NotNull Location location);
	
	/**
	 * Gets whether the specified entity's (block) position is inside this region.
	 *
	 * @param entity the entity to check
	 * @return whether the entity is inside
	 */
	@Contract(pure = true)
	public final boolean isInside(@NotNull Entity entity) {
		return isInside(entity.getLocation());
	}
	
	/**
	 * Gets whether the specified block is inside this region.
	 *
	 * @param block the block to check
	 * @return whether the block is inside
	 */
	@Contract(pure = true)
	public final boolean isInside(@NotNull Block block) {
		return isInside(block.getLocation());
	}
}
