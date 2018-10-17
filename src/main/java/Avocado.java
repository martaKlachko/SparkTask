import java.io.Serializable;

import java.util.Date;

public class Avocado implements Serializable{
    private Date date;
    private Double avgPrice;
    private Double volume;
    private String type;
    private Date year;
    private String region;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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

    public Date getYear() {
        return year;
    }

    public void setYear(Date year) {
        this.year = year;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((avgPrice == null) ? 0 : avgPrice.hashCode());
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((region == null) ? 0 : region.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((volume == null) ? 0 : volume.hashCode());
        result = prime * result + ((year == null) ? 0 : year.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Avocado other = (Avocado) obj;
        if (avgPrice == null) {
            if (other.avgPrice != null)
                return false;
        } else if (!avgPrice.equals(other.avgPrice))
            return false;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (region == null) {
            if (other.region != null)
                return false;
        } else if (!region.equals(other.region))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        if (volume == null) {
            if (other.volume != null)
                return false;
        } else if (!volume.equals(other.volume))
            return false;
        if (year == null) {
            if (other.year != null)
                return false;
        } else if (!year.equals(other.year))
            return false;
        return true;
    }

    public Avocado(Date date, Double avgPrice, Double volume, String type, Date year, String region) {
        super();
        this.date = date;
        this.avgPrice = avgPrice;
        this.volume = volume;
        this.type = type;
        this.year = year;
        this.region = region;
    }

    public Avocado() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public String toString() {
        return "Avocado [date=" + date + ", avgPrice=" + avgPrice + ", volume=" + volume + ", type=" + type + ", year="
                + year + ", region=" + region + "]+\n" ;
    }


}
