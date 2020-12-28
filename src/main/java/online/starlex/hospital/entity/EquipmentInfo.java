package online.starlex.hospital.entity;

import com.google.gson.annotations.SerializedName;

import javax.persistence.*;

@Table
@Entity(name = "equipment_info")
public class EquipmentInfo {
    @Id
    @Column(name = "equipment_id", columnDefinition = "bigint")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SerializedName("equipment_id")
    private long equipmentId;
    @Column(name = "equipment_name", columnDefinition = "varchar(255)")
    @SerializedName("equipment_name")
    private String equipmentName;
    @Column(name = "equipment_location", columnDefinition = "varchar(255)")
    @SerializedName("equipment_location")
    private String equipmentLocation;
    @Column(name = "value", columnDefinition = "int")
    @SerializedName("value")
    private int value;

    public long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getEquipmentLocation() {
        return equipmentLocation;
    }

    public void setEquipmentLocation(String equipmentLocation) {
        this.equipmentLocation = equipmentLocation;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
