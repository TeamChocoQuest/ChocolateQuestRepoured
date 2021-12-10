package team.cqr.cqrepoured.util.tool;

import java.text.DecimalFormat;

public class Progress {

	private static final DecimalFormat FORMAT = new DecimalFormat("0.0");
	private final int stages;
	private int stage;
	private double progress;
	private boolean errored;
	private boolean cancelled;

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

	public void setErrored() {
		this.errored = true;
	}

	public void setCancelled() {
		this.cancelled = true;
	}

	public int getStages() {
		return this.stages;
	}

	public int getStage() {
		return this.stage;
	}

	public double getProgress() {
		return this.progress;
	}

	public boolean isError() {
		return this.errored;
	}

	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public String toString() {
		if (this.stage == this.stages) {
			return "FINISHED";
		}
		if (this.errored) {
			return "ERROR";
		}
		if (this.cancelled) {
			return "CANCELLED";
		}
		return this.stage + "/" + this.stages + ": " + FORMAT.format(this.progress * 100) + "%";
	}

}
