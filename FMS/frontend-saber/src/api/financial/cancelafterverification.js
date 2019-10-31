import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/financial/cav/list',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}

export const batchStatus = (ids, status) => {
  return request({
    url: '/api/financial/cav/batch/status',
    method: 'post',
    params: {
      ids,
      status,
    }
  })
}

export const batchLockOrUnlock = (ids, lock) => {
  return request({
    url: '/api/financial/cav/batch/lockorunlock',
    method: 'post',
    params: {
      ids,
      lock,
    }
  })
}

export const modifySettledAmount = (id, settledAmount) => {
  return request({
    url: '/api/financial/cav/settledamount',
    method: 'post',
    params: {
      id,
      settledAmount,
    }
  })
}