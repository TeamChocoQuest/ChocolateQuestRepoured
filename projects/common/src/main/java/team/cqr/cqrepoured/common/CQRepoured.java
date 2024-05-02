package team.cqr.cqrepoured.common;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib.GeckoLib;

@Mod(CQRepoured.MODID)
public class CQRepoured implements IMainMod {
	
	public static final String MODID = "cqrepoured";
	public static final Logger LOGGER = LogManager.getLogger(MODID);
	public static final boolean isWorkspaceEnvironment = !CQRepoured.class.getResource("")
			.getProtocol()
			.equals("jar");

	public CQRepoured() {
		final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		
		GeckoLib.initialize();
		List<ISubProjectMain> children = this.getSubModMains();
		children.forEach(child -> {
			child.onModConstruction(bus);
			LOGGER.debug("Loaded child class " + child.getClass().getName());
		});
		children.forEach(child -> {
			child.registerEventHandlerObjects(MinecraftForge.EVENT_BUS::register);
			child.getEventListenersToReigster().forEach(listener -> {
				bus.addListener(listener);
			});
		});
	}
	
	
	
	
	
	public static final ResourceLocation prefix(final String path) {
		return new ResourceLocation(MODID, path);
	}
	
	/*public static final ResourceLocation prefixStructureTemplateId(final String path) {
		return new ResourceLocation(MODID_STRUCTURES, path);
	}*/
	
	static final String ANIMATION_SUFFIX = ".animation.json";
	public static final ResourceLocation prefixAnimation(final String path) {
		String pathToUse = path;
		if(!path.endsWith(ANIMATION_SUFFIX)) {
			pathToUse = path + ANIMATION_SUFFIX;
		}
		return new ResourceLocation(MODID, "animations/" + pathToUse);
	}
	
	public static final ResourceLocation prefixEntityAnimation(final String path) {
		return prefixAnimation("entity/" + path);
	}
	
	public static final ResourceLocation prefixBlockAnimation(final String path) {
		return prefixAnimation("block/" + path);
	}
	
	public static ResourceLocation prefixArmorAnimation(final String path) {
		return prefixAnimation("armor/" + path);
	}

	public static ResourceLocation prefixAssesEnforcementManager(String string) {
		return prefix("asset_manager/" + string);
	}

	public static ResourceLocation prefixAssetFinder(String string) {
		return prefix("asset_finder/" + string);
	}
	
}
