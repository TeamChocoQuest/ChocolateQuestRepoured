package team.cqr.cqrepoured.data;

import java.util.stream.Stream;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryObject;
import team.cqr.cqrepoured.CQRMain;

@EventBusSubscriber(modid = CQRMain.MODID, bus = Bus.MOD)
public class CQRDataGenerators {

	@SubscribeEvent
	public static void onGatherDataEvent(GatherDataEvent event) {
		DataGenerator dataGenerator = event.getGenerator();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
		dataGenerator.addProvider(new CQRBlockStateProvider(dataGenerator, existingFileHelper));
		dataGenerator.addProvider(new CQRItemModelProvider(dataGenerator, existingFileHelper));
	}

	public static <T extends IForgeRegistryEntry<T>, R extends T> Stream<R> valuesOfClass(DeferredRegister<T> register, Class<R> clazz) {
		return register.getEntries().stream()
				.map(RegistryObject::get)
				.filter(v -> v.getClass() == clazz)
				.map(clazz::cast);
	}

	public static <T extends IForgeRegistryEntry<T>, R extends T> Stream<R> valuesOfType(DeferredRegister<T> register, Class<R> clazz) {
		return register.getEntries().stream()
				.map(RegistryObject::get)
				.filter(clazz::isInstance)
				.map(clazz::cast);
	}

    public static ResourceLocation blockLoc(ResourceLocation rl) {
    	if (rl.getPath().startsWith(ModelProvider.BLOCK_FOLDER)) {
    		return rl;
    	}
        return new ResourceLocation(rl.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + rl.getPath());
    }

    public static ResourceLocation itemLoc(ResourceLocation rl) {
    	if (rl.getPath().startsWith(ModelProvider.ITEM_FOLDER)) {
    		return rl;
    	}
        return new ResourceLocation(rl.getNamespace(), ModelProvider.ITEM_FOLDER + "/" + rl.getPath());
    }

	public static ResourceLocation extend(ResourceLocation rl, String suffix) {
		return new ResourceLocation(rl.getNamespace(), rl.getPath() + suffix);
	}

}
