package com.teamcqr.chocolatequestrepoured.objects.items.swords;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Multimap;
import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.init.base.SwordBase;
import com.teamcqr.chocolatequestrepoured.network.ParticlesMessageToClient;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemDaggerBase extends SwordBase
{
	private int cooldown;
	private AttributeModifier movementSpeed;
	
	public ItemDaggerBase(String name, ToolMaterial material, int cooldown)
	{
		super(name, material);
		
		this.cooldown = cooldown;
		this.movementSpeed = new AttributeModifier("DaggerSpeedModifier", 0.05D, 2);
	}
	
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot equipmentSlot, ItemStack stack)
    {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(equipmentSlot, stack);

        if(equipmentSlot == EntityEquipmentSlot.MAINHAND)
        {
            multimap.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), this.movementSpeed);
        }
        return multimap;
    }
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
		ItemStack stack = playerIn.getHeldItem(handIn);
		
        if(playerIn.onGround && !playerIn.isSwingInProgress)
        {
        	float rot = playerIn.rotationYawHead * 3.1416F / 180.0F;
        	double mx = -Math.sin(rot);
        	double mz = Math.cos(rot);
        	playerIn.motionX += mx * playerIn.moveForward;
        	playerIn.motionZ += mz * playerIn.moveForward;
        	rot = (float)(rot - 1.57D);
        	mx = -Math.sin(rot);
        	mz = Math.cos(rot);
        	playerIn.motionX += mx * playerIn.moveStrafing;
        	playerIn.motionZ += mz * playerIn.moveStrafing;
        	playerIn.motionY = 0.2D;
        	playerIn.getCooldownTracker().setCooldown(stack.getItem(), cooldown);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
	
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
		double angle = attacker.rotationYaw - target.rotationYaw;
		while (angle > 360.0D) angle -= 360.0D;
		while (angle < 0.0D) angle += 360.0D;
		angle = Math.abs(angle - 180.0D);
		
		if(angle > 130.0D)
		{
			Vec3d vec3d = target.getPositionVector();
			
			ParticlesMessageToClient packet = new ParticlesMessageToClient(vec3d, 9, 12);
			CQRMain.NETWORK.sendToAllAround(packet, packet.getTargetPoint(target));
			
			float damage = (float)attacker.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
			target.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer)attacker), damage * 2F * (attacker.fallDistance > 0.0F ? 1.5F : 1.0F));
		}
		return true;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
		tooltip.add(TextFormatting.BLUE + "200% " + I18n.format("description.rear_damage.name"));
		
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
		{
			tooltip.add(TextFormatting.BLUE + I18n.format("description.dagger.name"));
		}		
		else
		{
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
    }
}