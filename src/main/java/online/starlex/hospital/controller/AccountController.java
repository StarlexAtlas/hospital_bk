package online.starlex.hospital.controller;

import online.starlex.hospital.entity.Model;
import online.starlex.hospital.service.AccountService;
import online.starlex.hospital.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
public class AccountController {

    @Autowired
    AccountService accountService;

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public Model login(@RequestParam("staff_id") long staffId,
                       @RequestParam("password") String password) {
        Model model = new Model();
        Map<String, Object> data = new HashMap<>();
        ErrorCode errorCode = accountService.login(staffId, password);

        if(errorCode.getCode() == 0) {
            int auth = accountService.returnAuth(staffId);
            String department = accountService.returnDepartment(staffId);
            data.put("auth", auth);
            data.put("department", department);
        }

        model.setCode(errorCode.getCode());
        model.setMsg(errorCode.getMsg());
        model.setData(data);
        return model;
    }
}
