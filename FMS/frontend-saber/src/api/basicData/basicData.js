import request from '@/router/axios';

export const judgeUser = () => {
  return request({
    url: '/api/basicData/judgeUser',
    method: 'post',
  })
}

export const getAirport = (airCode) => {
  return request({
    url: '/api/basicData/airport',
    method: 'get',
    params: {
      airCode,
    }
  })
}

export const getSeaport = (portCode) => {
  return request({
    url: '/api/basicData/seaport',
    method: 'get',
    params: {
      portCode
    }
  })
}



export const getCurrency = (currencyCode) => {
  return request({
    url: '/api/basicData/currency',
    method: 'get',
    params: {
      currencyCode,
    }
  })
}
export const getMainOrderNoData = (mainOrderNo) => {
  return request({
    url: '/api/basicData/mainOrderNo',
    method: 'get',
    params: {
      mainOrderNo,
    }
  })
}
export const getCostType = (costCode) => {
  return request({
    url: '/api/basicData/costtype',
    method: 'get',
    params: {
      costCode,
    }
  })
}

export const getWorkNumRulesUser = () => {
  return request({
    url: '/api/basicData/user',
    method: 'get',
  })
}



export const getSeaWorkrules = () => {
  return request({
    url: '/api/basicData/seaworkrules',
    method: 'get',
  })
}
