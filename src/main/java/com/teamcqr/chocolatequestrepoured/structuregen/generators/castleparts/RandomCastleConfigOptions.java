package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts;

public class RandomCastleConfigOptions {

	public enum RoofType {
		TWO_SIDED(0),
		FOUR_SIDED(1),
		SPIRE (2);

		public final int value;

		RoofType(int valueIn) {
			this.value = valueIn;
		}
	}

	public enum WindowType {
		BASIC_GLASS, CROSS_GLASS, SQUARE_BARS, OPEN_SLIT;
	}
}
