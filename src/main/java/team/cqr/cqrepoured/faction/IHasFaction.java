package team.cqr.cqrepoured.faction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.world.IWorld;

public interface IHasFaction {

	@Nullable
	public default Faction getFaction() {
		return this.getDefaultFactionInstance();
	}
	
	@Nonnull
	Faction getDefaultFactionInstance();
	
	public default void setFaction(@Nonnull Faction faction) {
		this.setFaction(faction.getName());
	}
	public void setFaction(String newFaction);
	
	public default boolean hasFaction() {
		return this.getFaction() != null;
	}

	@Nullable
	public default IWorld getLevel() {
		if(this instanceof Entity) {
			return ((Entity)this).level;
		}
		return null;
	}
	
}
