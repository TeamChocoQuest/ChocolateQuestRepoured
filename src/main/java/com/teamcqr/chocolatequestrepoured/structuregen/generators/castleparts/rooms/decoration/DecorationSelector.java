package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration;

import com.teamcqr.chocolatequestrepoured.util.WeightedRandom;

import java.util.Random;

public class DecorationSelector
{
    private WeightedRandom<IRoomDecor> edgeDecor;
    private WeightedRandom<IRoomDecor> midDecor;

    public DecorationSelector(Random random)
    {
        this.edgeDecor = new WeightedRandom<>(random);
        this.midDecor = new WeightedRandom<>(random);
    }

    public void registerEdgeDecor(EnumRoomDecor decor, int weight)
    {
        edgeDecor.add(decor.createInstance(), weight);
    }

    public IRoomDecor randomEdgeDecor()
    {
        return edgeDecor.next();
    }
}
