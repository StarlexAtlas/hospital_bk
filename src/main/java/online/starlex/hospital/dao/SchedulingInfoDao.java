package online.starlex.hospital.dao;

import online.starlex.hospital.entity.SchedulingInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SchedulingInfoDao extends JpaRepository<SchedulingInfo, Long> {
    List<SchedulingInfo> findAllByResourcesTypeAndResourcesId(int resourcesType, long resourcesId);
    List<SchedulingInfo> findAllByOrderId(long orderId);
}
