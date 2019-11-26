package com.mory.moryblog.entity;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Pic implements Serializable {

    private String thumbnailPic;
    private String bmiddlePic;
    private String originalPic;

}
