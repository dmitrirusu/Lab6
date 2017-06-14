import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@WebSocket
public class WebsocketHandler {

    public static Map<String, Session> sessions = new ConcurrentHashMap<>();

    @OnWebSocketConnect
    public void connected(Session session) {
    }

    @OnWebSocketClose
    public void closed(Session session, int statusCode, String reason) {
        sessions.values().remove(session);
    }

    @OnWebSocketMessage
    public void message(Session session, String message) throws IOException {
        Message message1 = new Gson().fromJson(message, Message.class);
        switch (message1.val1) {
            case "+":
                String text = String.valueOf(Integer.valueOf(message1.val2) + Integer.valueOf(message1.val3));
                session.getRemote().sendString(text);
                break;
            case "-":
                String text1 = String.valueOf(Integer.valueOf(message1.val2) - Integer.valueOf(message1.val3));
                session.getRemote().sendString(text1);
                break;
            case "*":
                String text2 = String.valueOf(Integer.valueOf(message1.val2) * Integer.valueOf(message1.val3));
                session.getRemote().sendString(text2);
                break;
            case "/":
                session.getRemote().sendString(String.valueOf(Integer.valueOf(message1.val2) / Integer.valueOf(message1.val3)));
                break;
            case "getDay":
                Long l = Long.valueOf(message1.val2);
                session.getRemote().sendString(dayStringFormat(l));
                break;
            case "getMonth":
                Long month = Long.valueOf(message1.val2);
                session.getRemote().sendString(getMonthForInt(new Date(month).getMonth()));
                break;
        }

    }

    public static String dayStringFormat(long msecs) {
        GregorianCalendar cal = new GregorianCalendar();

        cal.setTime(new Date(msecs));

        int dow = cal.get(Calendar.DAY_OF_WEEK);

        switch (dow) {
            case Calendar.MONDAY:
                return "Monday";
            case Calendar.TUESDAY:
                return "Tuesday";
            case Calendar.WEDNESDAY:
                return "Wednesday";
            case Calendar.THURSDAY:
                return "Thursday";
            case Calendar.FRIDAY:
                return "Friday";
            case Calendar.SATURDAY:
                return "Saturday";
            case Calendar.SUNDAY:
                return "Sunday";
        }

        return "Unknown";
    }

    String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11) {
            month = months[num];
        }
        return month;
    }
}