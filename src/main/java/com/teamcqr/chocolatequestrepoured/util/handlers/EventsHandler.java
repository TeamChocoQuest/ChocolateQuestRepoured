package com.teamcqr.chocolatequestrepoured.util.handlers;

import java.util.UUID;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.init.base.ItemDungeonPlacer;
import com.teamcqr.chocolatequestrepoured.network.ParticlesMessageToClient;
import com.teamcqr.chocolatequestrepoured.objects.entity.EntitySlimePart;
import com.teamcqr.chocolatequestrepoured.util.IHasModel;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber 
public class EventsHandler
{
	private static final UUID bonusHealthUUID = UUID.randomUUID();
	
	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().registerAll(ModItems.ITEMS.toArray(new Item[0]));
		ItemDungeonPlacer.itemRegistry = event.getRegistry();
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
	
	@SubscribeEvent
	public static void onDefense(LivingAttackEvent event)
	{
		boolean tep = false;
		
		if(event.getEntityLiving() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)event.getEntityLiving();
			Entity attacker = event.getSource().getTrueSource();
			@SuppressWarnings("unused")
			float amount = event.getAmount();
			World world = player.world;
			
			if(player.getActiveItemStack().getItem() != ModItems.SHIELD_WALKER || player.getHeldItemMainhand().getItem() != ModItems.SWORD_WALKER || player.getRidingEntity() != null || attacker == null) 
			{
				return;
			}
			
			double d = attacker.posX + (attacker.world.rand.nextDouble() - 0.5D) * 4.0D;
			double d1 = attacker.posY;
			double d2 = attacker.posZ + (attacker.world.rand.nextDouble() - 0.5D) * 4.0D;
			
			@SuppressWarnings("unused")
			double d3 = player.posX;
			@SuppressWarnings("unused")
			double d4 = player.posY;
			@SuppressWarnings("unused")
			double d5 = player.posZ;
			
			int i = MathHelper.floor(d);
			int j = MathHelper.floor(d1);
			int k = MathHelper.floor(d2);
			
			BlockPos ep = new BlockPos(i, j, k);
			BlockPos ep1 = new BlockPos(i, j + 1, k);
			
			if(world.getCollisionBoxes(player, player.getEntityBoundingBox()).size() == 0 && !world.containsAnyLiquid(attacker.getEntityBoundingBox()) && player.isActiveItemStackBlocking() && player.getDistanceSq(attacker) >= 25.0D)
			{
				if(world.getBlockState(ep).getBlock().isPassable(world, ep) && world.getBlockState(ep1).getBlock().isPassable(world, ep1))
				{
					tep = true;
				}
				else
				{
					tep = false;
					ParticlesMessageToClient packet = new ParticlesMessageToClient(player.getPositionVector(), 12, 12);
					CQRMain.NETWORK.sendToAllAround(packet, packet.getTargetPoint(player));
				}
			}
			
			if(tep)
			{
				if(world.getBlockState(ep).getBlock().isPassable(world, ep) && world.getBlockState(ep1).getBlock().isPassable(world, ep1))
				{		
					if(player instanceof EntityPlayerMP)
					{
						EntityPlayerMP playerMP = (EntityPlayerMP)player;

						playerMP.connection.setPlayerLocation(d, d1, d2, playerMP.rotationYaw, playerMP.rotationPitch);
						ParticlesMessageToClient packet = new ParticlesMessageToClient(playerMP.getPositionVector(), 24, 12);
						CQRMain.NETWORK.sendToAllAround(packet, packet.getTargetPoint(playerMP));
						world.playSound(null, d, d1, d2, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.MASTER, 1.0F, 1.0F);
					}
					event.setCanceled(true);
					tep = false;
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onLivingUpdate(LivingUpdateEvent event)
	{
		if(!(event.getEntity() instanceof EntityPlayer))
		{
			return;
		}
		
		EntityPlayer player = (EntityPlayer)event.getEntity();
		
		if(!player.world.isRemote)
		{
			IAttributeInstance attribute = player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
			AttributeModifier bonusHealth = new AttributeModifier(bonusHealthUUID, "TurtleBonusHealthModifier", 4D, 0);
			
			if(player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.HELMET_SLIME && player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == ModItems.CHESTPLATE_SLIME && player.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() == ModItems.LEGGINGS_SLIME && player.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() == ModItems.BOOTS_SLIME)
	    	{
				if(attribute.hasModifier(bonusHealth))
				{
					return;
				}
				
				else if(!attribute.hasModifier(bonusHealth))
				{
					attribute.applyModifier(bonusHealth);
				} 
	    	}
			else
			{
				if(attribute.hasModifier(bonusHealth))
				{
					attribute.removeModifier(bonusHealth);
				}
			}
		}
	} 
}