package online.starlex.hospital.service;

import online.starlex.hospital.entity.PatientInfo;
import online.starlex.hospital.utils.ErrorCode;

public interface PatientService {
    //新病人入院
    ErrorCode register(PatientInfo patientInfo);
    //入院
    ErrorCode admission(long medicalRecordId, String department, long roomId, int preBalance);
    //转移病房
    ErrorCode transfer(long medicalRecordId, String department, long targetRoomId);
    //出院
    ErrorCode disCharge(long medicalRecordId);
    //返回当前余额（出院时结算返回数值，如果大于等于0则清空余额）
    int clearCurrentBalance(long medicalRecordId);

    //检查是否存在该旧病人
    boolean isExists(long medicalRecordId);
    boolean isExists(String idNumber);
    //检查该病人是否入院
    boolean isAdmission(long medicalRecordId);
    //检查指定病房是否有空余床位
    boolean hasAvailableBed(long roomId);
}
