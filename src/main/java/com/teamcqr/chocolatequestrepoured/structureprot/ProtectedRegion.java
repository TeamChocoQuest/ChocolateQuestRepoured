package com.teamcqr.chocolatequestrepoured.structureprot;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.network.server.packet.SPacketProtectedRegionRemoveBlockDependency;
import com.teamcqr.chocolatequestrepoured.network.server.packet.SPacketProtectedRegionRemoveEntityDependency;
import com.teamcqr.chocolatequestrepoured.util.ByteBufUtil;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;
import com.teamcqr.chocolatequestrepoured.util.ChunkUtil;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class ProtectedRegion {

	private static final Set<Material> MATERIAL_BLACKLIST = new HashSet<>();
	private static boolean liquidsWhitelisted = false;

	private static final String PROTECTED_REGION_VERSION = "1.1.0";
	private final World world;
	private UUID uuid = MathHelper.getRandomUUID();
	private String name;
	private BlockPos pos;
	private BlockPos startPos;
	private BlockPos endPos;
	private BlockPos size;
	private byte[] protectedBlocks;
	private boolean preventBlockBreaking = false;
	private boolean preventBlockPlacing = false;
	private boolean preventExplosionsTNT = false;
	private boolean preventExplosionsOther = false;
	private boolean preventFireSpreading = false;
	private boolean preventEntitySpawning = false;
	private boolean ignoreNoBossOrNexus = false;
	private boolean isGenerating = true;
	private final Set<UUID> entityDependencies = new HashSet<>();
	private final Set<BlockPos> blockDependencies = new HashSet<>();

	public ProtectedRegion(World world, String dungeonName, BlockPos pos, BlockPos startPos, BlockPos endPos) {
		this.world = world;
		this.name = dungeonName;
		this.pos = pos.toImmutable();
		this.startPos = DungeonGenUtils.getValidMinPos(startPos, endPos);
		this.endPos = DungeonGenUtils.getValidMaxPos(startPos, endPos);
		int sizeX = this.endPos.getX() - this.startPos.getX() + 1;
		int sizeY = this.endPos.getY() - this.startPos.getY() + 1;
		int sizeZ = this.endPos.getZ() - this.startPos.getZ() + 1;
		this.size = new BlockPos(sizeX, sizeY, sizeZ);
		this.protectedBlocks = new byte[sizeX * sizeY * sizeZ];
	}

	public ProtectedRegion(World world, NBTTagCompound compound) {
		this.world = world;
		this.readFromNBT(compound);
	}

	public ProtectedRegion(World world, ByteBuf buf) {
		this.world = world;
		this.readFromByteBuf(buf);
	}

	public NBTTagCompound writeToNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setString("version", PROTECTED_REGION_VERSION);
		compound.setTag("uuid", NBTUtil.createUUIDTag(this.uuid));
		compound.setString("name", this.name);
		compound.setTag("pos", NBTUtil.createPosTag(this.pos));
		compound.setTag("startPos", NBTUtil.createPosTag(this.startPos));
		compound.setTag("endPos", NBTUtil.createPosTag(this.endPos));
		compound.setByteArray("protectedBlocks", this.protectedBlocks);
		compound.setBoolean("preventBlockBreaking", this.preventBlockBreaking);
		compound.setBoolean("preventBlockPlacing", this.preventBlockPlacing);
		compound.setBoolean("preventExplosionsTNT", this.preventExplosionsTNT);
		compound.setBoolean("preventExplosionsOther", this.preventExplosionsOther);
		compound.setBoolean("preventFireSpreading", this.preventFireSpreading);
		compound.setBoolean("preventEntitySpawning", this.preventEntitySpawning);
		compound.setBoolean("ignoreNoBossOrNexus", this.ignoreNoBossOrNexus);
		compound.setBoolean("isGenerating", this.isGenerating);
		NBTTagList nbtTagList1 = new NBTTagList();
		for (UUID entityUuid : this.entityDependencies) {
			nbtTagList1.appendTag(NBTUtil.createUUIDTag(entityUuid));
		}
		compound.setTag("entityDependencies", nbtTagList1);
		NBTTagList nbtTagList2 = new NBTTagList();
		for (BlockPos blockPos : this.blockDependencies) {
			nbtTagList1.appendTag(NBTUtil.createPosTag(blockPos));
		}
		compound.setTag("blockDependencies", nbtTagList2);
		return compound;
	}

	public void readFromNBT(NBTTagCompound compound) {
		String version = compound.getString("version");
		if (!version.equals(PROTECTED_REGION_VERSION)) {
			CQRMain.logger.warn("Warning! Trying to create protected region from file which was created with an older/newer version of CQR! Expected {} but got {}.", PROTECTED_REGION_VERSION, version);
		}

		this.uuid = NBTUtil.getUUIDFromTag(compound.getCompoundTag("uuid"));
		this.name = compound.getString("name");
		this.pos = NBTUtil.getPosFromTag(compound.getCompoundTag("pos"));
		this.startPos = NBTUtil.getPosFromTag(compound.getCompoundTag("startPos"));
		this.endPos = NBTUtil.getPosFromTag(compound.getCompoundTag("endPos"));
		int sizeX = this.endPos.getX() - this.startPos.getX() + 1;
		int sizeY = this.endPos.getY() - this.startPos.getY() + 1;
		int sizeZ = this.endPos.getZ() - this.startPos.getZ() + 1;
		this.size = new BlockPos(sizeX, sizeY, sizeZ);
		if (compound.hasKey("protectedBlocks", Constants.NBT.TAG_BYTE_ARRAY)) {
			this.protectedBlocks = compound.getByteArray("protectedBlocks");
		} else {
			this.protectedBlocks = new byte[sizeX * sizeY * sizeZ];
			for (int i = 0; i < this.protectedBlocks.length; i++) {
				this.protectedBlocks[i] = 1;
			}
		}
		this.preventBlockBreaking = compound.getBoolean("preventBlockBreaking");
		this.preventBlockPlacing = compound.getBoolean("preventBlockPlacing");
		this.preventExplosionsTNT = compound.getBoolean("preventExplosionsTNT");
		this.preventExplosionsOther = compound.getBoolean("preventExplosionsOther");
		this.preventFireSpreading = compound.getBoolean("preventFireSpreading");
		this.preventEntitySpawning = compound.getBoolean("preventEntitySpawning");
		this.ignoreNoBossOrNexus = compound.getBoolean("ignoreNoBossOrNexus");
		this.isGenerating = compound.getBoolean("isGenerating");
		this.entityDependencies.clear();
		NBTTagList nbtTagList1 = compound.getTagList("entityDependencies", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < nbtTagList1.tagCount(); i++) {
			this.entityDependencies.add(NBTUtil.getUUIDFromTag(nbtTagList1.getCompoundTagAt(i)));
		}
		this.blockDependencies.clear();
		NBTTagList nbtTagList2 = compound.getTagList("blockDependencies", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < nbtTagList2.tagCount(); i++) {
			this.blockDependencies.add(NBTUtil.getPosFromTag(nbtTagList2.getCompoundTagAt(i)));
		}

		// TODO Fix for protected regions not deleting themselves. Should be removed in the future.
		if (!version.equals(PROTECTED_REGION_VERSION)) {
			this.isGenerating = false;
		}
	}

	public void writeToByteBuf(ByteBuf buf) {
		ByteBufUtil.writeUuid(buf, this.uuid);
		ByteBufUtils.writeUTF8String(buf, this.name);
		ByteBufUtil.writeBlockPos(buf, this.pos);
		ByteBufUtil.writeBlockPos(buf, this.startPos);
		ByteBufUtil.writeBlockPos(buf, this.endPos);
		buf.writeBytes(this.protectedBlocks);

		byte flags = 0;
		flags |= this.preventBlockBreaking ? 1 : 0;
		flags |= this.preventBlockPlacing ? (1 << 1) : 0;
		flags |= this.preventExplosionsTNT ? (1 << 2) : 0;
		flags |= this.preventExplosionsOther ? (1 << 3) : 0;
		flags |= this.preventFireSpreading ? (1 << 4) : 0;
		flags |= this.preventEntitySpawning ? (1 << 5) : 0;
		flags |= this.ignoreNoBossOrNexus ? (1 << 6) : 0;
		flags |= this.isGenerating ? (1 << 7) : 0;
		buf.writeByte(flags);

		buf.writeShort(this.entityDependencies.size());
		for (UUID entityUuid : this.entityDependencies) {
			ByteBufUtil.writeUuid(buf, entityUuid);
		}

		buf.writeShort(this.blockDependencies.size());
		for (BlockPos blockPos : this.blockDependencies) {
			ByteBufUtil.writeBlockPos(buf, blockPos);
		}
	}

	public void readFromByteBuf(ByteBuf buf) {
		this.uuid = ByteBufUtil.readUuid(buf);
		this.name = ByteBufUtils.readUTF8String(buf);
		this.pos = ByteBufUtil.readBlockPos(buf);
		this.startPos = ByteBufUtil.readBlockPos(buf);
		this.endPos = ByteBufUtil.readBlockPos(buf);
		int sizeX = this.endPos.getX() - this.startPos.getX() + 1;
		int sizeY = this.endPos.getY() - this.startPos.getY() + 1;
		int sizeZ = this.endPos.getZ() - this.startPos.getZ() + 1;
		this.size = new BlockPos(sizeX, sizeY, sizeZ);
		this.protectedBlocks = new byte[sizeX * sizeY * sizeZ];
		buf.readBytes(this.protectedBlocks);

		byte flags = buf.readByte();
		this.preventBlockBreaking = (flags & 1) == 1;
		this.preventBlockPlacing = ((flags >> 1) & 1) == 1;
		this.preventExplosionsTNT = ((flags >> 2) & 1) == 1;
		this.preventExplosionsOther = ((flags >> 3) & 1) == 1;
		this.preventFireSpreading = ((flags >> 4) & 1) == 1;
		this.preventEntitySpawning = ((flags >> 5) & 1) == 1;
		this.ignoreNoBossOrNexus = ((flags >> 6) & 1) == 1;
		this.isGenerating = ((flags >> 7) & 1) == 1;

		short entityDependenciesCount = buf.readShort();
		for (int i = 0; i < entityDependenciesCount; i++) {
			this.entityDependencies.add(ByteBufUtil.readUuid(buf));
		}

		short blockDependenciesCount = buf.readShort();
		for (int i = 0; i < blockDependenciesCount; i++) {
			this.blockDependencies.add(ByteBufUtil.readBlockPos(buf));
		}
	}

	public boolean isInsideProtectedRegion(BlockPos pos) {
		if (pos.getX() < this.startPos.getX()) {
			return false;
		}
		if (pos.getY() < this.startPos.getY()) {
			return false;
		}
		if (pos.getZ() < this.startPos.getZ()) {
			return false;
		}
		if (pos.getX() > this.endPos.getX()) {
			return false;
		}
		if (pos.getY() > this.endPos.getY()) {
			return false;
		}
		return pos.getZ() <= this.endPos.getZ();
	}

	public boolean isProtected(BlockPos pos) {
		if (!this.isInsideProtectedRegion(pos)) {
			return false;
		}
		int x = (pos.getX() - this.startPos.getX()) * this.size.getY() * this.size.getZ();
		int y = (pos.getY() - this.startPos.getY()) * this.size.getZ();
		int z = pos.getZ() - this.startPos.getZ();
		return this.protectedBlocks[x + y + z] != 0;
	}

	public boolean isValid() {
		return this.isGenerating || !this.entityDependencies.isEmpty() || !this.blockDependencies.isEmpty() || this.ignoreNoBossOrNexus;
	}

	public void setup(boolean preventBlockBreaking, boolean preventBlockPlacing, boolean preventExplosionsTNT, boolean preventExplosionsOther, boolean preventFireSpreading, boolean preventEntitySpawning, boolean ignoreNoBossOrNexus) {
		if (!this.isGenerating) {
			return;
		}

		this.preventBlockBreaking = preventBlockBreaking;
		this.preventBlockPlacing = preventBlockPlacing;
		this.preventExplosionsTNT = preventExplosionsTNT;
		this.preventExplosionsOther = preventExplosionsOther;
		this.preventFireSpreading = preventFireSpreading;
		this.preventEntitySpawning = preventEntitySpawning;
		this.ignoreNoBossOrNexus = ignoreNoBossOrNexus;
	}

	public void updateProtectedBlocks() {
		if (!this.isGenerating) {
			return;
		}

		ForgeChunkManager.Ticket chunkTicket = ChunkUtil.getTicket(this.world, this.startPos, this.endPos, true);

		for (BlockPos.MutableBlockPos mutablePos : BlockPos.getAllInBoxMutable(this.startPos, this.endPos)) {
			IBlockState state = this.world.getBlockState(mutablePos);
			Material material = state.getMaterial();
			int x = (mutablePos.getX() - this.startPos.getX()) * this.size.getY() * this.size.getZ();
			int y = (mutablePos.getY() - this.startPos.getY()) * this.size.getZ();
			int z = mutablePos.getZ() - this.startPos.getZ();
			this.protectedBlocks[x + y + z] = (byte) (!MATERIAL_BLACKLIST.contains(material) && (!liquidsWhitelisted || !material.isLiquid()) ? 1 : 0);
		}

		if (chunkTicket != null) {
			ForgeChunkManager.releaseTicket(chunkTicket);
		}
	}

	public World getWorld() {
		return this.world;
	}

	public UUID getUuid() {
		return this.uuid;
	}

	public String getName() {
		return this.name;
	}

	public BlockPos getPos() {
		return this.pos;
	}

	public BlockPos getStartPos() {
		return this.startPos;
	}

	public BlockPos getEndPos() {
		return this.endPos;
	}

	public void setPreventBlockBreaking(boolean preventBlockBreaking) {
		this.preventBlockBreaking = preventBlockBreaking;
	}

	public boolean preventBlockBreaking() {
		return this.preventBlockBreaking;
	}

	public void setPreventBlockPlacing(boolean preventBlockPlacing) {
		this.preventBlockPlacing = preventBlockPlacing;
	}

	public boolean preventBlockPlacing() {
		return this.preventBlockPlacing;
	}

	public void setPreventExplosionsTNT(boolean preventExplosionsTNT) {
		this.preventExplosionsTNT = preventExplosionsTNT;
	}

	public boolean preventExplosionsTNT() {
		return this.preventExplosionsTNT;
	}

	public void setPreventExplosionsOther(boolean preventExplosionsOther) {
		this.preventExplosionsOther = preventExplosionsOther;
	}

	public boolean preventExplosionsOther() {
		return this.preventExplosionsOther;
	}

	public void setPreventFireSpreading(boolean preventFireSpreading) {
		this.preventFireSpreading = preventFireSpreading;
	}

	public boolean preventFireSpreading() {
		return this.preventFireSpreading;
	}

	public void setPreventEntitySpawning(boolean preventEntitySpawning) {
		this.preventEntitySpawning = preventEntitySpawning;
	}

	public boolean preventEntitySpawning() {
		return this.preventEntitySpawning;
	}

	public void setIgnoreNoBossOrNexus(boolean ignoreNoBossOrNexus) {
		this.ignoreNoBossOrNexus = ignoreNoBossOrNexus;
	}

	public boolean ignoreNoBossOrNexus() {
		return this.ignoreNoBossOrNexus;
	}

	public void addEntityDependency(UUID uuid) {
		if (!this.isGenerating) {
			return;
		}
		this.entityDependencies.add(uuid);
	}

	public void removeEntityDependency(UUID uuid) {
		boolean flag = this.entityDependencies.remove(uuid);

		if (flag && this.world != null && !this.world.isRemote) {
			if (!this.isValid()) {
				ProtectedRegionManager protectedRegionManager = ProtectedRegionManager.getInstance(this.world);
				if (protectedRegionManager != null) {
					ProtectedRegionManager.getInstance(this.world).removeProtectedRegion(this);
				}
			} else {
				CQRMain.NETWORK.sendToDimension(new SPacketProtectedRegionRemoveEntityDependency(this.uuid, uuid), this.world.provider.getDimension());
			}
		}
	}

	public boolean isEntityDependency(UUID uuid) {
		return this.entityDependencies.contains(uuid);
	}

	public Set<UUID> getEntityDependencies() {
		return Collections.unmodifiableSet(this.entityDependencies);
	}

	public void addBlockDependency(BlockPos pos) {
		if (!this.isGenerating) {
			return;
		}
		this.blockDependencies.add(pos);
	}

	public void removeBlockDependency(BlockPos pos) {
		boolean flag = this.blockDependencies.remove(pos);

		if (flag && this.world != null && !this.world.isRemote) {
			if (!this.isValid()) {
				ProtectedRegionManager protectedRegionManager = ProtectedRegionManager.getInstance(this.world);
				if (protectedRegionManager != null) {
					ProtectedRegionManager.getInstance(this.world).removeProtectedRegion(this);
				}
			} else {
				CQRMain.NETWORK.sendToDimension(new SPacketProtectedRegionRemoveBlockDependency(this.uuid, pos), this.world.provider.getDimension());
			}
		}
	}

	public boolean isBlockDependency(BlockPos pos) {
		return this.blockDependencies.contains(pos);
	}

	public Set<BlockPos> getBlockDependencies() {
		return Collections.unmodifiableSet(this.blockDependencies);
	}

	public void finishGenerating() {
		this.isGenerating = false;
	}

	public boolean isGenerating() {
		return this.isGenerating;
	}

	public static void updateMaterialBlacklist() {
		MATERIAL_BLACKLIST.clear();
		liquidsWhitelisted = false;

		for (String s : CQRConfig.dungeonProtection.protectionSystemMaterialBlacklist) {
			if (s.equalsIgnoreCase("liquid")) {
				liquidsWhitelisted = true;
			} else {
				Material m = getMaterialByName(s);
				if (m != null) {
					MATERIAL_BLACKLIST.add(m);
				}
			}
		}
	}

	@Nullable
	private static Material getMaterialByName(String name) {
		switch (name.toLowerCase()) {
		case "air":
			return Material.AIR;
		case "grass":
			return Material.GRASS;
		case "ground":
			return Material.GROUND;
		case "wood":
			return Material.WOOD;
		case "rock":
			return Material.ROCK;
		case "iron":
			return Material.IRON;
		case "anvil":
			return Material.ANVIL;
		case "water":
			return Material.WATER;
		case "lava":
			return Material.LAVA;
		case "leaves":
			return Material.LEAVES;
		case "plants":
			return Material.PLANTS;
		case "vine":
			return Material.VINE;
		case "sponge":
			return Material.SPONGE;
		case "cloth":
			return Material.CLOTH;
		case "fire":
			return Material.FIRE;
		case "sand":
			return Material.SAND;
		case "circuits":
			return Material.CIRCUITS;
		case "carpet":
			return Material.CARPET;
		case "glass":
			return Material.GLASS;
		case "redstone_light":
			return Material.REDSTONE_LIGHT;
		case "tnt":
			return Material.TNT;
		case "coral":
			return Material.CORAL;
		case "ice":
			return Material.ICE;
		case "packed_ice":
			return Material.PACKED_ICE;
		case "snow":
			return Material.SNOW;
		case "crafted_snow":
			return Material.CRAFTED_SNOW;
		case "cactus":
			return Material.CACTUS;
		case "clay":
			return Material.CLAY;
		case "gourd":
			return Material.GOURD;
		case "dragon_egg":
			return Material.DRAGON_EGG;
		case "portal":
			return Material.PORTAL;
		case "cake":
			return Material.CAKE;
		case "web":
			return Material.WEB;
		case "piston":
			return Material.PISTON;
		case "barrier":
			return Material.BARRIER;
		case "structure_void":
			return Material.STRUCTURE_VOID;
		default:
			return null;
		}
	}

}
