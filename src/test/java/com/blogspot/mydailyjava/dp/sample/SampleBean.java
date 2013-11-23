package com.blogspot.mydailyjava.dp.sample;

import com.blogspot.mydailyjava.dp.annotation.*;

@MatchBy(SampleBean.PATTERN)
@Skip(Skip.Policy.EMPTY)
public class SampleBean {

    public static final String PATTERN = "@string@;@userResolvedString@;@userPatternString@;" +
            "@optionalString@;@aInt@;@aShort@;@aByte@;@aLong@;@aChar@;" +
            "@aDouble@;@aFloat@;@aBoolean@";
    public static final String ALTERED_PATTERN = "--" + PATTERN + "--";

    private String string;

    @ResolveMatchWith(SampleDelegate.class)
    private String userResolvedString;

    @MatchBy("[a-z_]+")
    private String userPatternString;

    @OptionalMatch
    private String optionalString;

    private int aInt;

    private short aShort;

    private byte aByte;

    private long aLong;

    private char aChar;

    private double aDouble;

    private float aFloat;

    private boolean aBoolean;

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public String getUserResolvedString() {
        return userResolvedString;
    }

    public String getUserPatternString() {
        return userPatternString;
    }

    public void setUserPatternString(String userPatternString) {
        this.userPatternString = userPatternString;
    }

    public void setUserResolvedString(String userResolvedString) {
        this.userResolvedString = userResolvedString;
    }

    public String getOptionalString() {
        return optionalString;
    }

    public void setOptionalString(String optionalString) {
        this.optionalString = optionalString;
    }

    public int getaInt() {
        return aInt;
    }

    public void setaInt(int aInt) {
        this.aInt = aInt;
    }

    public short getaShort() {
        return aShort;
    }

    public void setaShort(short aShort) {
        this.aShort = aShort;
    }

    public byte getaByte() {
        return aByte;
    }

    public void setaByte(byte aByte) {
        this.aByte = aByte;
    }

    public long getaLong() {
        return aLong;
    }

    public void setaLong(long aLong) {
        this.aLong = aLong;
    }

    public char getaChar() {
        return aChar;
    }

    public void setaChar(char aChar) {
        this.aChar = aChar;
    }

    public double getaDouble() {
        return aDouble;
    }

    public void setaDouble(double aDouble) {
        this.aDouble = aDouble;
    }

    public float getaFloat() {
        return aFloat;
    }

    public void setaFloat(float aFloat) {
        this.aFloat = aFloat;
    }

    public boolean isaBoolean() {
        return aBoolean;
    }

    public void setaBoolean(boolean aBoolean) {
        this.aBoolean = aBoolean;
    }
}
