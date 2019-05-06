package hu.trigary.tribukkit.message;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class CachedMessages extends Messages {
	private final Map<String, String> cache = new HashMap<>();
	
	public CachedMessages(@NotNull Function<String, String> messageSource) {
		super(messageSource);
	}
	
	public CachedMessages(boolean translateColors, @NotNull ConfigurationSection section) {
		super(translateColors, section);
	}
	
	
	
	@Override
	@Contract(pure = true)
	public String get(String input) {
		return cache.computeIfAbsent(input, super::get);
	}
}
