package patientmonitorsystem.unit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import patientmonitorsystem.data.ConsolidatedMonitorRecord;
import patientmonitorsystem.monitors.Monitor;
import patientmonitorsystem.data.MonitorDataRecord;

public class MonitoredPatient {
    public String patientId;
    private ArrayList<Monitor> monitors;
    private HashMap<String, ArrayList<MonitorDataRecord>> data;
    
    public MonitoredPatient(String id, Monitor...monitors) {
        this.patientId = id;
        this.monitors = new ArrayList<>();
        this.monitors.addAll(Arrays.asList(monitors));
        
        this.data = new HashMap<>();
        for (Monitor monitor: this.monitors) {
            this.data.put(monitor.getMonitorId(), new ArrayList<>());
        }
    }
    
    public void start() {
        for (Monitor monitor: this.monitors) {
            monitor.start(this.patientId);
        }
    }
    
    public void configure() {
        for (Monitor monitor: this.monitors) {
            monitor.configure(this.patientId);
        }
    }
    
    public void readPatientMonitors(int clock) {
        for (Monitor monitor: this.monitors) {
            MonitorDataRecord patientRecord = monitor.read(this.patientId);
            
            if (patientRecord != null) {
                patientRecord.setTime(clock);
                System.out.println("\tMonitor Unit Display (" + this.patientId + "): " + patientRecord.toString());
            }
            
            ArrayList<MonitorDataRecord> currentMonitorRecords = this.data.get(monitor.getMonitorId());
            
            // just an extra precaution in case, somehow, unknown monitors get into the records
            if (currentMonitorRecords == null) {
                currentMonitorRecords = new ArrayList<>();
                currentMonitorRecords.add(patientRecord);
                this.data.put(monitor.getMonitorId(), currentMonitorRecords);
            }
            else {
                currentMonitorRecords.add(patientRecord);
            }
        }
    }
    
    public void consolidate(Formatter f, int lower, int higher, int delayTime) {
        System.out.println("The consolidated readings for time " + (lower * delayTime) + " - " + (higher * delayTime) + " are: ");
        
        for (Monitor monitor: this.monitors) {
            if (monitor.getStatus() == Monitor.Status.FAILED) {
                System.out.println(patientId + " < " + monitor.getMonitorId() + " " + monitor.getType() + " " + monitor.getStatus() + " monitor> ");
            }
            else {
                this.consolidate(f, monitor, lower, higher, delayTime);
            }
        }
        System.out.println();
    }
    
    public void consolidate(Formatter f, Monitor m, int lower, int upper, int delayTime) {
        ArrayList<MonitorDataRecord> records = data.get(m.getMonitorId());
        List<MonitorDataRecord> window = new ArrayList<>(); 
       
        int n = 0;
        for (int i = lower; i <= upper; i++) {
            MonitorDataRecord r = records.get(i);
            if (r != null) {
                n++;
               window.add(r); 
            } 
        }

        if (n != 0) {
            ConsolidatedMonitorRecord cdr = new ConsolidatedMonitorRecord(m, lower*delayTime,  upper * delayTime);
            
            cdr.setStatistics(window); 

            System.out.println(this.patientId + ": " + cdr);
            f.format("Consolidated Data Record (%s) - %s\n", this.patientId, cdr);
            f.flush();
        }
    }
}
