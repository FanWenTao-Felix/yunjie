import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/financial/rp/list',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}

export const getDetail = (internalOrderNo) => {
  return request({
    url: '/api/financial/rp/detail',
    method: 'get',
    params: {
      internalOrderNo
    }
  })
}