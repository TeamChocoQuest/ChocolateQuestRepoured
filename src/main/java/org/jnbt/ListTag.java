package org.jnbt;

import java.util.*;

public final class ListTag extends Tag
{
    private final Class<? extends Tag> type;
    private final List<Tag> value;
    
    public ListTag(final String name, final Class<? extends Tag> type, final List<Tag> value) {
        super(name);
        this.type = type;
        this.value = Collections.unmodifiableList((List<? extends Tag>)value);
    }
    
    public Class<? extends Tag> getType() {
        return this.type;
    }
    
    @Override
    public List<Tag> getValue() {
        return this.value;
    }
    
    @Override
    public String toString() {
        final String name = this.getName();
        String append = "";
        if (name != null && !name.equals("")) {
            append = "(\"" + this.getName() + "\")";
        }
        final StringBuilder bldr = new StringBuilder();
        bldr.append("TAG_List" + append + ": " + this.value.size() + " entries of type " + NBTUtils.getTypeName(this.type) + "\r\n{\r\n");
        for (final Tag t : this.value) {
            bldr.append("   " + t.toString().replaceAll("\r\n", "\r\n   ") + "\r\n");
        }
        bldr.append("}");
        return bldr.toString();
    }
}
