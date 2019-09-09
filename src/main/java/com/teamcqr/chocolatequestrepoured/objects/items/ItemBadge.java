package com.teamcqr.chocolatequestrepoured.objects.items;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.capability.itemhandler.CapabilityItemStackHandlerProvider;
import com.teamcqr.chocolatequestrepoured.objects.entity.EntityEquipmentExtraSlot;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ItemBadge extends Item {

	public ItemBadge() {
		this.setMaxStackSize(1);
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		if (player.isCreative()) {
			if (!player.world.isRemote) {
				if (entity instanceof AbstractEntityCQR) {
					((AbstractEntityCQR) entity).setItemStackToExtraSlot(EntityEquipmentExtraSlot.BadgeSlot, stack.copy());
					((WorldServer) player.world).spawnParticle((EntityPlayerMP) player, EnumParticleTypes.SPELL_WITCH, false, entity.posX, entity.posY + 0.5D, entity.posZ, 8, 0.5D, 0.5D, 0.5D, 0.1D);
				} else {
					IItemHandler capability = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
					NBTTagList itemList = new NBTTagList();
					for (int i = 0; i < capability.getSlots(); i++) {
						ItemStack stack1 = capability.getStackInSlot(i);
						NBTTagCompound slotTag = new NBTTagCompound();
						slotTag.setInteger("Index", 0);
						stack1.writeToNBT(slotTag);
						itemList.appendTag(slotTag);
					}
					if (!itemList.hasNoTags()) {
						entity.getEntityData().setTag("Items", itemList);
						((WorldServer) player.world).spawnParticle((EntityPlayerMP) player, EnumParticleTypes.SPELL_WITCH, false, entity.posX, entity.posY + 0.5D, entity.posZ, 8, 0.5D, 0.5D, 0.5D, 0.1D);
					}
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if (playerIn.isCreative() && !worldIn.isRemote) {
			playerIn.openGui(CQRMain.INSTANCE, Reference.BADGE_GUI_ID, worldIn, handIn.ordinal(), 0, 0);
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.badge.name"));
		} else {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}

		IItemHandler capability = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		for (int i = 0; i < capability.getSlots(); i++) {
			ItemStack stack1 = capability.getStackInSlot(i);
			if (!stack1.isEmpty()) {
				tooltip.add(stack1.getDisplayName() + " x" + stack1.getCount());
			}
		}
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return new CapabilityItemStackHandlerProvider(9);
	}

}
