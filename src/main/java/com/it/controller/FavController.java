package com.it.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.it.dao.*;
import com.it.entity.*;
import com.it.util.Info;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@Controller
public class FavController extends BaseController {

	@Resource
    FavDAO favDAO;

    @Resource
    JobDAO jobDAO;
    @Resource
    CategoryDAO categoryDAO;
    @Resource
    AreaDAO areaDAO;
    @Resource
    MemberDAO memberDAO;


	//收藏夹
	@ResponseBody
	@RequestMapping("scList")
	public HashMap<String,Object> scList(@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum,@RequestParam(defaultValue = "1",value = "pageSize") Integer pageSize,HttpServletRequest request){
        Member sessionmember = (Member) request.getSession().getAttribute("sessionmember");
	    String key = request.getParameter("key");
        HashMap<String,Object> res = new HashMap<String,Object>();
		HashMap map = new HashMap();
		map.put("memberid", sessionmember.getId());
        map.put("type", "1");
        List<Fav> favlist = favDAO.selectAll(map);
		PageHelper.startPage(pageNum, pageSize);
		List<Fav> list = favDAO.selectAll(map);
		for(Fav fav:list){
            Job job = jobDAO.findById(fav.getJobid());
            Area area = areaDAO.findById(job.getAreaid());
            job.setArea(area);
            Category category = categoryDAO.findById(job.getCategoryid());
            job.setCategory(category);
            Member company = memberDAO.findById(job.getCompanyid());
            job.setCompany(company);
            fav.setJob(job);
        }
		PageInfo<Fav> pageInfo = new PageInfo<Fav>(list);
		res.put("pageInfo", pageInfo);
		res.put("list", favlist);
		return res;
	}


    //收藏
    @ResponseBody
    @RequestMapping("favAdd")
    public HashMap<String,Object> favAdd(Fav fav,HttpServletRequest request) {
        Member sessionmember = (Member) request.getSession().getAttribute("sessionmember");
        HashMap<String,Object> res = new HashMap<String,Object>();
        if(sessionmember != null){
            HashMap map = new HashMap();
            map.put("memberid", sessionmember.getId());
            map.put("jobid",fav.getJobid());
            map.put("type", "1");
            List<Fav> favlist = favDAO.selectAll(map);
            if(favlist.size() == 0){
                fav.setMemberid(sessionmember.getId());
                fav.setType("1");
                favDAO.add(fav);
                res.put("data","200");
            }else{
                res.put("data","300");//已收藏过
            }
        }else{
            res.put("data","-1");
        }
        return res;
    }

    //移除收藏
    @ResponseBody
    @RequestMapping("favDel")
    public void favDel(int id,HttpServletRequest request) {
	    favDAO.delete(id);
    }

    //点赞
    @ResponseBody
    @RequestMapping("dzAdd")
    public HashMap<String,Object> dzAdd(Fav fav,HttpServletRequest request) {
        Member sessionmember = (Member) request.getSession().getAttribute("sessionmember");
        HashMap<String,Object> res = new HashMap<String,Object>();
        if(sessionmember != null){
            HashMap map = new HashMap();
            map.put("memberid", sessionmember.getId());
            map.put("jobid",fav.getJobid());
            map.put("type", "2");
            List<Fav> dzlist = favDAO.selectAll(map);
            if(dzlist.size() == 0){
                fav.setMemberid(sessionmember.getId());
                fav.setType("2");
                favDAO.add(fav);
                res.put("data","200");
            }else{
                res.put("data","300");//已点赞过
            }
        }else{
            res.put("data","-1");
        }
        return res;
    }

    //记录浏览记录
    @ResponseBody
    @RequestMapping("lookrecordAdd")
    public HashMap<String,Object> lookrecordAdd(Fav fav,HttpServletRequest request) {
        Member sessionmember = (Member) request.getSession().getAttribute("sessionmember");
        HashMap<String,Object> res = new HashMap<String,Object>();
        if(sessionmember != null){
            fav.setMemberid(sessionmember.getId());
            fav.setType("3");
            favDAO.add(fav);
            res.put("data","200");
        }else{
            res.put("data","-1");
        }
        return res;
    }
}
