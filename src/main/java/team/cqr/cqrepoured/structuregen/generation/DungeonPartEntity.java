package team.cqr.cqrepoured.structuregen.generation;

import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.common.util.Constants;
import team.cqr.cqrepoured.structuregen.inhabitants.DungeonInhabitant;
import team.cqr.cqrepoured.structuregen.inhabitants.DungeonInhabitantManager;
import team.cqr.cqrepoured.structuregen.structurefile.EntityInfo;

public class DungeonPartEntity extends AbstractDungeonPart {

	protected final Deque<EntityInfo> entityInfoList = new LinkedList<>();
	protected PlacementSettings settings;
	protected DungeonInhabitant dungeonMobType;

	public DungeonPartEntity(World world, DungeonGenerator dungeonGenerator) {
		this(world, dungeonGenerator, BlockPos.ORIGIN, Collections.emptyList(), new PlacementSettings(), DungeonInhabitantManager.DEFAULT_DUNGEON_INHABITANT);
	}

	public DungeonPartEntity(World world, DungeonGenerator dungeonGenerator, BlockPos partPos, Collection<EntityInfo> entities, PlacementSettings settings, DungeonInhabitant dungeonMobType) {
		super(world, dungeonGenerator, partPos);
		for (EntityInfo entityInfo : entities) {
			if (entityInfo != null) {
				this.updateMinAndMaxPos(partPos.add(entityInfo.getPos()));
				this.entityInfoList.add(entityInfo);
			}
		}
		this.settings = settings;
		this.dungeonMobType = dungeonMobType;

		if (!this.entityInfoList.isEmpty()) {
			EntityInfo firstEntityInfo = this.entityInfoList.getFirst();
			this.minPos = partPos.add(Template.transformedBlockPos(settings, firstEntityInfo.getPos()));
			this.maxPos = this.minPos;

			BlockPos.MutableBlockPos p1 = new BlockPos.MutableBlockPos(this.minPos);
			BlockPos.MutableBlockPos p2 = new BlockPos.MutableBlockPos(this.minPos);
			BlockPos.MutableBlockPos p3 = new BlockPos.MutableBlockPos(this.minPos);
			for (EntityInfo entityInfo : this.entityInfoList) {
				this.transformedXYZasMutablePos(partPos, entityInfo.getX(), entityInfo.getY(), entityInfo.getZ(), settings.getMirror(), settings.getRotation(), p1);
				p2.setPos(Math.min(p2.getX(), p1.getX()), Math.min(p2.getY(), p1.getY()), Math.min(p2.getZ(), p1.getZ()));
				p3.setPos(Math.max(p3.getX(), p1.getX()), Math.max(p3.getY(), p1.getY()), Math.max(p3.getZ(), p1.getZ()));
			}
			this.updateMinAndMaxPos(p2);
			this.updateMinAndMaxPos(p3);
		}
	}

	@Override
	public NBTTagCompound writeToNBT() {
		NBTTagCompound compound = super.writeToNBT();

		compound.setInteger("mirror", this.settings.getMirror().ordinal());
		compound.setInteger("rotation", this.settings.getRotation().ordinal());
		compound.setString("mob", this.dungeonMobType.getName());

		// Save entities
		NBTTagList nbtTagList = new NBTTagList();
		for (EntityInfo entityInfo : this.entityInfoList) {
			nbtTagList.appendTag(entityInfo.getEntityData());
		}
		compound.setTag("entityInfoList", nbtTagList);

		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.entityInfoList.clear();
		this.settings = new PlacementSettings();
		this.settings.setMirror(Mirror.values()[compound.getInteger("mirror")]);
		this.settings.setRotation(Rotation.values()[compound.getInteger("rotation")]);
		this.dungeonMobType = DungeonInhabitantManager.instance().getInhabitant(compound.getString("mob"));

		// Load entities
		for (NBTBase nbt : compound.getTagList("entityInfoList", Constants.NBT.TAG_COMPOUND)) {
			this.entityInfoList.add(new EntityInfo((NBTTagCompound) nbt));
		}
	}

	@Override
	public String getId() {
		return DUNGEON_PART_ENTITY_ID;
	}

	@Override
	public void generateNext() {
		if (!this.entityInfoList.isEmpty()) {
			EntityInfo entityInfo = this.entityInfoList.removeFirst();
			entityInfo.generate(this.world, this.dungeonGenerator.getPos(), this.partPos, this.settings, this.dungeonMobType, this.dungeonGenerator.getProtectedRegion());
		}
	}

	@Override
	public boolean isGenerated() {
		return this.entityInfoList.isEmpty();
	}

}
