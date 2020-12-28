package online.starlex.hospital.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.persistence.*;

@Table
@Entity(name = "staff_info")
public class StaffInfo {
    @Id
    @Expose
    @SerializedName("staff_id")
    @Column(name = "staff_id", columnDefinition = "bigint")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long staffId;
    @Expose(serialize = false, deserialize = false)
    @Column(name = "password", columnDefinition = "varchar(255)")
    private String password;
    @Expose
    @SerializedName("staff_auth")
    @Column(name = "staff_auth", columnDefinition = "int")
    private int staffAuth;
    @Expose
    @SerializedName("staff_name")
    @Column(name = "staff_name", columnDefinition = "varchar(255)")
    private String staffName;
    @Expose
    @SerializedName("staff_sex")
    @Column(name = "staff_sex", columnDefinition = "varchar(255)")
    private String staffSex;
    @Expose
    @SerializedName("staff_department")
    @Column(name = "staff_department", columnDefinition = "varchar(255)")
    private String staffDepartment;

    public long getStaffId() {
        return staffId;
    }

    public void setStaffId(long staffId) {
        this.staffId = staffId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStaffAuth() {
        return staffAuth;
    }

    public void setStaffAuth(int staffAuth) {
        this.staffAuth = staffAuth;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffSex() {
        return staffSex;
    }

    public void setStaffSex(String staffSex) {
        this.staffSex = staffSex;
    }

    public String getStaffDepartment() {
        return staffDepartment;
    }

    public void setStaffDepartment(String staffDepartment) {
        this.staffDepartment = staffDepartment;
    }
}
