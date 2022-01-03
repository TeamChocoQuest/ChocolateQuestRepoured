package team.cqr.cqrepoured.item.sword;

import com.google.common.collect.Multimap;

import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import team.cqr.cqrepoured.util.ItemUtil;

public class ItemCQRWeapon extends SwordItem {

	// private static final UUID CQR_ATTACK_DAMAGE_MODIFIER = UUID.fromString("2636acdb-9693-428f-8eb3-328f9bb05ed2");
	// private static final UUID CQR_ATTACK_SPEED_MODIFIER = UUID.fromString("e97e2edc-f024-4b1d-a832-17ae497ea18b");
	private final double attackDamageBonus;
	private final double attackSpeedBonus;

	public ItemCQRWeapon(IItemTier material, int attackDamageBonus, float attackSpeedBonus, Item.Properties itemProps) {
		super(material, attackDamageBonus, attackSpeedBonus, itemProps);
		this.attackDamageBonus = attackDamageBonus;
		this.attackSpeedBonus = attackSpeedBonus;
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType slot) {
		Multimap<Attribute, AttributeModifier> modifiers = super.getDefaultAttributeModifiers(slot);
		if (slot == EquipmentSlotType.MAINHAND) {
			if (this.attackDamageBonus != 0.0D) {
				ItemUtil.replaceModifier(modifiers, Attributes.ATTACK_DAMAGE, BASE_ATTACK_DAMAGE_UUID, oldValue -> (oldValue + this.attackDamageBonus));
			}
			if (this.attackSpeedBonus != 0.0D) {
				ItemUtil.replaceModifier(modifiers, Attributes.ATTACK_SPEED, BASE_ATTACK_SPEED_UUID, oldValue -> (oldValue + this.attackSpeedBonus));
			}
		}
		return modifiers;
	}

}
