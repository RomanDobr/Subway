package metro.src.subway;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Station {
    private String name;
    private String prevStation;
    private String nextStation;
    private Duration drivingTime;
    private SubwayLine subwayLine;
    private List<Station> changeLines;
    private Subway subway;
    private Cash cash;

    public Station(String name, SubwayLine subwayLine, Subway subway, String prevStation,
                   String nextStation, Duration drivingTime,  ArrayList<Station> changeLines) {
        this.name = name;
        this.prevStation = prevStation;
        this.nextStation = nextStation;
        this.drivingTime = drivingTime;
        this.subwayLine = subwayLine;
        this.changeLines = changeLines;
        this.subway = subway;
        this.cash = new Cash();
    }

    public Station(String name, SubwayLine subwayLine, Subway subway) {
        this.name = name;
        this.subwayLine = subwayLine;
        this.subway = subway;
        this.cash = new Cash();
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

    public List<Station> getChangeLines() {
        return changeLines;
    }

    public Subway getSubway() {
        return subway;
    }

    public void setChangeLines(ArrayList<Station> changeLines) {
        this.changeLines = changeLines;
    }

    protected ColorLine getNameLine() {
        if (this.changeLines != null) {
            for (Station station : this.changeLines) {
                return station.getSubwayLine().getColor();
            }
        }
        return null;
    }

    public Map<LocalDate, Double> ticketSelling(LocalDate localDate, String nameStartStation, String nameEndStation) {
        if (nameStartStation.equals(nameEndStation)) {
            return null;
        }
        int countStage = subway.getCountStageDifferentLines(nameStartStation, nameEndStation);
        double summa = (countStage * 5) + 20;
        Map<LocalDate, Double> mapTmp = this.cash.income;
        if (mapTmp.size() != 0) {
            for (Map.Entry<LocalDate, Double> pair : mapTmp.entrySet()) {
                if (pair.getKey() == localDate) {
                    double value = pair.getValue() + summa;
                    pair.setValue(value);
                }
            }
            return mapTmp;
        }
        this.cash.income.put(localDate, summa);
        return mapTmp;
    }

    @Override
    public String toString() {
        return "Station{"
                + "name='" + name + '\''
                + ", changeLines=" + this.getNameLine() + '}';
    }
}
