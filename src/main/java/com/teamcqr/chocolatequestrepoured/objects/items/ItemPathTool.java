package com.teamcqr.chocolatequestrepoured.objects.items;

import com.teamcqr.chocolatequestrepoured.capability.pathtool.CapabilityPathToolProvider;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
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
		if(entity instanceof AbstractEntityCQR) {
			
		}
		return super.onLeftClickEntity(stack, player, entity);
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
