package team.cqr.cqrepoured.util.tool;

import java.text.DecimalFormat;

public class Progress {

	private static final DecimalFormat FORMAT = new DecimalFormat("0.0");
	private final int stages;
	private int stage;
	private double progress;

	public Progress(int maxStage) {
		this.stages = maxStage;
	}

	public void finishStage() {
		if (this.stage >= this.stages) {
			throw new IllegalStateException();
		}
		this.stage++;
		this.progress = 0.0D;
	}

	public void setProgress(double progress) {
		this.progress = Math.max(progress, this.progress);
	}

	public int getStages() {
		return stages;
	}

	public int getStage() {
		return stage;
	}

	public double getProgress() {
		return progress;
	}

	@Override
	public String toString() {
		if (stage == stages) {
			return "FINISHED";
		}
		return stage + "/" + stages + ": " + FORMAT.format(progress * 100) + "%";
	}

}
