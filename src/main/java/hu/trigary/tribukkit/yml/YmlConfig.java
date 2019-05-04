package hu.trigary.tribukkit.yml;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YmlConfig extends YamlConfiguration {
	private final Map<String, Object> cache = new HashMap<>();
	private final File file;
	
	public YmlConfig(@NotNull JavaPlugin plugin, @NotNull String fileName, boolean saveDefault) {
		file = new File(plugin.getDataFolder(), fileName);
		if (file.exists()) {
			load();
		} else if (saveDefault) {
			plugin.saveResource(fileName, false);
			load();
		}
	}
	
	
	
	public void load() {
		try {
			load(file);
		} catch (IOException | InvalidConfigurationException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void save() {
		try {
			save(file);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
	public String getColoredString(@NotNull String key, @Nullable String def) {
		return (String) cache.computeIfAbsent(key, k -> {
			String raw = getString(k, def);
			return raw == null ? null : ChatColor.translateAlternateColorCodes('&', raw);
		});
	}
	
	public String getColoredString(@NotNull String key) {
		return getColoredString(key, null);
	}
	
	
	
	public List<String> getColoredList(@NotNull String key) {
		//noinspection unchecked
		return (List<String>) cache.computeIfAbsent(key, k -> {
			List<String> raw = getStringList(k);
			raw.replaceAll(line -> ChatColor.translateAlternateColorCodes('&', line));
			return raw;
		});
	}
}
