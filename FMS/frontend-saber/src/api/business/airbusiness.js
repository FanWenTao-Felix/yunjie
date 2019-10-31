import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/business/airbusiness/list',
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
    url: '/api/business/airbusiness/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/business/airbusiness/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/business/airbusiness/save',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/business/airbusiness/update',
    method: 'post',
    data: row
  })
}

export const getMainOrderDetail = (mainOrderNo) => {
  return request({
    url: '/api/business/airbusiness/detail',
    method: 'get',
    params: {
      mainOrderNo,
      isMain: true,
    }
  })
}

export const getSubList = (current, size, mainOrderNo, isMain, params) => {
  return request({
    url: '/api/business/airbusiness/list',
    method: 'get',
    params: {
      ...params,
      current,
      size,
      mainOrderNo,
      isMain,
    }
  })
}

export const verifyAllFee = (internalOrderNo) => {
  return request({
    url: '/api/business/airbusiness/verifyAllFee',
    method: 'post',
    params: {
      internalOrderNo,
    }
  })
}

// export const getStart = () => {
//   return request({
//     url: '/api/business/airbusiness/getStart',
//     method: 'get',
//   })
// }
