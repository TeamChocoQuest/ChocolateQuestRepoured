package team.cqr.cqrepoured.asm;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.common.launcher.FMLInjectionAndSortingTweaker;
import net.minecraftforge.fml.relauncher.CoreModManager;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import team.cqr.cqrepoured.asm.tweaker.CQRTweaker;

@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.TransformerExclusions({ "team.cqr.cqrepoured.asm", "meldexun.asmutil2" })
public class CQRPlugin implements IFMLLoadingPlugin {

	@SuppressWarnings("unchecked")
	public CQRPlugin() {
		try {
			if (((List<ITweaker>) Launch.blackboard.get("Tweaks")).stream().noneMatch(FMLInjectionAndSortingTweaker.class::isInstance)) {
				((List<String>) Launch.blackboard.get("TweakClasses")).add(CQRTweaker.class.getName());
			} else {
				((List<ITweaker>) Launch.blackboard.get("Tweaks")).add(new CQRTweaker());
			}
			Field _tweakSorting = CoreModManager.class.getDeclaredField("tweakSorting");
			_tweakSorting.setAccessible(true);
			((Map<String, Integer>) _tweakSorting.get(null)).put(CQRTweaker.class.getName(), 1001);
		} catch (ReflectiveOperationException e) {
			throw new UnsupportedOperationException(e);
		}
	}

	@Override
	public String[] getASMTransformerClass() {
		return null;
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		if (Boolean.FALSE.equals(data.get("runtimeDeobfuscationEnabled"))) {
			MixinBootstrap.init();
			MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
			CoreModManager.getIgnoredMods().add("mixin-0.8.5.jar");
			CoreModManager.getIgnoredMods().add("asm-util-6.2.jar");
			CoreModManager.getIgnoredMods().add("asm-analysis-6.2.jar");
			CoreModManager.getIgnoredMods().add("asm-tree-6.2.jar");
			CoreModManager.getIgnoredMods().add("asm-6.2.jar");
		}
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

}
