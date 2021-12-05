package Lab.planmytrip.Model;

public class PackageItem {

    private int id;
    private boolean status;
    private String itemName;

    public PackageItem() {
    }

    public PackageItem(int id, boolean status, String itemName) {
        this.id = id;
        this.status = status;
        this.itemName = itemName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}