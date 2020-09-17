package com.teamcqr.chocolatequestrepoured.objects.items;

import java.util.List;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntitySummoningCircle;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntitySummoningCircle.ECircleTexture;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSummoningBone extends Item {

	public ItemSummoningBone() {
		this.setMaxDamage(3);
		this.setMaxStackSize(1);
		this.setNoRepair();
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 40;
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		if (!worldIn.isRemote && this.spawnEntity((EntityPlayer) entityLiving, worldIn, stack)) {
			stack.damageItem(1, entityLiving);
		}
		if (entityLiving instanceof EntityPlayerMP) {
			((EntityPlayerMP) entityLiving).getCooldownTracker().setCooldown(this, 20);
		}
		if (stack.getItemDamage() >= stack.getMaxDamage()) {
			return ItemStack.EMPTY;
		}
		return super.onItemUseFinish(stack, worldIn, entityLiving);
	}

	public boolean spawnEntity(EntityPlayer player, World worldIn, ItemStack item) {
		Vec3d start = player.getPositionEyes(1.0F);
		Vec3d end = start.add(player.getLookVec().scale(5.0D));
		RayTraceResult result = worldIn.rayTraceBlocks(start, end);

		if (result != null) {
			if (worldIn.isAirBlock(result.getBlockPos().offset(EnumFacing.UP, 1)) && worldIn.isAirBlock(new BlockPos(result.hitVec).offset(EnumFacing.UP, 2))) {
				// DONE: Spawn circle
				ResourceLocation resLoc = new ResourceLocation(Reference.MODID, "skeleton");
				// Get entity id
				if (item.hasTagCompound() && item.getTagCompound().hasKey("entity_to_summon")) {
					try {
						NBTTagCompound tag = item.getTagCompound();// .getCompoundTag("tag");
						resLoc = new ResourceLocation(tag.getString("entity_to_summon"));
						if (!EntityList.isRegistered(resLoc)) {
							resLoc = new ResourceLocation(Reference.MODID, "skeleton");
						}
					} catch (Exception ex) {
						resLoc = new ResourceLocation(Reference.MODID, "skeleton");
					}
				}
				EntitySummoningCircle circle = new EntitySummoningCircle(worldIn, resLoc, 1F, ECircleTexture.METEOR, null, player);
				circle.setSummon(resLoc);
				circle.setNoGravity(false);
				circle.setPosition(result.hitVec.x, result.hitVec.y + 1, result.hitVec.z);
				worldIn.spawnEntity(circle);
				return true;
			}
		}
		return false;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		playerIn.setActiveHand(handIn);
		return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
		super.onPlayerStoppedUsing(stack, worldIn, entityLiving, timeLeft);
		if (entityLiving instanceof EntityPlayer) {
			((EntityPlayer) entityLiving).getCooldownTracker().setCooldown(this, 20);
		}
		// stack.damageItem(1, entityLiving);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("entity_to_summon")) {
			try {
				NBTTagCompound tag = stack.getTagCompound();// .getCompoundTag("tag");
				tooltip.add(TextFormatting.BLUE + I18n.format("description.cursed_bone.name") + " " + this.getEntityName(tag.getString("entity_to_summon")));
			} catch (Exception ex) {
				tooltip.add(TextFormatting.BLUE + I18n.format("description.cursed_bone.name") + "missingNo");
			}
			return;
		}
		tooltip.add(TextFormatting.BLUE + I18n.format("description.cursed_bone.name") + " " + this.getEntityName(Reference.MODID + ":skeleton"));
	}

	private String getEntityName(String registryName) {
		EntityEntry entityEntry = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(registryName));
		if (entityEntry != null) {
			return I18n.format("entity." + ForgeRegistries.ENTITIES.getValue(new ResourceLocation(registryName)).getName() + ".name");
		}
		return "missingNO";
	}

}
