package com.bbangle.bbangle.util;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;

public class KomoranUtil {

    // Komoran 형태소 분석 사용 Util
    private KomoranUtil() {
    }

    public static Komoran getInstance() {
        return KomoranInstance.instance;
    }

    private static class KomoranInstance {

        public static final Komoran instance = new Komoran(DEFAULT_MODEL.FULL);

    }

}
