package metro.src.subway;

import java.util.ArrayList;

public class SubwayLine {
    private String color;
    private ArrayList<Station> stationsOnLine = new ArrayList<>();
    private Subway subway;

    public SubwayLine(String color, ArrayList<Station> stationsOnLine, Subway subway) {
        this.color = color;
        this.subway = subway;
        this.stationsOnLine = stationsOnLine;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public ArrayList<Station> getStationsOnLine() {
        return stationsOnLine;
    }

    public void setStationsOnLine(ArrayList<Station> stationsOnLine) {
        this.stationsOnLine = stationsOnLine;
    }

    @Override
    public String toString() {
        return "Line{" + "color='" + color + '\''
                + ", stations=" + stationsOnLine + '}';
    }
}
