import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/reconciliationstatement/bill/list',
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
    url: '/api/reconciliationstatement/bill/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/reconciliationstatement/bill/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/reconciliationstatement/bill/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/reconciliationstatement/bill/submit',
    method: 'post',
    data: row
  })
}

export const createBillData = (bill, feeIds) => {
  return request({
    url: '/api/reconciliationstatement/bill/save',
    method: 'post',
    data: bill,
    params: {
      feeIds,
    },
  })
}

export const joinBill = (billId, feeIds) => {
  return request({
    url: '/api/reconciliationstatement/bill/join',
    method: 'post',
    params: {
      billId,
      feeIds
    }
  })
}

export const archiveBill = (ids) => {
  return request({
    url: '/api/reconciliationstatement/bill/archive',
    method: 'post',
    params: {
      ids,
    }
  })
}
export const settleBill = (row) => {
  return request({
    url: '/api/reconciliationstatement/bill/settle',
    method: 'post',
    data: row
  })
}


export const addToBill = (feeList, bill) => {
  return request({
    url: '/api/reconciliationstatement/bill/addToBill',
    method: 'post',
    data: feeList,
    params: {
      ...bill,
    }
  })
}