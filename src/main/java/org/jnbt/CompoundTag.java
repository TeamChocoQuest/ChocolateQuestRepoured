package org.jnbt;

import java.util.*;

public final class CompoundTag extends Tag
{
    private final Map<String, Tag> value;
    
    public CompoundTag(final String name, final Map<String, Tag> value) {
        super(name);
        this.value = Collections.unmodifiableMap((Map<? extends String, ? extends Tag>)value);
    }
    
    @Override
    public Map<String, Tag> getValue() {
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
        bldr.append("TAG_Compound" + append + ": " + this.value.size() + " entries\r\n{\r\n");
        for (final Map.Entry<String, Tag> entry : this.value.entrySet()) {
            bldr.append("   " + entry.getValue().toString().replaceAll("\r\n", "\r\n   ") + "\r\n");
        }
        bldr.append("}");
        return bldr.toString();
    }
}
