package online.starlex.hospital.service;

import online.starlex.hospital.dao.*;
import online.starlex.hospital.entity.*;
import online.starlex.hospital.utils.ErrorCode;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    SchedulingInfoDao schedulingInfoDao;
    @Autowired
    OrderInfoDao orderInfoDao;
    @Autowired
    StockService stockService;
    @Autowired
    DrugStockDao drugStockDao;
    @Autowired
    EquipmentInfoDao equipmentInfoDao;
    @Autowired
    SickbedInfoDao sickbedInfoDao;
    @Autowired
    PatientInfoDao patientInfoDao;

    @Override
    public ErrorCode createDrugOrder(long medicalRecordId, String drugList) {
        JSONArray drugListJ = new JSONArray(drugList);
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setMedicalRecordId(medicalRecordId);
        orderInfo.setOperateTime(new Date(System.currentTimeMillis()));
        orderInfo.setStatus(false);
        orderInfoDao.save(orderInfo);
        for (int i = 0; i < drugListJ.length(); i++) {
            JSONObject drugMap = drugListJ.getJSONObject(i);
            SchedulingInfo schedulingInfo = new SchedulingInfo();
            schedulingInfo.setResourcesType(1);
            schedulingInfo.setResourcesId(drugMap.getLong("drug_id"));
            schedulingInfo.setResourcesNum(drugMap.getInt("drug_count"));
            if (stockService.isResourcesExists(1, schedulingInfo.getResourcesId())) {
                if (stockService.isEnoughDrug(schedulingInfo.getResourcesId(), schedulingInfo.getResourcesNum())) {
                    if (stockService.frozeStock(schedulingInfo.getResourcesId(), schedulingInfo.getResourcesNum())) {
                        //关联调度和订单
                        schedulingInfo.setOrderId(orderInfo.getOrderId());
                        DrugStock drugStock = drugStockDao.findByDrugId(schedulingInfo.getResourcesId());
                        //修改总价
                        orderInfo.setSum(orderInfo.getSum() + drugStock.getValue() * schedulingInfo.getResourcesNum());
                        schedulingInfoDao.save(schedulingInfo);
                        orderInfoDao.save(orderInfo);
                    } else {
                        return ErrorCode.DRUG_NOT_ENOUGH;
                    }
                } else {
                    return ErrorCode.DRUG_NOT_ENOUGH;
                }
            } else {
                return ErrorCode.DRUG_NOT_EXISTS;
            }
        }
        return ErrorCode.OK;
    }

    @Override
    public ErrorCode createEquipmentOrder(long medicalRecordId, long equipmentId, int equipmentNum) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOperateTime(new Date(System.currentTimeMillis()));
        orderInfo.setStatus(false);
        orderInfo.setMedicalRecordId(medicalRecordId);
        orderInfoDao.save(orderInfo);
        SchedulingInfo schedulingInfo = new SchedulingInfo();
        schedulingInfo.setOrderId(orderInfo.getOrderId());
        schedulingInfo.setResourcesType(2);
        if (stockService.isResourcesExists(2, equipmentId)) {
            schedulingInfo.setResourcesType(2);
            schedulingInfo.setResourcesId(equipmentId);
            schedulingInfo.setResourcesNum(equipmentNum);
            orderInfo.setSum(equipmentInfoDao.findByEquipmentId(equipmentId).getValue() * equipmentNum);
            orderInfoDao.save(orderInfo);
            schedulingInfoDao.save(schedulingInfo);
            return ErrorCode.OK;
        } else {
            return ErrorCode.EQUIPMENT_NOT_EXISTS;
        }
    }

    @Override
    public ErrorCode createDrugReturnOrder(long medicalRecordId, long drugId, int drugNum) {
        if (patientInfoDao.existsByMedicalRecordId(medicalRecordId)) {
            List<OrderInfo> orderInfoList = orderInfoDao.findAllByMedicalRecordIdAndStatus(medicalRecordId, true);
            int haveNum = 0;
            for (OrderInfo orderInfo : orderInfoList) {
                List<SchedulingInfo> schedulingInfoList = schedulingInfoDao.findAllByOrderId(orderInfo.getOrderId());
                for (SchedulingInfo schedulingInfo : schedulingInfoList) {
                    if (schedulingInfo.getResourcesType() == 1 && schedulingInfo.getResourcesId() == drugId) {
                        haveNum += schedulingInfo.getResourcesNum();
                    }
                }
            }
            if (haveNum >= drugNum) {
                PatientInfo patientInfo = patientInfoDao.findByMedicalRecordId(medicalRecordId);
                DrugStock drugStock = drugStockDao.findByDrugId(drugId);
                drugStock.setStock(drugStock.getStock() + drugNum);
                drugStockDao.save(drugStock);
                patientInfo.setBalance(patientInfo.getBalance() + drugStock.getValue() * drugNum);
                patientInfoDao.save(patientInfo);
                return ErrorCode.OK;
            } else {
                return ErrorCode.RESOURCES_NOT_ENOUGH;
            }
        } else {
            return ErrorCode.MEDICAL_NOT_EXISTS;
        }
    }

    @Override
    public boolean sumSickbedOrder(long medicalRecordId) {
        PatientInfo patientInfo = patientInfoDao.findByMedicalRecordId(medicalRecordId);
        SickbedInfo sickbedInfo = sickbedInfoDao.findByBedId(patientInfo.getCurrentBedId());
        Date date = sickbedInfo.getCheckInTime();
        Date dateNow = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Calendar calendarNow = Calendar.getInstance();
        calendarNow.setTime(dateNow);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendarNow.set(Calendar.MINUTE, 0);
        calendarNow.set(Calendar.SECOND, 0);
        calendarNow.set(Calendar.MILLISECOND, 0);
        int countDays = 0;
        while (calendar.compareTo(calendarNow) < 0) {
            countDays++;
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        if (countDays <= 0) {
            countDays = 1;
        }
        patientInfo.setBalance(patientInfo.getBalance() - countDays * sickbedInfo.getBedPrice());
        patientInfoDao.save(patientInfo);
        return false;
    }

    @Override
    public boolean checkHaveNotResources(int resourcesType, long resourcesId) {
        List<SchedulingInfo> schedulingInfoList = schedulingInfoDao.findAllByResourcesTypeAndResourcesId(resourcesType, resourcesId);
        for (SchedulingInfo schedulingInfo : schedulingInfoList) {
            if (orderInfoDao.findByOrderId(schedulingInfo.getOrderId()) != null && !orderInfoDao.findByOrderId(schedulingInfo.getOrderId()).getStatus()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean checkOrder(long orderId) {
        return orderInfoDao.existsByOrderId(orderId);
    }

    @Override
    public boolean checkResources(int resourcesType, long resourcesId) {
        //药品
        if (resourcesType == 1) {
            return drugStockDao.existsByDrugId(resourcesId);
        }
        //设备
        if (resourcesType == 2) {
            return equipmentInfoDao.existsByEquipmentId(resourcesId);
        }

        return false;
    }

    @Override
    @Transactional
    public void clearNoUseOrder(long medicalRecordId) {
        orderInfoDao.deleteAllByMedicalRecordId(medicalRecordId);
    }

}
