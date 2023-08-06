package team.cqr.cqrepoured.init;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import team.cqr.cqrepoured.CQRMain;

public class CQRBlockTags {

	public static final TagKey<Block> SPIDER_WEBS = BlockTags.create(CQRMain.prefix("spider_webs"));
	public static final TagKey<Block> HOMING_ENDER_EYE_DESTROYABLE = BlockTags.create(CQRMain.prefix("ender_eye_destroyable"));
	public static final TagKey<Block> CONDUCTING_BLOCKS = BlockTags.create(CQRMain.prefix("conducting"));
	
}
