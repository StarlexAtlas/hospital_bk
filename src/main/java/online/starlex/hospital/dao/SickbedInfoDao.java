package online.starlex.hospital.dao;

import online.starlex.hospital.entity.SickbedInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SickbedInfoDao extends JpaRepository<SickbedInfo, Long> {
    SickbedInfo findByBedId(long bedId);

    SickbedInfo findFirstByRoomIdAndBedStatus(long roomId, int status);

    boolean existsByRoomIdAndBedStatus(long roomId, int status);

    boolean existsByBedId(long bedId);

    List<SickbedInfo> findAllByBedDepartmentAndBedStatus(String department, int status);

    List<SickbedInfo> findAll();

    void deleteByBedId(long bedId);
}
