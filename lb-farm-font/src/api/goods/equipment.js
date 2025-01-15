import request from '@/utils/request'


export function treeCategory() {
    return request({
        url: '/equipment/categoryTree',
        method: 'get',
    })
}
