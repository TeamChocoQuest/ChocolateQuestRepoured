package team.cqr.cqrepoured.capability.itemhandler;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;

@AutoRegisterCapability
public interface CapabilitySerializableItemHandler extends IItemHandler, INBTSerializable<CompoundTag> {

}
