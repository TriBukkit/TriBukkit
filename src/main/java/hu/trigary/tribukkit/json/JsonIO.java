package hu.trigary.tribukkit.json;

import com.google.common.base.Charsets;
import com.google.gson.*;
import hu.trigary.tribukkit.TriJavaPlugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.lang.reflect.Type;

public abstract class JsonIO {
	
	public static <T> T load(@NotNull Gson gson, @NotNull File file, @NotNull Type type, @Nullable T fallbackValue) {
		if (file.length() == 0) {
			return fallbackValue;
		}
		
		try (FileReader reader = new FileReader(file, Charsets.UTF_8)) {
			return gson.fromJson(reader, type);
		} catch (IOException | JsonParseException e) {
			throw new RuntimeException("Error while reading JSON file: " + file, e);
		}
	}
	
	public static <T> T load(@NotNull Gson gson, @NotNull JavaPlugin plugin,
			@NotNull String fileName, @NotNull Type type, @Nullable T fallbackValue) {
		return load(gson, new File(plugin.getDataFolder(), fileName), type, fallbackValue);
	}
	
	public static <T> T load(@NotNull Gson gson, @NotNull String fileName,
			@NotNull Type type, @Nullable T fallbackValue) {
		return load(gson, new File(TriJavaPlugin.getInstance().getDataFolder(), fileName), type, fallbackValue);
	}
	
	
	
	public static void save(@NotNull Gson gson, @NotNull File file, @NotNull Type type, @NotNull Object value) {
		//noinspection ResultOfMethodCallIgnored
		file.getParentFile().mkdirs();
		
		try (FileWriter writer = new FileWriter(file, Charsets.UTF_8)) {
			gson.toJson(value, type, writer);
		} catch (IOException | JsonParseException e) {
			throw new RuntimeException("Error while writing JSON file: " + file, e);
		}
	}
	
	public static void save(@NotNull Gson gson, @NotNull JavaPlugin plugin,
			@NotNull String fileName, @NotNull Type type, @NotNull Object value) {
		save(gson, new File(plugin.getDataFolder(), fileName), type, value);
	}
	
	public static void save(@NotNull Gson gson, @NotNull String fileName,
			@NotNull Type type, @NotNull Object value) {
		save(gson, new File(TriJavaPlugin.getInstance().getDataFolder(), fileName), type, value);
	}
	
	
	
	public static void save(@NotNull Gson gson, @NotNull File file, @NotNull Object value) {
		save(gson, file, value.getClass(), value);
	}
	
	public static void save(@NotNull Gson gson, @NotNull JavaPlugin plugin,
			@NotNull String fileName, @NotNull Object value) {
		save(gson, new File(plugin.getDataFolder(), fileName), value.getClass(), value);
	}
	
	public static void save(@NotNull Gson gson, @NotNull String fileName, @NotNull Object value) {
		save(gson, new File(TriJavaPlugin.getInstance().getDataFolder(), fileName), value.getClass(), value);
	}
}
