package metro.src.subway;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


public class Subway {
    private String city;
    private Station station;
    private SubwayLine line;
    private List<SubwayLine> lines = new ArrayList<>();

    public Subway(String city) {
        this.city = city;
    }

    public void createNewLine(ColorLine colorLine, Subway subway) {
        checkColorLinesExists(colorLine.getColor());
        lines.add(new SubwayLine(colorLine, new ArrayList<>(), subway));
    }

    public void createFirstStationOnLine(ColorLine colorLine, String nameStation, Subway subway) {
        checkColorLinesNotExists(colorLine.getColor());
        checkNotNameStation(lines, nameStation);

        for (SubwayLine line : lines) {
            if (line.getStationsOnLine().size() == 0 && line.getColor().equals(colorLine)) {
                line.getStationsOnLine().add(new Station(nameStation, line, subway));
            }
        }
    }

    public void createEndStation(String nameStation, ColorLine colorLine, Subway subway,
                                 String prevStation, String nextStation, Duration drivingTime,
                                 ArrayList<Station> changeLines) {

        checkColorLinesNotExists(colorLine.getColor()); // -Линия с таким именем существует
        checkPrevStation(colorLine, prevStation); //-Проверка на существование предыдущей станции.
        addDurationTime(colorLine, drivingTime); //-Время перегона больше 0, добавление времени в предыдущую станцию
        checkNotNameStation(lines, nameStation); //-Станции с таким именем не существует во всех линиях.
        addNamePrevStationToNameNextStation(nameStation, colorLine); //предыдущей станции заносим имя следующей станции

        SubwayLine line = findLine(lines, colorLine);
        line.getStationsOnLine().add(new Station(nameStation, line, subway,
                                         prevStation, nextStation, drivingTime, changeLines));
    }

    //2.1 определения станции на пересадку
    public Station getStationOnChange(ColorLine colorLine1, String line2) {
        SubwayLine lineOne = findLine(lines, colorLine1);
        Station resultStation = null;
        for (Station station : lineOne.getStationsOnLine()) {
            if ((station.getChangeLines() != null) && station.getChangeLines()
                    .get(0)
                    .getSubwayLine()
                    .getColor().getColor()
                    .equals(line2)) {
                resultStation = station;
            }
        }
        return resultStation;
    }

    //2.2 количество перегонов между станциями по Next
    private int getStageByNextStation(String nameStartStation, String nameEndStation) {
        if (getListStationOnLine(nameStartStation, nameEndStation) == null) {
            return -1;
        }
        Station stationStart = getListStationOnLine(nameStartStation, nameEndStation).get(0);
        Station stationEnd = getListStationOnLine(nameStartStation, nameEndStation).get(1);
        int i = 0;
        int indexStartStation = stationEnd.getSubwayLine().getStationsOnLine().indexOf(stationStart);
        int indexEndStation = stationEnd.getSubwayLine().getStationsOnLine().indexOf(stationEnd);
        for (int y = indexStartStation; y < indexEndStation; y++) {
            if (stationStart.getSubwayLine().getStationsOnLine().get(y).getNextStation().equals(nameEndStation)) {
                i++;
                return i;
            }
            i++;
        }
        return -1;
    }

    //2.3 количество перегонов между станциями по Prev
    private int getStageByPrevStation(String nameStartStation, String nameEndStation) {
        if (getListStationOnLine(nameStartStation, nameEndStation) == null) {
            return -1;
        }
        Station stationStart = getListStationOnLine(nameStartStation, nameEndStation).get(0);
        Station stationEnd = getListStationOnLine(nameStartStation, nameEndStation).get(1);
        int i = 0;
        int indexEndStation = stationEnd.getSubwayLine().getStationsOnLine().indexOf(stationEnd);
        int indexStartStation = stationEnd.getSubwayLine().getStationsOnLine().indexOf(stationStart);
        for (int y = indexEndStation; y >= indexStartStation; y--) {
            if (stationEnd.getSubwayLine().getStationsOnLine().get(y).getPrevStation().equals(nameStartStation)) {
                i++;
                return i;
            }
            i++;
        }
        return -1;
    }

    //2.4 количество перегонов между станциями по Prev и Next
    private int getCountStageBetweenOnLine(String nameStartStation, String nameEndStation) {
        int i = getStageByNextStation(nameStartStation, nameEndStation);
        int y = getStageByPrevStation(nameStartStation, nameEndStation);
        if ((i == -1) && (y == -1)) {
            throw new RuntimeException("нет пути от начальной " + nameStartStation + " до " + nameEndStation);
        }

        return (i == y) ? i : -1;
    }

    //2.5 количества перегонов если станции находятся на разных линиях
    public int getCountStageDifferentLines(String nameStartStation, String nameEndStation) {
        if (nameStartStation.equals(nameEndStation)) {
            return -1;
        }
        List<Station> listStationTmp = getListStation(nameStartStation, nameEndStation);
        int i = 0;
        if (listStationTmp.get(0).getSubwayLine().getColor().equals(listStationTmp.get(1).getSubwayLine().getColor())) {
            i = getCountStageBetweenOnLine(nameStartStation, nameEndStation);
            return i;
        }
        Station stationForChange = getStationOnChange(listStationTmp.get(0).getSubwayLine().getColor(),
                listStationTmp.get(1).getSubwayLine().getColor().getColor());
        i = getCountStageBetweenOnLine(nameStartStation, stationForChange.getName());
        i += getCountStageBetweenOnLine(stationForChange.getChangeLines().get(0).getName(), nameEndStation);
        return i;
    }

