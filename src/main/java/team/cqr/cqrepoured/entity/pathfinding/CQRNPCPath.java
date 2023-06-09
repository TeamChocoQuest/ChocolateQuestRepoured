package team.cqr.cqrepoured.entity.pathfinding;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntLists;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListTag;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.Constants;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.util.DungeonGenUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CQRNPCPath {

	private static final String VERSION = "1.0.0";
	private final List<PathNode> nodes = new ArrayList<>();

	public CompoundTag writeToNBT() {
		CompoundTag compound = new CompoundTag();
		compound.putString("version", VERSION);
		ListTag nbtTagList = new ListTag();
		for (PathNode node : this.nodes) {
			nbtTagList.add(node.writeToNBT());
		}
		compound.put("nodes", nbtTagList);
		return compound;
	}

	public void readFromNBT(CompoundTag compound) {
		this.nodes.clear();
		String s = compound.getString("version");
		if (!s.equals(VERSION)) {
			CQRMain.logger.warn("Reading path: Expected version {} but got {}", VERSION, s);
		}
		for (INBT nbt : compound.getList("nodes", Constants.NBT.TAG_COMPOUND)) {
			this.nodes.add(new PathNode(this, (CompoundTag) nbt));
		}
		this.onPathChanged();
	}

	public boolean addNode(PathNode rootNode, BlockPos pos, int waitingTimeMin, int waitingTimeMax, float waitingRotation, int weight, int timeMin, int timeMax, boolean bidirectional) {
		if (this.nodes.isEmpty()) {
			PathNode node = new PathNode(this, pos, waitingTimeMin, waitingTimeMax, waitingRotation, weight, timeMin, timeMax, this.nodes.size());
			this.nodes.add(node);
			this.onPathChanged();
			return true;
		} else if (rootNode != null && this.getNode(pos) == null) {
			PathNode node = new PathNode(this, pos, waitingTimeMin, waitingTimeMax, waitingRotation, weight, timeMin, timeMax, this.nodes.size());
			this.nodes.add(node);
			rootNode.addConnectedNode(node, bidirectional, false);
			this.onPathChanged();
			return true;
		}
		return false;
	}

	public boolean removeNode(PathNode node) {
		if (this.getNode(node.index) == node) {
			this.nodes.remove(node.index);
			for (PathNode otherNode : this.nodes) {
				otherNode.removeConnectedNode(node, false);
				otherNode.removeBlacklistedPrevNode(node, false);
			}
			for (PathNode otherNode : this.nodes) {
				if (otherNode.index > node.index) {
					otherNode.index--;
				}
				for (int i = 0; i < otherNode.connectedNodes.size(); i++) {
					if (otherNode.connectedNodes.getInt(i) > node.index) {
						otherNode.connectedNodes.set(i, otherNode.connectedNodes.getInt(i) - 1);
					}
				}
				for (int i = 0; i < otherNode.blacklistedPrevNodes.size(); i++) {
					if (otherNode.blacklistedPrevNodes.getInt(i) > node.index) {
						otherNode.blacklistedPrevNodes.set(i, otherNode.blacklistedPrevNodes.getInt(i) - 1);
					}
				}
			}
			this.onPathChanged();
			return true;
		}
		return false;
	}

	public void clear() {
		this.nodes.clear();
		this.onPathChanged();
	}

	public List<PathNode> getNodes() {
		return Collections.unmodifiableList(this.nodes);
	}

	public int getSize() {
		return this.nodes.size();
	}

	public boolean isEmpty() {
		return this.nodes.isEmpty();
	}

	@Nullable
	public PathNode getNode(int index) {
		if (index < 0 || index >= this.nodes.size()) {
			return null;
		}
		return this.nodes.get(index);
	}

	@Nullable
	public PathNode getNode(BlockPos pos) {
		for (PathNode node : this.nodes) {
			if (node.pos.equals(pos)) {
				return node;
			}
		}
		return null;
	}

	public void copyFrom(CQRNPCPath path) {
		this.copyFrom(path, BlockPos.ZERO);
	}

	public void copyFrom(CQRNPCPath path, BlockPos offset) {
		this.nodes.clear();
		for (PathNode node : path.nodes) {
			this.nodes.add(node.copy(this, offset));
		}
		this.onPathChanged();
	}

	public void onPathChanged() {

	}

	public class PathNode {

		private final CQRNPCPath path;
		private BlockPos pos;
		private int waitingTimeMin;
		private int waitingTimeMax;
		private float waitingRotation;
		private int weight;
		private int timeMin;
		private int timeMax;
		private int index;
		private final IntList connectedNodes = new IntArrayList();
		private final IntList blacklistedPrevNodes = new IntArrayList();

		private PathNode(CQRNPCPath path, BlockPos pos, int waitingTimeMin, int waitingTimeMax, float waitingRotation, int weight, int timeMin, int timeMax, int index) {
			this.path = path;
			this.pos = pos.immutable();
			this.waitingTimeMin = Mth.clamp(Math.min(waitingTimeMin, waitingTimeMax), 0, 24000);
			this.waitingTimeMax = Mth.clamp(Math.max(waitingTimeMin, waitingTimeMax), 0, 24000);
			this.waitingRotation = Mth.wrapDegrees(waitingRotation);
			this.weight = Mth.clamp(weight, 1, 10000);
			this.timeMin = Mth.clamp(timeMin, 0, 24000);
			this.timeMax = Mth.clamp(timeMax, 0, 24000);
			this.index = index;
		}

		private PathNode(CQRNPCPath path, CompoundTag compound) {
			this.path = path;
			this.pos = DungeonGenUtils.readPosFromList(compound.getList("pos", Constants.NBT.TAG_INT));
			this.waitingTimeMin = compound.getInt("waitingTimeMin");
			this.waitingTimeMax = compound.getInt("waitingTimeMax");
			this.waitingRotation = compound.getFloat("waitingRotation");
			this.weight = compound.getInt("weight");
			this.timeMin = compound.getInt("timeMin");
			this.timeMax = compound.getInt("timeMax");
			this.index = compound.getInt("index");
			this.connectedNodes.addElements(0, compound.getIntArray("connectedNodes"));
			this.blacklistedPrevNodes.addElements(0, compound.getIntArray("blacklistedPrevNodes"));
		}

		private CompoundTag writeToNBT() {
			CompoundTag compound = new CompoundTag();
			compound.put("pos", DungeonGenUtils.writePosToList(this.pos));
			compound.putInt("waitingTimeMin", this.waitingTimeMin);
			compound.putInt("waitingTimeMax", this.waitingTimeMax);
			compound.putFloat("waitingRotation", this.waitingRotation);
			compound.putInt("weight", this.weight);
			compound.putInt("timeMin", this.timeMin);
			compound.putInt("timeMax", this.timeMax);
			compound.putInt("index", this.index);
			compound.putIntArray("connectedNodes", this.connectedNodes.toIntArray());
			compound.putIntArray("blacklistedPrevNodes", this.blacklistedPrevNodes.toIntArray());
			return compound;
		}

		public boolean addConnectedNode(PathNode node, boolean bidirectional) {
			return this.addConnectedNode(node, bidirectional, true);
		}

		private boolean addConnectedNode(PathNode node, boolean bidirectional, boolean updateWhenChanged) {
			if (node == this) {
				return false;
			}
			boolean flag1 = this.connectedNodes.contains(node.index);
			boolean flag2 = !bidirectional || node.connectedNodes.contains(this.index);
			if (flag1 && flag2) {
				return false;
			}
			if (!flag1) {
				this.connectedNodes.add(node.index);
			}
			if (!flag2) {
				node.connectedNodes.add(this.index);
			}
			if (updateWhenChanged) {
				CQRNPCPath.this.onPathChanged();
			}
			return true;
		}

		public boolean removeConnectedNode(PathNode node) {
			return this.removeConnectedNode(node, true);
		}

		private boolean removeConnectedNode(PathNode node, boolean updateWhenChanged) {
			boolean flag = this.connectedNodes.rem(node.index);
			boolean flag1 = node.connectedNodes.rem(this.index);
			if (!flag && !flag1) {
				return false;
			}
			if (updateWhenChanged) {
				CQRNPCPath.this.onPathChanged();
			}
			return true;
		}

		public boolean addBlacklistedPrevNode(PathNode node) {
			return this.addBlacklistedPrevNode(node, true);
		}

		private boolean addBlacklistedPrevNode(PathNode node, boolean updateWhenChanged) {
			if (node == this) {
				return false;
			}
			boolean flag = false;
			for (int i : this.connectedNodes) {
				for (int j : this.path.nodes.get(i).connectedNodes) {
					if (j == node.index) {
						flag = true;
						break;
					}
				}
				if (flag) {
					break;
				}
			}
			if (!flag) {
				return false;
			}
			this.blacklistedPrevNodes.add(node.index);
			if (updateWhenChanged) {
				CQRNPCPath.this.onPathChanged();
			}
			return true;
		}

		public boolean removeBlacklistedPrevNode(PathNode node) {
			return this.removeBlacklistedPrevNode(node, true);
		}

		private boolean removeBlacklistedPrevNode(PathNode node, boolean updateWhenChanged) {
			if (!this.blacklistedPrevNodes.rem(node.index)) {
				return false;
			}
			if (updateWhenChanged) {
				CQRNPCPath.this.onPathChanged();
			}
			return true;
		}

		@Nullable
		public PathNode getNextNode(Level world, Random rand, @Nullable PathNode prevNode) {
			if (this.connectedNodes.isEmpty()) {
				return null;
			}
			long time = world.getGameTime() % 24000;
			int totalWeight = 0;
			int totalWeightIgnoreTime = 0;
			for (int connectedNode : this.connectedNodes) {
				PathNode node = this.path.nodes.get(connectedNode);
				if (prevNode != null && (node == prevNode || node.blacklistedPrevNodes.contains(prevNode.index))) {
					continue;
				}
				if (time < node.timeMin || time > node.timeMax) {
					continue;
				}
				totalWeight += node.weight;
			}
			if (totalWeight == 0 && totalWeightIgnoreTime == 0) {
				return prevNode;
			}
			int o = rand.nextInt(totalWeight);
			for (int connectedNode : this.connectedNodes) {
				PathNode node = this.path.nodes.get(connectedNode);
				if (prevNode != null && (node == prevNode || node.blacklistedPrevNodes.contains(prevNode.index))) {
					continue;
				}
				if (time < node.timeMin || time > node.timeMax) {
					continue;
				}
				o -= node.weight;
				if (o < 0) {
					return node;
				}
			}
			o = rand.nextInt(totalWeightIgnoreTime);
			for (int connectedNode : this.connectedNodes) {
				PathNode node = this.path.nodes.get(connectedNode);
				if (prevNode != null && (node == prevNode || node.blacklistedPrevNodes.contains(prevNode.index))) {
					continue;
				}
				o -= node.weight;
				if (o < 0) {
					return node;
				}
			}
			return prevNode;
		}

		public IntList getConnectedNodes() {
			return IntLists.unmodifiable(this.connectedNodes);
		}

		public int getSizeOfConnectedNodes() {
			return this.connectedNodes.size();
		}

		@Nullable
		public PathNode getConnectedNode(int index) {
			if (index < 0 || index >= this.connectedNodes.size()) {
				return null;
			}
			return this.path.nodes.get(this.connectedNodes.getInt(index));
		}

		public void setPos(BlockPos pos) {
			this.pos = pos.immutable();
			CQRNPCPath.this.onPathChanged();
		}

		public BlockPos getPos() {
			return this.pos;
		}

		public void setWaitingTimeMin(int waitingTimeMin) {
			this.waitingTimeMin = waitingTimeMin;
			CQRNPCPath.this.onPathChanged();
		}

		public int getWaitingTimeMin() {
			return this.waitingTimeMin;
		}

		public void setWaitingTimeMax(int waitingTimeMax) {
			this.waitingTimeMax = waitingTimeMax;
			CQRNPCPath.this.onPathChanged();
		}

		public int getWaitingTimeMax() {
			return this.waitingTimeMax;
		}

		public void setWaitingRotation(float waitingRotation) {
			this.waitingRotation = waitingRotation;
			CQRNPCPath.this.onPathChanged();
		}

		public float getWaitingRotation() {
			return this.waitingRotation;
		}

		public void setWeight(int weight) {
			this.weight = weight;
			CQRNPCPath.this.onPathChanged();
		}

		public int getWeight() {
			return this.weight;
		}

		public void setTimeMin(int timeMin) {
			this.timeMin = timeMin;
			CQRNPCPath.this.onPathChanged();
		}

		public int getTimeMin() {
			return this.timeMin;
		}

		public void setTimeMax(int timeMax) {
			this.timeMax = timeMax;
			CQRNPCPath.this.onPathChanged();
		}

		public int getTimeMax() {
			return this.timeMax;
		}

		public int getIndex() {
			return this.index;
		}

		private PathNode copy(CQRNPCPath path, BlockPos offset) {
			BlockPos posNew = new BlockPos(
					this.pos.getX() + offset.getX(),
					this.pos.getY() + offset.getY(),
					this.pos.getZ() + offset.getZ()
			);
			PathNode copy = new PathNode(path, posNew, this.waitingTimeMin, this.waitingTimeMax, this.waitingRotation, this.weight, this.timeMin, this.timeMax, this.index);
			copy.connectedNodes.addAll(this.connectedNodes);
			return copy;
		}

	}

}
