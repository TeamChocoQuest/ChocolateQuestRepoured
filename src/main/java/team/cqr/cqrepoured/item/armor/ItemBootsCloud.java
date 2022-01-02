package team.cqr.cqrepoured.item.armor;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Multimap;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.potion.Effects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;

public class ItemBootsCloud extends ArmorItem {

	private AttributeModifier movementSpeed;

	public ItemBootsCloud(ArmorMaterial materialIn, int renderIndexIn, EquipmentSlotType equipmentSlotIn) {
		super(materialIn, renderIndexIn, equipmentSlotIn);

		this.movementSpeed = new AttributeModifier("CloudBootsSpeedModifier", 0.15D, 2);
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

		if (slot == MobEntity.getSlotForItemStack(stack)) {
			multimap.put(Attributes.MOVEMENT_SPEED.getName(), this.movementSpeed);
		}

		return multimap;
	}

	@Override
	public void onArmorTick(World world, PlayerEntity player, ItemStack stack) {
		player.addPotionEffect(new EffectInstance(Effects.JUMP_BOOST, 0, 4, false, false));

		player.jumpMovementFactor += 0.04F;
		if (player.fallDistance > 0.0F || player.isSprinting()) {
			world.spawnParticle(ParticleTypes.CLOUD, player.posX, player.posY, player.posZ, (itemRand.nextFloat() - 0.5F) / 2.0F, -0.5D, (itemRand.nextFloat() - 0.5F) / 2.0F);
		}
	}

	@Override
	@Dist(OnlyIn.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.cloud_boots.name"));
		} else {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
	}

}
