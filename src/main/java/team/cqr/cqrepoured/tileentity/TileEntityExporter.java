package team.cqr.cqrepoured.tileentity;

import java.io.File;
import java.util.Arrays;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntArrayNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.init.CQRBlockEntities;
import team.cqr.cqrepoured.network.client.packet.CPacketSaveStructureRequest;
import team.cqr.cqrepoured.network.datasync.DataEntryBoolean;
import team.cqr.cqrepoured.network.datasync.DataEntryInt;
import team.cqr.cqrepoured.network.datasync.DataEntryObject;
import team.cqr.cqrepoured.network.datasync.DataEntryString;
import team.cqr.cqrepoured.network.datasync.TileEntityDataManager;
import team.cqr.cqrepoured.util.ByteBufUtil;
import team.cqr.cqrepoured.world.structure.generation.structurefile.CQStructure;

public class TileEntityExporter extends TileEntity implements ITileEntitySyncable {

	private final TileEntityDataManager dataManager = new TileEntityDataManager(this);

	private final DataEntryString structureName = new DataEntryString("StructureName", "NoName", true);
	private final DataEntryInt startX = new DataEntryInt("StartX", 0, true) {
		@Override
		protected void onValueChanged() {
			super.onValueChanged();
			TileEntityExporter.this.onPositionsChanged();
		}
	};
	private final DataEntryInt startY = new DataEntryInt("StartY", 0, true) {
		@Override
		protected void onValueChanged() {
			super.onValueChanged();
			TileEntityExporter.this.onPositionsChanged();
		}
	};
	private final DataEntryInt startZ = new DataEntryInt("StartZ", 0, true) {
		@Override
		protected void onValueChanged() {
			super.onValueChanged();
			TileEntityExporter.this.onPositionsChanged();
		}
	};
	private final DataEntryInt endX = new DataEntryInt("EndX", 0, true) {
		@Override
		protected void onValueChanged() {
			super.onValueChanged();
			TileEntityExporter.this.onPositionsChanged();
		}
	};
	private final DataEntryInt endY = new DataEntryInt("EndY", 0, true) {
		@Override
		protected void onValueChanged() {
			super.onValueChanged();
			TileEntityExporter.this.onPositionsChanged();
		}
	};
	private final DataEntryInt endZ = new DataEntryInt("EndZ", 0, true) {
		@Override
		protected void onValueChanged() {
			super.onValueChanged();
			TileEntityExporter.this.onPositionsChanged();
		}
	};
	private final DataEntryBoolean relativeMode = new DataEntryBoolean("RelativeMode", true, true) {
		@Override
		protected void onValueChanged() {
			super.onValueChanged();
			TileEntityExporter.this.onPositionsChanged();
		}
	};
	private final DataEntryBoolean ignoreEntities = new DataEntryBoolean("IgnoreEntities", true, true) {
		@Override
		protected void onValueChanged() {
			super.onValueChanged();
			TileEntityExporter.this.onPositionsChanged();
		}
	};
	private final DataEntryObject<BlockPos[]> unprotectedBlocks = new DataEntryObject<BlockPos[]>("UnprotectedBlocks", new BlockPos[0], true) {
		@Override
		public INBT write() {
			int[] data = new int[this.value.length * 3];
			for (int i = 0; i < this.value.length; i++) {
				data[i * 3] = this.value[i].getX();
				data[i * 3 + 1] = this.value[i].getY();
				data[i * 3 + 2] = this.value[i].getZ();
			}
			return new IntArrayNBT(data);
		}

		@Override
		protected void readInternal(INBT nbt) {
			if (nbt instanceof IntArrayNBT) {
				int[] data = ((IntArrayNBT) nbt).getAsIntArray();
				this.value = new BlockPos[data.length / 3];
				for (int i = 0; i < this.value.length; i++) {
					this.value[i] = new BlockPos(data[i * 3], data[i * 3 + 1], data[i * 3 + 2]);
				}
			}
		}

		@Override
		public void writeChanges(PacketBuffer buf) {
			buf.writeInt(this.value.length);
			for (BlockPos pos : this.value) {
				buf.writeBlockPos(pos);
			}
		}

		@Override
		protected void readChangesInternal(PacketBuffer buf) {
			this.value = new BlockPos[buf.readInt()];
			for (int i = 0; i < this.value.length; i++) {
				this.value[i] = buf.readBlockPos();
			}
		}
	};

	private final BlockPos.Mutable minPos = new BlockPos.Mutable();
	private final BlockPos.Mutable maxPos = new BlockPos.Mutable();
	private final BlockPos.Mutable minPosRelative = new BlockPos.Mutable();
	private final BlockPos.Mutable maxPosRelative = new BlockPos.Mutable();

	public TileEntityExporter() {
		super(CQRBlockEntities.EXPORTER.get());
		this.dataManager.register(this.structureName);
		this.dataManager.register(this.startX);
		this.dataManager.register(this.startY);
		this.dataManager.register(this.startZ);
		this.dataManager.register(this.endX);
		this.dataManager.register(this.endY);
		this.dataManager.register(this.endZ);
		this.dataManager.register(this.relativeMode);
		this.dataManager.register(this.ignoreEntities);
		this.dataManager.register(this.unprotectedBlocks);
	}

