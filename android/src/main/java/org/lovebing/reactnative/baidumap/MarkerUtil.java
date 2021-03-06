package org.lovebing.reactnative.baidumap;

import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.os.Bundle;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.facebook.react.bridge.ReadableMap;

/**
 * Created by lovebing on Sept 28, 2016.
 */
public class MarkerUtil {
    public static void updateMaker(Marker maker, ReadableMap option, Context ctx) {
        int type = option.getInt("type");
        int state = option.getInt("state");
        int online = option.getInt("online");
        int hasDevice = option.getInt("hasDevice");
        int flag = option.getInt("flag");
        double condition = option.getDouble("condition");
        int businessState = option.getInt("businessState");

        BitmapDescriptor bitmap = null;
        String imageName = "";
        if (type == 10) {// 变电站
            imageName = "marker_elec2";
        } else {
            if (hasDevice == 1) {
                imageName = "none";
            } else if (type == 11 || type == 15 || type == 16) {
                imageName = "switch_close";
            } else if (type == 17) {
                imageName = "distributed_power";
            } else {
                switch (businessState) {
                case 1:
                    imageName = "normal";
                    break;
                case 0:
                    imageName = "setting";
                    break;
                case 3:
                    imageName = "fix";
                    break;
                case 2:
                    imageName = "gaojing";
                    break;
                }
                if (online == 1) {
                    if (condition != 11002 && condition != 11001 && condition != 0) {
                        if (flag == 1) {
                            imageName = imageName + "5";
                        } else {
                            imageName = imageName + "1";
                        }
                    } else {
                        imageName = imageName + "2";
                    }
                } else {
                    if (condition != 11002 && condition != 11001 && condition != 0) {
                        if (flag == 1) {
                            imageName = imageName + "5";
                        } else {
                            imageName = imageName + "3";
                        }
                    } else {
                        if (flag == 1) {
                            imageName = imageName + "2";
                        } else {
                            imageName = imageName + "4";
                        }
                    }
                }
            }
        }

        int resId = ctx.getResources().getIdentifier(imageName, "mipmap", ctx.getPackageName());
        bitmap = BitmapDescriptorFactory.fromResource(resId);

        Bundle bundle = new Bundle();
        bundle.putInt("index", option.getInt("index"));
        bundle.putInt("section", option.getInt("section"));
        bundle.putInt("flag", option.getInt("flag"));
        bundle.putInt("type", option.getInt("type"));
        LatLng position = getLatLngFromOption(option);

        maker.setPosition(position);
        maker.setTitle(option.getString("title"));
        maker.setIcon(bitmap);
        maker.setPerspective(true);
        maker.setExtraInfo(bundle);

        // OverlayOptions overlayOptions = new
        // MarkerOptions().icon(bitmap).position(position)
        // .title(option.getString("title")).perspective(true)// 是否开启近大远小效果
        // .extraInfo(bundle);// 额外信息

        // maker = (Marker) mapView.getMap().addOverlay(overlayOptions);
    }

    public static Marker addMarker(MapView mapView, ReadableMap option) {
        int type = option.getInt("type");
        int state = option.getInt("state");
        int online = option.getInt("online");
        int hasDevice = option.getInt("hasDevice");
        int flag = option.getInt("flag");
        double condition = option.getDouble("condition");
        int businessState = option.getInt("businessState");

        Context ctx = mapView.getContext();
        BitmapDescriptor bitmap = null;
        String imageName = "";
        if (type == 10) {// 变电站
            imageName = "marker_elec2";
        } else {
            if (hasDevice == 1) {
                imageName = "none";
            } else if (type == 11 || type == 15 || type == 16) {
                imageName = "switch_close";
            } else if (type == 17) {
                imageName = "distributed_power";
            } else {
                switch (businessState) {
                case 1:
                    imageName = "normal";
                    break;
                case 0:
                    imageName = "setting";
                    break;
                case 3:
                    imageName = "fix";
                    break;
                case 2:
                    imageName = "gaojing";
                    break;
                }
                if (online == 1) {
                    if (condition != 11002 && condition != 11001 && condition != 0) {
                        if (flag == 1) {
                            imageName = imageName + "5";
                        } else {
                            imageName = imageName + "1";
                        }
                    } else {
                        imageName = imageName + "2";
                    }
                } else {
                    if (condition != 11002 && condition != 11001 && condition != 0) {
                        if (flag == 1) {
                            imageName = imageName + "5";
                        } else {
                            imageName = imageName + "3";
                        }
                    } else {
                        if (flag == 1) {
                            imageName = imageName + "2";
                        } else {
                            imageName = imageName + "4";
                        }
                    }
                }
            }
        }

        int resId = ctx.getResources().getIdentifier(imageName, "mipmap", ctx.getPackageName());
        bitmap = BitmapDescriptorFactory.fromResource(resId);

        Bundle bundle = new Bundle();
        bundle.putInt("index", option.getInt("index"));
        bundle.putInt("section", option.getInt("section"));
        bundle.putInt("flag", option.getInt("flag"));
        bundle.putInt("type", option.getInt("type"));
        LatLng position = getLatLngFromOption(option);
        OverlayOptions overlayOptions = new MarkerOptions().icon(bitmap).position(position)
                .title(option.getString("title")).perspective(true)// 是否开启近大远小效果
                .extraInfo(bundle);// 额外信息

        Marker marker = (Marker) mapView.getMap().addOverlay(overlayOptions);
        return marker;
    }

    private static LatLng getLatLngFromOption(ReadableMap option) {
        double latitude = option.getDouble("latitude");
        double longitude = option.getDouble("longitude");
        return new LatLng(latitude, longitude);

    }
}
