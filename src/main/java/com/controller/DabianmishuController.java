
package com.controller;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import com.alibaba.fastjson.JSONObject;
import java.util.*;
import org.springframework.beans.BeanUtils;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.ContextLoader;
import javax.servlet.ServletContext;
import com.service.TokenService;
import com.utils.*;
import java.lang.reflect.InvocationTargetException;

import com.service.DictionaryService;
import org.apache.commons.lang3.StringUtils;
import com.annotation.IgnoreAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.entity.*;
import com.entity.view.*;
import com.service.*;
import com.utils.PageUtils;
import com.utils.R;
import com.alibaba.fastjson.*;

/**
 * 答辩秘书
 * 后端接口
 * @author
 * @email
*/
@RestController
@Controller
@RequestMapping("/dabianmishu")
public class DabianmishuController {
    private static final Logger logger = LoggerFactory.getLogger(DabianmishuController.class);

    @Autowired
    private DabianmishuService dabianmishuService;


    @Autowired
    private TokenService tokenService;
    @Autowired
    private DictionaryService dictionaryService;

    //级联表service

    @Autowired
    private YonghuService yonghuService;
    @Autowired
    private PingyuejiaoshiService pingyuejiaoshiService;
    @Autowired
    private ZhidaojiaoshiService zhidaojiaoshiService;


