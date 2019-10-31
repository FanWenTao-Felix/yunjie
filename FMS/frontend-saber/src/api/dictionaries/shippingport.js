import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/dictionaries/shippingport/list',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}


export const getDetail = (id) => {
  return request({
    url: '/api/dictionaries/shippingport/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/dictionaries/shippingport/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/dictionaries/shippingport/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/dictionaries/shippingport/submit',
    method: 'post',
    data: row
  })
}

export const getDetailBySeaCode = (portCode) => {
  return request({
    url: '/api/dictionaries/shippingport/detail',
    method: 'get',
    params: {
      portCode
    }
  })
}


