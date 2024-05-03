package com.dao;

import com.entity.DabianmishuEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.entity.view.DabianmishuView;

/**
 * 答辩秘书 Dao 接口
 *
 * @author 
 */
public interface DabianmishuDao extends BaseMapper<DabianmishuEntity> {

   List<DabianmishuView> selectListView(Pagination page,@Param("params")Map<String,Object> params);

}
