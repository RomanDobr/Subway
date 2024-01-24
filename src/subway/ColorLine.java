package metro.src.subway;

public enum ColorLine {
    RED("Красная"), BLUE("Синяя");

    private String color;

    ColorLine(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
