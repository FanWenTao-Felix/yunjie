import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/business/salesmanagement/list',
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
    url: '/api/business/salesmanagement/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/business/salesmanagement/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/business/salesmanagement/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/business/salesmanagement/submit',
    method: 'post',
    data: row
  })
}

export const airdetail = () => {
  return request({
    url: '/api/business/salesmanagement/airdetail',
    method: 'get',
  })
}

export const seadetail = () => {
  return request({
    url: '/api/business/salesmanagement/seadetail',
    method: 'get',
  })
}

