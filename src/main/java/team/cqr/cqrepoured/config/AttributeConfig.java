package team.cqr.cqrepoured.config;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.registries.ForgeRegistries;

public class AttributeConfig implements Supplier<Multimap<Attribute, AttributeModifier>> {

	private final ConfigValue<List<? extends String>> attributes;

	public AttributeConfig(ForgeConfigSpec.Builder builder, String name, Multimap<Attribute, AttributeModifier> attributeModifiers) {
		this(builder, name, () -> attributeModifiers.entries().stream().map(e -> {
			return new StringBuilder()
					.append(ForgeRegistries.ATTRIBUTES.getKey(e.getKey())).append(',')
					.append(e.getValue().getName()).append(',')
					.append(e.getValue().getAmount()).append(',')
					.append(e.getValue().getOperation().toValue())
					.toString();
		}).collect(Collectors.toList()));
	}

	public AttributeConfig(ForgeConfigSpec.Builder builder, String name, Supplier<List<? extends String>> defaultSupplier) {
		builder.comment("").push(name);
		this.attributes = builder.comment("").defineList("attributes", defaultSupplier, s -> {
			if (!(s instanceof String)) {
				return false;
			}
			String[] a = ((String) s).trim().split("\\s*,\\s*");
			if (a.length != 4) {
				return false;
			}
			if (!ForgeRegistries.ATTRIBUTES.containsKey(new ResourceLocation(a[0]))) {
				return false;
			}
			try {
				Double.parseDouble(a[2]);
			} catch (NumberFormatException e) {
				return false;
			}
			try {
				Operation.fromValue(Integer.parseInt(a[3]));
			} catch (IllegalArgumentException e) {
				return false;
			}
			return true;
		});
		builder.pop();
	}

	@Override
	public Multimap<Attribute, AttributeModifier> get() {
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		List<? extends String> list = this.attributes.get();
		for (int i = 0; i < list.size(); i++) {
			String s = list.get(i);
			String[] a = s.trim().split("\\s*,\\s*");
			Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(a[0]));
			AttributeModifier modifier = new AttributeModifier(createUUID(a[1]), a[1], Double.parseDouble(a[2]), Operation.fromValue(Integer.parseInt(a[3])));
			builder.put(attribute, modifier);
		}
		return builder.build();
	}

	private static UUID createUUID(String name) {
		byte[] sha1Bytes = DigestUtils.sha1(name);
		sha1Bytes[6] &= 0x0f; // clear version
		sha1Bytes[6] |= 0x50; // set to version 5
		sha1Bytes[8] &= 0x3f; // clear variant
		sha1Bytes[8] |= 0x80; // set to IETF variant
		long msb = 0;
		long lsb = 0;
		for (int i = 0; i < 8; i++)
			msb = (msb << 8) | (sha1Bytes[i] & 0xff);
		for (int i = 8; i < 16; i++)
			lsb = (lsb << 8) | (sha1Bytes[i] & 0xff);
		return new UUID(msb, lsb);
	}

}
