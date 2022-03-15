package team.cqr.cqrepoured.item.armor;

import java.util.List;

import com.google.common.collect.Multimap;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.client.init.CQRArmorModels;
import team.cqr.cqrepoured.item.ItemLore;
import team.cqr.cqrepoured.util.ItemUtil;

public class ItemArmorBull extends ArmorItem {

	private AttributeModifier strength;

	public ItemArmorBull(IArmorMaterial materialIn, EquipmentSlotType equipmentSlotIn, Properties prop) {
		super(materialIn, equipmentSlotIn, prop);

		this.strength = new AttributeModifier("BullArmorModifier", 1D, Operation.ADDITION);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
		Multimap<Attribute, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

		if (slot == MobEntity.getEquipmentSlotForItem(stack)) {
			multimap.put(Attributes.ATTACK_DAMAGE, this.strength);
		}

		return multimap;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		ItemLore.addHoverTextLogic(tooltip, flagIn, this.getRegistryName().getPath());
	}

	@Override
	public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
		super.onArmorTick(stack, world, player);
		if (ItemUtil.hasFullSet(player, ItemArmorBull.class)) {
			if (player.isSprinting()) {
				player.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 0, 1, false, false));
			}
		}
	}

	@SuppressWarnings("unchecked")
	@OnlyIn(Dist.CLIENT)
	@Override
	public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
		return armorSlot == EquipmentSlotType.LEGS ? (A) CQRArmorModels.BULL_ARMOR_LEGS : (A) CQRArmorModels.BULL_ARMOR;
	}

}
