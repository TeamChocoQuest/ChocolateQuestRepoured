package com.example.chocolatequest.faction;

public interface IFactionRelated {

    EDefaultFaction getDefaultFaction();

    default EDefaultFaction getFaction() {
        return getDefaultFaction();
    }

    default boolean isAllyOf(IFactionRelated other) {
        return getFaction().isAlly(other.getFaction());
    }

    default boolean isEnemyOf(IFactionRelated other) {
        return getFaction().isEnemy(other.getFaction());
    }
}
