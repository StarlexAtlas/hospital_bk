package online.starlex.hospital.entity;

import com.google.gson.annotations.SerializedName;

import javax.persistence.*;
import java.util.Date;

@Table
@Entity(name = "sickbed_info")
public class SickbedInfo {
    @Id
    @SerializedName("bed_id")
    @Column(name = "bed_id", columnDefinition = "bigint")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long bedId;
    @SerializedName("room_id")
    @Column(name = "room_id", columnDefinition = "bigint")
    private long roomId;
    @SerializedName("check_in_time")
    @Column(name = "check_in_time", columnDefinition = "date")
    private Date checkInTime;
    @SerializedName("bed_department")
    @Column(name = "bed_department", columnDefinition = "varchar(255)")
    private String bedDepartment;
    @SerializedName("bed_price")
    @Column(name = "bed_price", columnDefinition = "int")
    private int bedPrice;
    @SerializedName("bed_status")
    @Column(name = "bed_status", columnDefinition = "int(1)")
    private int bedStatus;

    public long getBedId() {
        return bedId;
    }

    public void setBedId(long bedId) {
        this.bedId = bedId;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public Date getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(Date checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getBedDepartment() {
        return bedDepartment;
    }

    public void setBedDepartment(String bedDepartment) {
        this.bedDepartment = bedDepartment;
    }

    public int getBedPrice() {
        return bedPrice;
    }

    public void setBedPrice(int bedPrice) {
        this.bedPrice = bedPrice;
    }

    public int getBedStatus() {
        return bedStatus;
    }

    public void setBedStatus(int bedStatus) {
        this.bedStatus = bedStatus;
    }
}
