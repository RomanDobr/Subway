package metro.src.subway;


import java.time.Duration;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;


public class Station {
    private String name;
    private Station prevStation;
    private Station nextStation;
    private Duration drivingTime;
    private SubwayLine subwayLine;
    private Set<Station> changeLines = new LinkedHashSet<>();
    private Subway subway;
    private Cash cash = new Cash();

    public Station(String name, SubwayLine subwayLine, Subway subway, Station prevStation,
                   Station nextStation, Duration drivingTime,  Set<Station> changeLines, Cash cash) {
        this.name = name;
        this.prevStation = prevStation;
        this.nextStation = nextStation;
        this.drivingTime = drivingTime;
        this.subwayLine = subwayLine;
        this.changeLines = changeLines;
        this.subway = subway;
        this.cash = cash;
    }

    public Station(String name, SubwayLine subwayLine, Subway subway, Cash cash) {
        this.name = name;
        this.subwayLine = subwayLine;
        this.subway = subway;
        this.cash = cash;
    }

    public String getName() {
        return name;
    }

    protected Cash getCash() {
        return cash;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Station getPrevStation() {
        return prevStation;
    }

    public void setPrevStation(Station prevStation) {
        this.prevStation = prevStation;
    }

    public Station getNextStation() {
        return nextStation;
    }

    public void setNextStation(Station nextStation) {
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

    public Set<Station> getChangeLines() {
        return changeLines;
    }

    public Subway getSubway() {
        return subway;
    }

    public void setChangeLines(Set<Station> changeLines) {
        this.changeLines = changeLines;
    }

    public void getTickets(LocalDate localDate, Station nameStartStation, Station nameEndStation) {
        this.cash.ticketSelling(localDate, nameStartStation, nameEndStation);
    }

    public void saleTicketsOnMonth(LocalDate localDate) {
        subway.setTicketOnMonth(localDate);
        this.cash.ticketSellingOnMonth(localDate);
    }

    public void renewTicket(String numberTicket, LocalDate localDate) {
        LocalDate newDate = localDate.minusMonths(1);
        subway.setTicketOnMonth(numberTicket, newDate);
        this.cash.ticketSellingOnMonth(localDate);
    }

    public int sale(Station nameStartStation, Station nameEndStation) {
        if (nameStartStation.equals(nameEndStation)) {
            return 0;
        }
        int countStage = nameStartStation.getSubway().getCountStageDifferentLines(nameStartStation, nameEndStation);
        return (countStage * 5) + 20;
    }


    private String getLineColor() {
        String str = this.getSubwayLine().getColor().getColor();
        if (changeLines != null) {
            for (Station station : changeLines) {
                return str += " , " + station.getSubwayLine().getColor().getColor();
            }
        }
        return "";
    }

    @Override
    public String toString() {
        return "Station{"
                + "name='" + name + '\''
                + ", changeLines=" + getLineColor()
                + '}';
    }
}
