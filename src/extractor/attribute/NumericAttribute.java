package extractor.attribute;

public class NumericAttribute implements Attribute<Double> {
    private double value;

    public NumericAttribute(double value) {
        this.value = value;
    }

    @Override
    public Double value() {
        return value;
    }
}
