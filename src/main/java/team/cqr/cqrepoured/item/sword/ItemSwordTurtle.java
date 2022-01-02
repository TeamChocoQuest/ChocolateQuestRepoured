package team.cqr.cqrepoured.item.sword;

import com.google.common.collect.Multimap;

import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;

public class ItemSwordTurtle extends SwordItem {

	private AttributeModifier knockBack;

	public ItemSwordTurtle(ToolMaterial material) {
		super(material);

		this.knockBack = new AttributeModifier("KnockbackModifier", 1.0D, 0);
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot, ItemStack stack) {
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(equipmentSlot, stack);

		if (equipmentSlot == EquipmentSlotType.MAINHAND) {
			multimap.put(Attributes.KNOCKBACK_RESISTANCE.getName(), this.knockBack);
		}

		return multimap;
	}

}
