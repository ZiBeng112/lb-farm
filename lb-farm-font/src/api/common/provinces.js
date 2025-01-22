import request from '@/utils/request'

export function listByParentId(params) {
    return request({
        url: '/common/province/list',
        method: 'get',
        params: params
    })
}
