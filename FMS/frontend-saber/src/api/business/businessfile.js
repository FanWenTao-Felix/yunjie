import request from '@/router/axios';

export const getList = (current, size, internalOrderNo, params) => {
  return request({
    url: '/api/business/businessfile/list',
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
    url: '/api/business/businessfile/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/business/businessfile/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/business/businessfile/save',
    method: 'post',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/business/businessfile/update',
    method: 'post',
    data: row
  })
}