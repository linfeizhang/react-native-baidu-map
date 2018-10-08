package org.lovebing.reactnative.baidumap;
import android.os.Bundle; 
import android.util.Log;
import android.widget.Button;
import android.support.annotation.Nullable;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableArray;
import java.util.ArrayList;
import java.util.List;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;

/**
 * Created by lovebing on Sept 28, 2016.
 */
public class MarkerUtil {

   

    public static void updateMaker(Marker maker, ReadableMap option) {
        LatLng position = getLatLngFromOption(option);
        maker.setPosition(position);
        maker.setTitle(option.getString("title"));
    }

    public static Marker addMarker(MapView mapView, ReadableMap option) {
        int type = option.getInt("type"); 
        int state = option.getInt("state");
        int online = option.getInt("online");
        int hasDevice = option.getInt("hasDevice");
        int flag = option.getInt("flag");
        double condition = option.getDouble("condition");
        int businessState = option.getInt("businessState");
        BitmapDescriptor bitmap = null;

        if(type == 10){//变电站
            bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.marker_elec2);
        }else {
            if(hasDevice == 1){
                 bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.none);
            }else if(type == 11 || type == 15 || type == 16){
                bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.switch_close);
            }else if(type == 17){
                bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.distributed_power);
            }else{
                switch(businessState){
                    case 1 : 
                        if(online == 1){
                            if(condition != 11002 && condition != 0){
                                if(flag == 1){
                                    bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.normal5);
                                }else{
                                    bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.normal1);
                                }
                            }else{
                                bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.normal2);
                            }
                        }else{
                            if(condition != 11002 && condition != 0){
                                if(flag == 1){
                                    bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.normal5);
                                }else{
                                    bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.normal3);
                                }
                            }else{
                                bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.normal4);
                            }
                        }
                    break;
                    case 0 : 
                        if(online == 1){
                            if(condition != 11002 && condition != 0){
                                if(flag == 1){
                                    bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.setting5);
                                }else{
                                    bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.setting1);
                                }
                            }else{
                                bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.setting2);
                            }
                        }else{
                            if(condition != 11002 && condition != 0){
                                if(flag == 1){
                                    bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.setting5);
                                }else{
                                    bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.setting3);
                                }
                            }else{
                                bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.setting4);
                            }
                        }
                    break;
                    case 3 : 
                        if(online == 1){
                            if(condition != 11002 && condition != 0){
                                if(flag == 1){
                                    bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.fix5);
                                }else{
                                    bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.fix1);
                                }
                            }else{
                                bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.fix2);
                            }
                        }else{
                            if(condition != 11002 && condition != 0){
                                if(flag == 1){
                                    bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.fix5);
                                }else{
                                    bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.fix3);
                                }
                            }else{
                                bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.fix4);
                            }
                        }
                    break;
                    case 2 : 
                        if(online == 1){
                            if(condition != 11002 && condition != 0){
                                if(flag == 1){
                                    bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.gaojing5);
                                }else{
                                    bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.gaojing1);
                                }
                            }else{
                                bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.gaojing2);
                            }
                        }else{
                            if(condition != 11002 && condition != 0){
                                if(flag == 1){
                                    bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.gaojing5);
                                }else{
                                    bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.gaojing3);
                                }
                            }else{
                                bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.gaojing4);
                            }
                        }
                    break;
                }
            }
        }
        
        Bundle bundle = new Bundle();
        bundle.putInt("index",option.getInt("index"));
        bundle.putInt("section",option.getInt("section"));
        bundle.putInt("flag",option.getInt("flag"));
        bundle.putInt("type",option.getInt("type"));
        LatLng position = getLatLngFromOption(option);
        OverlayOptions overlayOptions = new MarkerOptions()
                .icon(bitmap)
                .position(position)
                .title(option.getString("title"))
                .extraInfo(bundle);


        Marker marker = (Marker)mapView.getMap().addOverlay(overlayOptions);
        return marker;
    }

    private static LatLng getLatLngFromOption(ReadableMap option) {
        double latitude = option.getDouble("latitude");
        double longitude = option.getDouble("longitude");
        return new LatLng(latitude, longitude);

    }
}
