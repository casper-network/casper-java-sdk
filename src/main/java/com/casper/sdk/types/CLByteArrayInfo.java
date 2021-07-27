package com.casper.sdk.types;

/**
 * Specialized {@link CLTypeInfo} for byte arrays that have a size.
 *
 * JSON Format:
 * <pre>{
 *      "cl_type": {
 *        "ByteArray": 32
 *      },
 *      "bytes": "0101010101010101010101010101010101010101010101010101010101010101",
 *      "parsed": "0101010101010101010101010101010101010101010101010101010101010101"
 * </pre>
 */
public class CLByteArrayInfo extends CLTypeInfo {

    /** The size of the byte array */
    private final int size;

    public CLByteArrayInfo(final int size) {
        super(CLType.BYTE_ARRAY);
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
