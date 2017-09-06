# ArcGIS-Android-Cluster
使用ArcGIS Runtime SDK for Android实现的点聚合。

## 版本要求
ArcGIS Runtime SDK for Android 100.1

## 效果图

![效果图](screenshot.png)


## 生成随机点
```java
for (int i = 0; i < 1000; i++) {
    double lat = Math.random() + 39.474923;
    double lon = Math.random() + 116.027116;
    Point point = new Point(lon, lat, mMapView.getSpatialReference());
    Graphic gra = new Graphic(point, simpleMarkerSymbol);
    graphicsOverlay.getGraphics().add(gra);
}
```
## 地图比例尺变化
```java
mapView.addMapScaleChangedListener(new MapScaleChangedListener() {
    @Override
    public void mapScaleChanged(MapScaleChangedEvent mapScaleChangedEvent) {
        if (!mapView.isNavigating()) {
            //do you something
        }
    }
});
```
