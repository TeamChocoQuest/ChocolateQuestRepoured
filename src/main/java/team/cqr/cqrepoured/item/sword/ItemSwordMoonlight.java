package team.cqr.cqrepoured.item.sword;

import java.util.List;
import java.util.UUID;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import team.cqr.cqrepoured.item.IEquipListener;
import team.cqr.cqrepoured.item.ItemLore;

public class ItemSwordMoonlight extends ItemCQRWeapon implements IEquipListener {

	protected static final UUID ATTACK_DAMAGE_MODIFIER = UUID.fromString("89ADDE87-B021-417C-9112-2E2C94CBB0D1");
	private static final double DAMAGE_BONUS = 3.0D;

	public ItemSwordMoonlight(Tier material, Item.Properties props) {
		super(material, props);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
		if (!(entityIn instanceof LivingEntity)) {
			return;
		}
		if (!isSelected) {
			return;
		}
		AttributeInstance attribute = ((LivingEntity) entityIn).getAttribute(Attributes.ATTACK_DAMAGE);
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
	public void onUnequip(LivingEntity entity, ItemStack stack, EquipmentSlot slot) {
		AttributeInstance attribute = entity.getAttribute(Attributes.ATTACK_DAMAGE);
		attribute.removeModifier(ATTACK_DAMAGE_MODIFIER);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		ItemLore.addHoverTextLogic(tooltip, flagIn, ForgeRegistries.ITEMS.getKey(this).getPath());

		tooltip.add(Component.translatable("item.cqrepoured.sword_moonlight.attack_damage_at_night", 3).withStyle(ChatFormatting.DARK_AQUA));
	}

	@Override
	public void onEquip(LivingEntity entity, ItemStack stack, EquipmentSlot slot) {
		//Ignored
	}

}
