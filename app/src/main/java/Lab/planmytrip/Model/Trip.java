package Lab.planmytrip.Model;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Trip {
    public ArrayList<GeoPoint> checkpoints;
    public String name;
    public ArrayList<Map<String, Object>> baggage;

    public Trip() {
    }

    public Trip(ArrayList<GeoPoint> checkpoints, String name, ArrayList<Map<String, Object>> baggage) {
        this.checkpoints = checkpoints;
        this.name = name;
        this.baggage = baggage;
    }

    public ArrayList<GeoPoint> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(ArrayList<GeoPoint> checkpoints) {
        this.checkpoints = checkpoints;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Map<String, Object>> getBaggage() {
        return baggage;
    }

    public void setBaggage(ArrayList<Map<String, Object>> baggage) {
        this.baggage = baggage;
    }
}
