package studycom.web.domain.UsersPart;

public enum Role {
    CASUAL("CASUAL"),
    ADMIN("ADMIN"),
    STAR("STAR");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
