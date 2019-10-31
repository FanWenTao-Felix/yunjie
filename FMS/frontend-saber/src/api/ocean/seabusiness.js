import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/ocean/seabusiness/list',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}



export const lists = (current, size,businessState, params) => {
  return request({
    url: '/api/ocean/seabusiness/list',
    method: 'get',
    params: {
      ...params,
      current,
      size,
      businessState
    }
  })
}

export const getLists = (current, size, params) => {
  return request({
    url: '/api/ocean/seabusiness/list',
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
    url: '/api/ocean/seabusiness/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/ocean/seabusiness/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/ocean/seabusiness/save',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/ocean/seabusiness/submit',
    method: 'post',
    data: row
  })
}

