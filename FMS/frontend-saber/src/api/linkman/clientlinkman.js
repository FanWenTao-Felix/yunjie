import request from '@/router/axios';

export const getList = (current, size, clientDataId, params) => {
  return request({
    url: '/api/linkman/clientlinkman/list',
    method: 'get',
    params: {
      ...params,
      current,
      size,
      clientDataId
    }
  })
}

export const getDetail = (id) => {
  return request({
    url: '/api/linkman/clientlinkman/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/linkman/clientlinkman/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/linkman/clientlinkman/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/linkman/clientlinkman/submit',
    method: 'post',
    data: row
  })
}