package org.originit;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit test for simple App.
 */
@ExtendWith(TimingExtension.class)
public class AppTest 
{

    @Test
    void testStringTable() {
        String s = new String("怪") + new String("物");
        s.intern();
        assertTrue(s == "怪物");
        assertTrue(s.equals("怪物"));

    }

    @Test
    void testStringAdd() {
        for (int i = 0; i < 100000; i++) {
            String.valueOf(i).intern();
        }
    }

    @Test
    void testStringBuilder() {
        StringBuilder s = new StringBuilder(200000);
        for (int i = 0; i < 100000; i++) {
            s.append(i);
        }
    }
}
