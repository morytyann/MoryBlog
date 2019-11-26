package com.mory.moryblog.entity;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Timeline {

    private List<Weibo> statuses;
    private long previousCursor;
    private long nextCursor;
    private long totalNumber;
}
