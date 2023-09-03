package patientmonitorsystem.data;

public class MonitorDataRecord {
    private String type;
    private String id;
    private double time;
    private double value;
    
    public MonitorDataRecord(String id, String type, double value) {
        this.id = id;
        this.type = type;
        this.time = 0.0; 
        this.value = value;
    }
    
    public void setTime(double t) {
        this.time = t;
    }

    // getters
    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public double getTime() {
        return time;
    }

    public double getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return "<" + this.getId() + ", " + this.getType() + ", time: " + String.format("%.1f", this.getTime()) + ", value: " + String.format("%.1f>", this.getValue());
    }
}
