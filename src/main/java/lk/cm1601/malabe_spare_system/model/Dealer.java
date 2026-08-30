package lk.cm1601.malabe_spare_system.model;

public class Dealer {

    private String dealerID;
    private String dealerName;
    private String phone;
    private String location;

    public Dealer(String dealerID, String dealerName, String phone, String location) {
        this.dealerID = dealerID;
        this.dealerName = dealerName;
        this.phone = phone;
        this.location = location;
    }

    public String getDealerID() {
        return dealerID;
    }

    public void setDealerID(String dealerID) {
        this.dealerID = dealerID;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}