    /**
    * 后端列表
    */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, HttpServletRequest request){
        logger.debug("page方法:,,Controller:{},,params:{}",this.getClass().getName(),JSONObject.toJSONString(params));
        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(false)
            return R.error(511,"永不会进入");
        else if("用户".equals(role))
            params.put("yonghuId",request.getSession().getAttribute("userId"));
        else if("评阅教师".equals(role))
            params.put("pingyuejiaoshiId",request.getSession().getAttribute("userId"));
        else if("答辩秘书".equals(role))
            params.put("dabianmishuId",request.getSession().getAttribute("userId"));
        else if("指导教师".equals(role))
            params.put("zhidaojiaoshiId",request.getSession().getAttribute("userId"));
        if(params.get("orderBy")==null || params.get("orderBy")==""){
            params.put("orderBy","id");
        }
        PageUtils page = dabianmishuService.queryPage(params);

        //字典表数据转换
        List<DabianmishuView> list =(List<DabianmishuView>)page.getList();
        for(DabianmishuView c:list){
            //修改对应字典表字段
            dictionaryService.dictionaryConvert(c, request);
        }
        return R.ok().put("data", page);
    }

    /**
    * 后端详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id, HttpServletRequest request){
        logger.debug("info方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        DabianmishuEntity dabianmishu = dabianmishuService.selectById(id);
        if(dabianmishu !=null){
            //entity转view
            DabianmishuView view = new DabianmishuView();
            BeanUtils.copyProperties( dabianmishu , view );//把实体数据重构到view中

            //修改对应字典表字段
            dictionaryService.dictionaryConvert(view, request);
            return R.ok().put("data", view);
        }else {
            return R.error(511,"查不到数据");
        }

    }

    /**
    * 后端保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody DabianmishuEntity dabianmishu, HttpServletRequest request){
        logger.debug("save方法:,,Controller:{},,dabianmishu:{}",this.getClass().getName(),dabianmishu.toString());

        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(false)
            return R.error(511,"永远不会进入");

        Wrapper<DabianmishuEntity> queryWrapper = new EntityWrapper<DabianmishuEntity>()
            .eq("username", dabianmishu.getUsername())
            .or()
            .eq("dabianmishu_phone", dabianmishu.getDabianmishuPhone())
            ;

        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        DabianmishuEntity dabianmishuEntity = dabianmishuService.selectOne(queryWrapper);
        if(dabianmishuEntity==null){
            dabianmishu.setInsertTime(new Date());
            dabianmishu.setCreateTime(new Date());
            dabianmishu.setPassword("123456");
            dabianmishuService.insert(dabianmishu);
            return R.ok();
        }else {
            return R.error(511,"账户或者联系方式已经被使用");
        }
    }

    /**
    * 后端修改
    */
    @RequestMapping("/update")
    public R update(@RequestBody DabianmishuEntity dabianmishu, HttpServletRequest request){
        logger.debug("update方法:,,Controller:{},,dabianmishu:{}",this.getClass().getName(),dabianmishu.toString());

        String role = String.valueOf(request.getSession().getAttribute("role"));
//        if(false)
//            return R.error(511,"永远不会进入");
        //根据字段查询是否有相同数据
        Wrapper<DabianmishuEntity> queryWrapper = new EntityWrapper<DabianmishuEntity>()
            .notIn("id",dabianmishu.getId())
            .andNew()
            .eq("username", dabianmishu.getUsername())
            .or()
            .eq("dabianmishu_phone", dabianmishu.getDabianmishuPhone())
            ;

        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        DabianmishuEntity dabianmishuEntity = dabianmishuService.selectOne(queryWrapper);
        if("".equals(dabianmishu.getDabianmishuPhoto()) || "null".equals(dabianmishu.getDabianmishuPhoto())){
                dabianmishu.setDabianmishuPhoto(null);
        }
        if(dabianmishuEntity==null){
            dabianmishuService.updateById(dabianmishu);//根据id更新
            return R.ok();
        }else {
            return R.error(511,"账户或者联系方式已经被使用");
        }
    }



    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids){
        logger.debug("delete:,,Controller:{},,ids:{}",this.getClass().getName(),ids.toString());
        dabianmishuService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }


    /**
     * 批量上传
     */
    @RequestMapping("/batchInsert")
    public R save( String fileName, HttpServletRequest request){
        logger.debug("batchInsert方法:,,Controller:{},,fileName:{}",this.getClass().getName(),fileName);
        Integer yonghuId = Integer.valueOf(String.valueOf(request.getSession().getAttribute("userId")));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            List<DabianmishuEntity> dabianmishuList = new ArrayList<>();//上传的东西
            Map<String, List<String>> seachFields= new HashMap<>();//要查询的字段
            Date date = new Date();
            int lastIndexOf = fileName.lastIndexOf(".");
            if(lastIndexOf == -1){
                return R.error(511,"该文件没有后缀");
            }else{
                String suffix = fileName.substring(lastIndexOf);
                if(!".xls".equals(suffix)){
                    return R.error(511,"只支持后缀为xls的excel文件");
                }else{
                    URL resource = this.getClass().getClassLoader().getResource("static/upload/" + fileName);//获取文件路径
                    File file = new File(resource.getFile());
                    if(!file.exists()){
                        return R.error(511,"找不到上传文件，请联系管理员");
                    }else{
                        List<List<String>> dataList = PoiUtil.poiImport(file.getPath());//读取xls文件
                        dataList.remove(0);//删除第一行，因为第一行是提示
                        for(List<String> data:dataList){
                            //循环
                            DabianmishuEntity dabianmishuEntity = new DabianmishuEntity();
//                            dabianmishuEntity.setUsername(data.get(0));                    //账户 要改的
//                            //dabianmishuEntity.setPassword("123456");//密码
//                            dabianmishuEntity.setDabianmishuName(data.get(0));                    //答辩秘书姓名 要改的
//                            dabianmishuEntity.setDabianmishuPhoto("");//详情和图片
//                            dabianmishuEntity.setSexTypes(Integer.valueOf(data.get(0)));   //性别 要改的
//                            dabianmishuEntity.setDabianmishuPhone(data.get(0));                    //联系方式 要改的
//                            dabianmishuEntity.setDabianmishuEmail(data.get(0));                    //邮箱 要改的
//                            dabianmishuEntity.setInsertTime(date);//时间
//                            dabianmishuEntity.setCreateTime(date);//时间
                            dabianmishuList.add(dabianmishuEntity);


                            //把要查询是否重复的字段放入map中
                                //账户
                                if(seachFields.containsKey("username")){
                                    List<String> username = seachFields.get("username");
                                    username.add(data.get(0));//要改的
                                }else{
                                    List<String> username = new ArrayList<>();
                                    username.add(data.get(0));//要改的
                                    seachFields.put("username",username);
                                }
                                //联系方式
                                if(seachFields.containsKey("dabianmishuPhone")){
                                    List<String> dabianmishuPhone = seachFields.get("dabianmishuPhone");
                                    dabianmishuPhone.add(data.get(0));//要改的
                                }else{
                                    List<String> dabianmishuPhone = new ArrayList<>();
                                    dabianmishuPhone.add(data.get(0));//要改的
                                    seachFields.put("dabianmishuPhone",dabianmishuPhone);
                                }
                        }

                        //查询是否重复
                         //账户
                        List<DabianmishuEntity> dabianmishuEntities_username = dabianmishuService.selectList(new EntityWrapper<DabianmishuEntity>().in("username", seachFields.get("username")));
                        if(dabianmishuEntities_username.size() >0 ){
                            ArrayList<String> repeatFields = new ArrayList<>();
                            for(DabianmishuEntity s:dabianmishuEntities_username){
                                repeatFields.add(s.getUsername());
                            }
                            return R.error(511,"数据库的该表中的 [账户] 字段已经存在 存在数据为:"+repeatFields.toString());
                        }
                         //联系方式
                        List<DabianmishuEntity> dabianmishuEntities_dabianmishuPhone = dabianmishuService.selectList(new EntityWrapper<DabianmishuEntity>().in("dabianmishu_phone", seachFields.get("dabianmishuPhone")));
                        if(dabianmishuEntities_dabianmishuPhone.size() >0 ){
                            ArrayList<String> repeatFields = new ArrayList<>();
                            for(DabianmishuEntity s:dabianmishuEntities_dabianmishuPhone){
                                repeatFields.add(s.getDabianmishuPhone());
                            }
                            return R.error(511,"数据库的该表中的 [联系方式] 字段已经存在 存在数据为:"+repeatFields.toString());
                        }
                        dabianmishuService.insertBatch(dabianmishuList);
                        return R.ok();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.error(511,"批量插入数据异常，请联系管理员");
        }
    }


    /**
    * 登录
    */
    @IgnoreAuth
    @RequestMapping(value = "/login")
    public R login(String username, String password, String captcha, HttpServletRequest request) {
        DabianmishuEntity dabianmishu = dabianmishuService.selectOne(new EntityWrapper<DabianmishuEntity>().eq("username", username));
        if(dabianmishu==null || !dabianmishu.getPassword().equals(password))
            return R.error("账号或密码不正确");
        //  // 获取监听器中的字典表
        // ServletContext servletContext = ContextLoader.getCurrentWebApplicationContext().getServletContext();
        // Map<String, Map<Integer, String>> dictionaryMap= (Map<String, Map<Integer, String>>) servletContext.getAttribute("dictionaryMap");
        // Map<Integer, String> role_types = dictionaryMap.get("role_types");
        // role_types.get(.getRoleTypes());
        String token = tokenService.generateToken(dabianmishu.getId(),username, "dabianmishu", "答辩秘书");
        R r = R.ok();
        r.put("token", token);
        r.put("role","答辩秘书");
        r.put("username",dabianmishu.getDabianmishuName());
        r.put("tableName","dabianmishu");
        r.put("userId",dabianmishu.getId());
        return r;
    }

    /**
    * 注册
    */
    @IgnoreAuth
    @PostMapping(value = "/register")
    public R register(@RequestBody DabianmishuEntity dabianmishu){
//    	ValidatorUtils.validateEntity(user);
        Wrapper<DabianmishuEntity> queryWrapper = new EntityWrapper<DabianmishuEntity>()
            .eq("username", dabianmishu.getUsername())
            .or()
            .eq("dabianmishu_phone", dabianmishu.getDabianmishuPhone())
            ;
        DabianmishuEntity dabianmishuEntity = dabianmishuService.selectOne(queryWrapper);
        if(dabianmishuEntity != null)
            return R.error("账户或者联系方式已经被使用");
        dabianmishu.setInsertTime(new Date());
        dabianmishu.setCreateTime(new Date());
        dabianmishuService.insert(dabianmishu);
        return R.ok();
    }

    /**
     * 重置密码
     */
    @GetMapping(value = "/resetPassword")
    public R resetPassword(Integer  id){
        DabianmishuEntity dabianmishu = new DabianmishuEntity();
        dabianmishu.setPassword("123456");
        dabianmishu.setId(id);
        dabianmishu.setInsertTime(new Date());
        dabianmishuService.updateById(dabianmishu);
        return R.ok();
    }


    /**
     * 忘记密码
     */
    @IgnoreAuth
    @RequestMapping(value = "/resetPass")
    public R resetPass(String username, HttpServletRequest request) {
        DabianmishuEntity dabianmishu = dabianmishuService.selectOne(new EntityWrapper<DabianmishuEntity>().eq("username", username));
        if(dabianmishu!=null){
            dabianmishu.setPassword("123456");
            boolean b = dabianmishuService.updateById(dabianmishu);
            if(!b){
               return R.error();
            }
        }else{
           return R.error("账号不存在");
        }
        return R.ok();
    }


    /**
    * 获取用户的session用户信息
    */
    @RequestMapping("/session")
    public R getCurrDabianmishu(HttpServletRequest request){
        Integer id = (Integer)request.getSession().getAttribute("userId");
        DabianmishuEntity dabianmishu = dabianmishuService.selectById(id);
        if(dabianmishu !=null){
            //entity转view
            DabianmishuView view = new DabianmishuView();
            BeanUtils.copyProperties( dabianmishu , view );//把实体数据重构到view中

            //修改对应字典表字段
            dictionaryService.dictionaryConvert(view, request);
            return R.ok().put("data", view);
        }else {
            return R.error(511,"查不到数据");
        }
    }


    /**
    * 退出
    */
    @GetMapping(value = "logout")
    public R logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return R.ok("退出成功");
    }





}
