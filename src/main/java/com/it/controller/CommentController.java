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
public class CommentController extends BaseController {

	@Resource
    CommentDAO commentDAO;
    @Resource
    JobDAO jobDAO;
    @Resource
    MemberDAO memberDAO;

	//评论列表
    @ResponseBody
    @RequestMapping("admin/commentList")
    public HashMap<String,Object> commentList(@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum,@RequestParam(defaultValue = "1",value = "pageSize") Integer pageSize,HttpServletRequest request){
        String key = request.getParameter("key");
        String key2 = request.getParameter("key2");
        HashMap<String,Object> res = new HashMap<String,Object>();
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("key", key);
        map.put("key2", key2);
        List<Comment> commentlist = commentDAO.selectAll(map);
        PageHelper.startPage(pageNum, pageSize);
        List<Comment> list = commentDAO.selectAll(map);
        for(Comment comment:list){
            Member company = memberDAO.findById(comment.getCompanyid());
            comment.setCompany(company);
            Member member = memberDAO.findById(comment.getMemberid());
            comment.setMember(member);
            Job job = jobDAO.findById(comment.getJobid());
            comment.setJob(job);

        }
        PageInfo<Comment> pageInfo = new PageInfo<Comment>(list);
        res.put("pageInfo", pageInfo);
        res.put("list", commentlist);
        return res;
    }



    //评论列表
    @ResponseBody
    @RequestMapping("commentLb")
    public HashMap<String,Object> commentLb(@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum,@RequestParam(defaultValue = "1",value = "pageSize") Integer pageSize,HttpServletRequest request){
        String jobid = request.getParameter("jobid");
        String memberid = request.getParameter("memberid");
        HashMap<String,Object> res = new HashMap<String,Object>();
        HashMap map = new HashMap();
        map.put("jobid", jobid);
        map.put("companyid", memberid);
        List<Comment> commentlist = commentDAO.selectAll(map);
        PageHelper.startPage(pageNum, pageSize);
        List<Comment> list = commentDAO.selectAll(map);
        for(Comment comment:list){
            Member company = memberDAO.findById(comment.getCompanyid());
            comment.setCompany(company);
            Member member = memberDAO.findById(comment.getMemberid());
            comment.setMember(member);
            Job job = jobDAO.findById(comment.getJobid());

            comment.setJob(job);

        }
        PageInfo<Comment> pageInfo = new PageInfo<Comment>(list);
        res.put("pageInfo", pageInfo);
        res.put("list", commentlist);
        return res;
    }

    //评论添加
    @ResponseBody
    @RequestMapping("commentAdd")
    public HashMap<String,Object> commentAdd(Comment comment,HttpServletRequest request) {
        HashMap<String,Object> res = new HashMap<String,Object>();
        Member sessionmember = (Member) request.getSession().getAttribute("sessionmember");
        if(sessionmember!=null){
                Job job = jobDAO.findById(comment.getJobid());
                comment.setCompanyid(job.getCompanyid());
                comment.setMemberid(sessionmember.getId());
                comment.setSavetime(Info.getDateStr());
                commentDAO.add(comment);
                res.put("data", 200);
        }else{
            res.put("data", 400);
        }
        return res;
    }

//
//    //编辑页面
//    @ResponseBody
//    @RequestMapping("admin/commentShow")
//    public HashMap<String,Object> commentShow(int id,HttpServletRequest request) {
//        HashMap<String,Object> res = new HashMap<String,Object>();
//        Comment comment = commentDAO.findById(id);
//        res.put("comment", comment);
//        return res;
//    }
//
//
    //编辑页面
    @ResponseBody
    @RequestMapping("commentShow")
    public HashMap<String,Object> commentShow(int id,HttpServletRequest request) {
        HashMap<String,Object> res = new HashMap<String,Object>();
        Comment comment = commentDAO.findById(id);
        res.put("comment", comment);
        return res;
    }


    //评论编辑
    @ResponseBody
    @RequestMapping("commentEdit")
    public void commentEdit(Comment comment,HttpServletRequest request) {
        commentDAO.update(comment);
    }

    //评论删除
    @ResponseBody
    @RequestMapping("admin/commentDel")
    public void commentDel(int id,HttpServletRequest request) {
	    commentDAO.delete(id);
    }


}
