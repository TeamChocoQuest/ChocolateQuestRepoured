package org.jnbt;

public final class DoubleTag extends Tag
{
    private final double value;
    
    public DoubleTag(final String name, final double value) {
        super(name);
        this.value = value;
    }
    
    @Override
    public Double getValue() {
        return this.value;
    }
    
    @Override
    public String toString() {
        final String name = this.getName();
        String append = "";
        if (name != null && !name.equals("")) {
            append = "(\"" + this.getName() + "\")";
        }
        return "TAG_Double" + append + ": " + this.value;
    }
}
