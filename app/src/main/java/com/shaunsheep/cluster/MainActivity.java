package com.shaunsheep.cluster;

import android.graphics.Color;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.layers.ArcGISVectorTiledLayer;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;

/**
 * update by jiang on 2019/7/23.
 */

public class MainActivity extends AppCompatActivity {
    private MapView mMapView;
    GraphicsOverlay graphicsOverlay = new GraphicsOverlay();
    FeatureLayer featureLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMapView = findViewById(R.id.mapview);
        mMapView.getGraphicsOverlays().add(graphicsOverlay);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initRamdData();
                graphicsOverlay.setVisible(true);
                ClusterLayer clusterLayer = new ClusterLayer(mMapView, graphicsOverlay, getApplicationContext());
                clusterLayer.setGraphicVisible(true);
            }
        });

        FloatingActionButton fabFeatureLayer = findViewById(R.id.fab_feature_layer);
        fabFeatureLayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                featureLayer.setVisible(false);
                ClusterFeatureLayer clusterFeatureLayer = new ClusterFeatureLayer(
                        mMapView, featureLayer, getApplicationContext(), 150
                );
                clusterFeatureLayer.setGraphicVisible(true);
            }
        });

        addtiledLayer();
        addFeatureLayer();
    }

    private void addFeatureLayer() {
        featureLayer = new FeatureLayer(new ServiceFeatureTable(
                "https://sampleserver6.arcgisonline.com/arcgis/rest/services/USA/MapServer/0"));
        mMapView.getMap().getOperationalLayers().add(featureLayer);
    }

    private void addtiledLayer() {
        String tiledUrl = "http://map.geoq.cn/arcgis/rest/services/ChinaOnlineCommunityENG/MapServer";
        ArcGISTiledLayer tiledLayer = new ArcGISTiledLayer(tiledUrl);
        ArcGISMap map = new ArcGISMap(new Basemap(tiledLayer));
        mMapView.setMap(map);
    }

    private void addVecLayer() {
        String vecUrl = "https://basemaps.arcgis.com/arcgis/rest/services/World_Basemap_v2/VectorTileServer";
        ArcGISVectorTiledLayer vectorTiledLayer = new ArcGISVectorTiledLayer(vecUrl);
        ArcGISMap map = new ArcGISMap(new Basemap(vectorTiledLayer));
        mMapView.setMap(map);
    }

    SimpleMarkerSymbol simpleMarkerSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.BLACK, 5);

    private void initRamdData() {
        graphicsOverlay.getGraphics().clear();
        //随机点
        for (int i = 0; i < 1000; i++) {
            double lat = Math.random() + 39.474923;
            double lon = Math.random() + 116.027116;
            Point point = new Point(lon, lat, SpatialReference.create(4326));
            Point graPoint= (Point)GeometryEngine.project(point,mMapView.getSpatialReference());
            Graphic gra1 = new Graphic(graPoint, simpleMarkerSymbol);
            graphicsOverlay.getGraphics().add(gra1);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
