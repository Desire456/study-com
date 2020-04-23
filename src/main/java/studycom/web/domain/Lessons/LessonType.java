package studycom.web.domain.Lessons;

public enum LessonType {
    PRACTICE("PRACTICE"),
    LECTURE("LECTURE"),
    LAB("LAB");

    private final String name;

    LessonType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
