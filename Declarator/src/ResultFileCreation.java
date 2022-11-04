import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.List;

public class ResultFileCreation {
    public void createResultFile() throws IOException, ParseException {
        final String pathName = "C:/Users/Jonas/Desktop/result.html";
        File file = new File(pathName);
        Calculation calculation = new Calculation();
        List<TradeCalculated> tradeList = calculation.calculate();

        Document doc = Jsoup.parse("<html><table><tbody>" + insertTrades(tradeList) + "</tbody></table> </html>");

        FileUtils.writeStringToFile(file, String.valueOf(doc), StandardCharsets.UTF_8);

        openFile(pathName);
    }

    private StringBuilder insertTrades(List<TradeCalculated> tradeList) {
        StringBuilder totalRows = new StringBuilder();

        for (TradeCalculated tradeCalculated : tradeList) {
            String totalCells = ("<td>" + tradeCalculated.getAmount() + "</td>"
                    + "<td>" + tradeCalculated.getCurrency() + "</td>"
                    + "<td>" + tradeCalculated.getSellPrice() + "</td>"
                    + "<td>" + tradeCalculated.getBuyPrice() + "</td>"
                    + "<td>" + tradeCalculated.getResult() + "</td>"
                    + "<td>" + tradeCalculated.getDollarResult() + "</td>");

            totalRows.append("<tr>").append(totalCells).append("</tr>");
        }

        return totalRows;
    }

    private static void openFile(String targetFilePath) throws IOException {
        Desktop desktop = Desktop.getDesktop();
        desktop.open(new File(targetFilePath));
    }
}
