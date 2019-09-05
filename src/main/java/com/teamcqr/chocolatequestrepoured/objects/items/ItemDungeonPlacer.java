package com.teamcqr.chocolatequestrepoured.objects.items;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.capability.CapabilityDungeonPlacerProvider;
import com.teamcqr.chocolatequestrepoured.capability.ICapabilityDungeonPlacer;
import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.structuregen.DungeonBase;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemDungeonPlacer extends Item {

	public static final int HIGHEST_ICON_NUMBER = 16;

	public ItemDungeonPlacer() {
		setMaxStackSize(1);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (this.isInCreativeTab(tab)) {
			for (DungeonBase dungeon : CQRMain.dungeonRegistry.dungeonList) {
				int iconID = dungeon.getIconID();
				ItemStack stack = new ItemStack(ModItems.DUNGEON_PLACER, 1, iconID <= HIGHEST_ICON_NUMBER ? iconID : 0);
				stack.getCapability(CapabilityDungeonPlacerProvider.DUNGEON_PLACER_CAPABILITY, null).setDungeon(dungeon);
				items.add(stack);
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
			ICapabilityDungeonPlacer icapability = playerIn.getHeldItem(handIn).getCapability(CapabilityDungeonPlacerProvider.DUNGEON_PLACER_CAPABILITY, null);
			playerIn.getCooldownTracker().setCooldown(playerIn.getHeldItemMainhand().getItem(), 30);
			playerIn.getCooldownTracker().setCooldown(this, 30);
			icapability.getDungeon().generate(playerIn.getPosition(), worldIn);
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
		if (entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entityLiving;
			if (!worldIn.isRemote) {
				player.getCooldownTracker().setCooldown(player.getHeldItemMainhand().getItem(), 30);
				player.getCooldownTracker().setCooldown(this, 30);
			}
		}
	}
	
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return new CapabilityDungeonPlacerProvider();
	}

}
