import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        Calculation calculate = new Calculation();
        JDateChooser chooser = new JDateChooser();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            String startMessage = "Choose start date:\n";
            Object[] paramsStart = {startMessage, chooser};
            JOptionPane.showConfirmDialog(null, paramsStart, "Start date", JOptionPane.DEFAULT_OPTION);

            String start;
            start = dateFormat.format(((JDateChooser) paramsStart[1]).getDate());
            LocalDate startDate = LocalDate.parse(start);

            String endMessage = "Choose end date:\n";
            Object[] paramsEnd = {endMessage, chooser};
            JOptionPane.showConfirmDialog(null, paramsEnd, "End date", JOptionPane.DEFAULT_OPTION);

            String end;
            end = dateFormat.format(((JDateChooser) paramsEnd[1]).getDate());
            LocalDate endDate = LocalDate.parse(end);

            calculate.calculate(startDate, endDate);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}