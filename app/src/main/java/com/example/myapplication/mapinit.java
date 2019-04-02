package com.example.myapplication;

import android.content.ContentValues;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.MyLocationOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.SimpleLocationOverlay;
import org.osmdroid.views.overlay.TilesOverlay;

import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.ViewGroup;

import java.util.ArrayList;
import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

public class mapinit extends AppCompatActivity {

    private MapView map;
    private SimpleLocationOverlay mMyLocationOverlay;
    private ScaleBarOverlay mScaleBarOverlay;
    ItemizedIconOverlay<OverlayItem> currentLocationOverlay;
    DefaultResourceProxyImpl resourceProxy;
    private MyLocationOverlay myLocationoverlay;

    ArrayList<OverlayItem> upload()
    {
        ArrayList<OverlayItem> overlay=new ArrayList<OverlayItem>();
        ArrayList<String> longitude=new ArrayList<String>();
        ArrayList<String> latitude=new ArrayList<String>();
        ContentValues val;
        SQLiteDatabase db;


        DatabaseSQLite obj=new DatabaseSQLite(this);
        db=obj.getReadableDatabase();

        String projection[]={"time","lat","longi"};

        Cursor c=db.query("first1",projection,null,null,null,null,null);


        c.moveToPosition(0);

        do {


            latitude.add(c.getString(1));
             longitude.add(c.getString(2));
        }
        while (c.moveToNext());
//        if(cur.moveToFirst())
//        {
//            do.
//            {
//                String temp_long=cur.getString(cur.getColumnIndex("longitude"));
//                String temp_lat=cur.getString(cur.getColumnIndex("latitude"));
//                longitude.add(temp_long);
//                latitude.add(temp_lat);
//            }
//            while(cur.moveToNext());
//            cur.close();
//        }
        for(int i=0;i<longitude.size();i++)
        {
            GeoPoint geo=new GeoPoint(Double.parseDouble(latitude.get(i)),Double.parseDouble(longitude.get(i)));
            OverlayItem over=new OverlayItem("Here", "Location", geo);
            Drawable draw=getResources().getDrawable(R.drawable.ic_launcher);
            over.setMarker(draw);
            overlay.add(over);
        }
        return overlay;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapinit);
        map = (MapView) findViewById(R.id.map);

        map.setTileSource(new XYTileSource("OSMPublicTransport",ResourceProxy.string.public_transport, 0, 16, 256, ".png", new String[] {
                "http://otile1.mqcdn.com/tiles/1.0.0/map/",
                "http://otile2.mqcdn.com/tiles/1.0.0/map/",
                "http://otile3.mqcdn.com/tiles/1.0.0/map/",
                "http://otile4.mqcdn.com/tiles/1.0.0/map/"}));
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        map.setUseDataConnection(false);
        IMapController mapController = map.getController();
        mapController.setZoom(15);
        LocationManager locationManger=(LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        Location lastLocation=locationManger.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        GeoPoint startPoint;
        if(lastLocation==null)
        {
            startPoint = new GeoPoint(22.5567,88.3022);
        }
        else
        {
            startPoint = new GeoPoint(lastLocation.getLatitude(),lastLocation.getLongitude());
            //!sr
        }

        mapController.setCenter(startPoint);

        this.mMyLocationOverlay = new SimpleLocationOverlay(this);
        map.getOverlays().add(mMyLocationOverlay);
        this.mScaleBarOverlay = new ScaleBarOverlay(this);
        map.getOverlays().add(mScaleBarOverlay);
        resourceProxy = new DefaultResourceProxyImpl(getApplicationContext());
        GeoPoint  currentLocation = new GeoPoint(22.5567,88.3022);
        GeoPoint  currentLocation2 = new GeoPoint(22.513365,88.403003);
        OverlayItem myLocationOverlayItem = new OverlayItem("Here", "Current Position", currentLocation);
        Drawable myCurrentLocationMarker = this.getResources().getDrawable(R.drawable.ic_launcher);
        myLocationOverlayItem.setMarker(myCurrentLocationMarker);


    }
}