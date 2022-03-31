package team.cqr.cqrepoured.client.gui;

import java.util.Objects;
import java.util.function.BooleanSupplier;

import javax.annotation.Nullable;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import team.cqr.cqrepoured.CQRMain;

public class NumberTextField<T extends Number & Comparable<T>> extends TextFieldWidget {

	public interface NumberParser<T extends Number> {

		T parse(String s) throws NumberFormatException;

	}

	private final T def;
	@Nullable
	private final T min;
	@Nullable
	private final T max;
	private final NumberParser<T> parser;
	private boolean isValidating;

	public NumberTextField(FontRenderer font, int x, int y, int width, int height, T def, @Nullable T min,
			@Nullable T max, NumberParser<T> parser) {
		super(font, x, y, width, height, null);
		this.def = Objects.requireNonNull(def);
		this.min = min;
		this.max = max;
		this.parser = Objects.requireNonNull(parser);
		if (min != null && def.compareTo(min) < 0)
			throw new IllegalArgumentException();
		if (max != null && def.compareTo(max) > 0)
			throw new IllegalArgumentException();
		if (min != null && max != null && min.compareTo(max) > 0)
			throw new IllegalArgumentException();
		this.setFilter(s -> {
			if (isValidating)
				return true;
			try {
				T number = parser.parse(s);
				if (min != null && number.compareTo(min) < 0)
					return false;
				if (max != null && number.compareTo(max) > 0)
					return false;
				return true;
			} catch (NumberFormatException e) {
				return false;
			}
		});
		this.setNumber(def);
	}

	public static NumberTextField<Integer> integerTextField(FontRenderer font, int x, int y, int width, int height,
			Integer def, @Nullable Integer min, @Nullable Integer max) {
		return new NumberTextField<>(font, x, y, width, height, def, min, max, Integer::parseInt);
	}

	public static NumberTextField<Integer> integerTextField(FontRenderer font, int x, int y, int width, int height,
			Integer def) {
		return integerTextField(font, x, y, width, height, def, null, null);
	}

	public static NumberTextField<Integer> positiveIntegerTextField(FontRenderer font, int x, int y, int width,
			int height, Integer def) {
		return integerTextField(font, x, y, width, height, def, 0, null);
	}

	public static NumberTextField<Double> doubleTextField(FontRenderer font, int x, int y, int width, int height,
			Double def, @Nullable Double min, @Nullable Double max) {
		return new NumberTextField<>(font, x, y, width, height, def, min, max, Double::parseDouble);
	}

	public static NumberTextField<Double> doubleTextField(FontRenderer font, int x, int y, int width, int height,
			Double def) {
		return doubleTextField(font, x, y, width, height, def, null, null);
	}

	public static NumberTextField<Double> positiveDoubleTextField(FontRenderer font, int x, int y, int width,
			int height, Double def) {
		return doubleTextField(font, x, y, width, height, def, 0.0D, null);
	}

	@Override
	public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
		return validate(() -> super.keyPressed(pKeyCode, pScanCode, pModifiers));
	}

	@Override
	public boolean charTyped(char pCodePoint, int pModifiers) {
		return validate(() -> super.charTyped(pCodePoint, pModifiers));
	}

	protected boolean validate(BooleanSupplier superCall) {
		String oldText = this.getValue();
		int oldPos = this.getCursorPosition();
		try {
			isValidating = true;
			if (!superCall.getAsBoolean()) {
				return false;
			}
		} finally {
			isValidating = false;
		}
		String newText = this.getValue();
		int newPos = this.getCursorPosition();
		if (newText.isEmpty() || newText.length() == 1 && !Character.isDigit(newText.charAt(0))) {
			T zero = parser.parse("0");
			if (min != null && zero.compareTo(min) < 0)
				zero = min;
			if (max != null && zero.compareTo(max) > 0)
				zero = max;
			this.setValue(zero.toString());
			return true;
		}
		T newValue;
		try {
			newValue = parser.parse(newText);
		} catch (NumberFormatException e) {
			this.setValue(oldText);
			this.setCursorPosition(oldPos);
			return false;
		}
		if (min != null && newValue.compareTo(min) < 0) {
			newValue = min;
			this.setValue(newValue.toString());
			this.setCursorPosition(newPos);
		}
		if (max != null && newValue.compareTo(max) < 0) {
			newValue = max;
			this.setValue(newValue.toString());
			this.setCursorPosition(newPos);
		}
		return true;
	}

	/**
	 * @deprecated Use {@link #setNumber()} instead.
	 */
	@Deprecated
	@Override
	public void setValue(String pText) {
		super.setValue(pText);
	}

	/**
	 * @deprecated Use {@link #getNumber()} instead.
	 */
	@Deprecated
	@Override
	public String getValue() {
		return super.getValue();
	}

	public void setNumber(T t) {
		this.setValue(t.toString());
	}

	public T getNumber() {
		try {
			return parser.parse(this.getValue());
		} catch (NumberFormatException e) {
			CQRMain.logger.error("GUI: Failed parsing number!");
			return def;
		}
	}

}
