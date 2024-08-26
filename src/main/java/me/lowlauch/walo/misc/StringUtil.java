package me.lowlauch.walo.misc;

public class StringUtil {
    // Thanks stackoverflow
    // https://stackoverflow.com/questions/11588916/java-replace-character-at-specific-position-of-string
    public static String replaceCharAt(String s, int pos, char c) {
        return s.substring(0,pos) + c + s.substring(pos+1);
    }
}
