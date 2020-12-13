package team.cqr.cqrepoured.API.events;

import java.util.HashSet;
import java.util.Set;

import net.minecraftforge.fml.common.eventhandler.Event;
import team.cqr.cqrepoured.magic.AbstractSpell;

public class CQRMagicSystemRegisterSpellsEvent extends Event {
	
	private Set<AbstractSpell> spellsToRegister = new HashSet<>();
	
	public boolean register(AbstractSpell spell) {
		return this.spellsToRegister.add(spell);
	}
	
	public CQRMagicSystemRegisterSpellsEvent(Set<AbstractSpell> spellSet) {
		this.spellsToRegister = spellSet;
	}
	
	@Override
	public boolean isCancelable() {
		return false;
	}

}
