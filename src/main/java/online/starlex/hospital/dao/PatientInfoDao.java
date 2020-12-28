package online.starlex.hospital.dao;

import online.starlex.hospital.entity.PatientInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientInfoDao extends JpaRepository<PatientInfo, Long> {
    boolean existsByIdNumber(String idNumber);
    boolean existsByMedicalRecordId(long medicalRecordId);
    PatientInfo findByMedicalRecordId(long medicalRecordId);
    List<PatientInfo> findAllByCurrentDepartment(String department);
}
