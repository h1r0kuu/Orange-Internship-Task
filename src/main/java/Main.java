import domain.Calendar;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Calendar calendar1 = Calendar.readFromFile("/calendar1.json");
        Calendar calendar2 = Calendar.readFromFile("/calendar2.json");
        System.out.println(Calendar.mergeAvailableIntervals(calendar1, calendar2));
    }
}
