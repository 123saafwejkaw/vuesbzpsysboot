package com.it.controller;

import com.it.dao.MemberDAO;
import com.it.dao.VitaeDAO;
import com.it.entity.Member;
import com.it.entity.Vitae;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@Controller
public class VitaeController extends BaseController {

	@Resource
    VitaeDAO vitaeDAO;
    @Resource
    MemberDAO memberDAO;




    //简历添加
    @ResponseBody
    @RequestMapping("vitaeAdd")
    public void vitaeAdd(Vitae vitae, HttpServletRequest request) {
        Member sessionmember = (Member) request.getSession().getAttribute("sessionmember");
        vitae.setMemberid(sessionmember.getId());
        vitaeDAO.add(vitae);
    }


    //编辑页面
    @ResponseBody
    @RequestMapping("admin/vitaeShow")
    public HashMap<String,Object> vitaeShow(int id,HttpServletRequest request) {
        HashMap<String,Object> res = new HashMap<String,Object>();
        Vitae vitae = vitaeDAO.findById(id);
        res.put("vitae", vitae);
        return res;
    }


    //查阅简历信息
    @ResponseBody
    @RequestMapping("vitaeShow")
    public HashMap<String,Object> vitaeShow1(int memberid,HttpServletRequest request) {
        HashMap<String,Object> res = new HashMap<String,Object>();
        HashMap map = new HashMap();
        map.put("memberid", memberid);
        List<Vitae> vitaes = vitaeDAO.selectAll(map);
        if(vitaes.size()==0){
            res.put("data",400 );
        }else{
            Member member = memberDAO.findById(memberid);
            res.put("data",200 );
            res.put("member",member);
            res.put("vitae", vitaes.get(0));
        }
        return res;
    }


    //简历编辑
    @ResponseBody
    @RequestMapping("vitaeEdit")
    public void vitaeEdit(Vitae vitae,HttpServletRequest request) {
        vitaeDAO.update(vitae);
    }

    //简历删除
    @ResponseBody
    @RequestMapping("admin/vitaeDel")
    public void vitaeDel(int id,HttpServletRequest request) {
	    vitaeDAO.delete(id);
    }


    /**
     * 判断是否有简历信息
     * @param memberid
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("issavevitae")
    public HashMap<String,Object> issavevitae(int memberid,HttpServletRequest request) {
        HashMap<String,Object> res = new HashMap<String,Object>();
        HashMap map = new HashMap();
        map.put("memberid", memberid);
        List<Vitae> vitaes = vitaeDAO.selectAll(map);
        if(vitaes.size()==0){
            res.put("data",400 );
        }else{
            res.put("data",200 );
        }
        return res;
    }


}
