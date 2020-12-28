package online.starlex.hospital.controller;

import online.starlex.hospital.dao.SchedulingInfoDao;
import online.starlex.hospital.entity.*;
import online.starlex.hospital.service.FinanceService;
import online.starlex.hospital.service.OrderService;
import online.starlex.hospital.service.StockService;
import online.starlex.hospital.utils.ErrorCode;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(path = "/finance")
public class FinanceController {

    @Autowired
    FinanceService financeService;
    @Autowired
    StockService stockService;
    @Autowired
    OrderService orderService;
    @Autowired
    SchedulingInfoDao schedulingInfoDao;

    @RequestMapping(path = "/recharge", method = RequestMethod.POST)
    public Model recharge(@RequestParam("medical_record_id") long medicalRecordId,
                          @RequestParam("amount") int amount) {
        Model model = new Model();
        ErrorCode errorCode = financeService.recharge(medicalRecordId, amount);
        model.setCode(errorCode.getCode());
        model.setMsg(errorCode.getMsg());
        return model;
    }

    @RequestMapping(path = "/resources/schedule", method = RequestMethod.POST)
    public Model schedule(@RequestParam("order_id") long orderId) {
        Model model = new Model();
        ErrorCode errorCode = financeService.schedule(orderId);
        model.setCode(errorCode.getCode());
        model.setMsg(errorCode.getMsg());
        return model;
    }

    @RequestMapping(path = "/order/detail", method = RequestMethod.POST)
    public Model getOrderInfo(@RequestParam("order_id") long orderId) {
        Model model = new Model();
        Map<String, Object> data = new HashMap<>();
        JSONObject resourcesData;
        JSONArray resourcesDataList = new JSONArray();
        ErrorCode errorCode = ErrorCode.RESOURCES_NOT_ENOUGH;

        if (orderService.checkOrder(orderId)) {
            List<SchedulingInfo> schedulingInfoList = schedulingInfoDao.findAllByOrderId(orderId);
            for (SchedulingInfo schedulingInfo : schedulingInfoList) {
                resourcesData = new JSONObject();
                if (orderService.checkResources(schedulingInfo.getResourcesType(), schedulingInfo.getResourcesId())) {
                    if (schedulingInfo.getResourcesType() == 1) {
                        resourcesData.put("resources_type", "药品");
                        resourcesData.put("resources_num", schedulingInfo.getResourcesNum());
                        DrugStock drugStock = stockService.getDrugStockByDrugId(schedulingInfo.getResourcesId());
                        resourcesData.put("resources_id", drugStock.getDrugId());
                        resourcesData.put("resources_name", drugStock.getDrugName());
                        resourcesData.put("resources_value", drugStock.getValue());
                    } else if (schedulingInfo.getResourcesType() == 2) {
                        resourcesData.put("resources_type", "检查项目");
                        resourcesData.put("resources_num", schedulingInfo.getResourcesNum());
                        EquipmentInfo equipmentInfo = stockService.getEquipmentInfoByEquipmentId(schedulingInfo.getResourcesId());
                        resourcesData.put("resources_id", equipmentInfo.getEquipmentId());
                        resourcesData.put("resources_name", equipmentInfo.getEquipmentName());
                        resourcesData.put("resources_value", equipmentInfo.getValue());
                    } else {
                        errorCode = ErrorCode.RESOURCES_NOT_ENOUGH;
                        break;
                    }
                    resourcesDataList.put(resourcesData);
                } else {
                    errorCode = ErrorCode.RESOURCES_NOT_ENOUGH;
                    break;
                }
                errorCode = ErrorCode.OK;
            }
        } else {
            errorCode = ErrorCode.ORDER_NOT_EXISTS;
        }

        data.put("data", resourcesDataList.toString());
        model.setCode(errorCode.getCode());
        model.setMsg(errorCode.getMsg());
        model.setData(data);
        return model;
    }

    @RequestMapping(path = "/drug/return", method = RequestMethod.POST)
    public Model drugReturn(@RequestParam("medical_record_id") long medicalRecordId,
                            @RequestParam("drug_id") long drugId,
                            @RequestParam("drug_num") int drugNum) {
        Model model = new Model();
        ErrorCode errorCode = orderService.createDrugReturnOrder(medicalRecordId, drugId, drugNum);
        model.setCode(errorCode.getCode());
        model.setMsg(errorCode.getMsg());
        return model;
    }

