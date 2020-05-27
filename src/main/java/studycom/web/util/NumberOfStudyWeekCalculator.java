package studycom.web.util;

public class NumberOfStudyWeekCalculator {
    public static int getNumberOfStudyWeek(int weekNumber, int countOfWeeks) {
        switch (countOfWeeks) {
            case 4 :
                if (weekNumber % 4 == 0) return 4;
            case 3:
                if (weekNumber % 3 == 0) return 3;
            case 2:
                return weekNumber % 2 == 0 ? 2 : 1;
            default:
                return 1;
        }
    }
}
