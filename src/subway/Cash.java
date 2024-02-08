package metro.src.subway;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import metro.src.subway.Station;

public class Cash {
    protected Map<LocalDate, BigDecimal> income = new HashMap<>();

    public void ticketSelling(LocalDate localDate, Station nameStartStation, Station nameEndStation) {
        int tecketPrice = nameStartStation.sale(nameStartStation, nameEndStation);
        if (income.containsKey(localDate)) {
            BigDecimal value = income.get(localDate);
            income.put(localDate, value.add(BigDecimal.valueOf(tecketPrice)));
        } else {
            this.income.put(localDate, new BigDecimal(tecketPrice));
        }
    }

    public void ticketSellingOnMonth(LocalDate localDate) {
        if (this.income.containsKey(localDate)) {
            this.income.put(localDate, this.income.get(localDate).add(new BigDecimal(3000)));
        } else {
            this.income.put(localDate, new BigDecimal(3000));
        }
    }

    @Override
    public String toString() {
        return "Cash{"
                + "income=" + income
                + '}';
    }
}
