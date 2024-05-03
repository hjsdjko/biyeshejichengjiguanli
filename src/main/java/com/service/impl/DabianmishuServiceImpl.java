package com.service.impl;

import com.utils.StringUtil;
import org.springframework.stereotype.Service;
import java.lang.reflect.Field;
import java.util.*;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import com.utils.PageUtils;
import com.utils.Query;
import org.springframework.web.context.ContextLoader;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import com.dao.DabianmishuDao;
import com.entity.DabianmishuEntity;
import com.service.DabianmishuService;
import com.entity.view.DabianmishuView;

/**
 * 答辩秘书 服务实现类
 */
@Service("dabianmishuService")
@Transactional
public class DabianmishuServiceImpl extends ServiceImpl<DabianmishuDao, DabianmishuEntity> implements DabianmishuService {

    @Override
    public PageUtils queryPage(Map<String,Object> params) {
        if(params != null && (params.get("limit") == null || params.get("page") == null)){
            params.put("page","1");
            params.put("limit","10");
        }
        Page<DabianmishuView> page =new Query<DabianmishuView>(params).getPage();
        page.setRecords(baseMapper.selectListView(page,params));
        return new PageUtils(page);
    }


}
