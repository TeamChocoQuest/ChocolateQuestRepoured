package team.cqr.cqrepoured.item.armor;

import java.util.List;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import team.cqr.cqrepoured.capability.armor.CapabilityCooldownHandlerHelper;
import team.cqr.cqrepoured.client.render.armor.RenderArmorTurtle;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.item.ItemLore;

public class ItemArmorTurtle extends CQRGeoArmorBase implements GeoItem {

	
	private final Multimap<Attribute, AttributeModifier> attributeModifier;

	public ItemArmorTurtle(ArmorMaterial materialIn, Type equipmentSlotIn, Properties prop) {
		super(materialIn, equipmentSlotIn, prop);

		Multimap<Attribute, AttributeModifier> attributeMap = getDefaultAttributeModifiers(EquipmentSlot.MAINHAND);
		ImmutableMultimap.Builder<Attribute, AttributeModifier> modifierBuilder = ImmutableMultimap.builder();
		modifierBuilder.putAll(attributeMap);
		modifierBuilder.put(Attributes.MAX_HEALTH, new AttributeModifier("TurtleHealthModifier", 2.0D, Operation.ADDITION));
		this.attributeModifier = modifierBuilder.build();
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		if (slot == Mob.getEquipmentSlotForItem(stack)) {
			return this.attributeModifier;
		}
		return super.getAttributeModifiers(slot, stack);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		Player player = Minecraft.getInstance().player;
		if (player != null) {
			int cooldown = CapabilityCooldownHandlerHelper.getCooldown(player, CQRItems.CHESTPLATE_TURTLE.get());
			if (cooldown > 0) {
				tooltip.add(Component.translatable("item.cqrepoured.turtle_armor.charging", this.convertCooldown(cooldown)).withStyle(ChatFormatting.RED));
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

	@Override
	public void registerControllers(ControllerRegistrar arg0) {
		
	}

	@Override
	protected GeoArmorRenderer<?> createRenderer() {
		return new RenderArmorTurtle();
	}
	
}
