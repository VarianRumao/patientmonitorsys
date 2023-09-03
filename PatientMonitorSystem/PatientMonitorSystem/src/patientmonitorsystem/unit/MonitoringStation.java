package patientmonitorsystem.unit;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Formatter;

public class MonitoringStation {
    private int clock;
    private int monitoringDuration;
    private int timeBetweenReadings;
    private int consolidationInterval;
    private ArrayList<MonitoredPatient> patients;
    
    public MonitoringStation(int duration, int readingInterval, int consolidationInterval) {
        this.monitoringDuration = duration;
        this.timeBetweenReadings = readingInterval;
        this.consolidationInterval = consolidationInterval;
        
        this.patients = new ArrayList<>();
    }
    
    public void addPatient(MonitoredPatient patient) {
        this.patients.add(patient);
        patient.configure();
    }
    
    public void run() {
        for (MonitoredPatient patient: this.patients) {
            patient.start();
        }
        
        try {
            Formatter consolidatedDataFormatter = new Formatter("consolidated.txt");
            
            int currentTimeIndex = 0;
            this.clock = 0;
            int numReadingsInWindow = 0;
            for (; currentTimeIndex < this.monitoringDuration; currentTimeIndex++) {
                for (MonitoredPatient patient: this.patients) {
                    patient.readPatientMonitors(this.clock);

                    if ((numReadingsInWindow + 1) % this.consolidationInterval == 0) {
                        patient.consolidate(
                                consolidatedDataFormatter, 
                                currentTimeIndex - this.consolidationInterval + 1, 
                                currentTimeIndex, 
                                this.timeBetweenReadings
                        );
                    }
                }
                numReadingsInWindow++;
                this.clock += this.timeBetweenReadings;
            }
            
            if (numReadingsInWindow % this.consolidationInterval != 0) {
                for (MonitoredPatient patient: this.patients) {
                    patient.consolidate(
                            consolidatedDataFormatter,
                            // Adjusting for consolidation across multiple patients
                            (currentTimeIndex - (numReadingsInWindow % this.consolidationInterval)),
                            (currentTimeIndex - 1), 
                            this.timeBetweenReadings);
                }
            }
        }
        catch (FileNotFoundException ex) {
            System.out.println("[!!] ERROR: Could not find output file for consolidated records!");
        }
    }
}
