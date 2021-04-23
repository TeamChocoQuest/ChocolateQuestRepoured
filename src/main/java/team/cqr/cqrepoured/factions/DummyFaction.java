package team.cqr.cqrepoured.factions;

import java.util.Optional;

import net.minecraft.entity.Entity;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;

public class DummyFaction extends CQRFaction {

	public static final String NAME = "DUMMY";

	public DummyFaction() {
		super(NAME, null, EReputationState.NEUTRAL, false, false, Optional.of(0), Optional.of(0), Optional.of(0));
	}

	@Override
	public void addAlly(CQRFaction ally) {

	}

	@Override
	public void addEnemy(CQRFaction enemy) {

	}

	@Override
	public boolean isEnemy(Entity ent) {
		return false;
	}

	@Override
	public boolean isEnemy(AbstractEntityCQR ent) {
		return false;
	}

	@Override
	public boolean isEnemy(CQRFaction faction) {
		return false;
	}

	@Override
	public boolean isAlly(Entity ent) {
		return false;
	}

	@Override
	public boolean isAlly(AbstractEntityCQR ent) {
		return false;
	}

	@Override
	public boolean isAlly(CQRFaction faction) {
		return false;
	}

}
