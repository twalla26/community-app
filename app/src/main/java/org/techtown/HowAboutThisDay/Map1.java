package org.techtown.HowAboutThisDay;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import net.daum.mf.map.api.MapView;

public class Map1 extends AppCompatActivity {

    MapView mapView;
    RelativeLayout mapViewContainer1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map1);

            MapView mapView = new MapView(this);

        mapViewContainer1 = (RelativeLayout) findViewById(R.id.map_view);
        mapViewContainer1.addView(mapView);

    }

    }