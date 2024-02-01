package metro.src.subway;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import metro.src.subway.Station;

public class Cash {
    protected Map<LocalDate, BigDecimal> income = new HashMap<>();

    public void ticketSelling(LocalDate localDate, Station nameStartStation, Station nameEndStation) {
        if (nameStartStation.equals(nameEndStation)) {
            return;
        }
        int countStage = nameStartStation.getSubway().getCountStageDifferentLines(nameStartStation, nameEndStation);
        int resultTmp = (countStage * 5) + 20;
        BigDecimal summa = new BigDecimal(Integer.valueOf(resultTmp));
        Map<LocalDate, BigDecimal> mapTmp = this.income;
        if (mapTmp.size() != 0) {
            if (income.containsKey(localDate)) {
                BigDecimal value = income.get(localDate);
                summa = summa.add(value);
                income.put(localDate, summa);
            }
            mapTmp.put(localDate, summa);
        }
        this.income.put(localDate, summa);
    }

    public void ticketSellingOnMonth(LocalDate localDate) {
        this.income.put(localDate, new BigDecimal(3000));
    }


    @Override
    public String toString() {
        return "Cash{"
                + "income=" + income
                + '}';
    }
}
