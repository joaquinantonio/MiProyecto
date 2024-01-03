package com.ja.miproyecto;

public class PrescriptionDetails {

    private String PatientName,PatientGender,Age,DoctorsAddress,PatientHeight,PatientWeight,ConsultationType,Consultation_Date,
            Previous_Lab_Report,Relevent_Point_from_history,Diagnosis_Information,Examinations,Instructions,Rx;
    private int flag;

    public PrescriptionDetails(String patientName, String patientGender, String age, String doctorsAddress, String patientHeight, String patientWeight, String consultationType, String consultation_Date, String previous_Lab_Report, String relevent_Point_from_history, String diagnosis_Information, String examinations, String instructions, String rx, int flag) {
        PatientName = patientName;
        PatientGender = patientGender;
        Age = age;
        DoctorsAddress = doctorsAddress;
        PatientHeight = patientHeight;
        PatientWeight = patientWeight;
        ConsultationType = consultationType;
        Consultation_Date = consultation_Date;
        Previous_Lab_Report = previous_Lab_Report;
        Relevent_Point_from_history = relevent_Point_from_history;
        Diagnosis_Information = diagnosis_Information;
        Examinations = examinations;
        Instructions = instructions;
        Rx = rx;
        this.flag = flag;
    }

    public String getPatientName() {
        return PatientName;
    }

    public String getPatientGender() {
        return PatientGender;
    }

    public String getAge() {
        return Age;
    }

    public String getDoctorsAddress() {
        return DoctorsAddress;
    }

    public String getPatientHeight() {
        return PatientHeight;
    }

    public String getPatientWeight() {
        return PatientWeight;
    }

    public String getConsultationType() {
        return ConsultationType;
    }

    public String getConsultation_Date() {
        return Consultation_Date;
    }

    public String getPrevious_Lab_Report() {
        return Previous_Lab_Report;
    }

    public String getRelevent_Point_from_history() {
        return Relevent_Point_from_history;
    }

    public String getDiagnosis_Information() {
        return Diagnosis_Information;
    }

    public String getExaminations() {
        return Examinations;
    }

    public void setExaminations(String examinations) {
        Examinations = examinations;
    }

    public String getInstructions() {
        return Instructions;
    }

    public String getRx() {
        return Rx;
    }

    public void setRx(String rx) {
        Rx = rx;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
