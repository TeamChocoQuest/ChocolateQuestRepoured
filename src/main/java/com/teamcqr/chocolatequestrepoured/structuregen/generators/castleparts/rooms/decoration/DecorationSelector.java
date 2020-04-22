package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration;

import com.teamcqr.chocolatequestrepoured.util.CQRWeightedRandom;

import java.util.Random;

public class DecorationSelector {
	private CQRWeightedRandom<IRoomDecor> edgeDecor;
	private CQRWeightedRandom<IRoomDecor> midDecor;

	public DecorationSelector(Random random) {
		this.edgeDecor = new CQRWeightedRandom<>(random);
		this.midDecor = new CQRWeightedRandom<>(random);
	}

	public void registerEdgeDecor(IRoomDecor decor, int weight) {
		this.edgeDecor.add(decor, weight);
	}

	public void registerMidDecor(IRoomDecor decor, int weight) {
		this.midDecor.add(decor, weight);
	}

	public boolean edgeDecorRegistered() {
		return this.edgeDecor.numItems() > 0;
	}

	public boolean midDecorRegistered() {
		return this.midDecor.numItems() > 0;
	}

	public IRoomDecor randomEdgeDecor() {
		return this.edgeDecor.next();
	}

	public IRoomDecor randomMidDecor() {
		return this.midDecor.next();
	}
}
