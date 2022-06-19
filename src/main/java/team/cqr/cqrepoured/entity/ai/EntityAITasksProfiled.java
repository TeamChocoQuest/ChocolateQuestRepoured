package team.cqr.cqrepoured.entity.ai;

import java.util.Iterator;

import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.profiler.IProfiler;
import net.minecraft.world.World;
import team.cqr.cqrepoured.CQRMain;

public class EntityAITasksProfiled extends GoalSelector {

	/** Instance of Profiler. */
	private final IProfiler profiler;
	private int tickCount;
	private int tickRate = 3;

	/** CQR profiling */
	private static final Object2LongMap<Class<? extends Goal>> AI_TIMES = new Object2LongOpenHashMap<>();
	private static long lastTimeLogged = 0;
	private final World world;

	public EntityAITasksProfiled(IProfiler profilerIn, World world) {
		super(() -> profilerIn);
		this.profiler = profilerIn;
		this.world = world;
	}

	@Override
	public void tick() {
		this.profiler.push("goalSetup");

		if (this.tickCount++ % this.tickRate == 0) {
			for (PrioritizedGoal entityaitasks$entityaitaskentry : this.availableGoals) {
				long t = System.nanoTime();

				if (entityaitasks$entityaitaskentry.isRunning()) {
					if (!this.canUseCQR(entityaitasks$entityaitaskentry) || !this.canContinueCQR(entityaitasks$entityaitaskentry)) {
						entityaitasks$entityaitaskentry.using = false;
						entityaitasks$entityaitaskentry.getGoal().stop();
						this.executingTaskEntries.remove(entityaitasks$entityaitaskentry);
					}
				} else if (this.canUseCQR(entityaitasks$entityaitaskentry) && entityaitasks$entityaitaskentry.getGoal().canUse()) {
					entityaitasks$entityaitaskentry.using = true;
					entityaitasks$entityaitaskentry.getGoal().start();
					this.executingTaskEntries.add(entityaitasks$entityaitaskentry);
				}

				t = System.nanoTime() - t;
				t += AI_TIMES.getLong(entityaitasks$entityaitaskentry.getGoal().getClass());
				AI_TIMES.put(entityaitasks$entityaitaskentry.getGoal().getClass(), t);
			}
		} else {
			Iterator<PrioritizedGoal> iterator = this.getRunningGoals().iterator();

			while (iterator.hasNext()) {
				PrioritizedGoal entityaitasks$entityaitaskentry1 = iterator.next();

				long t = System.nanoTime();

				if (!this.canContinueCQR(entityaitasks$entityaitaskentry1)) {
					entityaitasks$entityaitaskentry1.using = false;
					entityaitasks$entityaitaskentry1.getGoal().stop();
					iterator.remove();
				}

				t = System.nanoTime() - t;
				t += AI_TIMES.getLong(entityaitasks$entityaitaskentry1.getGoal().getClass());
				AI_TIMES.put(entityaitasks$entityaitaskentry1.getGoal().getClass(), t);
			}
		}

		this.profiler.pop();

		if (this.getRunningGoals().count() > 0) {
			this.profiler.push("goalTick");

			this.getRunningGoals().forEach((entityaitasks$entityaitaskentry2) -> {
				long t = System.nanoTime();

				entityaitasks$entityaitaskentry2.getGoal().tick();

				t = System.nanoTime() - t;
				t += AI_TIMES.getLong(entityaitasks$entityaitaskentry2.getGoal().getClass());
				AI_TIMES.put(entityaitasks$entityaitaskentry2.getGoal().getClass(), t);
			});

			this.profiler.pop();
		}

		if (this.world.getGameTime() - lastTimeLogged > 200) {
			lastTimeLogged = this.world.getGameTime();

			StringBuilder sb = new StringBuilder("AI Times: \n");
			for (Object2LongMap.Entry<Class<? extends Goal>> entry : AI_TIMES.object2LongEntrySet()) {
				String s = entry.getKey().getSimpleName();
				sb.append(s);
				sb.append(':');
				sb.append(' ');
				int j = 40;
				while (s.length() < j) {
					j--;
					sb.append(' ');
				}
				double d = entry.getLongValue() / 1_000_000.0D;
				for (int i = 10; i <= 10_000; i *= 10) {
					if (d < i) {
						sb.append(' ');
					}
				}
				sb.append(String.format("%.4f", d));
				sb.append('m');
				sb.append('s');
				sb.append('\n');

				entry.setValue(0);
			}
			CQRMain.logger.info(sb);
		}
	}

	/**
	 * Determine if a specific AI Task should continue being executed.
	 */
	private boolean canContinueCQR(PrioritizedGoal taskEntry) {
		return taskEntry.canContinueToUse();
	}

	/**
	 * Determine if a specific AI Task can be executed, which means that all running higher (= lower int value) priority
	 * tasks are compatible with it or all lower priority tasks can be interrupted.
	 */
	private boolean canUseCQR(PrioritizedGoal taskEntry) {
		if (this.executingTaskEntries.isEmpty()) {
			return true;
		} else if (this.isControlFlagDisabled(taskEntry.getGoal().getMutexBits())) {
			return false;
		} else {
			for (GoalSelector.EntityAITaskEntry entityaitasks$entityaitaskentry : this.executingTaskEntries) {
				if (entityaitasks$entityaitaskentry != taskEntry) {
					if (taskEntry.priority >= entityaitasks$entityaitaskentry.priority) {
						if (!this.areTasksCompatibleCQR(taskEntry, entityaitasks$entityaitaskentry)) {
							return false;
						}
					} else if (!entityaitasks$entityaitaskentry.action.isInterruptable()) {
						return false;
					}
				}
			}

			return true;
		}
	}

	/**
	 * Returns whether two EntityAITaskEntries can be executed concurrently
	 */
	private boolean areTasksCompatibleCQR(PrioritizedGoal taskEntry1, PrioritizedGoal taskEntry2) {
		return (taskEntry1.getGoal().getMutexBits() & taskEntry2.getGoal().getMutexBits()) == 0;
	}

}