	@Override
	public TileEntityDataManager getDataManager() {
		return this.dataManager;
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		this.dataManager.write(compound);
		return compound;
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		this.dataManager.read(compound);
		this.onPositionsChanged();
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.worldPosition, 0, this.dataManager.write(new CompoundNBT()));
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return this.save(new CompoundNBT());
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		this.dataManager.read(pkt.getTag());
		this.onPositionsChanged();
	}

	@Override
	public void setPosition(BlockPos posIn) {
		boolean flag = !this.worldPosition.equals(posIn);

		super.setPosition(posIn);

		if (flag) {
			this.onPositionsChanged();
		}
	}

	private void onPositionsChanged() {
		int x1 = Math.min(this.startX.getInt(), this.endX.getInt());
		int y1 = Math.min(this.startY.getInt(), this.endY.getInt());
		int z1 = Math.min(this.startZ.getInt(), this.endZ.getInt());
		int x2 = Math.max(this.startX.getInt(), this.endX.getInt());
		int y2 = Math.max(this.startY.getInt(), this.endY.getInt());
		int z2 = Math.max(this.startZ.getInt(), this.endZ.getInt());
		if (this.relativeMode.getBoolean()) {
			this.minPosRelative.set(x1, y1, z1);
			this.maxPosRelative.set(x2, y2, z2);
			x1 += this.worldPosition.getX();
			y1 += this.worldPosition.getY();
			z1 += this.worldPosition.getZ();
			x2 += this.worldPosition.getX();
			y2 += this.worldPosition.getY();
			z2 += this.worldPosition.getZ();
			this.minPos.set(x1, y1, z1);
			this.maxPos.set(x2, y2, z2);
		} else {
			this.minPos.set(x1, y1, z1);
			this.maxPos.set(x2, y2, z2);
			x1 -= this.worldPosition.getX();
			y1 -= this.worldPosition.getY();
			z1 -= this.worldPosition.getZ();
			x2 -= this.worldPosition.getX();
			y2 -= this.worldPosition.getY();
			z2 -= this.worldPosition.getZ();
			this.minPosRelative.set(x1, y1, z1);
			this.maxPosRelative.set(x2, y2, z2);
		}
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}
	
	@Override
	public double getViewDistance() {
		double d = Minecraft.getInstance().gameRenderer.getRenderDistance() * 16.0D;
		return d * d;
	}

	public void saveStructure(PlayerEntity author) {
		if (this.level == null) {
			return;
		}
		if (!this.level.isClientSide) {
			CQRMain.logger.info("Server is saving structure...");
			CQStructure structure = CQStructure.createFromWorld(this.level, this.minPos, this.maxPos, this.ignoreEntities.getBoolean(), Arrays.asList(this.unprotectedBlocks.get()), author.getName().toString());
			new Thread(() -> {
				if (structure.writeToFile(new File(CQRMain.CQ_EXPORT_FILES_FOLDER, this.structureName.get() + ".nbt"))) {
					author.sendMessage(new StringTextComponent("Successfully exported structure: " + this.structureName.get()), null);
				} else {
					author.sendMessage(new StringTextComponent("Failed to export structure: " + this.structureName.get()), null);
				}
			}, "CQR Export Thread").start();
		} else {
			this.dataManager.checkIfDirtyAndSync();
			CQRMain.NETWORK.sendToServer(new CPacketSaveStructureRequest(this.worldPosition));
		}
	}

	public void setValues(String structName, BlockPos startPos, BlockPos endPos, boolean useRelativeMode, boolean ignoreEntities, BlockPos[] unprotectedBlocks) {
		this.setValues(structName, startPos.getX(), startPos.getY(), startPos.getZ(), endPos.getX(), endPos.getY(), endPos.getZ(), useRelativeMode, ignoreEntities, unprotectedBlocks);
	}

	public void setValues(String structName, int sX, int sY, int sZ, int eX, int eY, int eZ, boolean useRelativeMode, boolean ignoreEntities, BlockPos[] unprotectedBlocks) {
		this.structureName.set(structName);
		this.startX.set(sX);
		this.startY.set(sY);
		this.startZ.set(sZ);
		this.endX.set(eX);
		this.endY.set(eY);
		this.endZ.set(eZ);
		this.relativeMode.set(useRelativeMode);
		this.ignoreEntities.set(ignoreEntities);
		this.unprotectedBlocks.set(unprotectedBlocks);
	}

	public String getStructureName() {
		return this.structureName.get();
	}

	public int getStartX() {
		return this.startX.getInt();
	}

	public int getStartY() {
		return this.startY.getInt();
	}

	public int getStartZ() {
		return this.startZ.getInt();
	}

	public int getEndX() {
		return this.endX.getInt();
	}

	public int getEndY() {
		return this.endY.getInt();
	}

	public int getEndZ() {
		return this.endZ.getInt();
	}

	public boolean isIgnoreEntities() {
		return this.ignoreEntities.getBoolean();
	}

	public boolean isRelativeMode() {
		return this.relativeMode.getBoolean();
	}

	public void setUnprotectedBlocks(BlockPos[] unprotectedBlocks) {
		this.unprotectedBlocks.set(unprotectedBlocks);
	}

	public BlockPos[] getUnprotectedBlocks() {
		return this.unprotectedBlocks.get();
	}

	public BlockPos.Mutable getMinPos() {
		return this.minPos;
	}

	public BlockPos.Mutable getMaxPos() {
		return this.maxPos;
	}

	public BlockPos getMinPosRelative() {
		return this.minPosRelative;
	}

	public BlockPos getMaxPosRelative() {
		return this.maxPosRelative;
	}

}
