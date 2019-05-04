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

public class YmlAdapters {
	
	public static void setLocation(@NotNull ConfigurationSection section,
			@NotNull String key, @Nullable Location location) {
		section.set(key, location == null ? null : JsonLocationAdapter.serialize(location));
	}
	
	public static Location getLocation(@NotNull ConfigurationSection section, @NotNull String key) {
		String value = section.getString(key);
		return value == null ? null : JsonLocationAdapter.deserialize(value);
	}
	
	
	
	public static void setLazyLocation(@NotNull ConfigurationSection section,
			@NotNull String key, @Nullable LazyLocation location) {
		section.set(key, location == null ? null : JsonLazyLocationAdapter.serialize(location));
	}
	
	public static LazyLocation getLazyLocation(@NotNull ConfigurationSection section, @NotNull String key) {
		String value = section.getString(key);
		return value == null ? null : JsonLazyLocationAdapter.deserialize(value);
	}
	
	
	
	public static void setVector(@NotNull ConfigurationSection section,
			@NotNull String key, @Nullable Vector vector) {
		section.set(key, vector == null ? null : JsonVectorAdapter.serialize(vector));
	}
	
	public static Vector getVector(@NotNull ConfigurationSection section, @NotNull String key) {
		String value = section.getString(key);
		return value == null ? null : JsonVectorAdapter.deserialize(value);
	}
	
	
	
	public static void setBlockLocation(@NotNull ConfigurationSection section,
			@NotNull String key, @Nullable BlockLocation location) {
		section.set(key, location == null ? null : JsonBlockLocationAdapter.serialize(location));
	}
	
	public static BlockLocation getBlockLocation(@NotNull ConfigurationSection section, @NotNull String key) {
		String value = section.getString(key);
		return value == null ? null : JsonBlockLocationAdapter.deserialize(value);
	}
	
	
	
	public static void setBlockVector(@NotNull ConfigurationSection section,
			@NotNull String key, @Nullable BlockVector vector) {
		section.set(key, vector == null ? null : JsonBlockVectorAdapter.serialize(vector));
	}
	
	public static BlockVector getBlockVector(@NotNull ConfigurationSection section, @NotNull String key) {
		String value = section.getString(key);
		return value == null ? null : JsonBlockVectorAdapter.deserialize(value);
	}
	
	
	
	public static void setItemString(@NotNull ConfigurationSection section,
			@NotNull String key, @Nullable ItemStack item) {
		section.set(key, item == null ? null : JsonItemStringAdapter.serialize(item));
	}
	
	public static ItemStack getItemString(@NotNull ConfigurationSection section, @NotNull String key) {
		String value = section.getString(key);
		return value == null ? null : JsonItemStringAdapter.deserialize(value);
	}
	
	
	
	public static void setItemSimple(@NotNull ConfigurationSection section,
			@NotNull String key, @Nullable ItemStack item) {
		section.set(key, item == null ? null : JsonItemSimpleAdapter.serialize(item));
	}
	
	public static ItemStack getItemSimple(@NotNull ConfigurationSection section, @NotNull String key) {
		ConfigurationSection value = section.getConfigurationSection(key);
		return value == null ? null : JsonItemSimpleAdapter.deserialize(value.getValues(true));
	}
}
