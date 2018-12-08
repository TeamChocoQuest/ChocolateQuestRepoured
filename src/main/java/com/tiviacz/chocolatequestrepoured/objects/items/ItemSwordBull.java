package com.tiviacz.chocolatequestrepoured.objects.items;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Multimap;
import com.tiviacz.chocolatequestrepoured.init.base.SwordBase;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
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

public class ItemSwordBull extends SwordBase
{
	private int damageScaling = 0;
	private boolean shouldScale = false;
	private String IS = "isReady";
	private String CD = "Cooldown";
	
	public ItemSwordBull(String name, ToolMaterial material)
	{
		super(name, material);
	}
	
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) 
	{
		Multimap<String, AttributeModifier> modifiers = super.getAttributeModifiers(slot, stack);
		replaceModifier(modifiers, SharedMonsterAttributes.ATTACK_SPEED, ATTACK_SPEED_MODIFIER, -0.6);
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
		if(stack.hasTagCompound())
		{
			if(!stack.getTagCompound().getBoolean(IS))
			{
				tooltip.add(TextFormatting.RED + I18n.format("description.charging.name"));
			}
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
		{
			tooltip.add(TextFormatting.BLUE + I18n.format("description.bull_sword.name"));
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
		
		double x = entityLiving.posX + itemRand.nextFloat() - 0.5D;
		double y = entityLiving.posY + itemRand.nextFloat();
		double z = entityLiving.posZ + itemRand.nextFloat() - 0.5D;
		
		AxisAlignedBB bb = new AxisAlignedBB(mx, my, mz, max, may, maz);
		
		List<EntityLiving> entitiesInAABB = worldIn.getEntitiesWithinAABB(EntityLiving.class, bb);
	
		for(int i = 0; i < entitiesInAABB.size(); i++) 
		{
			EntityLiving entityInAABB = entitiesInAABB.get(i);
			
			if(damageScaling <= 20)
			{
				entityInAABB.attackEntityFrom(DamageSource.causeExplosionDamage(entityLiving), 1.0F);
			}
			
			if(damageScaling > 20 && damageScaling <= 60)
			{
				entityInAABB.attackEntityFrom(DamageSource.causeExplosionDamage(entityLiving), 3.0F);
			}
			
			if(damageScaling > 60)
			{
				entityInAABB.attackEntityFrom(DamageSource.causeExplosionDamage(entityLiving), 4.0F);
			}
		}
		
		worldIn.playSound(x, y, z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.AMBIENT, 1.0F, 1.0F, false);
		worldIn.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, x, y, z, itemRand.nextFloat() - 0.5F, itemRand.nextFloat() - 0.5F, itemRand.nextFloat() - 0.5F);
		stack.getTagCompound().setBoolean(IS, false);
		stack.getTagCompound().setInteger(CD, 100);
		shouldScale = false;
		damageScaling = 0;
		stack.setItemDamage(stack.getItemDamage() + 1);
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
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		
		if(itemstack.getTagCompound().getBoolean(IS))
		{
			shouldScale = true;
		    playerIn.setActiveHand(handIn);
		    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
		}
		return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
    }
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
		NBTTagCompound nbt = stack.getTagCompound();
		
		if(nbt == null)
		{
		    nbt = new NBTTagCompound();
		    stack.setTagCompound(nbt);
		}
		
		if(!nbt.hasKey(IS))
		{
			nbt.setBoolean(IS, false);
		}
		
		if(!nbt.hasKey(CD))
		{
			nbt.setInteger(CD, 0);
		}
		
		if(!nbt.getBoolean(IS))
		{
			if(nbt.getInteger(CD) > 0)
			{
				nbt.setInteger(CD, nbt.getInteger(CD) - 1);
			}
				
			if(nbt.getInteger(CD) == 0)
			{
				nbt.setBoolean(IS, true);
			}
		}
			
		if(shouldScale)
		{
			this.damageScaling++;
		}
	}
	
/*	@Override
	@SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack)
    {
		if(stack.hasTagCompound())
		{
			if(stack.getTagCompound().getBoolean(IS))
	        {
	        	return true;
	        }
		}
		return false;
    } */
}