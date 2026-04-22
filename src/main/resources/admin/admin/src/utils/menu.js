const menu = {
  list() {
    return [
      {
        //<-------------------- 管理员 ------------------->
        backMenu: [
          {
            child: [
              {
                appFrontIcon: "cuIcon-paint",
                buttons: ["新增", "查看", "修改", "删除"],
                menu: "学生",
                menuJump: "列表",
                tableName: "student",
              },
            ],
            menu: "学生管理",
          },
          {
            child: [
              {
                appFrontIcon: "cuIcon-time",
                buttons: ["新增", "查看", "修改", "删除"],
                menu: "教师",
                menuJump: "列表",
                tableName: "teacher",
              },
            ],
            menu: "教师管理",
          },
          {
            child: [
              {
                appFrontIcon: "cuIcon-paint",
                buttons: ["新增", "查看", "修改", "删除"],
                menu: "等级",
                menuJump: "列表",
                tableName: "level",
              },
            ],
            menu: "等级管理",
          },
          {
            child: [
              {
                appFrontIcon: "cuIcon-album",
                buttons: ["新增", "查看", "修改", "删除"],
                menu: "课程类别",
                menuJump: "列表",
                tableName: "coursesort",
              },
            ],
            menu: "课程类别管理",
          },
          {
            child: [
              {
                appFrontIcon: "cuIcon-cardboard",
                buttons: ["新增", "查看", "修改", "删除", "查看评论"],
                menu: "课程信息",
                menuJump: "列表",
                tableName: "course",
              },
              {
                appFrontIcon: "cuIcon-cardboard",
                buttons: ["新增", "查看", "修改", "删除"],
                menu: "课后习题",
                tableName: "homework",
              },
            ],
            menu: "课程信息管理",
          },
          {
            child: [
              {
                appFrontIcon: "cuIcon-album",
                buttons: ["新增", "查看", "修改", "删除"],
                menu: "资料类别",
                menuJump: "列表",
                tableName: "resourcesort",
              },
            ],
            menu: "资料类别管理",
          },
          {
            child: [
              {
                appFrontIcon: "cuIcon-cardboard",
                buttons: ["新增", "查看", "修改", "删除", "查看评论"],
                menu: "资料信息",
                menuJump: "列表",
                tableName: "resource",
              },
            ],
            menu: "资料信息管理",
          },
          {
            child: [
              {
                appFrontIcon: "cuIcon-group",
                buttons: ["查看", "修改", "删除", "加精"],
                menu: "交流论坛",
                tableName: "forum",
              },
            ],
            menu: "交流论坛",
          },
          {
            child: [
              {
                appFrontIcon: "cuIcon-group",
                buttons: ["查看", "修改", "删除"],
                menu: "学习分享",
                tableName: "studyshare",
              },
            ],
            menu: "学习分享",
          },
          {
            child: [
              {
                appFrontIcon: "cuIcon-phone",
                buttons: ["查看", "修改"],
                menu: "试卷管理",
                tableName: "exampaper",
              },
            ],
            menu: "试卷管理",
          },
          {
            child: [
              {
                appFrontIcon: "cuIcon-time",
                buttons: ["查看", "删除"],
                menu: "考试记录",
                tableName: "examrecord",
              },
            ],
            menu: "考试管理",
          },
          {
            child: [
              {
                appFrontIcon: "cuIcon-time",
                buttons: ["审核"],
                menu: "选课申请",
                tableName: "selectionapplication",
              },
            ],
            menu: "选课申请管理",
          },
          {
            child: [
              {
                appFrontIcon: "cuIcon-news",
                buttons: ["新增", "查看", "修改", "删除"],
                menu: "AI问答记录",
                tableName: "chat",
              },
              {
                appFrontIcon: "cuIcon-pic",
                buttons: ["查看", "修改"],
                menu: "关于我们",
                tableName: "aboutus",
              },
              {
                appFrontIcon: "cuIcon-rank",
                buttons: ["查看", "修改"],
                menu: "系统简介",
                tableName: "systemintro",
              },
              {
                appFrontIcon: "cuIcon-wenzi",
                buttons: ["查看", "修改", "删除"],
                menu: "轮播图管理",
                tableName: "config",
              },
              {
                appFrontIcon: "cuIcon-news",
                buttons: ["新增", "查看", "修改", "删除"],
                menu: "公告资讯",
                tableName: "news",
              },
              {
                appFrontIcon: "cuIcon-gift",
                buttons: ["新增", "查看", "修改", "删除"],
                menu: "积分策略管理",
                tableName: "pointsstrategy",
              },
            ],
            menu: "系统管理",
          },
        ],
        frontMenu: [
          {
            child: [
              {
                appFrontIcon: "cuIcon-shop",
                buttons: ["查看", "查看评论"],
                menu: "课程信息列表",
                menuJump: "列表",
                tableName: "course",
              },
            ],
            menu: "课程信息模块",
          },
        ],
        hasBackLogin: "是",
        hasBackRegister: "否",
        hasFrontLogin: "否",
        hasFrontRegister: "否",
        roleName: "管理员",
        tableName: "users",
      },

      {
        backMenu: [],
        frontMenu: [
          {
            child: [
              {
                appFrontIcon: "cuIcon-shop",
                buttons: ["查看", "查看评论"],
                menu: "课程信息列表",
                menuJump: "列表",
                tableName: "course",
              },
            ],
            menu: "课程信息模块",
          },
        ],
        hasBackLogin: "否",
        hasBackRegister: "否",
        hasFrontLogin: "是",
        hasFrontRegister: "是",
        roleName: "学生",
        tableName: "student",
      },
      //<-------------------- 教师 ------------------->
      {
        backMenu: [
          {
            child: [
              {
                appFrontIcon: "cuIcon-cardboard",
                buttons: ["新增", "查看", "修改", "删除", "查看评论"],
                menu: "课程信息",
                menuJump: "列表",
                tableName: "course",
              },
            ],
            menu: "课程信息管理",
          },
          {
            child: [
              {
                appFrontIcon: "cuIcon-cardboard",
                buttons: ["新增", "查看", "修改", "删除", "查看评论"],
                menu: "资料信息",
                menuJump: "列表",
                tableName: "resource",
              },
            ],
            menu: "资料信息管理",
          },
          {
            child: [
              {
                appFrontIcon: "cuIcon-group",
                buttons: ["查看", "修改", "删除", "加精"],
                menu: "交流论坛",
                tableName: "forum",
              },
            ], 
            menu: "交流论坛",
          },
          {
            child: [
              {
                appFrontIcon: "cuIcon-phone",
                buttons: ["新增", "查看", "修改", "删除", "添加考题"],
                menu: "试卷管理",
                tableName: "exampaper",
              },
            ],
            menu: "试卷管理",
          },
          {
            child: [
              {
                appFrontIcon: "cuIcon-skin",
                buttons: ["新增", "查看", "修改", "删除"],
                menu: "试题管理",
                tableName: "examquestion",
              },
            ],
            menu: "试题管理",
          },
          {
            child: [
              {
                appFrontIcon: "cuIcon-time",
                buttons: ["查看"],
                menu: "考试记录",
                tableName: "examrecord",
              },
              {
                appFrontIcon: "cuIcon-time",
                buttons: ["查看", "统计"],
                menu: "错题本",
                tableName: "examrewrongquestion",
              },
            ],
            menu: "考试管理",
          },
        ],
        frontMenu: [
          {
            child: [
              {
                appFrontIcon: "cuIcon-shop",
                buttons: ["查看", "查看评论"],
                menu: "课程信息列表",
                menuJump: "列表",
                tableName: "course",
              },
            ],
            menu: "课程信息模块",
          },
        ],
        hasBackLogin: "是",
        hasBackRegister: "是",
        hasFrontLogin: "否",
        hasFrontRegister: "否",
        roleName: "教师",
        tableName: "teacher",
      },
    ];
  },
};
export default menu;
