package online.starlex.hospital.service;

import online.starlex.hospital.dao.PatientInfoDao;
import online.starlex.hospital.dao.SickbedInfoDao;
import online.starlex.hospital.entity.PatientInfo;
import online.starlex.hospital.entity.SickbedInfo;
import online.starlex.hospital.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    PatientInfoDao patientInfoDao;
    @Autowired
    SickbedInfoDao sickbedInfoDao;
    @Autowired
    FinanceService financeService;
    @Autowired
    OrderService orderService;

    @Override
    public ErrorCode register(PatientInfo patientInfo) {
        if (!isExists(patientInfo.getIdNumber())) {
            if (!isExists(patientInfo.getMedicalRecordId())) {
                patientInfoDao.save(patientInfo);
                return ErrorCode.OK;
            } else {
                //病历本编号已经被注册过
                return ErrorCode.MEDICAL_HAS_REGISTERED;
            }
        } else {
            //病人已存在，无法开新户
            return ErrorCode.PATIENT_HAS_EXISTS;
        }
    }

    @Override
    public ErrorCode admission(long medicalRecordId, String department, long roomId, int preBalance) {
        if (isExists(medicalRecordId)) {
            if (!isAdmission(medicalRecordId)) {
                if (hasAvailableBed(roomId)) {
                    SickbedInfo sickbedInfo = sickbedInfoDao.findFirstByRoomIdAndBedStatus(roomId, 0);
                    //修改病床为占用
                    sickbedInfo.setBedStatus(1);
                    //填写入院时间
                    sickbedInfo.setCheckInTime(new Date(System.currentTimeMillis()));
                    sickbedInfoDao.save(sickbedInfo);
                    //修改病人余额以及病床状态
                    PatientInfo patientInfo = patientInfoDao.findByMedicalRecordId(medicalRecordId);
                    patientInfo.setCurrentDepartment(department);
                    patientInfo.setBalance(preBalance);
                    patientInfo.setCurrentBedId(sickbedInfo.getBedId());
                    patientInfoDao.save(patientInfo);
                    return ErrorCode.OK;
                } else {
                    //病床不足
                    return ErrorCode.BED_NOT_ENOUGH;
                }
            } else {
                //病人已经入院
                return ErrorCode.PATIENT_HAS_ADMISSION;
            }
        } else {
            //病历本编号不存在
            return ErrorCode.MEDICAL_NOT_EXISTS;
        }
    }

    @Override
    public ErrorCode transfer(long medicalRecordId, String department, long targetRoomId) {
        if (isExists(medicalRecordId)) {
            if (isAdmission(medicalRecordId)) {
                if (hasAvailableBed(targetRoomId)) {
                    PatientInfo patientInfo = patientInfoDao.findByMedicalRecordId(medicalRecordId);
                    SickbedInfo sickbedInfo = sickbedInfoDao.findByBedId(patientInfo.getCurrentBedId());
                    orderService.sumSickbedOrder(medicalRecordId);
                    patientInfo = patientInfoDao.findByMedicalRecordId(medicalRecordId);
                    sickbedInfo.setBedStatus(0);
                    sickbedInfo.setCheckInTime(null);
                    sickbedInfoDao.save(sickbedInfo);
                    sickbedInfo = sickbedInfoDao.findFirstByRoomIdAndBedStatus(targetRoomId, 0);
                    sickbedInfo.setBedStatus(1);
                    sickbedInfo.setCheckInTime(new Date(System.currentTimeMillis()));
                    sickbedInfoDao.save(sickbedInfo);
                    patientInfo.setCurrentBedId(sickbedInfo.getBedId());
                    patientInfo.setCurrentDepartment(department);
                    patientInfoDao.save(patientInfo);
                    return ErrorCode.OK;
                } else {
                    return ErrorCode.BED_NOT_ENOUGH;
                }
            } else {
                return ErrorCode.PATIENT_CAN_NOT_TRANSFER;
            }
        } else {
            return ErrorCode.MEDICAL_NOT_EXISTS;
        }
    }

    @Override
    public ErrorCode disCharge(long medicalRecordId) {
        if (isExists(medicalRecordId)) {
            if (isAdmission(medicalRecordId)) {
                PatientInfo patientInfo = patientInfoDao.findByMedicalRecordId(medicalRecordId);
                SickbedInfo sickbedInfo = sickbedInfoDao.findByBedId(patientInfo.getCurrentBedId());
                orderService.sumSickbedOrder(medicalRecordId);
                orderService.clearNoUseOrder(medicalRecordId);
                patientInfo = patientInfoDao.findByMedicalRecordId(medicalRecordId);
                sickbedInfo.setBedStatus(0);
                sickbedInfo.setCheckInTime(null);
                patientInfo.setCurrentBedId(0);
                patientInfo.setCurrentDepartment(null);
                sickbedInfoDao.save(sickbedInfo);
                patientInfoDao.save(patientInfo);
                return ErrorCode.OK;
            } else {
                return ErrorCode.PATIENT_HAS_DISCHARGE;
            }
        } else {
            return ErrorCode.MEDICAL_NOT_EXISTS;
        }
    }

    @Override
    public int clearCurrentBalance(long medicalRecordId) {
        PatientInfo patientInfo = patientInfoDao.findByMedicalRecordId(medicalRecordId);
        if (patientInfo.getBalance() >= 0) {
            int extraAmount = patientInfo.getBalance();
            patientInfo.setBalance(0);
            patientInfoDao.save(patientInfo);
            return extraAmount;
        } else {
            return -1;
        }
    }

    @Override
    public boolean isExists(long medicalRecordId) {
        return patientInfoDao.existsByMedicalRecordId(medicalRecordId);
    }

    @Override
    public boolean isExists(String idNumber) {
        return patientInfoDao.existsByIdNumber(idNumber);
    }

    @Override
    public boolean isAdmission(long medicalRecordId) {
        return patientInfoDao.findByMedicalRecordId(medicalRecordId).getCurrentBedId() != 0;
    }

    @Override
    public boolean hasAvailableBed(long roomId) {
        return sickbedInfoDao.existsByRoomIdAndBedStatus(roomId, 0);
    }
}
