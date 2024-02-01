package metro.src.subway;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;





public class Subway {
    private String city;
    private Station station;
    private SubwayLine line;
    private Set<SubwayLine> lines =  new LinkedHashSet<>();
    private Map<String, LocalDate> ticketsOnMonth = new LinkedHashMap<>(10_000);

    public Subway(String city) {
        this.city = city;
    }

    public void createNewLine(ColorLine colorLine, Subway subway) {
        checkColorLinesExists(colorLine.getColor());
        lines.add(new SubwayLine(colorLine,  new LinkedHashSet<>(), subway));
    }

    /**
     * Создать первую станцию в линии
     */
    public Station createFirstStationOnLine(ColorLine colorLine, String nameStation, Subway subway, Cash cash) {
        checkColorLinesNotExists(colorLine.getColor());
        checkNotNameStation(lines, nameStation);
        Station station = null;
        for (SubwayLine line : lines) {
            if (line.getStationsOnLine().size() == 0 && line.getColor().equals(colorLine)) {
                station = new Station(nameStation, line, subway, new Cash());
                line.getStationsOnLine().add(station);
                return station;
            }
        }
        return null;
    }

    /**
     * Создать последнюю станцию в линии
     */
    public Station createEndStation(String nameStation, ColorLine colorLine, Subway subway,
                                 Station prevStation, Station nextStation, Duration drivingTime,
                                 Set<Station> changeLines, Cash cash) {

        checkColorLinesNotExists(colorLine.getColor()); // -Линия с таким именем существует
        checkPrevStation(colorLine, prevStation); //-Проверка на существование предыдущей станции.
        addDurationTime(colorLine, drivingTime, prevStation); //-Время перегона больше 0, добавление времени в предыдущую станцию
        checkNotNameStation(lines, nameStation); //-Станции с таким именем не существует во всех линиях.
        SubwayLine line = findLine(lines, colorLine);
        Station station = new Station(nameStation, line, subway,
                prevStation, nextStation, drivingTime, changeLines, cash);
        line.getStationsOnLine().add(station);
        addNamePrevStationToNameNextStation(nameStation, prevStation, colorLine); //предыдущей станции заносим имя следующей станции
        return station;
    }

    /**
     * определения станции на пересадку
     */
    protected Station getStationOnChange(ColorLine colorLine1) {
        SubwayLine lineOne = findLine(lines, colorLine1);
        return getStationOnChangeHelper(lineOne);
    }

    private Station getStationOnChangeHelper(SubwayLine lineOne) {
        Station resultStation = null;
        for (Station station : lineOne.getStationsOnLine()) {
            if ((station.getChangeLines() != null)) {
                for (Station changeLine : station.getChangeLines()) {
                    resultStation = changeLine;
                }
            }
        }
        return resultStation;
    }

    /**
     * количество перегонов между станциями по Next на одной линии
     */
    public int getStageByNextStation(Station nameStartStation, Station nameEndStation) {
        if (!checkNameStationOnLine(nameStartStation, nameEndStation) || nameStartStation.getNextStation() == null) {
            return -1;
        }
        List<Station> listTmp = new LinkedList<>();
        for (Station station : nameStartStation.getSubwayLine().getStationsOnLine()) {
            listTmp.add(station);
        }
        int i = 0;
        for (int y = listTmp.indexOf(nameStartStation); y < listTmp.size(); y++) {
            if (listTmp.get(y).getNextStation() == null) {
                break;
            }
            if (listTmp.get(y).getNextStation().getName().equals(nameEndStation.getName())) {
                return ++i;
            }
            ++i;
        }
        return -1;
    }

