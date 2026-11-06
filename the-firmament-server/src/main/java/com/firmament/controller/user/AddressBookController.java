package com.firmament.controller.user;

import com.firmament.context.BaseContext;
import com.firmament.entity.AddressBook;
import com.firmament.result.Result;
import com.firmament.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/addressBook")
@Slf4j
@Api(tags = "地址簿接口")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 新增地址
     *
     * @param address
     * @return
     */
    @PostMapping
    @ApiOperation("新增地址")
    public Result save(@RequestBody AddressBook address){
        log.info("新增地址: {}",address);
        addressBookService.save(address);
        return Result.success();
    }

    /**
     * 查询当前用户所有地址
     *
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("查询当前用户所有地址")
    public Result<List<AddressBook>> list(){
        log.info("查询用户 {} 的所有地址", BaseContext.getCurrentId());
        List<AddressBook> addressList = addressBookService.list();
        return Result.success(addressList);
    }

    /**
     * 获得当前用户的默认地址
     *
     * @return
     */
    @GetMapping("/default")
    @ApiOperation("获得当前用户的默认地址")
    public Result<AddressBook> getDefault(){
        log.info("查询用户 {} 的默认地址", BaseContext.getCurrentId());
        return Result.success(addressBookService.getDefault());
    }

    /**
     * 设为默认地址
     *
     * @param address
     * @return
     */
    @PutMapping("/default")
    @ApiOperation("设为默认地址")
    public Result setDefault(@RequestBody AddressBook address){
        log.info("设置地址： {} 为默认地址",address.getId());
        addressBookService.setDefault(address);
        return Result.success();
    }

    /**
     * 根据ID获取地址
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据ID获取地址")
    public Result<AddressBook> getById(@PathVariable Long id){
        log.info("获取ID: {} 的地址",id);
        return Result.success(addressBookService.getById(id));
    }

    /**
     * 修改地址信息
     *
     * @param address
     * @return
     */
    @PutMapping
    @ApiOperation("修改地址信息")
    public Result edit(@RequestBody AddressBook address){
        log.info("修改地址信息: {}",address);
        addressBookService.edit(address);
        return Result.success();
    }

    /**
     * 删除地址
     *
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation("删除地址")
    public Result delete(Long id){
        log.info("删除ID: {} 地址",id);
        addressBookService.delete(id);
        return Result.success();
    }
}
