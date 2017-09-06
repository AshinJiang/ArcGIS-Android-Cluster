package com.shaunsheep.cluster;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.layers.ArcGISMapImageLayer;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.layers.ArcGISVectorTiledLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.portal.PortalItem;
import com.esri.arcgisruntime.symbology.SimpleMarkerSceneSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;

public class MainActivity extends AppCompatActivity {
    private MapView mMapView;
    GraphicsOverlay graphicsOverlay = new GraphicsOverlay();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMapView = findViewById(R.id.mapview);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                graphicsOverlay.setVisible(false);
                ClusterLayer clusterLayer = new ClusterLayer(mMapView, graphicsOverlay, getApplicationContext());
            }
        });

        addVecLayer();
        initRamdData();
    }

    private void addVecLayer() {
        String vecUrl = "https://basemaps.arcgis.com/v1/arcgis/rest/services/World_Basemap_WGS84/VectorTileServer";
        ArcGISVectorTiledLayer vectorTiledLayer = new ArcGISVectorTiledLayer(vecUrl);
        ArcGISMap map = new ArcGISMap(new Basemap(vectorTiledLayer));
        mMapView.setMap(map);
    }
    SimpleMarkerSymbol simpleMarkerSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.BLACK, 5);

    private void initRamdData(){
        //随机点
        for (int i = 0; i < 1000; i++) {
            double lat = Math.random() + 39.474923;
            double lon = Math.random() + 116.027116;
            Point point = new Point(lon, lat, mMapView.getSpatialReference());
            Graphic gra1 = new Graphic(point, simpleMarkerSymbol);
            graphicsOverlay.getGraphics().add(gra1);
        }
        mMapView.getGraphicsOverlays().add(graphicsOverlay);
    }

    private void initData() {
        SimpleMarkerSymbol simpleMarkerSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.BLACK, 10);
        Point Point1 = new Point(116.318988, 39.976318, SpatialReference.create(4326));
        Graphic gra1 = new Graphic(Point1, simpleMarkerSymbol);
        Point Point2 = new Point(116.304504, 40.045813, SpatialReference.create(4326));
        Graphic gra2 = new Graphic(Point2, simpleMarkerSymbol);
        Point Point3 = new Point(116.507537, 39.923347, SpatialReference.create(4326));
        Graphic gra3 = new Graphic(Point3, simpleMarkerSymbol);
        Point Point4 = new Point(116.477378, 39.91125, SpatialReference.create(4326));
        Graphic gra4 = new Graphic(Point4, simpleMarkerSymbol);
        Point Point5 = new Point(116.312181, 40.041337, SpatialReference.create(4326));
        Graphic gra5 = new Graphic(Point5, simpleMarkerSymbol);
        Point Point6 = new Point(116.306446, 39.971879, SpatialReference.create(4326));
        Graphic gra6 = new Graphic(Point6, simpleMarkerSymbol);
        Point Point7 = new Point(116.463232, 40.002434, SpatialReference.create(4326));
        Graphic gra7 = new Graphic(Point7, simpleMarkerSymbol);
        Point Point8 = new Point(116.331772, 39.980959, SpatialReference.create(4326));
        Graphic gra8 = new Graphic(Point8, simpleMarkerSymbol);
        Point Point9 = new Point(116.508567, 39.925659, SpatialReference.create(4326));
        Graphic gra9 = new Graphic(Point9, simpleMarkerSymbol);
        Point Point10 = new Point(116.461778, 39.88543, SpatialReference.create(4326));
        Graphic gra10 = new Graphic(Point10, simpleMarkerSymbol);
        Point Point11 = new Point(116.311843, 39.98343, SpatialReference.create(4326));
        Graphic gra11 = new Graphic(Point11, simpleMarkerSymbol);
        Point Point12 = new Point(116.313302, 40.029849, SpatialReference.create(4326));
        Graphic gra12 = new Graphic(Point12, simpleMarkerSymbol);
        Point Point13 = new Point(115.997571, 36.467939, SpatialReference.create(4326));
        Graphic gra13 = new Graphic(Point13, simpleMarkerSymbol);
        Point Point14 = new Point(118.81726, 36.857609, SpatialReference.create(4326));
        Graphic gra14 = new Graphic(Point14, simpleMarkerSymbol);
        Point Point15 = new Point(118.813483, 36.857369, SpatialReference.create(4326));
        Graphic gra15 = new Graphic(Point15, simpleMarkerSymbol);
        Point Point16 = new Point(118.802071, 36.86053, SpatialReference.create(4326));
        Graphic gra16 = new Graphic(Point16, simpleMarkerSymbol);
        Point Point17 = new Point(118.776876, 36.857094, SpatialReference.create(4326));
        Graphic gra17 = new Graphic(Point17, simpleMarkerSymbol);
        Point Point18 = new Point(118.7707, 36.85641, SpatialReference.create(4326));
        Graphic gra18 = new Graphic(Point18, simpleMarkerSymbol);
        Point Point19 = new Point(110.757545, 19.603086, SpatialReference.create(4326));
        Graphic gra19 = new Graphic(Point19, simpleMarkerSymbol);

        graphicsOverlay.getGraphics().add(gra1);
        graphicsOverlay.getGraphics().add(gra2);
        graphicsOverlay.getGraphics().add(gra3);
        graphicsOverlay.getGraphics().add(gra4);
        graphicsOverlay.getGraphics().add(gra5);
        graphicsOverlay.getGraphics().add(gra6);
        graphicsOverlay.getGraphics().add(gra7);
        graphicsOverlay.getGraphics().add(gra8);
        graphicsOverlay.getGraphics().add(gra9);
        graphicsOverlay.getGraphics().add(gra10);
        graphicsOverlay.getGraphics().add(gra11);
        graphicsOverlay.getGraphics().add(gra12);
        graphicsOverlay.getGraphics().add(gra13);
        graphicsOverlay.getGraphics().add(gra14);
        graphicsOverlay.getGraphics().add(gra15);
        graphicsOverlay.getGraphics().add(gra16);
        graphicsOverlay.getGraphics().add(gra17);
        graphicsOverlay.getGraphics().add(gra18);
        graphicsOverlay.getGraphics().add(gra19);

        mMapView.getGraphicsOverlays().add(graphicsOverlay);
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