    /**
     * количество перегонов между станциями по Prev
     */
    public int getStageByPrevStation(Station nameStartStation, Station nameEndStation) {
        if (!checkNameStationOnLine(nameStartStation, nameEndStation) || nameStartStation.getPrevStation() == null) {
            return -1;
        }
        List<Station> listTmp = new LinkedList<>();
        for (Station station : nameStartStation.getSubwayLine().getStationsOnLine()) {
            listTmp.add(station);
        }
        Collections.reverse(listTmp);
        int i = 0;
        for (int y = listTmp.indexOf(nameStartStation); y < listTmp.size(); y++) {
            if (listTmp.get(y).getPrevStation() == null) {
                break;
            }
            if (listTmp.get(y).getPrevStation().getName().equals(nameEndStation.getName())) {
                return ++i;
            }
            ++i;
        }
        return -1;
    }

    /**
     * количество перегонов между станциями по Prev и Next
     */
    public int getCountStageBetweenOnLine(Station nameStartStation, Station nameEndStation) {
        int i = getStageByNextStation(nameStartStation, nameEndStation);
        int y = getStageByPrevStation(nameStartStation, nameEndStation);
        if ((i == -1) && (y == -1)) {
            throw new RuntimeException("нет пути от начальной " + nameStartStation.getName() + " до " + nameEndStation.getName());
        }

        if ((i == -1) && (y != -1)) {
            return y;
        } else if ((i != -1) && (y == -1)) {
            return i;
        }
        return (i == y) ? i : -1;
    }

    /**
     * количества перегонов если станции находятся на разных линиях
     */
    public int getCountStageDifferentLines(Station nameStartStation, Station nameEndStation) {
        if (equalNames(nameStartStation, nameEndStation)) {
            return -1;
        }
        int i = 0;
        if (!checkNameStationOnLine(nameStartStation, nameEndStation)) {
            if (nameStartStation.getSubwayLine().getColor().equals(nameEndStation.getSubwayLine().getColor())) {
                i = getCountStageBetweenOnLine(nameStartStation, nameEndStation);
                return i;
            }
        }
        Station stationForChange = getStationOnChange(nameStartStation.getSubwayLine().getColor());
        Station changeStation = null;
        for (Station stationTmp : nameStartStation.getSubwayLine().getStationsOnLine()) {
            if (stationTmp.getChangeLines() != null) {
                changeStation = stationTmp;
            }
        }
        i = getCountStageBetweenOnLine(nameStartStation, changeStation);
        i += getCountStageBetweenOnLine(stationForChange, nameEndStation);
        return i;
    }

    /**
     * проверяем, найденные станции находятся на одной ли линии или нет
     */
    private boolean checkNameStationOnLine(Station nameStartStation, Station nameEndStation) {
        if (equalNames(nameStartStation, nameEndStation)) {
            return false;
        }

        if (nameStartStation.getSubwayLine().getStationsOnLine().contains(nameEndStation)) {
            return true;
        }
        return false;
    }

    private boolean equalNames(Station nameStartStation, Station nameEndStation) {
        if (nameStartStation.getName().equals(nameEndStation.getName())) {
            return true;
        }
        return false;
    }

    public void createChangeLines(Station station1, Station station2) {
        Set<Station> setLine1 = new HashSet<>();
        Set<Station> setLine2 = new HashSet<>();
        setLine1.add(station2);
        setLine2.add(station1);
        station1.setChangeLines(setLine1);
        station2.setChangeLines(setLine2);
    }

    private void checkNotNameStation(Set<SubwayLine> lines, String nameStation) {
        for (SubwayLine line : lines) {
            checkHelperStation(line, nameStation, "Станция с " + nameStation + " существует");
        }
    }

    private void checkHelperStation(SubwayLine line, String nameStation, String message) {
        for (Station station : line.getStationsOnLine()) {
            if (station.getName().equals(nameStation)) {
                throw new RuntimeException(message);
            }
        }
    }

    private void checkColorLinesExists(String colorLine) {
        if (lines.size() != 0) {
            checkHelperColor(colorLine, "Линия с " + colorLine + " существует");
        }
    }

    private void checkHelperColor(String colorLine, String message) {
        for (SubwayLine line : lines) {
            if (line.getColor().getColor().equals(colorLine)) {
                throw new RuntimeException(message);
            }
        }
    }

