package com.blogspot.mydailyjava.dp.sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public final class SampleData {

    public static final String RESOURCE_ANNOTATED = "/sample.Bean.txt";
    public static final String RESOURCE_OVERRIDEN = "/sample.Bean.overriden.txt";
    public static final String RESOURCE_EMPTY_LINES = "/sample.Bean.emptyLines.txt";
    public static final String RESOURCE_ERRONEOUS = "/sample.Bean.erroneous.txt";
    public static final String RESOURCE_ESCAPE = "/sample.Bean.escape.txt";

    private static final double DELTA = 0.0000001d;

    public static List<SampleBean> make() {
        SampleBean first = makePrototype("First");
        SampleBean second = makePrototype("Second");
        second.setOptionalString(null);
        SampleBean third = makePrototype("Third");
        third.setaInt(-10000);
        SampleBean forth = makePrototype("Forth");
        SampleBean fifth = makePrototype("Fifth");
        fifth.setaDouble(-100.5d);
        SampleBean sixth = makePrototype("Sixth");
        sixth.setaBoolean(false);
        return Arrays.asList(first, second, third, forth, fifth, sixth);
    }

    public static void assertRead(List<SampleBean> actual) {
        List<SampleBean> expected = make();
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < actual.size(); i++) {
            assertEquals(expected.get(i).getString(), actual.get(i).getString());
            assertEquals(expected.get(i).getUserPatternString(), actual.get(i).getUserPatternString());
            assertEquals(expected.get(i).getUserResolvedString(), actual.get(i).getUserResolvedString());
            assertEquals(expected.get(i).getOptionalString(), actual.get(i).getOptionalString());
            assertEquals(expected.get(i).getaByte(), actual.get(i).getaByte());
            assertEquals(expected.get(i).getaChar(), actual.get(i).getaChar());
            assertEquals(expected.get(i).getaDouble(), actual.get(i).getaDouble(), DELTA);
            assertEquals(expected.get(i).getaFloat(), actual.get(i).getaFloat(), DELTA);
            assertEquals(expected.get(i).getaInt(), actual.get(i).getaInt());
            assertEquals(expected.get(i).getaLong(), actual.get(i).getaLong());
            assertEquals(expected.get(i).getaShort(), actual.get(i).getaShort());
            assertEquals(expected.get(i).isaBoolean(), actual.get(i).isaBoolean());
        }
    }

    public static void assertContentEquals(File expected, File actual) throws Exception {
        BufferedReader expectedReader = new BufferedReader(new FileReader(expected));
        BufferedReader actualReader = new BufferedReader(new FileReader(actual));
        try {
            for (String expectedLine = expectedReader.readLine(), actualLine = actualReader.readLine();
                 expectedLine != null && actualLine != null;
                 expectedLine = expectedReader.readLine(), actualLine = actualReader.readLine()) {
                assertEquals(expectedLine, actualLine);
            }
        } finally {
            expectedReader.close();
            actualReader.close();
        }
        assertEquals(expected.length(), actual.length());
    }

    private static SampleBean makePrototype(String string) {
        SampleBean prototype = new SampleBean();
        prototype.setString(string);
        prototype.setUserResolvedString("USER_RESOLVER");
        prototype.setUserPatternString("user_pattern");
        prototype.setOptionalString("optional");
        prototype.setaInt(10000);
        prototype.setaShort((short) 1000);
        prototype.setaByte((byte) 100);
        prototype.setaLong(10000000L);
        prototype.setaChar('X');
        prototype.setaDouble(100.5d);
        prototype.setaFloat(10.5f);
        prototype.setaBoolean(true);
        return prototype;
    }

    public static void assertEscapeBeans(List<EscapePatternBean> beans) {
        assertEquals(1, beans.size());
        EscapePatternBean bean = beans.get(0);
        assertEquals("val1", bean.getNormal());
        assertNull(bean.getEscaped());
        assertEquals("val3", bean.getUnescaped());
        assertEquals("val4", bean.getNormalInBrace());
        assertEquals("val5", bean.getDoubleNormalInBrace());
        assertNull(bean.getEscapedInEscapedBrace());
    }

    public static EscapePatternBean makeEscapeBean() {
        EscapePatternBean bean = new EscapePatternBean();
        bean.setNormal("val1");
        bean.setUnescaped("val3");
        bean.setNormalInBrace("val4");
        bean.setDoubleNormalInBrace("val5");
        return bean;
    }

    public static void assertEscapeBean(String value) {
        assertEquals(";val1;@escaped@;\\val3;(val4);\\val5\\;(@escapedInEscapedBrace@);", value);
    }

    private SampleData() {
        throw new AssertionError();
    }
}
