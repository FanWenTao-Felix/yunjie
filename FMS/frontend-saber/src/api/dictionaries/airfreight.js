import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/dictionaries/airfreight/list',
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
    url: '/api/dictionaries/airfreight/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/dictionaries/airfreight/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/dictionaries/airfreight/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/dictionaries/airfreight/submit',
    method: 'post',
    data: row
  })
}





export const getRelativeList = (current, size, freigthInfoId, params) => {
  return request({
    url: '/api/dictionaries/airfreight/list',
    method: 'get',
    params: {
      ...params,
      current,
      size,
      freigthInfoId
    }
  })
}



