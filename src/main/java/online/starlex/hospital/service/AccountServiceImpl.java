package online.starlex.hospital.service;

import online.starlex.hospital.dao.StaffInfoDao;
import online.starlex.hospital.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    StaffInfoDao staffInfoDao;

    @Override
    public ErrorCode login(long staffId, String password) {
        if(staffInfoDao.existsByStaffIdAndPassword(staffId, password)){
            return ErrorCode.OK;
        } else {
            return ErrorCode.STAFF_LOGIN_ERROR;
        }
    }

    @Override
    public int returnAuth(long staffId) {
        return staffInfoDao.findByStaffId(staffId).getStaffAuth();
    }

    @Override
    public String returnDepartment(long staffId) {
        return staffInfoDao.findByStaffId(staffId).getStaffDepartment();
    }


}
