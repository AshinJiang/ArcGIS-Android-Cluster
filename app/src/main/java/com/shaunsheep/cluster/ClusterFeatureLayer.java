package com.shaunsheep.cluster;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureQueryResult;
import com.esri.arcgisruntime.data.QueryParameters;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.Polygon;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapScaleChangedEvent;
import com.esri.arcgisruntime.mapping.view.MapScaleChangedListener;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.CompositeSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.Symbol;
import com.esri.arcgisruntime.symbology.TextSymbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * update by jiang on 2019/7/24.
 */

public class ClusterFeatureLayer {
    private int _clusterTolerance = 150;

    private double _clusterResolution;

    private MapView _mapView;

    private FeatureLayer _featureLayer;

    private GraphicsOverlay _GraphicsOverlay;

    private GraphicsOverlay _clusterGraphicsOverlay;

    private List<Map<String, Object>> _clusterData;

    private Context _context;

    private int clusterID = 0;

    ArrayList<Graphic> _clusterGraphics = new ArrayList<Graphic>();

    public ClusterFeatureLayer(final MapView mapView, FeatureLayer featureLayer, Context context
            , int clusterTolerance) {
        if (mapView == null || featureLayer == null) {
            return;
        }
        this._clusterTolerance = clusterTolerance;
        this._clusterResolution = _getExtent(mapView.getVisibleArea()).getWidth()
                / mapView.getWidth();
        this._mapView = mapView;
        this._featureLayer = featureLayer;
        this._clusterData = new ArrayList<>();
        this._GraphicsOverlay = new GraphicsOverlay();
        this._clusterGraphicsOverlay = new GraphicsOverlay();
        this._mapView.getGraphicsOverlays().add(this._clusterGraphicsOverlay);
        this._context = context;

        this._clusterFeatures();

        mapView.addMapScaleChangedListener(new MapScaleChangedListener() {
            @Override
            public void mapScaleChanged(MapScaleChangedEvent mapScaleChangedEvent) {
                if (!mapView.isNavigating()) {
                    _clusterResolution = _getExtent(_mapView.getVisibleArea())
                            .getWidth() / _mapView.getWidth();
                    _clusterData.clear();
                    _clusterGraphics.clear();
                    _GraphicsOverlay.getGraphics().clear();
                    _clusterGraphicsOverlay.getGraphics().clear();
                    _clusterFeatures();
                }
            }
        });
    }

    public GraphicsOverlay getGraphicLayer() {
        return this._clusterGraphicsOverlay;
    }

    public void setGraphicVisible(boolean visible) {
        if (_clusterGraphicsOverlay != null) {
            _clusterGraphicsOverlay.setVisible(visible);
        }
    }

    public void removeGraphiclayer() {
        if (_mapView != null && _clusterGraphicsOverlay != null) {
            try {
                this._mapView.getGraphicsOverlays().remove(_clusterGraphicsOverlay);
                _clusterGraphicsOverlay.getGraphics().clear();
            } catch (Exception e) {
            }
        }
    }

    public void clear() {
        if (_clusterGraphicsOverlay != null) {
            _clusterGraphicsOverlay.getGraphics().clear();
        }
    }

    public ArrayList<Graphic> getGraphicsByClusterID(int id) {
        ArrayList<Graphic> graphics = new ArrayList<>();
        for (Graphic gra : this._clusterGraphics
                ) {
            if (Integer.valueOf(gra.getAttributes().get("clusterID").toString()) == id) {
                graphics.add(gra);
            }
        }
        return graphics;
    }

    private void _clusterFeatures() {
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setGeometry(_mapView.getVisibleArea());
        queryParameters.setReturnGeometry(true);
        final ListenableFuture<FeatureQueryResult> futures =
                _featureLayer.getFeatureTable().queryFeaturesAsync(queryParameters);
        // add done loading listener to fire when the selection returns
        futures.addDoneListener(new Runnable() {
            @Override
            public void run() {
                try {
                    //call get on the future to get the result
                    FeatureQueryResult result = futures.get();
                    // create an Iterator
                    Iterator<Feature> iterator = result.iterator();
                    Feature feature;
                    // cycle through selections
                    int counter = 0;
                    while (iterator.hasNext()) {
                        feature = iterator.next();
                        _GraphicsOverlay.getGraphics().add(new Graphic(feature.getGeometry()));
                        counter++;
                    }
                    _clusterGraphics();
                } catch (Exception e) {
                    Log.d("cluster", e.getLocalizedMessage());
                }
            }
        });
    }

