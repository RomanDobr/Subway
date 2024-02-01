package metro.src;

import static metro.src.subway.ColorLine.BLUE;
import static metro.src.subway.ColorLine.RED;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collections;
import metro.src.subway.Cash;
import metro.src.subway.Station;
import metro.src.subway.Subway;
import metro.src.subway.SubwayLine;


public class Runner {
    public static void main(String[] args) {
        Subway subway = new Subway("Пермь");

        subway.createNewLine(RED, subway);
        Station sportivnaya = subway.createFirstStationOnLine(RED, "Спортивная", subway, new Cash());
        Station medvedskovskaya = subway.createEndStation("Медведковская", RED, subway, sportivnaya,
                null, Duration.ofMinutes(2).plusSeconds(21), null, new Cash());
        Station molodeznaya = subway.createEndStation("Молодежная", RED, subway, medvedskovskaya,
                null, Duration.ofMinutes(1).plusSeconds(58), null, new Cash());
        Station perm1 = subway.createEndStation("Пермь 1", RED, subway, molodeznaya,
                null, Duration.ofMinutes(3).plusSeconds(0), null, new Cash());
        Station perm2 = subway.createEndStation("Пермь 2", RED, subway, perm1,
                null, Duration.ofMinutes(2).plusSeconds(10), null, new Cash());
        Station palaceCulture = subway.createEndStation("Дворец Культуры", RED, subway, perm2,
                null, Duration.ofMinutes(4).plusSeconds(26), null, new Cash());

        subway.createNewLine(BLUE, subway);
        Station pacanskaya = subway.createFirstStationOnLine(BLUE, "Пацанская", subway, new Cash());
        Station kirovstreet = subway.createEndStation("Улица Кирова", BLUE, subway, pacanskaya,
                null, Duration.ofMinutes(1).plusSeconds(30), null, new Cash());
        Station tyazmash = subway.createEndStation("Тяжмаш", BLUE, subway, kirovstreet,
                null, Duration.ofMinutes(1).plusSeconds(47), null, new Cash());
        Station niznekamskaya = subway.createEndStation("Нижнекамская", BLUE, subway, tyazmash,
                null, Duration.ofMinutes(3).plusSeconds(19), null, new Cash());
        Station sobornaya = subway.createEndStation("Соборная", BLUE, subway, niznekamskaya,
                null, Duration.ofMinutes(1).plusSeconds(48), null, new Cash());

        subway.createChangeLines(perm1, tyazmash);
        System.out.println(subway.getLines().toString());
        sportivnaya.saleTicketsOnMonth(LocalDate.now());
        sportivnaya.getTickets(LocalDate.now(), sobornaya, palaceCulture);
        subway.printIncomeAllCash();
    }
}
