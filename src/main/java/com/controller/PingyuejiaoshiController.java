
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
 * 评阅教师
 * 后端接口
 * @author
 * @email
*/
@RestController
@Controller
@RequestMapping("/pingyuejiaoshi")
public class PingyuejiaoshiController {
    private static final Logger logger = LoggerFactory.getLogger(PingyuejiaoshiController.class);

    @Autowired
    private PingyuejiaoshiService pingyuejiaoshiService;


    @Autowired
    private TokenService tokenService;
    @Autowired
    private DictionaryService dictionaryService;

    //级联表service

    @Autowired
    private YonghuService yonghuService;
    @Autowired
    private DabianmishuService dabianmishuService;
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
        PageUtils page = pingyuejiaoshiService.queryPage(params);

        //字典表数据转换
        List<PingyuejiaoshiView> list =(List<PingyuejiaoshiView>)page.getList();
        for(PingyuejiaoshiView c:list){
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
        PingyuejiaoshiEntity pingyuejiaoshi = pingyuejiaoshiService.selectById(id);
        if(pingyuejiaoshi !=null){
            //entity转view
            PingyuejiaoshiView view = new PingyuejiaoshiView();
            BeanUtils.copyProperties( pingyuejiaoshi , view );//把实体数据重构到view中

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
    public R save(@RequestBody PingyuejiaoshiEntity pingyuejiaoshi, HttpServletRequest request){
        logger.debug("save方法:,,Controller:{},,pingyuejiaoshi:{}",this.getClass().getName(),pingyuejiaoshi.toString());

        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(false)
            return R.error(511,"永远不会进入");

        Wrapper<PingyuejiaoshiEntity> queryWrapper = new EntityWrapper<PingyuejiaoshiEntity>()
            .eq("username", pingyuejiaoshi.getUsername())
            .or()
            .eq("pingyuejiaoshi_phone", pingyuejiaoshi.getPingyuejiaoshiPhone())
            ;

        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        PingyuejiaoshiEntity pingyuejiaoshiEntity = pingyuejiaoshiService.selectOne(queryWrapper);
        if(pingyuejiaoshiEntity==null){
            pingyuejiaoshi.setInsertTime(new Date());
            pingyuejiaoshi.setCreateTime(new Date());
            pingyuejiaoshi.setPassword("123456");
            pingyuejiaoshiService.insert(pingyuejiaoshi);
            return R.ok();
        }else {
            return R.error(511,"账户或者联系方式已经被使用");
        }
    }

    /**
    * 后端修改
    */
    @RequestMapping("/update")
    public R update(@RequestBody PingyuejiaoshiEntity pingyuejiaoshi, HttpServletRequest request){
        logger.debug("update方法:,,Controller:{},,pingyuejiaoshi:{}",this.getClass().getName(),pingyuejiaoshi.toString());

        String role = String.valueOf(request.getSession().getAttribute("role"));
//        if(false)
//            return R.error(511,"永远不会进入");
        //根据字段查询是否有相同数据
        Wrapper<PingyuejiaoshiEntity> queryWrapper = new EntityWrapper<PingyuejiaoshiEntity>()
            .notIn("id",pingyuejiaoshi.getId())
            .andNew()
            .eq("username", pingyuejiaoshi.getUsername())
            .or()
            .eq("pingyuejiaoshi_phone", pingyuejiaoshi.getPingyuejiaoshiPhone())
            ;

        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        PingyuejiaoshiEntity pingyuejiaoshiEntity = pingyuejiaoshiService.selectOne(queryWrapper);
        if("".equals(pingyuejiaoshi.getPingyuejiaoshiPhoto()) || "null".equals(pingyuejiaoshi.getPingyuejiaoshiPhoto())){
                pingyuejiaoshi.setPingyuejiaoshiPhoto(null);
        }
        if(pingyuejiaoshiEntity==null){
            pingyuejiaoshiService.updateById(pingyuejiaoshi);//根据id更新
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
        pingyuejiaoshiService.deleteBatchIds(Arrays.asList(ids));
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
            List<PingyuejiaoshiEntity> pingyuejiaoshiList = new ArrayList<>();//上传的东西
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
                            PingyuejiaoshiEntity pingyuejiaoshiEntity = new PingyuejiaoshiEntity();
//                            pingyuejiaoshiEntity.setUsername(data.get(0));                    //账户 要改的
//                            //pingyuejiaoshiEntity.setPassword("123456");//密码
//                            pingyuejiaoshiEntity.setPingyuejiaoshiName(data.get(0));                    //评阅教师姓名 要改的
//                            pingyuejiaoshiEntity.setPingyuejiaoshiPhoto("");//详情和图片
//                            pingyuejiaoshiEntity.setSexTypes(Integer.valueOf(data.get(0)));   //性别 要改的
//                            pingyuejiaoshiEntity.setPingyuejiaoshiPhone(data.get(0));                    //联系方式 要改的
//                            pingyuejiaoshiEntity.setPingyuejiaoshiEmail(data.get(0));                    //邮箱 要改的
//                            pingyuejiaoshiEntity.setInsertTime(date);//时间
//                            pingyuejiaoshiEntity.setCreateTime(date);//时间
                            pingyuejiaoshiList.add(pingyuejiaoshiEntity);


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
                                if(seachFields.containsKey("pingyuejiaoshiPhone")){
                                    List<String> pingyuejiaoshiPhone = seachFields.get("pingyuejiaoshiPhone");
                                    pingyuejiaoshiPhone.add(data.get(0));//要改的
                                }else{
                                    List<String> pingyuejiaoshiPhone = new ArrayList<>();
                                    pingyuejiaoshiPhone.add(data.get(0));//要改的
                                    seachFields.put("pingyuejiaoshiPhone",pingyuejiaoshiPhone);
                                }
                        }

                        //查询是否重复
                         //账户
                        List<PingyuejiaoshiEntity> pingyuejiaoshiEntities_username = pingyuejiaoshiService.selectList(new EntityWrapper<PingyuejiaoshiEntity>().in("username", seachFields.get("username")));
                        if(pingyuejiaoshiEntities_username.size() >0 ){
                            ArrayList<String> repeatFields = new ArrayList<>();
                            for(PingyuejiaoshiEntity s:pingyuejiaoshiEntities_username){
                                repeatFields.add(s.getUsername());
                            }
                            return R.error(511,"数据库的该表中的 [账户] 字段已经存在 存在数据为:"+repeatFields.toString());
                        }
                         //联系方式
                        List<PingyuejiaoshiEntity> pingyuejiaoshiEntities_pingyuejiaoshiPhone = pingyuejiaoshiService.selectList(new EntityWrapper<PingyuejiaoshiEntity>().in("pingyuejiaoshi_phone", seachFields.get("pingyuejiaoshiPhone")));
                        if(pingyuejiaoshiEntities_pingyuejiaoshiPhone.size() >0 ){
                            ArrayList<String> repeatFields = new ArrayList<>();
                            for(PingyuejiaoshiEntity s:pingyuejiaoshiEntities_pingyuejiaoshiPhone){
                                repeatFields.add(s.getPingyuejiaoshiPhone());
                            }
                            return R.error(511,"数据库的该表中的 [联系方式] 字段已经存在 存在数据为:"+repeatFields.toString());
                        }
                        pingyuejiaoshiService.insertBatch(pingyuejiaoshiList);
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
        PingyuejiaoshiEntity pingyuejiaoshi = pingyuejiaoshiService.selectOne(new EntityWrapper<PingyuejiaoshiEntity>().eq("username", username));
        if(pingyuejiaoshi==null || !pingyuejiaoshi.getPassword().equals(password))
            return R.error("账号或密码不正确");
        //  // 获取监听器中的字典表
        // ServletContext servletContext = ContextLoader.getCurrentWebApplicationContext().getServletContext();
        // Map<String, Map<Integer, String>> dictionaryMap= (Map<String, Map<Integer, String>>) servletContext.getAttribute("dictionaryMap");
        // Map<Integer, String> role_types = dictionaryMap.get("role_types");
        // role_types.get(.getRoleTypes());
        String token = tokenService.generateToken(pingyuejiaoshi.getId(),username, "pingyuejiaoshi", "评阅教师");
        R r = R.ok();
        r.put("token", token);
        r.put("role","评阅教师");
        r.put("username",pingyuejiaoshi.getPingyuejiaoshiName());
        r.put("tableName","pingyuejiaoshi");
        r.put("userId",pingyuejiaoshi.getId());
        return r;
    }

    /**
    * 注册
    */
    @IgnoreAuth
    @PostMapping(value = "/register")
    public R register(@RequestBody PingyuejiaoshiEntity pingyuejiaoshi){
//    	ValidatorUtils.validateEntity(user);
        Wrapper<PingyuejiaoshiEntity> queryWrapper = new EntityWrapper<PingyuejiaoshiEntity>()
            .eq("username", pingyuejiaoshi.getUsername())
            .or()
            .eq("pingyuejiaoshi_phone", pingyuejiaoshi.getPingyuejiaoshiPhone())
            ;
        PingyuejiaoshiEntity pingyuejiaoshiEntity = pingyuejiaoshiService.selectOne(queryWrapper);
        if(pingyuejiaoshiEntity != null)
            return R.error("账户或者联系方式已经被使用");
        pingyuejiaoshi.setInsertTime(new Date());
        pingyuejiaoshi.setCreateTime(new Date());
        pingyuejiaoshiService.insert(pingyuejiaoshi);
        return R.ok();
    }

    /**
     * 重置密码
     */
    @GetMapping(value = "/resetPassword")
    public R resetPassword(Integer  id){
        PingyuejiaoshiEntity pingyuejiaoshi = new PingyuejiaoshiEntity();
        pingyuejiaoshi.setPassword("123456");
        pingyuejiaoshi.setId(id);
        pingyuejiaoshi.setInsertTime(new Date());
        pingyuejiaoshiService.updateById(pingyuejiaoshi);
        return R.ok();
    }


    /**
     * 忘记密码
     */
    @IgnoreAuth
    @RequestMapping(value = "/resetPass")
    public R resetPass(String username, HttpServletRequest request) {
        PingyuejiaoshiEntity pingyuejiaoshi = pingyuejiaoshiService.selectOne(new EntityWrapper<PingyuejiaoshiEntity>().eq("username", username));
        if(pingyuejiaoshi!=null){
            pingyuejiaoshi.setPassword("123456");
            boolean b = pingyuejiaoshiService.updateById(pingyuejiaoshi);
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
    public R getCurrPingyuejiaoshi(HttpServletRequest request){
        Integer id = (Integer)request.getSession().getAttribute("userId");
        PingyuejiaoshiEntity pingyuejiaoshi = pingyuejiaoshiService.selectById(id);
        if(pingyuejiaoshi !=null){
            //entity转view
            PingyuejiaoshiView view = new PingyuejiaoshiView();
            BeanUtils.copyProperties( pingyuejiaoshi , view );//把实体数据重构到view中

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
