package team.cqr.cqrepoured.world.processor;

import java.util.Optional;

import javax.annotation.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.BannerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.Template.BlockInfo;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.block.BlockBossBlock;
import team.cqr.cqrepoured.block.BlockSpawner;
import team.cqr.cqrepoured.block.banner.BannerHelper;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.faction.Faction;
import team.cqr.cqrepoured.faction.FactionRegistry;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.init.CQRStructureProcessors;
import team.cqr.cqrepoured.tileentity.TileEntityBoss;
import team.cqr.cqrepoured.world.structure.generation.generation.DungeonPlacement;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.EntityEquipmentSlot;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.EntityLiving;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.EntityLivingBase;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.NBTBase;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.NBTTagCompound;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.NBTTagList;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparableSpawnerInfo;
import team.cqr.cqrepoured.world.structure.generation.inhabitants.DungeonInhabitant;
import team.cqr.cqrepoured.world.structure.generation.inhabitants.DungeonInhabitantManager;

public class ProcessorFactionAdjuster extends StructureProcessor {
	
	public static final Codec<ProcessorFactionAdjuster> CODEC = RecordCodecBuilder.create(builder ->
	builder.group(
			Codec.STRING.fieldOf("inhabitantIdent").forGetter(ProcessorFactionAdjuster::getInhabitantIdent)
	).apply(builder, ProcessorFactionAdjuster::new));
	
	protected final String inhabitantIdent;
	
	protected final Optional<Faction> factionOverride;
	protected final DungeonInhabitant inhabitant;
	
	public ProcessorFactionAdjuster(String inhabitantIdent) {
		super();
		
		this.inhabitantIdent = inhabitantIdent;
		this.inhabitant = DungeonInhabitantManager.instance().getInhabitant(this.inhabitantIdent);
		
		this.factionOverride = Optional.ofNullable(FactionRegistry.getServerInstance().getFactionInstanceOrDefault(this.inhabitant.getFactionOverride(), null));
	}

	@Override
	protected IStructureProcessorType<?> getType() {
		return CQRStructureProcessors.PROCESSOR_FACTION_ADJUSTER;
	}
	
	public String getInhabitantIdent() {
		return this.inhabitantIdent;
	}
	
	
	
	@Override
	public BlockInfo process(IWorldReader world, BlockPos templatePos, BlockPos structureCenter, Template.BlockInfo blockInfo, Template.BlockInfo relativisedBlockInfo, PlacementSettings placementSettings, @Nullable Template template) {
		int spawnX = 0;
		int spawnZ = 0;
		if( world instanceof IWorld) {
			IWorld iworld = (IWorld)world;
			spawnX = iworld.getLevelData().getXSpawn();
			spawnZ = iworld.getLevelData().getZSpawn();
		}
		final DungeonInhabitant inhabitantToUse = DungeonInhabitantManager.instance().getInhabitantByDistance(this.inhabitant, spawnX, spawnZ, blockInfo.pos.getX(), blockInfo.pos.getZ()); 
		
		Optional<BlockInfo> result = Optional.empty();
		if(blockInfo.state.getBlock() instanceof BlockSpawner) {
			result = this.handleSpawner(inhabitantToUse, world, blockInfo, placementSettings, template, templatePos, structureCenter, relativisedBlockInfo);
		}
		else if(blockInfo.state.getBlock() instanceof AbstractBannerBlock) {
			result = this.handleBanner(inhabitantToUse, world, blockInfo, placementSettings, template, templatePos, structureCenter, relativisedBlockInfo);
		}
		else if(blockInfo.state.getBlock() instanceof BlockBossBlock) {
			result = this.handleBoss(inhabitantToUse, world, blockInfo, placementSettings, template, templatePos, structureCenter, relativisedBlockInfo);
		}
		if(result.isPresent()) {
			return result.get();
		}
		
		return relativisedBlockInfo;
	}
	
