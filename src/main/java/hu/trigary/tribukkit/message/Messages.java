package hu.trigary.tribukkit.message;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class Messages {
	private final Function<String, String> messageSource;
	
	public Messages(@NotNull Function<String, String> messageSource) {
		this.messageSource = messageSource;
	}
	
	public Messages(boolean translateColors, @NotNull ConfigurationSection section) {
		this(translateColors ? section::getString : key -> color(section.getString(key)));
	}
	
	
	
	@Contract(pure = true)
	public String get(String input) {
		return messageSource.apply(input);
	}
	
	@Contract(pure = true)
	public String get(String input, @NotNull String... placeholders) {
		for (int i = 0; i < placeholders.length; ) {
			input = StringUtils.replaceOnce(get(input), placeholders[i++], placeholders[i++]);
		}
		return input;
	}
	
	@Contract(pure = true)
	public String get(String input, @NotNull String placeholder, @NotNull Object value) {
		return StringUtils.replaceOnce(get(input), placeholder, value.toString());
	}
	
	
	
	@Contract(pure = true, value = "null -> null; !null -> !null")
	public static String color(@Nullable String input) {
		return input == null ? null : ChatColor.translateAlternateColorCodes('&', input);
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	public static String formatShortTime(long seconds) {
		if (seconds == 0) {
			return "0s";
		}
		
		Validate.isTrue(seconds > 0, "Time must be positive");
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
	
	@NotNull
	@Contract(pure = true)
	public static String formatLongTime(long seconds) {
		if (seconds == 0) {
			return "0 seconds";
		}
		
		Validate.isTrue(seconds > 0, "Time must be positive");
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
	
	
	
	public static String formatDecimal(float number) {
		return formatDecimal((double) number);
	}
	
	public static String formatDecimal(double number) {
		return String.format("%.2f", number);
	}
}
