package team.cqr.cqrepoured.item.sword;

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
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.item.IEquipListener;
import team.cqr.cqrepoured.item.ItemLore;

import java.util.List;
import java.util.UUID;

public class ItemSwordSunshine extends SwordItem implements IEquipListener {

	protected static final UUID ATTACK_DAMAGE_MODIFIER = UUID.fromString("126A7773-3B53-4AC4-8A90-1CD46C888FD4");
	private static final double DAMAGE_BONUS = 3.0D;

	public ItemSwordSunshine(IItemTier material, int attackDamage, Item.Properties props) {
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
		if (worldIn.isDay()) {
			if (attribute.getModifier(ATTACK_DAMAGE_MODIFIER) != null) {
				return;
			}
			attribute.addPermanentModifier(new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "sunshine_damage_bonus", DAMAGE_BONUS, Operation.ADDITION));
		} else {
			attribute.removeModifier(ATTACK_DAMAGE_MODIFIER);
		}
	}

	@Override
	public void onEquip(LivingEntity entity, ItemStack stack, EquipmentSlotType slot) {

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

		tooltip.add(new TranslationTextComponent(TextFormatting.GOLD + "description.attack_damage_at_day.name", 3));
	}

}
