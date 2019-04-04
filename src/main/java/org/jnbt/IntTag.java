package org.jnbt;

public final class IntTag extends Tag
{
    private final int value;
    
    public IntTag(final String name, final int value) {
        super(name);
        this.value = value;
    }
    
    @Override
    public Integer getValue() {
        return this.value;
    }
    
    @Override
    public String toString() {
        final String name = this.getName();
        String append = "";
        if (name != null && !name.equals("")) {
            append = "(\"" + this.getName() + "\")";
        }
        return "TAG_Int" + append + ": " + this.value;
    }
}
