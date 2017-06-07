package extractor.attribute;

import java.util.List;

public class MultiAttribute implements Attribute<List<String>> {
    private List<String> value;

    public MultiAttribute(List<String> value) {
        this.value = value;
    }

    @Override
    public List<String> value() {
        return null;
    }
}
