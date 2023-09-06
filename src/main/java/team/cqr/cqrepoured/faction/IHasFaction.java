package team.cqr.cqrepoured.faction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public interface IHasFaction {

	@Nullable
	public default Faction getFaction() {
		return this.getDefaultFactionInstance();
	}
	
	@Nonnull
	public Faction getDefaultFactionInstance();
	
	public default void setFaction(@Nonnull Faction faction) {
		this.setFaction(faction.getId());
	}
	public void setFaction(ResourceLocation newFaction);
	
	public default boolean hasFaction() {
		return this.getFaction() != null;
	}

	@Nullable
	public default Level getLevel() {
		if(this instanceof Entity) {
			return ((Entity)this).level();
		}
		return null;
	}
	
}
