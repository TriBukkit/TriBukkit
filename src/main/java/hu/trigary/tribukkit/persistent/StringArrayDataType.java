package hu.trigary.tribukkit.persistent;

import com.google.common.base.Charsets;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class StringArrayDataType implements PersistentDataType<byte[], String[]> {
	public static final StringArrayDataType INSTANCE = new StringArrayDataType();
	
	private StringArrayDataType() {}
	
	@NotNull
	@Override
	public Class<byte[]> getPrimitiveType() {
		return byte[].class;
	}
	
	@NotNull
	@Override
	public Class<String[]> getComplexType() {
		return String[].class;
	}
	
	@NotNull
	@Override
	public byte[] toPrimitive(@NotNull String[] complex, @NotNull PersistentDataAdapterContext context) {
		int size = complex.length * 4;
		byte[][] arrays = new byte[complex.length][];
		for (int i = 0; i < complex.length; i++) {
			byte[] array = complex[i].getBytes(Charsets.UTF_8);
			arrays[i] = array;
			size += array.length;
		}
		
		ByteBuffer buffer = ByteBuffer.wrap(new byte[size]);
		for (byte[] array : arrays) {
			buffer.putInt(array.length);
			buffer.put(array);
		}
		return buffer.array();
	}
	
	@NotNull
	@Override
	public String[] fromPrimitive(@NotNull byte[] primitive, @NotNull PersistentDataAdapterContext context) {
		List<String> complex = new ArrayList<>();
		ByteBuffer buffer = ByteBuffer.wrap(primitive);
		byte[] temp = null;
		
		while (buffer.hasRemaining()) {
			int length = buffer.getInt();
			if (temp == null || temp.length < length) {
				temp = new byte[length];
			}
			
			buffer.get(temp, 0, length);
			complex.add(new String(temp, 0, length, Charsets.UTF_8));
		}
		return complex.toArray(String[]::new);
	}
}
