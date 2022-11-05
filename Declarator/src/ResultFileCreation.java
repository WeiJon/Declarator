import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ResultFileCreation {
    private final List<TradeCalculated> tradeListAud = new ArrayList<>();
    private final List<TradeCalculated> tradeListEur = new ArrayList<>();
    private final List<TradeCalculated> tradeListGbp = new ArrayList<>();
    private final List<TradeCalculated> tradeListUsd = new ArrayList<>();

    public void createResultFile() throws IOException {
        final String pathName = "C:/Users/Jonas/Desktop/result.html";
        File file = new File(pathName);
        Calculation calculation = new Calculation();
        List<TradeCalculated> tradeList = calculation.calculate();

        splitList(tradeList);

        Document doc = compileHtml();

        FileUtils.writeStringToFile(file, String.valueOf(doc), StandardCharsets.UTF_8);

        openFile(pathName);
    }

    private void splitList(List<TradeCalculated> tradeList) {
        for (TradeCalculated trade : tradeList) {
            switch (trade.getCurrency()) {
                case "AUD" -> tradeListAud.add(trade);
                case "EUR" -> tradeListEur.add(trade);
                case "GBP" -> tradeListGbp.add(trade);
                case "USD" -> tradeListUsd.add(trade);
            }
        }
    }

    private StringBuilder[] insertTrades(List<TradeCalculated> tradeList) {
        StringBuilder totalRows = new StringBuilder();

        String currency = tradeList.get(0).getCurrency();
        long amountSum = 0L;
        BigDecimal sellPriceSum = BigDecimal.ZERO;
        BigDecimal buyPriceSum = BigDecimal.ZERO;
        BigDecimal resultSum = BigDecimal.ZERO;
        BigDecimal resultUsdSum = BigDecimal.ZERO;

        for (TradeCalculated tradeCalculated : tradeList) {
            String totalCells = (
                    "<td>" + tradeCalculated.getAmount() + "</td>" +
                    "<td>" + tradeCalculated.getCurrency() + "</td>" +
                    "<td>" + tradeCalculated.getSellPrice().setScale(6, RoundingMode.HALF_UP) + "</td>" +
                    "<td>" + tradeCalculated.getBuyPrice().setScale(6, RoundingMode.HALF_UP) + "</td>" +
                    "<td>" + tradeCalculated.getResult() + "</td>" +
                    "<td>" + tradeCalculated.getDollarResult() + "</td>");

            amountSum += tradeCalculated.getAmount();
            sellPriceSum = sellPriceSum.add(tradeCalculated.getSellPrice());
            buyPriceSum = buyPriceSum.add(tradeCalculated.getBuyPrice());
            resultSum = resultSum.add(tradeCalculated.getResult());
            resultUsdSum = resultUsdSum.add(tradeCalculated.getDollarResult());

            totalRows.append("<tr>").append(totalCells).append("</tr>");
        }
        StringBuilder[] totalPerCurrency = totalPerCurrency(amountSum, currency, sellPriceSum, buyPriceSum, resultSum, resultUsdSum);

        return new StringBuilder[] {totalRows, totalPerCurrency[0], totalPerCurrency[1],totalPerCurrency[2], totalPerCurrency[3]};
    }

    private StringBuilder[] totalPerCurrency(long amount, String currency, BigDecimal sellPrice, BigDecimal buyPrice, BigDecimal result, BigDecimal resultUsd) {
        return new StringBuilder[] {new StringBuilder(
                "<th>" + amount + "</th>" +
                "<th>" + currency + "</th>" +
                "<th>" + sellPrice.setScale(0, RoundingMode.HALF_UP) + "</th>" +
                "<th>" + buyPrice.setScale(0, RoundingMode.HALF_UP) + "</th>" +
                "<th>" + result.setScale(0, RoundingMode.HALF_UP) + "</th>" +
                "<th>" + resultUsd.setScale(0, RoundingMode.HALF_UP) + "</th>"),
                new StringBuilder(result.setScale(0, RoundingMode.HALF_UP).toString()),
                new StringBuilder(result.toString()),
                new StringBuilder(resultUsd.toString())};
    }

    private StringBuilder insertTotal(StringBuilder totalAud, StringBuilder totalEur, StringBuilder totalGbp, StringBuilder totalUsd, BigDecimal sek, BigDecimal usd) {
        Integer totalBefore =
                Integer.parseInt(totalAud.toString()) +
                Integer.parseInt(totalEur.toString()) +
                Integer.parseInt(totalGbp.toString()) +
                Integer.parseInt(totalUsd.toString());
        BigDecimal totalTax = new BigDecimal(totalBefore * 0.3).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalAfter = new BigDecimal(String.valueOf(totalBefore)).subtract(totalTax).setScale(2, RoundingMode.HALF_UP);

        return new StringBuilder(
                "<th>" + totalBefore + " SEK" +"</th>" +
                "<th>" + totalTax + " SEK" + "</th>" +
                "<th>" + totalAfter + " SEK" +"</th>" +
                "<th></th>" +
                "<th>Add To Account: </th>" +
                "<td>" + usd + "</td>" +
                "<td>" + sek + "</td>");
    }

    private Document compileHtml() {
        String[] htmlList = htmlParts();

        final StringBuilder emptyPerCurrency = new StringBuilder((
                "<th>" + 0 + "</th>" +
                "<th>" + "N/A" + "</th>" +
                "<th>" + 0 + "</th>" +
                "<th>" + 0 + "</th>" +
                "<th>" + 0 + "</th>" +
                "<th>" + 0 + "</th>"));
        final StringBuilder emptyList = new StringBuilder((
                "<td>" + 0 + "</td>" +
                "<td>" + "N/A" + "</td>" +
                "<td>" + 0 + "</td>" +
                "<td>" + 0 + "</td>" +
                "<td>" + 0 + "</td>" +
                "<td>" + 0 + "</td>"));

        StringBuilder[] tradesAud = new StringBuilder[] {emptyList,  emptyPerCurrency, new StringBuilder("0"), new StringBuilder("0"), new StringBuilder("0")};
        StringBuilder[] tradesEur = new StringBuilder[] {emptyList,  emptyPerCurrency, new StringBuilder("0"), new StringBuilder("0"), new StringBuilder("0")};
        StringBuilder[] tradesGbp = new StringBuilder[] {emptyList,  emptyPerCurrency, new StringBuilder("0"), new StringBuilder("0"), new StringBuilder("0")};
        StringBuilder[] tradesUsd = new StringBuilder[] {emptyList,  emptyPerCurrency, new StringBuilder("0"), new StringBuilder("0"), new StringBuilder("0")};

        if (!tradeListAud.isEmpty()) {
            tradesAud = insertTrades(tradeListAud);
        }
        if (!tradeListEur.isEmpty()) {
            tradesEur = insertTrades(tradeListEur);
        }
        if (!tradeListGbp.isEmpty()) {
            tradesGbp = insertTrades(tradeListGbp);
        }
        if (!tradeListUsd.isEmpty()) {
            tradesUsd = insertTrades(tradeListUsd);
        }

        BigDecimal totalSek = new BigDecimal(tradesAud[3].toString())
                .add(new BigDecimal(tradesEur[3].toString()))
                .add(new BigDecimal(tradesGbp[3].toString()))
                .add(new BigDecimal(tradesUsd[3].toString()));

        BigDecimal totalUsd = new BigDecimal(tradesAud[4].toString())
                .add(new BigDecimal(tradesEur[4].toString()))
                .add(new BigDecimal(tradesGbp[4].toString()))
                .add(new BigDecimal(tradesUsd[4].toString()));

        StringBuilder total = insertTotal(tradesAud[2], tradesEur[2], tradesGbp[2], tradesUsd[2], totalSek, totalUsd);

        return Jsoup.parse(htmlList[0] + total + htmlList[1] +
                htmlList[2] + tradesAud[1] + htmlList[3] + tradesAud[0] + htmlList[4] +
                htmlList[2] + tradesEur[1] + htmlList[3] + tradesEur[0] + htmlList[4] +
                htmlList[2] + tradesGbp[1] + htmlList[3] + tradesGbp[0] + htmlList[4] +
                htmlList[2] + tradesUsd[1] + htmlList[3] + tradesUsd[0] + htmlList[4] +
                htmlList[5]);
    }

    private String[] htmlParts() {
        String html0 = """
                <html>
                <head></head>
                <style>
                .tableBlock {
                display: inline;
                font-size: 15px;
                }
                .tableHead {
                display: flex;
                justify-content: center;
                font-size: x-large;
                margin-bottom: 10px;
                }
                div {
                display: flex;
                }
                table,th,td {
                border: 1px solid black;
                text-align: center;
                }
                </style>
                <body>
                <table class=tableHead>
                <tr>
                <td>Profit Before Tax</td>
                <td>Total Tax</td>
                <td>Profit After Tax</td>
                <td style="width: 50px"></td>
                <td></td>
                <td>USD</td>
                <td>SEK</td>
                </tr>
                <tr>""";
        String html1 = """
                </tr>
                </table>
                <div>""";
        String html2 = """
                <table class="tableBlock">
                <tbody>
                <tr>
                <th>Amount</th>
                <th>Currency</th>
                <th>SellPrice</th>
                <th>Buy Price</th>
                <th>Profit in SEK</th>
                <th>Profit in USD</th>
                </tr>
                <tr>""";
        String html3 = """
                </tr>
                </tbody>
                <tbody>""";
        String html4 = """
                </tbody>
                </table>
                """;
        String html5 = """
                </div>
                </tbody>
                </html>""";

        return new String[] {html0, html1, html2, html3, html4, html5};
    }

    private static void openFile(String targetFilePath) throws IOException {
        Desktop desktop = Desktop.getDesktop();
        desktop.open(new File(targetFilePath));
    }
}
