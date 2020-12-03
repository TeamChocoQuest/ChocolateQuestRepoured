package com.teamcqr.chocolatequestrepoured.objects.entity.pathfinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntLists;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class Path {

	private static final String VERSION = "1.0.0";
	private final List<PathNode> nodes = new ArrayList<>();

	public NBTTagCompound writeToNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setString("version", VERSION);
		NBTTagList nbtTagList = new NBTTagList();
		for (PathNode node : this.nodes) {
			nbtTagList.appendTag(node.writeToNBT());
		}
		compound.setTag("nodes", nbtTagList);
		return compound;
	}

	public void readFromNBT(NBTTagCompound compound) {
		this.nodes.clear();
		String s = compound.getString("version");
		if (!s.equals(VERSION)) {
			CQRMain.logger.warn("Reading path: Expected version {} but got {}", VERSION, s);
		}
		for (NBTBase nbt : compound.getTagList("nodes", Constants.NBT.TAG_COMPOUND)) {
			this.nodes.add(new PathNode(this, (NBTTagCompound) nbt));
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
					if (otherNode.connectedNodes.get(i) > node.index) {
						otherNode.connectedNodes.set(i, otherNode.connectedNodes.get(i) - 1);
					}
				}
				for (int i = 0; i < otherNode.blacklistedPrevNodes.size(); i++) {
					if (otherNode.blacklistedPrevNodes.get(i) > node.index) {
						otherNode.blacklistedPrevNodes.set(i, otherNode.blacklistedPrevNodes.get(i) - 1);
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

	public void copyFrom(Path path) {
		this.copyFrom(path, BlockPos.ORIGIN);
	}

	public void copyFrom(Path path, BlockPos offset) {
		this.nodes.clear();
		for (PathNode node : path.nodes) {
			this.nodes.add(node.copy(this, offset));
		}
		this.onPathChanged();
	}

	public void onPathChanged() {

	}

	public class PathNode {

		private final Path path;
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

		private PathNode(Path path, BlockPos pos, int waitingTimeMin, int waitingTimeMax, float waitingRotation, int weight, int timeMin, int timeMax, int index) {
			this.path = path;
			this.pos = pos.toImmutable();
			this.waitingTimeMin = MathHelper.clamp(Math.min(waitingTimeMin, waitingTimeMax), 0, 24000);
			this.waitingTimeMax = MathHelper.clamp(Math.max(waitingTimeMin, waitingTimeMax), 0, 24000);
			this.waitingRotation = MathHelper.wrapDegrees(waitingRotation);
			this.weight = MathHelper.clamp(weight, 1, 10000);
			this.timeMin = MathHelper.clamp(timeMin, 0, 24000);
			this.timeMax = MathHelper.clamp(timeMax, 0, 24000);
			this.index = index;
		}

		private PathNode(Path path, NBTTagCompound compound) {
			this.path = path;
			this.pos = DungeonGenUtils.readPosFromList(compound.getTagList("pos", Constants.NBT.TAG_INT));
			this.waitingTimeMin = compound.getInteger("waitingTimeMin");
			this.waitingTimeMax = compound.getInteger("waitingTimeMax");
			this.waitingRotation = compound.getFloat("waitingRotation");
			this.weight = compound.getInteger("weight");
			this.timeMin = compound.getInteger("timeMin");
			this.timeMax = compound.getInteger("timeMax");
			this.index = compound.getInteger("index");
			this.connectedNodes.addElements(0, compound.getIntArray("connectedNodes"));
			this.blacklistedPrevNodes.addElements(0, compound.getIntArray("blacklistedPrevNodes"));
		}

		private NBTTagCompound writeToNBT() {
			NBTTagCompound compound = new NBTTagCompound();
			compound.setTag("pos", DungeonGenUtils.writePosToList(this.pos));
			compound.setInteger("waitingTimeMin", this.waitingTimeMin);
			compound.setInteger("waitingTimeMax", this.waitingTimeMax);
			compound.setFloat("waitingRotation", this.waitingRotation);
			compound.setInteger("weight", this.weight);
			compound.setInteger("timeMin", this.timeMin);
			compound.setInteger("timeMax", this.timeMax);
			compound.setInteger("index", this.index);
			compound.setIntArray("connectedNodes", this.connectedNodes.toIntArray());
			compound.setIntArray("blacklistedPrevNodes", this.blacklistedPrevNodes.toIntArray());
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
				Path.this.onPathChanged();
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
				Path.this.onPathChanged();
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
				Path.this.onPathChanged();
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
				Path.this.onPathChanged();
			}
			return true;
		}

		@Nullable
		public PathNode getNextNode(World world, Random rand, @Nullable PathNode prevNode) {
			if (this.connectedNodes.isEmpty()) {
				return null;
			}
			long time = world.getWorldTime() % 24000;
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
			this.pos = pos.toImmutable();
			Path.this.onPathChanged();
		}

		public BlockPos getPos() {
			return this.pos;
		}

		public void setWaitingTimeMin(int waitingTimeMin) {
			this.waitingTimeMin = waitingTimeMin;
			Path.this.onPathChanged();
		}

		public int getWaitingTimeMin() {
			return this.waitingTimeMin;
		}

		public void setWaitingTimeMax(int waitingTimeMax) {
			this.waitingTimeMax = waitingTimeMax;
			Path.this.onPathChanged();
		}

		public int getWaitingTimeMax() {
			return this.waitingTimeMax;
		}

		public void setWaitingRotation(float waitingRotation) {
			this.waitingRotation = waitingRotation;
			Path.this.onPathChanged();
		}

		public float getWaitingRotation() {
			return this.waitingRotation;
		}

		public void setWeight(int weight) {
			this.weight = weight;
			Path.this.onPathChanged();
		}

		public int getWeight() {
			return this.weight;
		}

		public void setTimeMin(int timeMin) {
			this.timeMin = timeMin;
			Path.this.onPathChanged();
		}

		public int getTimeMin() {
			return this.timeMin;
		}

		public void setTimeMax(int timeMax) {
			this.timeMax = timeMax;
			Path.this.onPathChanged();
		}

		public int getTimeMax() {
			return this.timeMax;
		}

		public int getIndex() {
			return this.index;
		}

		private PathNode copy(Path path, BlockPos offset) {
			PathNode copy = new PathNode(path, this.pos.add(offset), this.waitingTimeMin, this.waitingTimeMax, this.waitingRotation, this.weight, this.timeMin, this.timeMax, this.index);
			copy.connectedNodes.addAll(this.connectedNodes);
			return copy;
		}

	}

}
