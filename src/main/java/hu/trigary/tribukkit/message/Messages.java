package hu.trigary.tribukkit.message;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * A class designed to make getting messages (especially ones with placeholders) easier.
 * The source of these messages are configurable, but in most cases they are a {@link ConfigurationSection}.
 * An alternative implementation of this class is {@link CachedMessages},
 * which caches the output of the message source,
 * this way eg. colors are only translated once.
 */
public class Messages {
	private final Function<String, String> messageSource;
	
	/**
	 * Creates a new instance with the specified message source.
	 * The message source can take null and return null.
	 *
	 * @param messageSource the function which produces the messages based on the input
	 */
	public Messages(@NotNull Function<String, String> messageSource) {
		this.messageSource = messageSource;
	}
	
	/**
	 * Creates a new instance which has a {@link ConfigurationSection} as its message source.
	 *
	 * @param translateColors whether the messages should be colorized
	 * @param section the section containing the messages
	 */
	public Messages(boolean translateColors, @NotNull ConfigurationSection section) {
		this(translateColors ? section::getString : key -> color(section.getString(key)));
	}
	
	
	
	/**
	 * Gets a {@link String} using the specified input from the message source.
	 *
	 * @param input the input based on which the message source produces the output
	 * @return the {@link String} produced by the message source
	 */
	@Contract(pure = true)
	public String get(String input) {
		return messageSource.apply(input);
	}
	
	/**
	 * Calls {@link #get(String)} internally and then replaces the
	 * placeholders according to {@link #format(String, String...)}.
	 *
	 * @param input the input based on which the message source produces its output
	 * @param placeholders the placeholder-replacements pairs
	 * @return the message with the placeholders replaced
	 */
	@Contract(pure = true)
	public String get(String input, @NotNull String... placeholders) {
		return format(get(input), placeholders);
	}
	
	/**
	 * Calls {@link #get(String)} internally and then replaces the
	 * placeholder according to {@link #format(String, String, Object)}.
	 *
	 * @param input the input based on which the message source produces its output
	 * @param placeholder the text that should be replaced
	 * @param value the object that should replace the text
	 * @return the message with the specified placeholder replaced
	 */
	@Contract(pure = true)
	public String get(String input, @NotNull String placeholder, @NotNull Object value) {
		return format(get(input), placeholder, value);
	}
	
	
	
	/**
	 * Translates the color codes of the input.
	 * Same as calling {@code ChatColor.translateAlternateColorCodes('&', input)},
	 * except that null values are allowed (and produce null results).
	 *
	 * @param input the text to colorize
	 * @return the colorized text or null, if the input was null
	 */
	@Contract(pure = true, value = "null -> null; !null -> !null")
	public static String color(@Nullable String input) {
		return input == null ? null : ChatColor.translateAlternateColorCodes('&', input);
	}
	
	
	
	/**
	 * Replaces the specified placeholders with the specified values in the specified input.
	 * The {@code placeholders} must contain the placeholder text
	 * and then its replacement directly after each other.
	 *
	 * @param input the text that should have its placeholders replaced
	 * @param placeholders the placeholder-replacements pairs
	 * @return the input with the placeholders replaced
	 */
	@Contract(pure = true, value = "null, _ -> null; !null, _ -> !null")
	public static String format(String input, @NotNull String... placeholders) {
		Validate.isTrue(placeholders.length % 2 == 0, "Count of placeholders and replacements must match");
		for (int i = 0; i < placeholders.length; ) {
			input = StringUtils.replaceOnce(input, placeholders[i++], placeholders[i++]);
		}
		return input;
	}
	
	/**
	 * Replaces the specified placeholder with the specified value in the specified input.
	 *
	 * @param input the text that should have its placeholder replaced
	 * @param placeholder the text that should be replaced
	 * @param value the object that should replace the text
	 * @return the input with the specified placeholder replaced
	 */
	@Contract(pure = true, value = "null, _, _ -> null; !null, _, _ -> !null")
	public static String format(String input, @NotNull String placeholder, @NotNull Object value) {
		return StringUtils.replaceOnce(input, placeholder, value.toString());
	}
	
	
	
	/**
	 * Formats the specified duration to "Xd, Xh, etc."
	 * An input of 0 outputs "0s".
	 * Negative values are not allowed.
	 *
	 * @param seconds the duration to format
	 * @return the formatted duration
	 */
	@NotNull
	@Contract(pure = true)
	public static String formatShortTime(long seconds) {
		if (seconds == 0) {
			return "0s";
		}
		
		Validate.isTrue(seconds > 0, "Time must be non-negative");
		long minutes = seconds / 60;
		seconds %= 60;
		long hours = minutes / 60;
		minutes %= 60;
		long days = hours / 24;
		hours %= 24;
		
		StringBuilder builder = new StringBuilder();
		if (days != 0) {
			builder.append(days).append("d ");
		}
		if (hours != 0) {
			builder.append(hours).append("h ");
		}
		if (minutes != 0) {
			builder.append(minutes).append("m ");
		}
		if (seconds != 0) {
			builder.append(seconds).append("s");
		}
		return builder.toString().trim();
	}
	
	/**
	 * Formats the specified duration to "X days, X hours, etc."
	 * An input of 0 outputs "0 seconds".
	 * Negative values are not allowed.
	 *
	 * @param seconds the duration to format
	 * @return the formatted duration
	 */
	@NotNull
	@Contract(pure = true)
	public static String formatLongTime(long seconds) {
		if (seconds == 0) {
			return "0 seconds";
		}
		
		Validate.isTrue(seconds > 0, "Time must be non-negative");
		long minutes = seconds / 60;
		seconds %= 60;
		long hours = minutes / 60;
		minutes %= 60;
		long days = hours / 24;
		hours %= 24;
		
		StringBuilder builder = new StringBuilder();
		if (days != 0) {
			builder.append(days).append(days == 1 ? " day " : " days ");
		}
		if (hours != 0) {
			builder.append(hours).append(hours == 1 ? " hour " : " hours ");
		}
		if (minutes != 0) {
			builder.append(minutes).append(minutes == 1 ? " minute " : " minutes ");
		}
		if (seconds != 0) {
			builder.append(seconds).append(seconds == 1 ? " second" : " seconds");
		}
		return builder.toString().trim();
	}
	
	
	
	/**
	 * Formats the specified decimal number, keeping 2 decimal places.
	 *
	 * @param number the number to format
	 * @return the formatted number
	 */
	public static String formatDecimal(float number) {
		return formatDecimal((double) number);
	}
	
	/**
	 * Formats the specified decimal number, keeping 2 decimal places.
	 *
	 * @param number the number to format
	 * @return the formatted number
	 */
	public static String formatDecimal(double number) {
		return String.format("%.2f", number);
	}
}
