package team.cqr.cqrepoured.structuregen.structurefile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import javax.annotation.Nullable;

import org.apache.commons.io.FileUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.objects.banners.BannerHelper;
import team.cqr.cqrepoured.objects.blocks.BlockExporterChest;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.structuregen.generation.DungeonPlacement;
import team.cqr.cqrepoured.structuregen.generation.GeneratableDungeon;
import team.cqr.cqrepoured.structuregen.generation.part.BlockDungeonPart;
import team.cqr.cqrepoured.structuregen.generation.part.EntityDungeonPart;
import team.cqr.cqrepoured.structuregen.generation.part.MultiBlockDungeonPart;
import team.cqr.cqrepoured.structuregen.generation.preparable.PreparableBannerInfo;
import team.cqr.cqrepoured.structuregen.generation.preparable.PreparableBlockInfo;
import team.cqr.cqrepoured.structuregen.generation.preparable.PreparableBossInfo;
import team.cqr.cqrepoured.structuregen.generation.preparable.PreparableEmptyInfo;
import team.cqr.cqrepoured.structuregen.generation.preparable.PreparableEntityInfo;
import team.cqr.cqrepoured.structuregen.generation.preparable.PreparableForceFieldNexusInfo;
import team.cqr.cqrepoured.structuregen.generation.preparable.PreparableLootChestInfo;
import team.cqr.cqrepoured.structuregen.generation.preparable.PreparableMapInfo;
import team.cqr.cqrepoured.structuregen.generation.preparable.PreparablePosInfo;
import team.cqr.cqrepoured.structuregen.generation.preparable.PreparableSpawnerInfo;
import team.cqr.cqrepoured.structuregen.inhabitants.DungeonInhabitant;
import team.cqr.cqrepoured.tileentity.TileEntityBoss;
import team.cqr.cqrepoured.tileentity.TileEntityMap;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.NBTCollectors;
import team.cqr.cqrepoured.util.NBTHelper;

public class CQStructure {

	private static final Comparator<PreparablePosInfo> DEFAULT_COMPARATOR = Comparator.comparingInt(PreparablePosInfo::getX)
			.thenComparingInt(PreparablePosInfo::getY).thenComparingInt(PreparablePosInfo::getZ);
	private static final Map<File, CQStructure> CACHED_STRUCTURES = new HashMap<>();
	public static final String CQR_FILE_VERSION = "1.2.0";
	private static final Set<Block> SPECIAL_BLOCKS = new HashSet<>();
	private static final Set<ResourceLocation> SPECIAL_ENTITIES = new HashSet<>();
	private final List<PreparablePosInfo> blockInfoList = new ArrayList<>();
	private final List<PreparableEntityInfo> entityInfoList = new ArrayList<>();
	private final List<BlockPos> unprotectedBlockList = new ArrayList<>();
	private BlockPos size = BlockPos.ORIGIN;
	private String author = "";

	private CQStructure() {

	}

	public static void cacheFiles() {
		CACHED_STRUCTURES.clear();

		if (!CQRConfig.advanced.cacheStructureFiles) {
			return;
		}

		List<File> fileList = new ArrayList<>(FileUtils.listFiles(CQRMain.CQ_STRUCTURE_FILES_FOLDER, new String[] { "nbt" }, true));
		fileList.sort((file1, file2) -> {
			if (file1.length() > file2.length()) {
				return -1;
			}
			if (file1.length() < file2.length()) {
				return 1;
			}
			return 0;
		});

		long fileSizeSum = 0;
		for (int i = 0; i < fileList.size() && i < CQRConfig.advanced.cachedStructureFilesMaxAmount; i++) {
			File file = fileList.get(i);
			long fileSize = file.length();
			if (fileSizeSum + fileSize < CQRConfig.advanced.cachedStructureFilesMaxSize * 1000) {
				CACHED_STRUCTURES.put(file, createFromFile(file));
				fileSizeSum += fileSize;
			}
		}
	}

	public static void clearCache() {
		CACHED_STRUCTURES.clear();
	}

	public static CQStructure createFromFile(File file) {
		if (CACHED_STRUCTURES.containsKey(file)) {
			return CACHED_STRUCTURES.get(file);
		}
		CQStructure structure = new CQStructure();
		structure.readFromFile(file);
		return structure;
	}

	public static CQStructure createFromWorld(World world, BlockPos startPos, BlockPos endPos, boolean ignoreBasicEntities,
			Collection<BlockPos> unprotectedBlocks, String author) {
		CQStructure structure = new CQStructure();
		structure.author = author;
		structure.takeBlocksAndEntitiesFromWorld(world, startPos, endPos, ignoreBasicEntities, unprotectedBlocks);
		return structure;
	}

