package cn.wangyanfsd.api.web.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.wangyanfsd.api.util.JSONUtils;
import cn.wangyanfsd.api.util.StrUtil;
import cn.wangyanfsd.api.util.TransactionUtil;
import cn.wangyanfsd.api.bean.FangWen;
import cn.wangyanfsd.api.bean.GxzdZfgs;
import cn.wangyanfsd.api.bean.LiuLiang;
import cn.wangyanfsd.api.service.FangWenService;
import cn.wangyanfsd.api.service.GxzdZfgsService;
import cn.wangyanfsd.api.service.LiuLiangService;
import cn.wangyanfsd.api.service.impl.FangWenServiceImpl;
import cn.wangyanfsd.api.service.impl.GxzdZfgsServiceImpl;
import cn.wangyanfsd.api.service.impl.LiuLiangServiceImpl;

public class GxzdZfgsAction extends HttpServlet {

    private GxzdZfgsService gxzdZfgsService = (GxzdZfgsService)new TransactionUtil().newProxy(new GxzdZfgsServiceImpl());
    private LiuLiangService liuLiangService = (LiuLiangService)new TransactionUtil().newProxy(new LiuLiangServiceImpl());
    Map <Object,Object> map;
    private HttpServletRequest request;
    private String message;


    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        this.request = request;
        request.setCharacterEncoding("UTF-8");
        String choice = request.getParameter("choice");
        Result r = null;

        if ("save".equals(choice)) {
            r = save();
        } else if ("remove".equals(choice)) {
            r = remove();
        } else if ("update".equals(choice)) {
            r = update();
        } else if ("list".equals(choice)) {
            r = list();
        } else if ("find".equals(choice)) {
            r = find();
        }

        PrintWriter out = response.getWriter();
        out.println(message);
        out.flush();

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
    }

    private Result save() {

        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String date = request.getParameter("date");
        String person = request.getParameter("person");
        map = new HashMap<Object,Object>();

        if(!"".equals(title)&&title!=null&&!"".equals(content)&&content!=null&&!"".equals(date)&&date!=null&&!"".equals(person)&&person!=null){
            GxzdZfgs gxzdZfgs = new GxzdZfgs();
            gxzdZfgs.setTitle(title);
            gxzdZfgs.setContent(content);
            gxzdZfgs.setDate(date);
            gxzdZfgs.setPerson(person);
            gxzdZfgsService.save(gxzdZfgs);
            map.put("gxzdZfgs", gxzdZfgs);
            map.put("message", "添加成功");
        }else{
        map.put("message", "输入参错不正确，或内容不存在");
        }

        message = JSONUtils.toJSONString(map);
        request.setAttribute("message",map);
        return new Result("/WEB-INF/index.html");
    }

    private Result remove() {

        String id = request.getParameter("id");
        map = new HashMap<Object,Object>();

        if(!"".equals(id)&&id!=null&&StrUtil.isNumeric(id)){
            int i = Integer.parseInt(id);
            map.put("gxzdZfgs", gxzdZfgsService.find(i));
            gxzdZfgsService.remove(i);
            map.put("message", "删除成功");
        }else{
        map.put("message", "输入参错不正确，或内容不存在");
        }

        message = JSONUtils.toJSONString(map);
        request.setAttribute("message",map);
        return new Result("/WEB-INF/index.html");
    }

    private Result update() {

        String id = request.getParameter("id");
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String date = request.getParameter("date");
        String person = request.getParameter("person");
        map = new HashMap<Object,Object>();

        if(!"".equals(id)&&id!=null&&StrUtil.isNumeric(id)&&!"".equals(title)&&title!=null&&!"".equals(content)&&content!=null&&!"".equals(date)&&date!=null&&!"".equals(person)&&person!=null){
            GxzdZfgs gxzdZfgs = new GxzdZfgs();
            gxzdZfgs.setId(Integer.parseInt(id));
            gxzdZfgs.setTitle(title);
            gxzdZfgs.setContent(content);
            gxzdZfgs.setDate(date);
            gxzdZfgs.setPerson(person);
            gxzdZfgsService.update(gxzdZfgs);
            map.put("gxzdZfgs", gxzdZfgs);
            map.put("message", "修改成功");
        }else{
        map.put("message", "输入参错不正确，或内容不存在");
        }

        message = JSONUtils.toJSONString(map);
        request.setAttribute("message",map);
        return new Result("/WEB-INF/index.html");
    }

    private Result list() {
    	
    	LiuLiang ll = liuLiangService.find("content = 'GxzdZfgs' ",null);
    	ll.setNumber(ll.getNumber() +1);
    	liuLiangService.update(ll);
    	
    	FangWenService fangWenService = (FangWenService)new TransactionUtil().newProxy(new FangWenServiceImpl());
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        FangWen fw = new FangWen();
        String date = sf.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
        fw.setDate(date);
        fangWenService.save(fw);

        String where = request.getParameter("where");
        List list = new ArrayList();

        if(!"".equals(where)&&where!=null){
            message = JSONUtils.toJSONString(gxzdZfgsService.list(" title LIKE '%"+where+"%' OR content LIKE '%"+where+"%'  ",null));
        }else{
            message = JSONUtils.toJSONString(gxzdZfgsService.list());
        }
        request.setAttribute("message",message);
        return new Result("/WEB-INF/index.html");
    }

    private Result find() {

        String id = request.getParameter("id");
        map = new HashMap<Object,Object>();

        if(!"".equals(id)&&id!=null&&StrUtil.isNumeric(id)){
            int i = Integer.parseInt(id);
            message = JSONUtils.toJSONString(gxzdZfgsService.find(i));
        }

        request.setAttribute("message",message);
        return new Result("/WEB-INF/index.html");
    }

}