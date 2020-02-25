package com.teamcqr.chocolatequestrepoured.objects.items;

import com.teamcqr.chocolatequestrepoured.capability.pathtool.CapabilityPathTool;
import com.teamcqr.chocolatequestrepoured.capability.pathtool.CapabilityPathToolProvider;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemPathTool extends Item {

	public ItemPathTool() {
		this.setMaxStackSize(1);
	}
	
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return CapabilityPathToolProvider.createProvider();
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		/*
		 * Left click -> apply path points and overwrite the existing ones
		 * Shift left click -> get existing path points
		 */
		CapabilityPathTool capa = stack.getCapability(CapabilityPathToolProvider.PATH_TOOL, null);
		if(entity instanceof AbstractEntityCQR && capa.getPathPoints().length > 0) {
			if(player.isSneaking()) {
				((AbstractEntityCQR)entity).setPath(capa.getPathPoints());
				((WorldServer) player.world).spawnParticle((EntityPlayerMP) player, EnumParticleTypes.VILLAGER_HAPPY, false, entity.posX, entity.posY + 0.5D, entity.posZ, 8, 0.5D, 0.5D, 0.5D, 0.1D);
			} else {
				BlockPos[] oldPath = ((AbstractEntityCQR)entity).getGuardPathPoints();
				final BlockPos home = ((AbstractEntityCQR)entity).getHomePositionCQR();
				if(oldPath.length > 0) {
					final BlockPos[] path = new BlockPos[oldPath.length];
					for(int i = 0; i < path.length; i++) {
						path[i] = oldPath[i];
						path[i] = path[i].add(home);
					}
					capa.setPathPoints(path);
				}
			}
		}
		return true;
	}
	
	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		/*
		 * Shift right click with no target block -> erase positions
		 * right click -> add position
		 */
		ItemStack stack = player.getHeldItem(hand);
		BlockPos position = pos.offset(side);
		if(player.isSneaking()) {
			resetPathPoints(stack, player);
		} else {
			addPathPoint(stack, position, player);
		}
		
		return EnumActionResult.SUCCESS;
	}
	
	
	/*
	 * Methods to edit capability
	 */
	public static void resetPathPoints(ItemStack stack, EntityPlayer player) {
		stack.getCapability(CapabilityPathToolProvider.PATH_TOOL, null).clearPathPoints();
		if(player.world.isRemote) {
			player.sendMessage(new TextComponentString("Cleared Path!"));
		}
	}
	
	public static void addPathPoint(ItemStack stack, BlockPos pos, EntityPlayer player) {
		stack.getCapability(CapabilityPathToolProvider.PATH_TOOL, null).addPathPoint(pos);
		if(player.world.isRemote) {
			player.sendMessage(new TextComponentString("Added " + pos.toString() + " to the path!"));
		}
	}

}
