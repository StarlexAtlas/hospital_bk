package online.starlex.hospital.controller;

import online.starlex.hospital.entity.Model;

import online.starlex.hospital.entity.PatientInfo;
import online.starlex.hospital.service.PatientService;
import online.starlex.hospital.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(path = "/admin")
public class AdminController {

    @Autowired
    PatientService patientService;

    @RequestMapping(path = "/admission/new", method = RequestMethod.POST)
    public Model admissionNew(@RequestParam("id_number") String idNumber,
                              @RequestParam("medical_record_id") long medicalRecordId,
                              @RequestParam("name") String name,
                              @RequestParam("sex") String sex,
                              @RequestParam("birthday") String birthday) {
        Model model = new Model();

        PatientInfo patientInfo = new PatientInfo();
        patientInfo.setIdNumber(idNumber);
        patientInfo.setMedicalRecordId(medicalRecordId);
        patientInfo.setName(name);
        patientInfo.setSex(sex);
        patientInfo.setCurrentBedId(0);
        patientInfo.setBalance(0);
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = simpleDateFormat.parse(birthday);
            patientInfo.setBirthday(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ErrorCode errorCode = patientService.register(patientInfo);
        model.setCode(errorCode.getCode());
        model.setMsg(errorCode.getMsg());
        return model;
    }

    @RequestMapping(path = "/admission", method = RequestMethod.POST)
    public Model admission(@RequestParam("medical_record_id") long medicalRecordId,
                           @RequestParam("department") String department,
                           @RequestParam("pre_balance") int preBalance,
                           @RequestParam("room_id") long roomId) {
        Model model = new Model();
        ErrorCode errorCode = patientService.admission(medicalRecordId, department, roomId, preBalance);
        model.setCode(errorCode.getCode());
        model.setMsg(errorCode.getMsg());
        return model;
    }

    @RequestMapping(path = "/transfer", method = RequestMethod.POST)
    public Model transfer(@RequestParam("medical_record_id") long medicalRecordId,
                          @RequestParam("department") String department,
                          @RequestParam("target_room_id") long targetRoomId) {
        Model model = new Model();
        ErrorCode errorCode = patientService.transfer(medicalRecordId, department, targetRoomId);
        model.setCode(errorCode.getCode());
        model.setMsg(errorCode.getMsg());
        return model;
    }

    @RequestMapping(path = "/discharge", method = RequestMethod.POST)
    public Model discharge(@RequestParam("medical_record_id") long medicalRecordId) {
        Model model = new Model();
        Map<String, Object> data = new HashMap<>();
        ErrorCode errorCode = patientService.disCharge(medicalRecordId);
        int extra_amount = patientService.clearCurrentBalance(medicalRecordId);
        model.setCode(errorCode.getCode());
        model.setMsg(errorCode.getMsg());
        data.put("extra_amount", extra_amount);
        model.setData(data);
        return model;
    }
}
