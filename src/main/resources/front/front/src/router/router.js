import VueRouter from "vue-router";

//引入组件
import Index from "../pages";
import Home from "../pages/home/home";
import Login from "../pages/login/login";
import Register from "../pages/register/register";
import Center from "../pages/center/center";
import Forum from "../pages/forum/list";
import ForumAdd from "../pages/forum/add";
import ForumDetail from "../pages/forum/detail";
import MyForumList from "../pages/forum/myForumList";

import ExamPaper from "../pages/exam/examPaper";
import Exam from "../pages/exam/exam";
import ExamList from "../pages/exam/examList";
import ExamRecord from "../pages/exam/examRecord";
import ExamResult from "../pages/exam/examResult";

import Storeup from "../pages/storeup/list";
import News from "../pages/news/news-list";
import NewsDetail from "../pages/news/news-detail";
import userList from "../pages/student/list";
import userDetail from "../pages/student/detail";
import userAdd from "../pages/student/add";
import teacherList from "../pages/teacher/list";
import teacherDetail from "../pages/teacher/detail";
import teacherAdd from "../pages/teacher/add";
import levelList from "../pages/level/list";
import levelDetail from "../pages/level/detail";
import levelAdd from "../pages/level/add";
import coursesortList from "../pages/coursesort/list";
import coursesortDetail from "../pages/coursesort/detail";
import coursesortAdd from "../pages/coursesort/add";
import courseList from "../pages/course/list";
import courseDetail from "../pages/course/detail";
import courseAdd from "../pages/course/add";

import aboutusList from "../pages/aboutus/list";
import aboutusDetail from "../pages/aboutus/detail";
import aboutusAdd from "../pages/aboutus/add";
import resourceList from "../pages/resource/list";
import resourceDetail from "../pages/resource/detail";
import resourceAdd from "../pages/resource/add";
import MySelection from "../pages/selection/mySelection";
// import Course from "../pages/study/course";
import StudyShare from "../pages/studyshare/list";
import StudyShareAdd from "../pages/studyshare/add";
import StudyShareDetail from "../pages/studyshare/detail";
import MyShareList from "../pages/studyshare/myShareList";
import Study from "../pages/course/study";
import MyPoints from "../pages/points/myPoints";
import KnowledgeHotword from "../pages/knowledge/hotword";
import KnowledgeMap from "../pages/knowledge/map";
import KnowledgeDetail from "../pages/knowledge/detail";

const originalPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch((err) => err);
};

//配置路由
export default new VueRouter({
  routes: [
    {
      path: "/",
      redirect: "/index/home",
    },
    {
      path: "/index",
      component: Index,
      children: [
        {
          path: "home",
          component: Home,
        },
        {
          path: "center",
          component: Center,
        },
        {
          path: "forum",
          component: Forum,
        },
        {
          path: "forumAdd",
          component: ForumAdd,
        },
        {
          path: "forumDetail",
          component: ForumDetail,
        },
        {
          path: "myForumList",
          component: MyForumList,
        },
        {
          path: "examPaper",
          component: ExamPaper,
        },
        {
          path: "examList",
          component: ExamList,
        },
        {
          path: "examRecord/:type",
          component: ExamRecord,
        },
        {
          path: "examResult",
          component: ExamResult,
        },
        {
          path: "storeup",
          component: Storeup,
        },
        {
          path: "news",
          component: News,
        },
        {
          path: "newsDetail",
          component: NewsDetail,
        },
        {
          path: "student",
          component: userList,
        },
        {
          path: "userDetail",
          component: userDetail,
        },
        {
          path: "userAdd",
          component: userAdd,
        },
        {
          path: "teacher",
          component: teacherList,
        },
        {
          path: "teacherDetail",
          component: teacherDetail,
        },
        {
          path: "teacherAdd",
          component: teacherAdd,
        },
        {
          path: "level",
          component: levelList,
        },
        {
          path: "levelDetail",
          component: levelDetail,
        },
        {
          path: "levelAdd",
          component: levelAdd,
        },
        {
          path: "coursesort",
          component: coursesortList,
        },
        {
          path: "coursesortDetail",
          component: coursesortDetail,
        },
        {
          path: "coursesortAdd",
          component: coursesortAdd,
        },
        {
          path: "course",
          component: courseList,
        },
        {
          path: "courseDetail",
          component: courseDetail,
        },
        {
          path: "courseAdd",
          component: courseAdd,
        },
        {
          path: "resource",
          component: resourceList,
        },
        {
          path: "resourceDetail",
          component: resourceDetail,
        },
        {
          path: "resourceAdd",
          component: resourceAdd,
        },
        {
          path: "aboutus",
          component: aboutusList,
        },
        {
          path: "aboutusDetail",
          component: aboutusDetail,
        },
        {
          path: "aboutusAdd",
          component: aboutusAdd,
        },
        {
          path: "mySelection",
          component: MySelection,
        },
        // {
        //   path:"course",
        //   component:Course,
        // },
        {
          path: "studyShare",
          component: StudyShare,
        },
        {
          path: "studyShareAdd",
          component: StudyShareAdd,
        },
        {
          path: "studyShareDetail",
          component: StudyShareDetail,
        },
        {
          path: "myShareList",
          component: MyShareList,
        },
        {
          path: 'study/:id',
          name: 'Study', 
          component: Study,
        },
        {
          path: "myPoints",
          component: MyPoints,
        },
        {
          path: "knowledgeHotword",
          component: KnowledgeHotword,
        },
        {
          path: "knowledgeMap",
          component: KnowledgeMap,
        },
        {
          path: "knowledgeDetail",
          component: KnowledgeDetail,
        }
      ],
    },
    {
      path: "/login",
      component: Login,
    },
    {
      path: "/register",
      component: Register,
    },
    {
      path: "/exam",
      component: Exam,
    },
  ],
});
