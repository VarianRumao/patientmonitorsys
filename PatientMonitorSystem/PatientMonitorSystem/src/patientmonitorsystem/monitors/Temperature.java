package patientmonitorsystem.monitors;

import patientmonitorsystem.data.MonitorDataRecord;

public class Temperature extends Monitor {
    private int numErrs;
    private double[] temps;
    private int nextIndex;

    public Temperature(String monitorId, double... initialValues) {
        super("Temperature", monitorId);
        this.temps = initialValues;
        this.numErrs = 0;
        this.nextIndex = 0;
    }

    @Override
    public MonitorDataRecord read(String patientId) {
        double returnValue = this.temps[nextIndex];
        if (returnValue < 0) {
            this.numErrs ++;
            returnValue = Monitor.INVALID;
        }
        if (this.numErrs >= 2) {
            if (this.getStatus() != Status.FAILED) {
                this.setStatus(Status.FAILED);
                System.out.println("[!!] Patient {" + patientId + "} {" + this.getType() + "} monitor {" + this.getMonitorId() + "} failed!");
            }
        }
        MonitorDataRecord returnRecord;
        
        if (this.numErrs >= 2 || this.getStatus() == Status.FAILED) {
            return null;
        }
        
        returnRecord = new MonitorDataRecord(
                this.getMonitorId(), 
                this.getType(),
                returnValue
        );
        if (returnRecord.getValue() < 36) {
            // alert
            System.out.println("PATIENT ALERT: " + patientId + " " + returnRecord.getId() + " " + returnRecord.getType() + " " + returnRecord.getValue() + " - hypothermic");
        }
        else if (returnRecord.getValue() > 37.5) {
            System.out.println("PATIENT ALERT: " + patientId + " " + returnRecord.getId() + " " + returnRecord.getType() + " " + returnRecord.getValue() + " - fever");
        }
        this.nextIndex ++;
        return returnRecord;
    }

    @Override
    public boolean configure(String patientId) {
        this.setStatus(Status.IDLE);
        System.out.println("[*] Configured {" + this.getType() + "} monitor {" + this.getMonitorId() + "} for Patient {" + patientId + "} to IDLE.");
        return true;
    }

    @Override
    public boolean start(String patientID) {
        this.setStatus(Status.RUNNING);
        System.out.println("[*] Configured {" + this.getType() + "} monitor {" + this.getMonitorId() + "} for Patient {" + patientID + "} to RUNNING.");
        return true;
    }
    
}
