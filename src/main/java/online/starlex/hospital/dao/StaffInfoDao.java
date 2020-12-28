package online.starlex.hospital.dao;

import online.starlex.hospital.entity.StaffInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StaffInfoDao extends JpaRepository<StaffInfo, Long> {
    boolean existsByStaffIdAndPassword(long staffId, String password);
    boolean existsByStaffId(long staffId);
    StaffInfo findByStaffId(long staffId);
    void deleteByStaffId(long staffId);
    List<StaffInfo> findAllByStaffNameLike(String staffName);
}
