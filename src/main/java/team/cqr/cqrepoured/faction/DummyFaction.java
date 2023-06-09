package team.cqr.cqrepoured.faction;

import java.util.Optional;

import net.minecraft.world.entity.Entity;

public class DummyFaction extends Faction {

	public static final String NAME = "DUMMY";

	public DummyFaction() {
		super(NAME, null, EReputationState.NEUTRAL, false, false, Optional.of(0), Optional.of(0), Optional.of(0));
	}

	@Override
	public void addAlly(Faction ally) {

	}

	@Override
	public void addEnemy(Faction enemy) {

	}

	@Override
	public boolean isEnemy(Entity ent) {
		return false;
	}

	@Override
	public boolean isEnemy(IHasFaction ent) {
		return false;
	}

	@Override
	public boolean isEnemy(Faction faction) {
		return false;
	}

	@Override
	public boolean isAlly(Entity ent) {
		return false;
	}

	@Override
	public boolean isAlly(IHasFaction ent) {
		return false;
	}

	@Override
	public boolean isAlly(Faction faction) {
		return false;
	}

}
