import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/dictionaries/air/list',
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
    url: '/api/dictionaries/air/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/dictionaries/air/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/dictionaries/air/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/dictionaries/air/submit',
    method: 'post',
    data: row
  })
}

export const download = (ids) => {
  return request({
    url: '/api/dictionaries/air/downloadfile',
    method: 'post',
    params: {
      ids
    }
  })
}

export const getDetailByAirCode = (airCode) => {
  return request({
    url: '/api/dictionaries/air/detail',
    method: 'get',
    params: {
      airCode
    }
  })
}