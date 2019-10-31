import request from '@/router/axios';

export const getList = (current, size, billId, params) => {
  return request({
    url: '/api/reconciliationstatement/billdetail/list',
    method: 'get',
    params: {
      ...params,
      current,
      billId: billId,
      size,
    }
  })
}

export const getDetail = (id) => {
  return request({
    url: '/api/reconciliationstatement/billdetail/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/reconciliationstatement/billdetail/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/reconciliationstatement/billdetail/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/reconciliationstatement/billdetail/submit',
    method: 'post',
    data: row
  })
}