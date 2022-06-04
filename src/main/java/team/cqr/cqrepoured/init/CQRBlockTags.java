package team.cqr.cqrepoured.init;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import team.cqr.cqrepoured.CQRMain;

public class CQRBlockTags {

	public static final ITag.INamedTag<Block> SPIDER_WEBS = BlockTags.bind(CQRMain.MODID + ":spider_webs");
	public static final ITag.INamedTag<Block> HOMING_ENDER_EYE_DESTROYABLE = BlockTags.bind(CQRMain.MODID + ":ender_eye_destroyable");
	
}
