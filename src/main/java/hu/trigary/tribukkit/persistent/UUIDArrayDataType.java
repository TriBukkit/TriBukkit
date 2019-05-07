package hu.trigary.tribukkit.persistent;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A {@link PersistentDataType} that stores a {@link UUID}[] as N*16 bytes.
 */
public class UUIDArrayDataType implements PersistentDataType<byte[], UUID[]> {
	public static final UUIDArrayDataType INSTANCE = new UUIDArrayDataType();
	
	private UUIDArrayDataType() {}
	
	@NotNull
	@Override
	public Class<byte[]> getPrimitiveType() {
		return byte[].class;
	}
	
	@NotNull
	@Override
	public Class<UUID[]> getComplexType() {
		return UUID[].class;
	}
	
	@NotNull
	@Override
	public byte[] toPrimitive(@NotNull UUID[] complex, @NotNull PersistentDataAdapterContext context) {
		ByteBuffer buffer = ByteBuffer.wrap(new byte[complex.length * 16]);
		for (UUID id : complex) {
			buffer.putLong(id.getMostSignificantBits());
			buffer.putLong(id.getLeastSignificantBits());
		}
		return buffer.array();
	}
	
	@NotNull
	@Override
	public UUID[] fromPrimitive(@NotNull byte[] primitive, @NotNull PersistentDataAdapterContext context) {
		List<UUID> complex = new ArrayList<>();
		ByteBuffer buffer = ByteBuffer.wrap(primitive);
		while (buffer.hasRemaining()) {
			complex.add(new UUID(buffer.getLong(), buffer.getLong()));
		}
		return complex.toArray(UUID[]::new);
	}
}
