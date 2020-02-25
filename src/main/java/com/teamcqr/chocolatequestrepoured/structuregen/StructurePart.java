package com.teamcqr.chocolatequestrepoured.structuregen;

import com.teamcqr.chocolatequestrepoured.objects.banners.EBanners;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructurePart;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;

public class StructurePart implements IStructure {

	private CQStructurePart part;
	private PlacementSettings settings;
	private BlockPos pos;
	private int dungeonChunkX;
	private int dungeonChunkZ;
	private EDungeonMobType dungeonMobType;
	private boolean replaceBanners;
	private EBanners dungeonBanner;
	private boolean hasShield;

	public StructurePart(NBTTagCompound compound) {
		this.readFromNBT(compound);
	}

	public StructurePart(CQStructurePart part, PlacementSettings settings, BlockPos pos, int dungeonChunkX, int dungeonChunkZ, EDungeonMobType dungeonMobType, boolean replaceBanners, EBanners dungeonBanner, boolean hasShield) {
		this.part = part;
		this.settings = settings;
		this.pos = pos;
		this.dungeonChunkX = dungeonChunkX;
		this.dungeonChunkZ = dungeonChunkZ;
		this.dungeonMobType = dungeonMobType;
		this.replaceBanners = replaceBanners;
		this.dungeonBanner = dungeonBanner;
		this.hasShield = hasShield;
	}

	@Override
	public void generate(World world) {
		this.part.addBlocksToWorld(world, this.pos, this.settings, this.dungeonChunkX, this.dungeonChunkZ, this.dungeonMobType, this.replaceBanners, this.dungeonBanner, this.hasShield);
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
		compound.setInteger("rot", this.settings.getRotation().ordinal());
		compound.setTag("pos", NBTUtil.createPosTag(this.pos));
		compound.setInteger("dungeonChunkX", this.dungeonChunkX);
		compound.setInteger("dungeonChunkZ", this.dungeonChunkZ);
		compound.setString("dungeonMob", this.dungeonMobType.toString());
		compound.setBoolean("replaceBanners", this.replaceBanners);
		compound.setString("dungeonBanner", this.dungeonBanner != null ? this.dungeonBanner.toString() : "");
		compound.setBoolean("hasShield", this.hasShield);
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		this.part = new CQStructurePart();
		this.part.read(compound.getCompoundTag("part"));
		this.settings = new PlacementSettings().setRotation(Rotation.values()[compound.getInteger("rot")]);
		this.pos = NBTUtil.getPosFromTag(compound.getCompoundTag("pos"));
		this.dungeonChunkX = compound.getInteger("dungeonChunkX");
		this.dungeonChunkZ = compound.getInteger("dungeonChunkZ");
		this.dungeonMobType = EDungeonMobType.byString(compound.getString("dungeonMob"));
		this.replaceBanners = compound.getBoolean("replaceBanners");
		this.dungeonBanner = EBanners.valueOf(compound.getString("dungeonBanner"));
		this.hasShield = compound.getBoolean("hasShield");
	}

	/*
	public CQStructurePart getPart() {
		return this.part;
	}

	public PlacementSettings getSettings() {
		return this.settings;
	}

	public BlockPos getPos() {
		return this.pos;
	}

	public int getDungeonChunkX() {
		return this.dungeonChunkX;
	}

	public int getDungeonChunkZ() {
		return this.dungeonChunkZ;
	}

	public EDungeonMobType getDungeonMobType() {
		return this.dungeonMobType;
	}

	public boolean isReplaceBanners() {
		return this.replaceBanners;
	}

	public EBanners getDungeonBanner() {
		return this.dungeonBanner;
	}

	public boolean isHasShield() {
		return this.hasShield;
	}
	*/

}
