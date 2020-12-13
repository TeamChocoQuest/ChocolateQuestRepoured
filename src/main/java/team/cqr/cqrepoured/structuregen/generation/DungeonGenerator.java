package team.cqr.cqrepoured.structuregen.generation;

import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.util.Constants;
import team.cqr.cqrepoured.structuregen.dungeons.DungeonBase;
import team.cqr.cqrepoured.structureprot.ProtectedRegion;
import team.cqr.cqrepoured.structureprot.ProtectedRegionManager;
import team.cqr.cqrepoured.util.CQRConfig;
import team.cqr.cqrepoured.util.ChunkUtil;
import team.cqr.cqrepoured.util.DungeonGenUtils;

public class DungeonGenerator {

	protected final World world;
	protected final Deque<AbstractDungeonPart> parts = new LinkedList<>();
	protected UUID uuid = MathHelper.getRandomUUID();
	protected String dungeonName;
	protected BlockPos pos;
	protected BlockPos minPos;
	protected BlockPos maxPos;
	protected ProtectedRegion protectedRegion;
	protected DungeonPartLight dungeonPartLight;
	protected EnumDungeonGeneratorState state = EnumDungeonGeneratorState.PRE_GENERATION;
	private long tickTime;
	private ForgeChunkManager.Ticket chunkTicket;
	private boolean ticketRequested;

	public enum EnumDungeonGeneratorState {
		PRE_GENERATION, GENERATION, POST_GENERATION;
	}

	public DungeonGenerator(World world, BlockPos pos, String dungeonName) {
		this.world = world;
		this.pos = pos;
		this.minPos = pos;
		this.maxPos = pos;
		this.dungeonName = dungeonName;
	}

	public DungeonGenerator(World world, NBTTagCompound compound) {
		this.world = world;
		this.readFromNBT(compound);
	}

	public NBTTagCompound writeToNBT() {
		NBTTagCompound compound = new NBTTagCompound();

		NBTTagList nbtTagList = new NBTTagList();
		for (AbstractDungeonPart part : this.parts) {
			nbtTagList.appendTag(part.writeToNBT());
		}
		compound.setTag("parts", nbtTagList);
		compound.setTag("uuid", NBTUtil.createUUIDTag(this.uuid));
		compound.setTag("pos", NBTUtil.createPosTag(this.pos));
		compound.setTag("minPos", NBTUtil.createPosTag(this.minPos));
		compound.setTag("maxPos", NBTUtil.createPosTag(this.maxPos));
		compound.setTag("partLight", this.dungeonPartLight.writeToNBT());
		if (this.protectedRegion != null) {
			compound.setTag("protectedRegion", NBTUtil.createUUIDTag(this.protectedRegion.getUuid()));
		}
		compound.setInteger("state", this.state.ordinal());
		compound.setString("dungeonName", this.dungeonName);

		return compound;
	}

	public void readFromNBT(NBTTagCompound compound) {
		this.parts.clear();

		for (NBTBase nbt : compound.getTagList("parts", Constants.NBT.TAG_COMPOUND)) {
			AbstractDungeonPart part = AbstractDungeonPart.createDungeonPart(this.world, this, (NBTTagCompound) nbt);
			if (part != null) {
				this.parts.add(part);
			}
		}
		this.uuid = NBTUtil.getUUIDFromTag(compound.getCompoundTag("uuid"));
		this.pos = NBTUtil.getPosFromTag(compound.getCompoundTag("pos"));
		this.minPos = NBTUtil.getPosFromTag(compound.getCompoundTag("minPos"));
		this.maxPos = NBTUtil.getPosFromTag(compound.getCompoundTag("maxPos"));
		this.dungeonPartLight = new DungeonPartLight(this.world, this);
		this.dungeonPartLight.readFromNBT(compound.getCompoundTag("partLight"));
		if (compound.hasKey("protectedRegion", Constants.NBT.TAG_COMPOUND)) {
			ProtectedRegionManager protectedRegionManager = ProtectedRegionManager.getInstance(this.world);
			if (protectedRegionManager != null) {
				UUID protectedRegionUuid = NBTUtil.getUUIDFromTag(compound.getCompoundTag("protectedRegion"));
				this.protectedRegion = protectedRegionManager.getProtectedRegion(protectedRegionUuid);
			}
		}
		this.state = EnumDungeonGeneratorState.values()[compound.getInteger("state")];
		this.dungeonName = compound.getString("dungeonName");
	}

