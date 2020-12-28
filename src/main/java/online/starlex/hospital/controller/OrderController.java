package online.starlex.hospital.controller;

import online.starlex.hospital.entity.Model;
import online.starlex.hospital.service.OrderService;
import online.starlex.hospital.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(path = "/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @RequestMapping(path = "/drug_order", method = RequestMethod.POST)
    public Model drugOrder(@RequestParam("medical_record_id") long medicalRecordId,
                           @RequestParam("drug_list") String drugList) {
        Model model = new Model();
        ErrorCode errorCode = orderService.createDrugOrder(medicalRecordId, drugList);
        model.setCode(errorCode.getCode());
        model.setMsg(errorCode.getMsg());
        return model;
    }

    @RequestMapping(path = "/equipment_order", method = RequestMethod.POST)
    public Model equipmentOrder(@RequestParam("medical_record_id") long medicalRecordId,
                                @RequestParam("equipment_id") long equipmentId,
                                @RequestParam("equipment_num") int equipmentNum){
        Model model = new Model();
        ErrorCode errorCode = orderService.createEquipmentOrder(medicalRecordId, equipmentId, equipmentNum);
        model.setCode(errorCode.getCode());;
        model.setMsg(errorCode.getMsg());
        return model;
    }

    @RequestMapping(path = "/drug_order/return", method = RequestMethod.POST)
    public Model drugReturn(@RequestParam("medical_record_id") long medicalRecordId,
                            @RequestParam("drug_id") long drugId,
                            @RequestParam("drug_num") int drugNum){
        Model model = new Model();
        ErrorCode errorCode = orderService.createDrugReturnOrder(medicalRecordId, drugId, drugNum);
        model.setCode(errorCode.getCode());
        model.setMsg(errorCode.getMsg());
        return model;
    }
}
