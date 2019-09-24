package cn.paulpaulzhang.fair.sc.main.interest.activity;

import android.os.Bundle;
import android.view.MotionEvent;

import androidx.annotation.Nullable;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.gyf.immersionbar.ImmersionBar;

import butterknife.BindView;
import butterknife.OnClick;
import cn.paulpaulzhang.fair.activities.FairActivity;
import cn.paulpaulzhang.fair.app.Fair;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.util.log.FairLogger;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.interest.activity
 * 创建时间：9/23/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class MapActivity extends FairActivity {

    @BindView(R2.id.map_view)
    MapView mMapView;

    private BaiduMap mMap;
    private LocationClient mLocationClient;

    @Override
    public int setLayout() {
        return R.layout.activity_map;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        initMap();
    }

    private void initMap() {
        mMap = mMapView.getMap();
        mMap.setMyLocationEnabled(true);

        mLocationClient = new LocationClient(Fair.getApplicationContext());

        //通过LocationClientOption设置LocationClient相关参数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);


        //设置locationClientOption
        mLocationClient.setLocOption(option);

        //注册LocationListener监听器
        MyLocationListener myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
        //开启地图定位图层
        mLocationClient.start();


    }

    private class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //mapView 销毁后不在处理新接收的位置
            if (bdLocation == null || mMapView == null) {
                return;
            }

            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            if (bdLocation.getFloor() != null) {
                // 当前支持高精度室内定位
                String buildingID = bdLocation.getBuildingID();// 百度内部建筑物ID
                String buildingName = bdLocation.getBuildingName();// 百度内部建筑物缩写
                String floor = bdLocation.getFloor();// 室内定位的楼层信息，如 f1,f2,b1,b2
                mLocationClient.startIndoorMode();// 开启室内定位模式（重复调用也没问题），开启后，定位SDK会融合各种定位信息（GPS,WI-FI，蓝牙，传感器等）连续平滑的输出定位结果；
            }
            navigateTo(bdLocation);

        }
    }

    private boolean isFirst = true;
    private void navigateTo(BDLocation location) {
        LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
        mMap.animateMapStatus(update);

        if (isFirst) {
            update = MapStatusUpdateFactory.zoomTo(19.5f);
            mMap.animateMapStatus(update);
            isFirst = false;
        }

        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())  // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(location.getDirection())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build();
        mMap.setMyLocationData(locData);
    }

    @OnClick(R2.id.iv_back)
    void back() {
        finish();
    }

    @Override
    protected void onDestroy() {
        if (mMapView != null) {
            mLocationClient.stop();
            mMap.setMyLocationEnabled(false);
            mMapView.onDestroy();
            mMapView = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (mMapView != null) {
            mMapView.onPause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mMapView != null) {
            mMapView.onResume();
        }
        super.onResume();
    }
}
