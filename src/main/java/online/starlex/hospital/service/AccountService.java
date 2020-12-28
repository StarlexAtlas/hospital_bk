package online.starlex.hospital.service;

import online.starlex.hospital.utils.ErrorCode;

public interface AccountService {
    ErrorCode login(long staffId, String password);
    int returnAuth(long staffId);
    String returnDepartment(long staffId);
}
