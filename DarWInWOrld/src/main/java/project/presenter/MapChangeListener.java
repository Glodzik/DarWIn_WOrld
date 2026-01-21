package project.presenter;

import project.model.map.RectangularMap;

public interface MapChangeListener {
    void mapChanged(RectangularMap worldMap, String message);
}
