package patientmonitorsystem.monitors;

import patientmonitorsystem.data.MonitorDataRecord;

/**
 * Abstract class for different types of monitors used in the system.
 */
public abstract class Monitor {
    // Constants
    public static final double INVALID = 9999.0;

    // Status options for the monitor
    public static enum Status { IDLE, RUNNING, FAILED };

    // Properties
    private String type;
    private String monitorID;
    private Status status;

 
    public Monitor(String type, String id) {
        this.type = type;
        this.monitorID = id;
        this.status = Status.IDLE;
    }

 
    public void setStatus(Status s) {
        this.status = s;
    }

    /**
     * Gets the current status of the monitor.
     * @return The current status.
     */
    public Status getStatus() {
        return this.status;
    }

    /**
     * Gets the type of the monitor.
     * @return The monitor type.
     */
    public String getType() {
        return this.type;
    }

    /**
     * Gets the unique ID of the monitor.
     * @return The monitor's ID.
     */
    public String getMonitorId() {
        return monitorID;
    }

    // Abstract methods that need to be implemented in subclasses
    public abstract MonitorDataRecord read(String patientId);
    public abstract boolean configure(String patientId);
    public abstract boolean start(String patientID);
}
