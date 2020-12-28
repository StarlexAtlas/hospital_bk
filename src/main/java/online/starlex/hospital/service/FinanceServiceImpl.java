package online.starlex.hospital.service;

import online.starlex.hospital.dao.OrderInfoDao;
import online.starlex.hospital.dao.PatientInfoDao;
import online.starlex.hospital.dao.SchedulingInfoDao;
import online.starlex.hospital.entity.OrderInfo;
import online.starlex.hospital.entity.PatientInfo;
import online.starlex.hospital.entity.SchedulingInfo;
import online.starlex.hospital.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FinanceServiceImpl implements FinanceService {

    @Autowired
    PatientInfoDao patientInfoDao;
    @Autowired
    OrderInfoDao orderInfoDao;
    @Autowired
    SchedulingInfoDao schedulingInfoDao;
    @Autowired
    PatientService patientService;
    @Autowired
    StockService stockService;

    @Override
    public ErrorCode recharge(long medicalRecordId, int amount) {
        if (patientService.isExists(medicalRecordId)) {
            PatientInfo patientInfo = patientInfoDao.findByMedicalRecordId(medicalRecordId);
            patientInfo.setBalance(patientInfo.getBalance() + amount);
            patientInfoDao.save(patientInfo);
            return ErrorCode.OK;
        } else {
            return ErrorCode.MEDICAL_NOT_EXISTS;
        }
    }

    @Override
    public ErrorCode schedule(long orderId) {
        if (orderInfoDao.existsByOrderId(orderId)) {
            OrderInfo orderInfo = orderInfoDao.findByOrderId(orderId);
            orderInfo.setStatus(true);
            orderInfo.setCompleteTime(new Date(System.currentTimeMillis()));
            if (patientInfoDao.existsByMedicalRecordId(orderInfo.getMedicalRecordId())) {
                PatientInfo patientInfo = patientInfoDao.findByMedicalRecordId(orderInfo.getMedicalRecordId());
                patientInfo.setBalance(patientInfo.getBalance() - orderInfo.getSum());
                if(getResourcesType(orderId) == 1){
                    List<SchedulingInfo> schedulingInfoList = schedulingInfoDao.findAllByOrderId(orderId);
                    for(SchedulingInfo schedulingInfo : schedulingInfoList){
                        if(stockService.unfrozeStock(schedulingInfo.getResourcesId(), schedulingInfo.getResourcesNum())){
                            patientInfoDao.save(patientInfo);
                            orderInfoDao.save(orderInfo);
                        } else {
                            return ErrorCode.DRUG_NOT_ENOUGH;
                        }
                    }
                } else {
                    patientInfoDao.save(patientInfo);
                    orderInfoDao.save(orderInfo);
                }
            } else {
                return ErrorCode.ORDER_CAN_NOT_COMPLETE;
            }
        } else {
            return ErrorCode.ORDER_NOT_EXISTS;
        }
        return ErrorCode.OK;
    }

    int getResourcesType(long orderId){
        List<SchedulingInfo> schedulingInfoList = schedulingInfoDao.findAllByOrderId(orderId);
        return schedulingInfoList.get(0).getResourcesType();
    }
}
