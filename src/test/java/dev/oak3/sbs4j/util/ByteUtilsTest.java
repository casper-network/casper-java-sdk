package dev.oak3.sbs4j.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class ByteUtilsTest {
    @Test
    void reverseEvenByteArray() {
        byte[] expected = new byte[]{0, 2, 4, 6};
        byte[] input = new byte[]{6, 4, 2, 0};

        ByteUtils.reverse(input);

        assertArrayEquals(expected, input);
    }

    @Test
    void reverseEvenByteArrayRange_from_1_to_2() {
        byte[] expected = new byte[]{0, 4, 2, 6};
        byte[] input = new byte[]{0, 2, 4, 6};

        ByteUtils.reverse(input, 1, 2);

        assertArrayEquals(expected, input);
    }

    @Test
    void reverseEvenByteArrayRange_from_1_to_3() {
        byte[] expected = new byte[]{0, 6, 4, 2};
        byte[] input = new byte[]{0, 2, 4, 6};

        ByteUtils.reverse(input, 1, 3);

        assertArrayEquals(expected, input);
    }

    @Test
    void reverseOddByteArray() {
        byte[] expected = new byte[]{8, 6, 4, 2, 0};
        byte[] input = new byte[]{0, 2, 4, 6, 8};

        ByteUtils.reverse(input);

        assertArrayEquals(expected, input);
    }

    @Test
    void reverseOddByteArrayRange_from_1_to_4() {
        byte[] expected = new byte[]{0, 8, 6, 4, 2};
        byte[] input = new byte[]{0, 2, 4, 6, 8};

        ByteUtils.reverse(input, 1, 4);

        assertArrayEquals(expected, input);
    }

    @Test
    void reverseOddByteArrayRange_from_0_to_4() {
        byte[] expected = new byte[]{8, 6, 4, 2, 0, 10};
        byte[] input = new byte[]{0, 2, 4, 6, 8, 10};

        ByteUtils.reverse(input, 0, 4);

        assertArrayEquals(expected, input);
    }

    @Test
    void reverseEmptyByteArray() {
        byte[] expected = new byte[]{};
        byte[] input = new byte[]{};

        ByteUtils.reverse(input);

        assertArrayEquals(expected, input);
    }

    @Test
    void reverseOneElementByteArray() {
        byte[] expected = new byte[]{0};
        byte[] input = new byte[]{0};

        ByteUtils.reverse(input);

        assertArrayEquals(expected, input);
    }
}