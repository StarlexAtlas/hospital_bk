package online.starlex.hospital.dao;

import online.starlex.hospital.entity.DrugInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrugInfoDao extends JpaRepository<DrugInfo, Long> {
    boolean existsByDrugCode(long drugCode);
    DrugInfo findByDrugCode(long drugCode);
}
