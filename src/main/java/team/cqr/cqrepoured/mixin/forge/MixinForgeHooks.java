package team.cqr.cqrepoured.mixin.forge;

import java.util.Deque;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.Queues;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTableManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ForgeHooks.LootTableContext;
import team.cqr.cqrepoured.event.events.ModdedLootTableLoadEvent;

@Mixin(ForgeHooks.class)
public abstract class MixinForgeHooks {
	
	@Accessor(value = "lootContext", remap = false)
	public static ThreadLocal<Deque<LootTableContext>> getLootContext() {
		throw new AssertionError();
	}
	
	@Inject(
			method = "loadLootTable(Lcom/google/gson/Gson;Lnet/minecraft/util/ResourceLocation;Lcom/google/gson/JsonElement;ZLnet/minecraft/loot/LootTableManager;)Lnet/minecraft/loot/LootTable;",
			at = @At("HEAD"),
			cancellable = true,
			remap = false
	)
	private static void mixinLoadLootTable(Gson gson, ResourceLocation name, JsonElement data, boolean custom, LootTableManager lootTableManager, CallbackInfoReturnable<LootTable> cir) {
		if(custom) {
			Deque<ForgeHooks.LootTableContext> que = getLootContext().get();
			if (que == null) {
				que = Queues.newArrayDeque();
				 getLootContext().set(que);
			}

			LootTable ret = null;
			try {
				que.push(new LootTableContext(name, custom));
				ret = gson.fromJson(data, LootTable.class);
				ret.setLootTableId(name);
				que.pop();
			} catch (JsonParseException e) {
				que.pop();
				throw e;
			}

			ret = ModdedLootTableLoadEvent.fireModdedLoottableLoadEvent(name, ret, lootTableManager);
						
			if (ret != null)
				ret.freeze();

			cir.setReturnValue(ret);
			cir.cancel();
		}
	}

	/*@Nullable
	@Overwrite
	public static LootTable loadLootTable(Gson gson, ResourceLocation name, JsonElement data, boolean custom, LootTableManager lootTableManager) {
		Deque<ForgeHooks.LootTableContext> que = getLootContext().get();
		if (que == null) {
			que = Queues.newArrayDeque();
			ForgeHooks.lootContext.set(que);
		}

		LootTable ret = null;
		try {
			que.push(new LootTableContext(name, custom));
			ret = gson.fromJson(data, LootTable.class);
			ret.setLootTableId(name);
			que.pop();
		} catch (JsonParseException e) {
			que.pop();
			throw e;
		}

		if (!custom) {
			ret = ForgeEventFactory.loadLootTable(name, ret, lootTableManager);
		} else {
			ret = fireModdedLoottableLoadEvent(name, ret, lootTableManager);
		}
					
		if (ret != null)
			ret.freeze();

		return ret;
	}*/

}
