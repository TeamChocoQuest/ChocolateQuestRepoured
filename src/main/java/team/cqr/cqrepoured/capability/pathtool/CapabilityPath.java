package team.cqr.cqrepoured.capability.pathtool;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import team.cqr.cqrepoured.entity.pathfinding.CQRNPCPath;

public class CapabilityPath implements ICapabilityPath {

	private final ItemStack stack;
	private final CQRNPCPath path = new CQRNPCPath() {
		@Override
		public void onPathChanged() {
			super.onPathChanged();
			CapabilityPath.this.writeToStack();
		}
	};
	private CQRNPCPath.PathNode selectedNode;
	private boolean readFromStack = false;
	private boolean isReading = false;

	public CapabilityPath(ItemStack stack) {
		this.stack = stack;
	}

	@Override
	public CQRNPCPath getPath() {
		this.readFromStack();
		return this.path;
	}

	@Override
	public void setSelectedNode(CQRNPCPath.PathNode selectedNode) {
		this.selectedNode = selectedNode;
		this.writeToStack();
	}

	@Override
	public CQRNPCPath.PathNode getSelectedNode() {
		this.readFromStack();
		return this.selectedNode;
	}

	private void writeToStack() {
		if (this.isReading) {
			return;
		}
		CompoundTag tag = this.stack.getTag();
		if (tag == null) {
			tag = new CompoundTag();
			this.stack.setTag(tag);
		}
		tag.put("path", this.path.writeToNBT());
		tag.putInt("selectedNode", this.selectedNode != null ? this.selectedNode.getIndex() : -1);
	}

	private void readFromStack() {
		if (this.readFromStack) {
			return;
		}
		this.readFromStack = true;
		this.isReading = true;
		CompoundTag tag = this.stack.getTag();
		if (tag == null) {
			this.isReading = false;
			return;
		}
		if (tag.contains("path", Tag.TAG_COMPOUND)) {
			this.path.readFromNBT(tag.getCompound("path"));
		}
		if (tag.contains("selectedNode", Tag.TAG_INT)) {
			this.selectedNode = this.path.getNode(tag.getInt("selectedNode"));
		}
		this.isReading = false;
		this.writeToStack();
	}

	@Override
	public CompoundTag serializeNBT() {
		if (this.path != null) {
			return this.path.writeToNBT();
		}
		return new CompoundTag();
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		if (nbt.isEmpty()) {
			return;
		}
		if (this.path == null) {
			throw new IllegalStateException("path can not be null!");
			return;
		}
		this.path.readFromNBT(nbt);
	}

}
