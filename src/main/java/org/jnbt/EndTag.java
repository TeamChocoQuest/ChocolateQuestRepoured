package org.jnbt;

public final class EndTag extends Tag
{
    public EndTag() {
        super("");
    }
    
    @Override
    public Object getValue() {
        return null;
    }
    
    @Override
    public String toString() {
        return "TAG_End";
    }
}
