package team.cqr.cqrepoured.asm.tweaker;

import java.io.File;
import java.util.List;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;
import team.cqr.cqrepoured.asm.CQRClassTransformer;

public class CQRTweaker implements ITweaker {

	@Override
	public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {

	}

	@Override
	public void injectIntoClassLoader(LaunchClassLoader classLoader) {
		classLoader.registerTransformer(CQRClassTransformer.class.getName());
	}

	@Override
	public String getLaunchTarget() {
		throw new RuntimeException("Invalid for use as a primary tweaker");
	}

	@Override
	public String[] getLaunchArguments() {
		return new String[0];
	}

}