    @RequestMapping(path = "/drug/add", method = RequestMethod.POST)
    public Model drugAdd(@RequestParam("drug_code") long drugCode,
                         @RequestParam("value") int value,
                         @RequestParam("expiration_date") String expirationDate,
                         @RequestParam("drug_name") String drugName,
                         @RequestParam("specification") String specification,
                         @RequestParam("company") String company,
                         @RequestParam("form") String form,
                         @RequestParam("stock") long stock) throws ParseException {
        Model model = new Model();
        DrugInfo drugInfo = new DrugInfo();
        drugInfo.setDrugCode(drugCode);
        drugInfo.setDrugName(drugName);
        drugInfo.setCompany(company);
        drugInfo.setForm(form);
        drugInfo.setSpecification(specification);
        DrugStock drugStock = new DrugStock();
        drugStock.setDrugName(drugName);
        drugStock.setStock(stock);
        drugStock.setValue(value);
        drugStock.setDrugCode(drugCode);
        drugStock.setExpirationDate(new SimpleDateFormat("yyyy-MM").parse(expirationDate));
        ErrorCode errorCode = stockService.drugAdd(drugInfo, drugStock);
        model.setCode(errorCode.getCode());
        model.setMsg(errorCode.getMsg());
        return model;
    }

    @RequestMapping(path = "/drug/set_stock", method = RequestMethod.POST)
    public Model drugSetStock(@RequestParam("drug_id") long drugId,
                              @RequestParam("stock_num") long stockNum) {
        Model model = new Model();
        ErrorCode errorCode = stockService.drugSetStock(drugId, stockNum);
        model.setCode(errorCode.getCode());
        model.setMsg(errorCode.getMsg());
        return model;
    }

    @RequestMapping(path = "/drug/remove", method = RequestMethod.POST)
    public Model drugRemove(@RequestParam("drug_id") long drugId) {
        Model model = new Model();
        ErrorCode errorCode = stockService.drugRemove(drugId);
        model.setCode(errorCode.getCode());
        model.setMsg(errorCode.getMsg());
        return model;
    }

    @RequestMapping(path = "/equipment/add", method = RequestMethod.POST)
    public Model equipmentAdd(@RequestParam("equipment_name") String equipmentName,
                              @RequestParam("equipment_location") String equipmentLocation,
                              @RequestParam("value") int value) {
        Model model = new Model();
        EquipmentInfo equipmentInfo = new EquipmentInfo();
        equipmentInfo.setEquipmentName(equipmentName);
        equipmentInfo.setEquipmentLocation(equipmentLocation);
        equipmentInfo.setValue(value);
        ErrorCode errorCode = stockService.equipmentAdd(equipmentInfo);
        model.setCode(errorCode.getCode());
        model.setMsg(errorCode.getMsg());
        return model;
    }

    @RequestMapping(path = "/equipment/remove", method = RequestMethod.POST)
    public Model equipmentRemove(@RequestParam("equipment_id") long equipmentId) {
        Model model = new Model();
        ErrorCode errorCode = stockService.equipmentRemove(equipmentId);
        model.setCode(errorCode.getCode());
        model.setMsg(errorCode.getMsg());
        return model;
    }

    @RequestMapping(path = "/staff/add", method = RequestMethod.POST)
    public Model staffAdd(@RequestParam("staff_auth") int staffAuth,
                          @RequestParam("staff_name") String staffName,
                          @RequestParam("staff_sex") String staffSex,
                          @RequestParam("staff_department") String staffDepartment,
                          @RequestParam("password") String password) {
        Model model = new Model();
        ErrorCode errorCode = stockService.staffAdd(staffAuth, staffName, staffSex, staffDepartment, password);
        model.setCode(errorCode.getCode());
        model.setMsg(errorCode.getMsg());
        return model;
    }

    @RequestMapping(path = "/staff/remove", method = RequestMethod.POST)
    public Model staffRemove(@RequestParam("staff_id") long staffId) {
        Model model = new Model();
        ErrorCode errorCode = stockService.staffRemove(staffId);
        model.setCode(errorCode.getCode());
        model.setMsg(errorCode.getMsg());
        return model;
    }

    @RequestMapping(path = "/sickbed/add", method = RequestMethod.POST)
    public Model sickbedAdd(@RequestParam("room_id") long roomId,
                            @RequestParam("bed_department") String bedDepartment,
                            @RequestParam("bed_price") int bedPrice) {
        Model model = new Model();
        ErrorCode errorCode = stockService.sickbedAdd(roomId, bedDepartment, bedPrice);
        model.setCode(errorCode.getCode());
        model.setMsg(errorCode.getMsg());
        return model;
    }

    @RequestMapping(path = "/sickbed/remove", method = RequestMethod.POST)
    public Model sickbedRemove(@RequestParam("bed_id") long bedId) {
        Model model = new Model();
        ErrorCode errorCode = stockService.sickbedRemove(bedId);
        model.setCode(errorCode.getCode());
        model.setMsg(errorCode.getMsg());
        return model;
    }
}
