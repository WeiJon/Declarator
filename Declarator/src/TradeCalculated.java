import java.math.BigDecimal;

public class TradeCalculated {
    private Long amount;
    private String currency;
    private BigDecimal buyPrice;
    private BigDecimal sellPrice;
    private BigDecimal result;
    private BigDecimal dollarResult;

    public TradeCalculated() {
    }
    public TradeCalculated(Long amount, String currency, BigDecimal buyPrice, BigDecimal sellPrice, BigDecimal result, BigDecimal dollarResult) {
        this.amount = amount;
        this.currency = currency;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.result = result;
        this.dollarResult = dollarResult;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(BigDecimal buyPrice) {
        this.buyPrice = buyPrice;
    }

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }

    public BigDecimal getResult() {
        return result;
    }

    public void setResult(BigDecimal result) {
        this.result = result;
    }

    public BigDecimal getDollarResult() {
        return dollarResult;
    }

    public void setDollarResult(BigDecimal dollarResult) {
        this.dollarResult = dollarResult;
    }
}
