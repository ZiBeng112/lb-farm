import request from '@/utils/request'

export function getMyShopInfo() {
    return request({
        url: '/shop/myShop/getShopInfo',
        method: 'get',
    })
}

export function addShopInfo(data) {
    return request({
        url: '/shop/myShop/addShopInfo',
        method: 'post',
        data: data
    })
}
