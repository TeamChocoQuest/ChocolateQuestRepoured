package team.cqr.cqrepoured.magic;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nullable;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import team.cqr.cqrepoured.API.events.CQRMagicSystemRegisterSpellsEvent;

public class SpellRegistry {

	//Structure: Spell-Namespace: Spellname => Spell object
	private static final Map<String, Map<String, AbstractSpell>> SPELL_REGISTRY = new ConcurrentHashMap<>();

	public static void initialize() {
		Set<AbstractSpell> spells = new HashSet<>();
		CQRMagicSystemRegisterSpellsEvent registerEvent = new CQRMagicSystemRegisterSpellsEvent(spells);
		if (MinecraftForge.EVENT_BUS.post(registerEvent)) {
			for (AbstractSpell spell : spells) {
				try {
					registerSpell(spell.getSpellId(), spell);
				} catch (Exception ex) {
					// Ignore
				}
			}
		}
	}

	@Nullable
	public static AbstractSpell getSpell(ResourceLocation spellId) {
		if (SPELL_REGISTRY.containsKey(spellId.getNamespace())) {
			return SPELL_REGISTRY.get(spellId.getNamespace()).getOrDefault(spellId.getPath(), null);
		}
		return null;
	}

	private static void registerSpell(ResourceLocation spellId, AbstractSpell spell) {
		SPELL_REGISTRY.computeIfAbsent(spellId.getNamespace(), key -> new ConcurrentHashMap<>()).put(spellId.getPath(), spell);
	}

}
