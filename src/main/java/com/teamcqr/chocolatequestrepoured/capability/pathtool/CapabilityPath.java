package com.teamcqr.chocolatequestrepoured.capability.pathtool;

import com.teamcqr.chocolatequestrepoured.objects.entity.pathfinding.Path;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

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
		NBTTagCompound tag = this.stack.getTagCompound();
		if (tag == null) {
			tag = new NBTTagCompound();
			this.stack.setTagCompound(tag);
		}
		tag.setTag("path", this.path.writeToNBT());
		tag.setInteger("selectedNode", this.selectedNode != null ? this.selectedNode.getIndex() : -1);
	}

	private void readFromStack() {
		if (this.readFromStack) {
			return;
		}
		this.readFromStack = true;
		this.isReading = true;
		NBTTagCompound tag = this.stack.getTagCompound();
		if (tag == null) {
			this.isReading = false;
			return;
		}
		if (tag.hasKey("path", Constants.NBT.TAG_COMPOUND)) {
			this.path.readFromNBT(tag.getCompoundTag("path"));
		}
		if (tag.hasKey("selectedNode", Constants.NBT.TAG_INT)) {
			this.selectedNode = this.path.getNode(tag.getInteger("selectedNode"));
		}
		this.isReading = false;
		this.writeToStack();
	}

}
