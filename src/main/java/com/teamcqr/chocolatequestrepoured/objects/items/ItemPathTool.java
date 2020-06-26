package com.teamcqr.chocolatequestrepoured.objects.items;

import java.util.List;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.Constants;

public class ItemPathTool extends Item {

	public ItemPathTool() {
		this.setMaxStackSize(1);
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		/*
		 * shift Left click -> apply path points and overwrite the existing ones
		 * left click -> get existing path points
		 */
		if (!player.world.isRemote && entity instanceof AbstractEntityCQR) {
			if (player.isSneaking()) {
				BlockPos[] path = this.getPathPoints(stack);

				if (path.length > 0) {
					((AbstractEntityCQR) entity).setPath(path);
					((WorldServer) player.world).spawnParticle((EntityPlayerMP) player, EnumParticleTypes.VILLAGER_HAPPY, false, entity.posX, entity.posY + 0.5D, entity.posZ, 8, 0.5D, 0.5D, 0.5D, 0.1D);
				}
			} else {
				BlockPos[] oldPath = ((AbstractEntityCQR) entity).getGuardPathPoints();
				BlockPos home = ((AbstractEntityCQR) entity).getHomePositionCQR();

				if (oldPath.length > 0) {
					BlockPos[] path = new BlockPos[oldPath.length];

					for (int i = 0; i < path.length; i++) {
						path[i] = oldPath[i];
						path[i] = path[i].add(home);
					}

					this.setPathPoints(stack, path);
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
		if (!world.isRemote) {
			ItemStack stack = player.getHeldItem(hand);
			BlockPos position = pos.offset(side);

			if (player.isSneaking()) {
				this.clearPathPoints(stack);
				player.sendMessage(new TextComponentString("Cleared Path!"));
			} else {
				this.addPathPoint(stack, position);
				player.sendMessage(new TextComponentString("Added " + pos.toString() + " to the path!"));
			}
		}

		return EnumActionResult.SUCCESS;
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		BlockPos[] path = this.getPathPoints(stack);
		if (path.length > 0) {
			tooltip.add("Path points: ");
			for (int i = 0; i < path.length; i++) {
				tooltip.add("  - " + path[i].toString());
			}
		} else {
			tooltip.add("No Path stored");
		}
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);

		if (isSelected && entityIn instanceof EntityPlayer && entityIn.world.isRemote) {
			BlockPos[] path = this.getPathPoints(stack);
			// Draw path
			if (path.length > 0) {
				for (int i = 0; i < path.length; i++) {
					EnumParticleTypes particle = EnumParticleTypes.VILLAGER_HAPPY;
					if (i == 0) {
						particle = EnumParticleTypes.TOTEM;
					} else if (i == path.length - 1) {
						particle = EnumParticleTypes.FLAME;
					}
					Vec3d pos = new Vec3d(path[i]);
					pos = pos.add(0.5, 0.5, 0.5);
					if (i > 0) {
						Vec3d v = new Vec3d(path[i]).subtract(new Vec3d(path[i - 1]));
						double dist = v.length();
						v = v.normalize();
						// Draw connection lines
						for (double j = 0.25; j < dist; j += 0.5) {
							worldIn.spawnParticle(EnumParticleTypes.CRIT_MAGIC, true, pos.x - j * v.x, pos.y - j * v.y, pos.z - j * v.z, v.x * 0.1, v.y * 0.1, v.z * 0.1, 3);
						}
					}
					// Draw start point
					worldIn.spawnParticle(particle, true, pos.x, pos.y, pos.z, 0.0, 0.025, 0.0, 10);
				}
			}
		}
	}

	public void addPathPoint(ItemStack stack, BlockPos pos) {
		NBTTagCompound compound = stack.getTagCompound();
		if (compound == null) {
			compound = new NBTTagCompound();
			stack.setTagCompound(compound);
		}
		NBTTagList nbtTagList;
		if (!compound.hasKey("pathPoints", Constants.NBT.TAG_LIST)) {
			nbtTagList = new NBTTagList();
			compound.setTag("pathPoints", nbtTagList);
		} else {
			nbtTagList = compound.getTagList("pathPoints", Constants.NBT.TAG_COMPOUND);
		}
		nbtTagList.appendTag(NBTUtil.createPosTag(pos));
	}

	public void clearPathPoints(ItemStack stack) {
		NBTTagCompound compound = stack.getTagCompound();
		if (compound == null || !compound.hasKey("pathPoints", Constants.NBT.TAG_LIST)) {
			return;
		}
		compound.removeTag("pathPoints");
	}

	public void setPathPoints(ItemStack stack, BlockPos[] path) {
		NBTTagCompound compound = stack.getTagCompound();
		if (compound == null) {
			compound = new NBTTagCompound();
			stack.setTagCompound(compound);
		}
		NBTTagList nbtTagList;
		if (!compound.hasKey("pathPoints", Constants.NBT.TAG_LIST)) {
			nbtTagList = new NBTTagList();
			compound.setTag("pathPoints", new NBTTagList());
		} else {
			nbtTagList = compound.getTagList("pathPoints", Constants.NBT.TAG_COMPOUND);
		}
		for (int i = 0; i < path.length; i++) {
			nbtTagList.appendTag(NBTUtil.createPosTag(path[i]));
		}
	}

	public BlockPos[] getPathPoints(ItemStack stack) {
		NBTTagCompound compound = stack.getTagCompound();
		if (compound == null || !compound.hasKey("pathPoints", Constants.NBT.TAG_LIST)) {
			return new BlockPos[0];
		}
		NBTTagList nbtTagList = compound.getTagList("pathPoints", Constants.NBT.TAG_COMPOUND);
		BlockPos[] pathPoints = new BlockPos[nbtTagList.tagCount()];
		for (int i = 0; i < nbtTagList.tagCount(); i++) {
			pathPoints[i] = NBTUtil.getPosFromTag(nbtTagList.getCompoundTagAt(i));
		}
		return pathPoints;
	}

}
