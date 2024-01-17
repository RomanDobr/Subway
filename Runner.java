package metro;

import java.time.Duration;
import metro.subway.Subway;


public class Runner {
    public static void main(String[] args) {
        Subway subway = new Subway("Пермь");

        subway.createNewLine("Красная", subway);
        subway.createFirstStationOnLine("Красная", "Спортивная", subway);
        subway.createEndStation("Медведковская", "Красная", subway, "Спортивная",
                null, Duration.ofMinutes(2).plusSeconds(21), null);
        subway.createEndStation("Молодежная", "Красная", subway, "Медведковская",
                null, Duration.ofMinutes(1).plusSeconds(58), null);
        subway.createEndStation("Пермь 1", "Красная", subway, "Молодежная",
                null, Duration.ofMinutes(3).plusSeconds(0), null);
        subway.createEndStation("Пермь 2", "Красная", subway, "Пермь 1",
                null, Duration.ofMinutes(2).plusSeconds(10), null);
        subway.createEndStation("Дворец Культуры", "Красная", subway, "Пермь 2",
                null, Duration.ofMinutes(4).plusSeconds(26), null);

        subway.createNewLine("Синяя", subway);
        subway.createFirstStationOnLine("Синяя", "Пацанская", subway);
        subway.createEndStation("Улица Кирова", "Синяя", subway, "Пацанская",
                null, Duration.ofMinutes(1).plusSeconds(30), null);
        subway.createEndStation("Тяжмаш", "Синяя", subway, "Улица Кирова",
                null, Duration.ofMinutes(1).plusSeconds(47), null);
        subway.createEndStation("Нижнекамская", "Синяя", subway, "Тяжмаш",
                null, Duration.ofMinutes(3).plusSeconds(19), null);
        subway.createEndStation("Соборная", "Синяя", subway, "Нижнекамская",
                null, Duration.ofMinutes(1).plusSeconds(48), null);

        subway.createChangeLines("Пермь 1", "Тяжмаш");
        System.out.println(subway.toString());

    }
}
