package org.lovebing.reactnative.baidumap;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.os.Bundle;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.baidu.mapapi.map.MarkerOptions;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.utils.DistanceUtil;

/**
 * Created by lovebing on 12/20/2015.
 */
public class BaiduMapViewManager extends ViewGroupManager<MapView> {

    private static final String REACT_CLASS = "RCTBaiduMapView";

    private ThemedReactContext mReactContext;

    private ReadableArray childrenPoints;
    private HashMap<String, Marker> mMarkerMap = new HashMap<>();
    private HashMap<String, List<Marker>> mMarkersMap = new HashMap<>();
    private List<OverlayOptions> OverlayOptions = new ArrayList<>();
    private List<OverlayOptions> OverlayPolylines = new ArrayList<>();
    private List<Marker> mMarkers = new ArrayList<>();
    private TextView mMarkerText;

    private Polyline mMarkerPolyLine;
    private InfoWindow infoWindow = null;
    private BitmapDescriptor textView = null;

    public String getName() {
        return REACT_CLASS;
    }

    public void initSDK(Context context) {
        SDKInitializer.initialize(context);
    }

    public MapView createViewInstance(ThemedReactContext context) {
        mReactContext = context;
        MapView mapView = new MapView(context);
        setListeners(mapView);
        return mapView;
    }

    @Override
    public void addView(MapView parent, View child, int index) {
        if (childrenPoints != null) {
            Point point = new Point();
            ReadableArray item = childrenPoints.getArray(index);
            if (item != null) {
                point.set(item.getInt(0), item.getInt(1));
                MapViewLayoutParams mapViewLayoutParams = new MapViewLayoutParams.Builder()
                        .layoutMode(MapViewLayoutParams.ELayoutMode.absoluteMode).point(point).build();
                parent.addView(child, mapViewLayoutParams);
            }
        }

    }

    @ReactProp(name = "zoomControlsVisible")
    public void setZoomControlsVisible(MapView mapView, boolean zoomControlsVisible) {
        mapView.showZoomControls(zoomControlsVisible);
    }

    @ReactProp(name = "trafficEnabled")
    public void setTrafficEnabled(MapView mapView, boolean trafficEnabled) {
        mapView.getMap().setTrafficEnabled(trafficEnabled);
    }

    @ReactProp(name = "baiduHeatMapEnabled")
    public void setBaiduHeatMapEnabled(MapView mapView, boolean baiduHeatMapEnabled) {
        mapView.getMap().setBaiduHeatMapEnabled(baiduHeatMapEnabled);
    }

    @ReactProp(name = "mapType")
    public void setMapType(MapView mapView, int mapType) {
        mapView.getMap().setMapType(mapType);
    }

    @ReactProp(name = "zoom")
    public void setZoom(MapView mapView, float zoom) {
        MapStatus mapStatus = new MapStatus.Builder().zoom(zoom).build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        mapView.getMap().setMapStatus(mapStatusUpdate);
    }

    @ReactProp(name = "zoomlevel")
    public void setzoomlevel(MapView mapView, ReadableMap position) {
        /*
         * MapStatus mapStatus = new MapStatus.Builder().zoom(zoom).build();
         * MapStatusUpdate mapStatusUpdate = MapStatusUpdateFac
         * tory.newMapStatus(mapStatus); mapView.getMap().setMapStatus(mapStatusUpdate);
         */
        if (position != null) {
            int zoomLevel[] = { 2000000, 1000000, 500000, 200000, 100000, 50000, 25000, 20000, 10000, 5000, 2000, 1000,
                    500, 100, 50, 20, 0 };

            double maxlat = position.getDouble("maxlat");
            double minlat = position.getDouble("minlat");
            double maxlon = position.getDouble("maxlon");
            double minlon = position.getDouble("minlon");

            final double midlat = (maxlat + minlat) / 2;
            final double midlon = (maxlon + minlon) / 2;
            LatLng latlon = new LatLng(midlat, midlon);
            int jl = (int) DistanceUtil.getDistance(new LatLng(maxlat, maxlon), new LatLng(minlat, minlon));
            int i;
            for (i = 0; i < 17; i++) {
                if (zoomLevel[i] < jl) {
                    break;
                }
            }
            float zoom = i + 5;

            MapStatus mapStatus = new MapStatus.Builder().zoom(zoom).build();
            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
            mapView.getMap().setMapStatus(mapStatusUpdate);
        }
    }

