import axios from "axios";
import router from "@/router";
import Element from "element-ui"

const request = axios.create({
    baseURL:'http://localhost:8081',
    timeout: 5000,
    headers: {
        'Content-Type': "application/json; charset=utf-8"
    }
})

request.interceptors.request.use(config => {
    let curUser = JSON.parse(window.sessionStorage.getItem('CurUser'))
    if (curUser != null){
        config.headers['Authorization'] = curUser.token
    }
    return config
})

request.interceptors.response.use(
    response => {
        let res = response.data
        if (res.code === 200 || res.code === 400) {
            return response
        } else {
            Element.Message.error(!res.msg ? '系统异常' : res.msg)
            return Promise.reject(response.data.msg)
        }
    },
    error => {

        if (error.response.status == 401) {
            error.message = "校验失败"
            router.push("/Error")
        }
        if (error.response.status == 403) {
            error.message = "用户被禁用"
            router.push("/Limit")
        }

        return Promise.reject(error.message)
    }
)

export default request