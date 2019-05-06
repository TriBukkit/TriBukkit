package hu.trigary.tribukkit;

import hu.trigary.tribukkit.inventory.CustomInventoryListener;
import hu.trigary.tribukkit.timing.TimingManager;
import hu.trigary.tribukkit.yml.YmlConfig;
import hu.trigary.tribukkit.inventory.CustomInventory;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

/**
 * The base class of the plugin.
 * The main class' of plugins should extend this instead of {@link JavaPlugin}.
 */
public abstract class TriJavaPlugin extends JavaPlugin {
	private static TriJavaPlugin instance;
	private YmlConfig config;
	private NameIdCache nameIdCache;
	
	@Override
	public final void onEnable() {
		instance = this;
		onEnableImpl();
	}
	
	/**
	 * A method which is called when the plugin gets enabled.
	 */
	public abstract void onEnableImpl();
	
	@Override
	public final void onDisable() {
		onDisableImpl();
		instance = null;
	}
	
	/**
	 * A method which is called when the plugin gets disabled.
	 */
	public abstract void onDisableImpl();
	
	
	
	/**
	 * Gets the current plugin instance.
	 * Plugins should override this method so that the return type is correct.
	 * For this method to reliably work, the TriBukkit library must be relocated.
	 * Other methods which optionally do not take a {@link JavaPlugin}
	 * instance depend on this method as well.
	 *
	 * @return the current plugin instance
	 */
	@NotNull
	@Contract(pure = true)
	public static TriJavaPlugin getInstance() {
		return instance;
	}
	
	/**
	 * Load the {@code "config.yml"} configuration file.
	 * If an embedded resource with the same name has been provided,
	 * then it will be used to load the defaults.
	 *
	 * @return the loaded config instance
	 */
	@NotNull
	@Override
	public YmlConfig getConfig() {
		if (config == null) {
			config = new YmlConfig(this, "config.yml", false, true);
		}
		return config;
	}
	
	/**
	 * Gets the current player name-UUID cache instance,
	 * internally creating one if it doesn't already exist.
	 *
	 * @return the cache instance
	 */
	@NotNull
	public NameIdCache getNameIdCache() {
		if (nameIdCache == null) {
			nameIdCache = new NameIdCache(this);
			Bukkit.getPluginManager().registerEvents(nameIdCache, this);
		}
		return nameIdCache;
	}
	
	
	
	/**
	 * Enables the use of {@link CustomInventory}.
	 */
	public void enableCustomInventories() {
		try {
			Bukkit.getPluginManager().registerEvents(CustomInventoryListener.class
					.getDeclaredConstructor().newInstance(), this);
		} catch (ReflectiveOperationException e) {
			throw new AssertionError("Can't reflective instantiate CustomInventoryListener", e);
		}
	}
	
	/**
	 * Starts periodic timing reports.
	 * This is the same as manually enabling {@link TimingManager}
	 * and periodically calling {@link TimingManager#printAllData()}
	 *
	 * @param verbose whether to use the verbose {@link TimingManager} implementation
	 * @param intervalSeconds how often the reports should be printed, in seconds
	 */
	public void enableScheduledTimingReports(boolean verbose, int intervalSeconds) {
		TimingManager.enable(verbose);
		Bukkit.getScheduler().runTaskTimer(this, TimingManager::printAllData, 14, intervalSeconds * 20L);
	}
	
	
	
	/**
	 * Logs a message using the plugin's logger.
	 * Shortcut utility method, just routes all parameters to the real logger.
	 *
	 * @param level the log level to use
	 * @param message the message to log
	 */
	public static void log(Level level, String message) {
		getInstance().getLogger().log(level, message);
	}
	
	/**
	 * Logs a message using the plugin's logger.
	 * Shortcut utility method, just routes all parameters to the real logger.
	 *
	 * @param level the log level to use
	 * @param message the message to log
	 * @param params the parameters of the message
	 */
	public static void log(Level level, String message, Object... params) {
		getInstance().getLogger().log(level, message, params);
	}
	
	/**
	 * Logs a message using the plugin's logger.
	 * Shortcut utility method, just routes all parameters to the real logger.
	 *
	 * @param level the log level to use
	 * @param message the message to log
	 * @param thrown the {@link Throwable} associated with this log message
	 */
	public static void log(Level level, String message, Throwable thrown) {
		getInstance().getLogger().log(level, message, thrown);
	}
}
