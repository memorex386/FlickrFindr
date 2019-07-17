package com.example.flickerexample.network;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.SOURCE)
@StringDef({PhotoSort.DATE_POSTED_ASC, PhotoSort.DATE_POSTED_DESC, PhotoSort.DATE_TAKEN_ASC, PhotoSort.DATE_TAKEN_DESC, PhotoSort.INTERESTING_ASC, PhotoSort.INTERESTING_DESC, PhotoSort.RELEVANCE})
public @interface PhotoSort {
    public static final String DATE_POSTED_ASC = "date-posted-asc";
    public static final String DATE_POSTED_DESC = "date-posted-desc";
    public static final String DATE_TAKEN_ASC = "date-taken-asc";
    public static final String DATE_TAKEN_DESC = "date-taken-desc";
    public static final String INTERESTING_DESC = "interestingness-desc";
    public static final String INTERESTING_ASC = "interestingness-asc";
    public static final String RELEVANCE = "relevance";

}
