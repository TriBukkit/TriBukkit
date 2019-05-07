package hu.trigary.tribukkit.persistent;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * A {@link PersistentDataType} that stores an UUID as 16 bytes.
 */
public class UUIDDataType implements PersistentDataType<byte[], UUID> {
	public static final UUIDDataType INSTANCE = new UUIDDataType();
	
	private UUIDDataType() {}
	
	@NotNull
	@Override
	public Class<byte[]> getPrimitiveType() {
		return byte[].class;
	}
	
	@NotNull
	@Override
	public Class<UUID> getComplexType() {
		return UUID.class;
	}
	
	@NotNull
	@Override
	public byte[] toPrimitive(@NotNull UUID complex, @NotNull PersistentDataAdapterContext context) {
		ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
		buffer.putLong(complex.getMostSignificantBits());
		buffer.putLong(complex.getLeastSignificantBits());
		return buffer.array();
	}
	
	@NotNull
	@Override
	public UUID fromPrimitive(@NotNull byte[] primitive, @NotNull PersistentDataAdapterContext context) {
		ByteBuffer buffer = ByteBuffer.wrap(primitive);
		return new UUID(buffer.getLong(), buffer.getLong());
	}
}