	public boolean isEmpty() {
		return this.blockInfoList.isEmpty() && this.entityInfoList.isEmpty();
	}

	public boolean writeToFile(File file) {
		try {
			if (file.isDirectory()) {
				throw new FileNotFoundException();
			}
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			try (OutputStream outputStream = new FileOutputStream(file)) {
				CompressedStreamTools.writeCompressed(this.writeToNBT(), outputStream);
			}
			return true;
		} catch (Exception e) {
			CQRMain.logger.error(String.format("Failed to write structure to file %s", file.getName()), e);
		}
		return false;
	}

	private boolean readFromFile(File file) {
		try {
			try (InputStream inputStream = new FileInputStream(file)) {
				this.readFromNBT(CompressedStreamTools.readCompressed(inputStream));
			}
			return true;
		} catch (Exception e) {
			CQRMain.logger.error(String.format("Failed to read structure from file %s", file.getName()), e);
		}
		return false;
	}

	private NBTTagCompound writeToNBT() {
		NBTTagCompound compound = new NBTTagCompound();

		compound.setString("cqr_file_version", CQStructure.CQR_FILE_VERSION);
		compound.setString("author", this.author);
		compound.setTag("size", NBTUtil.createPosTag(this.size));

		BlockStatePalette palette = new BlockStatePalette();
		NBTTagList compoundList = new NBTTagList();

		// Save normal blocks
		ByteBuf buf = Unpooled.buffer(this.blockInfoList.size() * 2);
		this.blockInfoList.forEach(preparable -> PreparablePosInfo.Registry.write(preparable, buf, palette, compoundList));
		compound.setByteArray("blockInfoList", Arrays.copyOf(buf.array(), buf.writerIndex()));

		// Save entities
		compound.setTag("entityInfoList", this.entityInfoList.stream().map(PreparableEntityInfo::getEntityData).collect(NBTCollectors.toList()));

		// Save block states
		compound.setTag("palette", palette.writeToNBT());

		// Save compound tags
		compound.setTag("compoundTagList", compoundList);

		compound.setIntArray("unprotectedBlockList",
				this.unprotectedBlockList.stream().flatMapToInt(pos -> IntStream.of(pos.getX(), pos.getY(), pos.getZ())).toArray());

		return compound;
	}

	private void readFromNBT(NBTTagCompound compound) {
		String cqrFileVersion = compound.getString("cqr_file_version");
		if (!cqrFileVersion.equals(CQR_FILE_VERSION)) {
			if (cqrFileVersion.equals("1.1.0")) {
				CQRMain.logger.warn("Structure nbt is deprecated! Expected {} but got {}.", CQR_FILE_VERSION, cqrFileVersion);
				this.readFromDeprecatedNBT(compound);
				return;
			} else {
				throw new IllegalArgumentException(String.format("Structure nbt is too old! Expected %s but got %s.", CQR_FILE_VERSION, cqrFileVersion));
			}
		}

		this.author = compound.getString("author");
		this.size = NBTUtil.getPosFromTag(compound.getCompoundTag("size"));

		this.blockInfoList.clear();
		this.entityInfoList.clear();

		BlockStatePalette blockStatePalette = new BlockStatePalette();

		// Load compound tags
		NBTTagList compoundTagList = compound.getTagList("compoundTagList", Constants.NBT.TAG_COMPOUND);

		// Load block states
		int blockStateIndex = 0;
		for (NBTBase nbt : compound.getTagList("palette", Constants.NBT.TAG_COMPOUND)) {
			blockStatePalette.addMapping(NBTUtil.readBlockState((NBTTagCompound) nbt), blockStateIndex++);
		}

		// Load normal blocks
		ByteBuf buf = Unpooled.wrappedBuffer(compound.getByteArray("blockInfoList"));
		for (int x = 0; x < this.size.getX(); x++) {
			for (int y = 0; y < this.size.getY(); y++) {
				for (int z = 0; z < this.size.getZ(); z++) {
					this.blockInfoList.add(PreparablePosInfo.Registry.read(x, y, z, buf, blockStatePalette, compoundTagList));
				}
			}
		}

		// Load special blocks
		if (compound.hasKey("specialBlockInfoList", Constants.NBT.TAG_BYTE_ARRAY)) {
			buf = Unpooled.wrappedBuffer(compound.getByteArray("specialBlockInfoList"));
			int specialBlockCount = buf.readInt();
			for (int i = 0; i < specialBlockCount; i++) {
				int x = buf.readShort();
				int y = buf.readShort();
				int z = buf.readShort();
				int index = ((x * this.size.getY()) + y) * this.size.getZ() + z;
				this.blockInfoList.set(index, PreparablePosInfo.Registry.read(x, y, z, buf, blockStatePalette, compoundTagList));
			}
		}

		// Load entities
		for (NBTBase nbt : compound.getTagList("entityInfoList", Constants.NBT.TAG_COMPOUND)) {
			this.entityInfoList.add(new PreparableEntityInfo((NBTTagCompound) nbt));
		}

		this.unprotectedBlockList.clear();
		int[] intArray = compound.getIntArray("unprotectedBlockList");
		IntStream.range(0, intArray.length / 3).mapToObj(i -> new BlockPos(intArray[i * 3], intArray[i * 3 + 1], intArray[i * 3 + 2]))
				.forEach(this.unprotectedBlockList::add);
	}

