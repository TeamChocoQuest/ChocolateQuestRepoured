package team.cqr.cqrepoured.item;

import java.util.List;

import javax.annotation.Nullable;

import net.java.games.input.Keyboard;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemSoulBottle extends Item {

	public static final String ENTITY_IN_TAG = "EntityIn";

	public ItemSoulBottle() {
		this.setMaxStackSize(64);
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
		if (player.isCreative()) {
			if (!player.world.isRemote && !(entity instanceof PartEntity)) {
				CompoundNBT bottle = stack.getTagCompound();

				if (bottle == null) {
					bottle = new CompoundNBT();
					stack.setTagCompound(bottle);
				}

				if (!bottle.hasKey(ENTITY_IN_TAG)) {
					CompoundNBT entityTag = new CompoundNBT();
					entity.writeToNBTOptional(entityTag);
					entityTag.removeTag("UUIDLeast");
					entityTag.removeTag("UUIDMost");
					entityTag.removeTag("Pos");
					ListNBT passengers = entityTag.getTagList("Passengers", 10);
					for (INBT passenger : passengers) {
						((CompoundNBT) passenger).removeTag("UUIDLeast");
						((CompoundNBT) passenger).removeTag("UUIDMost");
						((CompoundNBT) passenger).removeTag("Pos");
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
		return false;
	}

	@Override
	public ActionResultType onItemUse(PlayerEntity player, World worldIn, BlockPos pos, Hand hand, Direction facing, float hitX, float hitY, float hitZ) {
		if (!player.isSpectator()) {
			ItemStack stack = player.getHeldItem(hand);

			if (stack.hasTagCompound()) {
				CompoundNBT bottle = stack.getTagCompound();

				if (bottle.hasKey(ENTITY_IN_TAG)) {
					if (!worldIn.isRemote) {
						CompoundNBT entityTag = (CompoundNBT) bottle.getTag(ENTITY_IN_TAG);
						this.createEntityFromNBT(entityTag, worldIn, pos.getX() + hitX, pos.getY() + hitY, pos.getZ() + hitZ);

						if (player.isSneaking()) {
							bottle.removeTag(ENTITY_IN_TAG);
						}

						if (!player.isCreative()) {
							stack.shrink(1);
						}
					}
					return ActionResultType.SUCCESS;
				}
			}
		}
		return ActionResultType.FAIL;
	}

	public Entity createEntityFromNBT(CompoundNBT tag, World worldIn, double x, double y, double z) {
		if (!worldIn.isRemote) {
			{
				// needed because in earlier versions the uuid and pos were not removed when using a soul bottle/mob to spawner on an
				// entity
				tag.removeTag("UUIDLeast");
				tag.removeTag("UUIDMost");
				tag.removeTag("Pos");
				ListNBT passengers = tag.getTagList("Passengers", 10);
				for (INBT passenger : passengers) {
					((CompoundNBT) passenger).removeTag("UUIDLeast");
					((CompoundNBT) passenger).removeTag("UUIDMost");
					((CompoundNBT) passenger).removeTag("Pos");
				}
			}
			Entity entity = EntityList.createEntityFromNBT(tag, worldIn);
			entity.setPosition(x, y, z);
			worldIn.spawnEntity(entity);

			ListNBT list = tag.getTagList("Passengers", 10);
			if (!list.isEmpty()) {
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
			((ServerWorld) world).spawnParticle(ParticleTypes.CLOUD, x, y, z, 4, 0.25D, 0.25D, 0.25D, 0.0D);
			world.playSound(null, x, y, z, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 1.0F, 0.6F + itemRand.nextFloat() * 0.2F);
		}
	}

	@Override
	public boolean hasEffect(ItemStack stack) {
		if (stack.hasTagCompound()) {
			CompoundNBT bottle = stack.getTagCompound();

			if (bottle.hasKey(ENTITY_IN_TAG)) {
				return true;
			}
		}
		return false;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.soul_bottle.name"));
		} else {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}

		if (stack.hasTagCompound() && stack.getTagCompound().hasKey(ENTITY_IN_TAG)) {
			CompoundNBT tag = (CompoundNBT) stack.getTagCompound().getTag(ENTITY_IN_TAG);
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
