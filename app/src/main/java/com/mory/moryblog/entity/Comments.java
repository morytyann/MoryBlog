package com.mory.moryblog.entity;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Comments {

    private List<Comment> comments;
    private long previousCursor;
    private long nextCursor;
    private long totalNumber;

}
