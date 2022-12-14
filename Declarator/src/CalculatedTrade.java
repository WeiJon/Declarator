import java.math.BigDecimal;

public class CalculatedTrade {
    private Long amount;
    private String currency;
    private BigDecimal sellPrice;
    private BigDecimal buyPrice;
    private BigDecimal result;
    private BigDecimal dollarResult;
    private boolean isMt5;

    public CalculatedTrade() {
    }
    public CalculatedTrade(Long amount, String currency, BigDecimal sellPrice, BigDecimal buyPrice, BigDecimal result, BigDecimal dollarResult, boolean isMt5) {
        this.amount = amount;
        this.currency = currency;
        this.sellPrice = sellPrice;
        this.buyPrice = buyPrice;
        this.result = result;
        this.dollarResult = dollarResult;
        this.isMt5 = isMt5;
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

    public boolean isMt5() {
        return isMt5;
    }

    public void setMt5(boolean mt5) {
        isMt5 = mt5;
    }
}
