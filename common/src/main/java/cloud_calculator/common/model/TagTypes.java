package cloud_calculator.common.model;

public enum TagTypes {
    EXPRESSION("Expression");

    TagTypes(String tagName) {
        this.tagName = tagName;
    }

    private final String tagName;

    public String getTagName() {
        return tagName;
    }
}
