package com.firmament.service.impl;

import com.firmament.constant.MessageConstant;
import com.firmament.constant.StatusConstant;
import com.firmament.context.BaseContext;
import com.firmament.dto.SetmealDTO;
import com.firmament.dto.SetmealPageQueryDTO;
import com.firmament.entity.Dish;
import com.firmament.entity.Setmeal;
import com.firmament.entity.SetmealDish;
import com.firmament.exception.DeletionNotAllowedException;
import com.firmament.exception.SetmealEnableFailedException;
import com.firmament.mapper.DishMapper;
import com.firmament.mapper.SetmealDishMapper;
import com.firmament.mapper.SetmealMapper;
import com.firmament.result.PageResult;
import com.firmament.service.SetmealService;
import com.firmament.vo.DishItemVO;
import com.firmament.vo.SetmealVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishMapper dishMapper;

    /**
     * 分页查询菜套餐
     *
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        Page<Setmeal> page = setmealMapper.pageSelect(setmealPageQueryDTO);
        PageResult pageResult = new PageResult(page.getTotal(),page.getResult());
        return pageResult;
    }

    /**
     * 新增套餐并保存套餐内的菜品
     *
     * @param setmealDTO
     */
    @Transactional
    @Override
    public void saveWithDishes(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmeal.setStatus(StatusConstant.DISABLE);
        setmealMapper.insert(setmeal);

        Long setmealId = setmeal.getId();
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if(setmealDishes != null && !setmealDishes.isEmpty()){
            setmealDishes.forEach(setmealDish -> {
                setmealDish.setSetmealId(setmealId);
            });
            setmealDishMapper.insertBatch(setmealDishes);
        }
    }

    /**
     * 切换套餐状态
     *
     * @param id
     * @param status
     * @return
     */
    @Override
    @Transactional
    public void swtichStatus(Long id, Integer status) {
        List<SetmealDish> setmealDishes = setmealDishMapper.selectSetmealDishesBySetmealId(id);
        if(setmealDishes != null && !setmealDishes.isEmpty()){
            for (SetmealDish setmealDish : setmealDishes) {
                if(dishMapper.selectById(setmealDish.getDishId()).getStatus() == StatusConstant.DISABLE){
                    throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                }
            }
            Setmeal setmeal = Setmeal.builder()
                    .id(id)
                    .status(status)
                    .build();
            setmealMapper.update(setmeal);
        }
    }

    /**
     * 根据ID获取套餐
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public SetmealVO getById(Long id) {
        Setmeal setmeal = setmealMapper.selectById(id);
        List<SetmealDish> setmealDishes = setmealDishMapper.selectSetmealDishesBySetmealId(id);
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal,setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }

    /**
     * 更新套餐信息和关联菜品
     *
     * @param setmealDTO
     * @return
     */
    @Override
    @Transactional
    public void editWithSetmealDishes(SetmealDTO setmealDTO) {
        if(setmealDTO.getStatus() == StatusConstant.ENABLE){
            throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
        }
        Setmeal setmeal = new Setmeal();
        List<SetmealDish> setmealDishes = new ArrayList<SetmealDish>();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealDishes = setmealDTO.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealDTO.getId());
        }

        setmealMapper.update(setmeal);
        setmealDishMapper.deleteBySetmealId(setmealDTO.getId());
        setmealDishMapper.insertBatch(setmealDishes);

    }

    /**
     * 批量删除菜品
     *
     * @param setmealIds
     * @return
     */
    @Override
    @Transactional
    public void deleteWithSetmealDishIds(List<Long> setmealIds) {
        for (Long setmealId : setmealIds) {
            if(setmealMapper.selectById(setmealId).getStatus() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }
        setmealMapper.deleteBatch(setmealIds);
        setmealDishMapper.deleteBatchBySetmealId(setmealIds);
    }

    /**
     * 根据分类ID查询启售套餐
     *
     * @return
     */
    @Override
    public List<Setmeal> list(Long categoryId) {
        return setmealMapper.selectByCategoryId(categoryId);
    }

    /**
     * 查询套餐中包含的菜品简要信息
     *
     * @param id
     * @return
     */
    @Override
    public List<DishItemVO> getBySetmealId(Long id) {
        List<SetmealDish> setmealDishes = setmealDishMapper.selectSetmealDishesBySetmealId(id);
        List<DishItemVO> dishItemVOS = new ArrayList<>();
        for (SetmealDish setmealDish : setmealDishes) {
            Long dishId = setmealDish.getDishId();
            Dish dish = dishMapper.selectById(dishId);
            DishItemVO dishItemVO = new DishItemVO();
            BeanUtils.copyProperties(dish,dishItemVO);
            dishItemVOS.add(dishItemVO);
        }
        return dishItemVOS;
    }
}
