package fr.bourgmapper.tub.presentation.model;

/**
 * Class that represents a line in the presentation layer.
 */
public class LineModel {

    private String id;
    private String number;
    private String label;
    private String color;
    private String kmlPath;
    public LineModel(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getKmlPath() {
        return kmlPath;
    }

    public void setKmlPath(String kmlPath) {
        this.kmlPath = kmlPath;
    }
}
