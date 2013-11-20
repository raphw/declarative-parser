package com.blogspot.mydailyjava.dp.handler;

import com.blogspot.mydailyjava.dp.annotation.Skip;
import com.blogspot.mydailyjava.dp.sample.EscapePatternBean;
import com.blogspot.mydailyjava.dp.sample.SampleBean;
import com.blogspot.mydailyjava.dp.sample.SampleData;
import org.junit.Test;

import java.io.*;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class BeanTransformerTest {

    @Test
    public void testAnnotatedRead() throws Exception {
        BeanTransformer<SampleBean> beanTransformer = BeanTransformer.make(SampleBean.class);
        testRead(beanTransformer, makeResource(SampleData.RESOURCE_ANNOTATED));
    }

    @Test
    public void testAnnotatedWrite() throws Exception {
        BeanTransformer<SampleBean> beanTransformer = BeanTransformer.make(SampleBean.class);
        testWrite(beanTransformer, makeResource(SampleData.RESOURCE_ANNOTATED));
    }

    @Test
    public void testOverridenRead() throws Exception {
        BeanTransformer<SampleBean> beanTransformer = BeanTransformer.builder()
                .readPattern(SampleBean.ALTERED_PATTERN)
                .build(SampleBean.class);
        testRead(beanTransformer, makeResource(SampleData.RESOURCE_OVERRIDEN));
    }

    @Test
    public void testOverridenWrite() throws Exception {
        BeanTransformer<SampleBean> beanTransformer = BeanTransformer.builder()
                .readPattern(SampleBean.ALTERED_PATTERN)
                .build(SampleBean.class);
        testWrite(beanTransformer, makeResource(SampleData.RESOURCE_OVERRIDEN));
    }

    @Test
    public void testReadEmptyLines() throws Exception {
        BeanTransformer<SampleBean> beanTransformer = BeanTransformer.make(SampleBean.class);
        testRead(beanTransformer, makeResource(SampleData.RESOURCE_EMPTY_LINES));
    }

    @Test
    public void testReadFaultyLines() throws Exception {
        BeanTransformer<SampleBean> beanTransformer = BeanTransformer.builder()
                .skipPolicy(Skip.Policy.NON_MATCHING)
                .build(SampleBean.class);
        testRead(beanTransformer, makeResource(SampleData.RESOURCE_ERRONEOUS));
    }

    @Test
    public void testEscape() throws Exception {
        BeanTransformer<EscapePatternBean> beanTransformer = BeanTransformer.make(EscapePatternBean.class);
        FileReader fileReader = new FileReader(makeResource(SampleData.RESOURCE_ESCAPE));
        try {
            List<EscapePatternBean> beans = beanTransformer.read(fileReader);
            SampleData.assertEscapeBeans(beans);
        } finally {
            fileReader.close();
        }
        SampleData.assertEscapeBean(beanTransformer.writeSingle(SampleData.makeEscapeBean()));
    }

    private static void testRead(BeanTransformer<SampleBean> beanTransformer, File input) throws Exception {
        Reader reader = new FileReader(input);
        try {
            List<SampleBean> beans = beanTransformer.read(reader);
            SampleData.assertRead(beans);
        } finally {
            reader.close();
        }
    }

    private static void testWrite(BeanTransformer<SampleBean> beanTransformer, File expectedOutput) throws Exception {
        File file = File.createTempFile("output", ".txt");
        assertTrue(file.canWrite());
        Writer writer = new FileWriter(file);
        try {
            beanTransformer.write(writer, SampleData.make());
        } finally {
            writer.close();
        }
        SampleData.assertContentEquals(expectedOutput, file);
        assertTrue(file.delete());
    }

    private static File makeResource(String location) {
        return new File(BeanTransformerTest.class.getResource(location).getFile());
    }
}
