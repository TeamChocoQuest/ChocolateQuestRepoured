package com.teamcqr.chocolatequestrepoured.objects.items;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSoulBottle extends Item {

	public static final String ENTITY_IN_TAG = "EntityIn";

	public ItemSoulBottle() {
		this.setMaxStackSize(64);
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		if (!player.world.isRemote && !(entity instanceof MultiPartEntityPart)) {
			NBTTagCompound bottle = stack.getTagCompound();

			if (bottle == null) {
				bottle = new NBTTagCompound();
				stack.setTagCompound(bottle);
			}

			if (!bottle.hasKey(ENTITY_IN_TAG)) {
				NBTTagCompound entityTag = new NBTTagCompound();
				entity.writeToNBTOptional(entityTag);
				entityTag.removeTag("UUIDLeast");
				entityTag.removeTag("UUIDMost");
				entityTag.removeTag("Pos");
				NBTTagList passengers = entityTag.getTagList("Passengers", 10);
				for (NBTBase passenger : passengers) {
					((NBTTagCompound) passenger).removeTag("UUIDLeast");
					((NBTTagCompound) passenger).removeTag("UUIDMost");
					((NBTTagCompound) passenger).removeTag("Pos");
				}
				entity.setDead();
				for (Entity passenger : entity.getPassengers()) {
					passenger.setDead();
				}
				bottle.setTag(ENTITY_IN_TAG, entityTag);
				this.spawnAdditions(entity.world, entity.posX, entity.posY + entity.height * 0.5D, entity.posZ);
			}
		}

		return true;
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);

		if (stack.hasTagCompound()) {
			NBTTagCompound bottle = stack.getTagCompound();

			if (bottle.hasKey(ENTITY_IN_TAG)) {
				if (!worldIn.isRemote) {
					NBTTagCompound entityTag = (NBTTagCompound) bottle.getTag(ENTITY_IN_TAG);
					this.createEntityFromNBT(entityTag, worldIn, pos.getX() + hitX, pos.getY() + hitY, pos.getZ() + hitZ);

					if (!(player.isCreative() || player.isSpectator()) || (player.isCreative() && player.isSneaking())) {
						bottle.removeTag(ENTITY_IN_TAG);
					}
				}
				return EnumActionResult.SUCCESS;
			}
		}
		return EnumActionResult.FAIL;
	}

	private Entity createEntityFromNBT(NBTTagCompound tag, World worldIn, float x, float y, float z) {
		if (!worldIn.isRemote) {
			Entity entity = EntityList.createEntityFromNBT(tag, worldIn);
			entity.setPosition(x, y, z);
			worldIn.spawnEntity(entity);

			NBTTagList list = tag.getTagList("Passengers", 10);
			if (!list.hasNoTags()) {
				Entity rider = this.createEntityFromNBT(list.getCompoundTagAt(0), worldIn, x, y, z);
				rider.startRiding(entity);
			}

			this.spawnAdditions(entity.world, entity.posX, entity.posY + entity.height * 0.5D, entity.posZ);

			return entity;
		}
		return null;
	}

	private void spawnAdditions(World world, double x, double y, double z) {
		if (!world.isRemote) {
			((WorldServer) world).spawnParticle(EnumParticleTypes.CLOUD, x, y, z, 4, 0.25D, 0.25D, 0.25D, 0.0D);
			world.playSound(null, x, y, z, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 1.0F, 0.6F + itemRand.nextFloat() * 0.2F);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.soul_bottle.name"));
		} else {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}

		if (stack.hasTagCompound() && stack.getTagCompound().hasKey(ENTITY_IN_TAG)) {
			NBTTagCompound tag = (NBTTagCompound) stack.getTagCompound().getTag(ENTITY_IN_TAG);
			tooltip.add(TextFormatting.BLUE + I18n.format("description.contains.name") + " " + this.getEntityName(tag.getString("id")));
		} else {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.contains.name") + " " + I18n.format("description.empty.name"));
		}
	}

	private String getEntityName(String registryName) {
		EntityEntry entityEntry = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(registryName));
		if (entityEntry != null) {
			return I18n.format("entity." + ForgeRegistries.ENTITIES.getValue(new ResourceLocation(registryName)).getName() + ".name");
		}
		return "null";
	}

}