	public void tick() {
		if (this.state == EnumDungeonGeneratorState.GENERATION) {
			if (!this.ticketRequested) {
				this.chunkTicket = ChunkUtil.getTicket(this.world, this.minPos, this.maxPos, true);
				this.ticketRequested = true;
			}

			this.tickTime = Math.min(this.tickTime + CQRConfig.advanced.generationSpeed * 1000000, CQRConfig.advanced.generationSpeed * 1000000);
			int i = 0;

			while (this.tickTime > 0 && i < CQRConfig.advanced.generationLimit && !this.isGenerated()) {
				long t = System.nanoTime();

				if (!this.parts.isEmpty()) {
					AbstractDungeonPart part = this.parts.getFirst();
					part.generateNext();
					if (part.isGenerated()) {
						this.parts.removeFirst();
					}
				} else if (!this.dungeonPartLight.isGenerated()) {
					this.dungeonPartLight.generateNext();
				} else {
					this.endGeneration();
				}

				i++;
				this.tickTime -= System.nanoTime() - t;
			}
		}
	}

	public boolean isGenerated() {
		return this.state == EnumDungeonGeneratorState.POST_GENERATION;
	}

	public UUID getUuid() {
		return this.uuid;
	}

	public String getDungeonName() {
		return this.dungeonName;
	}

	public BlockPos getPos() {
		return this.pos;
	}

	public BlockPos getMinPos() {
		return this.minPos;
	}

	public BlockPos getMaxPos() {
		return this.maxPos;
	}

	public ProtectedRegion getProtectedRegion() {
		return this.protectedRegion;
	}

	public void add(AbstractDungeonPart part) {
		if (this.state == EnumDungeonGeneratorState.PRE_GENERATION && part != null && !part.isGenerated()) {
			this.parts.add(part);
			this.minPos = DungeonGenUtils.getValidMinPos(part.getMinPos(), this.minPos);
			this.maxPos = DungeonGenUtils.getValidMaxPos(part.getMaxPos(), this.maxPos);
		}
	}

	public void addAll(Collection<AbstractDungeonPart> parts) {
		if (this.state == EnumDungeonGeneratorState.PRE_GENERATION) {
			for (AbstractDungeonPart part : parts) {
				this.add(part);
			}
		}
	}

	public void startGeneration(@Nullable DungeonBase dungeon) {
		if (this.state == EnumDungeonGeneratorState.PRE_GENERATION) {
			this.dungeonPartLight = new DungeonPartLight(this.world, this, this.minPos, this.maxPos);

			if (dungeon != null && dungeon.isProtectionSystemEnabled()) {
				this.protectedRegion = new ProtectedRegion(this.world, dungeon.getDungeonName(), this.pos.up(dungeon.getUnderGroundOffset()), this.minPos, this.maxPos);
				this.protectedRegion.setup(dungeon.preventBlockBreaking(), dungeon.preventBlockPlacing(), dungeon.preventExplosionsTNT(), dungeon.preventExplosionsOther(), dungeon.preventFireSpreading(), dungeon.preventEntitySpawning(), dungeon.ignoreNoBossOrNexus());
				ProtectedRegionManager manager = ProtectedRegionManager.getInstance(this.world);

				if (manager != null) {
					manager.addProtectedRegion(this.protectedRegion);
				}
			}

			this.state = EnumDungeonGeneratorState.GENERATION;
		}
	}

	public void endGeneration() {
		if (this.state == EnumDungeonGeneratorState.GENERATION) {
			if (this.chunkTicket != null) {
				ForgeChunkManager.releaseTicket(this.chunkTicket);
			}

			this.state = EnumDungeonGeneratorState.POST_GENERATION;

			if (this.protectedRegion != null) {
				if (this.protectedRegion.isValid()) {
					this.protectedRegion.updateProtectedBlocks();
					this.protectedRegion.finishGenerating();
				} else {
					ProtectedRegionManager manager = ProtectedRegionManager.getInstance(this.world);

					if (manager != null) {
						manager.removeProtectedRegion(this.protectedRegion);
					}
				}
			}
		}
	}

}
