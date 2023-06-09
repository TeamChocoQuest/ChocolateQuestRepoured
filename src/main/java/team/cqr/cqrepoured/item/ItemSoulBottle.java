package team.cqr.cqrepoured.item;

import java.util.List;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.entity.EntityList;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.nbt.INBT;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemSoulBottle extends ItemLore {

	public static final String ENTITY_IN_TAG = "EntityIn";

	public ItemSoulBottle(Properties prop) {
		super(prop.stacksTo(64));
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
		if (player.isCreative()) {
			if (!player.level.isClientSide && !(entity instanceof PartEntity)) {
				CompoundTag bottle = stack.getTag();

				if (bottle == null) {
					bottle = new CompoundTag();
					stack.setTag(bottle);
				}

				if (!bottle.contains(ENTITY_IN_TAG)) {
					CompoundTag entityTag = new CompoundTag();
					entity.save(entityTag);
					entityTag.remove("UUID");
					entityTag.remove("Pos");
					ListTag passengers = entityTag.getList("Passengers", 10);
					for (INBT passenger : passengers) {
						((CompoundTag) passenger).remove("UUID");
						((CompoundTag) passenger).remove("Pos");
					}
					entity.remove();
					for (Entity passenger : entity.getPassengers()) {
						passenger.remove();
					}
					bottle.put(ENTITY_IN_TAG, entityTag);
					this.spawnAdditions(entity.level, entity.getX(), entity.getY() + entity.getBbHeight() * 0.5D, entity.getZ());
				}
				
				stack.setTag(bottle);
			}
			return true;
		}
		return false;
	}

	
	@Override
	public InteractionResult useOn(UseOnContext pContext) {
		Player player = pContext.getPlayer();
		Level worldIn = pContext.getLevel();
		BlockPos pos = pContext.getClickedPos();
		InteractionHand hand = pContext.getHand();
		
		if (!player.isSpectator()) {
			ItemStack stack = player.getItemInHand(hand);

			if (stack.hasTag()) {
				CompoundTag bottle = stack.getTag();

				if (bottle.contains(ENTITY_IN_TAG)) {
					if (!worldIn.isClientSide) {
						CompoundTag entityTag = (CompoundTag) bottle.get(ENTITY_IN_TAG);
						this.createEntityFromNBT(entityTag, worldIn, pos.getX() + pContext.getClickedFace().getNormal().getX(), pos.getY() + pContext.getClickedFace().getNormal().getY(), pos.getZ() +  + pContext.getClickedFace().getNormal().getZ());

						if (player.isCrouching()) {
							bottle.remove(ENTITY_IN_TAG);
						}

						if (!player.isCreative()) {
							stack.shrink(1);
						}
					}
					return InteractionResult.SUCCESS;
				}
			}
		}
		return InteractionResult.FAIL;
	}

	public Entity createEntityFromNBT(CompoundTag tag, Level worldIn, double x, double y, double z) {
		if (!worldIn.isClientSide) {
			{
				// needed because in earlier versions the uuid and pos were not removed when using a soul bottle/mob to spawner on an
				// entity
				tag.remove("UUID");
				tag.remove("Pos");
				ListTag passengers = tag.getList("Passengers", 10);
				for (INBT passenger : passengers) {
					((CompoundTag) passenger).remove("UUID");
					((CompoundTag) passenger).remove("Pos");
				}
			}
			Entity entity = EntityList.createEntityFromNBT(tag, worldIn);
			entity.setPos(x, y, z);
			worldIn.addFreshEntity(entity);

			ListTag list = tag.getList("Passengers", 10);
			if (!list.isEmpty()) {
				Entity rider = this.createEntityFromNBT(list.getCompound(0), worldIn, x, y, z);
				rider.startRiding(entity);
			}

			this.spawnAdditions(entity.level, entity.getX(), entity.getY() + entity.getBbHeight() * 0.5D, entity.getZ());

			return entity;
		}
		return null;
	}

	private void spawnAdditions(Level world, double x, double y, double z) {
		if (!world.isClientSide) {
			for(int i = 0; i < 4; i++) {
				((ServerLevel) world).sendParticles(ParticleTypes.CLOUD, x, y, z, 5, 0.25D, 0.25D, 0.25D, 1.0);
			}
			world.playSound(null, x, y, z, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0F, 0.6F + random.nextFloat() * 0.2F);
		}
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		if (stack.hasTag()) {
			CompoundTag bottle = stack.getTag();

			if (bottle.contains(ENTITY_IN_TAG)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<TextComponent> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		if (stack.hasTag() && stack.getTag().contains(ENTITY_IN_TAG)) {
			CompoundTag tag = (CompoundTag) stack.getTag().get(ENTITY_IN_TAG);
			tooltip.add((new TranslationTextComponent("item.cqrepoured.tooltip.contains", new TranslationTextComponent(this.getEntityName(tag.getString("id"))))).withStyle(ChatFormatting.BLUE));
		} else {
			tooltip.add((new TranslationTextComponent("item.cqrepoured.tooltip.contains_nothing")).withStyle(ChatFormatting.BLUE));
		}
	}

	private String getEntityName(String registryName) {
		EntityType<?> entityEntry = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(registryName));
		if (entityEntry != null) {
			return "entity." + entityEntry.getRegistryName().toString().replace(':', '.');
		}
		return "null";
	}

}
