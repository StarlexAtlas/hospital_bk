package online.starlex.hospital.dao;

import online.starlex.hospital.entity.DrugStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DrugStockDao extends JpaRepository<DrugStock, Long> {
    boolean existsByDrugId(long drugId);
    DrugStock findByDrugId(long drugId);
    void deleteByDrugId(long drugId);
    List<DrugStock> findAllByDrugNameLike(String drugName);
}
