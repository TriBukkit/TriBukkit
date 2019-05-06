package hu.trigary.tribukkit.yml;

import com.google.common.base.Charsets;
import hu.trigary.tribukkit.TriJavaPlugin;
import hu.trigary.tribukkit.message.Messages;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * A {@link YamlConfiguration} wrapper, which links the config to a file
 * and also adds some other utility methods.
 */
public class YmlConfig extends YamlConfiguration {
	private final File file;
	
	/**
	 * Creates a new config instance.
	 *
	 * @param plugin the plugin in which the config file is located
	 * @param fileName the name of the config file
	 * @param saveDefault whether to save the embedded default file
	 * @param loadDefaults whether to load the embedded file's values as default values
	 */
	public YmlConfig(@NotNull JavaPlugin plugin, @NotNull String fileName,
			boolean saveDefault, boolean loadDefaults) {
		file = new File(plugin.getDataFolder(), fileName);
		if (file.exists()) {
			load();
		} else if (saveDefault) {
			plugin.saveResource(fileName, false);
			load();
		}
		
		if (loadDefaults) {
			try (InputStream stream = plugin.getResource(fileName)) {
				Validate.notNull(stream, "Can only load defaults if an embedded resource has been provided");
				try (InputStreamReader reader = new InputStreamReader(stream, Charsets.UTF_8)) {
					setDefaults(loadConfiguration(reader));
				}
			} catch (IOException e) {
				throw new RuntimeException("Error while loading the defaults for config file: " + fileName, e);
			}
		}
	}
	
	/**
	 * Creates a new config instance.
	 *
	 * @param fileName the name of the config file
	 * @param saveDefault whether to save the embedded default file
	 * @param loadDefaults whether to load the embedded file's values as default values
	 */
	public YmlConfig(@NotNull String fileName, boolean saveDefault, boolean loadDefaults) {
		this(TriJavaPlugin.getInstance(), fileName, saveDefault, loadDefaults);
	}
	
	
	
	public void load() {
		try {
			load(file);
		} catch (IOException | InvalidConfigurationException e) {
			throw new RuntimeException("Error while loading config file: " + file.getPath(), e);
		}
	}
	
	public void save() {
		try {
			save(file);
		} catch (IOException e) {
			throw new RuntimeException("Error while saving config file: " + file.getPath(), e);
		}
	}
	
	
	
	public String getColoredString(@NotNull String key, @Nullable String def) {
		return Messages.color(getString(key, def));
	}
	
	public String getColoredString(@NotNull String key) {
		return getColoredString(key, null);
	}
	
	
	
	public List<String> getColoredList(@NotNull String key) {
		List<String> list = getStringList(key);
		list.replaceAll(Messages::color);
		return list;
	}
}
