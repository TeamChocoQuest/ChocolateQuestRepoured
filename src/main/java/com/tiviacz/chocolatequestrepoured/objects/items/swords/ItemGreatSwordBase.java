package com.tiviacz.chocolatequestrepoured.objects.items.swords;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Multimap;
import com.mojang.realmsclient.dto.PlayerInfo;
import com.tiviacz.chocolatequestrepoured.init.ModItems;
import com.tiviacz.chocolatequestrepoured.init.base.SwordBase;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemGreatSwordBase extends SwordBase
{
	private float damage;
	private int cooldown;
	private float attackSpeed;
	
	public ItemGreatSwordBase(String name, ToolMaterial material, float damage, int cooldown, float attackSpeed)
	{
		super(name, material);
		
		this.damage = damage;
		this.cooldown = cooldown;
		this.attackSpeed = attackSpeed;
	}
	
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) 
	{
		Multimap<String, AttributeModifier> modifiers = super.getAttributeModifiers(slot, stack);
		replaceModifier(modifiers, SharedMonsterAttributes.ATTACK_SPEED, ATTACK_SPEED_MODIFIER, attackSpeed);
		return modifiers;
	}
	 
	protected void replaceModifier(Multimap<String, AttributeModifier> modifierMultimap, IAttribute attribute, UUID id, double value)
	{
		Collection<AttributeModifier> modifiers = modifierMultimap.get(attribute.getName());
		Optional<AttributeModifier> modifierOptional = modifiers.stream().filter(attributeModifier -> attributeModifier.getID().equals(id)).findFirst();

		if(modifierOptional.isPresent()) 
		{ 
			AttributeModifier modifier = modifierOptional.get();
			modifiers.remove(modifier); 
			modifiers.add(new AttributeModifier(modifier.getID(), modifier.getName(), modifier.getAmount() + value, modifier.getOperation()));
	    }
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
		{
			tooltip.add(TextFormatting.BLUE + I18n.format("description.great_sword.name"));
		}		
		else
		{
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
    }
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
	{
		float range = 3F;
		double mx = entityLiving.posX - range;
		double my = entityLiving.posY - range;
		double mz = entityLiving.posZ - range;
		double max = entityLiving.posX + range;
		double may = entityLiving.posY + range;
		double maz = entityLiving.posZ + range;
		
		AxisAlignedBB bb = new AxisAlignedBB(mx, my, mz, max, may, maz);
		
		List<EntityLiving> entitiesInAABB = worldIn.getEntitiesWithinAABB(EntityLiving.class, bb);
	
		for(int i = 0; i < entitiesInAABB.size(); i++) 
		{
			EntityLiving entityInAABB = entitiesInAABB.get(i);
			
			if(getMaxItemUseDuration(stack) - timeLeft <= 30)
			{
				entityInAABB.attackEntityFrom(DamageSource.causeExplosionDamage(entityLiving), damage);
			}
			
			if(getMaxItemUseDuration(stack) - timeLeft > 30 && getMaxItemUseDuration(stack) - timeLeft <= 60)
			{
				entityInAABB.attackEntityFrom(DamageSource.causeExplosionDamage(entityLiving), damage * 3);
			}
			
			if(getMaxItemUseDuration(stack) - timeLeft > 60)
			{
				entityInAABB.attackEntityFrom(DamageSource.causeExplosionDamage(entityLiving), damage * 4);
			}
		}
		
		if(entityLiving instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)entityLiving;
			
			float x = (float)-Math.sin(Math.toRadians(player.rotationYaw));
			float z = (float)Math.cos(Math.toRadians(player.rotationYaw));
			float y = (float)-Math.sin(Math.toRadians(player.rotationPitch));
			x *= (1.0F - Math.abs(y));
			z *= (1.0F - Math.abs(y));
			
			if(player.onGround && getMaxItemUseDuration(stack) - timeLeft > 40)
			{
				player.posY += 0.1D;
				player.motionY += 0.35D;
			}
			
			player.getCooldownTracker().setCooldown(stack.getItem(), cooldown);
			player.swingArm(EnumHand.MAIN_HAND);
			worldIn.playSound(player.posX, player.posY, player.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.AMBIENT, 1.0F, 1.0F, false);
			worldIn.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, player.posX + x, player.posY + y + 1.5D, player.posZ + z, 0D, 0D, 0D);
			stack.damageItem(1, player);
		}
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
    {
        return 72000;
    }

	@Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BOW;
    }
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
		ItemStack stack = playerIn.getHeldItem(handIn);
		playerIn.setActiveHand(handIn);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    }
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
		if(!worldIn.isRemote)
		{
			if(entityIn instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer)entityIn;
				
				if(player.getHeldItemOffhand() == stack)
				{
					if(!player.inventory.addItemStackToInventory(player.getHeldItemOffhand()))
				    {
				        player.entityDropItem(player.getHeldItemOffhand(), 0F);
				    }
				    
				    if(!player.capabilities.isCreativeMode)
				    {
				    	player.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, ItemStack.EMPTY);
				    }
				}
			}
		}
	}
}
