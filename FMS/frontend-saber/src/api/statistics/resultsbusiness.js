import request from '@/router/axios';

export const getList = (current, size, params, begindate, enddate) => {
  return request({
    url: '/api/statistics/bushinesstatistics/resultsbusiness',
    method: 'get',
    params: {
      ...params,
      current,
      size,
      begindate,
      enddate
    }
  })
}

export const getDetail = (id) => {
  return request({
    url: '/api/statistics/bushinesstatistics/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/statistics/bushinesstatistics/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/statistics/bushinesstatistics/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/statistics/bushinesstatistics/submit',
    method: 'post',
    data: row
  })
}
