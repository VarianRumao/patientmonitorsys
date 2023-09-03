package patientmonitorsystem;
// 12222847 
// Author Varian rumao
import patientmonitorsystem.monitors.BloodPressure;
import patientmonitorsystem.monitors.Oxygen;
import patientmonitorsystem.monitors.Temperature;
import patientmonitorsystem.unit.MonitoredPatient;
import patientmonitorsystem.unit.MonitoringStation;

public class PatientMonitorSystem {

    public static void main(String[] args) {
        // Simulating patient monitor system behavior with different data scenarios

        // Data streams for a patient with stable health
        double[] oxygen1 = {95, 95, 97, 97, 97, 97, 98, 95, 90, 90, 90, 95, 99, 98};
        double[] temp1 = {36.5, 35.0, 37.0, 37.0, 37.0, 37.0, 38.5, 38.5, 37.5, 36.5, 37.0, 36.5, 37.0, 37.0};
        double[] systolic1 = {90, 90, 80, 100, 115, 98, 75, 70, 80, 85, 85, 98, 95, 87};
        double[] diastolic1 = {85, 60, 82, 90, 95, 98, 70, 85, 90, 92, 85, 98, 95, 80};
        
        // Data streams for a patient with various errors
        double[] oxygen2 = {95, 95, 97, 97, 97, 97, 98, -1, 90, 90, -1, 0, 6.5, 4 };
        double[] temp2 = {36.5, 35.0, 37.0, 37.0, 37.0, 37.0, 38.5, 38.5, 37.5, 36.5, -1, 36.5, -1, -1};
        double[] systolic2 = {90, 90, 80, 100, 115, -1, 75, 70, 80, 85, 85, 98, 95, -1};
        double[] diastolic2 = {85, 60, -1, 90, 95, 98, 70, 85, 90, -1, 85, 98, 95, -1};
        
        // Data streams for a patient with consecutive errors
        double[] systolic3 = {90, 90, 80, 100, 115, -1, 75, 70, 80, 85, 85, 98, 95, -1};
        double[] diastolic3 = {85, 60, -1, -1, 95, 98, 70, 85, 90, -1, 85, 98, 95, -1};
        
        // Creating monitors for the first patient
        Oxygen oxygenMonitor1 = new Oxygen("oxygen1", oxygen1);
        Temperature tempMonitor1 = new Temperature("temp1", temp1);
        BloodPressure bp1 = new BloodPressure("bp1", systolic1, diastolic1);
        
        // Creating monitors for the second patient
        Oxygen oxygenMonitor2 = new Oxygen("oxygen2", oxygen2);
        Temperature tempMonitor2 = new Temperature("temp2", temp2);
        BloodPressure bp2 = new BloodPressure("bp2", systolic2, diastolic2);
        BloodPressure bp3 = new BloodPressure("bp3", systolic3, diastolic3);
        
        // Creating monitored patients with their associated monitors
        MonitoredPatient patient1 = new MonitoredPatient("patient1", oxygenMonitor1, tempMonitor1, bp1);
        MonitoredPatient patient2 = new MonitoredPatient("patient2", oxygenMonitor2, tempMonitor2, bp2, bp3);
        
        // Creating a monitoring station with specified settings
        MonitoringStation station1 = new MonitoringStation(14, 10, 6);
        
        // Adding patients to the monitoring station
        station1.addPatient(patient1);
        station1.addPatient(patient2);
        
        // Running the monitoring simulation
        station1.run();
    }
}
