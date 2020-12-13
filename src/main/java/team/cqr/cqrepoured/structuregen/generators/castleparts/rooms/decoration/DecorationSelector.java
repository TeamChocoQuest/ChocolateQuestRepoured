package team.cqr.cqrepoured.structuregen.generators.castleparts.rooms.decoration;

import java.util.Random;

import team.cqr.cqrepoured.util.CQRWeightedRandom;

public class DecorationSelector {
	private CQRWeightedRandom<IRoomDecor> edgeDecor;
	private CQRWeightedRandom<IRoomDecor> midDecor;

	public DecorationSelector() {
		this.edgeDecor = new CQRWeightedRandom<>();
		this.midDecor = new CQRWeightedRandom<>();
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

	public IRoomDecor randomEdgeDecor(Random rand) {
		return this.edgeDecor.next(rand);
	}

	public IRoomDecor randomMidDecor(Random rand) {
		return this.midDecor.next(rand);
	}
}
