import java.io.Serializable;

import java.util.Date;

public class Avocado implements Serializable {
    private Long date;
    private Double avgPrice;
    private Double volume;
    private String type;

    private String region;

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Double getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(Double avgPrice) {
        this.avgPrice = avgPrice;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }


    public Avocado(Long date, Double avgPrice, Double volume, String type, String region) {
        super();

        this.avgPrice = avgPrice;
        this.volume = volume;
        this.type = type;
        this.date = date;
        this.region = region;
    }

    public Avocado() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public String toString() {
        return "Avocado{" +
                "date=" + date +
                ", avgPrice=" + avgPrice +
                ", volume=" + volume +
                ", type='" + type + '\'' +
                ", region='" + region + '\'' +
                '}';
    }
}
