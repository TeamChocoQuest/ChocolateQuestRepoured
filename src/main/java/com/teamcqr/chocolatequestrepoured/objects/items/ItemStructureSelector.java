package com.teamcqr.chocolatequestrepoured.objects.items;

import java.util.List;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.network.packets.toServer.StructureSelectorPacket;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityExporter;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ItemStructureSelector extends Item {

	public ItemStructureSelector() {
		this.setMaxStackSize(1);
	}

	@Override
	public boolean canDestroyBlockInCreative(World world, BlockPos pos, ItemStack stack, EntityPlayer player) {
		Block block = world.getBlockState(pos).getBlock();
		return block != ModBlocks.EXPORTER;
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);

		if (world.getTileEntity(pos) instanceof TileEntityExporter) {
			if (world.isRemote) {
				if (!this.hasFirstAndSecondPos(stack)) {
					player.sendMessage(new TextComponentString("Set both positions before using on a exporter"));
					return EnumActionResult.SUCCESS;
				}

				TileEntityExporter tileEntity = (TileEntityExporter) world.getTileEntity(pos);
				BlockPos pos1 = this.getFirstPos(stack);
				BlockPos pos2 = this.getSecondPos(stack);

				if (tileEntity.relativeMode) {
					pos1 = pos1.subtract(pos);
					pos2 = pos2.subtract(pos);
				}

				tileEntity.setValues(DungeonGenUtils.getMinPos(pos1, pos2), DungeonGenUtils.getMaxPos(pos1, pos2), tileEntity.structureName, tileEntity.partMode, tileEntity.relativeMode, tileEntity.ignoreEntities);
			}
		} else if (!world.isRemote) {
			if (player.isSneaking()) {
				BlockPos pos1 = new BlockPos(player);
				this.setSecondPos(stack, pos1);
				player.sendMessage(new TextComponentString("Second position set to " + pos1));
			} else {
				this.setSecondPos(stack, pos);
				player.sendMessage(new TextComponentString("Second position set to " + pos));
			}
		}

		return EnumActionResult.SUCCESS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);

		if (playerIn.isSneaking()) {
			BlockPos pos1 = new BlockPos(playerIn);
			this.setSecondPos(stack, pos1);
			playerIn.sendMessage(new TextComponentString("Second position set to " + pos1));
		}

		return new ActionResult<>(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		BlockPos pos1 = this.getFirstPos(stack);
		BlockPos pos2 = this.getSecondPos(stack);

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

	public void setFirstPos(ItemStack stack, BlockPos pos) {
		NBTTagCompound compound = stack.getTagCompound();
		if (compound == null) {
			compound = new NBTTagCompound();
			stack.setTagCompound(compound);
		}
		compound.setTag("pos1", NBTUtil.createPosTag(pos));
	}

	public void setSecondPos(ItemStack stack, BlockPos pos) {
		NBTTagCompound compound = stack.getTagCompound();
		if (compound == null) {
			compound = new NBTTagCompound();
			stack.setTagCompound(compound);
		}
		compound.setTag("pos2", NBTUtil.createPosTag(pos));
	}

	public BlockPos getFirstPos(ItemStack stack) {
		NBTTagCompound compound = stack.getTagCompound();
		if (compound == null || !compound.hasKey("pos1", Constants.NBT.TAG_COMPOUND)) {
			return null;
		}
		return NBTUtil.getPosFromTag(compound.getCompoundTag("pos1"));
	}

	public BlockPos getSecondPos(ItemStack stack) {
		NBTTagCompound compound = stack.getTagCompound();
		if (compound == null || !compound.hasKey("pos2", Constants.NBT.TAG_COMPOUND)) {
			return null;
		}
		return NBTUtil.getPosFromTag(compound.getCompoundTag("pos2"));
	}

	public boolean hasFirstPos(ItemStack stack) {
		return getFirstPos(stack) != null;
	}

	public boolean hasSecondPos(ItemStack stack) {
		return getSecondPos(stack) != null;
	}

	public boolean hasFirstAndSecondPos(ItemStack stack) {
		return hasFirstPos(stack) && hasSecondPos(stack);
	}

	@EventBusSubscriber(modid = Reference.MODID)
	public static class EventHandler {

		@SubscribeEvent
		public static void onLeftClickBlockEvent(PlayerInteractEvent.LeftClickBlock event) {
			EntityPlayer player = event.getEntityPlayer();
			ItemStack stack = player.getHeldItem(event.getHand());

			if (stack.getItem() instanceof ItemStructureSelector) {
				ItemStructureSelector structureSelector = (ItemStructureSelector) stack.getItem();

				if (player.isSneaking()) {
					BlockPos pos = new BlockPos(player);
					structureSelector.setFirstPos(stack, pos);
					player.sendMessage(new TextComponentString("First position set to " + pos));
				} else {
					BlockPos pos = event.getPos();
					structureSelector.setFirstPos(stack, pos);
					player.sendMessage(new TextComponentString("First position set to " + pos));
				}

				event.setCanceled(true);
			}
		}

		@SubscribeEvent
		public static void onLeftClickEmptyEvent(PlayerInteractEvent.LeftClickEmpty event) {
			EntityPlayer player = event.getEntityPlayer();
			ItemStack stack = player.getHeldItem(event.getHand());

			if (stack.getItem() instanceof ItemStructureSelector && player.isSneaking()) {
				CQRMain.NETWORK.sendToServer(new StructureSelectorPacket(event.getHand()));
			}
		}

	}

}
