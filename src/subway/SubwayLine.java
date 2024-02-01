package metro.src.subway;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class SubwayLine {
    private ColorLine color;
    private LinkedHashSet<Station> stationsOnLine = new LinkedHashSet<>();
    private Subway subway;
    private Station station;

    public SubwayLine(ColorLine color, LinkedHashSet<Station> stationsOnLine, Subway subway) {
        this.color = color;
        this.subway = subway;
        this.stationsOnLine = stationsOnLine;
    }

    public ColorLine getColor() {
        return color;
    }

    public void setColor(ColorLine color) {
        this.color = color;
    }

    public LinkedHashSet<Station> getStationsOnLine() {
        return stationsOnLine;
    }

    public void setStationsOnLine(LinkedHashSet<Station> stationsOnLine) {
        this.stationsOnLine = stationsOnLine;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    @Override
    public String toString() {
        return "Line{" + "color='" + color + '\''
                + ", stations=" + stationsOnLine + '}';
    }
}
