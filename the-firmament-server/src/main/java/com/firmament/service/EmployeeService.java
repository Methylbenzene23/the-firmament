package com.firmament.service;

import com.firmament.dto.EmployeeLoginDTO;
import com.firmament.entity.Employee;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

}
