package org.jnbt;

public final class ShortTag extends Tag
{
    private final short value;
    
    public ShortTag(final String name, final short value) {
        super(name);
        this.value = value;
    }
    
    @Override
    public Short getValue() {
        return this.value;
    }
    
    @Override
    public String toString() {
        final String name = this.getName();
        String append = "";
        if (name != null && !name.equals("")) {
            append = "(\"" + this.getName() + "\")";
        }
        return "TAG_Short" + append + ": " + this.value;
    }
}
