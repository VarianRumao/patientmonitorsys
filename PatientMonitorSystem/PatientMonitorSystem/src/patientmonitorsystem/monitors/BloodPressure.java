package patientmonitorsystem.monitors;

import patientmonitorsystem.data.MonitorDataRecord;

public class BloodPressure extends Monitor {
    private int numConsErrs;
    private int nextIndex;
    private double[] systolic;
    private double[] diastolic;

    public BloodPressure(String monitorId, double[] systolic, double[] diastolic) {
        super("Blood Pressure", monitorId);
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.numConsErrs = 0;
        this.nextIndex = 0; 
    }
    

    public BloodPressure(String monitorId, double... systDiast) {
        super("Blood Pressure", monitorId);

        if (systDiast.length % 2 != 0) {
            throw new IllegalArgumentException("Each systolic value must have a corresponding diastolic value.");
        }

        int halfLength = systDiast.length / 2;
        this.systolic = new double[halfLength];
        this.diastolic = new double[halfLength];

        for (int i = 0; i < halfLength; i++) {
            this.systolic[i] = systDiast[i];
            this.diastolic[i] = systDiast[i + halfLength];
        }

        this.numConsErrs = 0;
        this.nextIndex = 0;
    }

    


    @Override
    public MonitorDataRecord read(String patientId) {
        double returnMAP;
        double returnSystolic, returnDiastolic;
        
        if (this.systolic[nextIndex] < 0) {
            this.numConsErrs ++;
            returnSystolic = Monitor.INVALID;
        }
        else {
            // only remove a 'consequtive error if none of the monitors fail
            // this.numConsErrs --;
            returnSystolic = this.systolic[nextIndex];
        }
        if (this.diastolic[nextIndex] < 0) {
            this.numConsErrs ++;
            returnDiastolic = Monitor.INVALID;
        }
        else {
            // this.numConsErrs --;
            returnDiastolic = this.diastolic[nextIndex];
        }
        if (returnSystolic != Monitor.INVALID && returnDiastolic != Monitor.INVALID) {
            if (this.numConsErrs > 0) this.numConsErrs --;
        }
        returnMAP = ((1.0/3.0) * returnSystolic) + ((2.0/3.0) * returnDiastolic);
        
        if (numConsErrs >= 2) {
            if (this.getStatus() != Status.FAILED) {
                this.setStatus(Status.FAILED);
                System.out.println("[!!] Patient {" + patientId + "} {" + this.getType() + "} monitor {" + this.getMonitorId() + "} failed!");
            }
        }
        MonitorDataRecord returnRecord;
        
        if (this.numConsErrs >= 2 || this.getStatus() == Status.FAILED) {
            return null;
        }
        
        returnRecord = new MonitorDataRecord(
                patientId, 
                "MAP", 
                returnMAP
        );
        if (returnRecord.getValue() < 70) {
            System.out.println("PATIENT ALERT: " + patientId + " " + returnRecord.getId() + " " + returnRecord.getType() + " " + String.format("%.1f", returnRecord.getValue()) + " - LOW");
        }
        else if (returnRecord.getValue() > 100) {
            // alert
            System.out.println("PATIENT ALERT: " + patientId + " " + returnRecord.getId() + " " + returnRecord.getType() + " " + String.format("%.1f", returnRecord.getValue()) + " - HIGH");
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
