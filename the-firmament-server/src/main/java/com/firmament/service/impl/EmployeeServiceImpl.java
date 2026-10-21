package com.firmament.service.impl;

import com.firmament.constant.MessageConstant;
import com.firmament.constant.PasswordConstant;
import com.firmament.constant.StatusConstant;
import com.firmament.context.BaseContext;
import com.firmament.dto.EmployeeDTO;
import com.firmament.dto.EmployeeLoginDTO;
import com.firmament.dto.EmployeePageQueryDTO;
import com.firmament.dto.PasswordEditDTO;
import com.firmament.entity.Employee;
import com.firmament.exception.AccountLockedException;
import com.firmament.exception.AccountNotFoundException;
import com.firmament.exception.PasswordEditFailedException;
import com.firmament.exception.PasswordErrorException;
import com.firmament.mapper.EmployeeMapper;
import com.firmament.result.PageResult;
import com.firmament.service.EmployeeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //MD5加密密码
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //密码比对
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 新增员工
     *
     * @param employeeDTO
     */
    @Override
    public void save(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();

        // 对象属性拷贝
        BeanUtils.copyProperties(employeeDTO,employee);
        //设置其他属性
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        employee.setStatus(StatusConstant.ENABLE);

        employeeMapper.insert(employee);
    }

    /**
     * 根据姓名分页查询
     *
     * @param employeePageQueryDTO
     */
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        //开始分页查询
        PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());

        Page<Employee> page = employeeMapper.pageSelect(employeePageQueryDTO);
        PageResult pageResult = new PageResult(page.getTotal(),page.getResult());
        return pageResult;
    }

    /**
     * 启用禁用员工账号
     *
     * @param id
     * @param status
     * @return
     */
    @Override
    public void switchStatus(Long id, Integer status) {
        Employee employee = Employee.builder()
                .id(id)
                .status(status)
                .build();

        employeeMapper.update(employee);

    }

    /**
     * 根据ID查找员工
     *
     * @param id
     * @return
     */
    @Override
    public Employee getById(Long id) {
        Employee employee = employeeMapper.selectById(id);
        employee.setPassword(null);
        return employee;
    }

    /**
     * 修改员工信息
     *
     * @param employeeDTO
     * @return
     */
    @Override
    public void edit(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);
        employeeMapper.update(employee);
    }

    /**
     * 修改员工登陆密码
     *
     * @param passwordEditDTO
     * @return
     */
    @Override
    public void editPassword(PasswordEditDTO passwordEditDTO) {
        Long id = BaseContext.getCurrentId();
        String oldPassword = employeeMapper.selectById(id).getPassword();
        if(DigestUtils.md5DigestAsHex(passwordEditDTO.getOldPassword().getBytes()).equals(oldPassword)){
            Employee employee = Employee.builder()
                    .id(id)
                    .password(DigestUtils.md5DigestAsHex(passwordEditDTO.getNewPassword().getBytes()))
                    .build();
            employeeMapper.update(employee);
        }else{
            throw new PasswordEditFailedException(MessageConstant.PASSWORD_ERROR);
        }
    }

}
