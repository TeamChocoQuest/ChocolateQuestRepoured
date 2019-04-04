package org.jnbt;

public final class LongTag extends Tag
{
    private final long value;
    
    public LongTag(final String name, final long value) {
        super(name);
        this.value = value;
    }
    
    @Override
    public Long getValue() {
        return this.value;
    }
    
    @Override
    public String toString() {
        final String name = this.getName();
        String append = "";
        if (name != null && !name.equals("")) {
            append = "(\"" + this.getName() + "\")";
        }
        return "TAG_Long" + append + ": " + this.value;
    }
}