    @ReactProp(name = "center")
    public void setCenter(MapView mapView, ReadableMap position) {
        if (position != null) {
            double latitude = position.getDouble("latitude");
            double longitude = position.getDouble("longitude");
            LatLng point = new LatLng(latitude, longitude);
            MapStatus mapStatus = new MapStatus.Builder().target(point).build();
            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
            mapView.getMap().setMapStatus(mapStatusUpdate);
        }
    }

    @ReactProp(name = "marker")
    public void setMarker(MapView mapView, ReadableMap option) {
        if (option != null) {
            String key = "marker_" + mapView.getId();
            Marker marker = mMarkerMap.get(key);
            if (marker != null) {
                MarkerUtil.updateMaker(marker, option);
            } else {
                marker = MarkerUtil.addMarker(mapView, option);
                mMarkerMap.put(key, marker);
            }
        }
    }

    // 添加markers
    @ReactProp(name = "markers")
    public void setMarkers(MapView mapView, ReadableArray options) {
        if (options != null && options.size() > 0) {
            mapView.getMap().clear();
            for (int i = 0; i < options.size(); i++) {
                ReadableMap option = options.getMap(i);
                Map optionMap = option.toHashMap();

                int type = option.getInt("type");
                int state = option.getInt("state");
                int online = option.getInt("online");
                int hasDevice = option.getInt("hasDevice");
                int flag = option.getInt("flag");
                double condition = option.getDouble("condition");
                int businessState = option.getInt("businessState");
                BitmapDescriptor bitmap = null;

                if (type == 10) {// 变电站
                    bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.marker_elec2);
                } else {
                    if (hasDevice == 1) {
                        bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.none);
                    } else if (type == 11 || type == 15 || type == 16) {
                        bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.switch_close);
                    } else if (type == 17) {
                        bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.distributed_power);
                    } else {
                        switch (businessState) {
                        case 1:
                            if (online == 1) {
                                if (condition != 11002 && condition != 0) {
                                    if (flag == 1) {
                                        bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.normal5);
                                    } else {
                                        bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.normal1);
                                    }
                                } else {
                                    bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.normal2);
                                }
                            } else {
                                if (condition != 11002 && condition != 0) {
                                    if (flag == 1) {
                                        bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.normal5);
                                    } else {
                                        bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.normal3);
                                    }
                                } else {
                                    bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.normal4);
                                }
                            }
                            break;
                        case 0:
                            if (online == 1) {
                                if (condition != 11002 && condition != 0) {
                                    if (flag == 1) {
                                        bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.setting5);
                                    } else {
                                        bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.setting1);
                                    }
                                } else {
                                    bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.setting2);
                                }
                            } else {
                                if (condition != 11002 && condition != 0) {
                                    if (flag == 1) {
                                        bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.setting5);
                                    } else {
                                        bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.setting3);
                                    }
                                } else {
                                    bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.setting4);
                                }
                            }
                            break;
                        case 3:
                            if (online == 1) {
                                if (condition != 11002 && condition != 0) {
                                    if (flag == 1) {
                                        bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.fix5);
                                    } else {
                                        bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.fix1);
                                    }
                                } else {
                                    bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.fix2);
                                }
                            } else {
                                if (condition != 11002 && condition != 0) {
                                    if (flag == 1) {
                                        bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.fix5);
                                    } else {
                                        bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.fix3);
                                    }
                                } else {
                                    bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.fix4);
                                }
                            }
                            break;
                        case 2:
                            if (online == 1) {
                                if (condition != 11002 && condition != 0) {
                                    if (flag == 1) {
                                        bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.gaojing5);
                                    } else {
                                        bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.gaojing1);
                                    }
                                } else {
                                    bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.gaojing2);
                                }
                            } else {
                                if (condition != 11002 && condition != 0) {
                                    if (flag == 1) {
                                        bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.gaojing5);
                                    } else {
                                        bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.gaojing3);
                                    }
                                } else {
                                    bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.gaojing4);
                                }
                            }
                            break;
                        }
                    }
                }

                Bundle bundle = new Bundle();
                bundle.putInt("index", option.getInt("index"));
                bundle.putInt("section", option.getInt("section"));
                bundle.putInt("flag", option.getInt("flag"));
                bundle.putInt("type", option.getInt("type"));

                if (optionMap.containsKey("latitude") && optionMap.containsKey("longitude")) {
                    LatLng position = getLatLngFromOption(option);
                    OverlayOptions overlayOption = new MarkerOptions().icon(bitmap).position(position)
                            .title(option.getString("title")).perspective(true)// 是否开启近大远小效果
                            .extraInfo(bundle);// 额外信息
                    OverlayOptions.add(overlayOption);
                    Marker marker = (Marker) mapView.getMap().addOverlay(overlayOption);

                }
            }
        }

    }

    private static LatLng getLatLngFromOption(ReadableMap option) {
        double latitude = option.getDouble("latitude");
        double longitude = option.getDouble("longitude");
        return new LatLng(latitude, longitude);

    }

    // @ReactProp(name="markers")
    // public void setMarkers(MapView mapView, ReadableArray options) {
    // for (int i = 0; i < options.size(); i++) {
    // ReadableMap option = options.getMap(i);
    // if(mMarkers.size() > i + 1 && mMarkers.get(i) != null) {
    // MarkerUtil.updateMaker(mMarkers.get(i), option);
    // }
    // else {
    // mMarkers.add(i, MarkerUtil.addMarker(mapView, option));
    // }
    // }
    // if(options.size() < mMarkers.size()) {
    // int start = options.size();
    // for (int i = start; i < mMarkers.size(); i++) {
    // mMarkers.get(i).remove();
    // mMarkers.remove(i);
    // }
    // }
    // }
    @ReactProp(name = "polyline")
    public void setPolyline(MapView mapView, ReadableArray options) {
        if (options != null && options.size() > 0) {
            String linkInfo = "";
            String linkInfos = "";
            for (int i = 0; i < options.size(); i++) {
                ReadableMap position_ = options.getMap(i);
                if (linkInfo.equals(position_.getString("linkInfo"))) {

                } else {
                    linkInfo = position_.getString("linkInfo");
                    linkInfos = linkInfos + position_.getString("linkInfo");
                }
            }
            // ReadableMap position_ = options.getMap(0);
            // String linkInfo = position_.getString("linkInfo");// '0,4;0,3;0,2;0,1;'
            String[] strArray = linkInfos.split(";");

            for (int i = 0; i < strArray.length; i++) {
                String str = strArray[i];// 0,4
                String[] str_ = str.split(",");
                String start = str_[0];
                String end = str_[1];
                String flag = str_[2];

                List<LatLng> pts = new ArrayList<LatLng>();

                for (int j = 0; j < options.size(); j++) {

                    ReadableMap position = options.getMap(j);
                    Map positionMap = position.toHashMap();
                    if (positionMap.containsKey("latitude") && positionMap.containsKey("longitude")) {
                        double latitude = position.getDouble("latitude");
                        double longitude = position.getDouble("longitude");
                        int link = position.getInt("link");
                        String links = link + "";
                        if (links.equals(start) || links.equals(end)) {
                            pts.add(new LatLng(latitude, longitude));
                        }
                    }
                }
                if (pts.size() == 2) {
                    if (Integer.parseInt(flag) == 0) {// 红色线
                        OverlayOptions ooPolyline = new PolylineOptions().points(pts).width(6).color(0xFFFF0000);
                        mMarkerPolyLine = (Polyline) mapView.getMap().addOverlay(ooPolyline);
                        OverlayPolylines.add(ooPolyline);
                    } else {
                        OverlayOptions ooPolyline = new PolylineOptions().points(pts).width(6).color(0xFF4682B4);
                        mMarkerPolyLine = (Polyline) mapView.getMap().addOverlay(ooPolyline);
                        OverlayPolylines.add(ooPolyline);
                    }
                }
            }
        }
    }

    @ReactProp(name = "childrenPoints")
    public void setChildrenPoints(MapView mapView, ReadableArray childrenPoints) {
        this.childrenPoints = childrenPoints;
    }

    /**
     *
     * @param mapView
     */
    private void setListeners(final MapView mapView) {
        BaiduMap map = mapView.getMap();

        if (mMarkerText == null) {
            mMarkerText = new TextView(mapView.getContext());
            mMarkerText.setBackgroundResource(R.drawable.popup);
            mMarkerText.setPadding(32, 32, 32, 32);
        }
        map.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {

            private WritableMap getEventParams(MapStatus mapStatus) {
                WritableMap writableMap = Arguments.createMap();
                WritableMap target = Arguments.createMap();
                target.putDouble("latitude", mapStatus.target.latitude);
                target.putDouble("longitude", mapStatus.target.longitude);
                writableMap.putMap("target", target);
                writableMap.putDouble("zoom", mapStatus.zoom);
                writableMap.putDouble("overlook", mapStatus.overlook);
                return writableMap;
            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {
                sendEvent(mapView, "onMapStatusChangeStart", getEventParams(mapStatus));
            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {
                sendEvent(mapView, "onMapStatusChange", getEventParams(mapStatus));
            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                if (mMarkerText.getVisibility() != View.GONE) {
                    mMarkerText.setVisibility(View.GONE);
                }
                sendEvent(mapView, "onMapStatusChangeFinish", getEventParams(mapStatus));
            }
        });

        map.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                sendEvent(mapView, "onMapLoaded", null);
            }
        });

        map.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mapView.getMap().hideInfoWindow();
                WritableMap writableMap = Arguments.createMap();
                writableMap.putDouble("latitude", latLng.latitude);
                writableMap.putDouble("longitude", latLng.longitude);
                sendEvent(mapView, "onMapClick", writableMap);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                WritableMap writableMap = Arguments.createMap();
                writableMap.putString("name", mapPoi.getName());
                writableMap.putString("uid", mapPoi.getUid());
                writableMap.putDouble("latitude", mapPoi.getPosition().latitude);
                writableMap.putDouble("longitude", mapPoi.getPosition().longitude);
                sendEvent(mapView, "onMapPoiClick", writableMap);
                return true;
            }
        });
        map.setOnMapDoubleClickListener(new BaiduMap.OnMapDoubleClickListener() {
            @Override
            public void onMapDoubleClick(LatLng latLng) {
                WritableMap writableMap = Arguments.createMap();
                writableMap.putDouble("latitude", latLng.latitude);
                writableMap.putDouble("longitude", latLng.longitude);
                sendEvent(mapView, "onMapDoubleClick", writableMap);
            }
        });

        map.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                int flag = marker.getExtraInfo().getInt("flag");
                int type = marker.getExtraInfo().getInt("type");
                if (marker.getTitle().length() > 0) {

                    mMarkerText = new TextView(mapView.getContext());
                    mMarkerText.setBackgroundResource(R.drawable.popup);
                    mMarkerText.setPadding(32, 32, 32, 32);
                    mMarkerText.setText(marker.getTitle());

                    textView = BitmapDescriptorFactory.fromView(mMarkerText);
                    InfoWindow.OnInfoWindowClickListener listener = null;

                    double latitude = marker.getPosition().latitude;
                    double longitude = marker.getPosition().longitude;
                    LatLng position = new LatLng(latitude, longitude);

                    infoWindow = new InfoWindow(textView, position, -75, listener);

                    if (flag == 1) {
                        mapView.getMap().showInfoWindow(infoWindow);
                    } else {
                        if (type == 12) {
                            mapView.getMap().hideInfoWindow();
                        } else {
                            mapView.getMap().showInfoWindow(infoWindow);
                        }
                    }

                } else {
                    mapView.getMap().hideInfoWindow();
                }
                WritableMap writableMap = Arguments.createMap();
                WritableMap position = Arguments.createMap();
                position.putDouble("latitude", marker.getPosition().latitude);
                position.putDouble("longitude", marker.getPosition().longitude);
                writableMap.putMap("position", position);
                writableMap.putString("title", marker.getTitle());
                writableMap.putInt("index", marker.getExtraInfo().getInt("index"));
                writableMap.putInt("section", marker.getExtraInfo().getInt("section"));
                if (flag == 1) {
                    sendEvent(mapView, "onMarkerClick", writableMap);
                } else {
                    if (type == 12) {
                        sendEvent(mapView, "onMarkerClick", writableMap);
                    }
                }
                return true;
            }
        });

    }

    /**
     *
     * @param eventName
     * @param params
     */
    private void sendEvent(MapView mapView, String eventName, @Nullable WritableMap params) {
        WritableMap event = Arguments.createMap();
        event.putMap("params", params);
        event.putString("type", eventName);
        mReactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mapView.getId(), "topChange", event);
    }
}
