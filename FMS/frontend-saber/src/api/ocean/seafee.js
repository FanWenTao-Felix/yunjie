

import request from '@/router/axios';

export const getList = (current, size,internalOrderNo, params) => {
  return request({
    url: '/api/ocean/seafee/list',
    method: 'get',
    params: {
      ...params,
      current,
      size,
      internalOrderNo
    }
  })
}

export const getDetail = (id) => {
  return request({
    url: '/api/ocean/seafee/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/ocean/seafee/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/ocean/seafee/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/ocean/seafee/submit',
    method: 'post',
    data: row
  })
}


export const createPayable = (ids) => {
  return request({
    url: '/api/ocean/seafee/payable',
    method: 'post',
    params: {
      ids,
    }
  })
}
