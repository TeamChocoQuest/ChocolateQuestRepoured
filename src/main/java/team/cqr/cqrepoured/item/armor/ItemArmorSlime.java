package team.cqr.cqrepoured.item.armor;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.entity.MobEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.client.init.CQRArmorModels;
import team.cqr.cqrepoured.item.ItemLore;

import java.util.List;

public class ItemArmorSlime extends ArmorItem {

	private final Multimap<Attribute, AttributeModifier> attributeModifier;

	public ItemArmorSlime(IArmorMaterial materialIn, EquipmentSlot equipmentSlotIn, Properties prop)
	{
		super(materialIn, equipmentSlotIn, prop);
		
		Multimap<Attribute, AttributeModifier> attributeMap = getDefaultAttributeModifiers(EquipmentSlot.MAINHAND);
		ImmutableMultimap.Builder<Attribute, AttributeModifier> modifierBuilder = ImmutableMultimap.builder();
		modifierBuilder.putAll(attributeMap);
		modifierBuilder.put(Attributes.MAX_HEALTH, new AttributeModifier("SlimeHealthModifier", 2.0D, Operation.ADDITION));
		modifierBuilder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier("SlimeKnockbackModifier", -0.25D, Operation.ADDITION));
		this.attributeModifier = modifierBuilder.build();
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot equipmentSlot, ItemStack stack)
	{
		return equipmentSlot == MobEntity.getEquipmentSlotForItem(stack) ? this.attributeModifier : super.getAttributeModifiers(equipmentSlot, stack);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level worldIn, List<TextComponent> tooltip, TooltipFlag flagIn) {
		ItemLore.addHoverTextLogic(tooltip, flagIn, "slime_armor");
	}

	@SuppressWarnings("unchecked")
	@OnlyIn(Dist.CLIENT)
	@Override
	public <A extends HumanoidModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, A _default) {
		return armorSlot == EquipmentSlot.LEGS ? (A) CQRArmorModels.SLIME_ARMOR_LEGS : (A) CQRArmorModels.SLIME_ARMOR;
	}

}
