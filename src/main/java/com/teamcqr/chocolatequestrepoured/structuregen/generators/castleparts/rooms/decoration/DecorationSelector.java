package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration;

import java.util.Random;

import com.teamcqr.chocolatequestrepoured.util.CQRWeightedRandom;

public class DecorationSelector {
	private CQRWeightedRandom<IRoomDecor> edgeDecor;
	private CQRWeightedRandom<IRoomDecor> midDecor;

	public DecorationSelector(Random random) {
		this.edgeDecor = new CQRWeightedRandom<>(random);
		this.midDecor = new CQRWeightedRandom<>(random);
	}

	public void registerEdgeDecor(EnumRoomDecor decor, int weight) {
		this.edgeDecor.add(decor.createInstance(), weight);
	}

	public IRoomDecor randomEdgeDecor() {
		return this.edgeDecor.next();
	}
}
