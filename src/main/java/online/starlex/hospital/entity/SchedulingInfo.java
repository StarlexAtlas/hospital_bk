package online.starlex.hospital.entity;

import javax.persistence.*;

@Table
@Entity(name = "scheduling_info")
public class SchedulingInfo {
    @Id
    @Column(name = "scheduling_id", columnDefinition = "bigint")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long schedulingId;
    @Column(name = "order_id", columnDefinition = "bigint")
    private long orderId;
    @Column(name = "resources_type", columnDefinition = "int")
    private int resourcesType;
    @Column(name = "resources_id", columnDefinition = "bigint")
    private long resourcesId;
    @Column(name = "resources_num", columnDefinition = "bigint")
    private int resourcesNum;

    public long getSchedulingId() {
        return schedulingId;
    }

    public void setSchedulingId(long schedulingId) {
        this.schedulingId = schedulingId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public int getResourcesType() {
        return resourcesType;
    }

    public void setResourcesType(int resourcesType) {
        this.resourcesType = resourcesType;
    }

    public long getResourcesId() {
        return resourcesId;
    }

    public void setResourcesId(long resourcesId) {
        this.resourcesId = resourcesId;
    }

    public void setResourcesNum(int resourcesNum) {
        this.resourcesNum = resourcesNum;
    }

    public int getResourcesNum() {
        return resourcesNum;
    }
}
