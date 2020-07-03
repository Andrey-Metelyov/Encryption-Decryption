import java.util.Scanner;

class Time {

    int hour;
    int minute;
    int second;

    public static Time noon() {
        // write your code here
        return of(12, 0, 0);
    }

    public static Time midnight() {
        // write your code here
        return of(0, 0, 0);
    }

    public static Time ofSeconds(long seconds) {
        // write your code here
        int sec = (int) (seconds % (24 * 60 * 60));
        int hours = sec / 3600;
        int minutes = (sec - hours * 3600) / 60;
        sec = (sec - hours * 3600) % 60;
        return of(hours, minutes, sec);
    }

    public static Time of(int hour, int minute, int second) {
        // write your code here
//        System.out.println(hour + ":" + minute + ":" + second);
        if (hour > 23 || minute > 60 || second > 60
                || hour < 0 || minute < 0 || second < 0) {
            return null;
        }
        Time time = new Time();
        time.hour = hour;
        time.minute = minute;
        time.second = second;
        return time;
    }
}

/* Do not change code below */
public class Main {

    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        final String type = scanner.next();
        Time time = null;

        switch (type) {
            case "noon":
                time = Time.noon();
                break;
            case "midnight":
                time = Time.midnight();
                break;
            case "hms":
                int h = scanner.nextInt();
                int m = scanner.nextInt();
                int s = scanner.nextInt();
                time = Time.of(h, m, s);
                break;
            case "seconds":
                time = Time.ofSeconds(scanner.nextInt());
                break;
            default:
                time = null;
                break;
        }

        if (time == null) {
            System.out.println(time);
        } else {
            System.out.println(String.format("%s %s %s", time.hour, time.minute, time.second));
        }
    }
}