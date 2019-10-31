import request from '@/router/axios';

export const getList = (current, size, params,begindate,enddate) => {
  return request({
    url: '/api/statistics/bushinesstatistics/airlist',
    method: 'get',
    params: {
      ...params,
      current,
      size,
      begindate,
      enddate
    }
  })
};

export const getListdetail = (current, size, params,num) => {
  return request({
    url: '/api/statistics/bushinesstatistics/getListdetail',
    method: 'get',
    params: {
      ...params,
      current,
      size,
      num
    }
  })
};

export const getDetail = (id) => {
  return request({
    url: '/api/statistics/bushinesstatistics/detail',
    method: 'get',
    params: {
      id
    }
  })
};

export const remove = (ids) => {
  return request({
    url: '/api/statistics/bushinesstatistics/remove',
    method: 'post',
    params: {
      ids,
    }
  })
};

export const add = (row) => {
  return request({
    url: '/api/statistics/bushinesstatistics/submit',
    method: 'post',
    data: row
  })
};

export const update = (row) => {
  return request({
    url: '/api/statistics/bushinesstatistics/submit',
    method: 'post',
    data: row
  })
};
export const airdetail = () => {
  return request({
    url: '/api/business/salesmanagement/airdetail',
    method: 'get',
  })
};

export const seadetail = () => {
  return request({
    url: '/api/business/salesmanagement/seadetail',
    method: 'get',
  })
};
