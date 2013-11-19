package com.blogspot.mydailyjava.dp.sample;

import com.blogspot.mydailyjava.dp.annotation.MatchBy;

@MatchBy("^(@replace@);(\\@doNotReplace@)$")
public class EscapePatternBean {

    private String replace;

    private String doNotReplace;

    public String getReplace() {
        return replace;
    }

    public void setReplace(String replace) {
        this.replace = replace;
    }

    public String getDoNotReplace() {
        return doNotReplace;
    }

    public void setDoNotReplace(String doNotReplace) {
        this.doNotReplace = doNotReplace;
    }
}
