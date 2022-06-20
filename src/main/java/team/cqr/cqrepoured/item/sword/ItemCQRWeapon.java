package team.cqr.cqrepoured.item.sword;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.google.common.collect.Multimap;

import meldexun.reflectionutil.ReflectionField;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
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
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType pEquipmentSlot) {
		if (defaultModifiers == null) {
			defaultModifiers = ItemUtil.join(attributeModifierSuppliers.stream().map(Supplier::get).collect(Collectors.toList()));
		}
		return pEquipmentSlot == EquipmentSlotType.MAINHAND ? defaultModifiers : super.getDefaultAttributeModifiers(pEquipmentSlot);
	}

}
