package online.starlex.hospital.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import online.starlex.hospital.dao.*;
import online.starlex.hospital.entity.*;
import online.starlex.hospital.utils.ErrorCode;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping(path = "/info")
public class InfoController {

    @Autowired
    PatientInfoDao patientInfoDao;
    @Autowired
    DrugStockDao drugStockDao;
    @Autowired
    DrugInfoDao drugInfoDao;
    @Autowired
    EquipmentInfoDao equipmentInfoDao;
    @Autowired
    SickbedInfoDao sickbedInfoDao;
    @Autowired
    StaffInfoDao staffInfoDao;
    @Autowired
    OrderInfoDao orderInfoDao;

    @RequestMapping(path = "/patient_info", method = RequestMethod.GET)
    public Model patientQuery(@RequestParam("medical_record_id") long medicalRecordId) {
        Model model = new Model();
        Map<String, Object> data = new HashMap<>();
        PatientInfo patientInfo = patientInfoDao.findByMedicalRecordId(medicalRecordId);
        if (patientInfo != null) {
            JSONObject jsonValue = new JSONObject(new Gson().toJson(patientInfo));
            jsonValue.put("birthday", new SimpleDateFormat("yyyy-MM-dd").format(patientInfo.getBirthday()));
            if(sickbedInfoDao.findByBedId(patientInfo.getCurrentBedId()) == null){
                jsonValue.put("room_id", 0);
            } else {
                jsonValue.put("room_id", sickbedInfoDao.findByBedId(patientInfo.getCurrentBedId()).getRoomId());
            }
            data.put("patient_info", jsonValue.toString());
            model.setData(data);
            model.setCode(0);
            model.setMsg("成功");
        } else {
            model.setCode(ErrorCode.MEDICAL_NOT_EXISTS.getCode());
            model.setMsg(ErrorCode.MEDICAL_NOT_EXISTS.getMsg());
        }
        return model;
    }

    @RequestMapping(path = "/drug", method = RequestMethod.GET)
    public Model drugQuery(@RequestParam("drug_name") String drugName) {
        Model model = new Model();
        Map<String, Object> data = new HashMap<>();
        List<DrugStock> drugStockList = drugStockDao.findAllByDrugNameLike("%" + drugName + "%");
        JSONArray jsonArray = new JSONArray();
        for (DrugStock drugStock : drugStockList) {
            JSONObject jsonValue = new JSONObject(new Gson().toJson(drugStock));
            jsonValue.put("expiration_date", new SimpleDateFormat("yyyy-MM").format(drugStock.getExpirationDate()));
            DrugInfo drugInfo = drugInfoDao.findByDrugCode(drugStock.getDrugCode());
            jsonValue.put("specification", drugInfo.getSpecification());
            jsonValue.put("company", drugInfo.getCompany());
            jsonValue.put("form", drugInfo.getForm());
            jsonArray.put(jsonValue);
        }
        data.put("drug_info", jsonArray.toString());
        model.setData(data);
        model.setCode(0);
        model.setMsg("成功");
        return model;
    }

    @RequestMapping(path = "/equipment", method = RequestMethod.GET)
    public Model equipmentQuery(@RequestParam("equipment_name") String equipmentName) {
        Model model = new Model();
        Map<String, Object> data = new HashMap<>();
        List<EquipmentInfo> equipmentInfoList = equipmentInfoDao.findAllByEquipmentNameLike("%" + equipmentName + "%");
        data.put("equipment_info", new Gson().toJson(equipmentInfoList));
        model.setData(data);
        model.setCode(0);
        model.setMsg("成功");
        return model;
    }

    @RequestMapping(path = "/room", method = RequestMethod.GET)
    public Model roomQuery(@RequestParam("department") String department) {
        Model model = new Model();
        Map<String, Object> data = new HashMap<>();
        List<SickbedInfo> sickbedInfoList = sickbedInfoDao.findAllByBedDepartmentAndBedStatus(department, 0);
        List<Long> roomIdList = new ArrayList<>();
        for (SickbedInfo sickbedInfo : sickbedInfoList) {
            if (!roomIdList.contains(sickbedInfo.getRoomId())) {
                roomIdList.add(sickbedInfo.getRoomId());
            }
        }
        data.put("room_list", new Gson().toJson(roomIdList));
        model.setData(data);
        model.setCode(0);
        model.setMsg("成功");
        return model;
    }

    @RequestMapping(path = "/patient", method = RequestMethod.GET)
    public Model patientQuery(@RequestParam("department") String department) {
        Model model = new Model();
        Map<String, Object> data = new HashMap<>();
        List<PatientInfo> patientInfoList = patientInfoDao.findAllByCurrentDepartment(department);

        String value = new Gson().toJson(patientInfoList);
        JSONArray jsonArray = new JSONArray(value);
        for (int i = 0; i < jsonArray.length(); i++) {
            jsonArray.getJSONObject(i).put("birthday", new SimpleDateFormat("yyyy-MM-dd").format(patientInfoList.get(i).getBirthday()));
            jsonArray.getJSONObject(i).put("room_id", sickbedInfoDao.findByBedId(jsonArray.getJSONObject(i).getLong("current_bed_id")).getRoomId());
        }
        data.put("patient_info", jsonArray.toString());

        model.setData(data);
        model.setCode(0);
        model.setMsg("成功");
        return model;
    }

    @RequestMapping(path = "/sickbed", method = RequestMethod.GET)
    public Model sickbedQuery() {
        Model model = new Model();
        Map<String, Object> data = new HashMap<>();
        List<SickbedInfo> sickbedInfoList = sickbedInfoDao.findAll();
        data.put("sickbed_info", new Gson().toJson(sickbedInfoList));
        model.setData(data);
        model.setCode(0);
        model.setMsg("成功");
        return model;
    }

    @RequestMapping(path = "/staff", method = RequestMethod.GET)
    public Model staffQuery(@RequestParam("staff_name") String staffName) {
        Model model = new Model();
        Map<String, Object> data = new HashMap<>();
        List<StaffInfo> staffInfoList = staffInfoDao.findAllByStaffNameLike("%" + staffName + "%");
        data.put("staff_info", new Gson().toJson(staffInfoList));
        model.setData(data);
        model.setCode(0);
        model.setMsg("成功");
        return model;
    }

    @RequestMapping(path = "/order", method = RequestMethod.GET)
    public Model orderQuery(@RequestParam("medical_record_id") long medicalRecordId) {
        Model model = new Model();
        Map<String, Object> data = new HashMap<>();
        List<OrderInfo> orderInfoList = orderInfoDao.findAllByMedicalRecordIdAndStatus(medicalRecordId, false);
        data.put("order_info", new Gson().toJson(orderInfoList));
        model.setData(data);
        model.setCode(0);
        model.setMsg("成功");
        return model;
    }
}
