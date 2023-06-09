package team.cqr.cqrepoured.item.armor;

import java.util.List;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.entity.MobEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.item.ItemLore;

public class ItemBootsCloud extends ArmorItem {

	private final Multimap<Attribute, AttributeModifier> attributeModifier;
	
	//private AttributeModifier movementSpeed;

	public ItemBootsCloud(IArmorMaterial materialIn, EquipmentSlot equipmentSlotIn, Properties prop) {
		super(materialIn, equipmentSlotIn, prop);

		//this.movementSpeed = new AttributeModifier("CloudBootsSpeedModifier", 0.15D, Operation.MULTIPLY_TOTAL);
		Multimap<Attribute, AttributeModifier> attributeMap = getDefaultAttributeModifiers(EquipmentSlot.MAINHAND);
		ImmutableMultimap.Builder<Attribute, AttributeModifier> modifierBuilder = ImmutableMultimap.builder();
		modifierBuilder.putAll(attributeMap);
		modifierBuilder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier("CloudBootsSpeedModifier", 0.15D, Operation.MULTIPLY_TOTAL));
		this.attributeModifier = modifierBuilder.build();
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		return slot == MobEntity.getEquipmentSlotForItem(stack) ? this.attributeModifier : super.getAttributeModifiers(slot, stack);
	/*	Multimap<Attribute, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

		if (slot == MobEntity.getEquipmentSlotForItem(stack)) {
			multimap.put(Attributes.MOVEMENT_SPEED, this.movementSpeed);
		}

		return multimap; */
	}

	@Override
	public void onArmorTick(ItemStack stack, Level world, Player player) {
		super.onArmorTick(stack, world, player);
		
		player.addEffect(new MobEffectInstance(MobEffects.JUMP, 0, 4, false, false));

		player.flyingSpeed += 0.04F; //Correct replacement?
		if (player.fallDistance > 0.0F || player.isSprinting()) {
			if(!world.isClientSide) {
				((ServerLevel)world).sendParticles(ParticleTypes.CLOUD, player.position().x, player.position().y, player.position().y, 3, (random.nextFloat() - 0.5F) / 2.0F, -0.5D, (random.nextFloat() - 0.5F) / 2.0F, 1);
			}
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level worldIn, List<TextComponent> tooltip, TooltipFlag flagIn) {
		ItemLore.addHoverTextLogic(tooltip, flagIn, this.getRegistryName().getPath());
	}

}