    private void checkColorLinesNotExists(String colorLine) {
        if (lines.size() != 0 && checkColorLinesNotExistsHelper(colorLine)) {
            return;
        }
        throw new RuntimeException("Линия " + colorLine + " не существует");
    }

    private boolean checkColorLinesNotExistsHelper(String colorLine) {
        for (SubwayLine line : lines) {
            if (line.getColor().getColor().equals(colorLine)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkPrevStation(ColorLine colorLine, Station prevStation) {
        SubwayLine line = findLine(lines, colorLine);
        int i = line.getStationsOnLine().size();
        if (i > 0) {
            if (line.getStationsOnLine().contains(prevStation)) {
                return true;
            }
        }
        throw new RuntimeException("Это не последняя станция");
    }

    private void addDurationTime(ColorLine colorLine, Duration drivingTime, Station prevStation) {
        if (!checkDurationTimeNotNull(colorLine, drivingTime, prevStation)) {
            SubwayLine line = findLine(lines, colorLine);
            if (line.getStationsOnLine().contains(prevStation)) {
                prevStation.setDrivingTime(drivingTime);
            }
        }
    }

    private boolean checkDurationTimeNotNull(ColorLine colorLine, Duration drivingTime,  Station prevStation) {
        SubwayLine line = findLine(lines, colorLine);
        Duration time =  prevStation.getDrivingTime();
        if (time == null || time.getSeconds() == 0L) {
            return false;
        }
        return true;
    }

    private void addNamePrevStationToNameNextStation(String nameStation, Station prevStation, ColorLine colorLine) {
        SubwayLine line = findLine(lines, colorLine);
        Station stationTmp = null;
        for (Station station : line.getStationsOnLine()) {
            if (station.getName().equals(nameStation)) {
                stationTmp = station;
            }
        }
        if (line.getStationsOnLine().contains(prevStation)) {
            prevStation.setNextStation(stationTmp);
        }
    }

    private SubwayLine findLine(Set<SubwayLine> lines, ColorLine colorLine) {
        SubwayLine result = null;
        for (SubwayLine line : lines) {
            if (line.getColor().equals(colorLine)) {
                result = line;
            }
        }
        return result;
    }

    public Set<SubwayLine> getLines() {
        return lines;
    }

    public void setTicketOnMonth(LocalDate localDate) {
        int index = this.ticketsOnMonth.size();
        String str = String.format("%s%04d", "a", index);
        ticketsOnMonth.put(str, localDate.plusMonths(1));
    }

    public void setTicketOnMonth(String numberTicket, LocalDate localDate) {
        this.ticketsOnMonth.put(numberTicket, localDate);
    }

    protected boolean checkTicketOnMonth(String numberTicket, LocalDate localDate) {
        if (this.ticketsOnMonth.containsKey(numberTicket)) {
            if (localDate.isBefore(this.ticketsOnMonth.get(numberTicket))) {
                return true;
            }
        }
        return false;
    }

    public void printIncomeAllCash() {
        Map<LocalDate, BigDecimal> mapResult = new LinkedHashMap<>();
        for (SubwayLine line : lines) {
            for (Station station : line.getStationsOnLine()) {
                for (Map.Entry<LocalDate, BigDecimal> pair : station.getCash().income.entrySet()) {
                    LocalDate key = pair.getKey();
                    BigDecimal value = pair.getValue();
                    if (mapResult.containsKey(key)) {
                        BigDecimal tmp = mapResult.get(key);
                        tmp = tmp.add(value);
                        mapResult.put(key, tmp);
                    }
                    mapResult.put(key, value);
                }
            }
        }
        for (Map.Entry<LocalDate, BigDecimal> pair : mapResult.entrySet()) {
            System.out.println(pair.getKey().toString() + " - " + pair.getValue().toString());
        }
    }


    @Override
    public String toString() {
        return "Метро{"
                + "city='" + city + '\''
                + ", lines=" + lines + '}';
    }
}
