package metro.src.subway;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Cash {
    private double summa;
    private LocalDate localDate;
    protected Map<LocalDate, Double> income = new HashMap<>();

    public double getSumma() {
        return summa;
    }

    public void setSumma(double summa) {
        this.summa = summa;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }
}
