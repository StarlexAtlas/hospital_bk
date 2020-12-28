package online.starlex.hospital.entity;

import com.google.gson.annotations.SerializedName;

import javax.persistence.*;
import java.util.Date;

@Table
@Entity(name = "patient_info")
public class PatientInfo {
    @Id
    @Column(name = "medical_record_id", columnDefinition = "bigint")
    @SerializedName("medical_record_id")
    private long medicalRecordId;
    @Column(name = "current_bed_id", columnDefinition = "bigint")
    @SerializedName("current_bed_id")
    private long currentBedId = 0;
    @Column(name = "id_number", columnDefinition = "text(18)")
    @SerializedName("id_number")
    private String idNumber;
    @Column(name = "current_department", columnDefinition = "varchar(255)")
    @SerializedName("current_department")
    private String currentDepartment;
    @Column(name = "name", columnDefinition = "varchar(255)")
    @SerializedName("name")
    private String name;
    @Column(name = "sex", columnDefinition = "varchar(255)")
    @SerializedName("sex")
    private String sex;
    @Column(name = "birthday", columnDefinition = "date")
    @SerializedName("birthday")
    private Date birthday;
    @Column(name = "balance", columnDefinition = "int")
    @SerializedName("balance")
    private int balance;

    public long getMedicalRecordId() {
        return medicalRecordId;
    }

    public void setMedicalRecordId(long medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
    }

    public long getCurrentBedId() {
        return currentBedId;
    }

    public void setCurrentBedId(long currentBedId) {
        this.currentBedId = currentBedId;
    }

    public String getCurrentDepartment() {
        return currentDepartment;
    }

    public void setCurrentDepartment(String currentDepartment) {
        this.currentDepartment = currentDepartment;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
