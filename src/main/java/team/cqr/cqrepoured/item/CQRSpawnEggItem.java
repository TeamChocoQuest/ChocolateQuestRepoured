package team.cqr.cqrepoured.item;

import java.util.function.Supplier;

import net.minecraft.entity.EntityType;
import net.minecraftforge.common.ForgeSpawnEggItem;

public class CQRSpawnEggItem extends ForgeSpawnEggItem {

	public CQRSpawnEggItem(Supplier<? extends EntityType<?>> type, int backgroundColor, int highlightColor, Properties props) {
		super(type, backgroundColor, highlightColor, props);
	}
	
	@Override
	public int getColor(int pTintIndex) {
		if(pTintIndex > 1) {
			return -1;
		}
		return super.getColor(pTintIndex);
	}

}
