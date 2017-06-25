package extractor.attribute;

import java.util.List;
import java.util.stream.Collectors;

public class MultiAttribute implements Attribute<List<String>> {
    private List<String> value;

    public MultiAttribute(List<String> value) {
        this.value = value;
    }

    @Override
    public List<String> value() {
        return null;
    }

    @Override
    public String toString() {
        return value.stream().collect(Collectors.joining(" "));
    }
}
