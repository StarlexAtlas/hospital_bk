package online.starlex.hospital.entity;

import javax.persistence.*;
import java.util.Date;

@Table
@Entity(name = "order_info")
public class OrderInfo {
    @Id
    @Column(name = "order_id", columnDefinition = "bigint")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderId;
    @Column(name = "sum", columnDefinition = "int")
    private int sum;
    @Column(name = "medical_record_id", columnDefinition = "bigint")
    private long medicalRecordId;
    @Column(name = "operate_time", columnDefinition = "datetime")
    private Date operateTime;
    @Column(name = "complete_time", columnDefinition = "datetime")
    private Date completeTime;
    @Column(name = "status", columnDefinition = "tinyint")
    private boolean status;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public boolean isStatus() {
        return status;
    }

    public long getMedicalRecordId() {
        return medicalRecordId;
    }

    public void setMedicalRecordId(long medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
