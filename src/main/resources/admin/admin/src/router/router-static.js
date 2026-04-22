import Vue from "vue";
//配置路由
import VueRouter from "vue-router";
Vue.use(VueRouter);
//1.创建组件
import Index from "@/views/index";
import Home from "@/views/home";
import Login from "@/views/login";
import NotFound from "@/views/404";
import UpdatePassword from "@/views/update-password";
import pay from "@/views/pay";
import register from "@/views/register";
import center from "@/views/center";

import news from "@/views/modules/news/list";
import aboutus from "@/views/modules/aboutus/list";
import coursesort from "@/views/modules/coursesort/list";
import student from "@/views/modules/student/list";
import teacher from "@/views/modules/teacher/list";
import discusscourse from "@/views/modules/discusscourse/list";
import course from "@/views/modules/course/list";
import forum from "@/views/modules/forum/list";
import studyshare from "@/views/modules/studyshare/list";
import systemintro from "@/views/modules/systemintro/list";
import level from "@/views/modules/level/list";
import config from "@/views/modules/config/list";
import resourcesort from "@/views/modules/resourcesort/list";
import resource from "@/views/modules/resource/list";
import discussresource from "@/views/modules/discussresource/list";
import selectionapplication from "@/views/modules/selectionapplication/list";
import homework from '@/views/modules/homework/list';
import homeworktopic from '@/views/modules/homeworktopic/list';
import chat from '@/views/modules/chat/list';

import exampaper from '@/views/modules/exampaper/list';
import exampapertopic from '@/views/modules/exampapertopic/list';
import examquestion from '@/views/modules/examquestion/list';
import examrecord from '@/views/modules/examrecord/list';
import examredetails from '@/views/modules/examredetails/list';
import examrewrongquestion from '@/views/modules/examrewrongquestion/list';
import examrewrongquestionStatistics from '@/views/modules/examrewrongquestion/statistics';
import pointsstrategy from '@/views/modules/pointsstrategy/list';
import knowledgemapManage from '@/views/modules/knowledgemap/manage';

//2.配置路由   注意：名字
const routes = [
  {
    path: "/index",
    name: "系统首页",
    component: Index,
    children: [
      {
        // 这里不设置值，是把main作为默认页面
        path: "/",
        name: "系统首页",
        component: Home,
        meta: { icon: "", title: "center" },
      },
      {
        path: "/updatePassword",
        name: "修改密码",
        component: UpdatePassword,
        meta: { icon: "", title: "updatePassword" },
      },
      {
        path: "/pay",
        name: "支付",
        component: pay,
        meta: { icon: "", title: "pay" },
      },
      {
        path: "/center",
        name: "个人信息",
        component: center,
        meta: { icon: "", title: "center" },
      },
      {
        path: "/news",
        name: "公告资讯",
        component: news,
      },
      {
        path: "/aboutus",
        name: "关于我们",
        component: aboutus,
      },

      {
        path: "/coursesort",
        name: "课程类别",
        component: coursesort,
      },
      {
        path: "/student",
        name: "学生",
        component: student,
      },
      {
        path: "/teacher",
        name: "教师",
        component: teacher,
      },
      {
        path: "/discusscourse",
        name: "课程信息评论",
        component: discusscourse,
      },
      {
        path: "/course",
        name: "课程信息",
        component: course,
      },
      {
        path: "/resourcesort",
        name: "资料类别",
        component: resourcesort,
      },
      {
        path: "/resource",
        name: "资料信息",
        component: resource,
      },
      {
        path: "/discussresource",
        name: "资料信息评论",
        component: discussresource,
      },
      {
        path: "/forum",
        name: "交流论坛",
        component: forum,
      },
      {
        path: "/studyshare",
        name:"学习分享",
        component: studyshare,
      },
      {
        path: "/systemintro",
        name: "系统简介",
        component: systemintro,
      },
      {
        path: "/level",
        name: "等级",
        component: level,
      },
      {
        path: "/config",
        name: "轮播图管理",
        component: config,
      },
      {
        path: "/selectionapplication",
        name: "选课申请",
        component: selectionapplication,
      },
      {
        path: '/homework',
        name: '课后作业',
        component: homework
      },
      {
        path: '/homeworktopic',
        name: '课后作业选题',
        component: homeworktopic
      },
      {
        path: '/chat',
        name: 'AI问答记录',
        component: chat
      },
      {
        path: '/exampaper',
        name: '试卷',
        component: exampaper
      },
      {
        path: '/exampapertopic',
        name: '试卷选题',
        component: exampapertopic
      },
      {
        path: '/examquestion',
        name: '试题表',
        component: examquestion
      },
      {
        path: '/examrecord',
        name: '考试记录表',
        component: examrecord
      },
      {
        path: '/examredetails',
        name: '答题详情表',
        component: examredetails
      },
      {
        path: '/examrewrongquestion',
        name: '错题表',
        component: examrewrongquestion
      },
      {
        path: '/modules/examrewrongquestion/statistics',
        name: '错题统计',
        component: examrewrongquestionStatistics
      },
      {
        path: '/pointsstrategy',
        name: '积分策略管理',
        component: pointsstrategy
      },
      {
        path: '/knowledgemap-manage',
        name: '知识地图维护',
        component: knowledgemapManage
      }
    ],
  },
  {
    path: "/login",
    name: "login",
    component: Login,
    meta: { icon: "", title: "login" },
  },
  {
    path: "/register",
    name: "register",
    component: register,
    meta: { icon: "", title: "register" },
  },
  {
    path: "/",
    name: "系统首页",
    redirect: "/index",
  } /*默认跳转路由*/,
  {
    path: "*",
    component: NotFound,
  },
];
//3.实例化VueRouter  注意：名字
const router = new VueRouter({
  mode: "hash",
  /*hash模式改为history*/
  routes, // （缩写）相当于 routes: routes
});
const originalPush = VueRouter.prototype.push;
//修改原型对象中的push方法
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch((err) => err);
};
export default router;
