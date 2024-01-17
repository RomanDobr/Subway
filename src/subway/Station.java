package metro.src.subway;

import java.time.Duration;
import java.util.ArrayList;

public class Station {
    private String name;
    private String prevStation;
    private String nextStation;
    private Duration drivingTime;
    private SubwayLine subwayLine;
    private ArrayList<Station> changeLines;
    private Subway subway;

    public Station(String name, SubwayLine subwayLine, Subway subway, String prevStation,
                   String nextStation, Duration drivingTime,  ArrayList<Station> changeLines) {
        this.name = name;
        this.prevStation = prevStation;
        this.nextStation = nextStation;
        this.drivingTime = drivingTime;
        this.subwayLine = subwayLine;
        this.changeLines = changeLines;
        this.subway = subway;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrevStation() {
        return prevStation;
    }

    public void setPrevStation(String prevStation) {
        this.prevStation = prevStation;
    }

    public String getNextStation() {
        return nextStation;
    }

    public void setNextStation(String nextStation) {
        this.nextStation = nextStation;
    }

    public Duration getDrivingTime() {
        return drivingTime;
    }

    public void setDrivingTime(Duration drivingTime) {
        this.drivingTime = drivingTime;
    }

    public SubwayLine getSubwayLine() {
        return subwayLine;
    }

    public void setSubwayLine(SubwayLine subwayLine) {
        this.subwayLine = subwayLine;
    }

    public ArrayList<Station> getChangeLines() {
        return changeLines;
    }

    public void setChangeLines(ArrayList<Station> changeLines) {
        this.changeLines = changeLines;
    }

    private String getNameLine() {
        if (this.changeLines != null) {
            for (Station station : this.changeLines) {
                return station.getSubwayLine().getColor();
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Station{"
                + "name='" + name + '\''
                + ", changeLines=" + this.getNameLine() + '}';
    }



}