    private List<Station> getListStationOnLine(String nameStartStation, String nameEndStation) {
        Station stationStart = null;
        Station stationEnd = null;
        for (SubwayLine line : lines) {
            for (Station station : line.getStationsOnLine()) {
                if (station.getName().equals(nameStartStation)) {
                    stationStart = station;
                }
                if (station.getName().equals(nameEndStation)) {
                    stationEnd = station;
                }
            }
        }
        List<Station> listStation = null;
        //проверяем, найденные станции находятся на одной ли линии или нет
        if (stationStart.getSubwayLine().getColor().equals(stationEnd.getSubwayLine().getColor())) {
            listStation = List.of(stationStart, stationEnd);
        }
        return listStation;
    }

    private List<Station> getListStation(String nameStartStation, String nameEndStation) {
        Station stationStart = null;
        Station stationEnd = null;
        for (SubwayLine line : lines) {
            for (Station station : line.getStationsOnLine()) {
                if (station.getName().equals(nameStartStation)) {
                    stationStart = station;
                }
                if (station.getName().equals(nameEndStation)) {
                    stationEnd = station;
                }
            }
        }
        List<Station> listStation = List.of(stationStart, stationEnd);
        return listStation;
    }


    public void createChangeLines(String station1, String station2) {
        Station stationOne = null;
        Station stationTwo = null;
        for (SubwayLine line : lines) {
            for (Station station : line.getStationsOnLine()) {
                if (station.getName().equals(station1)) {
                    stationOne = station;
                }
                if (station.getName().equals(station2)) {
                    stationTwo = station;
                }
            }
        }
        ArrayList<Station> changeLines1 = new ArrayList<>();
        ArrayList<Station> changeLines2 = new ArrayList<>();
        changeLines1.add(stationTwo);
        changeLines2.add(stationOne);
        stationOne.setChangeLines(changeLines1);
        stationTwo.setChangeLines(changeLines2);

    }

    private void checkNotNameStation(List<SubwayLine> lines, String nameStation) {
        for (SubwayLine line : lines) {
            for (Station station : line.getStationsOnLine()) {
                if (station.getName().equals(nameStation)) {
                    throw new RuntimeException("Станция с " + nameStation + " существует");
                }
            }
        }
    }

    private void checkColorLinesExists(String colorLine) {
        if (lines.size() != 0) {
            for (SubwayLine line : lines) {
                if (line.getColor().getColor().equals(colorLine)) {
                    throw new RuntimeException("Линия с " + colorLine + " существует");
                }
            }
        }
    }

    private void checkColorLinesNotExists(String colorLine) {
        if (lines.size() != 0) {
            for (SubwayLine line : lines) {
                if (line.getColor().getColor().equals(colorLine)) {
                    return;
                }
            }
        }
        throw new RuntimeException("Линия " + colorLine + " не существует");
    }

    private boolean checkPrevStation(ColorLine colorLine, String prevStation) {
        SubwayLine line = findLine(lines, colorLine);
        int i = line.getStationsOnLine().size();
        if (i > 0) {
            if (line.getStationsOnLine()
                    .get(i - 1)
                    .getName()
                    .equals(prevStation)
                    && line
                    .getStationsOnLine()
                    .get(i - 1).getNextStation() == null) {
                return true;
            }
        }
        throw new RuntimeException("Это не последняя станция");
    }

    private void addDurationTime(ColorLine colorLine, Duration drivingTime) {
        if (!checkDurationTimeNotNull(colorLine, drivingTime)) {
            SubwayLine line = findLine(lines, colorLine);
            if (line.getColor().equals(colorLine)) {
                line.getStationsOnLine().get(line.getStationsOnLine().size() - 1).setDrivingTime(drivingTime);
            }
        }
    }

    private boolean checkDurationTimeNotNull(ColorLine colorLine, Duration drivingTime) {
        SubwayLine line = findLine(lines, colorLine);
        Duration time = line.getStationsOnLine().get(line.getStationsOnLine().size() - 1).getDrivingTime();
        if (time == null || time.getSeconds() == 0L) {
            return false;
        }
        return true;
    }

    private void addNamePrevStationToNameNextStation(String nameStation, ColorLine colorLine) {
        SubwayLine line = findLine(lines, colorLine);
        line.getStationsOnLine().get(line.getStationsOnLine().size() - 1).setNextStation(nameStation);
    }

    private SubwayLine findLine(List<SubwayLine> lines, ColorLine colorLine) {
        SubwayLine result = null;
        for (SubwayLine line : lines) {
            if (line.getColor().equals(colorLine)) {
                result = line;
            }
        }
        return result;
    }

    public List<SubwayLine> getLines() {
        return lines;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    @Override
    public String toString() {
        return "Метро{"
                + "city='" + city + '\''
                + ", lines=" + lines + '}';
    }
}
