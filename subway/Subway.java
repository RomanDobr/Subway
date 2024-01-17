package metro.subway;

import java.time.Duration;
import java.util.ArrayList;


public class Subway {
    private String city;
    private ArrayList<SubwayLine> lines = new ArrayList<>();

    public Subway(String city) {
        this.city = city;
    }

    public void createNewLine(String colorLine, Subway subway) {
        if (lines.size() != 0) {
            checkColorLinesExists(colorLine);
        }
        lines.add(new SubwayLine(colorLine, new ArrayList<Station>(), subway));
    }

    public void createFirstStationOnLine(String colorLine, String nameStation, Subway subway) {
        if (lines.size() != 0) {
            checkColorLinesNotExists(colorLine);
        }
        checkNotNameStation(lines, nameStation);

        for (SubwayLine line : lines) {
            if (line.getStationsOnLine().size() == 0 && line.getColor().equals(colorLine)) {
                line.getStationsOnLine().add(new Station(nameStation, line, subway,
                        null, null, null, null));
            }
        }
    }

    public void createEndStation(String nameStation, String colorLine, Subway subway,
                                 String prevStation, String nextStation, Duration drivingTime,
                                 ArrayList<Station> changeLines) {

        // -Линия с таким именем существует
        if (lines.size() != 0) {
            checkColorLinesNotExists(colorLine);
        }

        //-Проверка на существование предыдущей станции.
        if (!checkPrevStation(colorLine)) {
            throw new RuntimeException("Это не последняя станция");
        }

        //-Время перегона добавляется в предыдущую станцию
        for (SubwayLine line : lines) {
            if (line.getColor().equals(colorLine)) {
                line.getStationsOnLine().get(line.getStationsOnLine().size() - 1).setDrivingTime(drivingTime);
            }
        }

        //-Предыдущая станция должна не иметь следующей станции
        if (!checkOnNextStation(colorLine)) {
            throw new RuntimeException("У предыдущей станции присутствует следующая станция");
        }

        //-Станции с таким именем не существует во всех линиях.
        checkNotNameStation(lines, nameStation);

        //-Время перегона больше 0
        if (!checkDurationTimeNotNull(colorLine)) {
            throw new RuntimeException("У предыдущей станции отсутствует время перегона");
        }

        //предыдущей станции заносим имя следующей станции
        for (SubwayLine line : lines) {
            if (line.getColor().equals(colorLine)) {
                line.getStationsOnLine().get(line.getStationsOnLine().size() - 1).setNextStation(nameStation);
            }
        }

        for (SubwayLine line : lines) {
            if (line.getColor().equals(colorLine)) {
                line.getStationsOnLine().add(new Station(nameStation, line, subway,
                        prevStation, nextStation, drivingTime, changeLines));
            }
        }
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

    private void checkNotNameStation(ArrayList<SubwayLine> lines, String nameStation) {
        for (SubwayLine line : lines) {
            for (Station station : line.getStationsOnLine()) {
                if (station.getName().equals(nameStation)) {
                    throw new RuntimeException("Станция с " + nameStation + " существует");
                }
            }
        }
    }

    private void checkColorLinesExists(String colorLine) {
        for (SubwayLine line : lines) {
            if (line.getColor().equals(colorLine)) {
                throw new RuntimeException("Линия с " + colorLine + " существует");
            }
        }
    }

    private void checkColorLinesNotExists(String colorLine) {
        for (SubwayLine line : lines) {
            if (line.getColor().equals(colorLine)) {
                return;
            }
        }
        throw new RuntimeException("Линия " + colorLine + " не существует");
    }

    private boolean checkPrevStation(String colorLine) {
        for (SubwayLine line : lines) {
            if (line.getColor().equals(colorLine)) {
                if (line.getStationsOnLine().size() > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkDurationTimeNotNull(String colorLine) {
        for (SubwayLine line : lines) {
            if (line.getColor().equals(colorLine)) {
                Duration time = line.getStationsOnLine().get(line.getStationsOnLine().size() - 1).getDrivingTime();
                if (time.getSeconds() > 0L) {
                    return true;
                }

            }
        }
        return false;
    }

    private boolean checkOnNextStation(String colorLine) {
        for (SubwayLine line : lines) {
            if (line.getColor().equals(colorLine)) {
                if (line.getStationsOnLine().get(line.getStationsOnLine().size() - 1).getNextStation() != null) {
                    return false;
                }
            }
        }
        return true;
    }

    private ArrayList<SubwayLine> getLines() {
        return lines;
    }

    @Override
    public String toString() {
        return "Метро{"
                + "city='" + city + '\''
                + ", lines=" + lines + '}';
    }
}
