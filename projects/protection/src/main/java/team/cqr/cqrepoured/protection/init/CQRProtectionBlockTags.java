package team.cqr.cqrepoured.protection.init;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import team.cqr.cqrepoured.common.CQRepoured;

public class CQRProtectionBlockTags {
	
	public static final TagKey<Block> PLACABLE_IN_ACTIVE_REGION = BlockTags.create(CQRepoured.prefix("protection/placable_whitelist"));
	public static final TagKey<Block> BREAKABLE_IN_ACTIVE_REGION = BlockTags.create(CQRepoured.prefix("protection/breakable_whitelist"));

}
