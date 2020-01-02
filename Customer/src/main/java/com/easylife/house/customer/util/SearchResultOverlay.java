package com.easylife.house.customer.util;


import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.easylife.house.customer.bean.HousesDetailBaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 云图描点封装：搜索结果
 */
public class SearchResultOverlay {
    private List<HousesDetailBaseBean> mPois;
    private AMap mAMap;
    private ArrayList<Marker> mPoiMarks = new ArrayList<Marker>();

    public SearchResultOverlay(AMap amap, List<HousesDetailBaseBean> pois) {
        mAMap = amap;
        mPois = pois;
    }

    public void addToMap() {
        for (int i = 0; i < mPois.size(); i++) {
            Marker marker = mAMap.addMarker(getMarkerOptions(i));
            marker.setObject(i);
            mPoiMarks.add(marker);
        }
    }

    public void removeFromMap() {
        for (Marker mark : mPoiMarks) {
            mark.remove();
        }
    }

    public void zoomToSpan() {
        if (mPois != null && mPois.size() > 0) {
            if (mAMap == null)
                return;
            LatLngBounds bounds = getLatLngBounds();
            mAMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));
        }
    }

    private LatLngBounds getLatLngBounds() {
        LatLngBounds.Builder b = LatLngBounds.builder();
        for (int i = 0; i < mPois.size(); i++) {
            if (mPois.get(i).projectCoordinate != null && mPois.get(i).projectCoordinate.size() == 2) {
                b.include(new LatLng(mPois.get(i).projectCoordinate.get(1),
                        mPois.get(i).projectCoordinate.get(0)));
            }
        }
        return b.build();
    }

    private MarkerOptions getMarkerOptions(int i) {
        if (mPois.get(i).projectCoordinate != null && mPois.get(i).projectCoordinate.size() == 2) {
            return new MarkerOptions()
                    .position(
                            new LatLng(mPois.get(i).projectCoordinate.get(1), mPois.get(i).projectCoordinate.get(0)))
                    .title(getTitle(i)).snippet(getSnippet(i))
                    .icon(getBitmapDescriptor(i));
        } else {
            return null;
        }
    }

    protected BitmapDescriptor getBitmapDescriptor(int index) {
        return null;
    }

    protected String getTitle(int index) {
        return mPois.get(index).devName;
    }

    protected String getSnippet(int index) {
        return mPois.get(index).addressDetail;
    }

    public int getPoiIndex(Marker marker) {
        for (int i = 0; i < mPoiMarks.size(); i++) {
            if (mPoiMarks.get(i).equals(marker)) {
                return i;
            }
        }
        return -1;
    }

    public HousesDetailBaseBean getPoiItem(int index) {
        if (index < 0 || index >= mPois.size()) {
            return null;
        }
        return mPois.get(index);
    }
}