	private void takeBlocksAndEntitiesFromWorld(World world, BlockPos startPos, BlockPos endPos, boolean ignoreBasicEntities,
			Collection<BlockPos> unprotectedBlocks) {
		BlockPos pos1 = DungeonGenUtils.getValidMinPos(startPos, endPos);
		BlockPos pos2 = DungeonGenUtils.getValidMaxPos(startPos, endPos);

		this.size = pos2.subtract(pos1).add(1, 1, 1);

		this.takeBlocksFromWorld(world, pos1, pos2);
		this.takeEntitiesFromWorld(world, pos1, pos2, ignoreBasicEntities);

		this.unprotectedBlockList.clear();
		for (BlockPos pos : unprotectedBlocks) {
			if (pos.getX() < pos1.getX() && pos.getY() < pos1.getY() && pos.getZ() < pos1.getZ()) {
				continue;
			}
			if (pos.getX() > pos2.getX() && pos.getY() > pos2.getY() && pos.getZ() > pos2.getZ()) {
				continue;
			}
			this.unprotectedBlockList.add(pos.subtract(pos1));
		}
	}

	private void takeBlocksFromWorld(World world, BlockPos pos1, BlockPos pos2) {
		this.blockInfoList.clear();

		for (BlockPos.MutableBlockPos mutablePos : BlockPos.getAllInBoxMutable(pos1, pos2)) {
			IBlockState state = world.getBlockState(mutablePos);
			Block block = state.getBlock();

			if (state.getBlockHardness(world, mutablePos) < 0.0F) {
				CQRMain.logger.warn("Exporting unbreakable block: {} from {}", state, mutablePos);
			}

			BlockPos pos = mutablePos.subtract(pos1);
			TileEntity tileEntity = world.getTileEntity(mutablePos);

			if (block == Blocks.STRUCTURE_VOID || block == CQRBlocks.NULL_BLOCK) {
				this.blockInfoList.add(new PreparableEmptyInfo(pos));
			} else if ((block == Blocks.STANDING_BANNER || block == Blocks.WALL_BANNER)
					&& tileEntity instanceof TileEntityBanner
					&& BannerHelper.isCQBanner((TileEntityBanner) tileEntity)) {
				this.blockInfoList.add(new PreparableBannerInfo(pos, state, this.writeTileEntityToNBT(tileEntity)));
			} else if (block == CQRBlocks.SPAWNER) {
				NBTTagCompound compound = tileEntity.writeToNBT(new NBTTagCompound());
				compound.removeTag("x");
				compound.removeTag("y");
				compound.removeTag("z");
				NBTTagList items = compound.getCompoundTag("inventory").getTagList("Items", Constants.NBT.TAG_COMPOUND);
				for (int i = 0; i < items.tagCount(); i++) {
					NBTTagCompound itemTag = items.getCompoundTagAt(i);
					NBTTagCompound itemTagCompound = itemTag.getCompoundTag("tag");
					NBTTagCompound entityTag = itemTagCompound.getCompoundTag("EntityIn");
					Entity entity = this.createEntityForExporting(entityTag, world, mutablePos);
					if (entity != null) {
						NBTTagCompound newEntityTag = new NBTTagCompound();
						entity.writeToNBTAtomically(newEntityTag);
						itemTagCompound.setTag("EntityIn", newEntityTag);
					}
				}
				this.blockInfoList.add(new PreparableSpawnerInfo(pos, compound));
			} else if (block instanceof BlockExporterChest) {
				this.blockInfoList
						.add(new PreparableLootChestInfo(pos, ((BlockExporterChest) block).getLootTable(world, mutablePos), state.getValue(BlockChest.FACING)));
			} else if (block == CQRBlocks.FORCE_FIELD_NEXUS) {
				this.blockInfoList.add(new PreparableForceFieldNexusInfo(pos));
			} else if (block == CQRBlocks.BOSS_BLOCK && tileEntity instanceof TileEntityBoss) {
				this.blockInfoList.add(new PreparableBossInfo(pos, (TileEntityBoss) tileEntity));
			} else if (block == CQRBlocks.MAP_PLACEHOLDER) {
				this.blockInfoList.add(new PreparableMapInfo(pos, state.getValue(BlockHorizontal.FACING), (TileEntityMap) tileEntity));
			} else {
				this.blockInfoList.add(new PreparableBlockInfo(pos, state, this.writeTileEntityToNBT(tileEntity)));
			}
		}

		this.blockInfoList.sort(DEFAULT_COMPARATOR);
	}

