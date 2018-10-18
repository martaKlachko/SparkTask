import java.io.Serializable;

import java.util.Date;

public class Avocado implements Serializable{

    private Double avgPrice;
    private Double volume;
    private String type;

    private String region;



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




    public Avocado( Double avgPrice, Double volume, String type,  String region) {
        super();

        this.avgPrice = avgPrice;
        this.volume = volume;
        this.type = type;

        this.region = region;
    }

    public Avocado() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public String toString() {
        return "Avocado [ avgPrice=" + avgPrice + ", volume=" + volume + ", type=" + type  + ", region=" + region + "]+\n" ;
    }


}
