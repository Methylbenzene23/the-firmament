package com.firmament.service;

import com.firmament.dto.EmployeeDTO;
import com.firmament.dto.EmployeeLoginDTO;
import com.firmament.dto.EmployeePageQueryDTO;
import com.firmament.entity.Employee;
import com.firmament.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 新增员工
     * @param employeeDTO
     * @return
     */
    void save(EmployeeDTO employeeDTO);

    /**
     * 根据姓名分页查询
     * @param employeePageQueryDTO
     * @return
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 启用禁用员工账号
     *
     * @param id
     * @param status
     * @return
     */
    void switchStatus(Long id, Integer status);
}
