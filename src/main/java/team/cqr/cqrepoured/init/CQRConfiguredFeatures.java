package team.cqr.cqrepoured.init;

import net.minecraft.client.gui.NewChatGui;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.IFeatureConfig;

public class CQRConfiguredFeatures {

	public static ConfiguredFeature<?, ?> CONFIGURED_WALL_IN_THE_NORTH = CQRWorldGenFeatures.WALL_IN_THE_NORTH.get().configured();
	
}
