package team.cqr.cqrepoured.item.sword;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.google.common.collect.Multimap;

import meldexun.reflectionutil.ReflectionField;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.IItemTier;
import net.minecraft.world.item.Item;
import net.minecraft.item.SwordItem;
import team.cqr.cqrepoured.util.ItemUtil;

public class ItemCQRWeapon extends SwordItem {

	private static final ReflectionField<Multimap<Attribute, AttributeModifier>> DEFAULT_MODIFIERS = new ReflectionField<>(SwordItem.class, "defaultModifiers", "field_234810_b_");
	private final List<Supplier<Multimap<Attribute, AttributeModifier>>> attributeModifierSuppliers = new ArrayList<>();
	// TODO reset on config change
	private Multimap<Attribute, AttributeModifier> defaultModifiers;

	public ItemCQRWeapon(IItemTier tier, Item.Properties properties) {
		super(tier, 3, -2.4F, properties);
		this.addAttributeModifiers(() -> DEFAULT_MODIFIERS.get(this));
	}

	@SuppressWarnings("unchecked")
	public <T extends ItemCQRWeapon> T addAttributeModifiers(Supplier<Multimap<Attribute, AttributeModifier>> attributeModifierSupplier) {
		this.attributeModifierSuppliers.add(attributeModifierSupplier);
		return (T) this;
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot pEquipmentSlot) {
		if (defaultModifiers == null) {
			defaultModifiers = ItemUtil.join(attributeModifierSuppliers.stream().map(Supplier::get).collect(Collectors.toList()));
		}
		return pEquipmentSlot == EquipmentSlot.MAINHAND ? defaultModifiers : super.getDefaultAttributeModifiers(pEquipmentSlot);
	}

}
