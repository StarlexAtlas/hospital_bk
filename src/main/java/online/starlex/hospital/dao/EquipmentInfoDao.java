package online.starlex.hospital.dao;

import online.starlex.hospital.entity.EquipmentInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipmentInfoDao extends JpaRepository<EquipmentInfo, Long> {
    boolean existsByEquipmentId(long equipmentId);
    void deleteByEquipmentId(long equipmentId);
    List<EquipmentInfo> findAllByEquipmentNameLike(String equipmentName);
    EquipmentInfo findByEquipmentId(long equipmentId);
}