    private void _clusterGraphics() {
        // 遍历所有的point，判断添加到哪一个cluster中
        clusterID = 0;

        for (int j = 0, jl = _GraphicsOverlay.getGraphics().size(); j < jl; j++) {
            Graphic graphic = this._GraphicsOverlay.getGraphics().get(j);
            Point point = (Point) graphic.getGeometry();
            boolean clustered = false;
            for (int i = 0, il = this._clusterData.size(); i < il; i++) {
                Map<String, Object> cluster = this._clusterData.get(i);

                Point pointCluster = new Point((Double) cluster.get("x"),
                        (Double) cluster.get("y"), _mapView.getSpatialReference());

                if (this._clusterTest(point, pointCluster)) {
                    this._clusterAddGraphic(graphic, cluster);
                    clustered = true;
                    break;
                }
            }

            if (!clustered) {
                this._clusterCreate(graphic);
            }
        }
        this._showAllClusters();
    }

    private void _clusterAddGraphic(Graphic graphic, Map<String, Object> cluster) {
        int count = (Integer) cluster.get("count");
        Point point = (Point) graphic.getGeometry();
        double xCluster = (Double) cluster.get("x");
        double yCluster = (Double) cluster.get("y");

        double x = (point.getX() + (xCluster * count)) / (count + 1);
        double y = (point.getY() + (yCluster * count)) / (count + 1);
        cluster.remove("x");
        cluster.remove("y");
        cluster.put("x", x);
        cluster.put("y", y);

        Envelope envelope = (Envelope) Geometry.fromJson(cluster.get("extent").toString());
        double xMin, yMin, xMax, yMax;
        xMin = envelope.getXMin();
        yMin = envelope.getYMin();
        xMax = envelope.getXMax();
        yMax = envelope.getYMax();

        // 建立extent，把所有的point都包含在cluster中
        if (point.getX() < xMin) {
            xMin = point.getX();
        }

        if (point.getX() > xMax) {
            xMax = point.getX();
        }

        if (point.getY() < yMin) {
            yMin = point.getY();
        }

        if (point.getY() > yMax) {
            yMax = point.getY();
        }

        Envelope envelopeNew = new Envelope(xMin, yMin, xMax, yMax, _mapView.getSpatialReference());

        cluster.remove("extent");
        cluster.put("extent", envelopeNew.toJson());

        count++;
        cluster.remove("count");
        cluster.put("count", count);

        if (_clusterGraphics != null) {
            _clusterGraphics.add(graphic);
        }
    }

    private void _clusterCreate(Graphic graphic) {
        _clusterGraphics.add(graphic);
        Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("count", 1);
        Point point = (Point) graphic.getGeometry();
        hashMap.put("extent", new Envelope((Point) graphic.getGeometry(), 0, 0).toJson());
//        hashMap.put("graphics", graphics);
        hashMap.put("x", point.getX());
        hashMap.put("y", point.getY());
        hashMap.put("clusterID", clusterID);
        graphic.getAttributes().put("clusterID", clusterID);
        clusterID++;

        this._clusterData.add(hashMap);
    }

    private boolean _clusterTest(Point point, Point pointCluster) {
        double distance = (Math.sqrt(Math.pow(
                (pointCluster.getX() - point.getX()), 2)
                + Math.pow((pointCluster.getY() - point.getY()), 2)) / this._clusterResolution);
        return (distance <= this._clusterTolerance);
    }

    private void _showAllClusters() {
        this._clusterGraphicsOverlay.getGraphics().clear();

        for (int i = 0, il = this._clusterData.size(); i < il; i++) {
            Map<String, Object> cluster = this._clusterData.get(i);
            Point pointCluster = new Point((Double) cluster.get("x"),
                    (Double) cluster.get("y"), _mapView.getSpatialReference());

            Graphic graphic = new Graphic(pointCluster, cluster, createClusterSymbol(cluster));
            this._clusterGraphicsOverlay.getGraphics().add(graphic);
        }
    }

    private Symbol createClusterSymbol(Map<String, Object> cluster) {
        int count = (Integer) cluster.get("count");
        if (count == 1) {
            return markerSymbol;
        } else if (count > 1) {
            List<Symbol> symbols = new ArrayList<>();
            if (count <= 10) {
                symbols.add(markerSymbolS);
            } else if (count > 10 && count <= 20) {
                symbols.add(markerSymbolM);
            } else if (count > 20) {
                symbols.add(markerSymbolL);
            }
            TextSymbol textSymbol = new TextSymbol(18, count + "", Color.WHITE,
                    TextSymbol.HorizontalAlignment.CENTER,
                    TextSymbol.VerticalAlignment.MIDDLE);
            symbols.add(textSymbol);
            CompositeSymbol compositeSymbol = new CompositeSymbol(symbols);
            return compositeSymbol;
        }

        return null;
    }

    SimpleMarkerSymbol markerSymbolL = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE,
            Color.RED, 36);
    SimpleMarkerSymbol markerSymbolM = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE,
            Color.BLUE, 30);
    SimpleMarkerSymbol markerSymbolS = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE,
            Color.GREEN, 24);
    SimpleMarkerSymbol markerSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE,
            Color.YELLOW, 18);

    private Envelope _getExtent(Polygon polygon) {
        return polygon.getExtent();
    }
}
