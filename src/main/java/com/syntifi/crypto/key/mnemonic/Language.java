package com.syntifi.crypto.key.mnemonic;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * Enum with the languages, dictionaries and checkSum supported for Mnemonic generation
 * Abstract class for needed shared functionalities
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.3.0
 */
public enum Language {
    EN("english.txt", "ad90bf3beb7b0eb7e5acd74727dc0da96e0a280a258354e7293fb7e211ac03db", StandardCharsets.UTF_8, Locale.ENGLISH),
    PT("portuguese.txt", "eed387d44cf8f32f60754527e265230d8019e8a2277937c71ef812e7a46c93fd", StandardCharsets.UTF_8, Locale.forLanguageTag("PT")),
    ES("spanish.txt", "a556a26c6a5bb36db0fb7d8bf579cb7465fcaeec03957c0dda61b569962d9da5", StandardCharsets.UTF_8, Locale.forLanguageTag("ES")),
    FR("french.txt", "9cbdaadbd3ce9cbaee1b360fce45e935b21e3e2c56d9fcd56b3398ced2371866", StandardCharsets.UTF_8, Locale.FRENCH),
    CNS("chinese_simplified.txt", "bfd683b91db88609fabad8968c7efe4bf69606bf5a49ac4a4ba5e355955670cb", StandardCharsets.UTF_8, Locale.CHINA),
    CNT("chinese_traditional.txt", "", StandardCharsets.UTF_8, Locale.CHINESE);

    private final String fileName;
    private final String checkSum;
    private final Charset charset;
    private final Locale locale;

    Language(String fileName, String checkSum, Charset charset, Locale locale) {
        this.fileName = fileName;
        this.checkSum = checkSum;
        this.charset = charset;
        this.locale = locale;
    }


    public String getFileName() {
        return fileName;
    }

    public String getCheckSum() {
        return checkSum;
    }

    public Charset getCharset() {
        return charset;
    }

    public Locale getLocale() {
        return locale;
    }
}
