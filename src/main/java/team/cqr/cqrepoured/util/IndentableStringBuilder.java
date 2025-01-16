package team.cqr.cqrepoured.util;

public class IndentableStringBuilder implements CharSequence {

	private final StringBuilder stringBuilder;
	private int indentation;
	private boolean newLine = true;

	public IndentableStringBuilder() {
		this.stringBuilder = new StringBuilder();
	}

	public IndentableStringBuilder(int capacity) {
		this.stringBuilder = new StringBuilder(capacity);
	}

	@Override
	public String toString() {
		return this.stringBuilder.toString();
	}

	@Override
	public int length() {
		return this.stringBuilder.length();
	}

	@Override
	public char charAt(int index) {
		return this.stringBuilder.charAt(index);
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		return this.stringBuilder.subSequence(start, end);
	}

	public IndentableStringBuilder append(boolean b) {
		this.stringBuilder.append(b);
		this.newLine = false;
		return this;
	}

	public IndentableStringBuilder append(int i) {
		this.stringBuilder.append(i);
		this.newLine = false;
		return this;
	}

	public IndentableStringBuilder append(long lng) {
		this.stringBuilder.append(lng);
		this.newLine = false;
		return this;
	}

	public IndentableStringBuilder append(float f) {
		this.stringBuilder.append(f);
		this.newLine = false;
		return this;
	}

	public IndentableStringBuilder append(double d) {
		this.stringBuilder.append(d);
		this.newLine = false;
		return this;
	}

	public IndentableStringBuilder append(char c) {
		this.stringBuilder.append(c);
		this.newLine = false;
		return this;
	}

	public IndentableStringBuilder append(CharSequence s) {
		this.stringBuilder.append(s);
		this.newLine = false;
		return this;
	}

	public IndentableStringBuilder append(CharSequence s, int start, int end) {
		this.stringBuilder.append(s, start, end);
		this.newLine = false;
		return this;
	}

	public IndentableStringBuilder append(Object obj) {
		this.stringBuilder.append(obj);
		this.newLine = false;
		return this;
	}

	public IndentableStringBuilder append(String format, Object... args) {
		this.stringBuilder.append(String.format(format, args));
		this.newLine = false;
		return this;
	}

	public IndentableStringBuilder incrementIndentation() {
		this.indentation++;
		if (this.newLine) {
			this.stringBuilder.append(' ');
		}
		return this;
	}

	public IndentableStringBuilder decrementIndentation() {
		if (this.indentation <= 0) {
			throw new IndexOutOfBoundsException();
		}
		this.indentation--;
		if (this.newLine) {
			this.stringBuilder.deleteCharAt(this.stringBuilder.length() - 1);
		}
		return this;
	}

	public IndentableStringBuilder newLine() {
		this.stringBuilder.append('\n');
		for (int i = 0; i < this.indentation; i++) {
			this.stringBuilder.append(' ');
		}
		this.newLine = true;
		return this;
	}

}
