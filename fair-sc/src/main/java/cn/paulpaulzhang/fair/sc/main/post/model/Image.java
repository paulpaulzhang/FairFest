package cn.paulpaulzhang.fair.sc.main.post.model;

import java.io.File;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.post
 * 创建时间: 7/29/2019
 * 创建人: zlm31
 * 描述: 九宫格图片展示item
 */
public class Image {
    private File file;

    public Image(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