	@Nullable
	private static CompoundNBT getBossTag(TileEntityBoss tileEntityBoss) {
		ItemStack stack = null;
		LazyOptional<IItemHandler> lCap = tileEntityBoss.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		if(lCap.isPresent()) {
			if(lCap.resolve().isPresent()) {
				stack = lCap.resolve().get().getStackInSlot(0);
			}
		}
		if(stack == null) {
			return null;
		}
		if (!stack.hasTag()) {
			return null;
		}
		if (!stack.getTag().contains("EntityIn", Constants.NBT.TAG_COMPOUND)) {
			return null;
		}
		return stack.getTag().getCompound("EntityIn");
	}
	
	@Nullable
	public static Entity createEntityFromTagForSpawner(World world, DungeonPlacement placement, BlockPos pos, CompoundNBT entityTag) {
		if (entityTag.isEmpty()) {
			return null;
		}

		entityTag.remove("UUIDLeast");
		entityTag.remove("UUIDMost");
		entityTag.remove("Pos");

		String id = entityTag.getString("id");

		try {
			if (id.equals(CQRMain.MODID + ":dummy")) {
				entityTag.putString("id", placement.getInhabitant().getEntityID().toString());
			}

			Entity entity = EntityList.createEntityFromNBT(entityTag, world);

			if (entity == null) {
				return null;
			}

			entity.setPos(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);

			if (entity instanceof MobEntity) {
				// fix attribute modifiers being EquipmentSlotType in the first tick instead of directly when creating the entity from nbt
				for (EquipmentSlotType slot : EquipmentSlotType.values()) {
					ItemStack stack = ((MobEntity) entity).getItemBySlot(slot);

					if (!stack.isEmpty()) {
						((LivingEntity) entity).getAttributes().addTransientAttributeModifiers(stack.getAttributeModifiers(slot));

						/*if (slot.getType() == EquipmentSlotType.Group.HAND) {
							((MobEntity) entity).handItems.set(slot.getIndex(), stack);
						} else {
							((MobEntity) entity).armorItems.set(slot.getIndex(), stack);
						}*/
						entity.setItemSlot(slot, stack);
					}
				}

				if (entity instanceof LivingEntity) {
					((MobEntity) entity).setPersistenceRequired();

					if (entity instanceof AbstractEntityCQR) {
						((AbstractEntityCQR) entity).onSpawnFromCQRSpawnerInDungeon(placement);
					}
				}
			}

			ListNBT passengers = entityTag.getList("Passengers", Constants.NBT.TAG_COMPOUND);
			for (INBT passengerNBT : passengers) {
				Entity passenger = ProcessorFactionAdjuster.createEntityFromTagForSpawner(world, placement, pos, (CompoundNBT) passengerNBT);
				passenger.startRiding(entity);
			}

			return entity;
		} finally {
			entityTag.putString("id", id);
		}
	}

	private Entity createEntityFromTag(World world, DungeonPlacement placement, BlockPos pos, CompoundNBT bossTag) {
		Entity entity = ProcessorFactionAdjuster.createEntityFromTagForSpawner(world, placement, pos, bossTag);
		if (entity instanceof AbstractEntityCQR) {
			((AbstractEntityCQR) entity).enableBossBar();
		}
		return entity;
	}

	private Entity createEntityFromBossID(World world, DungeonPlacement placement, BlockPos pos) {
		Entity entity = EntityList.createEntityByIDFromName(placement.getInhabitant().getBossID(), world);
		entity.setPos(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);

		if (entity instanceof MobEntity) {
			((MobEntity) entity).onInitialSpawn(world.getCurrentDifficultyAt(pos), null);
			((MobEntity) entity).setPersistenceRequired();

			if (entity instanceof AbstractEntityCQR) {
				((AbstractEntityCQR) entity).onSpawnFromCQRSpawnerInDungeon(placement);
				((AbstractEntityCQR) entity).enableBossBar();
			}
		}

		return entity;
	}

