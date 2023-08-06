package team.cqr.cqrepoured.init;

import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import team.cqr.cqrepoured.CQRMain;

public class CQRFluidTags {
	
	public static final TagKey<Fluid> CONDUCTING_FLUIDS = FluidTags.create(CQRMain.prefix("conducting"));

}
