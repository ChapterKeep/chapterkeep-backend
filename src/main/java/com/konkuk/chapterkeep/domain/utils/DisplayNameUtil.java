package com.konkuk.chapterkeep.domain.utils;

import com.konkuk.chapterkeep.domain.Member;

public class DisplayNameUtil {

    public static String getDisplayName(boolean isAnonymous, Member member) {
        return isAnonymous ? "익명" : member.getName();
    }
}
