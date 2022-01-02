package team.cqr.cqrepoured.item.sword;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.SwordItem;
import org.lwjgl.input.Keyboard;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;
import team.cqr.cqrepoured.item.IEquipListener;

public class ItemSwordSunshine extends SwordItem implements IEquipListener {

	protected static final UUID ATTACK_DAMAGE_MODIFIER = UUID.fromString("126A7773-3B53-4AC4-8A90-1CD46C888FD4");
	private static final double DAMAGE_BONUS = 3.0D;

	public ItemSwordSunshine(ToolMaterial material) {
		super(material);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
		if (!(entityIn instanceof LivingEntity)) {
			return;
		}
		if (!isSelected) {
			return;
		}
		IAttributeInstance attribute = ((LivingEntity) entityIn).getEntityAttribute(Attributes.ATTACK_DAMAGE);
		if (worldIn.isDaytime()) {
			if (attribute.getModifier(ATTACK_DAMAGE_MODIFIER) != null) {
				return;
			}
			attribute.applyModifier(new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "sunshine_damage_bonus", DAMAGE_BONUS, 0).setSaved(false));
		} else {
			attribute.removeModifier(ATTACK_DAMAGE_MODIFIER);
		}
	}

	@Override
	public void onEquip(LivingEntity entity, ItemStack stack, EquipmentSlotType slot) {

	}

	@Override
	public void onUnequip(LivingEntity entity, ItemStack stack, EquipmentSlotType slot) {
		IAttributeInstance attribute = entity.getEntityAttribute(Attributes.ATTACK_DAMAGE);
		attribute.removeModifier(ATTACK_DAMAGE_MODIFIER);
	}

	@Override
	@Dist(OnlyIn.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.sunshine.name"));
		} else {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}

		tooltip.add(TextFormatting.GOLD + "+3 " + I18n.format("description.attack_damage_at_day.name"));
	}

}
