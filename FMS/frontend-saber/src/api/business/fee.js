import request from '@/router/axios';

export const getList = (current, size, internalOrderNo, params) => {
  return request({
    url: '/api/business/fee/list',
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
    url: '/api/business/fee/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/business/fee/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const seaSave = (row) => {
  return request({
    url: '/api/business/fee/seaSave',
    method: 'post',
    data: row
  })
}

export const seaUpdate = (row) => {
  return request({
    url: '/api/business/fee/seaUpdate',
    method: 'post',
    data: row
  })
}

export const add = (row) => {
  return request({
    url: '/api/business/fee/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/business/fee/submit',
    method: 'post',
    data: row
  })
}

export const createPayable = (ids) => {
  return request({
    url: '/api/business/fee/payable',
    method: 'post',
    params: {
      ids,
    }
  })
}


export const seaPayable = (ids) => {
  return request({
    url: '/api/business/fee/seaPayable',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const seaRemove = (ids) => {
  return request({
    url: '/api/business/fee/seaRemove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const getBillFee = (params) => {
  return request({
    url: '/api/business/fee/billfee',
    method: 'get',
    params: {
      ...params,
    }
  })
}