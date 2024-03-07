package team.cqr.cqrepoured.common.reference;

import java.lang.ref.WeakReference;

import com.google.common.base.Supplier;

public class WeakReferenceLazyLoadField<T>  implements Supplier<T> {
	
	private WeakReference<T> value;
	private int lifetime;
	private long lastSet;
	private final Supplier<T> function;

	public WeakReferenceLazyLoadField(Supplier<T> function) {
		this.value = null;
		this.lifetime = -1;
		this.lastSet = System.currentTimeMillis();
		this.function = function;
	}

	public WeakReferenceLazyLoadField(Supplier<T> function, int lifetime) {
		this(function);
		this.lifetime = lifetime;
	}

	public void reset() {
		this.value = null;
	}

	public T get() {
		if (this.lifetime > 0 && System.currentTimeMillis() - this.lastSet >= (long) this.lifetime) {
			this.lastSet = System.currentTimeMillis();
			T functionObject = this.function.get();
			if (functionObject != null) {
				this.value = new WeakReference<T>(functionObject);
			}
		}

		if (this.value == null || this.value.get() == null) {
			T functionObject = this.function.get();
			if (functionObject != null) {
				this.value = new WeakReference<T>(functionObject);
			}
		}

		if (this.value != null) {
			return this.value.get();
		}
		return null;
	}
}
