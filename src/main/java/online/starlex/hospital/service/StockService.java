package online.starlex.hospital.service;

import online.starlex.hospital.entity.DrugInfo;
import online.starlex.hospital.entity.DrugStock;
import online.starlex.hospital.entity.EquipmentInfo;
import online.starlex.hospital.utils.ErrorCode;

public interface StockService {
    ErrorCode drugAdd(DrugInfo drugInfo, DrugStock drugStock);
    ErrorCode drugSetStock(long drugId, long stockNum);
    ErrorCode drugRemove(long drugId);
    ErrorCode equipmentAdd(EquipmentInfo equipmentInfo);
    ErrorCode equipmentRemove(long equipmentId);
    ErrorCode staffAdd(int staffAuth, String staffName, String staffSex, String staffDepartment, String password);
    ErrorCode staffRemove(long staffId);
    ErrorCode sickbedAdd(long roomId, String bedDepartment, int bedPrice);
    ErrorCode sickbedRemove(long bedId);

    //检查资源是否存在
    boolean isResourcesExists(int resourcesType, long resourcesId);
    //检查药品是否足够
    boolean isEnoughDrug(long drugId, long drugNum);
    //冻结库存量
    boolean frozeStock(long drugId, long frozeNum);
    //解冻，如果冻结库存量不够就调度非冻结部分，如果还不够就返回false
    boolean unfrozeStock(long drugId, long unfrozeNum);
    //获得药品
    DrugStock getDrugStockByDrugId(long drugId);
    //获得设备
    EquipmentInfo getEquipmentInfoByEquipmentId(long equipmentId);
}
