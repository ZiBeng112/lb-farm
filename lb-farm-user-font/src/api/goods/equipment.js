import request from '@/utils/request'


export function treeCategory() {
    return request({
        url: '/equipment/categoryTree',
        method: 'get',
    })
}
export function listEquipment(params) {
    return request({
        url: '/equipment/list',
        method: 'get',
        params: params
    })
}
export function addEquipment(data) {
    return request({
        url: '/equipment/saveOrUpdate',
        method: 'post',
        data: data
    })
}
export function deleteEquipment(data) {
    return request({
        url: '/equipment/deleteByIds',
        method: 'delete',
        data: data
    })
}
export function batchUpdateStatusEquipment(data) {
    return request({
        url: '/equipment/updateStatusByIds',
        method: 'put',
        data: data
    })
}

