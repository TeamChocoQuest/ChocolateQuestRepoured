package team.cqr.cqrepoured.capability.pathtool;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;
import team.cqr.cqrepoured.entity.pathfinding.CQRNPCPath;

@AutoRegisterCapability
public interface ICapabilityPath extends INBTSerializable<CompoundTag>{

	CQRNPCPath getPath();

	void setSelectedNode(CQRNPCPath.PathNode selectedNode);

	CQRNPCPath.PathNode getSelectedNode();

}