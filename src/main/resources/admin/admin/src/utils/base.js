const base = {
    get() {
        return {
            url : "http://localhost:8080/onlinecourseteachsys/",
            name: "onlinecourseteachsys",
            // 退出到首页链接
            indexUrl: 'http://localhost:8080/onlinecourseteachsys/front/dist/index.html'
        };
    },
    getProjectName(){
        return {
            projectName:"在线课程教学系统"
        } 
    }
}
export default base
