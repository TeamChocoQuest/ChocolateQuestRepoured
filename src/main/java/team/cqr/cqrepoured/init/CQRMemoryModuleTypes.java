package team.cqr.cqrepoured.init;

import java.util.List;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraftforge.registries.RegistryObject;
import team.cqr.cqrepoured.common.services.CQRServices;

public class CQRMemoryModuleTypes {
	
	public static void init() {}

	// Undead
	public static final RegistryObject<MemoryModuleType<Boolean>> BURIED = CQRServices.ENTITY_AI.registerMemoryModuleType("buried", Codec.BOOL);
	public static final RegistryObject<MemoryModuleType<List<BlockPos>>> GRAVE_SPOTS = CQRServices.ENTITY_AI.registerMemoryModuleType("grave_spot", BlockPos.CODEC.listOf());

}
