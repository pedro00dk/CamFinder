package extractor.attribute;

public class StringAttribute implements Attribute<String> {
    private String value;

    public StringAttribute(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }
}
