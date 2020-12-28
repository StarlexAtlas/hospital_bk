package online.starlex.hospital.service;

import online.starlex.hospital.dao.*;
import online.starlex.hospital.entity.*;
import online.starlex.hospital.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class StockServiceImpl implements StockService {

    @Autowired
    DrugInfoDao drugInfoDao;
    @Autowired
    EquipmentInfoDao equipmentInfoDao;
    @Autowired
    SickbedInfoDao sickbedInfoDao;
    @Autowired
    StaffInfoDao staffInfoDao;
    @Autowired
    DrugStockDao drugStockDao;
    @Autowired
    OrderService orderService;

    @Override
    public ErrorCode drugAdd(DrugInfo drugInfo, DrugStock drugStock) {
        if (!drugInfoDao.existsByDrugCode(drugInfo.getDrugCode())) {
            //如果不存在该药品，则增加一种药品种类并增加库存
            drugInfoDao.save(drugInfo);
        }
        //如果存在该药品，则直接增加一类库存
        drugStockDao.save(drugStock);
        return ErrorCode.OK;
    }

    @Override
    public ErrorCode drugSetStock(long drugId, long stockNum) {
        if (isResourcesExists(1, drugId)) {
            DrugStock drugStock = drugStockDao.findByDrugId(drugId);
            drugStock.setStock(stockNum);
            drugStockDao.save(drugStock);
            return ErrorCode.OK;
        } else {
            return ErrorCode.DRUG_NOT_EXISTS;
        }
    }

    @Override
    @Transactional
    public ErrorCode drugRemove(long drugId) {
        if (isResourcesExists(1, drugId)) {
            if (orderService.checkHaveNotResources(1, drugId)) {
                drugStockDao.deleteByDrugId(drugId);
                return ErrorCode.OK;
            } else {
                return ErrorCode.DRUG_CAN_NOT_REMOVE;
            }
        } else {
            return ErrorCode.DRUG_NOT_EXISTS;
        }
    }

    @Override
    public ErrorCode equipmentAdd(EquipmentInfo equipmentInfo) {
        equipmentInfoDao.save(equipmentInfo);
        return ErrorCode.OK;
    }

    @Override
    @Transactional
    public ErrorCode equipmentRemove(long equipmentId) {
        if (isResourcesExists(2, equipmentId)) {
            if (orderService.checkHaveNotResources(2, equipmentId)) {
                equipmentInfoDao.deleteByEquipmentId(equipmentId);
                return ErrorCode.OK;
            } else {
                return ErrorCode.EQUIPMENT_CAN_NOT_REMOVE;
            }
        } else {
            return ErrorCode.EQUIPMENT_NOT_EXISTS;
        }
    }

    @Override
    public ErrorCode staffAdd(int staffAuth, String staffName, String staffSex, String staffDepartment, String password) {
        StaffInfo staffInfo = new StaffInfo();
        //TODO:如果可能的话，检查是否重复用户,下面的一样
        staffInfo.setStaffAuth(staffAuth);
        staffInfo.setStaffName(staffName);
        staffInfo.setStaffSex(staffSex);
        staffInfo.setStaffDepartment(staffDepartment);
        staffInfo.setPassword(password);
        staffInfoDao.save(staffInfo);
        return ErrorCode.OK;
    }

    @Override
    @Transactional
    public ErrorCode staffRemove(long staffId) {
        if (staffInfoDao.existsByStaffId(staffId)) {
            staffInfoDao.deleteByStaffId(staffId);
            return ErrorCode.OK;
        } else {
            return ErrorCode.STAFF_NOT_EXISTS;
        }
    }

    @Override
    public ErrorCode sickbedAdd(long roomId, String bedDepartment, int bedPrice) {
        SickbedInfo sickbedInfo = new SickbedInfo();
        sickbedInfo.setRoomId(roomId);
        sickbedInfo.setBedDepartment(bedDepartment);
        sickbedInfo.setBedPrice(bedPrice);
        sickbedInfo.setBedStatus(0);
        sickbedInfoDao.save(sickbedInfo);
        return ErrorCode.OK;
    }

    @Override
    @Transactional
    public ErrorCode sickbedRemove(long bedId) {
        if (sickbedInfoDao.existsByBedId(bedId)) {
            if (sickbedInfoDao.findByBedId(bedId).getBedStatus() == 0) {
                sickbedInfoDao.deleteByBedId(bedId);
                return ErrorCode.OK;
            } else {
                return ErrorCode.SICKBED_CAN_NOT_REMOVE;
            }
        } else {
            return ErrorCode.SICKBED_NOT_EXISTS;
        }
    }

    @Override
    public boolean isResourcesExists(int resourcesType, long resourcesId) {
        if (resourcesType == 0) {
            return sickbedInfoDao.existsByBedId(resourcesId);
        } else if (resourcesType == 1) {
            return drugStockDao.existsByDrugId(resourcesId);
        } else if (resourcesType == 2) {
            return equipmentInfoDao.existsByEquipmentId(resourcesId);
        } else {
            return false;
        }
    }

    @Override
    public boolean isEnoughDrug(long drugId, long drugNum) {
        return drugStockDao.findByDrugId(drugId).getStock() >= drugNum;
    }

    @Override
    public boolean frozeStock(long drugId, long frozeNum) {
        DrugStock drugStock = drugStockDao.findByDrugId(drugId);
        if (drugStock.getStock() >= frozeNum) {
            drugStock.setStock(drugStock.getStock() - frozeNum);
            drugStock.setFrozenStock(drugStock.getFrozenStock() + frozeNum);
            drugStockDao.save(drugStock);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean unfrozeStock(long drugId, long unfrozeNum) {
        DrugStock drugStock = drugStockDao.findByDrugId(drugId);
        if (drugStock.getFrozenStock() >= unfrozeNum) {
            drugStock.setFrozenStock(drugStock.getFrozenStock() - unfrozeNum);
            drugStockDao.save(drugStock);
            return true;
        } else {
            if (drugStock.getStock() >= unfrozeNum - drugStock.getFrozenStock()) {
                drugStock.setStock(drugStock.getStock() - (unfrozeNum - drugStock.getFrozenStock()));
                drugStock.setFrozenStock(0);
                drugStockDao.save(drugStock);
            }
        }
        return false;
    }

    @Override
    public DrugStock getDrugStockByDrugId(long drugId) {
        return drugStockDao.findByDrugId(drugId);
    }

    @Override
    public EquipmentInfo getEquipmentInfoByEquipmentId(long equipmentId) {
        return equipmentInfoDao.findByEquipmentId(equipmentId);
    }
}
