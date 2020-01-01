package com.teamcqr.chocolatequestrepoured.objects.items.swords;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Multimap;
import com.teamcqr.chocolatequestrepoured.util.EntityUtil;
import com.teamcqr.chocolatequestrepoured.util.ItemUtil;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemDagger extends ItemSword {

	private int cooldown;
	private AttributeModifier movementSpeed;

	public ItemDagger(ToolMaterial material, int cooldown) {
		super(material);

		this.cooldown = cooldown;
		this.movementSpeed = new AttributeModifier("DaggerSpeedModifier", 0.05D, 2);
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

		if (slot == EntityEquipmentSlot.MAINHAND) {
			multimap.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), this.movementSpeed);
		}

		return multimap;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);

		if (playerIn.onGround && !playerIn.isSwingInProgress) {
			EntityUtil.move2D(playerIn, playerIn.moveStrafing, playerIn.moveForward, 1.0D, playerIn.rotationYaw);

			playerIn.motionY = 0.2D;
			playerIn.getCooldownTracker().setCooldown(stack.getItem(), this.cooldown);
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		if (!attacker.world.isRemote && ItemUtil.compareRotations(target.rotationYaw, attacker.rotationYaw, 50.0D)) {
			float damage = (float) attacker.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
			DamageSource source = attacker instanceof EntityPlayer ? DamageSource.causePlayerDamage((EntityPlayer) attacker) : DamageSource.causeMobDamage(attacker);

			target.attackEntityFrom(source, damage * 2F * (attacker.fallDistance > 0.0F ? 1.5F : 1.0F));
			((WorldServer) attacker.world).spawnParticle(EnumParticleTypes.CRIT, target.posX, target.posY, target.posZ, 12, 0.5D, 0.5D, 0.5D, 1.0D);
		}
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.BLUE + "200% " + I18n.format("description.rear_damage.name"));

		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.dagger.name"));
		} else {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
	}

}
