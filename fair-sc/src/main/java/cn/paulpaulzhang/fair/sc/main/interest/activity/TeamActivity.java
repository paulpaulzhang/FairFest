package cn.paulpaulzhang.fair.sc.main.interest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.afollestad.materialdialogs.LayoutMode;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.bottomsheets.BottomSheet;
import com.afollestad.materialdialogs.customview.DialogCustomViewExtKt;
import com.afollestad.materialdialogs.lifecycle.LifecycleExtKt;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.android.material.appbar.MaterialToolbar;
import com.gyf.immersionbar.ImmersionBar;
import com.sunhapper.x.spedit.view.SpXEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import cn.paulpaulzhang.fair.activities.FairActivity;
import cn.paulpaulzhang.fair.constant.Api;
import cn.paulpaulzhang.fair.constant.UserConfigs;
import cn.paulpaulzhang.fair.net.RestClient;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.sc.main.interest.adapter.TeamAdapter;
import cn.paulpaulzhang.fair.sc.main.interest.model.Team;
import cn.paulpaulzhang.fair.sc.main.interest.model.TeamSection;
import cn.paulpaulzhang.fair.sc.main.user.activity.UserCenterActivity;
import cn.paulpaulzhang.fair.util.log.FairLogger;
import cn.paulpaulzhang.fair.util.storage.FairPreference;
import es.dmoral.toasty.Toasty;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.interest.activity
 * 创建时间：9/2/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class TeamActivity extends FairActivity {

    @BindView(R2.id.rv_team)
    RecyclerView mRecyclerView;

    @BindView(R2.id.srl_team)
    SwipeRefreshLayout mSwipeRefresh;

    @BindView(R2.id.tb_team)
    MaterialToolbar mToolbar;

    private TeamAdapter mAdapter;

    @Override
    public int setLayout() {
        return R.layout.activity_team;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarDarkFont(true).init();

        initToolbar(mToolbar);

        initSwipeRefresh();
        initRecyclerView();
        loadData();
        mSwipeRefresh.setRefreshing(true);

    }

    @Override
    public void initToolbar(Toolbar mToolbar) {
        mToolbar.setTitle(getString(R.string.team));
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initRecyclerView() {
        mAdapter = new TeamAdapter(R.layout.item_team, R.layout.item_section_header, new ArrayList<>());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            TeamSection item = (TeamSection) adapter.getItem(position);
            if (item != null) {
                if (item.isHeader) {
                    return;
                }

                Intent intent = new Intent(this, UserCenterActivity.class);
                intent.putExtra("uid", item.t.getUid());
                startActivity(intent);
            }
        });
    }

    private void initSwipeRefresh() {
        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent,
                android.R.color.holo_green_light);
        mSwipeRefresh.setOnRefreshListener(this::loadData);
    }

    private void loadData() {
        RestClient.builder()
                .url(Api.GET_TEAM_USERS)
                .params("uid", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()))
                .success(response -> {
                    String result = JSON.parseObject(response).getString("result");
                    if (!TextUtils.equals(result, "ok")) {
                        Toasty.error(this, "加载失败", Toasty.LENGTH_SHORT).show();
                        return;
                    }
                    List<TeamSection> sections = new ArrayList<>();
                    JSONArray recommend = JSON.parseObject(response).getJSONArray("recommedUser");
                    JSONArray all = JSON.parseObject(response).getJSONArray("all");
                    if (recommend.size() > 0) {
                        sections.add(new TeamSection(true, "推荐组队"));
                    }
                    parse(recommend, sections);
                    sections.add(new TeamSection(true, "全部组队"));
                    parse(all, sections);
                    mAdapter.setNewData(sections);
                    mSwipeRefresh.setRefreshing(false);
                })
                .error((code, msg) -> Toasty.error(this, "加载失败 " + code, Toasty.LENGTH_SHORT).show())
                .build()
                .get();
    }

    private void parse(JSONArray array, List<TeamSection> list) {
        for (int i = 0; i < array.size(); i++) {
            JSONObject object = array.getJSONObject(i);
            long uid = object.getLongValue("uid");
            String username = object.getString("username");
            String gender = object.getString("gender");
            String avatar = object.getString("avatar");
            String background = object.getString("background");
            String college = object.getString("college");
            String introduction = object.getString("message");
            long time = object.getLongValue("time");
            list.add(new TeamSection(
                    new Team(uid, username, gender,
                            avatar, background, college, introduction, time)));
        }
    }

    private void initDialog() {
        MaterialDialog dialog = new MaterialDialog(this, new BottomSheet(LayoutMode.WRAP_CONTENT));
        DialogCustomViewExtKt.customView(dialog, R.layout.dialog_team_bottom,
                null, false, true, false, true);
        LifecycleExtKt.lifecycleOwner(dialog, this);
        dialog.cornerRadius(8f, null);
        dialog.show();

        View customerView = DialogCustomViewExtKt.getCustomView(dialog);
        AppCompatEditText mInfo = customerView.findViewById(R.id.et_info);

        customerView.findViewById(R.id.iv_send).setOnClickListener(v -> {
            String info = Objects.requireNonNull(mInfo.getText()).toString();
            if (info.isEmpty()) {
                Toasty.info(this, "请填写队伍说明", Toasty.LENGTH_SHORT).show();
                return;
            }

            RestClient.builder()
                    .url(Api.CREATE_TEAM)
                    .params("uid", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()))
                    .params("message", info)
                    .success(response -> {
                        String result = JSON.parseObject(response).getString("result");
                        if (TextUtils.equals(result, "ok")) {
                            Toasty.success(this, "申请成功，请留意消息列表", Toasty.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    })
                    .error((code, msg) -> Toasty.error(this, "申请失败 " + code, Toasty.LENGTH_SHORT).show())
                    .build()
                    .post();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_team, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.add_team) {
            initDialog();
        } else if (item.getItemId() == R.id.team_manage) {
            startActivity(new Intent(this, TeamManageActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
