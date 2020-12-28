package online.starlex.hospital.service;

import online.starlex.hospital.utils.ErrorCode;

public interface OrderService {
    ErrorCode createDrugOrder(long medicalRecordId, String drugList);
    ErrorCode createEquipmentOrder(long medicalRecordId, long equipmentId, int equipmentNum);
    ErrorCode createDrugReturnOrder(long medicalRecordId, long drugId, int drugNum);
    boolean sumSickbedOrder(long medicalRecordId);
    //true表示有未完成订单
    boolean checkHaveNotResources(int resourcesType, long resourcesId);
    boolean checkOrder(long orderId);
    //是否存在该资源
    boolean checkResources(int resourcesType, long resourcesId);
    //清空未完成订单
    void clearNoUseOrder(long medicalRecordId);
}
