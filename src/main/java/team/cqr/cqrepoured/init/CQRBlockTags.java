package team.cqr.cqrepoured.init;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import team.cqr.cqrepoured.CQRMain;

public class CQRBlockTags {

	public static final TagKey<Block> SPIDER_WEBS = BlockTags.create(CQRMain.prefix("spider_webs"));
	public static final TagKey<Block> HOMING_ENDER_EYE_DESTROYABLE = BlockTags.create(CQRMain.prefix("ender_eye_destroyable"));
	public static final TagKey<Block> CONDUCTING_BLOCKS = BlockTags.create(CQRMain.prefix("conducting"));
	public static final TagKey<Block> STRUCTURE_EXPORT_IGNORE = BlockTags.create(CQRMain.prefix("structure_export_ignore"));
	
	// Protected regions
	public static final TagKey<Block> PROTECTED_REGIONS_BREAKABLE_WHITELIST = BlockTags.create(CQRMain.prefix("protection_system/breakable"));
	public static final TagKey<Block> PROTECTED_REGIONS_PLACABLE_WHITELIST = BlockTags.create(CQRMain.prefix("protection_system/placable"));
	
}
