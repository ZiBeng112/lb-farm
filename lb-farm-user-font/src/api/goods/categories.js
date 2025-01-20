import request from '@/utils/request'


export function listCategory(param) {
    return request({
        url: '/goods/category/list',
        method: 'get',
        params: param
    })
}

export function addCategory(data) {
    return request({
        url: '/goods/category/add',
        method: 'post',
        data: data
    })
}

export function deleteCategory(param) {
    return request({
        url: '/goods/category/delete',
        method: 'delete',
        params: param
    })
}

export function updateCategory(data) {
    return request({
        url: '/goods/category/update',
        method: 'put',
        data: data
    })
}

export function detailCategory(query) {
    return request({
        url: '/goods/category/get',
        method: 'get',
        params: query
    })
}




