package cn.paulpaulzhang.fair.sc.icon;

import com.joanzapata.iconify.Icon;

/**
 * 项目名：   FairFest
 * 包名：     com.paulpaulzhang.fair.ec.icon
 * 文件名：   EcIcons
 * 创建者：   PaulZhang
 * 创建时间： 2019/5/18 18:58
 * 描述：     自定义图标库
 */
public enum EcIcons implements Icon {
    icon_scan('\ue602'),
    icon_ali_pay('\ue606');

    private char character;

    EcIcons(char character) {
        this.character = character;
    }

    @Override
    public String key() {
        return name().replace('_', '-');
    }

    @Override
    public char character() {
        return character;
    }
}
