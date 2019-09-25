package cn.paulpaulzhang.fair.sc.main.interest.model;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.interest.model
 * 创建时间：9/24/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class TeamSection extends SectionEntity<Team> {

    public TeamSection(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public TeamSection(Team team) {
        super(team);
    }
}
