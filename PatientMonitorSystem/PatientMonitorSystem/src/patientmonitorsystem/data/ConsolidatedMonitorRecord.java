package patientmonitorsystem.data;

import java.util.List;
import patientmonitorsystem.monitors.Monitor;

/**
 * Represents consolidated data for a specific monitor's readings over a time window.
 */
public class ConsolidatedMonitorRecord {
    // Properties
    private String type;
    private String id;
    private Monitor.Status status;
    private double t1;
    private double t2;
    private int validCount;
    private int invalidCount;
    private double averageValue;
    private double maxValue;
    private double minValue;

    /**
     * Constructor to create a consolidated record for a monitor.
     * @param monitor The monitor for which to create the consolidated record.
     * @param t1 The starting time of the window.
     * @param t2 The ending time of the window.
     */
    public ConsolidatedMonitorRecord(Monitor monitor, double t1, double t2) {
        this.id = monitor.getMonitorId();
        this.type = monitor.getType();
        this.status = monitor.getStatus();
        this.t1 = t1;
        this.t2 = t2;
    }
    
    /**
     * Calculates statistics from the given window of monitor data records.
     * @param window List of monitor data records in the time window.
     */
    public void setStatistics(List<MonitorDataRecord> window) {
        double recordsSum = 0;
        this.maxValue = 0;
        this.minValue = Double.MAX_VALUE;
        
        for (MonitorDataRecord dataRecord: window) {
            if (dataRecord == null) {
                this.invalidCount++;
            }
            else {
                this.validCount++;
                recordsSum += dataRecord.getValue();
                
                if (dataRecord.getValue() < this.minValue) {
                    this.minValue = dataRecord.getValue();
                }
                if (dataRecord.getValue() > this.maxValue) {
                    this.maxValue = dataRecord.getValue();
                }
            }
        }
        this.averageValue = recordsSum / this.validCount;
    }
    
    @Override
    public String toString() {
        return String.format("<%s, %s, %s, <%.1f, %.1f>, %d, %d, %.1f, %.1f, %.1f>",
                this.id,
                this.type,
                this.status,
                this.t1,
                this.t2,
                this.validCount,
                this.invalidCount,
                this.averageValue,
                this.maxValue,
                this.minValue);
    }
}
