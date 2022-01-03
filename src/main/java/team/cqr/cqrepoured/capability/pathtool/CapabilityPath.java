package team.cqr.cqrepoured.capability.pathtool;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants;
import team.cqr.cqrepoured.entity.pathfinding.Path;

public class CapabilityPath {

	private final ItemStack stack;
	private final Path path = new Path() {
		@Override
		public void onPathChanged() {
			super.onPathChanged();
			CapabilityPath.this.writeToStack();
		}
	};
	private Path.PathNode selectedNode;
	private boolean readFromStack = false;
	private boolean isReading = false;

	public CapabilityPath(ItemStack stack) {
		this.stack = stack;
	}

	public Path getPath() {
		this.readFromStack();
		return this.path;
	}

	public void setSelectedNode(Path.PathNode selectedNode) {
		this.selectedNode = selectedNode;
		this.writeToStack();
	}

	public Path.PathNode getSelectedNode() {
		this.readFromStack();
		return this.selectedNode;
	}

	private void writeToStack() {
		if (this.isReading) {
			return;
		}
		CompoundNBT tag = this.stack.getTag();
		if (tag == null) {
			tag = new CompoundNBT();
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
		CompoundNBT tag = this.stack.getTag();
		if (tag == null) {
			this.isReading = false;
			return;
		}
		if (tag.contains("path", Constants.NBT.TAG_COMPOUND)) {
			this.path.readFromNBT(tag.getCompound("path"));
		}
		if (tag.contains("selectedNode", Constants.NBT.TAG_INT)) {
			this.selectedNode = this.path.getNode(tag.getInt("selectedNode"));
		}
		this.isReading = false;
		this.writeToStack();
	}

}
