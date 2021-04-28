package team.cqr.cqrepoured.objects.items.swords;

import com.google.common.collect.Multimap;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import team.cqr.cqrepoured.util.ItemUtil;

public class ItemCQRWeapon extends ItemSword {

	// private static final UUID CQR_ATTACK_DAMAGE_MODIFIER = UUID.fromString("2636acdb-9693-428f-8eb3-328f9bb05ed2");
	// private static final UUID CQR_ATTACK_SPEED_MODIFIER = UUID.fromString("e97e2edc-f024-4b1d-a832-17ae497ea18b");
	private final double attackDamageMultiplier;
	private final double attackSpeedMultiplier;

	public ItemCQRWeapon(ToolMaterial material, double attackDamageMultiplier) {
		this(material, attackDamageMultiplier, 1.0D / attackDamageMultiplier);
	}

	public ItemCQRWeapon(ToolMaterial material, double attackDamageMultiplier, double attackSpeedMultiplier) {
		super(material);
		this.attackDamageMultiplier = attackDamageMultiplier;
		this.attackSpeedMultiplier = attackSpeedMultiplier;
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
		Multimap<String, AttributeModifier> modifiers = super.getAttributeModifiers(slot, stack);
		if (slot == EntityEquipmentSlot.MAINHAND) {
			if (this.attackDamageMultiplier != 1.0D) {
				ItemUtil.replaceModifier(modifiers, SharedMonsterAttributes.ATTACK_DAMAGE, ATTACK_DAMAGE_MODIFIER, oldValue -> {
					return (oldValue + 1.0D) * this.attackDamageMultiplier - 1.0D;
				});
			}
			if (this.attackSpeedMultiplier != 1.0D) {
				ItemUtil.replaceModifier(modifiers, SharedMonsterAttributes.ATTACK_SPEED, ATTACK_SPEED_MODIFIER, oldValue -> {
					return (oldValue + 4.0D) * this.attackSpeedMultiplier - 4.0D;
				});
			}
		}
		return modifiers;
	}

}
