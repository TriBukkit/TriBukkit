package hu.trigary.tribukkit;

import hu.trigary.tribukkit.inventory.CustomInventoryListener;
import hu.trigary.tribukkit.timing.TimingManager;
import hu.trigary.tribukkit.yml.YmlConfig;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public abstract class TriJavaPlugin extends JavaPlugin {
	private static TriJavaPlugin instance;
	private YmlConfig config;
	private NameIdCache nameIdCache;
	
	@Override
	public final void onEnable() {
		instance = this;
		onEnableImpl();
	}
	
	public abstract void onEnableImpl();
	
	@Override
	public final void onDisable() {
		onDisableImpl();
		instance = null;
	}
	
	public abstract void onDisableImpl();
	
	
	
	@NotNull
	@Contract(pure = true)
	public static TriJavaPlugin getInstance() {
		return instance;
	}
	
	@NotNull
	@Override
	public YmlConfig getConfig() {
		if (config == null) {
			config = new YmlConfig(this, "config.yml", false);
		}
		return config;
	}
	
	@NotNull
	public NameIdCache getNameIdCache() {
		if (nameIdCache == null) {
			nameIdCache = new NameIdCache(this);
			Bukkit.getPluginManager().registerEvents(nameIdCache, this);
		}
		return nameIdCache;
	}
	
	
	
	public void enableCustomInventories() {
		try {
			Bukkit.getPluginManager().registerEvents(CustomInventoryListener.class
					.getDeclaredConstructor().newInstance(), this);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void enableScheduledTimingReports(boolean verbose, int intervalSeconds) {
		TimingManager.enable(verbose);
		Bukkit.getScheduler().runTaskTimer(this, TimingManager::printAllData, 14, intervalSeconds * 20L);
	}
}