	private Entity createEntityFromEntityID(World world, DungeonPlacement placement, BlockPos pos) {
		Entity entity = EntityList.createEntityByIDFromName(placement.getInhabitant().getEntityID(), world);
		entity.setPos(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
		entity.setCustomName(new StringTextComponent("Temporary Boss"));

		if (entity instanceof MobEntity) {
			MobEntity living = (MobEntity) entity;

			living.onInitialSpawn(world.getCurrentDifficultyAt(pos), null);
			living.setPersistenceRequired();

			if (entity instanceof AbstractEntityCQR) {
				AbstractEntityCQR entityCQR = (AbstractEntityCQR) entity;
				entityCQR.onSpawnFromCQRSpawnerInDungeon(placement);
				entityCQR.enableBossBar();
				entityCQR.getAttribute(Attributes.MAX_HEALTH).setBaseValue(entityCQR.getBaseHealth() * 5.0D);
			} else {
				living.getAttribute(Attributes.MAX_HEALTH).setBaseValue(100.0D);
			}

			living.setHealth(living.getMaxHealth());
			living.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(new AttributeModifier("temp_boss_speed_buff", 0.35D, Operation.ADDITION));

			// Some gear
			living.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
			living.setItemSlot(EquipmentSlotType.OFFHAND, new ItemStack(CQRItems.CURSED_BONE.get()));

			living.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(CQRItems.HELMET_HEAVY_DIAMOND.get()));
			living.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(CQRItems.CHESTPLATE_HEAVY_DIAMOND.get()));
			living.setItemSlot(EquipmentSlotType.LEGS, new ItemStack(CQRItems.LEGGINGS_HEAVY_DIAMOND.get()));
			living.setItemSlot(EquipmentSlotType.FEET, new ItemStack(CQRItems.BOOTS_HEAVY_DIAMOND.get()));
		}

		return entity;
	}
	
	private Optional<BlockInfo> handleBoss(DungeonInhabitant inhabitantToUse, IWorldReader worldReader, BlockInfo blockInfo, PlacementSettings placementSettings, Template template, BlockPos templatePos, BlockPos structureCenter, BlockInfo relativisedBlockInfo) {
		if(!(worldReader instanceof World)) {
			return Optional.empty();
		}
		World world = (World)worldReader;
		TileEntity te = worldReader.getBlockEntity(blockInfo.pos);
		if(te != null && te instanceof TileEntityBoss) {
			TileEntityBoss teb = (TileEntityBoss)te;
			Entity entity;

			@Nullable
			CompoundNBT bossTag = getBossTag(teb);
			if (bossTag != null) {
				entity = this.createEntityFromTag(world, placement, pos);
			} else if (inhabitantToUse.getBossID() != null) {
				entity = this.createEntityFromBossID(world, placement, pos);
			} else {
				entity = this.createEntityFromEntityID(world, placement, pos);
			}

		}
		return Optional.empty();
	}

	private Optional<BlockInfo> handleBanner(DungeonInhabitant inhabitantToUse, IWorldReader worldReader, BlockInfo blockInfo, PlacementSettings placementSettings, Template template, BlockPos templatePos, BlockPos structureCenter, BlockInfo relativisedBlockInfo) {
		TileEntity te = worldReader.getBlockEntity(blockInfo.pos);
		if(te != null && te instanceof BannerTileEntity) {
			BannerTileEntity bte = (BannerTileEntity)te;
			if(BannerHelper.isCQBanner(bte)) {
				if (inhabitantToUse.getBanner() != null) {
					bte.fromItem(inhabitantToUse.getBanner().getBanner(), inhabitantToUse.getBanner().getMainColor());
					bte.setChanged();
					
					return Optional.of(new BlockInfo(blockInfo.pos, bte.getBlockState(), bte.serializeNBT()));
				}
			}
		}
		return Optional.empty();
	}

	private Optional<BlockInfo> handleSpawner(DungeonInhabitant inhabitantToUse, IWorldReader worldReader, BlockInfo blockInfo, PlacementSettings placementSettings, Template template, BlockPos templatePos, BlockPos structureCenter, BlockInfo relativisedBlockInfo) {
		return Optional.empty();
	}

}
