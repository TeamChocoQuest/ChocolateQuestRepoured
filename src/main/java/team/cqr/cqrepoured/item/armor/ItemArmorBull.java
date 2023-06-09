package team.cqr.cqrepoured.item.armor;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.client.init.CQRArmorModels;
import team.cqr.cqrepoured.item.ItemLore;
import team.cqr.cqrepoured.util.ItemUtil;

import java.util.List;

public class ItemArmorBull extends ArmorItem {

	private final Multimap<Attribute, AttributeModifier> attributeModifier;

	public ItemArmorBull(IArmorMaterial materialIn, EquipmentSlot equipmentSlotIn, Properties prop) {
		super(materialIn, equipmentSlotIn, prop);

		Multimap<Attribute, AttributeModifier> attributeMap = getDefaultAttributeModifiers(EquipmentSlot.MAINHAND);
		ImmutableMultimap.Builder<Attribute, AttributeModifier> modifierBuilder = ImmutableMultimap.builder();
		modifierBuilder.putAll(attributeMap);
		modifierBuilder.put(Attributes.ATTACK_DAMAGE,new AttributeModifier("BullArmorModifier", 1D, Operation.ADDITION));
		this.attributeModifier = modifierBuilder.build();
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		if (slot == MobEntity.getEquipmentSlotForItem(stack)) {
			return this.attributeModifier;
		}
		return super.getAttributeModifiers(slot, stack);
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level worldIn, List<TextComponent> tooltip, TooltipFlag flagIn) {
		ItemLore.addHoverTextLogic(tooltip, flagIn, "bull_armor");
	}

	@Override
	public void onArmorTick(ItemStack stack, Level world, Player player) {
		super.onArmorTick(stack, world, player);
		if (ItemUtil.hasFullSet(player, ItemArmorBull.class)) {
			if (player.isSprinting()) {
				player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 0, 1, false, false));
			}
		}
	}

	@SuppressWarnings("unchecked")
	@OnlyIn(Dist.CLIENT)
	@Override
	public <A extends HumanoidModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, A _default) {
		return armorSlot == EquipmentSlot.LEGS ? (A) CQRArmorModels.BULL_ARMOR_LEGS : (A) CQRArmorModels.BULL_ARMOR;
	}

}
