package org.jnbt;

public final class ByteArrayTag extends Tag
{
    private final byte[] value;
    
    public ByteArrayTag(final String name, final byte[] value) {
        super(name);
        this.value = value;
    }
    
    @Override
    public byte[] getValue() {
        return this.value;
    }
    
    @Override
    public String toString() {
        final StringBuilder hex = new StringBuilder();
        byte[] value;
        for (int length = (value = this.value).length, i = 0; i < length; ++i) {
            final byte b = value[i];
            final String hexDigits = Integer.toHexString(b).toUpperCase();
            if (hexDigits.length() == 1) {
                hex.append("0");
            }
            hex.append(hexDigits).append(" ");
        }
        final String name = this.getName();
        String append = "";
        if (name != null && !name.equals("")) {
            append = "(\"" + this.getName() + "\")";
        }
        return "TAG_Byte_Array" + append + ": " + hex.toString();
    }
}
