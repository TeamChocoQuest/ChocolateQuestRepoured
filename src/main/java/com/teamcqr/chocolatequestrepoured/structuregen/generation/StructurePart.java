package com.teamcqr.chocolatequestrepoured.structuregen.generation;

import com.teamcqr.chocolatequestrepoured.objects.banners.EBanners;
import com.teamcqr.chocolatequestrepoured.structuregen.EDungeonMobType;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructurePart;
import com.teamcqr.chocolatequestrepoured.structureprot.ProtectedRegion;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

public class StructurePart implements IStructure {

	private CQStructurePart part;
	private PlacementSettings settings;
	private BlockPos pos;
	private int dungeonChunkX;
	private int dungeonChunkZ;
	private EDungeonMobType dungeonMobType;
	private boolean replaceBanners;
	private EBanners dungeonBanner;

	public StructurePart(NBTTagCompound compound) {
		this.readFromNBT(compound);
	}

	public StructurePart(CQStructurePart part, PlacementSettings settings, BlockPos pos, int dungeonChunkX, int dungeonChunkZ, EDungeonMobType dungeonMobType, boolean replaceBanners, EBanners dungeonBanner) {
		this.part = part;
		this.settings = settings;
		this.pos = pos;
		this.dungeonChunkX = dungeonChunkX;
		this.dungeonChunkZ = dungeonChunkZ;
		this.dungeonMobType = dungeonMobType;
		this.replaceBanners = replaceBanners;
		this.dungeonBanner = dungeonBanner;
	}

	@Override
	public void generate(World world, ProtectedRegion protectedRegion) {
		this.part.addBlocksToWorld(world, this.pos, this.settings, this.dungeonChunkX, this.dungeonChunkZ, this.dungeonMobType, this.replaceBanners, this.dungeonBanner, protectedRegion);
	}

	@Override
	public boolean canGenerate(World world) {
		return world.isAreaLoaded(this.pos, this.pos.add(this.part.getSize().rotate(this.settings.getRotation())));
	}

	@Override
	public NBTTagCompound writeToNBT() {
		NBTTagCompound compound = new NBTTagCompound();

		compound.setString("id", "structurePart");
		compound.setTag("part", this.part.writeToNBT(new NBTTagCompound()));
		compound.setInteger("rotation", this.settings.getRotation().ordinal());
		compound.setInteger("mirror", this.settings.getMirror().ordinal());
		compound.setTag("pos", NBTUtil.createPosTag(this.pos));
		compound.setInteger("dungeonChunkX", this.dungeonChunkX);
		compound.setInteger("dungeonChunkZ", this.dungeonChunkZ);
		compound.setString("dungeonMob", this.dungeonMobType.toString());
		compound.setBoolean("replaceBanners", this.replaceBanners);
		compound.setString("dungeonBanner", this.dungeonBanner != null ? this.dungeonBanner.toString() : "");

		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		this.part = new CQStructurePart();
		this.part.read(compound.getCompoundTag("part"));
		this.settings = new PlacementSettings();
		this.settings.setRotation(Rotation.values()[compound.getInteger("rotation")]);
		this.settings.setMirror(Mirror.values()[compound.getInteger("mirror")]);
		this.pos = NBTUtil.getPosFromTag(compound.getCompoundTag("pos"));
		this.dungeonChunkX = compound.getInteger("dungeonChunkX");
		this.dungeonChunkZ = compound.getInteger("dungeonChunkZ");
		this.dungeonMobType = EDungeonMobType.byString(compound.getString("dungeonMob"));
		this.replaceBanners = compound.getBoolean("replaceBanners");
		this.dungeonBanner = !compound.getString("dungeonBanner").isEmpty() ? EBanners.valueOf(compound.getString("dungeonBanner")) : null;
	}

	@Override
	public BlockPos getPos() {
		return DungeonGenUtils.getMinPos(this.pos, this.pos.add(Template.transformedBlockPos(this.settings, this.part.getSize())));
	}

	@Override
	public BlockPos getSize() {
		return DungeonGenUtils.getMaxPos(this.pos, this.pos.add(Template.transformedBlockPos(this.settings, this.part.getSize()))).subtract(this.getPos());
	}

}
