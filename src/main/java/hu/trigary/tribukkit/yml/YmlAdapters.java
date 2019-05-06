package hu.trigary.tribukkit.yml;

import hu.trigary.tribukkit.data.BlockLocation;
import hu.trigary.tribukkit.data.LazyLocation;
import hu.trigary.tribukkit.json.*;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A collection of methods which let you save and load some basic data types from {@link ConfigurationSection}.
 * These methods internally use the JSON adapters in the {@link hu.trigary.tribukkit.json} package.
 */
public class YmlAdapters {
	
	/**
	 * Sets the specified path to the given value.
	 * If the value is null, the entry will be removed.
	 * Any existing entry will be replaced, regardless of what the new value is.
	 *
	 * @param section the section to modify
	 * @param key the path to modify
	 * @param location the new value
	 */
	public static void setLocation(@NotNull ConfigurationSection section,
			@NotNull String key, @Nullable Location location) {
		section.set(key, location == null ? null : JsonLocationAdapter.serialize(location));
	}
	
	/**
	 * Gets the requested value from the specified path.
	 * If the path is not set, but a default value has been specified, this will return the default value.
	 * If the path is not set and no default value was specified, this will return null.
	 *
	 * @param section the section to query
	 * @param key the path of the value
	 * @return the requested (possibly default) value
	 */
	public static Location getLocation(@NotNull ConfigurationSection section, @NotNull String key) {
		String value = section.getString(key);
		return value == null ? null : JsonLocationAdapter.deserialize(value);
	}
	
	
	
	/**
	 * Sets the specified path to the given value.
	 * If the value is null, the entry will be removed.
	 * Any existing entry will be replaced, regardless of what the new value is.
	 *
	 * @param section the section to modify
	 * @param key the path to modify
	 * @param location the new value
	 */
	public static void setLazyLocation(@NotNull ConfigurationSection section,
			@NotNull String key, @Nullable LazyLocation location) {
		section.set(key, location == null ? null : JsonLazyLocationAdapter.serialize(location));
	}
	
	/**
	 * Gets the requested value from the specified path.
	 * If the path is not set, but a default value has been specified, this will return the default value.
	 * If the path is not set and no default value was specified, this will return null.
	 *
	 * @param section the section to query
	 * @param key the path of the value
	 * @return the requested (possibly default) value
	 */
	public static LazyLocation getLazyLocation(@NotNull ConfigurationSection section, @NotNull String key) {
		String value = section.getString(key);
		return value == null ? null : JsonLazyLocationAdapter.deserialize(value);
	}
	
	
	
	/**
	 * Sets the specified path to the given value.
	 * If the value is null, the entry will be removed.
	 * Any existing entry will be replaced, regardless of what the new value is.
	 *
	 * @param section the section to modify
	 * @param key the path to modify
	 * @param vector the new value
	 */
	public static void setVector(@NotNull ConfigurationSection section,
			@NotNull String key, @Nullable Vector vector) {
		section.set(key, vector == null ? null : JsonVectorAdapter.serialize(vector));
	}
	
	/**
	 * Gets the requested value from the specified path.
	 * If the path is not set, but a default value has been specified, this will return the default value.
	 * If the path is not set and no default value was specified, this will return null.
	 *
	 * @param section the section to query
	 * @param key the path of the value
	 * @return the requested (possibly default) value
	 */
	public static Vector getVector(@NotNull ConfigurationSection section, @NotNull String key) {
		String value = section.getString(key);
		return value == null ? null : JsonVectorAdapter.deserialize(value);
	}
	
	
	
	/**
	 * Sets the specified path to the given value.
	 * If the value is null, the entry will be removed.
	 * Any existing entry will be replaced, regardless of what the new value is.
	 *
	 * @param section the section to modify
	 * @param key the path to modify
	 * @param location the new value
	 */
	public static void setBlockLocation(@NotNull ConfigurationSection section,
			@NotNull String key, @Nullable BlockLocation location) {
		section.set(key, location == null ? null : JsonBlockLocationAdapter.serialize(location));
	}
	
	/**
	 * Gets the requested value from the specified path.
	 * If the path is not set, but a default value has been specified, this will return the default value.
	 * If the path is not set and no default value was specified, this will return null.
	 *
	 * @param section the section to query
	 * @param key the path of the value
	 * @return the requested (possibly default) value
	 */
	public static BlockLocation getBlockLocation(@NotNull ConfigurationSection section, @NotNull String key) {
		String value = section.getString(key);
		return value == null ? null : JsonBlockLocationAdapter.deserialize(value);
	}
	
	
	
	/**
	 * Sets the specified path to the given value.
	 * If the value is null, the entry will be removed.
	 * Any existing entry will be replaced, regardless of what the new value is.
	 *
	 * @param section the section to modify
	 * @param key the path to modify
	 * @param vector the new value
	 */
	public static void setBlockVector(@NotNull ConfigurationSection section,
			@NotNull String key, @Nullable BlockVector vector) {
		section.set(key, vector == null ? null : JsonBlockVectorAdapter.serialize(vector));
	}
	
	/**
	 * Gets the requested value from the specified path.
	 * If the path is not set, but a default value has been specified, this will return the default value.
	 * If the path is not set and no default value was specified, this will return null.
	 *
	 * @param section the section to query
	 * @param key the path of the value
	 * @return the requested (possibly default) value
	 */
	public static BlockVector getBlockVector(@NotNull ConfigurationSection section, @NotNull String key) {
		String value = section.getString(key);
		return value == null ? null : JsonBlockVectorAdapter.deserialize(value);
	}
	
	
	
	/**
	 * Sets the specified path to the given value.
	 * If the value is null, the entry will be removed.
	 * Any existing entry will be replaced, regardless of what the new value is.
	 *
	 * @param section the section to modify
	 * @param key the path to modify
	 * @param item the new value
	 */
	public static void setItemString(@NotNull ConfigurationSection section,
			@NotNull String key, @Nullable ItemStack item) {
		section.set(key, item == null ? null : JsonItemStringAdapter.serialize(item));
	}
	
	/**
	 * Gets the requested value from the specified path.
	 * If the path is not set, but a default value has been specified, this will return the default value.
	 * If the path is not set and no default value was specified, this will return null.
	 *
	 * @param section the section to query
	 * @param key the path of the value
	 * @return the requested (possibly default) value
	 */
	public static ItemStack getItemString(@NotNull ConfigurationSection section, @NotNull String key) {
		String value = section.getString(key);
		return value == null ? null : JsonItemStringAdapter.deserialize(value);
	}
	
	
	
	/**
	 * Sets the specified path to the given value.
	 * If the value is null, the entry will be removed.
	 * Any existing entry will be replaced, regardless of what the new value is.
	 *
	 * @param section the section to modify
	 * @param key the path to modify
	 * @param item the new value
	 */
	public static void setItemSimple(@NotNull ConfigurationSection section,
			@NotNull String key, @Nullable ItemStack item) {
		section.set(key, item == null ? null : JsonItemSimpleAdapter.serialize(item));
	}
	
	/**
	 * Gets the requested value from the specified path.
	 * If the path is not set, but a default value has been specified, this will return the default value.
	 * If the path is not set and no default value was specified, this will return null.
	 *
	 * @param section the section to query
	 * @param key the path of the value
	 * @return the requested (possibly default) value
	 */
	public static ItemStack getItemSimple(@NotNull ConfigurationSection section, @NotNull String key) {
		ConfigurationSection value = section.getConfigurationSection(key);
		return value == null ? null : JsonItemSimpleAdapter.deserialize(value.getValues(true));
	}
}
