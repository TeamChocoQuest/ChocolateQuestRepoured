package com.teamcqr.chocolatequestrepoured.objects.items;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.capability.dungeonplacer.CapabilityDungeonPlacerProvider;
import com.teamcqr.chocolatequestrepoured.structuregen.DungeonBase;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemDungeonPlacer extends Item {

	public static final int HIGHEST_ICON_NUMBER = 16;
	private int iconID;

	public ItemDungeonPlacer(int iconID) {
		setMaxStackSize(1);
		this.iconID = iconID;
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (this.isInCreativeTab(tab)) {
			for (DungeonBase dungeon : CQRMain.dungeonRegistry.dungeonList) {
				int iconID = dungeon.getIconID() <= HIGHEST_ICON_NUMBER ? dungeon.getIconID() : 0;
				if (this.iconID == iconID) {
					ItemStack stack = new ItemStack(this);
					stack.getCapability(CapabilityDungeonPlacerProvider.DUNGEON_PLACER_CAPABILITY, null).setDungeon(dungeon);
					items.add(stack);
				}
			}
		}
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 1;
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		DungeonBase dungeon = stack.getCapability(CapabilityDungeonPlacerProvider.DUNGEON_PLACER_CAPABILITY, null).getDungeon();
		if (dungeon != null) {
			return "Dungeon Placer - " + dungeon.getDungeonName();
		}
		return "Dungeon Placer";
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if (!worldIn.isRemote) {
			Vec3d pos = playerIn.getPositionVector();
			double eye = playerIn.getEyeHeight();
			Vec3d look = playerIn.getLookVec();
			RayTraceResult result = worldIn.rayTraceBlocks(pos.addVector(0.0D, eye, 0.0D),
					pos.addVector(64.0D * look.x, eye + 64.0D * look.y, 64.0D * look.z));
			if (result != null) {
				ItemStack stack = playerIn.getHeldItem(handIn);
				DungeonBase dungeon = stack.getCapability(CapabilityDungeonPlacerProvider.DUNGEON_PLACER_CAPABILITY, null).getDungeon();
				if (dungeon != null) {
					dungeon.generate(result.getBlockPos(), worldIn);

					playerIn.getCooldownTracker().setCooldown(stack.getItem(), 30);
				}
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return new CapabilityDungeonPlacerProvider();
	}

}
