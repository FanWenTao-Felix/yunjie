import request from '@/router/axios';

export const getList = (current, size,internalOrderNo, params) => {
  return request({
    url: '/api/ocean/separatelading/list',
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
    url: '/api/ocean/separatelading/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/ocean/separatelading/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/ocean/separatelading/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/ocean/separatelading/submit',
    method: 'post',
    data: row
  })
}

