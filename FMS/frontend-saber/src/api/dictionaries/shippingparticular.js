
import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/dictionaries/shippingparticular/list',
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
    url: '/api/dictionaries/shippingparticular/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/dictionaries/shippingparticular/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/dictionaries/shippingparticular/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/dictionaries/shippingparticular/submit',
    method: 'post',
    data: row
  })
}

export const getRelativeList = (current, size, lineId, params) => {
  //alert(lineId);
  return request({
    url: '/api/dictionaries/shippingparticular/list',
    method: 'get',
    params: {
      ...params,
      current,
      size,
      lineId
    }
  })
}



