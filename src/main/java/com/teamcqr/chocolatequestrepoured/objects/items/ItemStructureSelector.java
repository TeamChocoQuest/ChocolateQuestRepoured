package com.teamcqr.chocolatequestrepoured.objects.items;

import java.util.List;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.capability.structureselector.CapabilityStructureSelector;
import com.teamcqr.chocolatequestrepoured.capability.structureselector.CapabilityStructureSelectorProvider;
import com.teamcqr.chocolatequestrepoured.network.StructureSelectorPacket;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityExporter;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ItemStructureSelector extends Item {

	public ItemStructureSelector() {
		this.setMaxStackSize(1);
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX,
			float hitY, float hitZ, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);

		if (world.getTileEntity(pos) instanceof TileEntityExporter) {
			if (world.isRemote) {
				TileEntityExporter tileEntity = (TileEntityExporter) world.getTileEntity(pos);
				CapabilityStructureSelector capability = stack.getCapability(CapabilityStructureSelectorProvider.STRUCTURE_SELECTOR, null);
				BlockPos pos1 = capability.getPos1();
				BlockPos pos2 = capability.getPos2();
				if (tileEntity.relativeMode) {
					pos1 = pos1.subtract(pos);
					pos2 = pos2.subtract(pos);
				}
				tileEntity.setValues(pos1.getX(), pos1.getY(), pos1.getZ(), pos2.getX(), pos2.getY(), pos2.getZ(), tileEntity.structureName, tileEntity.partModeUsing, tileEntity.relativeMode);
			}
		} else {
			if (player.isSneaking()) {
				ItemStructureSelector.setSecondPos(stack, player.getPosition(), player);
			} else {
				ItemStructureSelector.setSecondPos(stack, pos, player);
			}
		}
		return EnumActionResult.SUCCESS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);

		if (playerIn.isSneaking()) {
			ItemStructureSelector.setSecondPos(stack, playerIn.getPosition(), playerIn);
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return CapabilityStructureSelectorProvider.createProvider();
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		CapabilityStructureSelector capability = stack.getCapability(CapabilityStructureSelectorProvider.STRUCTURE_SELECTOR, null);
		BlockPos pos1 = capability.getPos1();
		BlockPos pos2 = capability.getPos2();
		if (pos1 != null) {
			tooltip.add("First position: " + pos1);
		} else {
			tooltip.add("First position: not set");
		}
		if (pos2 != null) {
			tooltip.add("Second position: " + pos2);
		} else {
			tooltip.add("Second position: not set");
		}
	}

	@EventBusSubscriber(modid = Reference.MODID)
	public static class EventHandler {

		@SubscribeEvent
		public static void onLeftClickBlockEvent(PlayerInteractEvent.LeftClickBlock event) {
			EntityPlayer player = event.getEntityPlayer();
			ItemStack stack = player.getHeldItem(event.getHand());

			if (stack.getItem() instanceof ItemStructureSelector) {
				if (player.isSneaking()) {
					ItemStructureSelector.setFirstPos(stack, player.getPosition(), player);
				} else {
					ItemStructureSelector.setFirstPos(stack, event.getPos(), player);
				}
				event.setCanceled(true);
			}
		}

		@SubscribeEvent
		public static void onLeftClickEmptyEvent(PlayerInteractEvent.LeftClickEmpty event) {
			EntityPlayer player = event.getEntityPlayer();
			ItemStack stack = player.getHeldItem(event.getHand());

			if (stack.getItem() instanceof ItemStructureSelector) {
				if (player.isSneaking()) {
					ItemStructureSelector.setFirstPos(stack, event.getPos(), player);
					CQRMain.NETWORK.sendToServer(new StructureSelectorPacket(event.getHand().ordinal()));
				}
			}
		}

	}

	public static void setFirstPos(ItemStack stack, BlockPos pos, EntityPlayer player) {
		stack.getCapability(CapabilityStructureSelectorProvider.STRUCTURE_SELECTOR, null).setPos1(pos);
		if (player.world.isRemote) {
			player.sendMessage(new TextComponentString("First position set to " + pos));
		}
	}

	public static void setSecondPos(ItemStack stack, BlockPos pos, EntityPlayer player) {
		stack.getCapability(CapabilityStructureSelectorProvider.STRUCTURE_SELECTOR, null).setPos2(pos);
		if (player.world.isRemote) {
			player.sendMessage(new TextComponentString("Second position set to " + pos));
		}
	}

}
