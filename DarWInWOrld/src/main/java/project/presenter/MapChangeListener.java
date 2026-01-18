package project.presenter;

import project.model.Map.RectangularMap;

public interface MapChangeListener {
    void mapChanged(RectangularMap worldMap, String message);
}
