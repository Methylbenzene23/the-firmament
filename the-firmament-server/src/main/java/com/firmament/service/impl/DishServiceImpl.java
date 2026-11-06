package com.firmament.service.impl;

import com.firmament.constant.MessageConstant;
import com.firmament.constant.StatusConstant;
import com.firmament.dto.DishDTO;
import com.firmament.dto.DishPageQueryDTO;
import com.firmament.entity.Dish;
import com.firmament.entity.DishFlavor;
import com.firmament.exception.DeletionNotAllowedException;
import com.firmament.mapper.DishMapper;
import com.firmament.mapper.DishFlavorMapper;
import com.firmament.mapper.SetmealDishMapper;
import com.firmament.result.PageResult;
import com.firmament.service.DishService;
import com.firmament.vo.DishVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class DishServiceImpl implements DishService {

    private final static String redisDishKeyString = "dish:category:";

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;


    /**
     * 新增菜品与口味数据
     *
     * @param dishDTO
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {

        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);

        dishMapper.insert(dish);
        Long dishId = dish.getId();

        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null && !flavors.isEmpty()){
            flavors.forEach(flavor -> {
                flavor.setDishId(dishId);
            });
        }
        dishFlavorMapper.insertBatch(flavors);

        this.cleanRedisCache(redisDishKeyString+dishDTO.getCategoryId());
    }

    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.pageSelect(dishPageQueryDTO);
        PageResult pageResult = new PageResult(page.getTotal(),page.getResult());
        return pageResult;
    }

    /**
     * 批量删除菜品
     *
     * @param ids
     */
    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        for (Long id : ids) {
            Dish dish = dishMapper.selectById(id);
            if(dish.getStatus() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        List<Long> setmealIds = setmealDishMapper.selectSetmealIdsByDishIds(ids);
        if(setmealIds != null && !setmealIds.isEmpty()){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        dishMapper.deleteByIds(ids);
        dishFlavorMapper.deleteByDishIds(ids);

        this.cleanRedisCache(redisDishKeyString+"*");
    }

    /**
     * 根据ID查询菜品和口味
     *
     * @param id
     * @return
     */
    @Override
    public DishVO getByIdWithFlavor(Long id) {
        Dish dish = dishMapper.selectById(id);
        List<DishFlavor> flavors = dishFlavorMapper.selectByDishId(id);
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(flavors);
        return dishVO;
    }

    /**
     * 修改菜品信息或口味
     *
     * @param dishDTO
     * @return
     */
    @Override
    @Transactional
    public void editWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.update(dish);

        dishFlavorMapper.selectByDishId(dishDTO.getId());
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null && !flavors.isEmpty()){
            flavors.forEach(flavor -> {
                flavor.setDishId(dishDTO.getId());
            });
            dishFlavorMapper.insertBatch(flavors);
        }

        this.cleanRedisCache(redisDishKeyString+"*");
    }

    @Override
    public void switchStatus(Long id, Integer status) {
        Dish dish = Dish.builder()
                .id(id)
                .status(status)
                .build();
        dishMapper.update(dish);

        this.cleanRedisCache(redisDishKeyString+"*");
    }

    /**
     * 查询指定分类ID内的菜品
     *
     * @param categoryId
     * @return
     */
    @Override
    public List<Dish> getByCategoryId(Long categoryId) {
        return dishMapper.selectByCategoryId(categoryId);
    }

    /**
     * 根据分类ID查询菜品与口味
     *
     * @param categoryId
     * @return
     */
    @Override
    public List<DishVO> getEnableDishWithFlavorByCategoryId(Long categoryId) {

        // 构造redis的key,在redis里查询
        String redisKey = "dish:category:"+categoryId;
        List<DishVO> redisDishVOList = (List<DishVO>) redisTemplate.opsForValue().get(redisKey);
        if(redisDishVOList != null){
            return redisDishVOList;
        }
        //redis不存在时，从数据库查询，写入redis
        List<Dish> dishes = dishMapper.selectEnabledByCategoryId(categoryId);
        List<DishVO> DatabasedishVOList = new ArrayList<>();
        for (Dish dish : dishes) {
            DishVO dishVO = new DishVO();
            List<DishFlavor> dishFlavors = dishFlavorMapper.selectByDishId(dish.getId());
            BeanUtils.copyProperties(dish,dishVO);
            dishVO.setPrice(dishVO.getPrice().movePointRight(2));
            dishVO.setFlavors(dishFlavors);
            DatabasedishVOList.add(dishVO);
        }
        redisTemplate.opsForValue().set(redisKey,DatabasedishVOList);
        return DatabasedishVOList;
    }

    private void cleanRedisCache(String pattern){
        redisTemplate.delete(redisTemplate.keys(pattern));
    }
}
