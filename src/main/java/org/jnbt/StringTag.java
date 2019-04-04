package org.jnbt;

public final class StringTag extends Tag
{
    private final String value;
    
    public StringTag(final String name, final String value) {
        super(name);
        this.value = value;
    }
    
    @Override
    public String getValue() {
        return this.value;
    }
    
    @Override
    public String toString() {
        final String name = this.getName();
        String append = "";
        if (name != null && !name.equals("")) {
            append = "(\"" + this.getName() + "\")";
        }
        return "TAG_String" + append + ": " + this.value;
    }
}
