package hu.trigary.tribukkit.message;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * An alternative {@link Messages} implementation that caches the output of the message source.
 * This way eg. colors are only translated once.
 */
public class CachedMessages extends Messages {
	private final Map<String, String> cache = new HashMap<>();
	
	/**
	 * Creates a new instance with the specified message source.
	 * The message source can take null and return null.
	 *
	 * @param messageSource the function which produces the messages based on the input
	 */
	public CachedMessages(@NotNull Function<String, String> messageSource) {
		super(messageSource);
	}
	
	/**
	 * Creates a new instance which has a {@link ConfigurationSection} as its message source.
	 *
	 * @param translateColors whether the messages should be colorized
	 * @param section the section containing the messages
	 */
	public CachedMessages(boolean translateColors, @NotNull ConfigurationSection section) {
		super(translateColors, section);
	}
	
	
	
	@Override
	@Contract(pure = true)
	public String get(String input) {
		return cache.computeIfAbsent(input, super::get);
	}
}
