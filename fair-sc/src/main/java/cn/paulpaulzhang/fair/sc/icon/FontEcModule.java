package cn.paulpaulzhang.fair.sc.icon;

import com.joanzapata.iconify.Icon;
import com.joanzapata.iconify.IconFontDescriptor;

/**
 * 项目名：   FairFest
 * 包名：     com.paulpaulzhang.fair.ec.icon
 * 文件名：   FontEcModule
 * 创建者：   PaulZhang
 * 创建时间： 2019/5/18 18:58
 * 描述：     自定义字体图标Module
 */
public class FontEcModule implements IconFontDescriptor {
    @Override
    public String ttfFileName() {
        return "iconfont.ttf";
    }

    @Override
    public Icon[] characters() {
        return EcIcons.values();
    }
}
