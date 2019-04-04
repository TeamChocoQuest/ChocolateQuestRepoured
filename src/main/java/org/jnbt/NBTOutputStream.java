package org.jnbt;

import java.util.zip.*;
import java.io.*;
import java.util.*;

public final class NBTOutputStream implements Closeable
{
    private final DataOutputStream os;
    
    public NBTOutputStream(final OutputStream os) throws IOException {
        this.os = new DataOutputStream(new GZIPOutputStream(os));
    }
    
    public void writeTag(final Tag tag) throws IOException {
        final int type = NBTUtils.getTypeCode(tag.getClass());
        final String name = tag.getName();
        final byte[] nameBytes = name.getBytes(NBTConstants.CHARSET);
        this.os.writeByte(type);
        this.os.writeShort(nameBytes.length);
        this.os.write(nameBytes);
        if (type == 0) {
            throw new IOException("Named TAG_End not permitted.");
        }
        this.writeTagPayload(tag);
    }
    
    private void writeTagPayload(final Tag tag) throws IOException {
        final int type = NBTUtils.getTypeCode(tag.getClass());
        switch (type) {
            case 0: {
                this.writeEndTagPayload((EndTag)tag);
                break;
            }
            case 1: {
                this.writeByteTagPayload((ByteTag)tag);
                break;
            }
            case 2: {
                this.writeShortTagPayload((ShortTag)tag);
                break;
            }
            case 3: {
                this.writeIntTagPayload((IntTag)tag);
                break;
            }
            case 4: {
                this.writeLongTagPayload((LongTag)tag);
                break;
            }
            case 5: {
                this.writeFloatTagPayload((FloatTag)tag);
                break;
            }
            case 6: {
                this.writeDoubleTagPayload((DoubleTag)tag);
                break;
            }
            case 7: {
                this.writeByteArrayTagPayload((ByteArrayTag)tag);
                break;
            }
            case 8: {
                this.writeStringTagPayload((StringTag)tag);
                break;
            }
            case 9: {
                this.writeListTagPayload((ListTag)tag);
                break;
            }
            case 10: {
                this.writeCompoundTagPayload((CompoundTag)tag);
                break;
            }
            default: {
                throw new IOException("Invalid tag type: " + type + ".");
            }
        }
    }
    
    private void writeByteTagPayload(final ByteTag tag) throws IOException {
        this.os.writeByte(tag.getValue());
    }
    
    private void writeByteArrayTagPayload(final ByteArrayTag tag) throws IOException {
        final byte[] bytes = tag.getValue();
        this.os.writeInt(bytes.length);
        this.os.write(bytes);
    }
    
    private void writeCompoundTagPayload(final CompoundTag tag) throws IOException {
        for (final Tag childTag : tag.getValue().values()) {
            this.writeTag(childTag);
        }
        this.os.writeByte(0);
    }
    
    private void writeListTagPayload(final ListTag tag) throws IOException {
        final Class<? extends Tag> clazz = tag.getType();
        final List<Tag> tags = tag.getValue();
        final int size = tags.size();
        this.os.writeByte(NBTUtils.getTypeCode(clazz));
        this.os.writeInt(size);
        for (int i = 0; i < size; ++i) {
            this.writeTagPayload(tags.get(i));
        }
    }
    
    private void writeStringTagPayload(final StringTag tag) throws IOException {
        final byte[] bytes = tag.getValue().getBytes(NBTConstants.CHARSET);
        this.os.writeShort(bytes.length);
        this.os.write(bytes);
    }
    
    private void writeDoubleTagPayload(final DoubleTag tag) throws IOException {
        this.os.writeDouble(tag.getValue());
    }
    
    private void writeFloatTagPayload(final FloatTag tag) throws IOException {
        this.os.writeFloat(tag.getValue());
    }
    
    private void writeLongTagPayload(final LongTag tag) throws IOException {
        this.os.writeLong(tag.getValue());
    }
    
    private void writeIntTagPayload(final IntTag tag) throws IOException {
        this.os.writeInt(tag.getValue());
    }
    
    private void writeShortTagPayload(final ShortTag tag) throws IOException {
        this.os.writeShort(tag.getValue());
    }
    
    private void writeEndTagPayload(final EndTag tag) {
    }
    
    @Override
    public void close() throws IOException {
        this.os.close();
    }
}
