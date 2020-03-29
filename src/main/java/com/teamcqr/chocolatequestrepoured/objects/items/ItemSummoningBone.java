package com.teamcqr.chocolatequestrepoured.objects.items;

import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntitySummoningCircle;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntitySummoningCircle.ECircleTexture;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemSummoningBone extends Item {

	public ItemSummoningBone() {
		setMaxDamage(3);
		setMaxStackSize(1);
		setNoRepair();
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 40;
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		if(super.onItemUseFinish(stack, worldIn, entityLiving) != null) {
			if(entityLiving instanceof EntityPlayerMP) {
				if(spawnEntity((EntityPlayer) entityLiving, worldIn, stack)) {
					stack.attemptDamageItem(1, entityLiving.getRNG(), (EntityPlayerMP) entityLiving);
				}
			}
			return stack;
		} 
		return stack;
	}
	
	public boolean spawnEntity(EntityPlayer player, World worldIn, ItemStack item) {
		Vec3d v = player.getLookVec();
		v = v.normalize().scale(5);
		RayTraceResult result = worldIn.rayTraceBlocks(player.getPositionVector(), player.getPositionVector().add(v));//Minecraft.getMinecraft().getRenderViewEntity().rayTrace(20D, 1.0F);

		if (result != null) {
			if(worldIn.isAirBlock(result.getBlockPos().offset(EnumFacing.UP, 1)) && worldIn.isAirBlock(result.getBlockPos().offset(EnumFacing.UP, 2))) {
				//DONE: Spawn circle
				ResourceLocation resLoc = new ResourceLocation(Reference.MODID, "skeleton");
				//Get entity id
				if(item.hasTagCompound() && item.getTagCompound().hasKey("entity_to_summon")) {
					try {
						resLoc = new ResourceLocation(item.getTagCompound().getString("entity_to_summon"));
						if(!EntityList.isRegistered(resLoc)) {
							resLoc = new ResourceLocation(Reference.MODID, "skeleton");
						}
					} catch(Exception ex) {
						resLoc = new ResourceLocation(Reference.MODID, "skeleton");
					}
				}
				EntitySummoningCircle circle = new EntitySummoningCircle(worldIn, resLoc, 1F, ECircleTexture.METEOR, null, player);
				circle.setSummon(resLoc);
				circle.setPosition(result.getBlockPos().getX(), result.getBlockPos().getY() +1, result.getBlockPos().getZ());
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
		super.onPlayerStoppedUsing(stack, worldIn, entityLiving, timeLeft);
		if(entityLiving instanceof EntityPlayer) {
			((EntityPlayer) entityLiving).getCooldownTracker().setCooldown(this, 20);
		}
	}

}
