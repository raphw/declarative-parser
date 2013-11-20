package com.blogspot.mydailyjava.dp.sample;

import com.blogspot.mydailyjava.dp.annotation.MatchBy;
import com.blogspot.mydailyjava.dp.annotation.WritePattern;

@MatchBy(";(@normal@);(\\@escaped@);(\\\\@unescaped@);\\(@normalInBrace@\\);" +
        "\\\\(@doubleNormalInBrace@\\\\);\\(\\@escapedInEscapedBrace@\\);")
@WritePattern(";@normal@;\\@escaped@;\\\\@unescaped@;(@normalInBrace@);" +
        "\\\\@doubleNormalInBrace@\\\\;(\\@escapedInEscapedBrace@);")
public class EscapePatternBean {

    private String normal;

    private String escaped;

    private String unescaped;

    private String normalInBrace;

    private String doubleNormalInBrace;

    private String escapedInEscapedBrace;

    public String getNormal() {
        return normal;
    }

    public void setNormal(String normal) {
        this.normal = normal;
    }

    public String getEscaped() {
        return escaped;
    }

    public void setEscaped(String escaped) {
        this.escaped = escaped;
    }

    public String getUnescaped() {
        return unescaped;
    }

    public void setUnescaped(String unescaped) {
        this.unescaped = unescaped;
    }

    public String getNormalInBrace() {
        return normalInBrace;
    }

    public void setNormalInBrace(String normalInBrace) {
        this.normalInBrace = normalInBrace;
    }

    public String getDoubleNormalInBrace() {
        return doubleNormalInBrace;
    }

    public void setDoubleNormalInBrace(String doubleNormalInBrace) {
        this.doubleNormalInBrace = doubleNormalInBrace;
    }

    public String getEscapedInEscapedBrace() {
        return escapedInEscapedBrace;
    }

    public void setEscapedInEscapedBrace(String escapedInEscapedBrace) {
        this.escapedInEscapedBrace = escapedInEscapedBrace;
    }
}
