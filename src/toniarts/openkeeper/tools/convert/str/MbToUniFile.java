/*
 * Copyright (C) 2014-2019 OpenKeeper
 *
 * OpenKeeper is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenKeeper is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenKeeper.  If not, see <http://www.gnu.org/licenses/>.
 */
package toniarts.openkeeper.tools.convert.str;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import toniarts.openkeeper.tools.convert.ConversionUtils;

/**
 * Dungeon Keeper 2 MultiByte to Unicode codepage file reader. The file is used
 * to translate the interface texts to unicode. <br>
 * Format reverse engineered by:
 * <li>George Gensure</li>
 *
 * @author Toni Helenius <helenius.toni@gmail.com>
 */
public class MbToUniFile {

    private static final String CODEPAGE_HEADER_IDENTIFIER = "BFMU";

    /**
     * Count of single byte characters
     */
    private final short singleCount;
    private final short unknown1;
    private final short count1;
    private final short count2;
    private final CharBuffer charBuffer;

    private final int threshold;
    private final int count;

    public MbToUniFile(File file) {
        try (RandomAccessFile rawCodepage = new RandomAccessFile(file, "r")) {

            // Check the header
            byte[] header = new byte[4];
            rawCodepage.read(header);
            if (!CODEPAGE_HEADER_IDENTIFIER.equals(ConversionUtils.toString(header))) {
                throw new RuntimeException("Header should be " + CODEPAGE_HEADER_IDENTIFIER + " and it was " + ConversionUtils.toString(header) + "! Cancelling!");
            }
            singleCount = ConversionUtils.toUnsignedByte(rawCodepage.readByte());
            unknown1 = ConversionUtils.toUnsignedByte(rawCodepage.readByte());
            count1 = ConversionUtils.toUnsignedByte(rawCodepage.readByte());
            count2 = ConversionUtils.toUnsignedByte(rawCodepage.readByte());
            byte[] data = new byte[(int) (rawCodepage.length() - rawCodepage.getFilePointer())];
            rawCodepage.read(data);

            threshold = 255 - singleCount;
            count = (((count1 - 1) + 257) * 255) - threshold * 254 + count2;

            // Put the code page to byte buffer for fast access, it is a small file
            ByteBuffer buf = ByteBuffer.wrap(data).asReadOnlyBuffer();
            buf.order(ByteOrder.LITTLE_ENDIAN);
            charBuffer = buf.asCharBuffer();
        } catch (IOException e) {

            // Fug
            throw new RuntimeException("Failed to read the file " + file + "!", e);
        }
    }

    /**
     * Get threshold. The threshold governs should 1 or 2 bytes used for the
     * character index
     *
     * @return the multibyte threshold
     */
    public int getThreshold() {
        return threshold;
    }

    /**
     * Get character mapped at index
     *
     * @param index the character index
     * @return the character
     */
    public char getCharacter(int index) {
        return index < count ? charBuffer.charAt(index) : 0;
    }

}
