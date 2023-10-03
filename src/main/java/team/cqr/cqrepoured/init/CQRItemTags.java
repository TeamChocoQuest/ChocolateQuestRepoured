package team.cqr.cqrepoured.init;

import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import team.cqr.cqrepoured.CQRMain;

public class CQRItemTags {

	public static final TagKey<Item> TORCH_IGNITERS = ItemTags.create(CQRMain.prefix("torch_igniters"));
	public static final TagKey<Item> SOUL_FIRE_EMITTERS = ItemTags.create(CQRMain.prefix("soul_fire_emitters"));
	public static final TagKey<Item> SHIELDS = ItemTags.create(CQRMain.prefix("shields"));
	
}
