package team.cqr.cqrepoured.item.armor;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.entity.MobEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.capability.armor.CapabilityCooldownHandlerHelper;
import team.cqr.cqrepoured.client.init.CQRArmorModels;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.item.ItemLore;

import java.util.List;

public class ItemArmorTurtle extends ArmorItem {

	
	private final Multimap<Attribute, AttributeModifier> attributeModifier;

	public ItemArmorTurtle(IArmorMaterial materialIn, EquipmentSlot equipmentSlotIn, Properties prop) {
		super(materialIn, equipmentSlotIn, prop);

		Multimap<Attribute, AttributeModifier> attributeMap = getDefaultAttributeModifiers(EquipmentSlot.MAINHAND);
		ImmutableMultimap.Builder<Attribute, AttributeModifier> modifierBuilder = ImmutableMultimap.builder();
		modifierBuilder.putAll(attributeMap);
		modifierBuilder.put(Attributes.MAX_HEALTH, new AttributeModifier("TurtleHealthModifier", 2.0D, Operation.ADDITION));
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
		Player player = Minecraft.getInstance().player;
		if (player != null) {
			int cooldown = CapabilityCooldownHandlerHelper.getCooldown(player, CQRItems.CHESTPLATE_TURTLE.get());
			if (cooldown > 0) {
				tooltip.add(new TranslationTextComponent("item.cqrepoured.turtle_armor.charging", this.convertCooldown(cooldown)).withStyle(ChatFormatting.RED));
			}
		}
		
		ItemLore.addHoverTextLogic(tooltip, flagIn, "turtle_armor");
	}

	private String convertCooldown(int cd) {
		int i = cd / 20;
		int minutes = i / 60;
		int seconds = i % 60;

		if (seconds < 10) {
			return minutes + ":" + "0" + seconds;
		}

		return minutes + ":" + seconds;
	}
	
	@SuppressWarnings("unchecked")
	@OnlyIn(Dist.CLIENT)
	@Override
	public <A extends HumanoidModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, A _default) {
		return armorSlot == EquipmentSlot.LEGS ? (A) CQRArmorModels.TURTLE_ARMOR_LEGS : (A) CQRArmorModels.TURTLE_ARMOR;
	}

}
