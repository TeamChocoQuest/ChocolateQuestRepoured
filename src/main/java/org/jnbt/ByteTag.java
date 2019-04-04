package org.jnbt;

public final class ByteTag extends Tag
{
    private final byte value;
    
    public ByteTag(final String name, final byte value) {
        super(name);
        this.value = value;
    }
    
    @Override
    public Byte getValue() {
        return this.value;
    }
    
    @Override
    public String toString() {
        final String name = this.getName();
        String append = "";
        if (name != null && !name.equals("")) {
            append = "(\"" + this.getName() + "\")";
        }
        return "TAG_Byte" + append + ": " + this.value;
    }
}
