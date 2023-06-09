package team.cqr.cqrepoured.init;

import net.minecraft.tags.Tag;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.BlockTags;
import team.cqr.cqrepoured.CQRMain;

public class CQRBlockTags {

	public static final Tag.INamedTag<Block> SPIDER_WEBS = BlockTags.bind(CQRMain.MODID + ":spider_webs");
	public static final Tag.INamedTag<Block> HOMING_ENDER_EYE_DESTROYABLE = BlockTags.bind(CQRMain.MODID + ":ender_eye_destroyable");
	
}
