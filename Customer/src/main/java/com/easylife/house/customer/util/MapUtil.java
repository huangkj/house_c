package com.easylife.house.customer.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.cloud.CloudItem;
import com.amap.api.services.core.LatLonPoint;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.util.LogOut;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Mars on 2017/3/27 14:23.
 * 描述：
 */

public class MapUtil {
    public static final String TAG = "MapUtil";
    public static final int ZOOM_HOUSE_DETAIL = 13;

    public static String checkCity(String cityID) {
        if (!TextUtils.isEmpty(cityID)) {
            cityID = cityID.substring(0, 4) + "00";
        }
        return cityID;
    }

    public static String checkParams(String keySearch, CloudItem item) {
        String param = "";
        Iterator iter = item.getCustomfield().entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            if (keySearch.equals(key)) {
                param = val.toString();
            }
        }
        return param;
    }

    public static Bitmap convertViewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(bitmap));
        return bitmap;
    }

    public static LatLng convertToLatLng(LatLonPoint latLonPoint) {
        return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
    }

    public static LatLonPoint convertToLatLngPoint(LatLng latLng) {
        return new LatLonPoint(latLng.latitude, latLng.longitude);
    }

    public static void moveToCurrentLocation(AMap aMap, double lat, double lon) {
        LogOut.d("moveToCurrentLocation : ", lat + " , " + lon);
        aMap.moveCamera(
                CameraUpdateFactory.newCameraPosition(new CameraPosition(
                        new LatLng(lat, lon), ZOOM_HOUSE_DETAIL, 0, 0)));
    }

    public static void moveToPoint(AMap aMap, double lat, double lon, float zoom) {
        LogOut.d("moveToPoint : ", lat + " , " + lon);
        aMap.moveCamera(
                CameraUpdateFactory.newCameraPosition(new CameraPosition(
                        new LatLng(lat, lon), zoom, 0, 0)));
    }

    public static void startNavigation(Activity activity, double latCurrent, double lonCurrent, double lat, double lon, String address) {
        Intent intent = new Intent();
//        if (isInstalled(activity, "com.baidu.BaiduMap")) {
//            try {
//                intent = Intent.parseUri("intent://map/direction?" +
//                        "origin=latlng:" + latCurrent + "," + lonCurrent +
//                        "|name:" + addressCurrent +
//                        "&destination=latlng:" + lat + "," + lon +
//                        "|name:" + address +
//                        "&mode=driving" +
//                        "&src=Name|AppName" +
//                        "#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end", 0);
//            } catch (URISyntaxException e) {
//                e.printStackTrace();
//            }
//            activity.startActivity(intent);
//        } else
        if (isInstalled(activity, "com.autonavi.minimap")) {
            intent.setData(Uri
                    .parse("androidamap://route?" +
                            "sourceApplication=softname" +
                            "&slat=" + latCurrent +
                            "&slon=" + lonCurrent +
                            "&dlat=" + lat +
                            "&dlon=" + lon +
                            "&dname=" + address +
                            "&dev=0" +
                            "&m=0" +
                            "&t=2"));
            activity.startActivity(intent);
        } else {
            CustomerUtils.showTip(activity, "请先安装高德地图");
        }
    }

    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName：应用包名
     * @return
     */
    public static boolean isInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String pkName = packageInfos.get(i).packageName;
                if (pkName.equals(packageName)) return true;
            }
        }
        return false;
    }
}
