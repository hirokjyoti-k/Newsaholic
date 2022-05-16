package com.adbu.newsaholic.model;

import java.util.Comparator;

public class ChannelSortByName implements Comparator<LiveChannel> {
    @Override
    public int compare(LiveChannel o1, LiveChannel o2) {
        return (o1.getNewsName().compareTo(o2.getNewsName()));
    }
}
