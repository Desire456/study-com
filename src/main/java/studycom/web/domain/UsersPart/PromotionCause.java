package studycom.web.domain.UsersPart;

public enum PromotionCause {
    HOMEWORK("HOMEWORK"),
    CHARITY("CHARITY"),
    SESSION("SESSION");

    private final String name;

    PromotionCause(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String getNameType() {
        switch (this) {
            case HOMEWORK:
                return "Выполненная домашняя работа/сданная лаба/реферат";
            case CHARITY:
                return "Благотворительность на благо группы(написание конспектов, помощь с лабораторными...)";
            case SESSION:
                return "Сданная сессия";
            default:
                return "";
        }
    }

}
