package online.starlex.hospital.dao;

import online.starlex.hospital.entity.OrderInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderInfoDao extends JpaRepository<OrderInfo, Long> {
    boolean existsByOrderId(long orderId);
    OrderInfo findByOrderId(long orderId);
    void deleteAllByMedicalRecordId(long medicalRecordId);
    List<OrderInfo> findAllByMedicalRecordIdAndStatus(long medicalRecordId, boolean status);
}
