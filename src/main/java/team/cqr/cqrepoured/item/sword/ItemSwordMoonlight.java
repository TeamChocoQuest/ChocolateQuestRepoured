package team.cqr.cqrepoured.item.sword;

import java.util.List;
import java.util.UUID;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.item.IEquipListener;
import team.cqr.cqrepoured.item.ItemLore;

public class ItemSwordMoonlight extends SwordItem implements IEquipListener {

	protected static final UUID ATTACK_DAMAGE_MODIFIER = UUID.fromString("89ADDE87-B021-417C-9112-2E2C94CBB0D1");
	private static final double DAMAGE_BONUS = 3.0D;

	public ItemSwordMoonlight(IItemTier material, int attackDamage, Item.Properties props) {
		super(material, attackDamage, material.getSpeed(), props);
	}

	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
		if (!(entityIn instanceof LivingEntity)) {
			return;
		}
		if (!isSelected) {
			return;
		}
		ModifiableAttributeInstance attribute = ((LivingEntity) entityIn).getAttribute(Attributes.ATTACK_DAMAGE);
		if (!worldIn.isDay()) {
			if (attribute.getModifier(ATTACK_DAMAGE_MODIFIER) != null) {
				return;
			}
			attribute.addPermanentModifier(new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "moonlight_damage_bonus", DAMAGE_BONUS, Operation.ADDITION));
		} else {
			attribute.removeModifier(ATTACK_DAMAGE_MODIFIER);
		}
	}

	@Override
	public void onUnequip(LivingEntity entity, ItemStack stack, EquipmentSlotType slot) {
		ModifiableAttributeInstance attribute = entity.getAttribute(Attributes.ATTACK_DAMAGE);
		attribute.removeModifier(ATTACK_DAMAGE_MODIFIER);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		ItemLore.addHoverTextLogic(tooltip, flagIn, this.getRegistryName().getPath());

		tooltip.add(new StringTextComponent(TextFormatting.DARK_AQUA + "+3 " + I18n.get("description.attack_damage_at_night.name")));
	}

	@Override
	public void onEquip(LivingEntity entity, ItemStack stack, EquipmentSlotType slot) {
		//Ignored
	}

}
