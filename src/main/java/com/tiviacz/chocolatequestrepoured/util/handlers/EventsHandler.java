package com.tiviacz.chocolatequestrepoured.util.handlers;

import com.tiviacz.chocolatequestrepoured.CQRMain;
import com.tiviacz.chocolatequestrepoured.init.ModBlocks;
import com.tiviacz.chocolatequestrepoured.init.ModItems;
import com.tiviacz.chocolatequestrepoured.objects.armor.ItemArmorTurtle;
import com.tiviacz.chocolatequestrepoured.objects.entity.EntitySlimePart;
import com.tiviacz.chocolatequestrepoured.proxy.ClientProxy;
import com.tiviacz.chocolatequestrepoured.util.IHasModel;
import com.tiviacz.chocolatequestrepoured.util.Reference;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

@EventBusSubscriber 
public class EventsHandler
{
	public static void PreInitRegistries(FMLPreInitializationEvent event)
	{
		ClientProxy.registerRenderers();
		EntityHandler.registerEntity();
		MinecraftForge.EVENT_BUS.register(ItemArmorTurtle.class);
	}
	
	public static void initRegistries(FMLInitializationEvent event)
	{
		TileEntityHandler.registerTileEntity();
	}
	
	public static void PostInitRegistries(FMLPostInitializationEvent event)
	{
		
	}
	
	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().registerAll(ModItems.ITEMS.toArray(new Item[0]));
	}
	
	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> event)
	{
		event.getRegistry().registerAll(ModBlocks.BLOCKS.toArray(new Block[0]));
	}
	
	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event)
	{
		for(Item item : ModItems.ITEMS)
		{
			if(item instanceof IHasModel)
			{
				((IHasModel)item).registerModels();
			}
		}
		for(Block block : ModBlocks.BLOCKS)
		{
			if(block instanceof IHasModel)
			{
				((IHasModel)block).registerModels();
			}
		}
	}
	
	@SubscribeEvent
	public static void cancelPlayerFallDamage(LivingFallEvent event)
	{
		if(event.getEntity() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)event.getEntity();
			
			if(player.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() == ModItems.CLOUD_BOOTS)
			{
				event.setDistance(0.0F);
			}
			
			if(player.getHeldItem(EnumHand.MAIN_HAND).getItem() == ModItems.GOLDEN_FEATHER)
			{
				event.setDistance(0.0F);
			}
			
			if(player.getHeldItem(EnumHand.OFF_HAND).getItem() == ModItems.GOLDEN_FEATHER)
			{
				event.setDistance(0.0F);
			}
		}
	}
	
	@SubscribeEvent
	public static void dropGreatSword(PlayerTickEvent event)
	{
	    EntityPlayer player = event.player;
	    ItemStack offStack = player.getHeldItemOffhand();
	    
	    if(offStack.getItem() != ModItems.SWORD_BULL)
	        return;
	    
	    if(!player.inventory.addItemStackToInventory(offStack) && !player.world.isRemote)
	    {
	        player.entityDropItem(offStack, 0F);
	    }
	    
	    player.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, ItemStack.EMPTY);
	}
	
	@SubscribeEvent
	public static void onHit(LivingHurtEvent event)
	{
		if(event.getEntity() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)event.getEntity();
		    EntityLivingBase entity = event.getEntityLiving();
		    
		    if(!player.world.isRemote && entity.hurtTime == 0)
		    {
		    	if(player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.HELMET_SLIME &&
		    			player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == ModItems.CHESTPLATE_SLIME &&
		    			player.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() == ModItems.LEGGINGS_SLIME &&
		    			player.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() == ModItems.BOOTS_SLIME)
		    	{
		    		float amount = event.getAmount() * (1.0F / entity.getTotalArmorValue() * 4.0F);
		    		
		    		if(event.getSource().isFireDamage())
		    		{
		    			return;
		    		}
		    		
		    		if(event.getAmount() > 0F)
		    		{
		    			EntitySlimePart part = new EntitySlimePart(player.world, player, amount);
		    			double d = 1 + (2 - 1) * player.world.rand.nextDouble();
		    			part.setPosition(entity.posX + d, entity.posY + 1.0D, entity.posZ + d);
		    			part.motionX = player.world.rand.nextGaussian();
		    			part.motionY = player.world.rand.nextGaussian();
		    			part.motionZ = player.world.rand.nextGaussian();
		    			entity.world.spawnEntity(part);
		    		}
		    	}
		    }
		    
		    if(!player.world.isRemote)
			{
				if(player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == ModItems.CHESTPLATE_TURTLE)
				{
					if((event.getSource().getTrueSource() instanceof EntityLivingBase)) 
					{
						EntityLivingBase source = (EntityLivingBase)event.getSource().getTrueSource();
						double angle = player.rotationYaw - source.rotationYaw;
						while (angle > 360.0D) angle -= 360.0D;
						while (angle < 0.0D) angle += 360.0D;
						angle = Math.abs(angle - 180.0D);
						
						if(angle > 130.0D)
						{
							event.setCanceled(true);
						}
					}
				}
			}
		}
	}
}