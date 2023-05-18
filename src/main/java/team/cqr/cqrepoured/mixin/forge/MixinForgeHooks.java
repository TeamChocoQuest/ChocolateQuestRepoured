package team.cqr.cqrepoured.mixin.forge;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.loot.LootTable;
import net.minecraftforge.common.ForgeHooks;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.world.structure.generation.lootchests.LootTableLoader;

@Mixin(ForgeHooks.class)
public abstract class MixinForgeHooks {

	@ModifyVariable(
			method = "loadLootTable", 
			remap = false, 
			at = @At(
					value = "INVOKE_ASSIGN",
					target = "Lnet/minecraftforge/event/ForgeEventFactory;loadLootTable(Lnet/minecraft/util/ResourceLocation;Lnet/minecraft/loot/LootTable;Lnet/minecraft/loot/LootTableManager;)Lnet/minecraft/loot/LootTable;",
					shift = Shift.BY, 
					by = 1
			), 
			index = 6, 
			ordinal = 0, 
			name = "ret"
	)
	private static LootTable loadCQRLootTable(LootTable lootTable) {
		if (lootTable.getLootTableId().getNamespace().equals(CQRMain.MODID)) {
			return LootTableLoader.fillLootTable(lootTable.getLootTableId(), lootTable);
		}
		return lootTable;
	}

}
