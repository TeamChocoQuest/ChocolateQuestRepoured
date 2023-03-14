package team.cqr.cqrepoured.world.structure.debug;

import java.util.Arrays;

import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;

public class NoiseProfiler {

	public static final ThreadLocal<NoiseProfiler> INSTANCE = new ThreadLocal<>();
	public final LongList results = new LongArrayList();
	public long temp;

	public void start() {
		temp = System.nanoTime();
	}

	public void end() {
		results.add(System.nanoTime() - temp);
	}

	public String getData() {
		StringBuilder sb = new StringBuilder();
		sb.append("Timings (in microseconds): ");

		if (!results.isEmpty()) {
			if (results.size() <= 1) {
				sb.append(results.getLong(0) / 1000);
			} else {
				long[] data = results.toLongArray();
				Arrays.sort(data);

				sb.append(Arrays.stream(data).sum() / 1000).append(' ').append('(');
				sb.append("min=").append(data[0] / 1000).append(' ');
				sb.append("med=").append(data[(int) (data.length * 0.5)] / 1000).append(' ');
				sb.append("avg=").append(Arrays.stream(data).sum() / data.length / 1000).append(' ');
				sb.append("99th=").append(data[(int) (data.length * 0.99)] / 1000).append(' ');
				sb.append("99.9th=").append(data[(int) (data.length * 0.999)] / 1000).append(' ');
				sb.append("99.99th=").append(data[(int) (data.length * 0.9999)] / 1000).append(' ');
				sb.append("max=").append(data[data.length - 1] / 1000).append(')');
			}
		}

		sb.append('\n');
		return sb.toString();
	}

}
