import request from '@/router/axios';

export const getList = (current, size, businessId,params) => {
  return request({
    url: '/api/ocean/seawhole/list',
    method: 'get',
    params: {
      ...params,
      current,
      size,
      businessId
    }
  })
}

export const getDetail = (id) => {
  return request({
    url: '/api/ocean/seawhole/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/ocean/seawhole/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/ocean/seawhole/save',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/ocean/seawhole/update',
    method: 'post',
    data: row
  })
}

