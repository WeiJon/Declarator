import java.math.BigDecimal;

public class Rate {
    private String date;
    private BigDecimal cad;
    private BigDecimal chf;
    private BigDecimal jpy;
    private BigDecimal usd;

    public Rate() {
    }

    public Rate(String date, BigDecimal cad, BigDecimal chf, BigDecimal jpy, BigDecimal usd) {
        this.date = date;
        this.cad = cad;
        this.chf = chf;
        this.jpy = jpy;
        this.usd = usd;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public BigDecimal getCad() {
        return cad;
    }

    public void setCad(BigDecimal cad) {
        this.cad = cad;
    }

    public BigDecimal getChf() {
        return chf;
    }

    public void setChf(BigDecimal chf) {
        this.chf = chf;
    }

    public BigDecimal getJpy() {
        return jpy;
    }

    public void setJpy(BigDecimal jpy) {
        this.jpy = jpy;
    }

    public BigDecimal getUsd() {
        return usd;
    }

    public void setUsd(BigDecimal usd) {
        this.usd = usd;
    }
}
