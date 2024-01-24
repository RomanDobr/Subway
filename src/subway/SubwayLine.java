package metro.src.subway;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SubwayLine {
    private ColorLine color;
    private List<Station> stationsOnLine = new ArrayList<>();
    private Subway subway;

    public SubwayLine(ColorLine color, List<Station> stationsOnLine, Subway subway) {
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

    public List<Station> getStationsOnLine() {
        return stationsOnLine;
    }

    public void setStationsOnLine(List<Station> stationsOnLine) {
        this.stationsOnLine = stationsOnLine;
    }

    @Override
    public String toString() {
        return "Line{" + "color='" + color + '\''
                + ", stations=" + stationsOnLine + '}';
    }
}
