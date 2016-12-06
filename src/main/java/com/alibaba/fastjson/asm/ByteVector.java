/**
 * Copyright (C) 2014-2015 The Skfiy Open Association.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.fastjson.asm;

/**
 * A dynamically extensible vector of bytes. This class is roughly equivalent to a DataOutputStream on top of a
 * ByteArrayOutputStream, but is more efficient.
 * 
 * @author Eric Bruneton
 */
public class ByteVector {

    /**
     * The content of this vector.
     */
    byte[] data;

    /**
     * Actual number of bytes in this vector.
     */
    int    length;

    /**
     * Constructs a new {@link ByteVector ByteVector} with a default initial size.
     */
    public ByteVector(){
        data = new byte[64];
    }

    /**
     * Constructs a new {@link ByteVector ByteVector} with the given initial size.
     * 
     * @param initialSize the initial size of the byte vector to be constructed.
     */
    public ByteVector(final int initialSize){
        data = new byte[initialSize];
    }

    /**
     * Puts a byte into this byte vector. The byte vector is automatically enlarged if necessary.
     * 
     * @param b a byte.
     * @return this byte vector.
     */
    public ByteVector putByte(final int b) {
        int length = this.length;
        if (length + 1 > data.length) {
            enlarge(1);
        }
        data[length++] = (byte) b;
        this.length = length;
        return this;
    }

    /**
     * Puts two bytes into this byte vector. The byte vector is automatically enlarged if necessary.
     * 
     * @param b1 a byte.
     * @param b2 another byte.
     * @return this byte vector.
     */
    ByteVector put11(final int b1, final int b2) {
        int length = this.length;
        if (length + 2 > data.length) {
            enlarge(2);
        }
        byte[] data = this.data;
        data[length++] = (byte) b1;
        data[length++] = (byte) b2;
        this.length = length;
        return this;
    }

    /**
     * Puts a short into this byte vector. The byte vector is automatically enlarged if necessary.
     * 
     * @param s a short.
     * @return this byte vector.
     */
    public ByteVector putShort(final int s) {
        int length = this.length;
        if (length + 2 > data.length) {
            enlarge(2);
        }
        byte[] data = this.data;
        data[length++] = (byte) (s >>> 8);
        data[length++] = (byte) s;
        this.length = length;
        return this;
    }

    /**
     * Puts a byte and a short into this byte vector. The byte vector is automatically enlarged if necessary.
     * 
     * @param b a byte.
     * @param s a short.
     * @return this byte vector.
     */
    ByteVector put12(final int b, final int s) {
        int length = this.length;
        if (length + 3 > data.length) {
            enlarge(3);
        }
        byte[] data = this.data;
        data[length++] = (byte) b;
        data[length++] = (byte) (s >>> 8);
        data[length++] = (byte) s;
        this.length = length;
        return this;
    }

    /**
     * Puts an int into this byte vector. The byte vector is automatically enlarged if necessary.
     * 
     * @param i an int.
     * @return this byte vector.
     */
    public ByteVector putInt(final int i) {
        int length = this.length;
        if (length + 4 > data.length) {
            enlarge(4);
        }
        byte[] data = this.data;
        data[length++] = (byte) (i >>> 24);
        data[length++] = (byte) (i >>> 16);
        data[length++] = (byte) (i >>> 8);
        data[length++] = (byte) i;
        this.length = length;
        return this;
    }

    /**
     * Puts an UTF8 string into this byte vector. The byte vector is automatically enlarged if necessary.
     * 
     * @param s a String.
     * @return this byte vector.
     */
    public ByteVector putUTF8(final String s) {
        int charLength = s.length();
        int len = length;
        if (len + 2 + charLength > data.length) {
            enlarge(2 + charLength);
        }
        byte[] data = this.data;
        // optimistic algorithm: instead of computing the byte length and then
        // serializing the string (which requires two loops), we assume the byte
        // length is equal to char length (which is the most frequent case), and
        // we start serializing the string right away. During the serialization,
        // if we find that this assumption is wrong, we continue with the
        // general method.
        data[len++] = (byte) (charLength >>> 8);
        data[len++] = (byte) charLength;
        for (int i = 0; i < charLength; ++i) {
            char c = s.charAt(i);
            if (c >= '\001' && c <= '\177') {
                data[len++] = (byte) c;
            } else {
                throw new UnsupportedOperationException();
            }
        }
        length = len;
        return this;
    }

    /**
     * Puts an array of bytes into this byte vector. The byte vector is automatically enlarged if necessary.
     * 
     * @param b an array of bytes. May be <tt>null</tt> to put <tt>len</tt> null bytes into this byte vector.
     * @param off index of the fist byte of b that must be copied.
     * @param len number of bytes of b that must be copied.
     * @return this byte vector.
     */
    public ByteVector putByteArray(final byte[] b, final int off, final int len) {
        if (length + len > data.length) {
            enlarge(len);
        }
        if (b != null) {
            System.arraycopy(b, off, data, length, len);
        }
        length += len;
        return this;
    }

    /**
     * Enlarge this byte vector so that it can receive n more bytes.
     * 
     * @param size number of additional bytes that this byte vector should be able to receive.
     */
    private void enlarge(final int size) {
        int length1 = 2 * data.length;
        int length2 = length + size;
        byte[] newData = new byte[length1 > length2 ? length1 : length2];
        System.arraycopy(data, 0, newData, 0, length);
        data = newData;
    }
}
