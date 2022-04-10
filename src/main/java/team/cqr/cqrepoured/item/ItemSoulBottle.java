package team.cqr.cqrepoured.item;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemSoulBottle extends ItemLore {

	public static final String ENTITY_IN_TAG = "EntityIn";

	public ItemSoulBottle(Properties prop) {
		super(prop.stacksTo(64));
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
		if (player.isCreative()) {
			if (!player.level.isClientSide && !(entity instanceof PartEntity)) {
				CompoundNBT bottle = stack.getTag();

				if (bottle == null) {
					bottle = new CompoundNBT();
					stack.setTag(bottle);
				}

				if (!bottle.contains(ENTITY_IN_TAG)) {
					CompoundNBT entityTag = new CompoundNBT();
					entity.save(entityTag);
					entityTag.remove("UUID");
					entityTag.remove("Pos");
					ListNBT passengers = entityTag.getList("Passengers", 10);
					for (INBT passenger : passengers) {
						((CompoundNBT) passenger).remove("UUID");
						((CompoundNBT) passenger).remove("Pos");
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
	public ActionResultType useOn(ItemUseContext pContext) {
		PlayerEntity player = pContext.getPlayer();
		World worldIn = pContext.getLevel();
		BlockPos pos = pContext.getClickedPos();
		Hand hand = pContext.getHand();
		
		if (!player.isSpectator()) {
			ItemStack stack = player.getItemInHand(hand);

			if (stack.hasTag()) {
				CompoundNBT bottle = stack.getTag();

				if (bottle.contains(ENTITY_IN_TAG)) {
					if (!worldIn.isClientSide) {
						CompoundNBT entityTag = (CompoundNBT) bottle.get(ENTITY_IN_TAG);
						this.createEntityFromNBT(entityTag, worldIn, pos.getX() + pContext.getClickedFace().getNormal().getX(), pos.getY() + pContext.getClickedFace().getNormal().getY(), pos.getZ() +  + pContext.getClickedFace().getNormal().getZ());

						if (player.isCrouching()) {
							bottle.remove(ENTITY_IN_TAG);
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
		if (!worldIn.isClientSide) {
			{
				// needed because in earlier versions the uuid and pos were not removed when using a soul bottle/mob to spawner on an
				// entity
				tag.remove("UUID");
				tag.remove("Pos");
				ListNBT passengers = tag.getList("Passengers", 10);
				for (INBT passenger : passengers) {
					((CompoundNBT) passenger).remove("UUID");
					((CompoundNBT) passenger).remove("Pos");
				}
			}
			Entity entity = EntityList.createEntityFromNBT(tag, worldIn);
			entity.setPos(x, y, z);
			worldIn.addFreshEntity(entity);

			ListNBT list = tag.getList("Passengers", 10);
			if (!list.isEmpty()) {
				Entity rider = this.createEntityFromNBT(list.getCompound(0), worldIn, x, y, z);
				rider.startRiding(entity);
			}

			this.spawnAdditions(entity.level, entity.getX(), entity.getY() + entity.getBbHeight() * 0.5D, entity.getZ());

			return entity;
		}
		return null;
	}

	private void spawnAdditions(World world, double x, double y, double z) {
		if (!world.isClientSide) {
			for(int i = 0; i < 4; i++) {
				((ServerWorld) world).addParticle(ParticleTypes.CLOUD, x, y, z, 0.25D, 0.25D, 0.25D);
			}
			world.playSound(null, x, y, z, SoundEvents.ITEM_PICKUP, SoundCategory.PLAYERS, 1.0F, 0.6F + random.nextFloat() * 0.2F);
		}
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		if (stack.hasTag()) {
			CompoundNBT bottle = stack.getTag();

			if (bottle.contains(ENTITY_IN_TAG)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		if (stack.hasTag() && stack.getTag().contains(ENTITY_IN_TAG)) {
			CompoundNBT tag = (CompoundNBT) stack.getTag().get(ENTITY_IN_TAG);
			tooltip.add((new TranslationTextComponent("item.cqrepoured.tooltip.contains", new TranslationTextComponent(this.getEntityName(tag.getString("id"))))).withStyle(TextFormatting.BLUE));
		} else {
			tooltip.add((new TranslationTextComponent("item.cqrepoured.tooltip.contains_nothing")).withStyle(TextFormatting.BLUE));
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
