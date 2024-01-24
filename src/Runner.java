package metro.src;

import static metro.src.subway.ColorLine.BLUE;
import static metro.src.subway.ColorLine.RED;

import java.time.Duration;
import java.time.LocalDate;
import metro.src.subway.Station;
import metro.src.subway.Subway;

public class Runner {
    public static void main(String[] args) {
        Subway subway = new Subway("Пермь");

        subway.createNewLine(RED, subway);
        subway.createFirstStationOnLine(RED, "Спортивная", subway);
        subway.createEndStation("Медведковская", RED, subway, "Спортивная",
                null, Duration.ofMinutes(2).plusSeconds(21), null);
        subway.createEndStation("Молодежная", RED, subway, "Медведковская",
                null, Duration.ofMinutes(1).plusSeconds(58), null);
        subway.createEndStation("Пермь 1", RED, subway, "Молодежная",
                null, Duration.ofMinutes(3).plusSeconds(0), null);
        subway.createEndStation("Пермь 2", RED, subway, "Пермь 1",
                null, Duration.ofMinutes(2).plusSeconds(10), null);
        subway.createEndStation("Дворец Культуры", RED, subway, "Пермь 2",
                null, Duration.ofMinutes(4).plusSeconds(26), null);

        subway.createNewLine(BLUE, subway);
        subway.createFirstStationOnLine(BLUE, "Пацанская", subway);
        subway.createEndStation("Улица Кирова", BLUE, subway, "Пацанская",
                null, Duration.ofMinutes(1).plusSeconds(30), null);
        subway.createEndStation("Тяжмаш", BLUE, subway, "Улица Кирова",
                null, Duration.ofMinutes(1).plusSeconds(47), null);
        subway.createEndStation("Нижнекамская", BLUE, subway, "Тяжмаш",
                null, Duration.ofMinutes(3).plusSeconds(19), null);
        subway.createEndStation("Соборная", BLUE, subway, "Нижнекамская",
                null, Duration.ofMinutes(1).plusSeconds(48), null);

        subway.createChangeLines("Пермь 1", "Тяжмаш");
        System.out.println(subway.getLines().toString());;
    }
}
