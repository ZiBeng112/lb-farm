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