	private Entity createEntityForExporting(NBTTagCompound entityTag, World world, BlockPos pos) {
		Entity entity = EntityList.createEntityFromNBT(entityTag, world);
		if (entity != null) {
			entity.setPosition(pos.getX(), pos.getY(), pos.getZ());
			if (entity instanceof AbstractEntityCQR) {
				((AbstractEntityCQR) entity).onExportFromWorld();
			}
			NBTTagList passengers = entityTag.getTagList("Passengers", Constants.NBT.TAG_COMPOUND);
			for (NBTBase passengerNBT : passengers) {
				Entity passenger = this.createEntityForExporting((NBTTagCompound) passengerNBT, world, pos);
				passenger.startRiding(entity);
			}
		}
		return entity;
	}

	private NBTTagCompound writeTileEntityToNBT(@Nullable TileEntity tileEntity) {
		if (tileEntity == null) {
			return null;
		}
		NBTTagCompound compound = tileEntity.writeToNBT(new NBTTagCompound());
		compound.removeTag("x");
		compound.removeTag("y");
		compound.removeTag("z");
		return compound;
	}

	private void takeEntitiesFromWorld(World world, BlockPos pos1, BlockPos pos2, boolean ignoreBasicEntities) {
		this.entityInfoList.clear();

		for (Entity entity : world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos1, pos2.add(1, 1, 1)), input -> !(input instanceof EntityPlayer))) {
			if (!ignoreBasicEntities || SPECIAL_ENTITIES.contains(EntityList.getKey(entity))) {
				this.entityInfoList.add(new PreparableEntityInfo(pos1, entity));
			}
		}
	}

	public List<PreparablePosInfo> getBlockInfoList() {
		return Collections.unmodifiableList(this.blockInfoList);
	}

	public List<PreparableEntityInfo> getEntityInfoList() {
		return Collections.unmodifiableList(this.entityInfoList);
	}

	public List<BlockPos> getUnprotectedBlockList() {
		return Collections.unmodifiableList(this.unprotectedBlockList);
	}

	public BlockPos getSize() {
		return this.size;
	}

	public String getAuthor() {
		return this.author;
	}

	public void addAll(GeneratableDungeon.Builder builder, BlockPos pos, Offset offset) {
		this.addAll(builder, builder.getPlacement(offset.apply(pos, this, Mirror.NONE, Rotation.NONE)));
	}

	public void addAll(GeneratableDungeon.Builder builder, BlockPos pos, Offset offset, Mirror mirror, Rotation rotation) {
		this.addAll(builder, builder.getPlacement(offset.apply(pos, this, mirror, rotation), mirror, rotation));
	}

	public void addAll(GeneratableDungeon.Builder builder, BlockPos pos, Offset offset, DungeonInhabitant inhabitant) {
		this.addAll(builder, builder.getPlacement(offset.apply(pos, this, Mirror.NONE, Rotation.NONE), inhabitant));
	}

	public void addAll(GeneratableDungeon.Builder builder, BlockPos pos, Offset offset, Mirror mirror, Rotation rotation, DungeonInhabitant inhabitant) {
		this.addAll(builder, builder.getPlacement(offset.apply(pos, this, mirror, rotation), mirror, rotation, inhabitant));
	}

	public void addAll(GeneratableDungeon.Builder builder, DungeonPlacement placement) {
		builder.add(new BlockDungeonPart.Builder().addAll(this.blockInfoList), placement);
		builder.add(new EntityDungeonPart.Builder().addAll(this.entityInfoList), placement);
	}

	public static void updateSpecialBlocks() {
		CQStructure.SPECIAL_BLOCKS.clear();
		for (String s : CQRConfig.advanced.specialBlocks) {
			Block b = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(s));
			if (b != null) {
				CQStructure.SPECIAL_BLOCKS.add(b);
			}
		}
	}

	public static void updateSpecialEntities() {
		CQStructure.SPECIAL_ENTITIES.clear();
		for (String s : CQRConfig.advanced.specialEntities) {
			CQStructure.SPECIAL_ENTITIES.add(new ResourceLocation(s));
		}
	}

	@Deprecated
	public static void checkAndUpdateStructureFiles() {
		if (!CQRConfig.advanced.checkAndUpdateDeprecatedStructureFiles) {
			return;
		}
		Collection<File> files = FileUtils.listFiles(CQRMain.CQ_STRUCTURE_FILES_FOLDER, new String[] { "nbt" }, true);
		CQRMain.logger.info("Checking {} structure files", files.size());
		long lastTimeLogged = System.currentTimeMillis();
		long checkedFiles = 0;
		int updatedFiles = 0;
		for (File file : files) {
			if (!NBTHelper.getVersionOfStructureFile(file).equals(CQStructure.CQR_FILE_VERSION)) {
				CQStructure structure = CQStructure.createFromFile(file);
				if (!structure.isEmpty()) {
					structure.writeToFile(file);
					updatedFiles++;
				}
			}
			checkedFiles++;
			if (System.currentTimeMillis() - lastTimeLogged > 2000) {
				lastTimeLogged = System.currentTimeMillis();
				CQRMain.logger.info("{}/{}", checkedFiles, files.size());
			}
		}
		CQRMain.logger.info("Updated {} structure files", updatedFiles);
	}

	@Deprecated
	private void readFromDeprecatedNBT(NBTTagCompound compound) {
		String cqrFileVersion = compound.getString("cqr_file_version");
		if (!cqrFileVersion.equals("1.1.0")) {
			throw new IllegalArgumentException(String.format("Structure nbt is too old! Expected %s but got %s.", "1.1.0", cqrFileVersion));
		}

		this.author = compound.getString("author");
		this.size = NBTUtil.getPosFromTag(compound.getCompoundTag("size"));

		this.blockInfoList.clear();
		this.entityInfoList.clear();

		BlockStatePalette blockStatePalette = new BlockStatePalette();

		// Load compound tags
		NBTTagList compoundTagList = compound.getTagList("compoundTagList", Constants.NBT.TAG_COMPOUND);

		// Load block states
		int blockStateIndex = 0;
		for (NBTBase nbt : compound.getTagList("palette", Constants.NBT.TAG_COMPOUND)) {
			blockStatePalette.addMapping(NBTUtil.readBlockState((NBTTagCompound) nbt), blockStateIndex++);
		}

		// Load normal blocks
		int x = 0;
		int y = 0;
		int z = 0;
		for (NBTBase nbt : compound.getTagList("blockInfoList", Constants.NBT.TAG_INT_ARRAY)) {
			this.blockInfoList.add(PreparablePosInfo.Registry.read(x, y, z, (NBTTagIntArray) nbt, blockStatePalette, compoundTagList));
			if (x < this.size.getX() - 1) {
				x++;
			} else if (y < this.size.getY() - 1) {
				x = 0;
				y++;
			} else if (z < this.size.getZ() - 1) {
				x = 0;
				y = 0;
				z++;
			}
		}
		this.blockInfoList.sort(DEFAULT_COMPARATOR);

		// Load special blocks
		for (NBTBase nbt : compound.getTagList("specialBlockInfoList", Constants.NBT.TAG_COMPOUND)) {
			NBTTagCompound tag = (NBTTagCompound) nbt;
			if (tag.hasKey("blockInfo", Constants.NBT.TAG_INT_ARRAY)) {
				NBTTagList pos = tag.getTagList("pos", Constants.NBT.TAG_INT);
				this.blockInfoList.add(PreparablePosInfo.Registry.read(pos.getIntAt(0), pos.getIntAt(1), pos.getIntAt(2),
						(NBTTagIntArray) tag.getTag("blockInfo"), blockStatePalette, compoundTagList));
			}
		}

		// Load entities
		for (NBTBase nbt : compound.getTagList("entityInfoList", Constants.NBT.TAG_COMPOUND)) {
			this.entityInfoList.add(new PreparableEntityInfo((NBTTagCompound) nbt));
		}
	}

}
